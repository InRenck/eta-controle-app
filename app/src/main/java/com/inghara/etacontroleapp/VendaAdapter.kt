package com.inghara.etacontroleapp.ui.vendas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.R
import com.inghara.etacontroleapp.model.Venda
import java.util.Locale

class VendaAdapter(
    private val listener: OnVendaClickListener
) : RecyclerView.Adapter<VendaAdapter.VendaViewHolder>(), Filterable {

    private var listaVendasCompleta: List<Venda> = ArrayList()
    private var listaVendasFiltrada: List<Venda> = ArrayList()

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

    override fun getItemCount() = listaVendasFiltrada.size

    override fun onBindViewHolder(holder: VendaViewHolder, position: Int) {
        val venda = listaVendasFiltrada[position]

        holder.id.text = "ID: ${venda.id}"
        holder.cliente.text = "Cliente: ${venda.cliente}"
        holder.data.text = "Data: ${venda.data}"
        holder.hora.text = "Hora: ${venda.hora}"
        holder.total.text = "Total: ${venda.total}"
        holder.status.text = "Status: ${venda.status}"

        val context = holder.itemView.context
        val corResource = when (venda.status) {
            "Concluída" -> R.color.status_concluido
            "Pendente" -> R.color.status_pendente
            "Cancelada" -> R.color.status_cancelado
            else -> R.color.black
        }
        holder.status.setTextColor(ContextCompat.getColor(context, corResource))

        holder.itemView.setOnClickListener {
            listener.onVendaClick(venda)
        }
    }

    fun submitList(lista: List<Venda>) {
        listaVendasCompleta = lista
        listaVendasFiltrada = lista
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val textoBusca = constraint.toString().lowercase(Locale.getDefault()).trim()
                val resultadoFiltro = if (textoBusca.isEmpty()) {
                    listaVendasCompleta
                } else {
                    listaVendasCompleta.filter { venda ->
                        venda.cliente.lowercase(Locale.getDefault()).contains(textoBusca) ||
                                venda.status.lowercase(Locale.getDefault()).contains(textoBusca)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = resultadoFiltro
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaVendasFiltrada = results?.values as? List<Venda> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}