package com.github.liuyehcf.framework.compile.engine.test.rg;

import com.github.liuyehcf.framework.compile.engine.grammar.converter.SimplificationGrammarConverter;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.PrimaryProduction;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Production;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;
import com.github.liuyehcf.framework.compile.engine.rg.utils.GrammarUtils;

/**
 * Created by Liuye on 2017/10/24.
 */
class TestRegex {
    static final String[] REGEX_GROUP_1 = {
            "", "a", "z", "A", "Z", "!", ".", "#", "\0", "\n",
            "azAZ01!#@",
    };
    static final String[] REGEX_GROUP_2 = {
            "\\.", "\\|", "\\*", "\\+", "\\[", "\\]", "\\(", "\\)",
            "\\d", "\\D", "\\w", "\\W"
    };
    static final String[] REGEX_GROUP_3 = {
            "[\\[]", "[\\]]", "[\\d]", "[\\D]", "[\\d]", "[\\D]", "[\\^]",
            "[^\\[]", "[^\\]]", "[^\\d]", "[^\\D]", "[^\\d]", "[^\\D]",
            "[a-z]", "[-a-z]", "[a-z-]", "[a-a]", "[-a\\-z-]", "[0-9]", "[\0-A]"
    };
    static final String[] REGEX_GROUP_4 = {
            "a?", "z?", "!?", "\0?", "\n?", ".?",
            "\\.?", "\\|?", "\\*?", "\\+?", "\\[?", "\\]?", "\\(?", "\\)?",
            "\\d?", "\\D?", "\\w?", "\\W?", "\\s?", "\\S?",
            "a?c?", "a?c?efg", "012a?c?efg"
    };
    static final String[] REGEX_GROUP_5 = {
            "a*", "z*", "!*", "\0*", "\n*", ".*",
            "\\.*", "\\|*", "\\**", "\\+*", "\\[*", "\\]*", "\\(*", "\\)*",
            "\\d*", "\\D*", "\\w*", "\\W*", "\\s*", "\\S*",
            "a*c*", "a*c*efg", "012a*c*efg"
    };
    static final String[] REGEX_GROUP_6 = {
            "a+", "z+", "!+", "\0+", "\n+", ".+",
            "\\.+", "\\|+", "\\*+", "\\++", "\\[+", "\\]+", "\\(+", "\\)+",
            "\\d+", "\\D+", "\\w+", "\\W+", "\\s+", "\\S+",
            "a+c+", "a+c+efg", "012a+c+efg"
    };
    static final String[] REGEX_GROUP_7 = {
            "a{1,1}", "z{4,10}", "!{10,}", "\0{2,3}", "\n{1,6}", ".{1,1}",
            "\\.{2,3}", "\\|{2,3}", "\\*{2,3}", "\\+{10,}", "\\[{10,}", "\\]{10,}", "\\({1,1}", "\\){1,1}",
            "\\d{1,1}", "\\D{1}", "\\w{1,2}", "\\W{1,3}", "\\s{2,}", "\\S{1,2}",
    };
    static final String[] REGEX_GROUP_8 = {
            "a|b", "aaaaa|b", "a|bbbbbb", "0000|11111",
            "a|b12c|c|d!ef|ge+l#ksjd|a*po",
            "a|[abc\\d]", "[^\\W0-1]|bc",
            ".a|b12c|c.|d!ef|ge+l#ksjd|a*po",
            "a|0|1|b|z", "a|0[sdfsd]f|1|bddd|z",
            "a*|b+|c|.|[12]3|asdjf[\\dhaks]jdhf{2,5}|ced|ac\\d+edf|\0\\w",
            "111[\\d]ac|cd[^\\W].\\*+|cd09!%@#"
    };
    static final String[] REGEX_GROUP_9 = {
            "()", "()()", "(())", "((()))()()(()((()(()))))()",
            "(abc(cd)(ef(g)()))", "((a)|(b){3,})", "((a)+)", "a*", "((a(b|e))*)", "(\\()", "(\\()|(a)|(\\))",
            "(a*)+", "(a+)*", "(a+)*|(a*)+",
            "a(b(d(e|f)*)|((g(h|i)+)))+",
            "a(b(d(e+|f*))|((g(\\d|i)+)))+",
            "(a)", "(0)", "(\\d)", "(\\w)", "(.)", "(a+)", "(0*)", "(\0)",
            "(a|b)", "([0123]{7,7})", "(a|bc[\\d])",
            "(a|b+)", "(c[0123-9]+e)", "(a|bc[\\d])",
            "(ab*c+|1[^\\w]+)|(a(b(e(g|o9))+)).",
            "a|(bc?d|(efg|[\\w]))c\\++|liuye",
            "((a+())~!@#[\\d]ac)(\0)()((098)(2\\.9(3()(c()de){1,2}r[^\\S]c)s)ef)a()",
            "(ab|cd*f|es(d)(3|[\\s]|0a|9(3(37(9+)?)99|283(a(())!%#@)(a|(4(d)([1-2z-])))(7()1+)9)))"
    };
    static final String[] REGEX_GROUP_SPECIAL = {
            "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*",//email
            "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}",//UUID
            "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}",//IP
            "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3} - - \\[[^\\]]+\\] \"[^\"]+\" \\d+ \\d+ \"[^\"]+\" \"[^\"]+\"",
            createIdentifierRegex(),
    };

    private static Grammar createIdentifierGrammar() {
        Symbol digit = Symbol.createNonTerminator("digit");
        Symbol letter_ = Symbol.createNonTerminator("letter_");
        Symbol id = Symbol.createNonTerminator("id");

        return new SimplificationGrammarConverter(
                Grammar.create(
                        id,
                        Production.create(
                                PrimaryProduction.create(
                                        digit,
                                        GrammarUtils.createPrimaryProduction("[0-9]"),
                                        null
                                )
                        ),
                        Production.create(
                                PrimaryProduction.create(
                                        letter_,
                                        GrammarUtils.createPrimaryProduction("[a-zA-Z_]"),
                                        null
                                )
                        ),
                        Production.create(
                                PrimaryProduction.create(
                                        id,
                                        GrammarUtils.createPrimaryProduction(letter_, '(', letter_, '|', digit, ')', '*'),
                                        null
                                )
                        )
                )
        ).getConvertedGrammar();
    }

    static String createIdentifierRegex() {
        Grammar grammar = createIdentifierGrammar();

        StringBuilder sb = new StringBuilder();

        for (Symbol symbol : GrammarUtils.extractSymbolsFromGrammar(grammar)) {
            sb.append(symbol.getValue());
        }

        return sb.toString();
    }
}
