package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.Visitor

object EmptyNode : Node {
    override fun accept(visitor: Visitor) {}
}