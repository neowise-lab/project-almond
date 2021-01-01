package com.neowise.almond.semantics

import com.neowise.almond.exceptions.ParseException
import com.neowise.almond.semantics.metadata.Member
import com.neowise.almond.semantics.metadata.Metadata
import com.neowise.almond.semantics.metadata.ModuleMetadata
import com.neowise.almond.parser.ast.Options
import com.neowise.almond.parser.ast.Program
import com.neowise.almond.parser.ast.expressions.*
import com.neowise.almond.parser.ast.statements.*
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.AbstractVisitor
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SemanticAnalyzer(private val metadata: Metadata) : AbstractVisitor() {

    private lateinit var currentModule : ModuleMetadata

    private val usings = HashMap<String, ModuleMetadata>()
    private val usedMembers = HashMap<String, Member>()
    private val codeScopes = Stack<CodeScope>()
    private val variables = Variables()

    override fun visit(program: Program) {
        usings.clear()
        usedMembers.clear()
        codeScopes.push(CodeScope.GLOBAL)
        currentModule = metadata.get(program.location).get(program.name)
        importMembers(currentModule)

        super.visit(program)
    }

    override fun visit(using: UsingStatement) {

        // search defines in aliases
        if (usings.containsKey(using.name)) {
            error(using.token, "using '${using.name}' already defined!")
        }
        // search package
        if (!metadata.exists(using.packageName)) {
            error(using.token, "package '${using.packageName}' not exists")
        }
        // search module in package
        val pack = metadata.get(using.packageName)
        if (pack.exists(using.name)) {
            error(using.token, "module '${using.name}' not exists in package '${using.packageName}'")
        }
        // import module members
        val module = pack.get(using.name)
        importMembers(module)
        usings[using.name] = module
    }

    private fun importMembers(module : ModuleMetadata) {
        for((name, member) in module.allMembers()) {
            usedMembers[name] = member
        }
    }

    override fun visit(struct: StructStatement) {
        scope(CodeScope.STRUCT) {
            val structName = struct.name.text
            checkDuplicateOptions(struct.options, "struct $structName ")
            checkDuplicateFunctions(struct.functions, structName)
            super.visit(struct)
        }
    }

    private fun checkDuplicateFunctions(functions: List<FunctionDefineStatement>, struct: String) {
        val validFunctions = ArrayList<String>()
        // check function duplicates
        for(function in functions) {
            val name = function.name
            if (name.text in validFunctions) {
                error(name, "duplicate function define '${name.text}' in struct '$struct'")
            }
            else {
                validFunctions.add(name.text)
                // if function is valid, check function options
                checkDuplicateOptions(function.options, "function $struct::${name.text}")
            }
        }
    }

    private fun checkDuplicateOptions(options: Options, scope: String) {
        val validOptions = ArrayList<String>()
        // check option duplicates
        for(option in options) {
            if (option.text in validOptions) {
                error(option, "duplicate option '${option.text}' in '$scope'")
            }
            else {
                validOptions += option.text
            }
        }
    }

    override fun visit(variableDefine: VariableDefineStatement) {
        if (!isScope(CodeScope.GLOBAL)) {
            val name = variableDefine.name
            if (variables.exists(name.text)) {
                error(name, "variable '${name.text}' already defined!")
            }
            variables.add(name.text, variableDefine.isConst)
        }
        super.visit(variableDefine)
    }

    override fun visit(functionDefine: FunctionDefineStatement) {
        scope(CodeScope.FUNCTION) {
            variables.clear()
            checkDuplicateOptions(functionDefine.options, "function ${functionDefine.name}")
            super.visit(functionDefine)
        }
    }

    override fun visit(assignment: AssignmentStatement) {
        super.visit(assignment)
    }

    override fun visit(returnStatement: ReturnStatement) {
        super.visit(returnStatement)
    }

    override fun visit(ifElse: IfElseStatement) {
        super.visit(ifElse)
    }

    override fun visit(forTo: ForStatement) {
        super.visit(forTo)
    }

    override fun visit(foreach: ForeachStatement) {
        super.visit(foreach)
    }

    override fun visit(foreachMap: ForeachMapStatement) {
        super.visit(foreachMap)
    }

    override fun visit(repeat: RepeatStatement) {
        super.visit(repeat)
    }

    override fun visit(until: DoUntilStatement) {
        super.visit(until)
    }

    override fun visit(match: MatchStatement) {
        super.visit(match)
    }

    override fun visit(continueStmt: ContinueStatement) {
        super.visit(continueStmt)
    }

    override fun visit(breakStmt: BreakStatement) {
        super.visit(breakStmt)
    }

    override fun visit(error: ErrorStatement) {
        super.visit(error)
    }

    override fun visit(extract: ExtractStatement) {
        super.visit(extract)
    }

    override fun visit(expression: ExpressionStatement) {
        super.visit(expression)
    }

    override fun visit(ternary: TernaryExpression) {
        super.visit(ternary)
    }

    override fun visit(binary: BinaryExpression) {
        super.visit(binary)
    }

    override fun visit(unary: UnaryExpression) {
        super.visit(unary)
    }

    override fun visit(functionalChain: FunctionalChainExpression) {
        super.visit(functionalChain)
    }

    override fun visit(fieldAccess: FieldAccessExpression) {
        super.visit(fieldAccess)
    }

    override fun visit(arrayAccess: ArrayAccessExpression) {
        super.visit(arrayAccess)
    }

    override fun visit(objectFunctionReference: ObjectFunctionReferenceExpression) {
        super.visit(objectFunctionReference)
    }

    override fun visit(newInstance: NewInstanceExpression) {
        super.visit(newInstance)
    }

    override fun visit(lambda: LambdaExpression) {
        super.visit(lambda)
    }

    override fun visit(value: ValueExpression) {
        super.visit(value)
    }

    override fun visit(unknownWords: UnknownWordsExpression) {
        super.visit(unknownWords)
    }

    override fun visit(functionReference: FunctionReferenceExpression) {
        super.visit(functionReference)
    }

    private fun scope(scope: CodeScope, code: () -> Unit) {
        codeScopes.push(scope)
        code()
        codeScopes.pop()
    }

    private fun isScope(scope: CodeScope) = currentScope() == scope

    private fun currentScope() = codeScopes.peek()

    private fun error(token: Token, message: String) {
        throw ParseException("SemanticAnalyzer", token, message, currentModule.name())
    }

    private data class Var(
        val name: String,
        val isConst: Boolean,
        val level: Int,
        val index: Int
    )

    private class Variables {

        private lateinit var list: ArrayList<Var>
        private val varScope = Stack<ArrayList<Var>>()

        fun add(name: String, isConst: Boolean) {
            val level = varScope.size
            val index = list.size

            list.add(Var(name, isConst, level, index))
        }

        fun exists(name: String): Boolean = find(name) != null

        fun get(name: String): Var = find(name)!!

        fun find(name: String): Var? {
            for(scope in varScope) {
                for(variable in scope) {
                    if (variable.name == name) {
                        return variable
                    }
                }
            }
            return null
        }

        fun enter() {
            list = ArrayList()
            varScope.push(list)
        }

        fun exit() {
            varScope.pop()
            list = varScope.peek()
        }

        fun clear() {
            varScope.clear()
            list.clear()
        }
    }

    private enum class CodeScope {
        GLOBAL,
        STRUCT,
        FUNCTION,
        LAMBDA,
        LOOP,
        MATCH,
    }
}