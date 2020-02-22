package com.github.liuyehcf.framework.flow.engine.util;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/9/15
 */
public abstract class SplitUtils {

    public static <T> List<List<T>> split(List<T> list, int segmentSize) {
        Assert.assertNotNull(list);
        Assert.assertTrue(segmentSize > 0);

        final List<List<T>> segments = Lists.newArrayList();

        int left = 0, right = segmentSize < list.size() ? segmentSize : list.size();

        while (right < list.size()) {
            final List<T> segment = Lists.newArrayList(list.subList(left, right));

            left = right;

            if (right + segmentSize < list.size()) {
                right += segmentSize;
            } else {
                right = list.size();
            }

            segments.add(segment);
        }

        segments.add(Lists.newArrayList(list.subList(left, right)));

        return segments;
    }
}
