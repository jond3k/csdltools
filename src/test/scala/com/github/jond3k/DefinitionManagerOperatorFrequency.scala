package com.github.jond3k

import csdl.Csdl
import java.util.Scanner
import java.io.File
import com.codahale.jerkson.Json
import java.net.URL
import org.codehaus.jackson.JsonNode

/**
 * 
 */
object DefinitionManagerOperatorFrequency extends App {

  require(args.size < 1, "usage: url")

  val url     = args(0)
  val file    = new File("/Users/jon/unique_historic_streams.txt")
  val scanner = new Scanner(file)
  var allowed = -1

  val jmap    = FindOperatorFrequency.newJmap

  try {
    while (allowed != 0 && scanner.hasNextLine) {
      try {
        val line   = scanner.nextLine()
        val values = Json.parse[JsonNode](new URL(url + line)).path("values")

        require(values.path("definition_id").asText() == line,
          "definition_id did not match value in request: %s != %s" format (line, values.path("definition_id")))
        require(values.path("definition").isTextual, "definition text was: %s" format values.path("definition"))

        val csdl = Csdl.parse(values.path("definition").asText())

        FindOperatorFrequency(csdl, jmap)

        println("%-8s%s" format (math.abs(allowed), values))

      } catch {
        case e: InterruptedException => // bubble up
        case e: Exception => sys.error(e.toString)
      }
      allowed -= 1
    }
  } catch {
    case e: InterruptedException => // ignore, exit
  }

  FindOperatorFrequency.convertJMap(jmap) foreach {
    case (operator, count) =>
      println("%-15s%s" format (operator, count))
  }

}
