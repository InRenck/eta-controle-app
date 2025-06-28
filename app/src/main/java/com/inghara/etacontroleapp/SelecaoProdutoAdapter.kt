package com.inghara.etacontroleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SelecaoProdutoAdapter(
    private val listaProdutos: List<Produto>,
    private val onProdutoClick: (Produto) -> Unit
) : RecyclerView.Adapter<SelecaoProdutoAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeProduto: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Usando um layout simples do próprio Android para a lista
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder.nomeProduto.text = "${produto.nome} - R$ ${"%.2f".format(produto.preco)}"
        holder.itemView.setOnClickListener {
            onProdutoClick(produto)
        }
    }

    override fun getItemCount() = listaProdutos.size
}