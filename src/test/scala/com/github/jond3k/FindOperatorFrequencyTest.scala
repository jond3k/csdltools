package com.github.jond3k

import csdl.Csdl
import org.scalatest.matchers.MustMatchers
import org.scalatest.FlatSpec

/**
 * 
 */
class FindOperatorFrequencyTest extends MustMatchers with FlatSpec {

  it must "detect one == operator" in {
    FindOperatorFrequency(Csdl.parse("interaction == \"cheese\"")) must equal(
      Map("==" -> 1)
    )
  }

  it must "detect two == operators" in {
    FindOperatorFrequency(Csdl.parse("interaction.content == \"cheese\" and interaction.content == \"lemons\"")) must equal(
      Map("==" -> 2)
    )
  }

  it must "detect two == operators and one contains_any operator" in {
    FindOperatorFrequency(Csdl.parse(
      "interaction.content == \"cheese\" and interaction.content == \"lemons\" and interaction.content contains_any \"afasf, afas\"")) must equal(
      Map("==" -> 2, "contains_any" -> 1)
    )
  }

  it must "detect two == operators inside tag..returns" in {
    FindOperatorFrequency(Csdl.parse("tag \"jon\" {interaction.content == \"cheese\"} return {interaction.content == \"hh\"}" )) must equal(
      Map("==" -> 2)
    )
  }

}
