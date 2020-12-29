package com.neowise.almond.exceptions

import com.neowise.almond.parser.lexer.Token
import java.lang.RuntimeException

class ParseException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(token: Token, e: Exception) : super("${token.row}: $e")
    constructor(token: Token, text: String) : super("${token.row}: $text")
    constructor(tag: String, token: Token, text: String) : super("$tag -> ${token.row}: $text")
}