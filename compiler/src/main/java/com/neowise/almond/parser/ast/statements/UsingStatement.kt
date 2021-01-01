package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class UsingStatement(val token: Token) : Node {

    // using "path/to/pack:module"
    private val fullName = token.text
    val packageName = fullName.substring(0..fullName.lastIndexOf(':'))
    val name = fullName.substring(fullName.lastIndexOf(":"))

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "using \"${token.text}\""
    }
}