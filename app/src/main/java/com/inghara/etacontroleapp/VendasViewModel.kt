package com.inghara.etacontroleapp.viewmodel // Ou o pacote que você escolheu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inghara.etacontroleapp.model.Venda
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VendasViewModel : ViewModel() {

    private val _listaVendas = MutableLiveData<MutableList<Venda>>()

    val listaVendas: LiveData<MutableList<Venda>> = _listaVendas

    init {
        _listaVendas.value = mutableListOf(
            Venda("1", "Renan", "22/06/2025", "10:30", "R$ 55,00", "Concluída"),
            Venda("2", "Maria", "22/06/2025", "11:15", "R$ 32,50", "Pendente"),
            Venda("3", "José", "21/06/2025", "19:45", "R$ 88,00", "Cancelada")
        )
    }

    fun adicionarVenda(cliente: String, total: String) {
        val listaAtual = _listaVendas.value ?: mutableListOf()

        val novoId = (listaAtual.size + 1).toString()
        val dataFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val horaFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dataAtual = Date()

        val novaVenda = Venda(
            id = novoId,
            cliente = cliente,
            data = dataFormat.format(dataAtual),
            hora = horaFormat.format(dataAtual),
            total = total,
            status = "Pendente"
        )

        listaAtual.add(0, novaVenda)
        _listaVendas.value = listaAtual
    }

    fun atualizarStatusVenda(vendaId: String, novoStatus: String) {
        val listaAtual = _listaVendas.value ?: return

        val itemParaAtualizar = listaAtual.find { it.id == vendaId }

        itemParaAtualizar?.let {
            it.status = novoStatus
        }

        _listaVendas.value = listaAtual
    }
}