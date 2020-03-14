package com.github.liuyehcf.framework.flow.engine.dsl;

import com.github.liuyehcf.framework.common.tools.asserts.Assert;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.LALR;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SemanticAction;
import com.github.liuyehcf.framework.flow.engine.dsl.compile.semantic.AbstractSemanticAction;
import com.github.liuyehcf.framework.flow.engine.model.Flow;
import com.github.liuyehcf.framework.flow.engine.model.IDGenerator;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import static com.github.liuyehcf.framework.flow.engine.dsl.compile.GrammarDefinition.GRAMMAR;
import static com.github.liuyehcf.framework.flow.engine.dsl.compile.GrammarDefinition.LEXICAL_ANALYZER;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public class DslCompiler extends LALR<Flow> {

    static final String COMPILER_SERIALIZATION_FILE = "FLOW_DSL_COMPILER";

    private static final DslCompiler INSTANCE = loadCompiler();

    private DslCompiler() {
        super(GRAMMAR, LEXICAL_ANALYZER);
    }

    public static DslCompiler getInstance() {
        return INSTANCE;
    }

    private static DslCompiler loadCompiler() {
        try {
            InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(COMPILER_SERIALIZATION_FILE);
            Assert.assertNotNull(resourceStream, "resourceStream");
            ObjectInputStream inputStream = new ObjectInputStream(resourceStream);
            return (DslCompiler) inputStream.readObject();
        } catch (Throwable e) {
            //ignore
        }
        return new DslCompiler();
    }

    @Override
    protected Engine createCompiler(String input) {
        return new DslEngine(input);
    }

    public class DslEngine extends Engine {

        private final LinkedList<FlowSegment> flowStack = Lists.newLinkedList();
        private final IDGenerator idGenerator = IDGenerator.create();

        DslEngine(String input) {
            super(input);
        }

        @Override
        protected void before() {
            super.before();
        }

        @Override
        protected void after() {
            Assert.assertEquals(1, flowStack.size());
            FlowSegment flow = flowStack.pop();
            flow.init();
            setResult(flow);
        }

        @Override
        protected void onReduction(Context context) {
            List<SemanticAction<?>> semanticActions = context.getRawPrimaryProduction().getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (SemanticAction<?> semanticAction : semanticActions) {
                ((AbstractSemanticAction) semanticAction).onAction(new CompilerContext(context, flowStack, idGenerator));
            }
        }
    }
}
