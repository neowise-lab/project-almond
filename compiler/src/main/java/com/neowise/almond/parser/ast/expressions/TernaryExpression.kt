package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class TernaryExpression(val result: Node, val trueExpr: Node, val falseExpr: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

}
