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
class LogicalOperatorTest extends ParserTestBase {

  it must "parse logical operators" in {
    implicit val parserToTest = expressions

    parsing("a.a == \"a\" and b.b == \"b\"") must equal(And(
      Rule(Target("a.a"), Operator("=="), Text("a")),
      Rule(Target("b.b"), Operator("=="), Text("b"))
    ))

    parsing("a.a == \"a\" or b.b == \"b\"") must equal(Or(
      Rule(Target("a.a"), Operator("=="), Text("a")),
      Rule(Target("b.b"), Operator("=="), Text("b"))
    ))

    parsing("not a.a == \"a\"") must equal(Not(
      Rule(Target("a.a"), Operator("=="), Text("a"))
    ))
  }

  it must "obey explicit operator precedence" in {
    implicit val parserToTest = expressions

    parsing("(a.a == \"a\") and b.b == \"b\"") must equal(And(
      Rule(Target("a.a"), Operator("=="), Text("a")),
      Rule(Target("b.b"), Operator("=="), Text("b"))
    ))

    parsing("a.a == \"a\" or (b.b == \"b\" and c.c == \"c\")") must equal(Or(
      Rule(Target("a.a"), Operator("=="), Text("a")),
      And(
        Rule(Target("b.b"), Operator("=="), Text("b")),
        Rule(Target("c.c"), Operator("=="), Text("c"))
      )
    ))

    parsing("(a.a == \"a\" or b.b == \"b\") and c.c == \"c\"") must equal(And(
      Or(
        Rule(Target("a.a"), Operator("=="), Text("a")),
        Rule(Target("b.b"), Operator("=="), Text("b"))
      ),
      Rule(Target("c.c"), Operator("=="), Text("c"))
    ))
  }

  it must "obey implicit operator precedence" in {
    implicit val parserToTest = expressions

    parsing("a.a == \"a\" or b.b == \"b\" and c.c == \"c\"") must equal(Or(
      Rule(Target("a.a"), Operator("=="), Text("a")),
      And(
        Rule(Target("b.b"), Operator("=="), Text("b")),
        Rule(Target("c.c"), Operator("=="), Text("c"))
      )
    ))

    parsing("a.a == \"a\" and b.b == \"b\" or c.c == \"c\"") must equal(Or(
      And(
        Rule(Target("a.a"), Operator("=="), Text("a")),
        Rule(Target("b.b"), Operator("=="), Text("b"))
      ),
      Rule(Target("c.c"), Operator("=="), Text("c"))
    ))
  }
}
