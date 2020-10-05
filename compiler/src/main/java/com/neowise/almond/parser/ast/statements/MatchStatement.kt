package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor
import java.beans.Expression

class MatchStatement(val expression: Node, val cases: List<Case>) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    class Case(val value: Node, val body: Node)
}