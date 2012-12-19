package com.github.jond3k.csdl

import ast._
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class OperatorParsingTest extends ParserTestBase {

  it must "parse 'in'" in {
    implicit val parserToTest = binaryOperatorType
    parsing(" in ") must equal("in")
  }

  it must "not parse interaction" in {
    implicit val parserToTest = binaryOperatorType
    intercept[IllegalArgumentException](parsing(" interaction ") must equal("in"))
  }

}
