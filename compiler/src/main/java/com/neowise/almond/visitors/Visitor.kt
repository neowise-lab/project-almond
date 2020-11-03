package com.neowise.almond.visitors

import com.neowise.almond.parser.ast.expressions.ObjectFunctionReferenceExpression
import com.neowise.almond.parser.ast.Block
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.parser.ast.Program
import com.neowise.almond.parser.ast.expressions.*
import com.neowise.almond.parser.ast.statements.*

interface Visitor {
    fun visit(program: Program)
    fun visit(block: Block)
    fun visit(nodeList: NodeList)

    fun visit(using: UsingStatement)
    fun visit(struct: StructStatement)
    fun visit(variableDefine: VariableDefineStatement)
    fun visit(functionDefine: FunctionDefineStatement)
    fun visit(structFunctionDefine: StructFunctionDefineStatement)
    fun visit(assignment: AssignmentStatement)

    fun visit(returnStatement: ReturnStatement)

    fun visit(ifElse: IfElseStatement)
    fun visit(forTo: ForStatement)
    fun visit(foreach: ForeachStatement)
    fun visit(foreachMap: ForeachMapStatement)

    fun visit(repeat: RepeatStatement)
    fun visit(until: UntilStatement)
    fun visit(match: MatchStatement)
    fun visit(error: ErrorStatement)
    fun visit(extract: ExtractStatement)

    fun visit(expression: ExpressionStatement)

    fun visit(ternary: TernaryExpression)
    fun visit(binary: BinaryExpression)
    fun visit(unary: UnaryExpression)
    fun visit(value: ValueExpression)
    fun visit(unknownWords: UnknownWordsExpression)
    fun visit(functionalChain: FunctionalChainExpression)
    fun visit(fieldAccess: FieldAccessExpression)
    fun visit(arrayAccess: ArrayAccessExpression)
    fun visit(objectFunctionReference: ObjectFunctionReferenceExpression)
    fun visit(functionReference: FunctionReferenceExpression)
    fun visit(newInstance: NewInstanceExpression)
    fun visit(arrayExpression: ArrayExpression)
    fun visit(map: MapExpression)
    fun visit(lambda: LambdaExpression)
}