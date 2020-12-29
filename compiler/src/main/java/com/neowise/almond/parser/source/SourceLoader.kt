package com.neowise.almond.parser.source

import java.io.File
import java.io.FileInputStream

class SourceLoader {

    fun fromResource(location: String, name: String): Source {
        println("resource $name")
        val resource = SourceLoader::class.java.getResourceAsStream("/$name")
        val bytes = ByteArray(resource.available())
        resource.read(bytes)
        resource.close()
        println(name.withoutExtension())
        return Source(location, name.withoutExtension(), String(bytes))
    }

    fun fromFileSystem(location: String): Source {
        val file = File(location)
        val fis = FileInputStream(location)
        val bytes = ByteArray(fis.available())
        fis.read(bytes)
        fis.close()
        return Source(location, file.name.withoutExtension(), String(bytes))
    }

    private fun String.withoutExtension(): String {
        return replaceFirst("[.][^.]+$", "")
    }
}