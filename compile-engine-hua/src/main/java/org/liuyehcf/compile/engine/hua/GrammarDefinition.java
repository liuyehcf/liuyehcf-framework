package org.liuyehcf.compile.engine.hua;

import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.hua.production.*;

import static org.liuyehcf.compile.engine.hua.production.Block.NORMAL_ELSE;
import static org.liuyehcf.compile.engine.hua.production.Block.NORMAL_IF;
import static org.liuyehcf.compile.engine.hua.production.Program.NORMAL_VOID;
import static org.liuyehcf.compile.engine.hua.production.Program.PROGRAMS;
import static org.liuyehcf.compile.engine.hua.production.Type.*;

/**
 * @author chenlu
 * @date 2018/5/28
 */
public class GrammarDefinition {

    /**
     * 非终结符
     */
    public static final String ARRAY_INITIALIZER = "<array initializer>";
    public static final String RETURN_STATEMENT = "<return statement>";

    public static final String ARRAY_CREATION_EXPRESSION = "<array creation expression>";

    /**
     * 正则表达式的终结符
     */
    public static final String REGEX_IDENTIFIER = "@identifier";

    /**
     * 普通终结符
     */
    public static final String NORMAL_SMALL_LEFT_PARENTHESES = "(";
    public static final String NORMAL_SMALL_RIGHT_PARENTHESES = ")";
    public static final String NORMAL_MIDDLE_LEFT_PARENTHESES = "[";
    public static final String NORMAL_MIDDLE_RIGHT_PARENTHESES = "]";
    public static final String NORMAL_LARGE_LEFT_PARENTHESES = "{";
    public static final String NORMAL_LARGE_RIGHT_PARENTHESES = "}";
    public static final String NORMAL_ASSIGN = "=";
    public static final String NORMAL_COMMA = ",";
    public static final String NORMAL_SEMICOLON = ";";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            Program.PRODUCTIONS,
            Type.PRODUCTIONS,
            Block.PRODUCTIONS,
            Expression.PRODUCTIONS,
            Token.PRODUCTIONS
    );


    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES), "[")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES), "]")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES), "{")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES), "}")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_IF), "if")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ELSE), "else")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ASSIGN), "=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COMMA), ",")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SEMICOLON), ";")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_VOID), "void")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_INT), "int")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_FLOAT), "float")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN), "boolean")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .build();

    public static void main(String[] args) {
        LRCompiler compiler = LR1.create(LEXICAL_ANALYZER, GRAMMAR);
        System.out.println(compiler.isLegal());
        compiler.compile("void sort(int a){}\n" +
                "\n" +
                "int add(int a,int b){}\n" +
                "\n" +
                "void exchange(int a,int b){\n" +
                "    int a, b[][];\n" +
                "    a=b;\n" +
                "    b=c;\n" +
                "}");
    }
}
