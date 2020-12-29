package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.EmptyNode
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class ReturnStatement(val expression: Node = EmptyNode) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "return " + if (expression != EmptyNode) expression else ""
    }
}