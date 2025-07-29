package com.inghara.etacontroleapp

data class Ingrediente(
    val estoqueItemId: String = "",
    val nome: String = "",
    val quantidadeUsada: Double = 0.0
) {
    constructor() : this("", "", 0.0)
}