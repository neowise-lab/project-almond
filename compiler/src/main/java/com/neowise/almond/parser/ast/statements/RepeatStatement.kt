package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class RepeatStatement(val condition: Node, val body: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "repeat($condition) $body"
    }
}
