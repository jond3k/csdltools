package com.github.jond3k.csdl

import ast.CsdlBody
import java.io.{FileInputStream, File}
import org.apache.commons.io.IOUtils

/**
 * Provides facilities to parse CSDL expressions and turn them in to abstract syntax trees (ASTs) that can be used for
 * evaluation, analysis or transformation.
 */
object Csdl {

  /**
   * Does the real work
   */
  private val parser = new CsdlParsers

  /**
   * Convert a CSDL string in to a CsdlBody
   */
  def parse(s: String): CsdlBody = {
    implicit val parserToUse = parser.body
    parser.parse(s)
  }

  /**
   * Read CSDL from a file and turn it in to a CsdlBody
   */
  def parse(file: File): CsdlBody = parse(contents(file))

  /**
   * Extract the contents of a CSDL file
   */
  private def contents(file: File): String = {
    require(file.exists,  "File %s doesn't exist" format file)
    require(file.canRead, "File %s cannot be read" format file)
    IOUtils.toString(new FileInputStream(file))
  }
}
