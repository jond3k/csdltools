package com.github.jond3k.csdl

import ast.{Text, Operator, Target, Rule}
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class OperatorTest extends ParserTestBase {

  it must "parse " in {
    implicit val parserToTest = expressions
    parsing("interaction.type == \"facebook\"") must equal(Rule(Target("interaction.type"), Operator("=="), Text("facebook")))
  }

}
