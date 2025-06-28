package com.inghara.etacontroleapp

import java.util.UUID

data class EstoqueItem(
        val id: String = UUID.randomUUID().toString(),
        val nome: String,
        var estoque: Int,
        var valor: Double,
        var dataAtualizacao: String,
) {
        val statusCalculado: StatusEstoque
                get() {
                        return when {
                                estoque <= 0 -> StatusEstoque.FALTANDO
                                estoque <= 10 -> StatusEstoque.PERTO_DE_ACABAR
                                else -> StatusEstoque.EM_ESTOQUE
                        }
                }
}

enum class StatusEstoque {
        EM_ESTOQUE,
        PERTO_DE_ACABAR,
        FALTANDO
}