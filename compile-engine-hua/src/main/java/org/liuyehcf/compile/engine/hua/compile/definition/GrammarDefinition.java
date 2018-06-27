package org.liuyehcf.compile.engine.hua.compile.definition;

import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.hua.compile.definition.ProgramProductions.PROGRAMS;
import static org.liuyehcf.compile.engine.hua.compile.definition.TokenProductions.*;

/**
 * 文法定义
 *
 * @author hechenfeng
 * @date 2018/5/28
 */
public abstract class GrammarDefinition {

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
            ProgramProductions.PRODUCTIONS,
            TypeProductions.PRODUCTIONS,
            BlockProductions.PRODUCTIONS,
            ExpressionProductions.PRODUCTIONS,
            TokenProductions.PRODUCTIONS
    );


    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES), "[")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES), "]")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES), "{")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES), "}")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COMMA), ",")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SEMICOLON), ";")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COLON), ":")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_QUESTION_MARK), "?")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_REVERSED), "~")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_EQ), "==")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_NE), "!=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LE), "<=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_GE), ">=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LOGICAL_NOT), "!")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LOGICAL_OR), "||")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LOGICAL_AND), "&&")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL_ASSIGN), "*=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DIV_ASSIGN), "/=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_REM_ASSIGN), "%=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ADD_ASSIGN), "+=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SUB_ASSIGN), "-=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHL_ASSIGN), "<<=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHR_ASSIGN), ">>=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_USHR_ASSIGN), ">>>=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_AND_ASSIGN), "&=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_XOR_ASSIGN), "^=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_OR_ASSIGN), "|=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ASSIGN), "=")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DOUBLE_PLUS), "++")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DOUBLE_MINUS), "--")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MUL), "*")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DIV), "/")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_REM), "%")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ADD), "+")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SUB), "-")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHL), "<<")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_USHR), ">>>")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SHR), ">>")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_AND), "&")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_XOR), "^")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_OR), "|")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LT), "<")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_GT), ">")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_NUMBER_0), "0")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_NON_ZERO_DIGIT), "[1-9]")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_CHARACTER_LITERAL), "\'[^'\\\\]\'")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_STRING_LITERAL), "\"[^\"\\\\]*\"")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_IF), "if")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_ELSE), "else")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_WHILE), "while")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_FOR), "for")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_DO), "do")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_VOID), "void")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_INT), "int")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_FLOAT), "float")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN), "boolean")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_CHAR), "char")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_NEW), "new")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_RETURN), "return")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_TRUE), "true")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_FALSE), "false")
            .build();
}
