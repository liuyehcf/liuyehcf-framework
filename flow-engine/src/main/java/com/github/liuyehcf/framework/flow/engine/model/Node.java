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
     * @param predecessor predecessor node
     */
    void addPredecessor(Node predecessor);

    /**
     * remove predecessor node of current node
     *
     * @param predecessor predecessor node
     */
    void removePredecessor(Node predecessor);

    /**
     * add successor node of current node
     *
     * @param successor successor node
     * @param linkType  linkType
     */
    void addSuccessor(Node successor, LinkType linkType);

    /**
     * remove successor node of current node
     *
     * @param successor successor node
     */
    void removeSuccessor(Node successor);

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
    List<Node> getSuccessorsOf(LinkType linkType);

    /**
     * get link type of specified successor
     *
     * @param successor successor
     * @return link type
     */
    LinkType getLinkTypeOf(Node successor);
}
