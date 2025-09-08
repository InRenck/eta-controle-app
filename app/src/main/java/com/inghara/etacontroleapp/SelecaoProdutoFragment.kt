package com.inghara.etacontroleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.viewmodel.CardapioViewModel
import com.inghara.etacontroleapp.viewmodel.PedidoAtualViewModel

class SelecaoProdutoFragment : Fragment() {

    private val cardapioViewModel: CardapioViewModel by activityViewModels()
    private val pedidoAtualViewModel: PedidoAtualViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selecao_produto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvSelecaoProdutos)
        recyclerView.layoutManager = LinearLayoutManager(context)

        cardapioViewModel.listaDeProdutos.observe(viewLifecycleOwner) { produtosDoCardapio ->
            recyclerView.adapter = SelecaoProdutoAdapter(produtosDoCardapio) { produtoSelecionado ->
                pedidoAtualViewModel.addProdutoAoPedido(produtoSelecionado)
                findNavController().popBackStack()
            }
        }
    }
}