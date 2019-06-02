package com.github.liuyehcf.framework.compile.engine.grammar.converter;

import com.github.liuyehcf.framework.compile.engine.grammar.definition.Grammar;
import com.github.liuyehcf.framework.compile.engine.grammar.definition.Symbol;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 文法转换器抽象基类
 *
 * @author hechenfeng
 * @date 2018/04/16
 */
public abstract class AbstractGrammarConverter implements GrammarConverter, Serializable {

    /**
     * 待转换的文法
     */
    protected final Grammar originalGrammar;

    /**
     * 转换后的文法
     */
    private Grammar convertedGrammar;

    AbstractGrammarConverter(Grammar originalGrammar) {
        this.originalGrammar = originalGrammar;
    }

    static void traverseDirectedGraph(Map<Symbol, List<Symbol>> edges, Map<Symbol, Integer> degrees, List<Symbol> visitedSymbol) {
        Queue<Symbol> queue = new LinkedList<>();

        for (Map.Entry<Symbol, Integer> entry : degrees.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        while (!queue.isEmpty()) {
            Symbol curSymbol = queue.poll();
            visitedSymbol.add(curSymbol);

            /*
             * 有向邻接节点
             */
            List<Symbol> adjList = edges.get(curSymbol);

            for (Symbol adjSymbol : adjList) {
                degrees.put(adjSymbol, degrees.get(adjSymbol) - 1);
                /*
                 * 度为0，可以访问
                 */
                if (degrees.get(adjSymbol) == 0) {
                    queue.offer(adjSymbol);
                }
            }
        }
    }

    @Override
    public final Grammar getConvertedGrammar() {
        if (convertedGrammar == null) {
            convertedGrammar = doConvert();
        }
        return convertedGrammar;
    }

    /**
     * 文法转换的具体操作，交由子类实现
     *
     * @return 转换后的文法
     */
    protected abstract Grammar doConvert();
}
