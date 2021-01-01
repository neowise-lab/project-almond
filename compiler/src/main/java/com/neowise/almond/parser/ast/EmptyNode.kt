package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

object EmptyNode : Node {
    override fun accept(visitor: Visitor) {}
    override fun accept(visitor: ResultVisitor): Node = this
}