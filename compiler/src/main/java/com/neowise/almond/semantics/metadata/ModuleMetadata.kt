package com.neowise.almond.semantics.metadata

import com.neowise.almond.semantics.metadata.Member.Type

class ModuleMetadata(val location: String, val moduleName: String) {

    private val members = HashMap<String, Member>()

    fun addStruct(name: String) {
        addMember(name, Type.STRUCT)
    }

    fun addVariable(name: String, isConst: Boolean) {
        addMember(name, if (isConst) Type.CONSTANT else Type.VARIABLE)
    }

    fun addFunction(name: String) {
        addMember(name, Type.FUNCTION)
    }

    fun addMember(name: String, type: Type) {
        members[name] = Member(location, moduleName, name, type)
    }

    fun variableOrConstantExists(name: String): Boolean {
        return memberExists(name, Type.VARIABLE, Type.CONSTANT)
    }

    fun functionExists(name: String): Boolean {
        return memberExists(name, Type.FUNCTION)
    }

    fun structExists(name: String): Boolean {
        return memberExists(name, Type.STRUCT)
    }

    fun memberExists(name: String): Boolean {
        return members.containsKey(name)
    }

    private fun memberExists(name: String, vararg types: Type): Boolean {
        val member = members[name] ?: return false
        return member.type in types
    }

    fun allMembers(): Map<String, Member> = members

    fun name(): String = "$location/$moduleName"

    override fun toString(): String {
        return buildString {
            append("module $moduleName { \n")
            for((_, member) in members) {
                append("\t $member\n")
            }
            append("}")
        }
    }
}
