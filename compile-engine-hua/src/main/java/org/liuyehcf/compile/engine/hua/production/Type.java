package org.liuyehcf.compile.engine.hua.production;

import org.liuyehcf.compile.engine.core.grammar.definition.PrimaryProduction;
import org.liuyehcf.compile.engine.core.grammar.definition.Production;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.grammar.definition.SymbolString;

import static org.liuyehcf.compile.engine.hua.GrammarDefinition.NORMAL_MIDDLE_LEFT_PARENTHESES;
import static org.liuyehcf.compile.engine.hua.GrammarDefinition.NORMAL_MIDDLE_RIGHT_PARENTHESES;

/**
 * @author chenlu
 * @date 2018/5/31
 */
public class Type {

    public static final String TYPE = "<type>"; // 119
    public static final String PRIMITIVE_TYPE = "<primitive type>"; // 120
    public static final String NUMERIC_TYPE = "<numeric type>"; // 122
    public static final String INTEGRAL_TYPE = "<integral type>"; // 124
    public static final String FLOATING_POINT_TYPE = "<floating-point type>"; // 126
    public static final String REFERENCE_TYPE = "<reference type>"; // 128
    public static final String ARRAY_TYPE = "<array type>"; // 136

    public static final String NORMAL_BOOLEAN = "boolean";
    public static final String NORMAL_INT = "int";
    public static final String NORMAL_FLOAT = "float";

    public static final Production[] PRODUCTIONS = {
            /*
             * <type> 119
             * SAME
             */
            Production.create(
                    /*
                     * <type> → <primitive type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(PRIMITIVE_TYPE)
                            )
                            , null
                    ),
                    /*
                     * <type> → <primitive type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(REFERENCE_TYPE)
                            )
                            , null
                    )
            ),


            /*
             * <primitive type> 120
             * SAME
             */
            Production.create(
                    /*
                     * <primitive type> → <numeric type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMITIVE_TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(NUMERIC_TYPE)
                            )
                            , null
                    ),
                    /*
                     * <primitive type> → boolean
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(PRIMITIVE_TYPE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_BOOLEAN)
                            )
                            , null
                    )
            ),


            /*
             * <numeric type> 122
             * SAME
             */
            Production.create(
                    /*
                     * <numeric type> → <integral type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(NUMERIC_TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(INTEGRAL_TYPE)
                            )
                            , null
                    ),
                    /*
                     * <numeric type> → <floating-point type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(NUMERIC_TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(FLOATING_POINT_TYPE)
                            )
                            , null
                    )
            ),


            /*
             * <integral type> 124
             * LACK
             */
            Production.create(
                    /*
                     * <integral type> → int
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(INTEGRAL_TYPE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_INT)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <integral type> → byte
                     * <integral type> → short
                     * <integral type> → long
                     * <integral type> → char
                     */
            ),


            /*
             * <floating-point type> 126
             * LACK
             */
            Production.create(
                    /*
                     * <floating-point type> → float
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(FLOATING_POINT_TYPE),
                            SymbolString.create(
                                    Symbol.createTerminator(NORMAL_FLOAT)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <floating-point type> → double
                     */
            ),


            /*
             * <reference type> 128
             * LACK
             */
            Production.create(
                    /*
                     * <reference type> → <array type>
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(REFERENCE_TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(ARRAY_TYPE)
                            )
                            , null
                    )
                    /*
                     * TODO 缺少以下产生式
                     * <reference type> → <class or interface type>
                     */
            ),


            /*
             * <array type> 136
             * SAME
             */
            Production.create(
                    /*
                     * <array type> → <type> [ ]
                     */
                    PrimaryProduction.create(
                            Symbol.createNonTerminator(ARRAY_TYPE),
                            SymbolString.create(
                                    Symbol.createNonTerminator(TYPE),
                                    Symbol.createTerminator(NORMAL_MIDDLE_LEFT_PARENTHESES),
                                    Symbol.createTerminator(NORMAL_MIDDLE_RIGHT_PARENTHESES)
                            )
                            , null
                    )
            ),
    };
}
