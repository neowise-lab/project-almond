package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.ResultVisitor
import com.neowise.almond.visitors.Visitor

interface Node {
    fun accept(visitor: Visitor)
    fun accept(visitor: ResultVisitor) : Node
}