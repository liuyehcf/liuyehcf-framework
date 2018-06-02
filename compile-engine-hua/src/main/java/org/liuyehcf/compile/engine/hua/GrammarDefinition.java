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
 * TODO 检查?语法；补全TODO；doSomething无法识别
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
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_IF), "if")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ELSE), "else")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_WHILE), "while")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_FOR), "for")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DO), "do")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_VOID), "void")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_INT), "int")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_FLOAT), "float")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN), "boolean")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_NEW), "new")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_RETURN), "return")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_TRUE), "true")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_FALSE), "false")
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
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LEFT_SHIFT_ASSIGN), "<<=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_RIGHT_SHIFT_SIGNED_ASSIGN), ">>=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_RIGHT_SHIFT_UNSIGNED_ASSIGN), ">>>=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_RIGHT_SHIFT_UNSIGNED), ">>>")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LEFT_SHIFT), "<<")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_RIGHT_SHIFT_SIGNED), ">>")
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
