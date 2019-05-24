package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.auth.Credentials
import ru.mail.park.studtool.exception.ConflictApiException
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.UnauthorizedException

class AuthApiManager : ApiManager() {
    private val mClient = OkHttpClient()

    fun performSignUp(credentials: Credentials) {
        val body = RequestBody
            .create(mTypeJSON, toJSON(credentials))
        val request = Request.Builder()
            .url("$PUBLIC_REQUEST_V0_PREFIX/auth/profiles")
            .post(body)
            .build()
        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                }

                409 -> {
                    throw ConflictApiException(null)
                }

                else -> {
                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }

    fun performSignIn(credentials: Credentials): AuthInfo {
        val body = RequestBody
            .create(mTypeJSON, toJSON(credentials))
        val request = Request.Builder()
            .url("$PUBLIC_REQUEST_V0_PREFIX/auth/sessions")
            .post(body)
            .build()

        mClient.newCall(request).execute().use {
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

    fun performSignOut(authInfo: AuthInfo) {
        val authHeader = getAuthHeader(authInfo)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/auth/session/${authInfo.sessionId}")
            .header(authHeader.first, authHeader.second)
            .get()
            .build()

        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    return
                }

                else -> {
                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }
}
