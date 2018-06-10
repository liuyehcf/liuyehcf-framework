package org.liuyehcf.compile.engine.hua;

import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.hua.production.*;

import static org.liuyehcf.compile.engine.hua.production.Block.*;
import static org.liuyehcf.compile.engine.hua.production.Expression.*;
import static org.liuyehcf.compile.engine.hua.production.Program.NORMAL_VOID;
import static org.liuyehcf.compile.engine.hua.production.Program.PROGRAMS;
import static org.liuyehcf.compile.engine.hua.production.Token.*;
import static org.liuyehcf.compile.engine.hua.production.Type.*;

/**
 * @author hechenfeng
 * @date 2018/5/28
 */
public class GrammarDefinition {

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
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DOUBLE_PLUS), "++")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DOUBLE_MINUS), "--")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_EQUAL), "==")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_NOT_EQUAL), "!=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL_ASSIGN), "*=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DIV_ASSIGN), "/=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MOD_ASSIGN), "%=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ADD_ASSIGN), "+=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MINUS_ASSIGN), "-=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_AND_ASSIGN), "&=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_EXCLUSIVE_OR_ASSIGN), "^=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_OR_ASSIGN), "|=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHIFT_LEFT_ASSIGN), "<<=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHIFT_RIGHT_ASSIGN), ">>=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_UNSIGNED_SHIFT_RIGHT_ASSIGN), ">>>=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_UNSIGNED_SHIFT_RIGHT), ">>>")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHIFT_LEFT), "<<")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHIFT_RIGHT), ">>")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LESS_EQUAL), "<=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_EQUAL), ">=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LOGICAL_OR), "||")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LOGICAL_AND), "&&")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ASSIGN), "=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LESS), "<")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE), ">")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL), "*")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DIV), "/")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MOD), "%")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ADD), "+")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_OR), "|")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_AND), "&")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COLON), ":")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_QUESTION_MARK), "?")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_EXCLUSIVE_OR), "^")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MINUS), "-")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LOGICAL_NOT), "!")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_REVERSED), "~")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COMMA), ",")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SEMICOLON), ";")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_NUMBER_0), "0")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_NON_ZERO_DIGIT), "[1-9]")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_IF), "if")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_ELSE), "else")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_WHILE), "while")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_FOR), "for")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_DO), "do")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_VOID), "void")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_INT), "int")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_FLOAT), "float")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN), "boolean")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_NEW), "new")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_RETURN), "return")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_TRUE), "true")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_FALSE), "false")
            .build();

    public static void main(String[] args) {
        LRCompiler compiler = new LR1(GRAMMAR, LEXICAL_ANALYZER);
        System.out.println(compiler.isLegal());
        compiler.compile("void sort(int a){}\n" +
                "\n" +
                "int addByteCode(int a,int b){}\n" +
                "\n" +
                "void exchange(int a,int b){\n" +
                "    int a, b[][];\n" +
                "    a=b;\n" +
                "    b=c;\n" +
                "}");
    }
}
