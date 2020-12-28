package com.neowise.almond.parser.lexer

class TokenMap : HashMap<String, TokenType>() {

    operator fun plusAssign(value: TokenType) {
        this[value.text] = value
    }
}

fun tokenMapOf(vararg elements: TokenType): TokenMap {
    val map = TokenMap()
    for(element in elements) {
        map += element
    }
    return map
}