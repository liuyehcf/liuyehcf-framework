package com.github.liuyehcf.framework.io.athena.processor;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.io.athena.Cluster;
import com.github.liuyehcf.framework.io.athena.Member;
import com.github.liuyehcf.framework.io.athena.UnsafeEnvoy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hechenfeng
 * @date 2020/2/6
 */
public abstract class AbstractProcessor<T> implements Processor<T> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractProcessor.class);
    protected final UnsafeEnvoy envoy;
    protected final Cluster cluster;
    protected final Member self;
    private final TypeMatcher matcher;

    protected AbstractProcessor(UnsafeEnvoy envoy) {
        Assert.assertNotNull(envoy, "envoy");
        Class<?> type = TypeMatcher.fetchType(this, AbstractProcessor.class, "T");
        this.matcher = TypeMatcher.create(type);
        this.envoy = envoy;
        this.cluster = envoy.getCluster();
        this.self = cluster.getSelf();
    }

    @Override
    public final boolean match(Object event) {
        return matcher.match(event);
    }

    @Override
    public final void process(EventContext<T> context) throws Exception {
        if (needLog()) {
            LOGGER.debug("[{}] receive '{}' from [{}]",
                    self, context.getEvent().getClass().getSimpleName(), context.getPeer());
        }
        doProcess(context);
    }

    /**
     * process event
     *
     * @param context event context
     */
    protected void doProcess(EventContext<T> context) throws Exception {
        // default
    }

    protected boolean needLog() {
        return true;
    }
}
