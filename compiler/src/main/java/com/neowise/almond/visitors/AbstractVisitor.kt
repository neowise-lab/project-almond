package com.neowise.almond.visitors

import com.neowise.almond.parser.ast.Block
import com.neowise.almond.parser.ast.NodeList
import com.neowise.almond.parser.ast.Program
import com.neowise.almond.parser.ast.expressions.*
import com.neowise.almond.parser.ast.statements.*

abstract class AbstractVisitor : Visitor {

    override fun visit(program: Program) {
        program.nodes.accept(this)
    }

    override fun visit(block: Block) {
        for(node in block) {
            node.accept(this)
        }
    }

    override fun visit(nodeList: NodeList) {
        for(node in nodeList) {
            node.accept(this)
        }
    }

    override fun visit(struct: StructStatement) {
        struct.functions.accept(this)
    }

    override fun visit(variableDefine: VariableDefineStatement) {
        variableDefine.expression.accept(this)
    }

    override fun visit(functionDefine: FunctionDefineStatement) {
        functionDefine.body.accept(this)
    }

    override fun visit(structFunctionDefine: StructFunctionDefineStatement) {
        structFunctionDefine.body.accept(this)
    }

    override fun visit(assignment: AssignmentStatement) {
        assignment.variable.accept(this)
        assignment.expression.accept(this)
    }

    override fun visit(returnStatement: ReturnStatement) {
        returnStatement.expression.accept(this)
    }

    override fun visit(ifElse: IfElseStatement) {
        ifElse.condition.accept(this)
        ifElse.ifBody.accept(this)
        ifElse.elseBody.accept(this)
    }

    override fun visit(forTo: ForStatement) {
        forTo.initialValue.accept(this)
        forTo.finalValue.accept(this)
        forTo.body.accept(this)
    }

    override fun visit(foreach: ForeachStatement) {
        foreach.collection.accept(this)
        foreach.body.accept(this)
    }

    override fun visit(foreachMap: ForeachMapStatement) {
        foreachMap.map.accept(this)
        foreachMap.body.accept(this)
    }

    override fun visit(repeat: RepeatStatement) {
        repeat.condition.accept(this)
        repeat.body.accept(this)
    }

    override fun visit(until: DoUntilStatement) {
        until.condition.accept(this)
        until.body.accept(this)
    }

    override fun visit(match: MatchStatement) {
        match.expression.accept(this)
        for (matchCase in match.cases) {
            matchCase.value?.accept(this)
            matchCase.body.accept(this)
        }
    }

    override fun visit(error: ErrorStatement) {
        error.expression.accept(this)
    }

    override fun visit(extract: ExtractStatement) {
        extract.extractingValue.accept(this)
    }

    override fun visit(expression: ExpressionStatement) {
        expression.expression.accept(this)
    }

    override fun visit(ternary: TernaryExpression) {
        ternary.condition.accept(this)
        ternary.trueExpr.accept(this)
        ternary.falseExpr.accept(this)
    }

    override fun visit(binary: BinaryExpression) {
        binary.expr1.accept(this)
        binary.expr2.accept(this)
    }

    override fun visit(unary: UnaryExpression) {
        unary.value.accept(this)
    }

    override fun visit(functionalChain: FunctionalChainExpression) {
        functionalChain.expression.accept(this)
        functionalChain.arguments.accept(this)
    }

    override fun visit(fieldAccess: FieldAccessExpression) {
        fieldAccess.expression.accept(this)
    }

    override fun visit(arrayAccess: ArrayAccessExpression) {
        arrayAccess.target.accept(this)
        arrayAccess.index.accept(this)
    }

    override fun visit(objectFunctionReference: ObjectFunctionReferenceExpression) {
        objectFunctionReference.target.accept(this)
    }

    override fun visit(newInstance: NewInstanceExpression) {
        newInstance.arguments.accept(this)
    }

    override fun visit(arrayExpression: ArrayExpression) {
        arrayExpression.elements.accept(this)
    }

    override fun visit(map: MapExpression) {
        map.keys.accept(this)
        map.values.accept(this)
    }

    override fun visit(lambda: LambdaExpression) {
        lambda.block.accept(this)
    }

    override fun visit(using: UsingStatement) { }

    override fun visit(value: ValueExpression) { }

    override fun visit(unknownWords: UnknownWordsExpression) { }

    override fun visit(functionReference: FunctionReferenceExpression) { }

    override fun visit(breakStmt: BreakStatement) { }

    override fun visit(continueStmt: ContinueStatement) { }
}