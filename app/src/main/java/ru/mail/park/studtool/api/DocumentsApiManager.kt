package ru.mail.park.studtool.api

import android.security.keystore.UserNotAuthenticatedException
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

        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    val info = fromJSON(it.body()!!.string(), DocumentInfo::class.java)
                    return info as DocumentInfo
                }

                401 -> {
                    throw UnauthorizedException("not auth")
                }

                else -> {
                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }

    fun getDocumentsList(subject: String, authInfo: AuthInfo): Array<DocumentInfo> {
        val authHeader = getAuthHeader(authInfo)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents?owner_id=${authInfo.userId}&subject=$subject&size=50")
            .header(authHeader.first, authHeader.second)
            .get()
            .build()

        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    val info = fromJSON(it.body()!!.string(), Array<DocumentInfo>::class.java)
                    return info as Array<DocumentInfo>
                }

                404 -> {
                    return emptyArray()
//                    throw NotFoundApiException(it.body()?.string())
                }

                else -> {
                    return emptyArray()
//                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }


    fun getDocumentsDetails(documentId: String, authToken: String): String {
        val authHeader = getAuthHeader(authToken)

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents/${documentId}/content")
            .header(authHeader.first, authHeader.second)
            .get()
            .build()

        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    val info = it.body()!!.string()
                    return info
                }

                404 -> {
                    return ""
//                    throw NotFoundApiException(it.body()?.string())
                }

                else -> {
                    return ""
//                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }

    fun patchDocumentsDetails(documentId: String, data: String, authToken: String): String {
        val authHeader = getAuthHeader(authToken)

        val body = RequestBody
            .create(mTypeByte, data.toByteArray())

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents/${documentId}/content")
            .header(authHeader.first, authHeader.second)
            .patch(body)
            .build()

        mClient.newCall(request).execute().use {
            when (it.code()) {
                200 -> {
                    val info = it.body()!!.string()
                    return info
                }

                404 -> {
                    return ""
//                    throw NotFoundApiException(it.body()?.string())
                }

                else -> {
                    return ""
//                    throw InternalApiException(it.body()?.string())
                }
            }
        }
    }


}
