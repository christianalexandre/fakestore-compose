package com.christianalexandre.fakestore.presentation.feature_screens.products_list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.christianalexandre.fakestore.R
import com.christianalexandre.fakestore.domain.model.Product
import com.christianalexandre.fakestore.presentation.feature_screens.products_list.components.ProductListItem

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProductsListScreen(
    viewModel: ProductsListViewModel = hiltViewModel(),
    onProductClick: (Product) -> Unit
) {
    val lazyProducts = viewModel.productsFlow.collectAsLazyPagingItems()

    Scaffold { _ ->
        when (val refreshState = lazyProducts.loadState.refresh) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.error_loading_products,
                                refreshState.error.localizedMessage
                                    ?: stringResource(id = R.string.error_unknown)
                            ),
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(onClick = { lazyProducts.retry() }) {
                            Text(text = stringResource(id = R.string.retry))
                        }
                    }
                }
            }

            is LoadState.NotLoading -> {
                if (lazyProducts.itemCount == 0) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.products_list_empty))
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            count = lazyProducts.itemCount,
                            key = lazyProducts.itemKey { product -> product.id }
                        ) { index ->
                            val product = lazyProducts[index]
                            product?.let {
                                ProductListItem(
                                    product = it,
                                    onClick = { onProductClick(it) }
                                )
                            }
                        }

                        when (val appendState = lazyProducts.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }

                            is LoadState.Error -> {
                                item {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = stringResource(
                                                id = R.string.error_loading_more_products,
                                                appendState.error.localizedMessage
                                                    ?: stringResource(id = R.string.error_unknown)
                                            ),
                                            color = MaterialTheme.colorScheme.error
                                        )
                                        Button(onClick = { lazyProducts.retry() }) {
                                            Text(text = stringResource(id = R.string.retry))
                                        }
                                    }
                                }
                            }

                            is LoadState.NotLoading -> Unit
                        }
                    }
                }
            }
        }
    }
}
