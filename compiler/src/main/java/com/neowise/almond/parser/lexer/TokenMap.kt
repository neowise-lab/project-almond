package com.neowise.almond.parser.lexer

open class TokenMap : HashMap<String, TokenType>() {

    operator fun plusAssign(value: TokenType) {
        this[value.text] = value
    }
}

public fun tokenMapOf(vararg elements: TokenType): TokenMap {
    val map = TokenMap()
    for(element in elements) {
        map += element
    }
    return map
}