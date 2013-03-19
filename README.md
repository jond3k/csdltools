#CsdlTools


Tools for working with DataSift's Csdl query language


##Csdl Parser

You can start parsing Csdl straight away. Just use the Csdl.parse method

	val csdl = Csdl.parse("interaction.content contains \"cheese\"")

The object you'll get back is some form of CsdlBody. Using pattern matching you can decide what to do next.

    csdl match {
      case And(l, r) =>
        println("The left hand side is: %s" format l)
        println("The right hand side is: %s" format r)
      case _ => // ..

The hierarchy of AST classes is:

	CsdlNode
	|- CsdlBody
	| |- And
	| |- Or
	| |- Not
	| |- CsdlTaggedBody
	| |- Returns
	| |- Rule
	| |- Stream
	|- Argument
	| |- Operator
	| |- Target
	| |- Text
	| |- TextList
	|- Tag

The parser is implemented using Scala's built-in parser combinators and doesn't provide very good error-handling so isn't too good for use inside editors, etc. It can be pretty useful for automated analysis and optimzation of CSDL we already know is well-formed.

##Future Goals

Right now I'm interested in adding the following:

* Conversion to other languages
* Optimization
* Pretty-printing
* Versioning, to automatically upgrade statements to newer version of the language
