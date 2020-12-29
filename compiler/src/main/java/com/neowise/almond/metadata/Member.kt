package com.neowise.almond.metadata

class Member(
        val location: String,
        val module: String,
        val name: String,
        val type: Type
) {

    override fun toString(): String {
        return type.name.toLowerCase() + " $name"
    }

    enum class Type {
        STRUCT,
        VARIABLE,
        CONSTANT,
        FUNCTION
    }
}