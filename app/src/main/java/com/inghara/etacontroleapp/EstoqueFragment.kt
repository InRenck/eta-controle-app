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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.ChipGroup

class EstoqueFragment : Fragment(), EstoqueAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EstoqueAdapter
    private lateinit var searchBar: EditText
    private lateinit var filterChipGroup: ChipGroup

    // A lista mestre original que nunca será modificada diretamente.
    private val listaMestre = mutableListOf(
        EstoqueItem("Hambúrguer de Picanha", 15, "19/06/2025", ""),
        EstoqueItem("Refrigerante", 8, "18/06/2025", ""),
        EstoqueItem("Batata Frita", 0, "17/06/2025", ""),
        EstoqueItem("Água Mineral", 50, "20/06/2025", "")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_estoque, container, false)
    }

    // É melhor prática configurar as views e listeners aqui.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa as Views
        recyclerView = view.findViewById(R.id.recyclerEstoque)
        searchBar = view.findViewById(R.id.searchBar)
        filterChipGroup = view.findViewById(R.id.filterChipGroup)

        // Configura o Adapter e o RecyclerView
        adapter = EstoqueAdapter(listaMestre, this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Configura os Listeners
        setupSearchListener()
        setupFilterListener()
    }

    private fun setupSearchListener() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aplicarFiltros()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFilterListener() {
        filterChipGroup.setOnCheckedStateChangeListener { group, checkedId ->
            aplicarFiltros()
        }
    }

    private fun aplicarFiltros() {
        var listaFiltrada = listaMestre.toList() // Começa com uma cópia da lista completa

        // 1. Aplica o filtro por STATUS (Chip)
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

        // 2. Aplica o filtro por TEXTO (Busca) sobre o resultado anterior
        val textoBusca = searchBar.text.toString()
        if (textoBusca.isNotEmpty()) {
            listaFiltrada = listaFiltrada.filter {
                it.nome.contains(textoBusca, ignoreCase = true)
            }
        }

        // 3. Atualiza o adapter com o resultado final
        adapter.atualizarLista(listaFiltrada)
    }


    override fun onAdicionarClick(position: Int) {
        val clickedItem = adapter.itemList[position]
        clickedItem.estoque++
        aplicarFiltros() // Re-aplica os filtros para caso o status do item mude
        Toast.makeText(requireContext(), "Adicionado 1 item ao ${clickedItem.nome}", Toast.LENGTH_SHORT).show()
    }

    override fun onRemoverClick(position: Int) {
        val clickedItem = adapter.itemList[position]
        if (clickedItem.estoque > 0) {
            clickedItem.estoque--
            aplicarFiltros() // Re-aplica os filtros
            Toast.makeText(requireContext(), "Removido 1 item de ${clickedItem.nome}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "${clickedItem.nome} já está com estoque zerado.", Toast.LENGTH_SHORT).show()
        }
    }
}