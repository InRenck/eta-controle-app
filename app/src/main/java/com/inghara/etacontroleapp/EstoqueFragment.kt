package com.inghara.etacontroleapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inghara.etacontroleapp.viewmodel.EstoqueViewModel
import androidx.navigation.fragment.findNavController


class EstoqueFragment : Fragment(), EstoqueAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EstoqueAdapter
    private lateinit var searchBar: EditText
    private lateinit var filterChipGroup: ChipGroup

    private val viewModel: EstoqueViewModel by activityViewModels()

    private var listaCompleta: List<EstoqueItem> = listOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_estoque, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerEstoque)
        searchBar = view.findViewById(R.id.searchBar)
        filterChipGroup = view.findViewById(R.id.filterChipGroup)
        val fabAdd: FloatingActionButton = view.findViewById(R.id.fab_add_estoque)

        adapter = EstoqueAdapter(mutableListOf(), this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        setupSearchAndFilter()
        fabAdd.setOnClickListener {
            // TODO: No próximo passo, vamos navegar para a tela de cadastro de estoque
            findNavController().navigate(R.id.action_estoqueFragment_to_cadastroEstoqueFragment)
        }

        viewModel.listaEstoque.observe(viewLifecycleOwner) { lista ->

            listaCompleta = lista
            aplicarFiltros()
        }
    }

    private fun setupSearchAndFilter() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aplicarFiltros()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        filterChipGroup.setOnCheckedStateChangeListener { _, _ -> aplicarFiltros() }
    }

    private fun aplicarFiltros() {
        var listaFiltrada = listaCompleta.toList()

        val selectedChipId = filterChipGroup.checkedChipId
        if (selectedChipId != View.NO_ID && selectedChipId != R.id.chipTodos) {
            listaFiltrada = listaFiltrada.filter { item ->
                when (selectedChipId) {
                    R.id.chipEmEstoque -> item.statusCalculado == StatusEstoque.EM_ESTOQUE
                    R.id.chipPertoDeAcabar -> item.statusCalculado == StatusEstoque.PERTO_DE_ACABAR
                    R.id.chipFaltando -> item.statusCalculado == StatusEstoque.FALTANDO
                    else -> true
                }
            }
        }

        val textoBusca = searchBar.text.toString()
        if (textoBusca.isNotEmpty()) {
            listaFiltrada = listaFiltrada.filter {
                it.nome.contains(textoBusca, ignoreCase = true)
            }
        }

        adapter.atualizarLista(listaFiltrada)
    }

    override fun onAdicionarClick(item: EstoqueItem) {
        viewModel.incrementarEstoque(item)
    }

    override fun onRemoverClick(item: EstoqueItem) {
        viewModel.decrementarEstoque(item)
    }
}