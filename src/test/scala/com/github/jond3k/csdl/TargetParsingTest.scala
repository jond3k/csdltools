package com.github.jond3k.csdl

import com.github.jond3k.ParserTestBase

/**
 * 
 */
class TargetParsingTest extends ParserTestBase {

  it must "parse this thing" in {
    implicit val parserToTest = expressions
    val example = """not interactioncontent contains "W""""
    parsing(example) must equal("")
  }
}
