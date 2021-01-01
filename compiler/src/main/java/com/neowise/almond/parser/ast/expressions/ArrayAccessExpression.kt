package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class ArrayAccessExpression(val target: Node, val index: Node) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "$target[$index]"
    }
}
