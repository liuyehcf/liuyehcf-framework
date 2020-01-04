package com.github.liuyehcf.framework.flow.engine.model;

import com.github.liuyehcf.framework.flow.engine.model.event.Event;
import com.github.liuyehcf.framework.flow.engine.model.listener.Listener;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author hechenfeng
 * @date 2019/5/6
 */
public interface Flow extends Node, Serializable {

    /**
     * return flow name
     *
     * @return flow name
     */
    String getName();

    /**
     * return flow id
     *
     * @return flow id
     */
    String getId();

    /**
     * return start node of flow
     *
     * @return start node
     */
    Start getStart();

    /**
     * return all the end nodes of flow
     *
     * @return end nodes
     */
    List<Node> getEnds();

    /**
     * add listener to flow
     *
     * @param listener the specified listener
     */
    void addListener(Listener listener);

    /**
     * remove listener from flow
     *
     * @param listener the specified listener
     */
    void removeListener(Listener listener);

    /**
     * get all the listeners of flow
     * including node level and global level
     *
     * @return listeners
     */
    List<Listener> getListeners();

    /**
     * add event to flow
     *
     * @param event the specified event
     */
    void addEvent(Event event);

    /**
     * remove event from flow
     *
     * @param event the specified event
     */
    void removeEvent(Event event);

    /**
     * get all the events of flow
     *
     * @return events
     */
    List<Event> getEvents();

    /**
     * add element to flow
     *
     * @param element the specified element
     */
    void addElement(Element element);

    /**
     * remove element from flow
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
     * get all the elements of flow
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
