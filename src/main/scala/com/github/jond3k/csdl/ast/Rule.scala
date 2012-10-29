package com.github.jond3k.csdl.ast


/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
case class Rule(target: Target, operator: Operator, argument: Argument) extends Expression