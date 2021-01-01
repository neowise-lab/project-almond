package com.neowise.almond.semantics.metadata

class Metadata {

    private val packages = HashMap<String, PackageMetadata>()

    fun add(location: String): PackageMetadata {
        val packageMetadata = PackageMetadata(location)
        packages[location] = packageMetadata
        return packageMetadata
    }

    fun exists(location: String) : Boolean {
        return packages.containsKey(location)
    }

    fun get(location: String) : PackageMetadata {
        return packages[location]!!
    }

    override fun toString(): String {
        return buildString {
            append("#metadata\n\n")
            for((_, pack) in packages) {
                append("$pack \n")
                append("-".repeat(25))
            }
        }
    }
}