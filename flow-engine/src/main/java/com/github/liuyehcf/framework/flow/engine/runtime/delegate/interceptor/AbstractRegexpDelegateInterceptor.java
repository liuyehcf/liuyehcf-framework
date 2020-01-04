package com.github.liuyehcf.framework.flow.engine.runtime.delegate.interceptor;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author hechenfeng
 * @date 2019/7/19
 */
public abstract class AbstractRegexpDelegateInterceptor implements DelegateInterceptor {

    private static final Map<String, Pattern> REGEX_PATTERNS = Maps.newConcurrentMap();

    private volatile Pattern pattern;

    @Override
    public final boolean matches(String executableName) {
        if (pattern == null) {
            lazyInit();
        }

        return pattern.matcher(executableName).matches();
    }

    /**
     * get regex expression of this regex interceptor
     *
     * @return regex expression
     */
    protected abstract String getRegexExpression();

    private void lazyInit() {
        String regexp = getRegexExpression();
        Pattern pattern = REGEX_PATTERNS.get(regexp);
        if (pattern != null) {
            // this.pattern may be written concurrently, it's ok
            this.pattern = pattern;
            return;
        }

        pattern = Pattern.compile(regexp);
        REGEX_PATTERNS.putIfAbsent(regexp, pattern);

        // this.pattern may be written concurrently, it's ok
        this.pattern = pattern;
    }
}
