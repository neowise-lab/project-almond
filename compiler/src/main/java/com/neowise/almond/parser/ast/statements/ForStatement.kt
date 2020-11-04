package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class ForStatement(val variable: Token, val type: Type, val initialValue: Node, val finalValue: Node, val body: Node) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun toString(): String {
        return "for(${variable.text} : $initialValue $type $finalValue) $body"
    }

    enum class Type(val text: String) {
        TO("to"),
        DOWN_TO("downto");

        override fun toString(): String {
            return text;
        }
    }
}