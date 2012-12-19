package com.github.jond3k.csdl

import ast.{CsdlTaggedBody, CsdlBody}
import util.parsing.input.CharSequenceReader
import java.io.{FileInputStream, File}
import org.apache.commons.io.IOUtils

/**
 * 
 */
class CsdlParser protected extends CsdlParsers {

  def parse(s: String): CsdlBody = {
    implicit val parserToUse = body
    parseGeneric(s)
  }

  def parseTagged(s: String): CsdlTaggedBody = {
    implicit val parserToUse = taggedBody
    parseGeneric(s)
  }

  def parse(file: File): CsdlBody = parse(fileContents(file))

  def parseTagged(file: File): CsdlTaggedBody = parseTagged(fileContents(file))

  protected def parseGeneric[T](s: String)(implicit p: Parser[T]): T = {
    //wrap the parser in the phrase parse to make sure all input is consumed
    val phraseParser = phrase(p)
    //we need to wrap the string in a reader so our parser can digest it
    val input = new CharSequenceReader(s)
    phraseParser(input) match {
      case Success(t,_)     => t
      case NoSuccess(msg,_) => throw new IllegalArgumentException(
        "Could not parse '" + s + "': " + msg)
    }
  }

  protected def fileContents(file: File): String = {
    if (!file.exists()) throw new IllegalArgumentException("File %s doesn't exist" format file)
    if (!file.canRead)  throw new IllegalArgumentException("File %s cannot be read" format file)
    val stream = new FileInputStream(file)
    IOUtils.toString(stream)
  }
}
