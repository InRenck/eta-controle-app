package com.inghara.etacontroleapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EstoqueFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EstoqueAdapter
    private lateinit var searchBar: EditText

    private var estoqueList = listOf(
        EstoqueItem("Hambúrguer de Picanha", 15, "12/06/2025", "Precisa Comprar Mais"),
        EstoqueItem("Refrigerante", 25, "10/06/2025", "OK"),
        EstoqueItem("Batata Frita", 5, "09/06/2025", "Precisa Comprar Mais"),
        EstoqueItem("Água Mineral", 50, "11/06/2025", "OK")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_estoque, container, false)

        recyclerView = view.findViewById(R.id.recyclerEstoque)
        searchBar = view.findViewById(R.id.searchBar)

        adapter = EstoqueAdapter(estoqueList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Search filtering
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = estoqueList.filter {
                    it.nome.contains(s.toString(), ignoreCase = true)
                }
                adapter = EstoqueAdapter(filtered)
                recyclerView.adapter = adapter
            }
        })

        return view
    }
}
