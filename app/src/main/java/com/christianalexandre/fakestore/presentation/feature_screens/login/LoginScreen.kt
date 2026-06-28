package com.christianalexandre.fakestore.presentation.feature_screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var phone by rememberSaveable { mutableStateOf("") }
    var code by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(state) {
        if (state is LoginState.LoggedIn) onLoginSuccess()
        if (state is LoginState.Error) {
            snackbarHostState.showSnackbar((state as LoginState.Error).message)
            viewModel.resetError()
        }
    }

    val isOtpStep = state is LoginState.OtpSent || (state is LoginState.Loading && code.isNotEmpty())
    val isLoading = state is LoginState.Loading

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isOtpStep) "Confirmar código" else "Entrar")
                },
                navigationIcon = {
                    if (isOtpStep) {
                        IconButton(onClick = {
                            code = ""
                            viewModel.resetError()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (!isOtpStep) {
                Text("Seu número de telefone", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("+55 11 99999-0000") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.login(phone) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = phone.isNotBlank() && !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.height(20.dp))
                    else Text("Enviar código SMS")
                }
            } else {
                Text("Digite o código recebido por SMS", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = code,
                    onValueChange = { if (it.length <= 6) code = it },
                    label = { Text("Código de 6 dígitos") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.NumberPassword,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    enabled = !isLoading
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { viewModel.confirm(code) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = code.length == 6 && !isLoading
                ) {
                    if (isLoading) CircularProgressIndicator(modifier = Modifier.height(20.dp))
                    else Text("Confirmar")
                }
            }
        }
    }
}
