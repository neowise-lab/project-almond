package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class ForeachMapStatement(val value: Token, val key: Token, val map: Node, val body: Node) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}