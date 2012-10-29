package com.github.jond3k.csdl

import ast._
import util.parsing.combinator.RegexParsers
import com.github.jond3k.CaseInsensitivePattern

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class CsdlParsers extends RegexParsers {

  implicit def insensitivity(str: String): CaseInsensitivePattern = new CaseInsensitivePattern(str)

  def body: Parser[Expression] = tagging | expressions

  def tagging: Parser[Tags] = (tag*) ~ returns ^^ {
    s => new Tags(s._1, s._2)
  }

  def returns: Parser[Returns] = "return".ri ~ "{" ~ expressions ~ "}" ^^ {
    s => new Returns(s._1._2)
  }

  def tag: Parser[Tag] = "tag".ri ~ doubleQuotedText ~ "{" ~ expressions ~ "}" ^^ {
    s => new Tag(s._1._1._1._2.value, s._1._2)
  }

  def expressions: Parser[Expression] =
    disjunction

  def disjunction: Parser[Expression] =
    conjunction ~ "or".ri ~ disjunction ^^ {
      s => new Or(s._1._1, s._2)
    } |
    conjunction

  def conjunction: Parser[Expression] =
    expression ~ "and".ri ~ conjunction ^^ {
      s => new And(s._1._1, s._2)
    } |
    expression

  def expression: Parser[Expression] = rule |
                                       stream |
                                       negation |
                                       grouped

  def grouped: Parser[Expression] = "(" ~ expressions ~ ")" ^^ {
    s => s._1._2
  }

  def negation: Parser[Expression] = "not".ri ~ expression ^^ {
    s => new Not(s._2)
  }

  def rule: Parser[Rule] = target ~ binaryOperator ~ argument ^^ {
                             s => Rule(s._1._1, s._1._2.caseInsensitive, s._2)
                           } |
                           target ~ "cs".ri ~ binaryOperator ~ argument ^^ {
                             s => Rule(s._1._1._1, s._1._2.caseSensitive, s._2)
                           } |
                           target ~ unitaryOperator ^^ {
                             s => Rule(s._1, s._2, null)
                           }

  def target: Parser[Target] = """[\w\._]+""".r ^^ {
    s => new Target(s)
  }

  def stream: Parser[Stream] = "stream".ri ~ doubleQuotedText ^^ {
    s => new Stream(s._2.value)
  }

  def unitaryOperator: Parser[Operator] = List("exists").mkString("|").ri ^^ {
    s => new Operator(s.toLowerCase)
  }

  def binaryOperator: Parser[Operator] = List("contains",
    "substr",
    "contains_any",
    "contains_near",
    "in",
    "==",
    "!=",
    ">",
    ">=",
    "<",
    "<=",
    "regex_partial",
    "regex_exact",
    "geo_box",
    "geo_radius",
    "geo_polygon").mkString("|").ri ^^ {
    s => new Operator(s.toLowerCase)
  }

  def argument: Parser[Argument] = text | textList ^^ {
    s => s
  }

  def textList: Parser[TextList] = "[" ~ textListBody ~ "]" ^^ {
    s => new TextList(s._1._2)
  }

  def textListBody: Parser[List[Text]] = text ~ "," ~ textListBody ^^ {
    s => List(s._1._1) ++ s._2
  } |
  text ^^ {
    s => List(s)
  }

  def unquotedText: Parser[Text] = """\b\S+\b""".r ^^ {
    s => new Text(s)
  }

  def text: Parser[Text] = doubleQuotedText | singleQuotedText | unquotedText

  def doubleQuotedText: Parser[Text] = """"[^"\\]*(?:\\.[^"\\]*)*"""".r ^^ {
    s => Text.fromQuoted(s)
  }

  def singleQuotedText: Parser[Text] = """'[^'\\]*(?:\\.[^'\\]*)*'""".r ^^ {
    s => Text.fromQuoted(s)
  }

}
