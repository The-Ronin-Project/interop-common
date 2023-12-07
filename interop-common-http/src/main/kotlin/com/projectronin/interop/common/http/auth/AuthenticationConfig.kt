package com.projectronin.interop.common.http.auth

import javax.validation.Valid
import javax.validation.constraints.NotEmpty

// These need to be vars because we want projects to be able to define the prefix, but we can't
data class AuthenticationConfig(
    @Valid
    var token: Token = Token(),
    @NotEmpty
    var audience: String = "",
    @Valid
    var client: Client = Client(),
    @Valid
    var method: AuthMethod = AuthMethod.AUTH0,
)

data class Token(
    @NotEmpty
    var url: String = "",
)

data class Client(
    @NotEmpty
    var id: String = "",
    @NotEmpty
    var secret: String = "",
)

enum class AuthMethod {
    STANDARD,
    AUTH0,
}
