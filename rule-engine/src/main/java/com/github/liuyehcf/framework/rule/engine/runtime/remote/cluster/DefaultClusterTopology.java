package com.github.liuyehcf.framework.rule.engine.runtime.remote.cluster;

import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author hechenfeng
 * @date 2019/9/5
 */
public class DefaultClusterTopology implements ClusterTopology {

    private final Map<String, ClusterNode> nodes = Maps.newConcurrentMap();
    private volatile boolean isIdentifierValid = false;
    private volatile String identifier;

    @Override
    public final ClusterNode getNode(String identifier) {
        return nodes.get(identifier);
    }

    @Override
    public final void putNode(ClusterNode node) {
        isIdentifierValid = false;
        nodes.put(node.getIdentifier(), node);
    }

    @Override
    public final int activeNum() {
        int activeNum = 0;
        for (ClusterNode value : nodes.values()) {
            if (Objects.equals(ClusterNodeStatus.active, value.getStatus())) {
                activeNum++;
            }
        }
        return activeNum;
    }

    @Override
    public final int inactiveNum() {
        int inactiveNum = 0;
        for (ClusterNode value : nodes.values()) {
            if (Objects.equals(ClusterNodeStatus.inactive, value.getStatus())) {
                inactiveNum++;
            }
        }
        return inactiveNum;
    }

    @Override
    public final String getIdentifier() {
        if (isIdentifierValid) {
            return identifier;
        }
        synchronized (this) {
            if (!isIdentifierValid) {
                initIdentifier();
            }
        }
        return identifier;
    }

    @Override
    @Nonnull
    public final Iterator<ClusterNode> iterator() {
        return nodes.values().iterator();
    }

    private void initIdentifier() {
        Map<String, ClusterNode> orderedNodes = Maps.newTreeMap();

        for (ClusterNode node : this) {
            orderedNodes.put(node.getIdentifier(), node);
        }

        StringBuilder sb = new StringBuilder();
        for (ClusterNode node : orderedNodes.values()) {
            sb.append(node.getIdentifier())
                    .append('/').append(node.getStatus().name())
                    .append('/').append(node.getVersion())
                    .append(',');
        }

        identifier = DigestUtils.md5Hex(sb.toString());
    }
}
