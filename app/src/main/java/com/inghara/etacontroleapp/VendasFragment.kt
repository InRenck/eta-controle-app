package com.inghara.etacontroleapp.ui.vendas

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inghara.etacontroleapp.databinding.FragmentVendasBinding
import com.inghara.etacontroleapp.model.Venda
import com.inghara.etacontroleapp.viewmodel.VendasViewModel
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.inghara.etacontroleapp.R

class VendasFragment : Fragment(), VendaAdapter.OnVendaClickListener {

    private var _binding: FragmentVendasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VendasViewModel by activityViewModels()
    private lateinit var vendaAdapter: VendaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()

        viewModel.listaVendas.observe(viewLifecycleOwner) { lista ->
            if (lista.isNullOrEmpty()) {
                binding.recyclerVendas.visibility = View.GONE
                binding.tvEmptyList.visibility = View.VISIBLE
            } else {
                binding.recyclerVendas.visibility = View.VISIBLE
                binding.tvEmptyList.visibility = View.GONE
            }
            vendaAdapter.submitList(lista)
        }
    }

    private fun setupRecyclerView() {
        vendaAdapter = VendaAdapter(this)
        binding.recyclerVendas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = vendaAdapter
        }
    }

    private fun setupSearch() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vendaAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }


    override fun onVendaClick(venda: Venda) {
        val options = mutableListOf<String>()
        if (venda.status == "Pendente") {
            options.add("Editar Pedido")
        }
        options.add("Alterar Status")

        AlertDialog.Builder(requireContext())
            .setTitle("Pedido de ${venda.cliente}")
            .setItems(options.toTypedArray()) { _, which ->
                when (options[which]) {
                    "Editar Pedido" -> {
                        val bundle = bundleOf("vendaId" to venda.id)
                        findNavController().navigate(R.id.action_vendasFragment_to_pedidosFragment, bundle)
                    }
                    "Alterar Status" -> {
                        mostrarDialogoDeStatus(venda)
                    }
                }
            }
            .create()
            .show()
    }

    private fun mostrarDialogoDeStatus(venda: Venda) {
        val statusOptions = arrayOf("Pendente", "Concluída", "Cancelada")
        AlertDialog.Builder(requireContext())
            .setTitle("Alterar Status do Pedido")
            .setItems(statusOptions) { _, which ->
                val novoStatus = statusOptions[which]
                venda.id?.let { viewModel.atualizarStatusVenda(it, novoStatus) }
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}