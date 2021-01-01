package com.neowise.almond

import com.neowise.almond.parser.Parser
import com.neowise.almond.parser.ast.Program
import com.neowise.almond.parser.lexer.Lexer
import com.neowise.almond.parser.source.SourceLoader
import com.neowise.almond.semantics.Logger

fun main(args: Array<String>) {
    val logger = Logger()

    for((location, name) in mapOf("almond/howto2" to "main.alm", "almond/howto" to "util.alm")) {
        println(name)
        val program = parse(location, name)
        logger.log(program)
    }
    val metadata = logger.metadata()
    println(metadata)
}

private fun parse(location: String, name: String) : Program {
    val source = SourceLoader().fromResource(location, name)
    val tokens = Lexer(source).tokenize()
    val parser = Parser(source.location, source.name, tokens)
    return parser.parse()
}