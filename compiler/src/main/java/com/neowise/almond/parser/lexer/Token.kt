package com.neowise.almond.parser.lexer

class Token(val text: String = "", val type: TokenType, val row: Int, val col: Int) {
    override fun toString(): String = "'$text' at $row:$col"
}

