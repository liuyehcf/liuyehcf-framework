package com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Executable;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ActionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ConditionDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.Delegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.ListenerDelegate;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ActionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ConditionContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ExecutableContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.context.ListenerContext;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.factory.Factory;
import com.github.liuyehcf.framework.rule.engine.runtime.delegate.field.DefaultDelegateField;
import com.github.liuyehcf.framework.rule.engine.runtime.operation.context.OperationContext;
import com.github.liuyehcf.framework.rule.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.rule.engine.util.PlaceHolderUtils;
import com.github.liuyehcf.framework.rule.engine.util.ReflectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class ReflectiveDelegateInvocation implements DelegateInvocation {

    private final Executable executable;
    private final Delegate delegate;
    private final Set<String> delegateFieldNames;
    private final String[] argumentNames;
    private final Object[] argumentValues;
    private final OperationContext operationContext;
    private final ExecutableContext<? extends Element> executableContext;
    private final List<DelegateInterceptor> chains;

    private int index = 0;
    private int stackCnt = 0;

    public ReflectiveDelegateInvocation(Executable executable,
                                        Delegate delegate,
                                        OperationContext operationContext,
                                        ExecutableContext<? extends Element> executableContext,
                                        List<Factory<DelegateInterceptor>> factories) {
        Assert.assertNotNull(executable);
        Assert.assertNotNull(delegate);
        Assert.assertNotNull(operationContext);
        Assert.assertNotNull(executableContext);
        Assert.assertNotNull(factories);

        this.executable = executable;
        this.delegate = delegate;
        this.delegateFieldNames = ReflectionUtils.getAllDelegateFieldNames(this.delegate);
        this.argumentNames = new String[this.delegateFieldNames.size()];
        this.argumentValues = new Object[this.delegateFieldNames.size()];
        this.operationContext = operationContext;
        this.executableContext = executableContext;
        this.chains = Lists.newArrayList();

        for (Factory<DelegateInterceptor> factory : factories) {
            chains.add(factory.create());
        }
    }

    @Override
    public final Object proceed() throws Throwable {
        long startNanos = System.nanoTime();
        Throwable cause = null;
        Object result = null;
        try {
            if (stackCnt++ == 0) {
                injectDelegateFields(executable, delegate);
            }

            if (index < chains.size()) {
                DelegateInterceptor delegateInterceptor = chains.get(index++);
                return delegateInterceptor.invoke(this);
            } else {
                result = doInvoke();
                return result;
            }
        } catch (Throwable e) {
            cause = e;
            throw e;
        } finally {
            if (--stackCnt == 0) {
                recordTrace(result, cause, startNanos);
            }
        }
    }

    @Override
    public final Delegate getDelegate() {
        return delegate;
    }

    @Override
    public final ExecutableContext<? extends Element> getExecutableContext() {
        return executableContext;
    }

    @Override
    public final ElementType getType() {
        return ((Element) executable).getType();
    }

    @Override
    public final String[] getArgumentNames() {
        return this.argumentNames;
    }

    @Override
    public final Object[] getArgumentValues() {
        return this.argumentValues;
    }

    private Object doInvoke() throws Throwable {
        if (delegate instanceof ActionDelegate) {
            ((ActionDelegate) delegate).onAction((ActionContext) executableContext);
            return null;
        } else if (delegate instanceof ConditionDelegate) {
            return ((ConditionDelegate) delegate).onCondition((ConditionContext) executableContext);
        } else if (delegate instanceof ListenerDelegate) {
            ((ListenerDelegate) delegate).onListener((ListenerContext) executableContext);
            return null;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void recordTrace(Object result, Throwable cause, long startNanos) {
        List<PropertyUpdate> propertyUpdates = executableContext.getPropertyUpdates();
        Map<String, Attribute> attributes = executableContext.getLocalAttributes();

        List<Argument> arguments = Lists.newArrayList();

        for (int i = 0; i < argumentNames.length; i++) {
            arguments.add(new DefaultArgument(argumentNames[i], argumentValues[i]));
        }

        operationContext.addTrace(
                new DefaultTrace(
                        executableContext.getExecutionId(),
                        ((Element) executable).getId(),
                        ((Element) executable).getType(),
                        executable.getName(),
                        arguments,
                        result,
                        propertyUpdates,
                        attributes,
                        cause,
                        startNanos,
                        System.nanoTime()
                )
        );
    }

    private void injectDelegateFields(Executable executable, Delegate delegate) {
        String[] argumentNames = executable.getArgumentNames();
        Object[] argumentValues = executable.getArgumentValues();

        int actualIndex = 0;
        for (int i = 0; i < argumentNames.length; i++) {
            String argumentName = argumentNames[i];
            Object argumentValue = argumentValues[i];

            // ignore unexpected param
            if (!delegateFieldNames.contains(argumentName)) {
                continue;
            }

            argumentValue = parsePlaceHolderIfNecessary(argumentValue);

            this.argumentNames[actualIndex] = argumentName;
            this.argumentValues[actualIndex] = argumentValue;
            actualIndex++;

            injectDelegateField(delegate, argumentName, argumentValue);
        }

        injectMissingDelegateFields(delegate);
    }

    private Object parsePlaceHolderIfNecessary(Object originValue) {
        if (!(originValue instanceof String)) {
            return originValue;
        }

        return PlaceHolderUtils.parsePlaceHolder(operationContext.getEnv(), (String) originValue);
    }

    private void injectMissingDelegateFields(Delegate delegate) {
        Set<String> missingNames = Sets.newHashSet(this.delegateFieldNames);

        int index = 0;
        while (index < argumentNames.length) {
            String argumentName = argumentNames[index];
            if (argumentName != null) {
                missingNames.remove(argumentName);
                index++;
            } else {
                break;
            }
        }

        if (!missingNames.isEmpty()) {
            for (String missingName : missingNames) {
                injectDelegateField(delegate, missingName, null);
                Assert.assertNull(this.argumentNames[index]);
                this.argumentNames[index++] = missingName;
            }
        }
    }

    private void injectDelegateField(Delegate delegate, String fieldName, Object fieldValue) {
        if (delegate instanceof ListenerDelegate
                && Listener.ARGUMENT_NAME_EVENT.equals(fieldName)) {
            return;
        }

        Method setMethod = ReflectionUtils.getDelegateFieldSetMethod(delegate, fieldName);

        if (setMethod != null) {
            try {
                setMethod.invoke(delegate, new DefaultDelegateField(fieldValue));
                return;
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuleException(RuleErrorCode.DELEGATE_FIELD, e);
            }
        }

        Field field = ReflectionUtils.getDelegateField(delegate, fieldName);
        try {
            field.set(delegate, new DefaultDelegateField(fieldValue));
        } catch (IllegalAccessException e) {
            throw new RuleException(RuleErrorCode.DELEGATE_FIELD, e);
        }
    }
}
