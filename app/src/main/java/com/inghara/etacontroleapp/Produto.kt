package com.inghara.etacontroleapp

data class Produto(
    val nome: String,
    val preco: Double,
    val status: String? = null
)