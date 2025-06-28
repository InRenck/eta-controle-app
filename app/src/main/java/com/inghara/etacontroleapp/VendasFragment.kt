package com.inghara.etacontroleapp.ui.vendas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.inghara.etacontroleapp.databinding.FragmentVendasBinding
import com.inghara.etacontroleapp.model.Venda
import com.inghara.etacontroleapp.viewmodel.VendasViewModel
import androidx.appcompat.app.AlertDialog


// PASSO 2.1: O Fragment agora "assina o contrato" da nossa interface
class VendasFragment : Fragment(), VendaAdapter.OnVendaClickListener {

    private var _binding: FragmentVendasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VendasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVendasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerVendas.layoutManager = LinearLayoutManager(context)

        viewModel.listaVendas.observe(viewLifecycleOwner) { lista ->

            binding.recyclerVendas.adapter = VendaAdapter(lista, this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onVendaClick(venda: Venda) {
        val statusOptions = arrayOf("Pendente", "Concluída", "Cancelada")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Alterar Status do Pedido #${venda.id}")

        builder.setItems(statusOptions) { dialog, which ->
            val novoStatus = statusOptions[which]

            viewModel.atualizarStatusVenda(venda.id, novoStatus)
        }

        builder.create().show()
    }
}