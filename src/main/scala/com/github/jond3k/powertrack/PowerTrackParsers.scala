package com.github.jond3k.powertrack

import ast._
import util.parsing.combinator.RegexParsers

/**
 *
 * Grammar for http://support.gnip.com/customer/portal/articles/600544-twitter-powertrack-operators
 *
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class PowerTrackParsers extends RegexParsers {

  def body: Parser[Expression] =
    expressions

  def expressions: Parser[Expression] =
    disjunction

  def disjunction: Parser[Expression] =
    conjunction ~ "or" ~ disjunction ^^ {
      s => new Or(s._1._1, s._2)
    } |
      conjunction

  def conjunction: Parser[Expression] =
    conjunction ~ "and" ~ expression ^^ {
      s => new And(s._1._1, s._2)
    } |
    conjunction ~ expression ^^ {
      s => new And(s._1, s._2)
    } |
    expression

  def expression: Parser[Expression] =
    expression ~ "(" ~ expressions ~ ")" ^^ {
      s => new And(s._1._1._1, s._1._2)
    } |
      negation |
      command |
      hashTag |
      username |
      text

  def negation = "-" ~ expression ^^ {
    s => new Not(s._2)
  }

  def command: Parser[Command] = commandName ~ ":" ~ commandValue ^^ {
    s => new Command(s._1._1, s._2)
  }

  def commandName: Parser[Text] = unquotedText

  def commandValue: Parser[Text] = text

  def text: Parser[Text] = doubleQuotedText | singleQuotedText | unquotedText

  def doubleQuotedText: Parser[Text] = """"[^"\\]*(?:\\.[^"\\]*)*"""".r ^^ {
    s => Text.fromQuoted(s)
  }

  def singleQuotedText: Parser[Text] = """'[^'\\]*(?:\\.[^'\\]*)*'""".r ^^ {
    s => Text.fromQuoted(s)
  }

  def unquotedText: Parser[Text] = """\b\S+\b""".r ^^ {
    s => new Text(s)
  }

  def hashTag: Parser[HashTag] = """#\w+""".r ^^ {
    s => new HashTag(s)
  }

  def username: Parser[Username] = """@\w+""".r ^^ {
    s => new Username(s)
  }

}
