package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.Visitor

class Block : NodeList(), Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}