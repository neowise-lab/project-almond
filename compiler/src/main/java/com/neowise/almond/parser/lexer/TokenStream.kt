package com.neowise.almond.parser.lexer

import com.neowise.almond.exceptions.ParseException
import java.io.EOFException

class TokenStream(private val tokens: List<Token>) {

    private var pos = 0;
    private val size = tokens.size;

    fun current(): Token {
        return get(0)
    }

    fun consume(type: TokenType): Token {
        val current = get(0)
        if (type !== current.type) {
            throw ParseException(current, "'" + type.text + "' expected")
        }
        pos++
        return current
    }

    fun match(type: TokenType): Boolean {
        val current = get(0)
        if (type !== current.type) {
            return false
        }
        pos++
        return true
    }

    fun lookMatch(pos: Int, type: TokenType): Boolean {
        return get(pos).type === type
    }

    operator fun get(relativePosition: Int): Token {
        val position: Int = pos + relativePosition
        if (position >= size) {
            throw EOFException()
        }
        return tokens[position]
    }

    fun error(error: String): ParseException? {
        throw ParseException(get(0), error)
    }
}