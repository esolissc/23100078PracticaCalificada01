package com.example.a23100078practicacalificada01.conversion

data class ConversionRecord(
    val userId: String = "",
    val amount: Double = 0.0,
    val from: String = "",
    val to: String = "",
    val result: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
