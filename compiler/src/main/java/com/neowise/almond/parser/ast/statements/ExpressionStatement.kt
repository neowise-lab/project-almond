package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.visitors.Visitor

class ExpressionStatement(val expression: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

}
