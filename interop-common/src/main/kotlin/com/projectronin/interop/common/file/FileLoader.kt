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
        private const val CLASSPATH_PREFIX = "classpath:"
        private const val FILE_PREFIX = "file:"

        /**
         * Loads the supplied [fileName] from the classpath or underlying system and returns the contents of the file.
         * @throws FileNotFoundException if the file could not be found
         */
        @JvmStatic
        fun loadFile(fileName: String): String {
            if (fileName.startsWith(CLASSPATH_PREFIX)) {
                val classpathFilename = fileName.substring(CLASSPATH_PREFIX.length)
                return loadClasspathFile(classpathFilename)
                    ?: throw FileNotFoundException("$classpathFilename not found on classpath")
            }
            if (fileName.startsWith(FILE_PREFIX)) {
                val fileFilename = fileName.substring(FILE_PREFIX.length)
                return loadSystemFile(fileFilename) ?: throw FileNotFoundException("$fileFilename not found on system")
            }

            // Since it wasn't prefixed, we'll attempt to find it on the Classpath first, and then fall back to the System.
            val contents = loadClasspathFile(fileName)
            contents?.let { return it }

            return loadSystemFile(fileName) ?: throw FileNotFoundException("$fileName does not exist")
        }

        private fun loadClasspathFile(fileName: String): String? {
            val stream = this::class.java.classLoader.getResourceAsStream(fileName) ?: return null
            return String(stream.readAllBytes())
        }

        private fun loadSystemFile(fileName: String): String? {
            val file = File(fileName)
            if (!file.exists()) {
                return null
            }
            return FileReader(file).use { it.readText() }
        }
    }
}
