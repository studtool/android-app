package ru.mail.park.studtool.exception

open class ApiException(message: String?) : AppException(message)

class InternalApiException(message: String?) : ApiException(message)

class NotFoundApiException(message: String?) : ApiException(message)

class ConflictApiException(message: String?) : ApiException(message)

class UnauthorizedException(message: String?) : ApiException(message)
