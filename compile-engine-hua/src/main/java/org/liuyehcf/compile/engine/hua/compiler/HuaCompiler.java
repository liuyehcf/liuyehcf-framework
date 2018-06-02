package org.liuyehcf.compile.engine.hua.compiler;

import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LALR;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;

import java.util.List;

/**
 * @author chenlu
 * @date 2018/6/2
 */
public class HuaCompiler extends LALR {
    protected HuaCompiler(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        super(lexicalAnalyzer, originalGrammar);
    }

    public static LRCompiler create(LexicalAnalyzer lexicalAnalyzer, Grammar originalGrammar) {
        HuaCompiler compiler = new HuaCompiler(lexicalAnalyzer, originalGrammar);

        compiler.init();

        return compiler;
    }

    @Override
    protected Engine createCompiler(String input) {
        return new Engine(input) {
            @Override
            protected void action(PrimaryProduction ppReduction, SyntaxNode left, List<SyntaxNode> rights) {
                super.action(ppReduction, left, rights);
            }
        };
    }
}
