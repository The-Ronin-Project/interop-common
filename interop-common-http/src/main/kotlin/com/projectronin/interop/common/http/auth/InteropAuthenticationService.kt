package com.projectronin.interop.common.http.auth

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.projectronin.interop.common.auth.Authentication
import com.projectronin.interop.common.auth.BrokeredAuthenticator
import com.projectronin.interop.common.http.throwExceptionFromHttpStatus
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import java.time.Instant

/**
 * This is a class we use repeatedly for authentication in our clients
 */
class InteropAuthenticationService(
    private val client: HttpClient,
    private val authConfig: AuthenticationConfig
) : BrokeredAuthenticator() {

    override fun reloadAuthentication(): Authentication = if (authConfig.method == AuthMethod.AUTH0) {
        retrieveAuth0Authentication()
    } else {
        retrieveFormBasedAuthentication()
    }

    /**
     * Uses Auth0 to retrieve authentication.
     */
    private fun retrieveAuth0Authentication(): Authentication = runBlocking {
        val payload = Auth0Payload(authConfig.client.id, authConfig.client.secret, authConfig.audience)
        val httpResponse: HttpResponse = client.post(authConfig.token.url) {
            contentType(ContentType.Application.Json)
            setBody(payload)
        }
        httpResponse.throwExceptionFromHttpStatus("Auth0", "POST ${authConfig.token.url}")
        httpResponse.body<Auth0Authentication>()
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    private data class Auth0Payload(
        val clientId: String,
        val clientSecret: String,
        val audience: String,
        val grantType: String = "client_credentials"
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class Auth0Authentication(
        override val accessToken: String,
        override val tokenType: String,
        override val scope: String? = null,
        private val expiresIn: Long? = null,
        override val refreshToken: String? = null
    ) : Authentication {
        override val expiresAt: Instant? = expiresIn?.let { Instant.now().plusSeconds(expiresIn) }

        // Override toString() to prevent accidentally leaking the accessToken
        override fun toString(): String = this::class.simpleName!!
    }

    /**
     * Uses a more traditional form-based authentication.
     */
    private fun retrieveFormBasedAuthentication(): Authentication = runBlocking {
        val json: JsonNode = client.submitForm(
            url = authConfig.token.url,
            formParameters = Parameters.build {
                append("grant_type", "client_credentials")
                append("client_id", authConfig.client.id)
                append("client_secret", authConfig.client.secret)
            }
        ).body()
        val accessToken = json.get("access_token").asText()
        FormBasedAuthentication(accessToken)
    }

    data class FormBasedAuthentication(override val accessToken: String) : Authentication {
        override val tokenType: String = "Bearer"
        override val expiresAt: Instant? = null
        override val refreshToken: String? = null
        override val scope: String? = null
    }
}
