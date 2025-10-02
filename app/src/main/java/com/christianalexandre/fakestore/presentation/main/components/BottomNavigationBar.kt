package com.christianalexandre.fakestore.presentation.main.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.christianalexandre.fakestore.presentation.navigation.BottomBarRoute

@Composable
fun BottomNavigationBar(items: List<BottomBarRoute>, currentRoute: String?, onItemClick: (BottomBarRoute) -> Unit) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        item.icon, contentDescription = stringResource(id = item.labelResource)
                    )
                },
                label = { Text(stringResource(item.labelResource)) })
        }
    }
}