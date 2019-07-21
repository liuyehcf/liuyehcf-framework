package com.github.liuyehcf.framework.rule.engine.dsl;

import com.github.liuyehcf.framework.compile.engine.cfg.lr.Context;
import com.github.liuyehcf.framework.compile.engine.cfg.lr.LALR;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.SemanticAction;
import com.github.liuyehcf.framework.compile.engine.utils.Assert;
import com.github.liuyehcf.framework.rule.engine.model.IDGenerator;
import com.github.liuyehcf.framework.rule.engine.model.Rule;
import com.google.common.collect.Lists;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import static com.github.liuyehcf.framework.rule.engine.dsl.compile.GrammarDefinition.GRAMMAR;
import static com.github.liuyehcf.framework.rule.engine.dsl.compile.GrammarDefinition.LEXICAL_ANALYZER;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public class DslCompiler extends LALR<Rule> {

    static final String COMPILER_SERIALIZATION_FILE = "RULE_DSL_COMPILER";

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
            Assert.assertNotNull(resourceStream);
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

        private final LinkedList<RuleSegment> ruleStack = Lists.newLinkedList();
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
            Assert.assertEquals(1, ruleStack.size());
            RuleSegment rule = ruleStack.pop();
            rule.init();
            setResult(rule);
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void onReduction(Context context) {
            List<SemanticAction> semanticActions = context.getRawPrimaryProduction().getSemanticActions();
            if (semanticActions == null) {
                return;
            }

            for (SemanticAction semanticAction : semanticActions) {
                semanticAction.onAction(new CompilerContext(context, ruleStack, idGenerator));
            }
        }
    }
}
