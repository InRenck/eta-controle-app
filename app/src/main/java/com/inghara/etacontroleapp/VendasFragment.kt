package com.inghara.etacontroleapp.ui.vendas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.R
import com.inghara.etacontroleapp.databinding.FragmentVendasBinding
import com.inghara.etacontroleapp.model.Venda


class VendasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VendaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vendas, container, false)

        recyclerView = view.findViewById(R.id.recyclerVendas)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val listaVendas = listOf(
            Venda("001", "João Silva", "12/06/2025", "14:30", "R$250,00", "Concluída"),
            Venda("002", "Maria Oliveira", "13/06/2025", "10:15", "R$120,00", "Pendente"),
            Venda("003", "Pedro Costa", "14/06/2025", "16:45", "R$330,00", "Cancelada")
        )

        adapter = VendaAdapter(listaVendas)
        recyclerView.adapter = adapter

        return view
    }
}

