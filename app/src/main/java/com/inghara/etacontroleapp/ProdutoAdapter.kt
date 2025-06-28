package com.inghara.etacontroleapp // Garanta que o pacote está correto

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ProdutoAdapter(
    // 1. Mude de 'val' para 'var' para permitir que a lista seja atualizada
    private var listaProdutos: List<Produto>,
    private val onEditarClick: (Produto) -> Unit,
    private val onDeletarClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    // ... (seu ViewHolder e onCreateViewHolder continuam iguais)
    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeText: TextView = itemView.findViewById(R.id.tvNomeProduto)
        val precoText: TextView = itemView.findViewById(R.id.tvPreco)
        val statusText: TextView = itemView.findViewById(R.id.tvStatus)
        val btnSalvar: Button = itemView.findViewById(R.id.tvSalvar)
        val btnDeletar: Button = itemView.findViewById(R.id.tvDeletar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardapio, parent, false)
        return ProdutoViewHolder(itemView)
    }

    // ... (seu onBindViewHolder continua igual)
    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder.nomeText.text = produto.nome
        holder.precoText.text = "Total: R$%.2f".format(produto.preco)
        holder.statusText.text = "Status: ${produto.status}"

        // Lógica de cores para o status
        val context = holder.itemView.context
        val corDoStatus = when (produto.status) {
            "Ativo" -> ContextCompat.getColor(context, R.color.status_concluido)
            "Inativo" -> ContextCompat.getColor(context, R.color.status_cancelado)
            else -> ContextCompat.getColor(context, android.R.color.black)
        }
        holder.statusText.setTextColor(corDoStatus)

        holder.btnSalvar.setOnClickListener { onEditarClick(produto) }
        holder.btnDeletar.setOnClickListener { onDeletarClick(produto) }
    }

    override fun getItemCount(): Int = listaProdutos.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualizarLista(novaLista: List<Produto>) {
        listaProdutos = novaLista
        notifyDataSetChanged() // Avisa o RecyclerView para se redesenhar com os novos dados
    }
}