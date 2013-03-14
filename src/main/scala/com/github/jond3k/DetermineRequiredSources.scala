package com.github.jond3k

import csdl.CsdlParser
import java.util.Scanner

/**
 *
 *
 * Can be used to:
 *
 *  - Determine which shard to load the CSDL on
 *  -
 */
object DetermineRequiredSources extends App {

  val types = Set(
    "facebook",
    "twitter",
    "bitly",
    "digg",
    "myspace",
    "facebook_page"
  )

  val scanner = new Scanner(System.in)
  val parser  = new CsdlParser
  var line    = scanner.nextLine()
  while(line != null) {
    val csdl = parser.parse(line)

    line = scanner.nextLine()
  }


}
