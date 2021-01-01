package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class ErrorStatement(val expression: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "error $expression"
    }
}
