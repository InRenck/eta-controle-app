package com.inghara.etacontroleapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inghara.etacontroleapp.EstoqueItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EstoqueViewModel : ViewModel() {

    private val _listaEstoque = MutableLiveData<MutableList<EstoqueItem>>()
    val listaEstoque: LiveData<MutableList<EstoqueItem>> = _listaEstoque

    init {
        _listaEstoque.value = mutableListOf(
            EstoqueItem(nome = "Hambúrguer de Picanha", estoque = 15, dataAtualizacao = "19/06/2025", valor = 25.0),
            EstoqueItem(nome = "Refrigerante", estoque = 8, dataAtualizacao = "18/06/2025", valor = 5.0),
            EstoqueItem(nome = "Batata Frita", estoque = 0, dataAtualizacao = "17/06/2025", valor = 12.0),
            EstoqueItem(nome = "Água Mineral", estoque = 50, dataAtualizacao = "20/06/2025", valor = 3.0)
        )
    }

    fun adicionarItem(nome: String, quantidade: Int, valor: Double) {
        val listaAtual = _listaEstoque.value ?: mutableListOf()
        val novoItem = EstoqueItem(
            nome = nome,
            estoque = quantidade,
            valor = valor,
            dataAtualizacao = getDataAtualFormatada()
        )
        listaAtual.add(0, novoItem)
        _listaEstoque.value = listaAtual
    }


    private fun getDataAtualFormatada(): String {
        val dataFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dataFormat.format(Date())
    }

    private fun notificarAtualizacao() {
        _listaEstoque.value = _listaEstoque.value
    }

    fun incrementarEstoque(item: EstoqueItem) {
        item.estoque++
        item.dataAtualizacao = getDataAtualFormatada()
        notificarAtualizacao()
    }

    fun decrementarEstoque(item: EstoqueItem) {
        if (item.estoque > 0) {
            item.estoque--
            item.dataAtualizacao = getDataAtualFormatada()
            notificarAtualizacao()
        }
    }
}