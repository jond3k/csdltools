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
object OperatorFrequencyScript extends App {

  require(args.size >= 2, "usage: url file")

  val url     = args(0)
  val file    = new File(args(1))
  var allowed = if (args.size > 2) args(2).toInt else -1
  val scanner = new Scanner(file)

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
        case e: Exception => System.err.println(e.toString)
      }
      allowed -= 1
    }
  } catch {
    case e: InterruptedException => // ignore, exit
  }

  FindOperatorFrequency.convertJMap(jmap).toList.sortBy(_._2).foreach {
    case (operator, count) =>
      println("%-15s%s" format (operator, count))
  }

}
