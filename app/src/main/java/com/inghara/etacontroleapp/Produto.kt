package com.inghara.etacontroleapp

import java.util.UUID

data class Produto(
    val id: String = UUID.randomUUID().toString(),
    val nome: String,
    val preco: Double,
    var status: String? = null
)