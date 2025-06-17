package com.inghara.etacontroleapp.model

data class Venda(
    val id: String,
    val cliente: String,
    val data: String,
    val hora: String,
    val total: String,
    val status: String
)
