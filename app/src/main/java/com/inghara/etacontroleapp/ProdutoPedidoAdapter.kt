package com.inghara.etacontroleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoPedidoAdapter(
    private val produtos: MutableList<Produto>,
    private val onRemoveClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoPedidoAdapter.ViewHolder>() {

    var onDataChanged: (() -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeProduto: TextView = view.findViewById(R.id.tvNomeProduto)
        val btnRemover: Button = view.findViewById(R.id.btnRemover)

        fun bind(produto: Produto) {
            nomeProduto.text = produto.nome
            btnRemover.setOnClickListener {
                onRemoveClick(produto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedidos, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = produtos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(produtos[position])
    }

    fun addProduto(produto: Produto) {
        produtos.add(produto)
        notifyItemInserted(produtos.size - 1)
        onDataChanged?.invoke()
    }

    fun removeProduto(produto: Produto) {
        val index = produtos.indexOf(produto)
        if (index != -1) {
            produtos.removeAt(index)
            notifyItemRemoved(index)
            onDataChanged?.invoke()
        }
    }

    fun getLista(): List<Produto> = produtos
}
