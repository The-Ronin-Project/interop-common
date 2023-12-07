package com.projectronin.interop.common.http.ktor

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutputStreamContent
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelinePhase
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.io.OutputStream
import java.util.concurrent.TimeUnit

class ContentLengthSupplierTest {
    private val testOutputPhase = PipelinePhase("TestOutput")

    private val mockEngine =
        MockEngine {
            respond(
                content = "OK",
                status = HttpStatusCode.OK,
            )
        }

    private val httpClient =
        HttpClient(mockEngine) {
            install(ContentLengthSupplier)
        }

    @Test
    fun `key is set appropriately`() {
        assertEquals(
            AttributeKey<ContentLengthSupplier>("ContentLengthSupplier"),
            ContentLengthSupplier.key,
        )
    }

    @Test
    fun `prepare returns the plugin`() {
        assertNotNull(ContentLengthSupplier.prepare())
    }

    @Test
    fun `continues on with non OutputStreamContent`() {
        val content = ByteArrayContent("content".toByteArray())
        runPipelineAndAssertOutput(content) {
            assertEquals(it, content)
        }
    }

    @Test
    fun `continues on for OutputStreamContent with ContentLength set`() {
        val content = OutputStreamContent({ }, ContentType.Text.Plain, contentLength = 10)
        runPipelineAndAssertOutput(content) {
            assertEquals(it, content)
        }
    }

    @Test
    fun `transforms an OutputStreamContent with no ContentLength set`() {
        val expectedText = "this is my test"
        val outputStreamUnit: (OutputStream) -> Unit = {
            it.write(expectedText.toByteArray())
        }
        val inputContent = OutputStreamContent(outputStreamUnit, ContentType.Text.Plain)
        runPipelineAndAssertOutput(inputContent) { output ->
            output as ByteArrayContent
            assertEquals(expectedText, String(output.bytes()))
            assertEquals(ContentType.Text.Plain, output.contentType)
            assertEquals(expectedText.length.toLong(), output.contentLength)
        }
    }

    @Test
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    fun `handles very large reads of the OutputStreamContent`() {
        val expectedText = "x".repeat(1_000_000)
        val outputStreamUnit: (OutputStream) -> Unit = {
            it.write(expectedText.toByteArray())
        }
        val inputContent = OutputStreamContent(outputStreamUnit, ContentType.Text.Plain)
        runPipelineAndAssertOutput(inputContent) { output ->
            output as ByteArrayContent
            assertEquals(expectedText, String(output.bytes()))
            assertEquals(ContentType.Text.Plain, output.contentType)
            assertEquals(expectedText.length.toLong(), output.contentLength)
        }
    }

    private fun runPipelineAndAssertOutput(
        input: Any,
        assert: (Any) -> Unit,
    ) {
        httpClient.requestPipeline.insertPhaseAfter(ContentLengthSupplier.contentLengthPhase, testOutputPhase)
        httpClient.requestPipeline.intercept(testOutputPhase) { content ->
            assert(content)

            proceedWith(content)
        }

        runBlocking {
            httpClient.requestPipeline.execute(HttpRequestBuilder(), input)
        }
    }
}
