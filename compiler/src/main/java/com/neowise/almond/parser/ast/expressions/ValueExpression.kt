package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.parser.lexer.TokenType
import com.neowise.almond.visitors.Visitor

class ValueExpression(val value: Token) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return if (value.type == TokenType.TEXT) "'${text()}'" else text()
    }

    private fun text(): String {
        return if (value.text.isEmpty()) value.type.text else value.text
    }
}
