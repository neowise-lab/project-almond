package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.Operator
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class BinaryExpression(val operator: Operator, val expr1: Node, val expr2: Node) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "$expr1 $operator $expr2"
    }
}
