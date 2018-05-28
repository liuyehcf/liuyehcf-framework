package org.liuyehcf.compile.engine.core.utils;

/**
 * Tuple
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Tuple<FIRST, SECOND, THIRD> {
    private final FIRST first;

    private final SECOND second;

    private final THIRD third;

    public Tuple(FIRST first, SECOND second, THIRD third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public FIRST getFirst() {
        return first;
    }

    public SECOND getSecond() {
        return second;
    }

    public THIRD getThird() {
        return third;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        if (first != null) {
            hash += first.hashCode();
        }

        if (second != null) {
            hash += second.hashCode();
        }

        if (third != null) {
            hash += third.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple that = (Tuple) obj;

            if (that.first != null && that.second != null && that.third != null) {
                return that.first.equals(this.first)
                        && that.second.equals(this.second)
                        && that.third.equals(this.third);
            } else if (that.first != null && that.second != null) {
                return this.third == null
                        && that.first.equals(this.first)
                        && that.second.equals(this.second);
            } else if (that.first != null && that.third != null) {
                return this.second == null
                        && that.first.equals(this.first)
                        && that.third.equals(this.third);
            } else if (that.second != null && that.third != null) {
                return this.first == null
                        && that.second.equals(this.second)
                        && that.third.equals(this.third);
            } else if (that.first != null) {
                return this.second == null
                        && this.third == null
                        && that.first.equals(this.first);
            } else if (that.second != null) {
                return this.first == null
                        && this.third == null
                        && that.second.equals(this.second);
            } else if (that.third != null) {
                return this.first == null
                        && this.second == null
                        && that.third.equals(this.third);
            } else {
                return this.first == null && this.second == null && this.third == null;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}
