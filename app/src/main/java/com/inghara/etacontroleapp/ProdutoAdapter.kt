package com.inghara.etacontroleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ProdutoAdapter(
    private val onEditarClick: (Produto) -> Unit,
    private val onDeletarClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>(), Filterable {

    private var listaProdutosCompleta: List<Produto> = ArrayList()
    private var listaProdutosFiltrada: List<Produto> = ArrayList()

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

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutosFiltrada[position]
        holder.nomeText.text = produto.nome
        holder.precoText.text = "Total: R$%.2f".format(produto.preco)
        holder.statusText.text = "Status: ${produto.status}"

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

    override fun getItemCount(): Int = listaProdutosFiltrada.size

    fun submitList(novaLista: List<Produto>) {
        listaProdutosCompleta = novaLista
        listaProdutosFiltrada = novaLista
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val textoBusca = constraint.toString().lowercase(Locale.getDefault()).trim()
                val resultadoFiltro = if (textoBusca.isEmpty()) {
                    listaProdutosCompleta
                } else {
                    listaProdutosCompleta.filter { produto ->
                        produto.nome.lowercase(Locale.getDefault()).contains(textoBusca)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultadoFiltro
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaProdutosFiltrada = results?.values as? List<Produto> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}