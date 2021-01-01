package com.neowise.almond.exceptions

import com.neowise.almond.parser.lexer.Token

class DuplicateException(token: Token) : Throwable()