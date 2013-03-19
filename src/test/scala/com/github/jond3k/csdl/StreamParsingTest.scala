package com.github.jond3k.csdl

import ast.Stream
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class StreamParsingTest extends ParserTestBase {

  it must "parse stream expressions" in {
    implicit val parserToTest = expressions
    parsing("stream \"abcd\"") must equal(Stream("abcd"))
    parsing("rule \"abcd\"") must equal(Stream("abcd"))
  }

}
