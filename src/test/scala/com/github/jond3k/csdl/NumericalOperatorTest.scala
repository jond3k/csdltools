package com.github.jond3k.csdl

import ast._
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class NumericalOperatorTest extends ParserTestBase {


  it must "parse numerical operators" in {
    implicit val parserToTest = body
    parsing("twitter.text contains \"rihanna\" AND interaction.sample <= 1") must equal(
      And(
        Rule(Target("twitter.text"), Operator("contains"), Text("rihanna")),
        Rule(Target("interaction.sample"), Operator("<="), Text("1"))
      )
    )
  }

  it must "parse negative numbers" in {
    implicit val parserToTest = body
    parsing("sentiment.value <= -1") must equal(
      Rule(Target("sentiment.value"), Operator("<="), Text("-1"))
    )
  }

  it must "parse numerical operators in a return block" in {
    implicit val parserToTest = body
    parsing("return { twitter.text contains \"rihanna\" AND interaction.sample <= 1 }") must equal(
      CsdlTaggedBody(
        Nil,
        Returns(And(
          Rule(Target("twitter.text"), Operator("contains"), Text("rihanna")),
          Rule(Target("interaction.sample"), Operator("<="), Text("1"))
        ))
      )
    )
  }


}
