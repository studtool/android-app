package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ru.mail.park.studtool.exception.ConflictApiException
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException
import java.util.*

data class Credentials(
    val email: String,
    val password: String
)

data class AuthInfo(
    var userId: String = "",
    var authToken: String = "",
    var refreshToken: String = "",
    var expireTime: Date = Date()
)

class AuthApiManager : ApiManager() {
    fun performSignUp(credentials: Credentials) {
        val client = OkHttpClient()

        val body = RequestBody
            .create(mType, toJSON(credentials))
        val request = Request.Builder()
            .url("$REQUEST_PREFIX/v0/auth/profiles")
            .post(body)
            .build()
        client.newCall(request).execute().use {
            if (!it.isSuccessful && it.code() == 409) {
                if (it.code() == 409) {
                    throw ConflictApiException(it.message())
                } else {
                    throw InternalApiException(it.toString())
                }
            }
        }
    }

    fun performSignIn(credentials: Credentials): AuthInfo {
        val client = OkHttpClient()

        val body = RequestBody
            .create(mType, toJSON(credentials))
        val request = Request.Builder()
            .url("$REQUEST_PREFIX/v0/auth/sessions")
            .post(body)
            .build()

        client.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    val info = fromJSON(it.body()!!.string(), AuthInfo::class.java)
                    return info as AuthInfo
                }

                404 -> {
                    throw UnauthorizedException(null)
                }

                else -> {
                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }
}
