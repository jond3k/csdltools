package com.github.jond3k.csdl

import ast._
import util.parsing.combinator.RegexParsers
import com.github.jond3k.CaseInsensitivePattern
import util.parsing.input.CharSequenceReader

/**
 * Parsers for the CSDL language
 *
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class CsdlParsers extends RegexParsers {

  protected override val whiteSpace = """(\s|//.*|(?m)/\*(\*(?!/)|[^*])*\*/)+""".r

  implicit def insensitivity(str: String): CaseInsensitivePattern = new CaseInsensitivePattern(str)

  def body: Parser[CsdlBody] = taggedBody | expressions

  def taggedBody: Parser[CsdlTaggedBody] = (tag*) ~ returns ^^ {
    s => new CsdlTaggedBody(s._1, s._2)
  }

  def returns: Parser[Returns] = "return".rbi ~ "{" ~ expressions ~ "}" ^^ {
    s => new Returns(s._1._2)
  }

  def tag: Parser[Tag] = "tag".rbi ~ doubleQuotedText ~ "{" ~ expressions ~ "}" ^^ {
    s => new Tag(s._1._1._1._2.value, s._1._2)
  }

  def expressions: Parser[CsdlBody] =
    disjunction

  def disjunction: Parser[CsdlBody] =
    conjunction ~ "or".rbi ~ disjunction ^^ {
      s => new Or(s._1._1, s._2)
    } |
    conjunction

  def conjunction: Parser[CsdlBody] =
    expression ~ "and".rbi ~ conjunction ^^ {
      s => new And(s._1._1, s._2)
    } |
    expression

  def expression: Parser[CsdlBody] = negation |
                                     rule |
                                     stream |
                                     grouped

  def grouped: Parser[CsdlBody] = "(" ~ expressions ~ ")" ^^ {
    s => s._1._2
  }

  def negation: Parser[CsdlBody] = "not".rbi ~ expressions ^^ {
    s => new Not(s._2)
  }

  def rule: Parser[Rule] = target ~ binaryOperatorType ~ argument ^^ {
                             s => Rule(s._1._1, Operator(s._1._2, cs=false), s._2)
                           } |
                           target ~ "cs".rbi ~ binaryOperatorType ~ argument ^^ {
                             s => Rule(s._1._1._1, Operator(s._1._2, cs=true), s._2)
                           } |
                           target ~ unitaryOperatorType ^^ {
                             s => Rule(s._1, Operator(s._2, cs=false), null)
                           }

  def target: Parser[Target] = """[\w\._]+""".r ^^ {
    s => new Target(s)
  }

  def stream: Parser[Stream] = "stream".ri ~ doubleQuotedText ^^ {
    s => new Stream(s._2.value)
  }

  def unitaryOperatorType: Parser[String] = unitaryOperatorRegex ^^ {
    _.toLowerCase
  }

  val unitaryOperatorList: List[String] = List("exists".b)

  val unitaryOperatorRegex = orMatchRegex(unitaryOperatorList).ri

  val binaryOperatorList: List[String] = List(
    "contains".b,
    "substr".b,
    "contains_any".b,
    "any".b,
    "contains_near".b,
    "any".b,
    "in".b,
    "==",
    "!=",
    ">",
    ">=",
    "<",
    "<=",
    "regex_partial".b,
    "regex_exact".b,
    "geo_box".b,
    "geo_radius".b,
    "geo_polygon".b)

  protected def orMatchRegex(list: List[String]) = list.mkString("|")

  val binaryOperatorRegex = orMatchRegex(binaryOperatorList).ri

  def binaryOperatorType: Parser[String] = binaryOperatorRegex ^^ {
    _.toLowerCase
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

  def unquotedText: Parser[Text] = "\\S+".b.r ^^ {
    s => new Text(s)
  }

  def text: Parser[Text] = doubleQuotedText | singleQuotedText | unquotedText

  def doubleQuotedText: Parser[Text] = """"[^"\\]*(?:\\.[^"\\]*)*"""".r ^^ {
    s => Text.fromQuoted(s)
  }

  def singleQuotedText: Parser[Text] = """'[^'\\]*(?:\\.[^'\\]*)*'""".r ^^ {
    s => Text.fromQuoted(s)
  }

  def parse[T](s: String)(implicit p: Parser[T]): T = {
    //wrap the parser in the phrase parse to make sure all input is consumed
    val phraseParser = phrase(p)
    //we need to wrap the string in a reader so our parser can digest it
    val input = new CharSequenceReader(s)
    phraseParser(input) match {
      case Success(t,_)     => t
      case NoSuccess(msg,_) => throw new IllegalArgumentException(
        "Could not parse '" + s + "': " + msg)
    }
  }
}
