package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class UsingStatement(val text: Token) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}