package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.visitors.Visitor

class MapExpression(val keys: NodeList, val values: NodeList) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}