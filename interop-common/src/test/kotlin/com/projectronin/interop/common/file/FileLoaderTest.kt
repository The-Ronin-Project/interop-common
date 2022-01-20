package com.projectronin.interop.common.file

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.FileNotFoundException

class FileLoaderTest {
    @Test
    fun `throws exception when classpath-prefixed file is not found`() {
        val exception = assertThrows<FileNotFoundException> { FileLoader.loadFile("classpath:unknown.txt") }
        assertEquals("unknown.txt not found on classpath", exception.message)
    }

    @Test
    fun `loads top level classpath-prefixed file`() {
        val contents = FileLoader.loadFile("classpath:Sample1.txt")
        assertEquals("This is the first sample.\n", contents)
    }

    @Test
    fun `loads nested classpath-prefixed file`() {
        val contents = FileLoader.loadFile("classpath:nested/Sample2.txt")
        assertEquals("This is the second, nested sample.\n", contents)
    }

    @Test
    fun `throws exception when file-prefixed file is not found`() {
        val exception = assertThrows<FileNotFoundException> { FileLoader.loadFile("file:/User/Unknown/unknown.txt") }
        assertEquals("/User/Unknown/unknown.txt not found on system", exception.message)
    }

    @Test
    fun `loads file-prefixed file`() {
        // First we have to get the absolute path to the resource to ensure this works for every system
        val url = this.javaClass.classLoader.getResource("Sample1.txt")
        val contents = FileLoader.loadFile("file:${url.path}")
        assertEquals("This is the first sample.\n", contents)
    }

    @Test
    fun `loads file from classpath`() {
        val contents = FileLoader.loadFile("Sample1.txt")
        assertEquals("This is the first sample.\n", contents)
    }

    @Test
    fun `loads file from system`() {
        // First we have to get the absolute path to the resource to ensure this works for every system
        val url = this.javaClass.classLoader.getResource("Sample1.txt")
        val contents = FileLoader.loadFile(url.path)
        assertEquals("This is the first sample.\n", contents)
    }

    @Test
    fun `throws exception when file not found on classpath or system`() {
        val exception = assertThrows<FileNotFoundException> { FileLoader.loadFile("/unknown.txt") }
        assertEquals("/unknown.txt does not exist", exception.message)
    }
}
