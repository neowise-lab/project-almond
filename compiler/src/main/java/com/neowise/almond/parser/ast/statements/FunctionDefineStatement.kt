package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

open class FunctionDefineStatement(val name: Token, val options: Options, val body: Node) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "func $name$options $body"
    }
}