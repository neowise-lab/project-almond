package com.neowise.almond.exceptions

import com.neowise.almond.parser.lexer.Token
import java.lang.RuntimeException

class ParseException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(token: Token, e: Exception) : super("$token: $e")
    constructor(token: Token, text: String) : super("$token: $text")
}