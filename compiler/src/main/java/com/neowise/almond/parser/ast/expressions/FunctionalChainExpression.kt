package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.visitors.Visitor

class FunctionalChainExpression(expr: Node, arguments: NodeList) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

}
