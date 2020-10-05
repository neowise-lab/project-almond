package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.Operator
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class BinaryExpression(operator: Operator, expr1: Node, expr2: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

}
