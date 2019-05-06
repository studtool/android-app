package ru.mail.park.studtool.api

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.MediaType
import ru.mail.park.studtool.exception.InternalApiException

open class ApiManager {
    companion object {
        private const val HTTP_PROTO = "http"
        private const val SERVER_ADDRESS = "80.252.155.65:8000"

        const val REQUEST_PREFIX = "$HTTP_PROTO://$SERVER_ADDRESS/api"

        val JSON: MediaType = MediaType.get("application/json; charset=utf-8")

        fun toJSON(obj: Any): String {
            return Gson().toJson(obj)
        }

        fun fromJSON(str: String, cls: Class<*>): Any {
            try {
                return Gson().fromJson(str, cls)
            } catch (exception: JsonSyntaxException) {
                throw InternalApiException(exception.message!!)
            }
        }
    }
}
