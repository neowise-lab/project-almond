package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class NewInstanceExpression(val struct: Token, val arguments: NodeList) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "new $struct(" + buildString {
            arguments.forEach {
                append("$it, ")
            }
        } + ")"
    }
}