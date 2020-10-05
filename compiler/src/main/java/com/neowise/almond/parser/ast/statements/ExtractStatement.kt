package com.neowise.almond.parser.ast.statements

import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.visitors.Visitor

class ExtractStatement(options: Options, extractingValue: Node) : Node {
    override fun accept(visitor: Visitor) {
        visitor.visit(this)
    }

}
