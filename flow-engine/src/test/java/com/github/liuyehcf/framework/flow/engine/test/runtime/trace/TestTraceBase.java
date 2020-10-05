package com.github.liuyehcf.framework.flow.engine.test.runtime.trace;

import com.github.liuyehcf.framework.common.tools.time.TimeUtils;
import com.github.liuyehcf.framework.flow.engine.FlowErrorCode;
import com.github.liuyehcf.framework.flow.engine.FlowException;
import com.github.liuyehcf.framework.flow.engine.model.ElementType;
import com.github.liuyehcf.framework.flow.engine.model.listener.ListenerEvent;
import com.github.liuyehcf.framework.flow.engine.runtime.statistics.*;
import com.github.liuyehcf.framework.flow.engine.test.runtime.TestRuntimeBase;
import org.junit.Assert;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/4/29
 */
@SuppressWarnings("all")
public class TestTraceBase extends TestRuntimeBase {

    private static final long MAX_TIMESTAMP_OFFSET = 2000;
    private static final long MAX_USETIME_NANO = 2000 * 1000 * 1000;

    protected void assertStart(Trace trace) {
        Assert.assertEquals(ElementType.START, trace.getType());
        Assert.assertNull(trace.getName());
        Assert.assertNull(trace.getArguments());
        Assert.assertNull(trace.getResult());
        Assert.assertNull(trace.getPropertyUpdates());
        Assert.assertNull(trace.getAttributes());
        Assert.assertEquals(trace.getEndTimestamp(), trace.getStartTimestamp());
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertEquals(0, trace.getUseTimeNano());
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertSetPropertyAction(Trace trace, String name, Object value, PropertyUpdateType type, Object oldValue) {
        Argument argument;
        PropertyUpdate propertyUpdate;

        Assert.assertEquals(ElementType.ACTION, trace.getType());
        Assert.assertEquals("setPropertyAction", trace.getName());
        Assert.assertEquals(2, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("value", argument.getName());
        Assert.assertEquals(value, argument.getValue());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(1, trace.getPropertyUpdates().size());
        propertyUpdate = trace.getPropertyUpdates().get(0);
        Assert.assertEquals(type, propertyUpdate.getType());
        Assert.assertEquals(name, propertyUpdate.getName());
        Assert.assertEquals(value, propertyUpdate.getNewValue());
        Assert.assertEquals(oldValue, propertyUpdate.getOldValue());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertGetPropertyAction(Trace trace, String name, Object expectedValue) {
        Argument argument;

        Assert.assertEquals(ElementType.ACTION, trace.getType());
        Assert.assertEquals("getPropertyAction", trace.getName());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        if (trace.getArguments().size() == 2) {
            argument = trace.getArguments().get(1);
            Assert.assertEquals("expectedValue", argument.getName());
            Assert.assertEquals(expectedValue, argument.getValue());
        }
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertPauseAction(Trace trace, long timeout, boolean isCancel, boolean isSuccess, Class<? extends Throwable> errorClass) {
        Argument argument;

        Assert.assertEquals(ElementType.ACTION, trace.getType());
        Assert.assertEquals("pauseAction", trace.getName());
        Assert.assertEquals(4, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("pause", argument.getName());
        Assert.assertEquals(timeout, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("isCancel", argument.getName());
        Assert.assertEquals(isCancel, argument.getValue());
        argument = trace.getArguments().get(2);
        Assert.assertEquals("isSuccess", argument.getName());
        Assert.assertEquals(isSuccess, argument.getValue());
        argument = trace.getArguments().get(3);
        Assert.assertEquals("cause", argument.getName());
        if (errorClass != null) {
            Assert.assertEquals(errorClass, argument.getValue().getClass());
        }

        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertPrintAction(Trace trace, String content) {
        Argument argument;

        Assert.assertEquals(ElementType.ACTION, trace.getType());
        Assert.assertEquals("printAction", trace.getName());
        Assert.assertEquals(1, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("content", argument.getName());
        Assert.assertTrue(Pattern.matches(content, (String) argument.getValue()));
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertSetAttrAction(Trace trace, String name, Object value) {
        Argument argument;
        Attribute attr;

        Assert.assertEquals(ElementType.ACTION, trace.getType());
        Assert.assertEquals("setTraceAttrAction", trace.getName());
        Assert.assertEquals(2, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("value", argument.getName());
        Assert.assertEquals(value, argument.getValue());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(1, trace.getAttributes().size());
        attr = trace.getAttributes().get(name);
        Assert.assertEquals(name, attr.getName());
        Assert.assertEquals(value, attr.getValue());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertThrowLinkTerminateAction(Trace trace) {
        Assert.assertEquals(ElementType.ACTION, trace.getType());
        Assert.assertEquals("throwLinkTerminateAction", trace.getName());
        Assert.assertEquals(0, trace.getArguments().size());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        if (trace.getCause() == null) {
            sleep(1);
        }
        Assert.assertNotNull(trace.getCause());
    }

    protected void assertSetPropertyCondition(Trace trace, String name, Object value, boolean output, PropertyUpdateType type, Object oldValue) {
        Argument argument;
        PropertyUpdate propertyUpdate;

        Assert.assertEquals(ElementType.CONDITION, trace.getType());
        Assert.assertEquals("setPropertyCondition", trace.getName());
        Assert.assertEquals(3, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("value", argument.getName());
        Assert.assertEquals(value, argument.getValue());
        argument = trace.getArguments().get(2);
        Assert.assertEquals("output", argument.getName());
        Assert.assertEquals(output, argument.getValue());
        Assert.assertEquals(output, trace.getResult());
        Assert.assertEquals(1, trace.getPropertyUpdates().size());
        propertyUpdate = trace.getPropertyUpdates().get(0);
        Assert.assertEquals(type, propertyUpdate.getType());
        Assert.assertEquals(name, propertyUpdate.getName());
        Assert.assertEquals(value, propertyUpdate.getNewValue());
        Assert.assertEquals(oldValue, propertyUpdate.getOldValue());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertPauseCondition(Trace trace, long timeout, boolean isCancel, boolean isSuccess, Class<? extends Throwable> errorClass, boolean output) {
        Argument argument;

        Assert.assertEquals(ElementType.CONDITION, trace.getType());
        Assert.assertEquals("pauseCondition", trace.getName());
        Assert.assertEquals(5, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("pause", argument.getName());
        Assert.assertEquals(timeout, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("isCancel", argument.getName());
        Assert.assertEquals(isCancel, argument.getValue());
        argument = trace.getArguments().get(2);
        Assert.assertEquals("isSuccess", argument.getName());
        Assert.assertEquals(isSuccess, argument.getValue());
        argument = trace.getArguments().get(3);
        Assert.assertEquals("cause", argument.getName());
        if (errorClass != null) {
            Assert.assertEquals(errorClass, argument.getValue().getClass());
        }
        argument = trace.getArguments().get(4);
        Assert.assertEquals("output", argument.getName());
        Assert.assertEquals(output, argument.getValue());

        Assert.assertEquals(output, trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertPrintCondition(Trace trace, String content, Boolean output) {
        Argument argument;

        Assert.assertEquals(ElementType.CONDITION, trace.getType());
        Assert.assertEquals("printCondition", trace.getName());
        Assert.assertEquals(2, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("content", argument.getName());
        Assert.assertTrue(Pattern.matches(content, (String) argument.getValue()));
        argument = trace.getArguments().get(1);
        Assert.assertEquals("output", argument.getName());
        if (output != null) {
            Assert.assertEquals(output, argument.getValue());

            if (trace.getCause() == null) {
                if (trace.getResult() == null) {
                    sleep(1);
                }
                if (trace.getCause() == null) {
                    Assert.assertEquals(output, trace.getResult());
                }
            }
        }
        if (trace.getCause() == null) {
            if (trace.getResult() == null) {
                sleep(1);
            }
            if (trace.getCause() == null) {
                Assert.assertNotNull(trace.getResult());
            }
        }
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertSetAttrCondition(Trace trace, String name, Object value, boolean output) {
        Argument argument;
        Attribute attr;

        Assert.assertEquals(ElementType.CONDITION, trace.getType());
        Assert.assertEquals("setTraceAttrCondition", trace.getName());
        Assert.assertEquals(3, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("value", argument.getName());
        Assert.assertEquals(value, argument.getValue());
        argument = trace.getArguments().get(2);
        Assert.assertEquals("output", argument.getName());
        Assert.assertEquals(output, argument.getValue());
        Assert.assertEquals(output, trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(1, trace.getAttributes().size());
        attr = trace.getAttributes().get(name);
        Assert.assertEquals(name, attr.getName());
        Assert.assertEquals(value, attr.getValue());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertThrowLinkTerminateCondition(Trace trace) {
        Assert.assertEquals(ElementType.CONDITION, trace.getType());
        Assert.assertEquals("throwLinkTerminateCondition", trace.getName());
        Assert.assertEquals(0, trace.getArguments().size());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        if (trace.getCause() == null) {
            sleep(1);
        }
        Assert.assertNotNull(trace.getCause());
    }

    protected void assertSleepCondition(Trace trace, long timeout, boolean output) {
        Argument argument;
        Attribute attr;

        Assert.assertEquals(ElementType.CONDITION, trace.getType());
        Assert.assertEquals("sleepCondition", trace.getName());
        Assert.assertEquals(2, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("timeout", argument.getName());
        Assert.assertEquals(timeout, argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("output", argument.getName());
        Assert.assertEquals(output, argument.getValue());
        Assert.assertEquals(output, trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertSetPropertyListener(Trace trace, String name, Object value, ListenerEvent event, PropertyUpdateType type, Object oldValue) {
        Argument argument;
        PropertyUpdate propertyUpdate;

        Assert.assertEquals(ElementType.LISTENER, trace.getType());
        Assert.assertEquals("setPropertyListener", trace.getName());
        Assert.assertEquals(3, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("event", argument.getName());
        Assert.assertEquals(event.name(), argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        argument = trace.getArguments().get(2);
        Assert.assertEquals("value", argument.getName());
        Assert.assertEquals(value, argument.getValue());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(1, trace.getPropertyUpdates().size());
        propertyUpdate = trace.getPropertyUpdates().get(0);
        Assert.assertEquals(type, propertyUpdate.getType());
        Assert.assertEquals(name, propertyUpdate.getName());
        Assert.assertEquals(value, propertyUpdate.getNewValue());
        Assert.assertEquals(oldValue, propertyUpdate.getOldValue());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertGetPropertyListener(Trace trace, String name, Object expectedValue, ListenerEvent event) {
        Argument argument;

        Assert.assertEquals(ElementType.LISTENER, trace.getType());
        Assert.assertEquals("getPropertyListener", trace.getName());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("event", argument.getName());
        Assert.assertEquals(event.name(), argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        if (trace.getArguments().size() == 3) {
            argument = trace.getArguments().get(2);
            Assert.assertEquals("expectedValue", argument.getName());
            Assert.assertEquals(expectedValue, argument.getValue());
        }
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertPrintListener(Trace trace, String content, ListenerEvent event) {
        Argument argument;

        Assert.assertEquals(ElementType.LISTENER, trace.getType());
        Assert.assertEquals("printListener", trace.getName());
        Assert.assertEquals(3, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("event", argument.getName());
        Assert.assertEquals(event.name(), argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("content", argument.getName());
        Assert.assertTrue(Pattern.matches(content, (String) argument.getValue()));
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertThrowLinkTerminateListener(Trace trace, ListenerEvent event) {
        Argument argument;

        Assert.assertEquals(ElementType.LISTENER, trace.getType());
        Assert.assertEquals("throwLinkTerminateListener", trace.getName());
        Assert.assertEquals(2, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("event", argument.getName());
        Assert.assertEquals(event.name(), argument.getValue());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        if (trace.getCause() == null) {
            sleep(1);
        }
        Assert.assertNotNull(trace.getCause());
    }

    protected void assertSetAttrListener(Trace trace, String name, String value, ListenerEvent event) {
        Argument argument;
        Attribute attr;

        Assert.assertEquals(ElementType.LISTENER, trace.getType());
        Assert.assertEquals("setTraceAttrListener", trace.getName());
        Assert.assertEquals(3, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("event", argument.getName());
        Assert.assertEquals(event.name(), argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("name", argument.getName());
        Assert.assertEquals(name, argument.getValue());
        argument = trace.getArguments().get(2);
        Assert.assertEquals("value", argument.getName());
        Assert.assertEquals(value, argument.getValue());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(1, trace.getAttributes().size());
        attr = trace.getAttributes().get(name);
        Assert.assertEquals(name, attr.getName());
        Assert.assertEquals(value, attr.getValue());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertSuccessResultListener(Trace trace, Object result, ListenerEvent event) {
        Argument argument;
        Attribute attr;

        Assert.assertEquals(ElementType.LISTENER, trace.getType());
        Assert.assertEquals("successResultListener", trace.getName());
        Assert.assertEquals(2, trace.getArguments().size());
        argument = trace.getArguments().get(0);
        Assert.assertEquals("event", argument.getName());
        Assert.assertEquals(event.name(), argument.getValue());
        argument = trace.getArguments().get(1);
        Assert.assertEquals("result", argument.getName());
        Assert.assertEquals(result, argument.getValue());
        Assert.assertNull(trace.getResult());
        Assert.assertEquals(0, trace.getPropertyUpdates().size());
        Assert.assertEquals(0, trace.getAttributes().size());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertExclusiveGateway(Trace trace) {
        Assert.assertEquals(ElementType.EXCLUSIVE_GATEWAY, trace.getType());
        Assert.assertNull(trace.getName());
        Assert.assertNull(trace.getArguments());
        Assert.assertNull(trace.getResult());
        Assert.assertNull(trace.getPropertyUpdates());
        Assert.assertNull(trace.getAttributes());
        Throwable cause = trace.getCause();
        Assert.assertEquals(trace.getEndTimestamp(), trace.getStartTimestamp());
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertEquals(0, trace.getUseTimeNano());
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertJoinGateway(Trace trace) {
        Assert.assertEquals(ElementType.JOIN_GATEWAY, trace.getType());
        Assert.assertNull(trace.getName());
        Assert.assertNull(trace.getArguments());
        Assert.assertNull(trace.getResult());
        Assert.assertNull(trace.getPropertyUpdates());
        Assert.assertNull(trace.getAttributes());
        Assert.assertEquals(trace.getEndTimestamp(), trace.getStartTimestamp());
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertEquals(0, trace.getUseTimeNano());
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertFlow(Trace trace) {
        Assert.assertEquals(ElementType.SUB_FLOW, trace.getType());
        Assert.assertNotNull(trace.getName());
        Assert.assertNull(trace.getArguments());
        Assert.assertNull(trace.getResult());
        Assert.assertNull(trace.getPropertyUpdates());
        Assert.assertNull(trace.getAttributes());
        Assert.assertTrue(trace.getEndTimestamp() >= trace.getStartTimestamp());
        Assert.assertTrue(trace.getEndTimestamp() - trace.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - trace.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(trace.getUseTimeNano() >= 0);
        Assert.assertTrue(trace.getUseTimeNano() < MAX_USETIME_NANO);
        Throwable cause = trace.getCause();
        if (cause != null) {
            Assert.assertTrue(cause instanceof FlowException);
            Assert.assertEquals(FlowErrorCode.FLOW_ALREADY_FINISHED, ((FlowException) cause).getCode());
        }
    }

    protected void assertExecutionInstance(ExecutionInstance executionInstance, int finishedSize, int unFinishedSize, int traceNum) {
        Assert.assertEquals(finishedSize, executionInstance.getLinks().size());
        Assert.assertEquals(unFinishedSize, executionInstance.getUnreachableLinks().size());
        Assert.assertEquals(traceNum, executionInstance.getTraces().size());
        Assert.assertTrue(executionInstance.getEndTimestamp() >= executionInstance.getStartTimestamp());
        Assert.assertTrue(executionInstance.getEndTimestamp() - executionInstance.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - executionInstance.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(executionInstance.getUseTimeNano() >= 0);
        Assert.assertTrue(executionInstance.getUseTimeNano() < MAX_USETIME_NANO);

        for (ExecutionLink link : executionInstance.getLinks()) {
            List<Trace> traces = link.getTraces();

            for (int i = 0; i < traces.size() - 1; i++) {
                Trace pre = traces.get(i);
                Trace next = traces.get(i + 1);

                Assert.assertTrue(pre.getExecutionId() < next.getExecutionId());
            }
        }
    }

    protected void assertExecutionInstance(ExecutionInstance executionInstance, int finishedSize, int minUnfinishedSize, int maxUnfinishedSize, int traceNum) {
        Assert.assertEquals(finishedSize, executionInstance.getLinks().size());
        Assert.assertTrue(executionInstance.getUnreachableLinks().size() >= minUnfinishedSize
                && executionInstance.getUnreachableLinks().size() <= maxUnfinishedSize);
        Assert.assertEquals(traceNum, executionInstance.getTraces().size());
        Assert.assertTrue(executionInstance.getEndTimestamp() >= executionInstance.getStartTimestamp());
        Assert.assertTrue(executionInstance.getEndTimestamp() - executionInstance.getStartTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(System.currentTimeMillis() - executionInstance.getEndTimestamp() < MAX_TIMESTAMP_OFFSET);
        Assert.assertTrue(executionInstance.getUseTimeNano() >= 0);
        Assert.assertTrue(executionInstance.getUseTimeNano() < MAX_USETIME_NANO);

        for (ExecutionLink link : executionInstance.getLinks()) {
            List<Trace> traces = link.getTraces();

            for (int i = 0; i < traces.size() - 1; i++) {
                Trace pre = traces.get(i);
                Trace next = traces.get(i + 1);

                Assert.assertTrue(pre.getExecutionId() < next.getExecutionId());
            }
        }
    }

    protected void assertExecutionLink(ExecutionLink executionLink, int size) {
        Assert.assertEquals(size, executionLink.getTraces().size());
    }

    protected void assertExecutionLink(ExecutionLink executionLink, int minSize, int maxSize) {
        Assert.assertTrue(executionLink.getTraces().size() >= minSize
                && executionLink.getTraces().size() <= maxSize);

        Assert.assertFalse(executionLink.getTraces().size() < minSize);
        Assert.assertFalse(executionLink.getTraces().size() > maxSize);
    }

    protected ExecutionLink findLink(ExecutionInstance executionInstance, int traceNum) {
        for (ExecutionLink link : executionInstance.getLinks()) {
            if (link.getTraces().size() == traceNum) {
                return link;
            }
        }
        throw new RuntimeException();
    }

    protected ExecutionLink findUnreachableLink(ExecutionInstance executionInstance, int traceNum) {
        for (ExecutionLink link : executionInstance.getUnreachableLinks()) {
            if (link.getTraces().size() == traceNum) {
                return link;
            }
        }
        throw new RuntimeException();
    }

    private void sleep(int seconds) {
        TimeUtils.sleep(seconds);
    }
}
