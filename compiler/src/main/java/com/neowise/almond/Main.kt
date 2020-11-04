package com.neowise.almond

import com.neowise.almond.parser.Parser
import com.neowise.almond.parser.lexer.Lexer
import com.neowise.almond.parser.source.SourceLoader

fun main(args: Array<String>) {
    val source = SourceLoader().fromResource("/main.alm")
    val lexer = Lexer(source)
    val parser = Parser("almond/lang", "main", lexer.tokenize())

    val program = parser.parse()

    println(program)
}