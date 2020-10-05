package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.Visitor

open class NodeList : ArrayList<Node>(), Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}