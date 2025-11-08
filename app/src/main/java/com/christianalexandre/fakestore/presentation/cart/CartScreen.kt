package com.christianalexandre.fakestore.presentation.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.christianalexandre.fakestore.R
import com.christianalexandre.fakestore.domain.model.CartItem
import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double): String = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(viewModel: CartViewModel = hiltViewModel()) {
    val cartItems by viewModel.cartItems.collectAsState(initial = null)
    val cartTotal by viewModel.cartTotal.collectAsState()

    Scaffold(
        bottomBar = {
            if (!cartItems.isNullOrEmpty()) {
                CheckoutFooter(
                    total = cartTotal,
                    onCheckoutClick = {
                        // TODO: Implementar navegação para checkout
                    },
                )
            }
        },
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when {
                cartItems == null -> {
                    CircularProgressIndicator()
                }

                cartItems!!.isEmpty() -> {
                    Text(
                        text = stringResource(R.string.cart_empty),
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(cartItems!!, key = { it.productId }) { item ->
                            CartListItem(
                                item = item,
                                onIncrease = { viewModel.onIncreaseQuantity(item) },
                                onDecrease = { viewModel.onDecreaseQuantity(item) },
                            )
                            Divider(thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartListItem(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = item.thumbnail,
            contentDescription = item.title,
            modifier =
                Modifier
                    .size(88.dp)
                    .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = formatCurrency(item.price * item.quantity),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(Modifier.width(16.dp))

        QuantitySelector(
            quantity = item.quantity,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
        )
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        IconButton(
            onClick = onDecrease,
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Diminuir quantidade",
                tint = MaterialTheme.colorScheme.error,
            )
        }

        Text(
            text = "$quantity",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.widthIn(min = 24.dp),
            textAlign = TextAlign.Center,
        )

        IconButton(
            onClick = onIncrease,
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Aumentar quantidade",
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun CheckoutFooter(
    total: Double,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = formatCurrency(total),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Button(
                onClick = onCheckoutClick,
                modifier = Modifier.height(48.dp),
            ) {
                Text(
                    text = "Checkout",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
