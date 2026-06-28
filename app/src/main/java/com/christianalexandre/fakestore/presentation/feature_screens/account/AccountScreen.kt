package com.christianalexandre.fakestore.presentation.feature_screens.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AccountScreen(
    onLogout: () -> Unit,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val s = state) {
            is AccountViewModel.State.Loading -> CircularProgressIndicator()

            is AccountViewModel.State.Error -> {
                Text(
                    text = "Não foi possível carregar o perfil",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            is AccountViewModel.State.Loaded -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Minha Conta", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = s.name, style = MaterialTheme.typography.titleLarge)

                    if (!s.email.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = s.email, style = MaterialTheme.typography.bodyMedium)
                    }

                    if (s.bio.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = s.bio,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}
