package com.github.liuyehcf.framework.language.hua.compile.definition.model;

import java.io.Serializable;
import java.util.Objects;

import static com.github.liuyehcf.framework.common.tools.asserts.Assert.assertTrue;
import static com.github.liuyehcf.framework.language.hua.compile.definition.Constant.*;

/**
 * 变量类型信息
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class Type implements Serializable {

    /**
     * 基本类型字长
     */
    public static final int VOID_TYPE_WIDTH = 0;
    public static final int BOOLEAN_TYPE_WIDTH = 1;
    public static final int CHAR_TYPE_WIDTH = 2;
    public static final int INT_TYPE_WIDTH = 4;
    public static final int LONG_TYPE_WIDTH = 8;
    public static final int FLOAT_TYPE_WIDTH = 4;
    public static final int DOUBLE_TYPE_WIDTH = 8;
    public static final int REFERENCE_TYPE_WIDTH = 8;
    /**
     * 基本类型的一维数组
     */
    public static final Type TYPE_BOOLEAN_ARRAY = createArrayType(NORMAL_BOOLEAN, 1);
    public static final Type TYPE_CHAR_ARRAY = createArrayType(NORMAL_CHAR, 1);
    public static final Type TYPE_INT_ARRAY = createArrayType(NORMAL_INT, 1);
    public static final Type TYPE_LONG_ARRAY = createArrayType(NORMAL_LONG, 1);
    public static final Type TYPE_FLOAT_ARRAY = createArrayType(NORMAL_FLOAT, 1);
    public static final Type TYPE_DOUBLE_ARRAY = createArrayType(NORMAL_DOUBLE, 1);
    public static final Type TYPE_STRING_ARRAY = createArrayType(NORMAL_CHAR, 2);
    private static final int NORMAL_TYPE_DIM = 0;
    /**
     * 基本类型
     */
    public static final Type TYPE_VOID = createNormalType(NORMAL_VOID, VOID_TYPE_WIDTH);
    public static final Type TYPE_BOOLEAN = createNormalType(NORMAL_BOOLEAN, BOOLEAN_TYPE_WIDTH);
    public static final Type TYPE_CHAR = createNormalType(NORMAL_CHAR, CHAR_TYPE_WIDTH);
    public static final Type TYPE_INT = createNormalType(NORMAL_INT, INT_TYPE_WIDTH);
    public static final Type TYPE_LONG = createNormalType(NORMAL_LONG, LONG_TYPE_WIDTH);
    public static final Type TYPE_FLOAT = createNormalType(NORMAL_FLOAT, FLOAT_TYPE_WIDTH);
    public static final Type TYPE_DOUBLE = createNormalType(NORMAL_DOUBLE, DOUBLE_TYPE_WIDTH);
    private static final String ARRAY_DIM_DESCRIPTION = "[]";

    /**
     * 类型名称
     */
    private final String typeName;

    /**
     * 类型长度
     */
    private final int typeWidth;

    /**
     * 维度，非数组类型该字段值为0
     */
    private final int dim;

    public Type(String typeName, int typeWidth, int dim) {
        this.typeName = typeName;
        this.typeWidth = typeWidth;
        this.dim = dim;
    }

    private static Type createNormalType(String typeName, int typeWidth) {
        return new Type(typeName, typeWidth, NORMAL_TYPE_DIM);
    }

    public static Type createArrayType(String typeName, int dim) {
        return new Type(typeName, REFERENCE_TYPE_WIDTH, dim);
    }

    public static Type createType(String typeName, int typeWidth, int dim) {
        return new Type(typeName, typeWidth, dim);
    }

    public static Type parse(String typeDescription) {
        int dim = 0;
        String remain = typeDescription;
        while (remain.endsWith(ARRAY_DIM_DESCRIPTION)) {
            dim++;
            remain = remain.substring(0, remain.length() - ARRAY_DIM_DESCRIPTION.length());
        }

        switch (remain) {
            case NORMAL_BOOLEAN:
                if (dim == 0) {
                    return TYPE_BOOLEAN;
                } else {
                    return createArrayType(NORMAL_BOOLEAN, dim);
                }
            case NORMAL_CHAR:
                if (dim == 0) {
                    return TYPE_CHAR;
                } else {
                    return createArrayType(NORMAL_CHAR, dim);
                }
            case NORMAL_INT:
                if (dim == 0) {
                    return TYPE_INT;
                } else {
                    return createArrayType(NORMAL_INT, dim);
                }
            case NORMAL_LONG:
                if (dim == 0) {
                    return TYPE_LONG;
                } else {
                    return createArrayType(NORMAL_LONG, dim);
                }
            case NORMAL_FLOAT:
                if (dim == 0) {
                    return TYPE_FLOAT;
                } else {
                    return createArrayType(NORMAL_FLOAT, dim);
                }
            case NORMAL_DOUBLE:
                if (dim == 0) {
                    return TYPE_DOUBLE;
                } else {
                    return createArrayType(NORMAL_DOUBLE, dim);
                }
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static boolean isCompatible(Type mainType, Type subType) {
        /*
         * int兼容char
         */
        if (TYPE_INT.equals(mainType)) {
            return TYPE_INT.equals(subType)
                    || TYPE_CHAR.equals(subType);
        }
        /*
         * long兼容int、char
         */
        else if (TYPE_LONG.equals(mainType)) {
            return TYPE_LONG.equals(subType)
                    || TYPE_INT.equals(subType)
                    || TYPE_CHAR.equals(subType);
        }
        /*
         * float兼容int、char、long
         */
        else if (TYPE_FLOAT.equals(mainType)) {
            return TYPE_FLOAT.equals(subType)
                    || TYPE_LONG.equals(subType)
                    || TYPE_INT.equals(subType)
                    || TYPE_CHAR.equals(subType);
        }
        /*
         * double兼容int、char、long、float
         */
        else if (TYPE_DOUBLE.equals(mainType)) {
            return TYPE_DOUBLE.equals(subType)
                    || TYPE_FLOAT.equals(subType)
                    || TYPE_LONG.equals(subType)
                    || TYPE_INT.equals(subType)
                    || TYPE_CHAR.equals(subType);
        }

        return mainType.equals(subType);
    }

    public String getTypeName() {
        return typeName;
    }

    public int getTypeWidth() {
        return typeWidth;
    }

    public int getDim() {
        return dim;
    }

    public boolean isArrayType() {
        return dim != NORMAL_TYPE_DIM;
    }

    public Type toDimDecreasedType() {
        assertTrue(isArrayType(), "[SYSTEM_ERROR] - Non-array type cannot perform dimensionality reduction");

        if (this.dim == 1) {
            switch (this.typeName) {
                case NORMAL_BOOLEAN:
                    return TYPE_BOOLEAN;
                case NORMAL_CHAR:
                    return TYPE_CHAR;
                case NORMAL_INT:
                    return TYPE_INT;
                case NORMAL_LONG:
                    return TYPE_LONG;
                case NORMAL_FLOAT:
                    return TYPE_FLOAT;
                case NORMAL_DOUBLE:
                    return TYPE_DOUBLE;
                default:
                    throw new UnsupportedOperationException();
            }
        }
        return new Type(this.typeName, this.typeWidth, this.dim - 1);
    }

    public Type toDimIncreasedType() {
        if (this.dim == 0) {
            return createArrayType(this.typeName, this.dim + 1);
        }
        return new Type(this.typeName, this.typeWidth, this.dim + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return typeWidth == type.typeWidth &&
                dim == type.dim &&
                Objects.equals(typeName, type.typeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeName, typeWidth, dim);
    }

    public String toTypeDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(typeName);
        for (int i = 0; i < dim; i++) {
            sb.append(ARRAY_DIM_DESCRIPTION);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Type{" +
                "typeName='" + typeName + '\'' +
                ", typeWidth=" + typeWidth +
                ", dim=" + dim +
                '}';
    }
}
