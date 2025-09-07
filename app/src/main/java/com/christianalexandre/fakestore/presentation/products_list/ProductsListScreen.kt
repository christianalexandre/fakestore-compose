package com.christianalexandre.fakestore.presentation.products_list

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.christianalexandre.fakestore.domain.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleProductsListScreen(
    viewModel: ProductsListViewModel = hiltViewModel(),
) {
    val lazyProducts = viewModel.productsFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Products (Simple Paginated)") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    count = lazyProducts.itemCount,
                    key = lazyProducts.itemKey { product -> product.id }
                ) { index ->
                    val product = lazyProducts[index]
                    product?.let {
                        SimpleProductItem(product = it)
                    }
                }

                if (lazyProducts.loadState.refresh is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            Log.d("ProductsListScreen", "Refreshing list...")
                        }
                    }
                }

                if (lazyProducts.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                            Log.d("ProductsListScreen", "Loading next page...")
                        }
                    }
                }

                if (lazyProducts.loadState.refresh is LoadState.Error) {
                    val error = (lazyProducts.loadState.refresh as LoadState.Error).error
                    item {
                        Text(
                            text = "Error loading initial items: ${error.localizedMessage}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Log.e(
                            "ProductsListScreen",
                            "Error refreshing: ${error.localizedMessage}",
                            error
                        )
                    }
                }

                if (lazyProducts.loadState.append is LoadState.Error) {
                    val error = (lazyProducts.loadState.append as LoadState.Error).error
                    item {
                        Text(
                            text = "Error loading more items: ${error.localizedMessage}",
                            modifier = Modifier.padding(16.dp)
                        )
                        Log.e(
                            "ProductsListScreen",
                            "Error appending: ${error.localizedMessage}",
                            error
                        )
                    }
                }
            }

            Log.d("ProductsListScreen", "ItemCount: ${lazyProducts.itemCount}")
            Log.d("ProductsListScreen", "LoadState Refresh: ${lazyProducts.loadState.refresh}")
            Log.d("ProductsListScreen", "LoadState Append: ${lazyProducts.loadState.append}")
        }
    }
}

@Composable
fun SimpleProductItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = product.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}
