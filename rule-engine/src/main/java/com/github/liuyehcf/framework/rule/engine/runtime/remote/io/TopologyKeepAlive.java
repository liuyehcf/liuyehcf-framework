package com.github.liuyehcf.framework.rule.engine.runtime.remote.io;

import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNode;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNodeConfig;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterNodeStatus;
import com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster.ClusterTopology;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author hechenfeng
 * @date 2019/9/8
 */
public class TopologyKeepAlive extends BaseScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopologyKeepAlive.class);

    private static final String DEFAULT_GROUP = "topologyKeepAlive";
    private static final String KEY_EVENT_LOOP = "eventLoop";

    static void register(ClusterEventLoop clusterEventLoop, int keepAliveInterval) {
        String name = UUID.randomUUID().toString();

        JobDetail jobDetail = JobBuilder.newJob(TopologyKeepAliveJob.class)
                .withIdentity(name, DEFAULT_GROUP)
                .build();

        jobDetail.getJobDataMap().put(KEY_EVENT_LOOP, clusterEventLoop);

        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, DEFAULT_GROUP)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(keepAliveInterval).repeatForever())
                .startAt(new Date(System.currentTimeMillis() + keepAliveInterval * 1000))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            clusterEventLoop.addCloseListener((promise) -> {
                LOGGER.info("Unregister topologyKeepAlive due to cluster event loop shutdown");
            });
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public static final class TopologyKeepAliveJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDetail jobDetail = context.getJobDetail();
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            ClusterEventLoop clusterEventLoop = (ClusterEventLoop) jobDataMap.get(KEY_EVENT_LOOP);

            ClusterTopology topology = clusterEventLoop.getTopology();
            ClusterNodeConfig selfConfig = clusterEventLoop.getEngine()
                    .getProperties()
                    .getSelfConfig();

            for (ClusterNode node : topology) {
                if (Objects.equals(ClusterNodeStatus.active, node.getStatus())
                        && !Objects.equals(selfConfig.getIdentifier(), node.getIdentifier())
                        && !clusterEventLoop.hasChannel(node.getIdentifier())) {
                    try {
                        clusterEventLoop.createChannel(node);
                    } catch (Throwable e) {
                        LOGGER.error("Create cluster channel catch unknown error, errorMsg={}", e.getMessage(), e);
                    }
                }
            }
        }
    }
}
