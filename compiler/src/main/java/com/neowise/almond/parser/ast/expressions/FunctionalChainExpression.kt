package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.visitors.Visitor

class FunctionalChainExpression(val expr: Node, val arguments: NodeList) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "$expr(" + buildString {
            arguments.forEachIndexed { index, node ->
                if (index > 0) append(", ")
                append("$node")
            }
        } + ")"
    }
}
