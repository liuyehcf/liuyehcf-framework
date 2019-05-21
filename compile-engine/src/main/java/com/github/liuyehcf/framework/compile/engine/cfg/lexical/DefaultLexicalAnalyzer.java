package com.github.liuyehcf.framework.compile.engine.cfg.lexical;

import com.github.liuyehcf.framework.compile.engine.cfg.lexical.identifier.TokenIdentifier;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.MorphemeType;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.liuyehcf.framework.compile.engine.utils.CharacterUtil.isBlankChar;

/**
 * 词法分析器
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public final class DefaultLexicalAnalyzer implements LexicalAnalyzer, Serializable {

    /**
     * Token对应的词素说明
     */
    private final List<Morpheme> morphemes;

    /**
     * 关键字集合
     */
    private final Map<String, Symbol> keyWords;

    private DefaultLexicalAnalyzer(List<Morpheme> morphemes, Map<String, Symbol> keyWords) {
        this.morphemes = morphemes;
        this.keyWords = keyWords;
    }

    @Override
    public TokenIterator iterator(String input) {
        return new DefaultTokenIterator(input);
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

        private Builder() {

        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder addNormalMorpheme(Symbol id, String morpheme) {
            return addMorpheme(id, morpheme, MorphemeType.NORMAL, null);
        }

        public Builder addKeyWordMorpheme(Symbol id, String morpheme) {
            keyWords.put(morpheme, id);
            return addMorpheme(id, morpheme, MorphemeType.NORMAL, null);
        }

        public Builder addRegexMorpheme(Symbol id, String morpheme) {
            return addMorpheme(id, morpheme, MorphemeType.REGEX, null);
        }

        public Builder addTokenOperator(Symbol id, TokenIdentifier tokenIdentifier) {
            return addMorpheme(id, null, MorphemeType.OPERATOR, tokenIdentifier);
        }

        private Builder addMorpheme(Symbol id, String morpheme, MorphemeType type, TokenIdentifier tokenIdentifier) {
            tokenRegex.add(new Morpheme(id, morpheme, type, tokenIdentifier));
            return this;
        }

        public DefaultLexicalAnalyzer build() {
            return new DefaultLexicalAnalyzer(tokenRegex, keyWords);
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

        /**
         * Token处理逻辑
         */
        final TokenIdentifier tokenIdentifier;

        private Morpheme(Symbol id, String value, MorphemeType morphemeType, TokenIdentifier tokenIdentifier) {
            this.id = id;
            this.value = value;
            this.morphemeType = morphemeType;
            this.tokenIdentifier = tokenIdentifier;
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

        TokenIdentifier getTokenIdentifier() {
            return tokenIdentifier;
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

        @Override
        public int position() {
            return index;
        }

        private boolean doHasNext() {
            if (index >= input.length()) {
                return false;
            }

            boolean canBreak = false;

            while (!canBreak) {
                String remainInput = input.substring(index);

                for (Morpheme morpheme : morphemes) {

                    if (morpheme.getMorphemeType() == MorphemeType.OPERATOR) {
                        TokenContext tokenContext = new TokenContext(morpheme.getId(), remainInput);
                        Token token = morpheme.getTokenIdentifier().identify(tokenContext);

                        if (token != null) {
                            hasNext = true;
                            nextToken = token;
                            index += tokenContext.getMoveLength();

                            canBreak = true;
                            break;
                        }

                    } else if (morpheme.getMorphemeType() == MorphemeType.REGEX) {
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
