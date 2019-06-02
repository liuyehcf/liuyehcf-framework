package com.github.liuyehcf.framework.compile.engine.utils;

/**
 * 工具类Pair
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public class Pair<FIRST, SECOND> {

    private final FIRST first;
    private final SECOND second;

    public Pair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    public FIRST getFirst() {
        return first;
    }

    public SECOND getSecond() {
        return second;
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
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair that = (Pair) obj;

            if (that.first != null && that.second != null) {
                return that.first.equals(this.first)
                        && that.second.equals(this.second);
            } else if (that.first != null) {
                return this.second == null
                        && that.first.equals(this.first);
            } else if (that.second != null) {
                return this.first == null
                        && that.second.equals(this.second);
            } else {
                return this.first == null && this.second == null;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
