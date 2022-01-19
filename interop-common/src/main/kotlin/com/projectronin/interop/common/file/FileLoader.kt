package com.projectronin.interop.common.file

import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

/**
 * Capable of loading files from the classpath or underlying file system. Classpath resources should be prefixed with
 * "classpath:" or they will be treated as though they are on the file system.
 */
class FileLoader private constructor() {
    companion object {
        private val CLASSPATH_PREFIX = "classpath:"

        /**
         * Loads the supplied [fileName] from the classpath or underlying system and returns the contents of the file.
         * @throws FileNotFoundException if the file could not be found
         */
        @JvmStatic
        fun loadFile(fileName: String): String {
            if (fileName.startsWith(CLASSPATH_PREFIX)) {
                val classpathFilename = fileName.substring(CLASSPATH_PREFIX.length)
                val stream = this::class.java.classLoader.getResourceAsStream(classpathFilename)
                    ?: throw FileNotFoundException("$classpathFilename not found on classpath")
                return String(stream.readAllBytes())
            }

            val file = File(fileName)
            if (!file.exists()) {
                throw FileNotFoundException("$fileName does not exist")
            }
            return FileReader(file).use { it.readText() }
        }
    }
}
