package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.Visitor

class Block : NodeList() {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return buildString {
            append("{")
            forEach {
                append("\t $it\n")
            }
            append("}\n")
        }
    }
}