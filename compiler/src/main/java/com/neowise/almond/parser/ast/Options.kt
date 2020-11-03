package com.neowise.almond.parser.ast

import com.neowise.almond.parser.lexer.Token

class Options(private vararg val options: Token) : ArrayList<Token>() {
    init {
        addAll(options)
    }

    override fun toString(): String {
        return buildString {
            append("(")
            forEach {
                append("$it, ")
            }
            append(")")
        }
    }
}