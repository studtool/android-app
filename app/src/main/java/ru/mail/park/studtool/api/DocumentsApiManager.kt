package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException

class DocumentsApiManager : ApiManager() {

    private val mClient = OkHttpClient()

    fun addDocument(documentInfo: DocumentInfo, authInfo: AuthInfo): DocumentInfo {
        val body = RequestBody
            .create(mTypeJSON, toJSON(documentInfo))

        val authHeader = getAuthHeader(authInfo)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents")
            .post(body)
            .header(authHeader.first, authHeader.second)
            .build()

        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    val info = fromJSON(it.body()!!.string(), DocumentInfo::class.java)
                    return info as DocumentInfo
                }

                else -> {
                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }
}
