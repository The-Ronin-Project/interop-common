package com.projectronin.interop.common.http.ktor

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpCallValidator
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutputStreamContent
import io.ktor.util.AttributeKey
import io.ktor.util.pipeline.PipelinePhase
import io.ktor.utils.io.core.readText
import io.ktor.utils.io.writer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mu.KotlinLogging

/**
 * Ktor plugin ensuring that the Content-Length header is appropriately set when an OutputStreamContent is encountered.
 */
class ContentLengthSupplier {
    companion object Plugin : HttpClientPlugin<HttpCallValidator.Config, ContentLengthSupplier> {
        private val logger = KotlinLogging.logger { }

        val contentLengthPhase = PipelinePhase("SetContentLength")
        private val coroutineScope = CoroutineScope(Dispatchers.Main)
        override val key: AttributeKey<ContentLengthSupplier> = AttributeKey("ContentLengthSupplier")

        override fun prepare(block: HttpCallValidator.Config.() -> Unit): ContentLengthSupplier {
            return ContentLengthSupplier()
        }

        override fun install(plugin: ContentLengthSupplier, scope: HttpClient) {
            scope.requestPipeline.insertPhaseAfter(HttpRequestPipeline.Transform, contentLengthPhase)

            scope.requestPipeline.intercept(contentLengthPhase) { content ->
                if (content is OutputStreamContent && content.contentLength == null) {
                    logger.debug { "Found an OutputStreamContent with no ContentLength" }

                    val readChannel = coroutineScope.writer(Dispatchers.IO) {
                        content.writeTo(channel)
                    }.channel

                    val text = runCatching { readChannel.readRemaining().readText(Charsets.UTF_8) }.getOrElse {
                        logger.error(it) { "Error while calculating the Content-Length" }
                        throw it
                    }

                    val byteArrayContent = ByteArrayContent(text.toByteArray(), content.contentType)
                    proceedWith(byteArrayContent)
                } else {
                    proceedWith(content)
                }
            }
        }
    }
}
