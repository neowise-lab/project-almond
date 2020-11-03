package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.visitors.Visitor

class LambdaExpression(val options: Options, val block: Node) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "$options ->\n $block"
    }
}