package com.neowise.almond.semantics.metadata

class Member(
        val location: String,
        val module: String,
        val name: String,
        val type: Type
) {

    override fun toString(): String = type.name.toLowerCase() + " $name"

    enum class Type {
        STRUCT,
        VARIABLE,
        CONSTANT,
        FUNCTION
    }
}