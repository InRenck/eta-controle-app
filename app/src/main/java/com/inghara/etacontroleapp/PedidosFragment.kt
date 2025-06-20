package com.inghara.etacontroleapp

import com.inghara.etacontroleapp.ProdutoPedidoAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PedidosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoPedidoAdapter
    private lateinit var tvTotal: TextView
    private val listaProdutos = mutableListOf<Produto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedidos, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewProdutos)
        tvTotal = view.findViewById(R.id.tvTotalPedido)

        adapter = ProdutoPedidoAdapter(listaProdutos) { produto ->
            adapter.removeProduto(produto)
        }

        adapter.onDataChanged = {
            atualizarTotal()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val btnAdd = view.findViewById<Button>(R.id.bttnAdd)
        btnAdd.setOnClickListener {
            val produto = Produto("Produto Exemplo", 10.0)
            adapter.addProduto(produto)
        }

        atualizarTotal()

        return view
    }

    private fun atualizarTotal() {
        val total = adapter.getLista().sumOf { it.preco }
        tvTotal.text = "Total: R$ %.2f".format(total)
    }
}
