package com.neowise.almond.visitors

import com.neowise.almond.parser.ast.expressions.ObjectFunctionReferenceExpression
import com.neowise.almond.parser.ast.Block
import com.neowise.almond.parser.ast.Node
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.parser.ast.Program
import com.neowise.almond.parser.ast.expressions.*
import com.neowise.almond.parser.ast.statements.*
import org.graalvm.compiler.graph.spi.Canonicalizable

abstract class ResultVisitor {

    fun visit(program: Program) : Node {
        program.nodes.accept(this)
        return program
    }

    fun visit(block: Block) : Node  {
        for(node in block) {
            node.accept(this)
        }
        return block
    }

    fun visit(nodeList: NodeList) : Node  {
        for(node in nodeList) {
            node.accept(this)
        }
        return nodeList
    }

    fun visit(struct: StructStatement) : Node  {
        for(function in struct.functions) {
            function.accept(this)
        }
        return struct
    }

    fun visit(variableDefine: VariableDefineStatement) : Node  {
        variableDefine.expression.accept(this)
        return variableDefine
    }

    fun visit(functionDefine: FunctionDefineStatement) : Node  {
        functionDefine.body.accept(this)
        return functionDefine
    }

    fun visit(assignment: AssignmentStatement) : Node {
        assignment.variable.accept(this)
        assignment.expression.accept(this)
        return assignment
    }

    fun visit(returnStatement: ReturnStatement) : Node  {
        returnStatement.expression.accept(this)
        return returnStatement
    }

    fun visit(ifElse: IfElseStatement) : Node  {
        ifElse.condition.accept(this)
        ifElse.ifBody.accept(this)
        ifElse.elseBody.accept(this)
        return ifElse
    }

    fun visit(forTo: ForStatement) : Node {
        forTo.initialValue.accept(this)
        forTo.finalValue.accept(this)
        forTo.body.accept(this)
        return forTo
    }

    fun visit(foreach: ForeachStatement) : Node  {
        foreach.collection.accept(this)
        foreach.body.accept(this)
        return foreach
    }

    fun visit(foreachMap: ForeachMapStatement) : Node  {
        foreachMap.map.accept(this)
        foreachMap.body.accept(this)
        return foreachMap
    }

    fun visit(repeat: RepeatStatement) : Node  {
        repeat.condition.accept(this)
        repeat.body.accept(this)
        return repeat
    }

    fun visit(until: DoUntilStatement) : Node {
        until.condition.accept(this)
        until.body.accept(this)
        return until
    }

    fun visit(match: MatchStatement) : Node  {
        match.expression.accept(this)
        for (matchCase in match.cases) {
            matchCase.value?.accept(this)
            matchCase.body.accept(this)
        }
        return match
    }

    fun visit(error: ErrorStatement) : Node  {
        error.expression.accept(this)
        return error
    }

    fun visit(extract: ExtractStatement) : Node  {
        extract.extractingValue.accept(this)
        return extract
    }

    fun visit(expression: ExpressionStatement) : Node  {
        expression.expression.accept(this)
        return expression
    }

    fun visit(ternary: TernaryExpression) : Node  {
        ternary.condition.accept(this)
        ternary.trueExpr.accept(this)
        ternary.falseExpr.accept(this)
        return ternary
    }

    fun visit(binary: BinaryExpression) : Node  {
        binary.expr1.accept(this)
        binary.expr2.accept(this)
        return binary
    }

    fun visit(unary: UnaryExpression) : Node  {
        unary.value.accept(this)
        return unary
    }

    fun visit(functionalChain: FunctionalChainExpression) : Node  {
        functionalChain.expression.accept(this)
        functionalChain.arguments.accept(this)
        return functionalChain
    }

    fun visit(fieldAccess: FieldAccessExpression) : Node  {
        fieldAccess.expression.accept(this)
        return fieldAccess
    }

    fun visit(arrayAccess: ArrayAccessExpression) : Node  {
        arrayAccess.target.accept(this)
        arrayAccess.index.accept(this)
        return arrayAccess
    }

    fun visit(objectFunctionReference: ObjectFunctionReferenceExpression) : Node  {
        objectFunctionReference.target.accept(this)
        return objectFunctionReference
    }

    fun visit(newInstance: NewInstanceExpression) : Node  {
        newInstance.arguments.accept(this)
        return newInstance
    }

    fun visit(arrayExpression: ArrayExpression) : Node  {
        arrayExpression.elements.accept(this)
        return arrayExpression
    }

    fun visit(map: MapExpression) : Node  {
        map.keys.accept(this)
        map.values.accept(this)
        return map
    }

    fun visit(lambda: LambdaExpression) : Node  {
        lambda.block.accept(this)
        return lambda
    }

    fun visit(using: UsingStatement) : Node  { return using }

    fun visit(value: ValueExpression) : Node  { return value }

    fun visit(unknownWords: UnknownWordsExpression) : Node  { return unknownWords }

    fun visit(functionReference: FunctionReferenceExpression) : Node  { return functionReference}

    fun visit(breakStmt: BreakStatement) : Node  { return breakStmt }

    fun visit(continueStmt: ContinueStatement) : Node  { return continueStmt }
}