package com.github.jond3k.powertrack.ast

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class Text(val value: String) extends Expression

object Text {
  def fromQuoted(value: String) = new Text(value) // TODO
}