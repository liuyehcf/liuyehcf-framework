package com.github.liuyehcf.framework.rule.engine.runtime.delegate.interceptor;

import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.RuleErrorCode;
import com.github.liuyehcf.framework.rule.engine.RuleException;
import com.github.liuyehcf.framework.rule.engine.model.Element;
import com.github.liuyehcf.framework.rule.engine.model.ElementType;
import com.github.liuyehcf.framework.rule.engine.model.Executable;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.rule.engine.model.listener.ListenerScope;
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
import java.util.Objects;
import java.util.Set;

/**
 * @author hechenfeng
 * @date 2019/4/27
 */
public class ReflectiveDelegateInvocation implements DelegateInvocation {

    // result and cause only for listener
    private final Object result;
    private final Throwable cause;

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

    public ReflectiveDelegateInvocation(Object result,
                                        Throwable cause,
                                        Executable executable,
                                        Delegate delegate,
                                        OperationContext operationContext,
                                        ExecutableContext<? extends Element> executableContext,
                                        List<Factory<DelegateInterceptor>> factories) {
        Assert.assertNotNull(executable);
        Assert.assertNotNull(delegate);
        Assert.assertNotNull(operationContext);
        Assert.assertNotNull(executableContext);
        Assert.assertNotNull(factories);

        this.result = result;
        this.cause = cause;
        this.executable = executable;
        this.delegate = delegate;
        this.delegateFieldNames = ReflectionUtils.getAllDelegateFieldNames(this.delegate);
        this.argumentNames = new String[this.delegateFieldNames.size()];
        this.argumentValues = new Object[this.delegateFieldNames.size()];
        this.operationContext = operationContext;
        this.executableContext = executableContext;
        this.chains = Lists.newArrayList();

        for (Factory<DelegateInterceptor> factory : factories) {
            DelegateInterceptor delegateInterceptor = factory.create();
            if (delegateInterceptor.matches(executable.getName())) {
                chains.add(delegateInterceptor);
            }
        }
    }

    @Override
    public final DelegateResult proceed() throws Throwable {
        long startNanos = System.nanoTime();
        Throwable cause = null;
        DelegateResult delegateResult = null;
        try {
            if (stackCnt++ == 0) {
                injectDelegateFields(executable, delegate);
            }

            if (index < chains.size()) {
                DelegateInterceptor delegateInterceptor = chains.get(index++);
                delegateResult = delegateInterceptor.invoke(this);
                return delegateResult;
            } else {
                delegateResult = doInvoke();
                return delegateResult;
            }
        } catch (Throwable e) {
            cause = e;
            delegateResult = new DefaultDelegateResult(false, null, null);
            throw e;
        } finally {
            if (--stackCnt == 0) {
                recordTrace(delegateResult, cause, startNanos);
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

    private DelegateResult doInvoke() throws Throwable {
        if (delegate instanceof ActionDelegate) {
            ((ActionDelegate) delegate).onAction((ActionContext) executableContext);

            if (executableContext.isAsync()) {
                return new DefaultDelegateResult(true, null, executableContext.getDelegatePromise());
            } else {
                return new DefaultDelegateResult(false, null, null);
            }
        } else if (delegate instanceof ConditionDelegate) {
            boolean result = ((ConditionDelegate) delegate).onCondition((ConditionContext) executableContext);

            if (executableContext.isAsync()) {
                return new DefaultDelegateResult(true, null, executableContext.getDelegatePromise());
            } else {
                return new DefaultDelegateResult(false, result, null);
            }

        } else if (delegate instanceof ListenerDelegate) {
            Listener listener = (Listener) executable;
            if (ListenerEvent.before.equals(listener.getEvent())) {
                ((ListenerDelegate) delegate).onBefore((ListenerContext) executableContext);
            } else if (ListenerEvent.success.equals(listener.getEvent())) {
                ((ListenerDelegate) delegate).onSuccess((ListenerContext) executableContext, result);
            } else if (ListenerEvent.failure.equals(listener.getEvent())) {
                ((ListenerDelegate) delegate).onFailure((ListenerContext) executableContext, cause);
            }

            if (executableContext.isAsync()) {
                return new DefaultDelegateResult(true, null, executableContext.getDelegatePromise());
            } else {
                return new DefaultDelegateResult(false, null, null);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private void recordTrace(DelegateResult delegateResult, Throwable cause, long startNanos) {
        List<PropertyUpdate> propertyUpdates = executableContext.getPropertyUpdates();
        Map<String, Attribute> attributes = executableContext.getLocalAttributes();

        List<Argument> arguments = Lists.newArrayList();

        for (int i = 0; i < argumentNames.length; i++) {
            arguments.add(new DefaultArgument(argumentNames[i], argumentValues[i]));
        }

        Object executableResult;

        if (delegateResult.isAsync()) {
            executableResult = null;
        } else {
            executableResult = delegateResult.getResult();
        }

        DefaultTrace trace = new DefaultTrace(
                executableContext.getExecutionId(),
                ((Element) executable).getId(),
                ((Element) executable).getType(),
                executable.getName(),
                arguments,
                executableResult,
                propertyUpdates,
                attributes,
                cause,
                startNanos,
                System.nanoTime()
        );

        if (delegateResult.isAsync()) {
            delegateResult.getDelegatePromise().addListener(promise -> {
                if (promise.isSuccess()) {
                    trace.setResult(promise.get());
                } else if (promise.isFailure()) {
                    trace.setCause(promise.cause());
                }
                trace.setEndNanos(System.nanoTime());
            });
        }

        if (executable instanceof Listener
                && Objects.equals(ListenerScope.GLOBAL, ((Listener) executable).getScope())) {
            operationContext.addTraceToExecutionInstance(trace);
        } else {
            operationContext.addTraceToExecutionLink(trace);
        }
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

        return PlaceHolderUtils.parsePlaceHolder(executableContext.getEnv(), (String) originValue);
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
