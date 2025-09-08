package com.inghara.etacontroleapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.inghara.etacontroleapp.Produto
import com.inghara.etacontroleapp.model.Venda
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VendasViewModel : ViewModel() {

    private val _listaVendas = MutableLiveData<List<Venda>>()
    val listaVendas: LiveData<List<Venda>> = _listaVendas

    private val _pedidosPendentes = MutableLiveData<List<Venda>>()
    val pedidosPendentes: LiveData<List<Venda>> = _pedidosPendentes

    private val _valorTotalVendidoDia = MutableLiveData<Double>()
    val valorTotalVendidoDia: LiveData<Double> = _valorTotalVendidoDia

    private val db = Firebase.firestore
    private val vendasCollection = db.collection("vendas")

    init {
        vendasCollection.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("Firestore", "Error listening for sales", error)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val vendas = it.toObjects(Venda::class.java)
                    _listaVendas.value = vendas
                    filtrarPedidosPendentes(vendas)
                    calcularVendasDoDia(vendas)
                }
            }
    }

    private fun calcularVendasDoDia(vendas: List<Venda>) {
        val hoje = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val vendasDeHoje = vendas.filter { it.data == hoje && it.status == "Concluída" }
        val total = vendasDeHoje.sumOf {
            it.total.replace("R$", "").replace(",", ".").trim().toDoubleOrNull() ?: 0.0
        }
        _valorTotalVendidoDia.value = total
    }

    private fun filtrarPedidosPendentes(vendas: List<Venda>) {
        val pendentes = vendas.filter { it.status == "Pendente" }
        _pedidosPendentes.value = pendentes
    }

    fun adicionarVenda(cliente: String, total: String, itensVendidos: List<Produto>) {
        val dataFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val horaFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dataAtual = Date()

        val novaVenda = hashMapOf(
            "cliente" to cliente,
            "data" to dataFormat.format(dataAtual),
            "hora" to horaFormat.format(dataAtual),
            "total" to total,
            "status" to "Pendente",
            "itens" to itensVendidos,
            "timestamp" to com.google.firebase.Timestamp(dataAtual)
        )
        vendasCollection.add(novaVenda)
    }

    fun atualizarVenda(vendaId: String, cliente: String, total: String, itens: List<Produto>) {
        val vendaAtualizada = mapOf(
            "cliente" to cliente,
            "total" to total,
            "itens" to itens
        )
        vendasCollection.document(vendaId).update(vendaAtualizada)
    }

    fun atualizarStatusVenda(vendaId: String, novoStatus: String) {
        vendasCollection.document(vendaId).update("status", novoStatus)
    }
}