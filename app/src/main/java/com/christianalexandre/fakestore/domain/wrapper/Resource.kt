package com.christianalexandre.fakestore.domain.wrapper

import com.christianalexandre.fakestore.domain.exception.DomainException

/**
 * Wrapper class for asynchronous operations.
 * Used to communicate the state between the Domain layer (UseCases) and the Presentation layer (ViewModels).
 * @param T the type of the result of the operation
 */
sealed class Resource<out T> {
    /**
     * Loading state
     */
    data object Loading : Resource<Nothing>()

    /**
     * Success state
     * @param data the result from the operation
     */
    data class Success<T>(
        val data: T,
    ) : Resource<T>()

    /**
     * Failure state
     * @param exception the exception that caused the failure
     */
    data class Error(
        val exception: DomainException,
    ) : Resource<Nothing>()
}
