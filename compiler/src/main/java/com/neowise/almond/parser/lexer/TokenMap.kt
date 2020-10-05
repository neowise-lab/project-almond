package com.neowise.almond.parser.lexer

open class TokenMap : HashMap<String, TokenType>() {

    operator fun plusAssign(value: TokenType) {
        this[value.text] = value
    }
}