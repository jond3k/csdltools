package com.github.jond3k.csdl

import util.parsing.combinator.RegexParsers
import util.parsing.json.Parser



/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class CsdlParsers extends RegexParsers {

  val csdl = exp_r

  val exp_r = disj

  val disj = conj ~ "or" ~ disj | conj

  val conj = "and" ~ exp | exp | exp

  val exp = /*"(" ~ expressions ~ ")" |*/ "not" ~ exp | target ~ target_op ~ matcher

  val matcher = "a, b, c"

  val target = """(?i)[\w\._]+""".r

  val target_op = "==" | "contains"

}
