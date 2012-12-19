package com.github.jond3k

import csdl.CsdlParsers
import util.parsing.input.CharSequenceReader
import org.scalatest.matchers.MustMatchers
import util.parsing.combinator.RegexParsers
import org.scalatest.FlatSpec

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
trait ParserTestBase extends CsdlParsers with MustMatchers with RegexParsers  with FlatSpec {

  type Ex = IllegalArgumentException

  protected def assertFail[T](input: String)(implicit p: Parser[T]) {
    evaluating(parsing(input)(p)) must produce[IllegalArgumentException]
  }

  protected def parsing[T](s: String)(implicit p: Parser[T]): T = {
    //wrap the parser in the phrase parse to make sure all input is consumed
    val phraseParser = phrase(p)
    //we need to wrap the string in a reader so our parser can digest it
    val input = new CharSequenceReader(s)
    phraseParser(input) match {
      case Success(t, _) => t
      case NoSuccess(msg, _) => throw new IllegalArgumentException(
        "Could not parse '" + s + "': " + msg)
    }
  }
}
