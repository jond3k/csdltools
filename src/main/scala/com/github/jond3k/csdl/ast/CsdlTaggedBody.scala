package com.github.jond3k.csdl.ast

/**
 * @author Jonathan Davey <jon.davey@datasift.com>
 */
case class CsdlTaggedBody(tag: List[Tag], returns: Returns) extends CsdlBody
