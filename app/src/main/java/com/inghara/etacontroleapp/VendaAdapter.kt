package com.inghara.etacontroleapp.ui.vendas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.R
import com.inghara.etacontroleapp.databinding.ItemVendaBinding
import com.inghara.etacontroleapp.model.Venda

class VendaAdapter(private val lista: List<Venda>) :
    RecyclerView.Adapter<VendaAdapter.VendaViewHolder>() {

    class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvId: TextView = itemView.findViewById(R.id.tvVendaId)
        val tvCliente: TextView = itemView.findViewById(R.id.tvVendaCliente)
        val tvData: TextView = itemView.findViewById(R.id.tvVendaData)
        val tvHora: TextView = itemView.findViewById(R.id.tvVendaHora)
        val tvTotal: TextView = itemView.findViewById(R.id.tvVendaPreco)
        val tvStatus: TextView = itemView.findViewById(R.id.tvVendaStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_venda, parent, false)
        return VendaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val venda = lista[position]
        holder.tvId.text = "ID: ${venda.id}"
        holder.tvCliente.text = "Cliente: ${venda.cliente}"
        holder.tvData.text = "Data: ${venda.data}"
        holder.tvHora.text = "Hora: ${venda.hora}"
        holder.tvTotal.text = "Total: ${venda.total}"
        holder.tvStatus.text = "Status: ${venda.status}"
    }

    override fun getItemCount(): Int = lista.size
}
