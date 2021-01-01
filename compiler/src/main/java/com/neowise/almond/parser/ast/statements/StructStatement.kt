package com.neowise.almond.parser.ast.statements;

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.parser.lexer.Token;
import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

class StructStatement(val name: Token, val options: Options, val functions: List<FunctionDefineStatement>) : Node {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

    override fun accept(visitor: ResultVisitor): Node {
        return visitor.visit(this)
    }

    override fun toString(): String {
        return "struct $name$options" + if (functions.isEmpty()) "" else functionsToString()
    }

    private fun functionsToString(): String {
        return buildString {
            functions.forEach {
                append(" { \n")
                append("\t $it \n")
                append(" } \n")
            }
        }
    }
}
