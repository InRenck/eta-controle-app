package com.inghara.etacontroleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.viewmodel.PedidoAtualViewModel
import com.inghara.etacontroleapp.viewmodel.VendasViewModel
import java.util.Locale



class PedidosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoPedidoAdapter
    private lateinit var tvTotal: TextView

    private val pedidoAtualViewModel: PedidoAtualViewModel by activityViewModels()
    private val vendasViewModel: VendasViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pedidos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTotal = view.findViewById(R.id.tvTotalPedido)
        recyclerView = view.findViewById(R.id.recyclerViewProdutos)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ProdutoPedidoAdapter(mutableListOf()) { produto ->
            pedidoAtualViewModel.removeProdutoDoPedido(produto)
        }
        recyclerView.adapter = adapter

        pedidoAtualViewModel.produtosNoPedido.observe(viewLifecycleOwner) { listaDeProdutos ->
            adapter.atualizarLista(listaDeProdutos) // Atualiza o adapter com a nova lista
        }

        pedidoAtualViewModel.totalDoPedido.observe(viewLifecycleOwner) { total ->
            tvTotal.text = String.format(Locale.getDefault(), "Total: R$ %.2f", total)
        }

        val btnAddProduto = view.findViewById<Button>(R.id.bttnAdd)
        btnAddProduto.setOnClickListener {
            // Navega para a tela de seleção de produtos usando a ação que você criou no nav_graph
            findNavController().navigate(R.id.action_pedidosFragment_to_selecaoProdutoFragment)
        }

        val btnSalvar = view.findViewById<Button>(R.id.btnSalvarPedido)
        val clienteInput = view.findViewById<EditText>(R.id.editTextCliente)

        btnSalvar.setOnClickListener {
            val cliente = clienteInput.text.toString().trim()
            val totalFormatado = tvTotal.text.toString()

            if (cliente.isNotBlank() && pedidoAtualViewModel.produtosNoPedido.value?.isNotEmpty() == true) {
                vendasViewModel.adicionarVenda(cliente, totalFormatado)
                Toast.makeText(context, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show()

                clienteInput.text.clear()
                pedidoAtualViewModel.limparPedido()
            } else {
                Toast.makeText(context, "Adicione um cliente e produtos ao pedido.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}