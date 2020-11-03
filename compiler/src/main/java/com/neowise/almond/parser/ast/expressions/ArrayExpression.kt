package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.visitors.Visitor

class ArrayExpression(val elements: NodeList) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return buildString {
            append("[")
            elements.forEach {
                append("$it, ")
            }
            append("]")
        }
    }
}