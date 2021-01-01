package com.neowise.almond.parser

import bytecode.Instruction;

enum class Operator(val opcode: Instruction) {
    ADD(Instruction.ADD),
    SUBTRACT(Instruction.SUB),
    MULTIPLY(Instruction.MULT),
    DIVIDE(Instruction.DIV),
    REMAINDER(Instruction.REM),
    AND(Instruction.AND),
    XOR(Instruction.XOR),
    OR(Instruction.OR),
    PUSH(Instruction.PUSH),
    LSHIFT(Instruction.LSHIFT),
    RSHIFT(Instruction.RSHIFT),
    URSHIFT(Instruction.URSHIFT),
    INC(Instruction.INC),
    DEC(Instruction.DEC),
    EQUALS(Instruction.EQ),
    NOT_EQUALS(Instruction.NTEQ),
    LT(Instruction.LT),
    LTEQ(Instruction.LTEQ),
    GT(Instruction.GT),
    GTEQ(Instruction.GTEQ),
    NEGATE(Instruction.NEG),
    NOT(Instruction.NEG),
    COMPLEMENT(Instruction.COMP),
    IS(Instruction.IS),
    SET(Instruction.SET);
}