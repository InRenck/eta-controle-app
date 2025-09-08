package com.inghara.etacontroleapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class EstoqueAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EstoqueAdapter.EstoqueViewHolder>(), Filterable {

    private var listaCompleta: List<EstoqueItem> = ArrayList()
    private var listaExibida: List<EstoqueItem> = ArrayList()
    private var isAdminMode: Boolean = false

    interface OnItemClickListener {
        fun onAdicionarClick(item: EstoqueItem)
        fun onRemoverClick(item: EstoqueItem)
        fun onItemClick(item: EstoqueItem)
        fun onExcluirClick(item: EstoqueItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(novaLista: List<EstoqueItem>) {
        listaCompleta = novaLista
        listaExibida = novaLista
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAdminMode(isAdmin: Boolean) {
        isAdminMode = isAdmin
        notifyDataSetChanged()
    }

    inner class EstoqueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvProductName)
        val price: TextView = itemView.findViewById(R.id.tvProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val addButton: TextView = itemView.findViewById(R.id.tvAdd)
        val removeButton: TextView = itemView.findViewById(R.id.tvRemove)
        val deleteButton: TextView = itemView.findViewById(R.id.tvDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstoqueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estoque, parent, false)
        return EstoqueViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstoqueViewHolder, position: Int) {
        val item = listaExibida[position]

        holder.name.text = item.nome
        holder.price.text = "R$ %.2f".format(item.valor)

        val statusTexto = when (item.statusCalculado) {
            StatusEstoque.EM_ESTOQUE -> "Em Estoque"
            StatusEstoque.PERTO_DE_ACABAR -> "Perto de Acabar"
            StatusEstoque.FALTANDO -> "Faltando"
        }
        holder.quantity.text = "Estoque: ${item.estoque} | Status: $statusTexto"

        val visibility = if (isAdminMode) View.VISIBLE else View.GONE
        holder.addButton.visibility = visibility
        holder.removeButton.visibility = visibility
        holder.deleteButton.visibility = visibility

        holder.addButton.setOnClickListener { listener.onAdicionarClick(item) }
        holder.removeButton.setOnClickListener { listener.onRemoverClick(item) }
        holder.itemView.setOnClickListener { listener.onItemClick(item)}
        holder.deleteButton.setOnClickListener { listener.onExcluirClick(item) }
    }

    override fun getItemCount() = listaExibida.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val textoBusca = constraint.toString().lowercase(Locale.getDefault()).trim()
                val resultadoFiltro = if (textoBusca.isEmpty()) {
                    listaCompleta
                } else {
                    listaCompleta.filter { item ->
                        item.nome.lowercase(Locale.getDefault()).contains(textoBusca)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultadoFiltro
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaExibida = results?.values as? List<EstoqueItem> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}