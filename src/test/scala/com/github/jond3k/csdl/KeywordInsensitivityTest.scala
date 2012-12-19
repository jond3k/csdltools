package com.github.jond3k.csdl

import ast._
import ast.And
import ast.Operator
import ast.Or
import ast.Rule
import ast.Target
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class KeywordInsensitivityTest extends ParserTestBase {
  it must "parse keywords in a case-insensitive way" in {
    implicit val parserToTest = expressions
    parsing("a SuBStR \"abc\"") must equal(Rule(Target("a"), Operator("substr"), Text("abc")))
    parsing("a exists aNd b eXisTs") must equal(And(Rule(Target("a"), Operator("exists")), Rule(Target("b"), Operator("exists"))))
    parsing("a exists Or b eXisTs") must equal(Or(Rule(Target("a"), Operator("exists")), Rule(Target("b"), Operator("exists"))))
    parsing("NOT a exisTS") must equal(Not(Rule(Target("a"), Operator("exists"))))
  }
}
