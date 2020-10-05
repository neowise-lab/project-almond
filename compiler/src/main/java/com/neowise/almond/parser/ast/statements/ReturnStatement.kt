package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.EmptyNode
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class ReturnStatement(val expr: Node = EmptyNode) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}