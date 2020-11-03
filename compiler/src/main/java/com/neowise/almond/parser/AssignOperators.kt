package com.neowise.almond.parser

import com.neowise.almond.parser.lexer.TokenType

object AssignOperators {

    private val operators = hashMapOf(
            TokenType.EQ to Operator.SET,
            TokenType.PLUSEQ to Operator.ADD,
            TokenType.MINUSEQ to Operator.SUBTRACT,
            TokenType.STAREQ to Operator.MULTIPLY,
            TokenType.SLASHEQ to Operator.DIVIDE,
            TokenType.PERCENTEQ to Operator.REMAINDER,
            TokenType.AMPEQ to Operator.AND,
            TokenType.CARETEQ to Operator.XOR,
            TokenType.BAREQ to Operator.OR,
            TokenType.COLONCOLONEQ to Operator.PUSH,
            TokenType.LTLTEQ to Operator.LSHIFT,
            TokenType.GTGTEQ to Operator.RSHIFT,
            TokenType.GTGTGTEQ to Operator.URSHIFT
    )

    fun contains(type: TokenType) = operators.containsKey(type)

    operator fun get(type: TokenType) = operators[type]
}