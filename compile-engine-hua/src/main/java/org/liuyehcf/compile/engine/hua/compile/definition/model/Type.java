package org.liuyehcf.compile.engine.hua.compile.definition.model;

import java.io.Serializable;
import java.util.Objects;

import static org.liuyehcf.compile.engine.hua.compile.definition.Constant.*;

/**
 * 变量类型信息
 *
 * @author hechenfeng
 * @date 2018/6/12
 */
public class Type implements Serializable {

    public final static int REFERENCE_TYPE_WIDTH = 4;
    public final static Type TYPE_INT_ARRAY = createArrayType(NORMAL_INT, 1);
    public final static Type TYPE_INT_BOOLEAN = createArrayType(NORMAL_BOOLEAN, 1);
    public final static Type TYPE_CHAR_ARRAY = createArrayType(NORMAL_CHAR, 1);
    private final static int NORMAL_TYPE_DIM = 0;
    public final static Type TYPE_INT = createNormalType(NORMAL_INT, 4);
    public final static Type TYPE_BOOLEAN = createNormalType(NORMAL_BOOLEAN, 1);
    public final static Type TYPE_CHAR = createNormalType(NORMAL_CHAR, 2);
    public final static Type TYPE_VOID = createNormalType(NORMAL_VOID, 0);
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

    public static Type createNormalType(String typeName, int typeWidth) {
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
            case NORMAL_INT:
                if (dim == 0) {
                    return TYPE_INT;
                } else {
                    return createArrayType(NORMAL_INT, dim);
                }
            case NORMAL_BOOLEAN:
                if (dim == 0) {
                    return TYPE_BOOLEAN;
                } else {
                    createArrayType(NORMAL_BOOLEAN, dim);
                }
            default:
                throw new UnsupportedOperationException();
        }
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
        if (!isArrayType()) {
            throw new RuntimeException("Non-array type cannot perform dimensionality reduction");
        }

        if (this.dim == 1) {
            switch (this.typeName) {
                case NORMAL_BOOLEAN:
                    return TYPE_BOOLEAN;
                case NORMAL_CHAR:
                    return TYPE_CHAR;
                case NORMAL_INT:
                    return TYPE_INT;
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
