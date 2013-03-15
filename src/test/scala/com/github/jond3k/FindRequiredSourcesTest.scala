package com.github.jond3k

import csdl.Csdl
import org.scalatest.matchers.MustMatchers
import org.scalatest.FlatSpec

/**
 * 
 */
class FindRequiredSourcesTest extends MustMatchers with FlatSpec {

  val allTypes = Set(
    "facebook",
    "twitter",
    "interaction",
    "bitly"
  )

  it must "recognise a type inside a rule" in {
    FindRequiredSources(
      Csdl.parse("facebook.id == \"facebook\""),
      allTypes
    ) must equal (Set("facebook"))
  }

  it must "invert the list when encountering a negation" in {
    FindRequiredSources(
      Csdl.parse("not (facebook.id == \"facebook\")"),
      allTypes
    ) must equal (allTypes - "facebook")
  }

  it must "join the results of two rules if there's an or" in {
    FindRequiredSources(
      Csdl.parse("facebook.id == \"facebook\" or twitter.retweeted_by == \"@jond3k\""),
      allTypes
    ) must equal (Set("facebook", "twitter"))
  }

  it must "join two rules if there's an and" in {
    FindRequiredSources(
      Csdl.parse("facebook.id == \"facebook\" and twitter.retweeted_by == \"@jond3k\""),
      allTypes
    ) must equal (Set("facebook", "twitter"))
  }
}
