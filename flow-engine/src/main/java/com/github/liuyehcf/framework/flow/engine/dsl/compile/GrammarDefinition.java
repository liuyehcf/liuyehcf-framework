package com.github.liuyehcf.framework.flow.engine.dsl.compile;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.DefaultLexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.LexicalAnalyzer;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.impl.FloatIdentifier;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.impl.IntegerIdentifier;
import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.impl.StringIdentifier;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import static com.github.liuyehcf.framework.flow.engine.dsl.compile.Constant.*;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public class GrammarDefinition {

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            ProgramProductions.PRODUCTIONS,
            BlockProductions.PRODUCTIONS,
            NodeProductions.PRODUCTIONS,
            TokenProductions.PRODUCTIONS
    );


    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.create()
            .addTokenOperator(Symbol.createIdentifierTerminator(IDENTIFIER_INTEGER_LITERAL), new IntegerIdentifier())
            .addTokenOperator(Symbol.createIdentifierTerminator(IDENTIFIER_FLOATING_POINT_LITERAL), new FloatIdentifier())
            .addTokenOperator(Symbol.createIdentifierTerminator(IDENTIFIER_STRING_LITERAL), new StringIdentifier())
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_DOLLAR), "$")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_AND), "&")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_BIT_OR), "|")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES), "[")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES), "]")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES), "{")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES), "}")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COMMA), ",")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_ASSIGN), "=")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_DOT_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*(\\.[a-zA-Z_]([a-zA-Z_]|[0-9])*)+")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_SLASH_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*(/[a-zA-Z_]([a-zA-Z_]|[0-9])*)+")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_DASH_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*(-[a-zA-Z_]([a-zA-Z_]|[0-9])*)+")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_IDENTIFIER), "[a-zA-Z_]([a-zA-Z_]|[0-9])*")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_TRUE), "true")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_BOOLEAN_FALSE), "false")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_SUB), "sub")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_SELECT), "select")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_JOIN), "join")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_THEN), "then")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_IF), "if")
            .addKeyWordMorpheme(Symbol.createTerminator(NORMAL_ELSE), "else")
            .build();
}
