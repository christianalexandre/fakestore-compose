package com.christianalexandre.fakestore.presentation.feature_screens.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.christianalexandre.fakestore.domain.mappers.toCartItem
import com.christianalexandre.fakestore.domain.model.Product
import com.christianalexandre.fakestore.domain.use_case.add_items_to_cart.AddItemToCartUseCase
import com.christianalexandre.fakestore.domain.use_case.get_product.GetProduct
import com.christianalexandre.fakestore.domain.wrapper.Resource
import com.christianalexandre.fakestore.presentation.navigation.routes.ProductDetailScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductDetailState {
    data object Loading : ProductDetailState
    data class Success(val product: Product) : ProductDetailState
    data class Error(val message: String) : ProductDetailState
}

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProduct: GetProduct,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val state: StateFlow<ProductDetailState> = _state.asStateFlow()

    private val productId: Int = checkNotNull(savedStateHandle[ProductDetailScreen.ARG_PRODUCT_ID])

    init {
        getProduct()
    }

    fun onRetry() {
        getProduct()
    }

    private fun getProduct() {
        viewModelScope.launch {
            getProduct(productId).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { ProductDetailState.Loading }
                    }

                    is Resource.Success -> {
                        result.data.let { product ->
                            _state.update { ProductDetailState.Success(product) }
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            ProductDetailState.Error(
                                result.exception.message ?: "An unexpected error occurred"
                            )
                        }
                    }
                }
            }
        }
    }

    fun addToCart(product: Product) {
        viewModelScope.launch {
            addItemToCartUseCase(product.toCartItem())
        }
    }
}
