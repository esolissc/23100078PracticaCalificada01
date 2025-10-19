package com.example.a23100078practicacalificada01.conversion

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionScreen(
    viewModel: ConversionViewModel = viewModel(),
    onNavigateToHistorial: () -> Unit,
    onLogout: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var resultText by remember { mutableStateOf("") }

    val currencies = listOf("USD", "EUR", "PEN", "GBP", "JPY")

    Scaffold(
        topBar = {
            // 🔹 Solo botón de cerrar sesión
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 🔹 Contenido centrado vertical y horizontal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Título dentro del cuerpo
                Text(
                    text = "Conversor de Monedas",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Monto") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CurrencyDropdown("De", currencies, fromCurrency) { fromCurrency = it }
                    CurrencyDropdown("A", currencies, toCurrency) { toCurrency = it }
                }

                Spacer(Modifier.height(32.dp))

                Button(onClick = {
                    val amt = amount.toDoubleOrNull()
                    if (amt != null) {
                        viewModel.convertAndSave(amt, fromCurrency, toCurrency) {
                            resultText = it
                        }
                    } else {
                        resultText = "❗ Ingresa un monto válido"
                    }
                }) {
                    Text("Convertir")
                }

                Spacer(Modifier.height(16.dp))

                if (resultText.isNotEmpty()) {
                    Text(resultText)
                }

                Spacer(Modifier.height(24.dp))

                Button(onClick = { onNavigateToHistorial() }) {
                    Text("Ver Historial de Conversiones")
                }
            }
        }
    }
}

// 🔹 Dropdown reutilizable
@Composable
fun CurrencyDropdown(
    label: String,
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label)
        Box {
            Button(onClick = { expanded = true }) {
                Text(selected)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
