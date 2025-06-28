package com.inghara.etacontroleapp.ui.vendas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.R
import com.inghara.etacontroleapp.model.Venda

class VendaAdapter(
    private val listaVendas: List<Venda>,

    private val listener: OnVendaClickListener
) : RecyclerView.Adapter<VendaAdapter.VendaViewHolder>() {

    interface OnVendaClickListener {
        fun onVendaClick(venda: Venda)
    }

    inner class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.tvVendaId)
        val cliente: TextView = itemView.findViewById(R.id.tvVendaCliente)
        val data: TextView = itemView.findViewById(R.id.tvVendaData)
        val hora: TextView = itemView.findViewById(R.id.tvVendaHora)
        val total: TextView = itemView.findViewById(R.id.tvVendaPreco)
        val status: TextView = itemView.findViewById(R.id.tvVendaStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venda, parent, false)
        return VendaViewHolder(view)
    }

    override fun getItemCount() = listaVendas.size

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val venda = listaVendas[position]

        // Preenche os dados (como antes)
        holder.id.text = "ID: ${venda.id}"
        holder.cliente.text = "Cliente: ${venda.cliente}"
        holder.data.text = "Data: ${venda.data}"
        holder.hora.text = "Hora: ${venda.hora}"
        holder.total.text = "Total: ${venda.total}"
        holder.status.text = "Status: ${venda.status}"

        // Lógica das cores (como antes)
        val context = holder.itemView.context
        val corResource = when (venda.status) {
            "Concluída" -> R.color.status_concluido
            "Pendente" -> R.color.status_pendente
            "Cancelada" -> R.color.status_cancelado
            else -> R.color.black
        }
        holder.status.setTextColor(ContextCompat.getColor(context, corResource))

        // PASSO 1.3: Tornamos o card inteiro clicável
        holder.itemView.setOnClickListener {
            // Quando clicado, ele chama a função da nossa interface
            listener.onVendaClick(venda)
        }
    }
}