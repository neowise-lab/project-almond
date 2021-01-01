package com.neowise.almond.semantics.metadata

class PackageMetadata(private val location: String) {

    private val modules = HashMap<String, ModuleMetadata>()

    fun add(name: String): ModuleMetadata {
        val moduleMetadata = ModuleMetadata(location, name)
        modules[name] = moduleMetadata
        return moduleMetadata
    }

    fun exists(name: String) : Boolean {
        return modules.containsKey(name)
    }

    fun get(name: String): ModuleMetadata {
        return modules[name]!!
    }

    fun getAll() : Map<String, ModuleMetadata> = modules

    override fun toString(): String {
        return buildString {
            append("package ${location}\n\n")
            for((_, module) in modules) {
                append("$module \n")
            }
        }
    }
}
