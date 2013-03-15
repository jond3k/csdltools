package com.github.jond3k

import csdl.ast._
import java.util
import org.apache.commons.lang3.mutable.MutableInt
import scala.collection.JavaConverters._
import scala.util.control.TailCalls.TailRec
import annotation.tailrec

/**
 * 
 */
object FindOperatorFrequency {

  /**
   * Increment or add the mutable value of
   */
  def add(k: String, result: util.HashMap[String, MutableInt], value: Int = 1) {
    val current = result.get(k)
    if (current == null) {
      result.put(k, new MutableInt(value))
    } else {
      current.add(value)
    }
  }

  /**
   * Merge the results from multiple jmaps in to one useful for the join phase of a multi-core fork-join
   */
  def merge(dest: util.HashMap[String, MutableInt], sources: util.HashMap[String, MutableInt]*) {
    sources map {
      source =>
        require(source != dest, "destination cannot be a source")
        source.asScala } foreach {
      source =>
        source.foreach {
          kv =>
            add(kv._1, dest, kv._2.intValue())
        }
    }
  }

  /**
   * Create a java map that has an integer object that can be incremented in place
   * Not thread-safe
   */
  def newJmap = new util.HashMap[String, MutableInt]()

  /**
   * Convert the jmap in to a Scala equivalent. Avoid using in a tight loop
   */
  def convertJMap(jmap: util.HashMap[String, MutableInt]): Map[String, Int] = {
    jmap.asScala.map {
      case (k, v) => k -> v.intValue()
    }.toMap
  }

  /**
   * Count the frequency of operators in a block of CSDL
   *
   * The jmap will be modified in a non-thread-safe manner
   */
  def apply(csdl: CsdlBody, result: util.HashMap[String, MutableInt]) {
    csdl match {
      case CsdlTaggedBody(tags, returns) =>
        tags.foreach(tag => apply(tag.expressions, result))
        apply(returns.expression, result)
      case _: Stream =>
        add("stream", result)
      case And(l, r) =>
        apply(l, result)
        apply(r, result)
      case Or(l, r) =>
        apply(l, result)
        apply(r, result)
      case Not(e) =>
        apply(e, result)
      case Rule(_, op, _) =>
        add(op.operator, result)
      case e =>
        throw new IllegalArgumentException("Cannot process " + e)
    }
  }

  /**
   * A slower version of apply that returns a scala map. Useful for getting metrics about single CSDL definitions but
   * you should instead use the version that takes a Java HashMap if you intend to get aggregated results.
   */
  def apply(csdl: CsdlBody): Map[String, Int] = {
    val jmap = newJmap
    apply(csdl, jmap)
    convertJMap(jmap)
  }
}
