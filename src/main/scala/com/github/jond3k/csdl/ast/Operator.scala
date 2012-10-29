package com.github.jond3k.csdl.ast

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
case class Operator(operator: String, cs: Boolean = false) extends CsdlNode
