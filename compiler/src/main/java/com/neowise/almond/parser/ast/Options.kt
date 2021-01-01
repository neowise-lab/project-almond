package com.neowise.almond.parser.ast

import com.neowise.almond.listItems
import com.neowise.almond.parser.lexer.Token

class Options(vararg options: Token) : ArrayList<Token>() {

    init { addAll(options) }

    override fun toString(): String {
        return "(" + listItems(", ") + ")"
    }

}