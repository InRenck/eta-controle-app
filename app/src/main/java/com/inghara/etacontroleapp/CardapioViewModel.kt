package com.inghara.etacontroleapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.inghara.etacontroleapp.Ingrediente
import com.inghara.etacontroleapp.Produto

class CardapioViewModel : ViewModel() {

    private val _listaDeProdutos = MutableLiveData<List<Produto>>()
    val listaDeProdutos: LiveData<List<Produto>> = _listaDeProdutos

    private val db = Firebase.firestore
    private val produtosCollection = db.collection("produtos")

    init {
        produtosCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("Firestore", "Error listening for products", error)
                return@addSnapshotListener
            }
            snapshot?.let {
                val produtos = it.toObjects(Produto::class.java)
                _listaDeProdutos.value = produtos
            }
        }
    }

    fun adicionarProduto(nome: String, preco: Double, isAtivo: Boolean, ingredientes: List<Ingrediente>) {
        val status = if (isAtivo) "Ativo" else "Inativo"
        val novoProduto = Produto(
            nome = nome,
            preco = preco,
            status = status,
            ingredientes = ingredientes
        )
        produtosCollection.add(novoProduto)
    }

    fun deletarProduto(produto: Produto) {
        produto.id?.let { produtosCollection.document(it).delete() }
    }


    fun atualizarProduto(id: String, nome: String, preco: Double, isAtivo: Boolean, ingredientes: List<Ingrediente>) {
        val status = if (isAtivo) "Ativo" else "Inativo"
        val produtoAtualizado = mapOf(
            "nome" to nome,
            "preco" to preco,
            "status" to status,
            "ingredientes" to ingredientes
        )
        produtosCollection.document(id).update(produtoAtualizado)
    }
}