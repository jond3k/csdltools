package com.github.jond3k.csdl.ast

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
case class Text(value: String) extends Argument

object Text {
  /**
   * Unquote and unescape a string
   */
  def fromQuoted(value: String) = {
    if (value.length < 2) throw new IllegalArgumentException("Invalid quoting style for %s" format value)

    val start  = value(0)
    val end    = value(value.length - 1)
    val middle = value.substring(1, value.length-1)

    if (start != '"' && start != '\'') throw new IllegalArgumentException("Unrecoginsed quote style %s for %s" format (start, value))
    if (end != start) throw new IllegalArgumentException("Began with %s but ended with %s for %s" format(start, end, value))

    val escaping  = "\\" + start
    val unescaped = middle.replace(escaping, start.toString)

    new Text(unescaped)
  }
}