package com.projectronin.interop.common.spring

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutputStreamContent
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelinePhase
import io.ktor.utils.io.ByteChannel
import kotlinx.coroutines.runBlocking

/**
 * Ktor plugin ensuring that the Content-Length header is appropriately set when an OutputStreamContent is encountered.
 */
class ContentLengthSupplier {
    companion object Plugin : HttpClientPlugin<HttpCallValidator.Config, ContentLengthSupplier> {
        val contentLengthPhase = PipelinePhase("SetContentLength")
        override val key: AttributeKey<ContentLengthSupplier> = AttributeKey("ContentLengthSupplier")

        override fun prepare(block: HttpCallValidator.Config.() -> Unit): ContentLengthSupplier {
            return ContentLengthSupplier()
        }

        override fun install(plugin: ContentLengthSupplier, scope: HttpClient) {
            scope.requestPipeline.insertPhaseAfter(HttpRequestPipeline.Transform, contentLengthPhase)

            scope.requestPipeline.intercept(contentLengthPhase) { content ->
                if (content is OutputStreamContent && content.contentLength == null) {
                    val byteChannel = ByteChannel(true)
                    content.writeTo(byteChannel)
                    val byteArray = byteChannel.readAll()
                    val newContent = ByteArrayContent(byteArray, content.contentType)
                    proceedWith(newContent)
                } else {
                    proceedWith(content)
                }
            }
        }

        private const val MAX_READ = 1024
        private fun ByteChannel.readAll(): ByteArray {
            val stringBuffer = StringBuffer(MAX_READ)

            var offset = 0
            val byteArray = ByteArray(MAX_READ)
            runBlocking {
                while (true) {
                    val read = readAvailable(byteArray, 0, byteArray.size)
                    if (read <= 0) break

                    stringBuffer.append(String(byteArray.copyOfRange(0, read)))
                    offset += read

                    if (read < MAX_READ) break
                }
            }
            return stringBuffer.toString().toByteArray()
        }
    }
}
