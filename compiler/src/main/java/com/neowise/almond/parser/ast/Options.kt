package com.neowise.almond.parser.ast

import com.neowise.almond.listItems
import com.neowise.almond.parser.lexer.Token

class Options(vararg options: Token) {

    private val options: ArrayList<Token> = ArrayList()

    init {
        this.options += options
    }

    operator fun plusAssign(token: Token) {
        options += token
    }

    override fun toString(): String {
        return "(" + options.listItems(", ") + ")"
    }

}