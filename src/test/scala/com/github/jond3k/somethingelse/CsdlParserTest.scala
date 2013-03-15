package com.github.jond3k.somethingelse

import com.github.jond3k.csdl.ast._
import org.scalatest.FlatSpec
import org.scalatest.matchers.MustMatchers
import com.github.jond3k.csdl.Csdl

/**
 * 
 */
class CsdlParserTest extends FlatSpec with MustMatchers {

  it must "allow parsing" in {
    Csdl.parse("i.c == a") must equal(
      Rule(
        Target("i.c"),
        Operator("=="),
        Text("a")
      )
    )
  }
}
