package com.github.jond3k.csdl

import ast._
import ast.Operator
import ast.Rule
import ast.Target
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class RuleParsingTest extends ParserTestBase {

  it must "parse rule expressions" in {
    implicit val parserToTest = expressions
    parsing("interaction.type == \"facebook\"") must equal(Rule(Target("interaction.type"), Operator("=="), Text("facebook")))
    parsing("interaction.type contains_any \"facebook\"") must equal(Rule(Target("interaction.type"), Operator("contains_any"), Text("facebook")))
    parsing("interaction.schema.version == 123") must equal(Rule(Target("interaction.schema.version"), Operator("=="), Text("123")))
    parsing("something.user_ids in [123, 456]") must equal(Rule(Target("something.user_ids"), Operator("in"), TextList(List(Text("123"), Text("456")))))
    parsing("interaction.schema.version cs != 123") must equal(Rule(Target("interaction.schema.version"), Operator("!=", cs=true), Text("123")))
    parsing("interaction.schema.version exists") must equal(Rule(Target("interaction.schema.version"), Operator("exists"), null))
    assertFail("== \"fakebook\"")
    assertFail("interaction.type ==")
    assertFail("interaction.type")
    assertFail("\"interaction.type\" == \"facebook\"")
  }
}
