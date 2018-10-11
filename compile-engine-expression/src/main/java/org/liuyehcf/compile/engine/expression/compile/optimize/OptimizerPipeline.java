package org.liuyehcf.compile.engine.expression.compile.optimize;

import com.google.common.collect.Lists;
import org.liuyehcf.compile.engine.expression.core.ExpressionCode;

import java.util.Collections;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2018/9/30
 */
public class OptimizerPipeline implements Optimizer {

    private final List<Optimizer> optimizers;

    private OptimizerPipeline(List<Optimizer> optimizers) {
        this.optimizers = Collections.unmodifiableList(optimizers);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void optimize(final ExpressionCode expressionCode) {
        for (Optimizer optimizer : optimizers) {
            optimizer.optimize(expressionCode);
        }
    }

    public static final class Builder {
        private final List<Optimizer> optimizers = Lists.newArrayList();

        private Builder() {

        }

        public Builder addOptimizer(Optimizer optimizer) {
            optimizers.add(optimizer);
            return this;
        }

        public OptimizerPipeline build() {
            return new OptimizerPipeline(optimizers);
        }
    }
}
