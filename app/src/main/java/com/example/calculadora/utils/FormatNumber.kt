package com.example.calculadora.utils

import com.example.calculadora.OperationAdapter

fun formatNumber( value: Double): String {
    if (value % 1 == 0.0) {
       return value.toInt().toString()
    } else {
       return  String.format("%.2f", value)
    }
}
