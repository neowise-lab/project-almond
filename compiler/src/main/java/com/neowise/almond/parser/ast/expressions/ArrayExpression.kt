package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.listItems
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class ArrayExpression(val elements: NodeList) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "[" + elements.listItems(", ") + "]"
    }
}