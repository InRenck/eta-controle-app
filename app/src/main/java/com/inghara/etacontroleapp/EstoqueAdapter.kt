package com.inghara.etacontroleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EstoqueAdapter(private val itemList: List<EstoqueItem>) :
    RecyclerView.Adapter<EstoqueAdapter.EstoqueViewHolder>() {

    inner class EstoqueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.food_name)
        val quantity: TextView = itemView.findViewById(R.id.quantidadeItem)
        val date: TextView = itemView.findViewById(R.id.ultimaAtualização)
        val status: TextView = itemView.findViewById(R.id.tvVendaStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstoqueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_estoque, parent, false)
        return EstoqueViewHolder(view)
    }

    override fun onBindViewHolder(holder: EstoqueViewHolder, position: Int) {
        val item = itemList[position]
        holder.name.text = item.nome
        holder.quantity.text = "Estoque: ${item.estoque}"
        holder.date.text = "Data: ${item.dataAtualizacao}"
        holder.status.text = "Status: ${item.status}"
    }

    override fun getItemCount() = itemList.size
}
