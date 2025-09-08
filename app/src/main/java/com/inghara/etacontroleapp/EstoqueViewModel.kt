package com.inghara.etacontroleapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.inghara.etacontroleapp.EstoqueItem
import com.inghara.etacontroleapp.PedidosFragment
import com.inghara.etacontroleapp.Produto
import com.inghara.etacontroleapp.StatusEstoque
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EstoqueViewModel : ViewModel() {

    private val _listaEstoque = MutableLiveData<List<EstoqueItem>>()
    val listaEstoque: LiveData<List<EstoqueItem>> = _listaEstoque

    private val _itensEstoqueBaixo = MutableLiveData<Int>()
    val itensEstoqueBaixo: LiveData<Int> = _itensEstoqueBaixo

    private val db = Firebase.firestore
    private val estoqueCollection = db.collection("estoque")

    init {
        estoqueCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("Firestore", "Error listening for stock items", error)
                return@addSnapshotListener
            }
            snapshot?.let {
                _listaEstoque.value = it.toObjects(EstoqueItem::class.java)
                contarItensEmEstoqueBaixo(_listaEstoque.value ?: emptyList())
            }
        }
    }

    private fun contarItensEmEstoqueBaixo(lista: List<EstoqueItem>) {
        val contagem = lista.count { it.statusCalculado == StatusEstoque.PERTO_DE_ACABAR || it.statusCalculado == StatusEstoque.FALTANDO }
        _itensEstoqueBaixo.value = contagem
    }

    private fun getDataAtualFormatada(): String {
        val dataFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dataFormat.format(Date())
    }

    fun adicionarItem(nome: String, quantidade: Int, valor: Double) {
        val novoItem = EstoqueItem(
            nome = nome,
            estoque = quantidade,
            valor = valor,
            dataAtualizacao = getDataAtualFormatada()
        )
        estoqueCollection.add(novoItem)
    }

    fun atualizarItem(id: String, nome: String, quantidade: Int, valor: Double) {
        val itemAtualizado = mapOf(
            "nome" to nome,
            "estoque" to quantidade,
            "valor" to valor,
            "dataAtualizacao" to getDataAtualFormatada()
        )
        estoqueCollection.document(id).set(itemAtualizado)
    }

    fun deletarItem(item: EstoqueItem) {
        item.id?.let { estoqueCollection.document(it).delete() }
    }

    fun decrementarEstoque(item: EstoqueItem) {
        item.id?.let {
            if ((item.estoque ?: 0) > 0) {
                estoqueCollection.document(it).update("estoque", FieldValue.increment(-1))
            }
        }
    }

    fun consumirItensDeVenda(produtosVendidos: List<Produto>) {
        val contagemDeProdutos = produtosVendidos.groupingBy { it }.eachCount()

        contagemDeProdutos.forEach { (produto, quantidadeTotalVendida) ->
            if (produto.ingredientes.isNotEmpty()) {
                produto.ingredientes.forEach { ingrediente ->
                    val quantidadeBaixar = ingrediente.quantidadeUsada * quantidadeTotalVendida
                    darBaixaEstoquePorId(ingrediente.estoqueItemId, quantidadeBaixar)
                }
            } else {
                darBaixaEstoquePorNome(produto.nome, quantidadeTotalVendida)
            }
        }
    }

    fun ajustarEstoqueAposEdicao(itensOriginais: List<Produto>, itensNovos: List<Produto>) {
        val contagemOriginal = itensOriginais.groupingBy { it }.eachCount()
        val contagemNova = itensNovos.groupingBy { it }.eachCount()
        val todosOsProdutos = (contagemOriginal.keys + contagemNova.keys).distinct()

        todosOsProdutos.forEach { produto ->
            val qtdOriginal = contagemOriginal[produto] ?: 0
            val qtdNova = contagemNova[produto] ?: 0
            val diferenca = qtdNova - qtdOriginal

            if (diferenca > 0) {
                consumirItensDeVenda(List(diferenca) { produto })
            } else if (diferenca < 0) {
                val quantidadeADevolver = -diferenca
                devolverItensAoEstoque(List(quantidadeADevolver) { produto })
            }
        }
    }

    private fun devolverItensAoEstoque(produtosDevolvidos: List<Produto>) {
        val contagemDeProdutos = produtosDevolvidos.groupingBy { it }.eachCount()

        contagemDeProdutos.forEach { (produto, quantidadeTotalDevolvida) ->
            if (produto.ingredientes.isNotEmpty()) {
                produto.ingredientes.forEach { ingrediente ->
                    val quantidadeDevolver = ingrediente.quantidadeUsada * quantidadeTotalDevolvida
                    adicionarQuantidadePorId(ingrediente.estoqueItemId, quantidadeDevolver)
                }
            } else {
                adicionarQuantidadePorNome(produto.nome, quantidadeTotalDevolvida)
            }
        }
    }

    fun adicionarQuantidade(item: EstoqueItem, quantidade: Int) {
        if (quantidade > 0) {
            item.id?.let {
                estoqueCollection.document(it).update("estoque", FieldValue.increment(quantidade.toLong()))
            }
        }
    }

    fun adicionarQuantidadeNome(nomeProduto: String, quantidade: Int) {
        if (quantidade > 0) {
            estoqueCollection.whereEqualTo("nome", nomeProduto).limit(1).get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val documentId = documents.first().id
                        estoqueCollection.document(documentId).update("estoque", FieldValue.increment(quantidade.toLong()))
                    }
                }
        }
    }

    fun darBaixaEstoque(nomeProduto: String, quantidadeVendida: Int) {
        estoqueCollection.whereEqualTo("nome", nomeProduto).limit(1).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val documentId = documents.first().id
                    val itemAtual = documents.first().toObject(EstoqueItem::class.java)

                    val novaQuantidade = (itemAtual.estoque - quantidadeVendida).coerceAtLeast(0)

                    estoqueCollection.document(documentId).update("estoque", novaQuantidade)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error giving stock out", e)
            }
    }

    private fun darBaixaEstoquePorNome(nomeProduto: String, quantidade: Int) {
        if (quantidade <= 0) return
        estoqueCollection.whereEqualTo("nome", nomeProduto).limit(1).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.first().id
                    estoqueCollection.document(docId).update("estoque", FieldValue.increment(-quantidade.toLong()))
                }
            }
    }

    private fun darBaixaEstoquePorId(itemId: String, quantidade: Double) {
        if (quantidade <= 0) return
        estoqueCollection.document(itemId).update("estoque", FieldValue.increment(-quantidade))
    }

    private fun adicionarQuantidadePorNome(nomeProduto: String, quantidade: Int) {
        if (quantidade <= 0) return
        estoqueCollection.whereEqualTo("nome", nomeProduto).limit(1).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.first().id
                    estoqueCollection.document(docId).update("estoque", FieldValue.increment(quantidade.toLong()))
                }
            }
    }

    private fun adicionarQuantidadePorId(itemId: String, quantidade: Double) {
        if (quantidade <= 0) return
        estoqueCollection.document(itemId).update("estoque", FieldValue.increment(quantidade))
    }
}