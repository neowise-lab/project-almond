package com.neowise.almond.parser.ast

import com.neowise.almond.listItems
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class Block : NodeList() {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "{ \n " + listItems("\n") + "} \n"
    }
}