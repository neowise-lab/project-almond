package com.neowise.almond.semantics

import com.neowise.almond.exceptions.ParseException
import com.neowise.almond.semantics.metadata.Member.*
import com.neowise.almond.parser.ast.Program
import com.neowise.almond.parser.ast.statements.FunctionDefineStatement
import com.neowise.almond.parser.ast.statements.StructStatement
import com.neowise.almond.parser.ast.statements.VariableDefineStatement
import com.neowise.almond.semantics.metadata.Metadata
import com.neowise.almond.semantics.metadata.ModuleMetadata
import com.neowise.almond.semantics.metadata.PackageMetadata
import com.neowise.almond.parser.lexer.Token
import com.neowise.almond.visitors.AbstractVisitor

class Logger : AbstractVisitor() {

    private val metadata = Metadata()
    private lateinit var module: ModuleMetadata

    fun log(vararg programs: Program) {
        for(program in programs) {
            program.accept(this)
        }
    }

    fun metadata(): Metadata {
        return metadata
    }

    override fun visit(program: Program) {
        val packageMetadata = getPackage(program.location)
        module = packageMetadata.add(program.name)
        super.visit(program)
    }

    override fun visit(struct: StructStatement) {
        addMember(struct.name, Type.STRUCT)
    }

    override fun visit(variableDefine: VariableDefineStatement) {
        addMember(variableDefine.name, if (variableDefine.isConst) Type.CONSTANT else Type.VARIABLE)
    }

    override fun visit(functionDefine: FunctionDefineStatement) {
        addMember(functionDefine.name, Type.FUNCTION)
    }

    private fun addMember(name: Token, type: Type) {
        if (module.memberExists(name.text)) {
            error(name, "member '${name.text}' already defined")
        }
        module.addMember(name.text, type)
    }

    private fun getPackage(location: String) : PackageMetadata {
        return if (metadata.exists(location)) metadata.get(location) else metadata.add(location)
    }

    private fun error(token: Token, message: String) {
        throw ParseException("Logger", token, "$message in module ${module.name()}")
    }
}