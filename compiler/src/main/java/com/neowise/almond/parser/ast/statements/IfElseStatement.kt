package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.EmptyNode
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class IfElseStatement(val condition: Node, val ifBody: Node, val elseBody: Node = EmptyNode) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "if($condition) \n $ifBody \n" + if (elseBody != EmptyNode) " else \n$elseBody" else ""
    }
}
