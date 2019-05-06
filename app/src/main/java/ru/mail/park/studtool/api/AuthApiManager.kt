package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

data class Credentials(
    val email: String, val password: String
)

class AuthApiManager : ApiManager() {
    fun performSignUp(credentials: Credentials) {
        val client = OkHttpClient()

        val body = RequestBody
            .create(JSON, "")
        val request = Request.Builder()
            .url("$REQUEST_PREFIX/v0/auth/profiles")
            .post(body)
            .build()
        client.newCall(request).execute().use {
            println(it)
        }
    }
}
