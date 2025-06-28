package com.inghara.etacontroleapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoPedidoAdapter(
    private var produtos: MutableList<Produto>,
    private val onRemoveClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoPedidoAdapter.ViewHolder>() {

    /**
     * Esta é a única função que o Fragment precisa para atualizar o Adapter.
     * Ela recebe a nova lista do ViewModel e avisa o RecyclerView para se redesenhar.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun atualizarLista(novaLista: MutableList<Produto>) {
        produtos = novaLista
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nomeProduto: TextView = view.findViewById(R.id.tvNomeProduto)
        private val btnRemover: Button = view.findViewById(R.id.btnRemover)

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
}