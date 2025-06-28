package com.inghara.etacontroleapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstoqueAdapter(
    var itemList: MutableList<EstoqueItem>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<EstoqueAdapter.EstoqueViewHolder>() {

    // A interface agora espera o objeto inteiro, não a posição
    interface OnItemClickListener {
        fun onAdicionarClick(item: EstoqueItem)
        fun onRemoverClick(item: EstoqueItem)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun atualizarLista(novaLista: List<EstoqueItem>) {
        itemList = novaLista.toMutableList()
        notifyDataSetChanged()
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

        val statusTexto = when (item.statusCalculado) {
            StatusEstoque.EM_ESTOQUE -> "Em Estoque"
            StatusEstoque.PERTO_DE_ACABAR -> "Perto de Acabar"
            StatusEstoque.FALTANDO -> "Faltando"
        }
        holder.quantity.text = "Estoque: ${item.estoque} | Status: $statusTexto"
        holder.price.text = ""

        // Agora passamos o 'item' inteiro no clique
        holder.addButton.setOnClickListener { listener.onAdicionarClick(item) }
        holder.removeButton.setOnClickListener { listener.onRemoverClick(item) }
    }

    override fun getItemCount() = itemList.size
}