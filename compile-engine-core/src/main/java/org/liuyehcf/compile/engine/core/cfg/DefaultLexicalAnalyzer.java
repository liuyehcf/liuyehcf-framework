package org.liuyehcf.compile.engine.core.cfg;

import org.liuyehcf.compile.engine.core.grammar.definition.MorphemeType;
import org.liuyehcf.compile.engine.core.grammar.definition.Symbol;
import org.liuyehcf.compile.engine.core.utils.SetUtils;

import java.io.Serializable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 词法分析器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public final class DefaultLexicalAnalyzer implements LexicalAnalyzer, Serializable {

    /**
     * 允许转义的字符
     */
    private static final Set<Character> ESCAPE_CHARS = SetUtils.of('a', 'b', 'f', 'n', 'r', 't', 'v', '\\', '\'', '"', '0');

    /**
     * Token对应的词素说明
     */
    private final List<Morpheme> morphemes;

    /**
     * 关键字集合
     */
    private final Map<String, Symbol> keyWords;

    /**
     * String对应的Symbol
     */
    private final Symbol stringId;

    /**
     * char对应的Symbol
     */
    private final Symbol charId;

    private DefaultLexicalAnalyzer(List<Morpheme> morphemes, Map<String, Symbol> keyWords, Symbol stringId, Symbol charId) {
        this.morphemes = morphemes;
        this.keyWords = keyWords;
        this.stringId = stringId;
        this.charId = charId;
    }

    @Override
    public TokenIterator iterator(String input) {
        return new DefaultTokenIterator(input);
    }

    private boolean isBlankChar(char c) {
        return 9 <= c && c <= 13 || c == 32;
    }

    public static class Builder {

        /**
         * token对应的regex，i<j,morphemes[i]的优先级高于tokenRegex[j]
         */
        private List<Morpheme> tokenRegex = new ArrayList<>();

        /**
         * 关键字集合
         */
        private Map<String, Symbol> keyWords = new HashMap<>();

        /**
         * String对应的Symbol
         */
        private Symbol stringId;

        /**
         * char对应的Symbol
         */
        private Symbol charId;

        private Builder() {

        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder addNormalMorpheme(Symbol id, String morpheme) {
            return addMorpheme(id, morpheme, MorphemeType.NORMAL);
        }

        public Builder addKeyWordMorpheme(Symbol id, String morpheme) {
            keyWords.put(morpheme, id);
            return addMorpheme(id, morpheme, MorphemeType.NORMAL);
        }

        public Builder addRegexMorpheme(Symbol id, String morpheme) {
            return addMorpheme(id, morpheme, MorphemeType.REGEX);
        }

        public Builder addStringMorpheme(Symbol id) {
            stringId = id;
            return this;
        }

        public Builder addCharMorpheme(Symbol id) {
            charId = id;
            return this;
        }

        private Builder addMorpheme(Symbol id, String morpheme, MorphemeType type) {
            tokenRegex.add(new Morpheme(id, morpheme, type));
            return this;
        }

        public DefaultLexicalAnalyzer build() {
            return new DefaultLexicalAnalyzer(tokenRegex, keyWords, stringId, charId);
        }

    }

    /**
     * 词素
     */
    private static final class Morpheme implements Serializable {
        /**
         * 当前词素对应的Symbol，与文法中的符号一一对应
         */
        final Symbol id;

        /**
         * 如果morphemeType是正则表达式类型，那么value就是正则表达式，否则就是普通字符串
         */
        final String value;

        /**
         * 词素类型
         */
        final MorphemeType morphemeType;

        /**
         * 仅用于正则表达式
         */
        final Pattern pattern;

        private Morpheme(Symbol id, String value, MorphemeType morphemeType) {
            this.id = id;
            this.value = value;
            this.morphemeType = morphemeType;
            if (!id.getType().equals(morphemeType)) {
                throw new IllegalStateException();
            }
            if (this.morphemeType == MorphemeType.REGEX) {
                this.pattern = Pattern.compile(value);
            } else {
                this.pattern = null;
            }
        }

        Symbol getId() {
            return id;
        }

        String getValue() {
            return value;
        }

        MorphemeType getMorphemeType() {
            return morphemeType;
        }

        Pattern getPattern() {
            return pattern;
        }
    }

    private final class DefaultTokenIterator implements TokenIterator {
        /**
         * 待扫描的输入序列
         */
        private final String input;

        /**
         * 当前扫描到的位置
         */
        private int index;

        /**
         * <p>null：尚未计算</p>
         * <p>true：还有后续Token</p>
         * <p>false：没有后续Token</p>
         */
        private Boolean hasNext;

        /**
         * 下一个Token
         */
        private Token nextToken;

        private DefaultTokenIterator(String input) {
            this.input = input;
            this.index = 0;
            this.hasNext = null;
            this.nextToken = null;
        }

        @Override
        public boolean hasNext() {
            if (hasNext == null) {
                hasNext = doHasNext();
            }
            return hasNext;
        }

        @Override
        public Token next() {
            Token token = nextToken;
            nextToken = null;
            hasNext = null;
            if (token == null) {
                throw new RuntimeException();
            }
            return token;
        }

        @Override
        public boolean reachesEof() {
            return input.length() == index;
        }

        private boolean doHasNext() {
            if (index >= input.length()) {
                return false;
            }

            boolean canBreak = false;

            while (!canBreak) {
                String remainInput = input.substring(index);
                if (charId != null && 0 < remainInput.length() && remainInput.charAt(0) == '\'') {
                    /*
                     * 如果是'\n'这种转义字符
                     */
                    if (1 < remainInput.length() && remainInput.charAt(1) == '\\') {
                        if (3 < remainInput.length() && ESCAPE_CHARS.contains(remainInput.charAt(2)) && remainInput.charAt(3) == '\'') {
                            canBreak = true;
                            hasNext = true;
                            nextToken = new Token(charId, remainInput.substring(0, 4));
                            index += 4;
                        } else {
                            canBreak = true;
                            hasNext = false;
                        }
                    }
                    /*
                     * 普通非'的字符
                     */
                    else if (2 < remainInput.length() && remainInput.charAt(1) != '\'' && remainInput.charAt(2) == '\'') {
                        canBreak = true;
                        hasNext = true;
                        nextToken = new Token(charId, remainInput.substring(0, 3));
                        index += 3;
                    }
                    /*
                     * 非法的字符
                     */
                    else {
                        canBreak = true;
                        hasNext = false;
                    }
                    continue;
                }

                if (stringId != null && 0 < remainInput.length() && remainInput.charAt(0) == '"') {
                    int i = 1;
                    boolean reachRight = false;
                    while (!reachRight && i < remainInput.length()) {
                        /*
                         * 转义字符
                         */
                        if (remainInput.charAt(i) == '\\') {
                            if (i + 1 >= remainInput.length() || !ESCAPE_CHARS.contains(remainInput.charAt(i + 1))) {
                                canBreak = true;
                                hasNext = false;
                                break;
                            }
                            i += 2;
                        }
                        /*
                         * "
                         */
                        else if (remainInput.charAt(i) == '"') {
                            reachRight = true;
                            canBreak = true;
                            hasNext = true;
                            nextToken = new Token(stringId, remainInput.substring(0, i + 1));
                            index += i + 1;
                        }
                        /*
                         * 普通字符
                         */
                        else {
                            i++;
                        }
                    }
                    continue;
                }

                for (Morpheme morpheme : morphemes) {

                    if (morpheme.getMorphemeType() == MorphemeType.REGEX) {
                        Matcher matcher = morpheme.getPattern().matcher(remainInput);
                        if (matcher.find()
                                && matcher.start(0) == 0) {
                            String s = matcher.group();

                            hasNext = true;
                            /*
                             * 如果仅为keyword，那么按照keyword处理
                             */
                            if (keyWords.containsKey(s)) {
                                nextToken = new Token(keyWords.get(s), s);
                            } else {
                                nextToken = new Token(morpheme.getId(), s);
                            }
                            index += s.length();

                            canBreak = true;
                            break;
                        }
                    } else {
                        if (remainInput.startsWith(morpheme.getValue())) {
                            hasNext = true;
                            nextToken = new Token(morpheme.getId(), morpheme.getValue());
                            index += morpheme.getValue().length();

                            canBreak = true;
                            break;
                        }
                    }
                }
                /*
                 * 如果所有词素的正则表达式均不匹配，且当前字符是空白符号，那么允许跳过
                 */
                if (hasNext == null) {
                    if (isBlankChar(input.charAt(index))) {
                        index++;
                        if (index == input.length()) {
                            canBreak = true;
                        }
                    } else {
                        canBreak = true;
                        hasNext = false;
                    }
                }
            }

            if (hasNext == null) {
                hasNext = false;
            }

            return hasNext;
        }
    }
}
