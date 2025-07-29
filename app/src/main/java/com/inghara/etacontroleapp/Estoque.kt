package com.inghara.etacontroleapp

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class EstoqueItem(
        @DocumentId
        var id: String? = null,
        val nome: String = "",
        var estoque: Int = 0,
        var valor: Double = 0.0,
        var dataAtualizacao: String = ""
) : Parcelable {
        @get:Exclude
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