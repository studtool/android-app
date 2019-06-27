package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.document.DocumentInfo
import ru.mail.park.studtool.exception.InternalApiException
import ru.mail.park.studtool.exception.NotFoundApiException
import ru.mail.park.studtool.exception.UnauthorizedException

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

        try {
            mClient.newCall(request).execute().use { response ->
                when (response.code()) {
                    200 -> {
                        return parseJSONBody(response)
                    }

                    401 -> {
                        throw UnauthorizedException(null)
                    }

                    else -> {
                        throw InternalApiException(null)
                    }
                }
            }
        } catch (exc: Exception) {
            throw InternalApiException(exc.message)
        }
    }

    fun getDocumentsList(subject: String, authInfo: AuthInfo): Array<DocumentInfo> {
        val ownerId = authInfo.userId
        val authHeader = getAuthHeader(authInfo)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents?owner_id=$ownerId&subject=$subject&size=$PAGE_SIZE")
            .get()
            .header(authHeader.first, authHeader.second)
            .build()

        try {
            mClient.newCall(request).execute().use { response ->
                when (response.code()) {
                    200 -> {
                        return parseJSONBody(response)
                    }

                    401 -> {
                        throw UnauthorizedException(null)
                    }

                    404 -> {
                        throw NotFoundApiException(null)
                    }

                    else -> {
                        throw InternalApiException(null)
                    }
                }
            }
        } catch (exc: Exception) {
            throw InternalApiException(exc.message)
        }
    }

    fun setDocumentContent(documentId: String, content: ByteArray, authInfo: AuthInfo) {
        val authHeader = getAuthHeader(authInfo)

        val body = RequestBody
            .create(mTypeJSON, content)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents/$documentId/content")
            .patch(body)
            .header(authHeader.first, authHeader.second)
            .build()

        try {
            mClient.newCall(request).execute().use { response ->
                when (response.code()) {
                    200 -> {
                    }

                    401 -> {
                        throw UnauthorizedException(null)
                    }

                    404 -> {
                        throw NotFoundApiException(null)
                    }

                    else -> {
                        throw InternalApiException(null)
                    }
                }
            }
        } catch (exc: Exception) {
            throw InternalApiException(exc.message)
        }
    }

    fun getDocumentContent(documentId: String, authInfo: AuthInfo): ByteArray {
        val authHeader = getAuthHeader(authInfo)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents/$documentId/content")
            .get()
            .header(authHeader.first, authHeader.second)
            .build()

        try {
            mClient.newCall(request).execute().use { response ->
                when (response.code()) {
                    200 -> {
                        if (response.body() == null) {
                            throw InternalApiException(null)
                        }
                        return response.body()!!.bytes()
                    }

                    401 -> {
                        throw UnauthorizedException(null)
                    }

                    404 -> {
                        throw NotFoundApiException(null)
                    }

                    else -> {
                        throw InternalApiException(null)
                    }
                }
            }
        } catch (exc: Exception) {
            throw InternalApiException(exc.message)
        }
    }

    private companion object {
        private const val PAGE_SIZE = 100
    }
}
