package ru.mail.park.studtool.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import okhttp3.MediaType
import ru.mail.park.studtool.auth.AuthInfo
import ru.mail.park.studtool.exception.InternalApiException

open class ApiManager {
    companion object {
        private const val HTTP_PROTO = "http"
        private const val SERVER_ADDRESS = "80.252.155.65:8000"

        private const val REQUEST_PREFIX = "$HTTP_PROTO://$SERVER_ADDRESS/api"

        private const val REQUEST_V0_PREFIX = "$REQUEST_PREFIX/v0"

        const val PUBLIC_REQUEST_V0_PREFIX = "$REQUEST_V0_PREFIX/public"
        const val PROTECTED_REQUEST_V0_PREFIX = "$REQUEST_V0_PREFIX/protected"

        val mTypeJSON: MediaType = MediaType.get("application/json; charset=utf-8")
        private val mSerializer: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

        fun toJSON(obj: Any): String {
            return mSerializer.toJson(obj)
        }

        fun fromJSON(str: String, cls: Class<*>): Any {
            try {
                return mSerializer.fromJson(str, cls)
            } catch (exception: JsonSyntaxException) {
                throw InternalApiException(exception.message!!)
            }
        }

        fun getAuthHeader(authInfo: AuthInfo): Pair<String, String> {
            return Pair("Authorization", "Bearer: ${authInfo.authToken}")
        }
    }
}
