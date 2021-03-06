package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class MapExpression(val keys: NodeList, val values: NodeList) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "{" + buildString {
            for ((index, key) in keys.withIndex()) {
                val value = values[index]
                if (index > 0) append(", ")
                append("$key : $value")
            }
        } + "}"
    }
}