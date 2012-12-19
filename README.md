csdltools
=========

Tools for working with DataSift's Csdl query language. Contents include:

* Csdl parser
* Csdl optimiser


Csdl parser
===========

The library uses Scala's built-in parser combinators for lexing & parsing. You can use them directly by using the
CsdlParsers class, or more easily you can opt for the CsdlParser, which looks more like a traditional parser: it exposes
a parse function that throws an InvalidCsdlException when it encounters invalid input.

The resulting AST consists of case classes. Match them at will!

Csdl Optimiser
==============

The library currently optimises the following:

* many 'contains' matches on the same target, joined by 'and' become a single 'contains_any'

Future goals
============

Right now I'm interested in adding the following

 * Conversion to other languages
 * Versioning, to automatically upgrade statements to newer version of the language
