package com.neowise.almond.parser.ast.expressions

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class FieldAccessExpression(val expr: Node, val word: Token) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}