package com.github.jond3k.csdl

import ast.{Text, TextList}
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class ListParsingTest extends ParserTestBase {

  it must "parse text lists" in {
    implicit val parserToTest = textList
    parsing("[123, 456]") must equal(TextList(List(Text("123"), Text("456"))))
    parsing("[123, \"abc\"]") must equal(TextList(List(Text("123"), Text("abc"))))
    parsing("[123, \"ab\\\"c\"]") must equal(TextList(List(Text("123"), Text("ab\"c"))))
    assertFail("[]")
    assertFail("[")
    assertFail("[123,]")
  }
}
