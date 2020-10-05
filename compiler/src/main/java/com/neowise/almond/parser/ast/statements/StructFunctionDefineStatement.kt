package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Block
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.Visitor

class StructFunctionDefineStatement
(
    val struct: Token,
    name: Token,
    options: Options,
    body: Node
) : FunctionDefineStatement(name, options, body)  {

    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }
}