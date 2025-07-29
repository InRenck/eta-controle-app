package com.inghara.etacontroleapp

import com.google.firebase.firestore.DocumentId

data class Produto(
    @DocumentId
    var id: String? = null,
    val nome: String = "",
    val preco: Double = 0.0,
    var status: String? = "Ativo",
    val ingredientes: List<Ingrediente> = emptyList()
) {
    constructor() : this(null, "", 0.0, "Ativo", emptyList())
}