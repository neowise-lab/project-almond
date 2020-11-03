package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class ArrayAccessExpression(val result: Node, val index: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "$result [$index]"
    }
}
