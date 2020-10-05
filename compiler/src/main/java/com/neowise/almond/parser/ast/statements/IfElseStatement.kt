package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.EmptyNode
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class IfElseStatement(condition: Node, ifBody: Node, elseBody: Node = EmptyNode) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}
