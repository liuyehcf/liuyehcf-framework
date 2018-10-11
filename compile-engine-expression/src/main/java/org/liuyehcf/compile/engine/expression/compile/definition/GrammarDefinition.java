package org.liuyehcf.compile.engine.expression.compile.definition;

import org.liuyehcf.compile.engine.core.cfg.lexical.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lexical.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lexical.identifier.impl.FloatIdentifier;
import org.liuyehcf.compile.engine.core.cfg.lexical.identifier.impl.IntegerIdentifier;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.expression.compile.definition.identifier.ExpressionStringIdentifier;

import static org.liuyehcf.compile.engine.expression.compile.definition.Constant.*;
import static org.liuyehcf.compile.engine.expression.compile.definition.ProgramProductions.PROGRAMS;

/**
 * 文法定义
 *
 * @author hechenfeng
 * @date 2018/9/25
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
    public static final String NORMAL_COMMA = ",";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            ProgramProductions.PRODUCTIONS,
            ExpressionProductions.PRODUCTIONS,
            TokenProductions.PRODUCTIONS
    );


    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addTokenOperator(Symbol.createIdentifierTerminator(TokenProductions.IDENTIFIER_INTEGER_LITERAL), new IntegerIdentifier())
            .addTokenOperator(Symbol.createIdentifierTerminator(TokenProductions.IDENTIFIER_FLOATING_POINT_LITERAL), new FloatIdentifier())
            .addTokenOperator(Symbol.createIdentifierTerminator(TokenProductions.IDENTIFIER_STRING_LITERAL), new ExpressionStringIdentifier())
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES), "[")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES), "]")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COMMA), ",")
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
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*(\\.[a-zA-Z_]([a-zA-Z_]|[0-9])*)*")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_TRUE), "true")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_FALSE), "false")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_NULL), "null")
            .build();
}
