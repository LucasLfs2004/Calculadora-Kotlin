package com.example.calculadora.model

data class Operacao(
    val id: Long,
    val operation: String,
    val value1: Double,
    val value2: Double,
    val result: Double,
    val date: String
)