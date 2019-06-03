package ru.mail.park.studtool.auth

data class Credentials(
    val email: String,
    val password: String
)

data class AuthInfo(
    var userId: String = "",
    var authToken: String = "",
    var refreshToken: String = "",
    var sessionId: String = "",
    var expireTime: String = ""
)
