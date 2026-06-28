package com.christianalexandre.fakestore.presentation.feature_screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christianalexandre.fakestore.domain.model.CartItem
import com.christianalexandre.fakestore.domain.use_case.add_items_to_cart.AddItemToCartUseCase
import com.christianalexandre.fakestore.domain.use_case.checkout.CheckoutUseCase
import com.christianalexandre.fakestore.domain.use_case.get_cart_items.GetCartItemsUseCase
import com.christianalexandre.fakestore.domain.use_case.remove_item_from_cart.RemoveItemFromCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    getCartItems: GetCartItemsUseCase,
    private val addItemToCart: AddItemToCartUseCase,
    private val removeItemFromCart: RemoveItemFromCartUseCase,
    private val checkoutUseCase: CheckoutUseCase
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>?> =
        getCartItems()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = null,
            )

    val cartTotal: StateFlow<Double> =
        cartItems
            .mapNotNull { list ->
                list?.sumOf { it.price * it.quantity }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = 0.0,
            )

    private val _checkoutEvent = MutableSharedFlow<String>()
    val checkoutEvent = _checkoutEvent.asSharedFlow()

    fun onIncreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            addItemToCart(item.copy(quantity = 1))
        }
    }

    fun onDecreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            removeItemFromCart(item)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            try {
                checkoutUseCase()
                _checkoutEvent.emit("Pedido realizado! Aguardando confirmação de pagamento.")
            } catch (e: Exception) {
                _checkoutEvent.emit(e.message ?: "Erro ao finalizar pedido")
            }
        }
    }
}
