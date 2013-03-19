package com.github.jond3k.csdl

import ast._
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class ParenthesisParsingTest extends ParserTestBase {

  it must "parse parenthesis around or operators" in {
    implicit val parserToTest = body
    parsing("(klout.score >= 49 OR twitter.user.profile_age <= 25)") must equal(
      Or(
        Rule(Target("klout.score"), Operator(">="), Text("49")),
        Rule(Target("twitter.user.profile_age"), Operator("<="), Text("25"))
      )
    )
  }

  it must "parse parenthesis in complex expressions" in {
    implicit val parserToTest = body
    parsing("twitter.text cs contains_any \"Big Data\" AND (klout.score >= 49 OR twitter.user.profile_age <= 25)") must equal(
      And(
        Rule(Target("twitter.text"), Operator("contains_any", cs=true), Text("Big Data")),
        Or(
          Rule(Target("klout.score"), Operator(">="), Text("49")),
          Rule(Target("twitter.user.profile_age"), Operator("<="), Text("25"))
        )
      )
    )
  }


}
