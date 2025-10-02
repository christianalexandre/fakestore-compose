package com.christianalexandre.fakestore.presentation.product_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun CarouselItem(imageUrls: List<String>) {
    if (imageUrls.isEmpty()) return

    if (imageUrls.count() == 1) {
        ImageWithLoading(
            imageUrl = imageUrls.first(),
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(imageUrls) { imageUrl ->
            ImageWithLoading(
                imageUrl = imageUrl,
                modifier = Modifier.fillParentMaxWidth(0.8f)
            )
        }
    }
}

@Composable
private fun ImageWithLoading(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = imageUrl,
        modifier = modifier,
        contentScale = ContentScale.Fit,
        loading = {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    )
}
