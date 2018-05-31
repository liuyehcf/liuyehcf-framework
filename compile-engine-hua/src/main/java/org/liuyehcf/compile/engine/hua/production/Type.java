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

    public static final String TYPE = "<type>";
    public static final String PRIMITIVE_TYPE = "<primitive type>";
    public static final String REFERENCE_TYPE = "<reference type>";
    public static final String NUMERIC_TYPE = "<numeric type>";
    public static final String INTEGRAL_TYPE = "<integral type>";
    public static final String FLOATING_POINT_TYPE = "<floating-point type>";
    public static final String ARRAY_TYPE = "<array type>";

    public static final String NORMAL_BOOLEAN = "boolean";
    public static final String NORMAL_INT = "int";
    public static final String NORMAL_FLOAT = "float";

    public static final Production[] PRODUCTIONS = {
            /*
             * <type>
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
             * <primitive type>
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
             * <numeric type>
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
             * <integral type>
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
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <floating-point type>
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
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <reference type>
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
                    // TODO 可以扩展更为复杂的语法
            ),


            /*
             * <array type>
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
                    // TODO 可以扩展更为复杂的语法
            ),
    };
}
