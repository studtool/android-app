package ru.mail.park.studtool.api

import okhttp3.MediaType

open class ApiManager {
    companion object {
        private const val HTTP_PROTO = "http"
        private const val SERVER_ADDRESS = "80.252.155.65:8000"

        const val REQUEST_PREFIX = "$HTTP_PROTO://$SERVER_ADDRESS/api"

        val JSON: MediaType = MediaType.get("application/json; charset=utf-8")
    }
}
