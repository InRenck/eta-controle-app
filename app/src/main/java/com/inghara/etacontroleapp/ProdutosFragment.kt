package com.inghara.etacontroleapp

import android.os.Bundle
import androidx.core.os.bundleOf
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.inghara.etacontroleapp.viewmodel.CardapioViewModel

class ProdutosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoAdapter
    private lateinit var searchBar: EditText

    private val cardapioViewModel: CardapioViewModel by activityViewModels()

    private var listaCompleta = listOf<Produto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_produtos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBar = view.findViewById(R.id.searchBar)
        recyclerView = view.findViewById(R.id.recyclerViewProdutos)
        val fabAdicionar: FloatingActionButton = view.findViewById(R.id.fab_add_produto)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        cardapioViewModel.listaDeProdutos.observe(viewLifecycleOwner) { produtos ->
            listaCompleta = produtos

            adapter = ProdutoAdapter(
                produtos,
                onEditarClick = { produto ->
                    // Substitua o Toast por estas linhas:

                    // 1. Cria um "pacote" (Bundle) para enviar o ID do produto
                    val bundle = bundleOf("produtoId" to produto.id)

                    // 2. Navega para a tela de cadastro, levando o pacote junto
                    findNavController().navigate(R.id.action_produtosFragment_to_cadastroProdutoFragment, bundle)
                },
                onDeletarClick = { produto ->
                    // ...
                }
            )
            recyclerView.adapter = adapter
            configurarBusca()
        }

        fabAdicionar.setOnClickListener {
            findNavController().navigate(R.id.action_produtosFragment_to_cadastroProdutoFragment)
        }
    }

    private fun configurarBusca() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase()
                val listaFiltrada = if (query.isEmpty()) {
                    listaCompleta
                } else {
                    listaCompleta.filter { it.nome.lowercase().contains(query) }
                }
                adapter.atualizarLista(listaFiltrada)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}