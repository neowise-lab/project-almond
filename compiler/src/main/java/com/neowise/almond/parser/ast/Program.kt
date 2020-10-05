package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.Visitor

class Program(val location: String, val name: String) : Node {

    private val children : MutableList<Node> = ArrayList()

    operator fun plusAssign(value: Node) {
        children.add(value)
    }

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}
