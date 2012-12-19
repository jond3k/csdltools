package com.github.jond3k.csdl

import ast._
import org.scalatest.matchers.MustMatchers
import org.scalatest.FlatSpec
import com.github.jond3k.ParserTestBase

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
class CommentParsingTest extends ParserTestBase {

  it must "skip over comments in tagged bodies" in {
    implicit val parserToTest = taggedBody
    parsing("// this is nothing of interest\ntag \"tag1\" {interaction.content any \"facebook\"} tag \"tag2\" {interaction.content any \"cheese\"} return {interaction.type==\"twitter\"}") must equal(
      CsdlTaggedBody(
        List(
          Tag("tag1", Rule(Target("interaction.content"), Operator("any"), Text("facebook"))),
          Tag("tag2", Rule(Target("interaction.content"), Operator("any"), Text("cheese")))
        ),
        Returns(Rule(Target("interaction.type"), Operator("=="), Text("twitter"))))
    )
  }

}
