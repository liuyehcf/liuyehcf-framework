package com.github.liuyehcf.framework.rule.engine.model;

import com.github.liuyehcf.framework.rule.engine.model.event.Event;
import com.github.liuyehcf.framework.rule.engine.model.listener.Listener;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public interface Rule extends Node, Serializable {

    /**
     * return rule name
     *
     * @return rule name
     */
    String getName();

    /**
     * return rule id
     *
     * @return rule id
     */
    String getId();

    /**
     * return start node of rule
     *
     * @return start node
     */
    Start getStart();

    /**
     * return all the end nodes of rule
     *
     * @return end nodes
     */
    List<Node> getEnds();

    /**
     * add listener to rule
     *
     * @param listener the specified listener
     */
    void addListener(Listener listener);

    /**
     * remove listener from rule
     *
     * @param listener the specified listener
     */
    void removeListener(Listener listener);

    /**
     * get all the listeners of rule
     * including node level and global level
     *
     * @return listeners
     */
    List<Listener> getListeners();

    /**
     * add event to rule
     *
     * @param event the specified event
     */
    void addEvent(Event event);

    /**
     * remove event from rule
     *
     * @param event the specified event
     */
    void removeEvent(Event event);

    /**
     * get all the events of rule
     *
     * @return events
     */
    List<Event> getEvents();

    /**
     * add element to rule
     *
     * @param element the specified element
     */
    void addElement(Element element);

    /**
     * remove element from rule
     *
     * @param element the specified element
     */
    void removeElement(Element element);

    /**
     * get element of specified id
     *
     * @return element
     */
    Element getElement(String id);

    /**
     * get all the elements of rule
     * including action condition gateway listener
     *
     * @return elements
     */
    Collection<Element> getElements();

    /**
     * init operations including kinds of check
     */
    void init();
}
