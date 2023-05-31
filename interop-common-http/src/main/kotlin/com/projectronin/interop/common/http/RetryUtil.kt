package com.projectronin.interop.common.http

import io.ktor.client.request.HttpRequest
import io.ktor.client.request.HttpRequestBuilder

const val NO_RETRY_HEADER = "Interop-No-Retry"

/**
 * Returns true if the request does not contain the [NO_RETRY_HEADER] with a value of "true".
 */
fun HttpRequest.retry() = !headers.contains(NO_RETRY_HEADER, "true")

/**
 * Returns true if the request does not contain the [NO_RETRY_HEADER] with a value of "true".
 */
fun HttpRequestBuilder.retry() = !headers.contains(NO_RETRY_HEADER, "true")
