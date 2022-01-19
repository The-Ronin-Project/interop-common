package com.projectronin.interop.common.file

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.FileNotFoundException

class FileLoaderTest {
    @Test
    fun `throws exception when classpath file is not found`() {
        val exception = assertThrows<FileNotFoundException> { FileLoader.loadFile("classpath:unknown.txt") }
        assertEquals("unknown.txt not found on classpath", exception.message)
    }

    @Test
    fun `loads top level classpath file`() {
        val contents = FileLoader.loadFile("classpath:Sample1.txt")
        assertEquals("This is the first sample.\n", contents)
    }

    @Test
    fun `loads nested classpath file`() {
        val contents = FileLoader.loadFile("classpath:nested/Sample2.txt")
        assertEquals("This is the second, nested sample.\n", contents)
    }

    @Test
    fun `throws exception when file system file is not found`() {
        val exception = assertThrows<FileNotFoundException> { FileLoader.loadFile("/User/Unknown/unknown.txt") }
        assertEquals("/User/Unknown/unknown.txt does not exist", exception.message)
    }

    @Test
    fun `loads nested file system file`() {
        // First we have to get the absolute path to the resource to ensure this works for every system
        val url = this.javaClass.classLoader.getResource("Sample1.txt")
        val contents = FileLoader.loadFile(url.path)
        assertEquals("This is the first sample.\n", contents)
    }
}
