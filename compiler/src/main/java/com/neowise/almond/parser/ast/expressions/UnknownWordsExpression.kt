package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class UnknownWordsExpression(val words: List<Token>) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return buildString {
            words.forEachIndexed { index, token ->
                if (index > 0) {
                    append('.')
                }
                append("$token")
            }
        }
    }
}
