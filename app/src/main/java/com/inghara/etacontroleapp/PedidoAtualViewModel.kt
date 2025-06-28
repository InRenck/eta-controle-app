package com.inghara.etacontroleapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inghara.etacontroleapp.Produto
import java.util.Locale

class PedidoAtualViewModel : ViewModel() {

    // LiveData para a lista de produtos no pedido atual
    private val _produtosNoPedido = MutableLiveData<MutableList<Produto>>(mutableListOf())
    val produtosNoPedido: LiveData<MutableList<Produto>> = _produtosNoPedido

    // LiveData para o valor total do pedido
    private val _totalDoPedido = MutableLiveData<Double>(0.0)
    val totalDoPedido: LiveData<Double> = _totalDoPedido

    fun addProdutoAoPedido(produto: Produto) {
        val lista = _produtosNoPedido.value ?: mutableListOf()
        lista.add(produto)
        _produtosNoPedido.value = lista // Notifica os observadores da lista
        recalcularTotal()
    }

    fun removeProdutoDoPedido(produto: Produto) {
        val lista = _produtosNoPedido.value ?: return
        lista.remove(produto)
        _produtosNoPedido.value = lista // Notifica os observadores da lista
        recalcularTotal()
    }

    fun limparPedido() {
        _produtosNoPedido.value = mutableListOf()
        recalcularTotal()
    }

    private fun recalcularTotal() {
        val total = _produtosNoPedido.value?.sumOf { it.preco } ?: 0.0
        _totalDoPedido.value = total
    }
}