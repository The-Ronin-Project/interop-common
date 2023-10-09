package com.projectronin.interop.common.http.auth

import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@Validated
@Configuration
data class AuthenticationSpringConfig(
    @Valid
    val token: Token = Token(),
    @NotEmpty
    val audience: String = "",
    @Valid
    val client: Client = Client(),

    @Valid
    val method: AuthMethod = AuthMethod.AUTH0
)

data class Token(@NotEmpty val url: String = "")
data class Client(
    @NotEmpty
    val id: String = "",
    @NotEmpty
    val secret: String = ""
)

enum class AuthMethod {
    STANDARD,
    AUTH0
}
