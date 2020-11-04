package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor
import kotlin.math.exp

class VariableDefineStatement(val name: Token, val expression: Node, val isConst: Boolean) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return (if (isConst) "const" else "var") + " $name = $expression"
    }
}
