package com.neowise.almond.parser.ast

import com.neowise.almond.visitors.Visitor

interface Node {
    fun accept(visitor: Visitor)
}