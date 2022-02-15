package com.projectronin.interop.common.exception
import com.projectronin.interop.common.exceptions.VendorIdentifierNotFoundException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VendorIdentifierNotFoundExceptionTest {
    @Test
    fun `VendorIdentifierNotFoundException allows for no parameters`() {
        val exception = VendorIdentifierNotFoundException()
        Assertions.assertEquals(null, exception.message)
        Assertions.assertEquals(null, exception.cause)
    }

    @Test
    fun `VendorIdentifierNotFoundException only message provided`() {
        val exceptionMessage = "Test exception message"
        val exception = VendorIdentifierNotFoundException(exceptionMessage)
        Assertions.assertEquals(exceptionMessage, exception.message)
        Assertions.assertEquals(null, exception.cause)
    }

    @Test
    fun `VendorIdentifierNotFoundException allows only cause provided`() {
        val causedByException = Exception()
        val exception = VendorIdentifierNotFoundException(causedByException)
        Assertions.assertEquals(null, exception.message)
        Assertions.assertEquals(causedByException, exception.cause)
    }

    @Test
    fun `VendorIdentifierNotFoundException allows all parameters`() {
        val causedByException = Exception()
        val exceptionMessage = "Test exception message"
        val exception = VendorIdentifierNotFoundException(exceptionMessage, causedByException)
        Assertions.assertEquals(exceptionMessage, exception.message)
        Assertions.assertEquals(causedByException, exception.cause)
    }
}
