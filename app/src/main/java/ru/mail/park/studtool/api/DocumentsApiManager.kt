package ru.mail.park.studtool.api

import okhttp3.OkHttpClient
import okhttp3.Request
import ru.mail.park.studtool.exception.ApiException

data class DocumentInfo(
    val id: String = "",
    val title: String = "",
    val ownerId: String = "",
    val subject: String = "",
    val documentMeta: DocumentMeta = DocumentMeta()
)

data class DocumentMeta(
    val size: Long = 0
)

class DocumentsApiManager : ApiManager() {

    fun getDocumentsList(): Array<DocumentInfo> {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("$REQUEST_PREFIX/v0/documents")
            .get()
            .build()
        client.newCall(request).execute().use {
            if (it.code() == 200) {
                val info = fromJSON(it.body()!!.string(), Array<DocumentInfo>::class.java)
                return info as Array<DocumentInfo>
            } else {
                throw ApiException(null)
            }
        }
    }
}
