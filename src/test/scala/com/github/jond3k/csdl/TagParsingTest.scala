package com.github.jond3k.csdl

import ast._
import ast.CsdlTaggedBody
import ast.Operator
import ast.Returns
import ast.Rule
import ast.Tag
import ast.Target
import com.github.jond3k.ParserTestBase

/**
 * 
 */
class TagParsingTest extends ParserTestBase {

  it must "parse return blocks correctly" in {
    implicit val parserToTest = returns
    parsing("return {interaction.type==\"twitter\"}") must equal(
      Returns(Rule(Target("interaction.type"), Operator("=="), Text("twitter"))))
    parsing("return { twitter.text contains \"rihanna\" }") must equal(
      Returns(Rule(Target("twitter.text"), Operator("contains"), Text("rihanna"))))
  }

  it must "parse tag blocks correctly, unitary op" in {
    implicit val parserToTest = tag
    parsing("tag \"tag1\" { interaction.content exists }") must equal(
      Tag("tag1", Rule(Target("interaction.content"), Operator("exists"))))
  }

  it must "parse tag blocks correctly, binary op" in {
    implicit val parserToTest = tag
    parsing("tag \"tag1\" { interaction.content any \"facebook\" }") must equal(
      Tag("tag1", Rule(Target("interaction.content"), Operator("any"), Text("facebook"))))
  }

  it must "parse tagged bodies correctly" in {
    implicit val parserToTest = taggedBody
    parsing("tag \"tag1\" {interaction.content contains_any \"facebook\"} tag \"tag2\" {interaction.content contains_any \"cheese\"} return {interaction.type==\"twitter\"}") must equal(
      CsdlTaggedBody(
        List(
          Tag("tag1", Rule(Target("interaction.content"), Operator("contains_any"), Text("facebook"))),
          Tag("tag2", Rule(Target("interaction.content"), Operator("contains_any"), Text("cheese")))
        ),
        Returns(Rule(Target("interaction.type"), Operator("=="), Text("twitter"))))
    )
  }

  it must "negotiate between bodies and tagged bodies correctly" in {
    implicit val parserToTest = body
    parsing("tag \"tag1\" {interaction.content any \"facebook\"} return {interaction.type==\"twitter\" }") must equal(
      CsdlTaggedBody(
        List(
          Tag("tag1", Rule(Target("interaction.content"), Operator("any"), Text("facebook")))
        ),
        Returns(Rule(Target("interaction.type"), Operator("=="), Text("twitter"))))
    )
  }

  it must "allow return blocks by themselves" in {
    implicit val parserToTest = body
    parsing("return { twitter.hashtags in \"AmexBaubleBarOffer\" }") must equal(
      CsdlTaggedBody(
        Nil,
        Returns(Rule(Target("twitter.hashtags"), Operator("in"), Text("AmexBaubleBarOffer"))))
    )
  }
}
