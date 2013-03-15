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

  it must "detect stream and treat it as an operator" in {
    FindOperatorFrequency(Csdl.parse("Stream \"390070f8f7a6593adebf073c6a70b9fa\"")) must equal(
      Map("stream" -> 1)
    )
  }

  it must "detect two == operators inside tag..returns" in {
    FindOperatorFrequency(Csdl.parse("tag \"jon\" {interaction.content == \"cheese\"} return {interaction.content == \"hh\"}" )) must equal(
      Map("==" -> 2)
    )
  }

  it must "allow usage of a supplied java map" in {
    val jmap = FindOperatorFrequency.newJmap
    FindOperatorFrequency(Csdl.parse("interaction == \"cheese\""), jmap)
    FindOperatorFrequency.convertJMap(jmap) must equal(Map("==" -> 1))
  }

  it must "allow accumulated usage of a supplied java map" in {
    val jmap = FindOperatorFrequency.newJmap
    FindOperatorFrequency(Csdl.parse("interaction == \"cheese\""), jmap)
    FindOperatorFrequency(Csdl.parse("interaction == \"cheese\""), jmap)
    FindOperatorFrequency.convertJMap(jmap) must equal(Map("==" -> 2))
  }

  it must "allow merging of multiple java maps" in {
    val jmapA = FindOperatorFrequency.newJmap
    val jmapB = FindOperatorFrequency.newJmap
    val jmapC = FindOperatorFrequency.newJmap
    FindOperatorFrequency(Csdl.parse("interaction == \"cheese\""), jmapA)
    FindOperatorFrequency(Csdl.parse("interaction == \"cheese\""), jmapB)
    FindOperatorFrequency.merge(jmapC, jmapA, jmapB, jmapA, jmapB)
    FindOperatorFrequency.convertJMap(jmapC) must equal(Map("==" -> 4))
  }

  it must "prevent merging of one map in to itself" in {
    val jmapA = FindOperatorFrequency.newJmap
    intercept[IllegalArgumentException](FindOperatorFrequency.merge(jmapA, jmapA))
  }
}
