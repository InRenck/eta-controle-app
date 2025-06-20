package com.inghara.etacontroleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstoqueAdapter(
    // A lista inicial que o adapter recebe.
    var itemList: List<EstoqueItem>,
    private val listener: OnItemClickListener
) :
    RecyclerView.Adapter<EstoqueAdapter.EstoqueViewHolder>() {

    interface OnItemClickListener {
        fun onAdicionarClick(position: Int)
        fun onRemoverClick(position: Int)
    }

    // Método para atualizar a lista do adapter de forma eficiente
    fun atualizarLista(novaLista: List<EstoqueItem>) {
        itemList = novaLista
        notifyDataSetChanged() // Avisa o RecyclerView para se redesenhar
    }

    inner class EstoqueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvProductName)
        val price: TextView = itemView.findViewById(R.id.tvProductPrice)
        val quantity: TextView = itemView.findViewById(R.id.tvProductQuantity)
        val addButton: TextView = itemView.findViewById(R.id.tvAdd)
        val removeButton: TextView = itemView.findViewById(R.id.tvRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstoqueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estoque, parent, false)
        return EstoqueViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstoqueViewHolder, position: Int) {
        val item = itemList[position]

        holder.name.text = item.nome

        // Vamos usar o statusCalculado que criamos!
        val statusTexto = when (item.statusCalculado) {
            StatusEstoque.EM_ESTOQUE -> "Em Estoque"
            StatusEstoque.PERTO_DE_ACABAR -> "Perto de Acabar"
            StatusEstoque.FALTANDO -> "Faltando"
        }
        holder.quantity.text = "Estoque: ${item.estoque} | Status: $statusTexto"

        holder.price.text = "" // Opcional: defina um preço ou esconda

        holder.addButton.setOnClickListener { listener.onAdicionarClick(position) }
        holder.removeButton.setOnClickListener { listener.onRemoverClick(position) }
    }

    override fun getItemCount() = itemList.size
}