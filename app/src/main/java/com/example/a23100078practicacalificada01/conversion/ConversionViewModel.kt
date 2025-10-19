package com.example.a23100078practicacalificada01.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ConversionViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Tasas predefinidas (puedes modificarlas)
    private val rates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.92,
        "PEN" to 3.75,
        "GBP" to 0.79,
        "JPY" to 151.5
    )

    fun convertAndSave(amount: Double, from: String, to: String, onResult: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val fromRate = rates[from] ?: 1.0
                val toRate = rates[to] ?: 1.0
                val result = amount * (toRate / fromRate)

                val uid = auth.currentUser?.uid
                if (uid == null) {
                    onResult("❌ Usuario no autenticado")
                    return@launch
                }

                val record = hashMapOf(
                    "userId" to uid,
                    "timestamp" to Timestamp.now(), // ✅ Cumple con "Fecha/Hora"
                    "amount" to amount,
                    "from" to from,
                    "to" to to,
                    "result" to result
                )

                firestore.collection("historial")
                    .add(record)
                    .addOnSuccessListener {
                        onResult("✅ $amount $from = ${"%.2f".format(result)} $to (Guardado)")
                    }
                    .addOnFailureListener { e ->
                        onResult("❌ Error al guardar: ${e.message}")
                    }
            } catch (e: Exception) {
                onResult("⚠️ Error: ${e.message}")
            }
        }
    }
}
