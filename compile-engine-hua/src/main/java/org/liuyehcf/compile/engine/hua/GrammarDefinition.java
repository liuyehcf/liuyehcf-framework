package org.liuyehcf.compile.engine.hua;

import org.liuyehcf.compile.engine.core.cfg.DefaultLexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.LexicalAnalyzer;
import org.liuyehcf.compile.engine.core.cfg.lr.LR1;
import org.liuyehcf.compile.engine.core.cfg.lr.LRCompiler;
import org.liuyehcf.compile.engine.core.grammar.definition.Grammar;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.hua.production.Block;
import org.liuyehcf.compile.engine.hua.production.Expression;
import org.liuyehcf.compile.engine.hua.production.Program;
import org.liuyehcf.compile.engine.hua.production.Token;

/**
 * @author chenlu
 * @date 2018/5/28
 */
public class GrammarDefinition {

    /**
     * 非终结符
     */
    public static final String PROGRAMS = "<programs>";
    public static final String METHOD_DECLARATIONS = "<method declarations>";
    public static final String METHOD_DECLARATION = "<method declaration>";
    public static final String METHOD_BODY = "<method body>";
    public static final String FORMAL_PARAMETERS = "<formal parameters>";
    public static final String FORMAL_PARAMETER_LIST = "<formal parameter list>";
    public static final String FORMAL_PARAMETER = "<formal parameter>";
    public static final String BLOCK = "<block>";
    public static final String BLOCK_STATEMENTS = "<block statements>";
    public static final String BLOCK_STATEMENT = "<block statement>";
    public static final String LOCAL_VARIABLE_DECLARATION_STATEMENT = "<local variable declaration statement>";
    public static final String LOCAL_VARIABLE_DECLARATION = "<local variable declaration>";
    public static final String VARIABLE_DECLARATORS = "<variable declarators>";
    public static final String VARIABLE_DECLARATOR = "<variable declarator>";
    public static final String VARIABLE_DECLARATOR_ID = "<variable declarator id>";
    public static final String VARIABLE_INITIALIZER = "<variable initializer>";
    public static final String ARRAY_INITIALIZER = "<array initializer>";
    public static final String EXPRESSION = "<expression>";
    public static final String STATEMENT = "<statement>";
    public static final String STATEMENT_WITHOUT_TRAILING_SUBSTATEMENT = "<statement without trailing substatement>";
    public static final String EMPTY_STATEMENT = "<empty statement>";
    public static final String EXPRESSION_STATEMENT = "<expression statement>";
    public static final String RETURN_STATEMENT = "<return statement>";

    public static final String STATEMENT_EXPRESSION = "<statement expression>";
    public static final String ASSIGNMENT = "<assignment>";
    public static final String LEFT_HAND_SIDE = "<left hand side>";
    public static final String ASSIGNMENT_OPERATOR = "<assignment operator>";
    public static final String ASSIGNMENT_EXPRESSION = "<assignment expression>";
    public static final String EXPRESSION_NAME = "<expression name>";
    public static final String ARRAY_CREATION_EXPRESSION = "<array creation expression>";

    /**
     * 正则表达式的终结符
     */
    public static final String REGEX_IDENTIFIER = "@identifier";
    public static final String REGEX_TYPE = "@type";

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
    public static final String NORMAL_COMMA = "comma";
    public static final String NORMAL_SEMICOLON = ";";

    public static final Grammar GRAMMAR = Grammar.create(
            Symbol.createNonTerminator(PROGRAMS),
            Program.PRODUCTIONS,
            Block.PRODUCTIONS,
            Expression.PRODUCTIONS,
            Token.PRODUCTIONS
    );


    public static LexicalAnalyzer LEXICAL_ANALYZER = DefaultLexicalAnalyzer.Builder.builder()
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_LEFT_PARENTHESES), "(")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_SMALL_RIGHT_PARENTHESES), ")")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_LEFT_PARENTHESES), "{")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_LARGE_RIGHT_PARENTHESES), "}")
            .addNormalMorpheme(Symbol.createTerminator(NORMAL_COMMA), ",")
            .addRegexMorpheme(Symbol.createRegexTerminator(REGEX_TYPE), "(void)|(int)|(float)|(char)|(bool)")
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
