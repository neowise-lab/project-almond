package com.neowise.almond.parser.ast

import com.neowise.almond.exceptions.ParseException
import com.neowise.almond.visitors.Visitor

class Program(
        val location: String,
        val name: String,
        val nodes: NodeList,
        val errors: List<ParseException>
) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "program $name ($location)\n$nodes"
    }
}
