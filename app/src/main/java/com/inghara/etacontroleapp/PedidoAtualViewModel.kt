package com.inghara.etacontroleapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inghara.etacontroleapp.Produto
import com.inghara.etacontroleapp.model.Venda

class PedidoAtualViewModel : ViewModel() {

    private val _produtosNoPedido = MutableLiveData<MutableList<Produto>>(mutableListOf())
    val produtosNoPedido: LiveData<MutableList<Produto>> = _produtosNoPedido

    private val _totalDoPedido = MutableLiveData<Double>(0.0)
    val totalDoPedido: LiveData<Double> = _totalDoPedido

    fun carregarVendaParaEdicao(venda: Venda) {
        _produtosNoPedido.value = venda.itens.toMutableList()
        recalcularTotal()
    }

    fun addProdutoAoPedido(produto: Produto) {
        val lista = _produtosNoPedido.value ?: mutableListOf()
        lista.add(produto)
        _produtosNoPedido.value = lista
        recalcularTotal()
    }

    fun removeProdutoDoPedido(produto: Produto) {
        val lista = _produtosNoPedido.value ?: return
        lista.remove(produto)
        _produtosNoPedido.value = lista
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