package com.github.liuyehcf.framework.flow.engine.model.gateway;

/**
 * An exclusive gateway (also called the XOR gateway or more technical the exclusive data-based gateway),
 * is used to model a decision in the flow. When the execution arrives at this gateway,
 * all outgoing conditions are evaluated in the order in which they are defined.
 *
 * @author hechenfeng
 * @date 2019/5/9
 */
public interface ExclusiveGateway extends Gateway {
}
