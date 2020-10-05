package com.neowise.almond.parser.source

import java.io.FileInputStream

class SourceLoader {

    fun fromResource(location: String): Source {
        val resource = SourceLoader::class.java.getResourceAsStream(location)
        val bytes = ByteArray(resource.available())
        resource.read(bytes)
        resource.close()
        return Source(String(bytes))
    }

    fun fromFileSystem(location: String): Source {
        val fis = FileInputStream(location)
        val bytes = ByteArray(fis.available())
        fis.read(bytes)
        fis.close()
        return Source(String(bytes))
    }
}