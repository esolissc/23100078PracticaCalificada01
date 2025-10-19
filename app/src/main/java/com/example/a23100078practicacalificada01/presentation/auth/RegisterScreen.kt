package com.example.a23100078practicacalificada01.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.a23100078practicacalificada01.data.remote.firebase.FirebaseAuthManger
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Registro de Usuario", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellido") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch {
                        loading = true
                        val result = FirebaseAuthManger.registerUser(email, name, lastName, password)
                        loading = false
                        result.onSuccess {
                            message = "Registro exitoso"
                            onNavigateToLogin()
                        }.onFailure {
                            message = it.message ?: "Error desconocido"
                        }
                    }
                },
                enabled = !loading
            ) {
                Text(if (loading) "Registrando..." else "Registrar")
            }

            Spacer(Modifier.height(8.dp))

            TextButton(onClick = { onNavigateToLogin() }) {
                Text("¿Ya tienes cuenta? Inicia sesión")
            }

            if (message.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text(message, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
