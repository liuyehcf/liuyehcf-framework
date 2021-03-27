package com.github.liuyehcf.framework.flow.engine.model;

import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;

import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/4/23
 */
public interface Node extends Element {

    /**
     * add listener of current node
     *
     * @param listener listener
     */
    void addListener(Listener listener);

    /**
     * remove listener of current node
     *
     * @param listener listener
     */
    void removeListener(Listener listener);

    /**
     * add predecessor node of current node
     *
     * @param node predecessor node
     */
    void addPredecessor(Node node);

    /**
     * remove predecessor node of current node
     *
     * @param node predecessor node
     */
    void removePredecessor(Node node);

    /**
     * add successor node of current node
     *
     * @param node     successor node
     * @param linkType linkType
     */
    void addSuccessor(Node node, LinkType linkType);

    /**
     * remove successor node of current node
     *
     * @param node successor node
     */
    void removeSuccessor(Node node);

    /**
     * listeners of this node
     *
     * @return listeners
     */
    List<Listener> getListeners();

    /**
     * predecessor nodes of this node
     *
     * @return predecessor nodes
     */
    List<Node> getPredecessors();

    /**
     * successor nodes of this node
     *
     * @return successor nodes
     */
    List<Node> getSuccessors();

    /**
     * get successors of link type
     *
     * @param linkType link type
     * @return successor nodes
     */
    List<Node> getSuccessors(LinkType linkType);

    /**
     * get link type of specified successor
     *
     * @param node successor
     * @return link type
     */
    LinkType getLinkType(Node node);
}
