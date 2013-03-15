package com.github.jond3k

import csdl.ast._

/**
 * Traverses some CSDL to find all the sources it requires, based on the first part of every target.
 *
 * Can be used to determine which shard to load CSDL if the "pickle prism" shards by source type
 */
object FindRequiredSources {

  /**
   * Assumes the start of the CSDL is the type
   */
  def typeFromTarget(target: String, allTypes: Set[String]) = {
    val start = target.split('.').head
    if (!allTypes.contains(start)) {
      throw new IllegalArgumentException(start + " is not known to this script. Make sure types is up to date!")
    }
    start
  }

  /**
   * Get the set of types that might be produced for this CSDL
   */
  def apply(
              csdl: CsdlBody,
              allTypes: Set[String]
             ): Set[String] = {
    apply(csdl, allTypes, Set.empty)
  }

  /**
   * Get the set of types that might be produced for this CSDL
   */
  def apply(
              csdl:     CsdlNode,
              allTypes: Set[String],
              types:    Set[String]): Set[String] = {
    csdl match {
      case CsdlTaggedBody(tags, returns) =>
        types ++ tags.flatMap(apply(_, allTypes, types))
      case And(l, r) =>
        types ++ apply(l, allTypes, types) ++ apply(r, allTypes, types)
      case Or(l, r) =>
        types ++ apply(l, allTypes, types) ++ apply(r, allTypes, types)
      case Not(e) =>
        types ++ (allTypes -- apply(e, allTypes, types))
      case Rule(target, _, _) =>
        types + typeFromTarget(target.value, allTypes)
      case e =>
        throw new IllegalArgumentException("Cannot process " + e)
    }
  }

}
