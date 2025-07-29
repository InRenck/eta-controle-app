package com.inghara.etacontroleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inghara.etacontroleapp.databinding.FragmentHomeBinding
import com.inghara.etacontroleapp.model.Venda
import com.inghara.etacontroleapp.ui.vendas.VendaAdapter
import com.inghara.etacontroleapp.viewmodel.VendasViewModel

class HomeFragment : Fragment(), VendaAdapter.OnVendaClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val vendasViewModel: VendasViewModel by activityViewModels()
    private lateinit var homeVendasAdapter: VendaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        vendasViewModel.pedidosPendentes.observe(viewLifecycleOwner) { pedidos ->
            if (pedidos.isNullOrEmpty()) {
                binding.recyclerPedidosHome.visibility = View.GONE
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.recyclerPedidosHome.visibility = View.VISIBLE
                binding.tvEmptyState.visibility = View.GONE
                homeVendasAdapter.submitList(pedidos)
            }
        }
    }

    private fun setupRecyclerView() {
        homeVendasAdapter = VendaAdapter(this)
        binding.recyclerPedidosHome.apply {
            adapter = homeVendasAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onVendaClick(venda: Venda) {
        Toast.makeText(context, "Pedido de ${venda.cliente}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}