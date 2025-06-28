package com.inghara.etacontroleapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inghara.etacontroleapp.Produto

class CardapioViewModel : ViewModel() {

    private val _listaDeProdutos = MutableLiveData<List<Produto>>()
    val listaDeProdutos: LiveData<List<Produto>> = _listaDeProdutos

    init {
        val produtos = listOf(
            Produto(nome = "Combo Hamburguer de Costela", preco = 57.0, status = "Ativo"),
            Produto(nome = "Pizza Calabresa", preco = 42.0, status = "Inativo"),
            Produto(nome = "Suco Natural", preco = 10.0, status = "Ativo"),
            Produto(nome = "Refrigerante", preco = 8.0, status = "Ativo")
        )
        _listaDeProdutos.value = produtos
    }

    fun adicionarProduto(nome: String, preco: Double, isAtivo: Boolean) {
        val listaAtual = _listaDeProdutos.value?.toMutableList() ?: mutableListOf()
        val status = if (isAtivo) "Ativo" else "Inativo"
        val novoProduto = Produto(nome = nome, preco = preco, status = status)

        listaAtual.add(novoProduto)
        _listaDeProdutos.value = listaAtual
    }

    fun getProdutoPorId(id: String): Produto? {
        return _listaDeProdutos.value?.find { it.id == id }
    }


    fun atualizarProduto(id: String, nome: String, preco: Double, isAtivo: Boolean) {
        val listaAtual = _listaDeProdutos.value?.toMutableList() ?: return
        val produtoParaAtualizar = listaAtual.find { it.id == id }

        produtoParaAtualizar?.let {
            // Encontra o índice do produto e o substitui pela nova versão
            val index = listaAtual.indexOf(it)
            val status = if (isAtivo) "Ativo" else "Inativo"
            listaAtual[index] = Produto(id, nome, preco, status)
            _listaDeProdutos.value = listaAtual
        }
    }

}