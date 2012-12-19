package com.github.jond3k.csdl

import ast.Text
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class StringParsingTest extends ParserTestBase {

  it must "parse double-quoted strings" in {
    implicit val parserToTest = doubleQuotedText
    parsing(""""a"""") must equal(Text("a"))
    parsing("""""""") must equal(Text(""))
    parsing(""""\""""") must equal(Text("\""))
    parsing(""""\'"""") must equal(Text("\\'"))
    parsing(""""'"""") must equal(Text("'"))
    assertFail("")
    assertFail("aa")
    assertFail("aaa")
  }

  it must "parse unquoted strings" in {
    implicit val parserToTest = unquotedText
    parsing("""a""") must equal(Text("a"))
    assertFail("")
  }

  it must "parse dollar symbols" in {
    implicit val parserToTest = doubleQuotedText
    parsing( """"$"""") must equal(Text("$"))
  }

}
