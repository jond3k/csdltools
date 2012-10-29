package com.github.jond3k.csdl

import ast._
import org.scalatest.matchers.MustMatchers
import org.scalatest.FlatSpec
import com.github.jond3k.ParserTestHelper

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class CsdlParsersTest extends CsdlParsers with FlatSpec with MustMatchers with ParserTestHelper {

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

  it must "parse text lists" in {
    implicit val parserToTest = textList
    parsing("[123, 456]") must equal(TextList(List(Text("123"), Text("456"))))
    parsing("[123, \"abc\"]") must equal(TextList(List(Text("123"), Text("abc"))))
    parsing("[123, \"ab\\\"c\"]") must equal(TextList(List(Text("123"), Text("ab\"c"))))
    assertFail("[]")
    assertFail("[")
    assertFail("[123,]")
  }

  it must "parse rule expressions" in {
    implicit val parserToTest = expressions
    parsing("interaction.type == \"facebook\"") must equal(Rule(Target("interaction.type"), Operator("=="), Text("facebook")))
    parsing("interaction.schema.version == 123") must equal(Rule(Target("interaction.schema.version"), Operator("=="), Text("123")))
    parsing("something.user_ids in [123, 456]") must equal(Rule(Target("something.user_ids"), Operator("in"), TextList(List(Text("123"), Text("456")))))
    parsing("interaction.schema.version cs != 123") must equal(Rule(Target("interaction.schema.version"), Operator("!=", cs=true), Text("123")))
    parsing("interaction.schema.version exists") must equal(Rule(Target("interaction.schema.version"), Operator("exists"), null))
    assertFail("== \"fakebook\"")
    assertFail("interaction.type ==")
    assertFail("interaction.type")
    assertFail("\"interaction.type\" == \"facebook\"")
  }

  it must "parse stream expressions" in {
    implicit val parserToTest = expressions
    parsing("stream \"abcd\"") must equal(Stream("abcd"))
  }

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
