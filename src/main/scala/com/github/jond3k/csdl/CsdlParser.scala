package com.github.jond3k.csdl

import ast.{CsdlTaggedBody, CsdlBody}
import java.io.{FileInputStream, File}
import org.apache.commons.io.IOUtils

/**
 * 
 */
class CsdlParser {

  val parser = new CsdlParsers

  def parse(s: String): CsdlBody = {
    implicit val parserToUse = parser.body
    parser.parse(s)
  }

  def parseTagged(s: String): CsdlTaggedBody = {
    implicit val parserToUse = parser.taggedBody
    parser.parse(s)
  }

  def parse(file: File): CsdlBody = parse(fileContents(file))

  def parseTagged(file: File): CsdlTaggedBody = parseTagged(fileContents(file))

  protected def fileContents(file: File): String = {
    if (!file.exists()) throw new IllegalArgumentException("File %s doesn't exist" format file)
    if (!file.canRead)  throw new IllegalArgumentException("File %s cannot be read" format file)
    val stream = new FileInputStream(file)
    IOUtils.toString(stream)
  }
}
