package com.inghara.etacontroleapp.model

import com.google.firebase.firestore.DocumentId
import com.inghara.etacontroleapp.Produto

data class Venda(
    @DocumentId
    var id: String? = null,
    val cliente: String = "",
    val data: String = "",
    val hora: String = "",
    val total: String = "",
    var status: String = "Pendente",
    val itens: List<Produto> = emptyList()
) {
    constructor() : this(null, "", "", "", "", "Pendente", emptyList())
}