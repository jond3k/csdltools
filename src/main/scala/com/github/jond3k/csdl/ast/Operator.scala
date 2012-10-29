package com.github.jond3k.csdl.ast

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
case class Operator(operator: String, cs: Boolean = false) extends CsdlNode {
  def caseSensitive: Operator = Operator(this.operator, cs = true)
  def caseInsensitive: Operator = Operator(this.operator, cs = false)
}
