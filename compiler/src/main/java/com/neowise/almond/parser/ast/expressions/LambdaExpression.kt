package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.visitors.Visitor

class LambdaExpression(options: Options, block: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}