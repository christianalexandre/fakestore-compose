package com.christianalexandre.fakestore.domain.exception

/**
 * Base exception for domain layer errors
 */
sealed class DomainException(
    message: String,
) : Exception(message)

/**
 * Errors that extends base domain exception
 */
class NetworkException : DomainException("Network exception")

class UnknownException : DomainException("Unknown exception")
