package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import ru.mail.park.studtool.document.DocumentInfo

class DocumentsApiManager : ApiManager() {

    val mClient = OkHttpClient()

    fun addDocument(documentInfo: DocumentInfo): DocumentInfo {
        val body = RequestBody
            .create(mTypeJSON, toJSON(documentInfo))

        val request = Request.Builder()
            .url("$PROTECTED_REQUEST_V0_PREFIX/documents")
            .post(body)
            .build()

        TODO()
    }
}
