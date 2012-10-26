package com.github.jond3k.powertrack.ast

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class HashTag(val value: String) extends Expression {
  val twitterPrefix = "#"

  if (!value.startsWith(twitterPrefix))
    throw new IllegalArgumentException("%s did not begin with %s" format(value, twitterPrefix))

  lazy val withoutPrefix = value.substring(twitterPrefix.length)

}
