package com.inghara.etacontroleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.inghara.etacontroleapp.databinding.FragmentPedidosBinding
import com.inghara.etacontroleapp.model.Venda
import com.inghara.etacontroleapp.viewmodel.EstoqueViewModel
import com.inghara.etacontroleapp.viewmodel.PedidoAtualViewModel
import com.inghara.etacontroleapp.viewmodel.VendasViewModel
import java.util.Locale

class PedidosFragment : Fragment() {

    private var _binding: FragmentPedidosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProdutoPedidoAdapter

    private val pedidoAtualViewModel: PedidoAtualViewModel by activityViewModels()
    private val vendasViewModel: VendasViewModel by activityViewModels()
    private val estoqueViewModel: EstoqueViewModel by activityViewModels()

    private val args: PedidosFragmentArgs by navArgs()
    private var vendaOriginal: Venda? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPedidosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vendaIdParaEditar = args.vendaId
        setupRecyclerView()

        if (vendaIdParaEditar == null) {
            pedidoAtualViewModel.limparPedido()
            setupParaNovoPedido()
        } else {
            vendasViewModel.listaVendas.value?.find { it.id == vendaIdParaEditar }?.let {
                vendaOriginal = it.copy()
                pedidoAtualViewModel.carregarVendaParaEdicao(it)
                setupParaEdicao(it)
            }
        }

        setupObservers()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = ProdutoPedidoAdapter(mutableListOf()) { produto ->
            pedidoAtualViewModel.removeProdutoDoPedido(produto)
        }
        binding.recyclerViewProdutos.adapter = adapter
        binding.recyclerViewProdutos.layoutManager = LinearLayoutManager(context)
    }

    private fun setupListeners() {
        binding.bttnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_pedidosFragment_to_selecaoProdutoFragment)
        }

        binding.btnSalvarPedido.setOnClickListener {
            val cliente = binding.editTextCliente.text.toString().trim()
            val total = binding.tvTotalPedido.text.toString()
            val itensNovos = pedidoAtualViewModel.produtosNoPedido.value

            if (cliente.isNotBlank() && !itensNovos.isNullOrEmpty()) {
                if (vendaOriginal == null) {
                    vendasViewModel.adicionarVenda(cliente, total, itensNovos)
                    estoqueViewModel.consumirItensDeVenda(itensNovos)
                    Toast.makeText(context, "Pedido salvo!", Toast.LENGTH_SHORT).show()
                } else {
                    estoqueViewModel.ajustarEstoqueAposEdicao(vendaOriginal!!.itens, itensNovos)
                    vendasViewModel.atualizarVenda(vendaOriginal!!.id!!, cliente, total, itensNovos)
                    Toast.makeText(context, "Pedido atualizado!", Toast.LENGTH_SHORT).show()
                }
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Cliente e itens são obrigatórios.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupParaNovoPedido() {
        binding.btnSalvarPedido.text = "Salvar Pedido"
    }

    private fun setupParaEdicao(venda: Venda) {
        binding.btnSalvarPedido.text = "Atualizar Pedido"
        binding.editTextCliente.setText(venda.cliente)
    }

    private fun setupObservers() {
        pedidoAtualViewModel.produtosNoPedido.observe(viewLifecycleOwner) { lista ->
            adapter.atualizarLista(lista.toMutableList())
        }
        pedidoAtualViewModel.totalDoPedido.observe(viewLifecycleOwner) { total ->
            binding.tvTotalPedido.text = String.format(Locale.getDefault(), "Total: R$ %.2f", total)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pedidoAtualViewModel.limparPedido()
        _binding = null
    }
}