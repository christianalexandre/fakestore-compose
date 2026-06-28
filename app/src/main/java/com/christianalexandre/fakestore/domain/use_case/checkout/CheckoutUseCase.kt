package com.christianalexandre.fakestore.domain.use_case.checkout

import com.christianalexandre.fakestore.domain.repository.CartRepository
import javax.inject.Inject

class CheckoutUseCase @Inject constructor(private val cartRepository: CartRepository) {
    suspend operator fun invoke() = cartRepository.checkout()
}
