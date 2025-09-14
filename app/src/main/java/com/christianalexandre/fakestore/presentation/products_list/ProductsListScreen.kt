package com.christianalexandre.fakestore.presentation.products_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.christianalexandre.fakestore.R
import com.christianalexandre.fakestore.presentation.products_list.components.ProductListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsListScreen(
    viewModel: ProductsListViewModel = hiltViewModel(),
) {
    val lazyProducts = viewModel.productsFlow.collectAsLazyPagingItems()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
        ) {
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
                        ProductListItem(product = it)
                    }
                }

                if (lazyProducts.loadState.refresh is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (lazyProducts.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (lazyProducts.loadState.refresh is LoadState.Error) {
                    val error = (lazyProducts.loadState.refresh as LoadState.Error).error
                    item {
                        Text(
                            text = stringResource(
                                id = R.string.error_loading_products,
                                error.localizedMessage ?: ""
                            ),
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                if (lazyProducts.loadState.append is LoadState.Error) {
                    val error = (lazyProducts.loadState.append as LoadState.Error).error
                    item {
                        Text(
                            text = stringResource(
                                id = R.string.error_loading_more_products,
                                error.localizedMessage ?: ""
                            ),
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

