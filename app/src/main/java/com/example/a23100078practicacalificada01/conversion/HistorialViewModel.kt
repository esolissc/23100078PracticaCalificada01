package com.example.a23100078practicacalificada01.conversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistorialViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _historial = MutableStateFlow<List<ConversionRecord>>(emptyList())
    val historial = _historial.asStateFlow()

    fun cargarHistorial() {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch

            firestore.collection("historial")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        error.printStackTrace()
                        return@addSnapshotListener
                    }

                    val lista = snapshot?.documents?.mapNotNull { doc ->
                        val ts = doc.get("timestamp")  // 👈 obtenemos genérico
                        val timeMillis = when (ts) {
                            is Timestamp -> ts.toDate().time
                            is Long -> ts
                            else -> System.currentTimeMillis()
                        }

                        ConversionRecord(
                            userId = doc.getString("userId") ?: "",
                            amount = doc.getDouble("amount") ?: 0.0,
                            from = doc.getString("from") ?: "",
                            to = doc.getString("to") ?: "",
                            result = doc.getDouble("result") ?: 0.0,
                            timestamp = timeMillis
                        )
                    } ?: emptyList()

                    _historial.value = lista
                }
        }
    }
}
