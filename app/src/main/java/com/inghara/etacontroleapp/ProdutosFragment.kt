package com.inghara.etacontroleapp

import ProdutoAdapter
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProdutosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProdutoAdapter
    private lateinit var searchBar: EditText
    private val listaCompleta = mutableListOf<Produto>()
    private val listaFiltrada = mutableListOf<Produto>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout para este fragmento
        return inflater.inflate(R.layout.fragment_produtos, container, false)
    }

    // onViewCreated é chamado DEPOIS que a view do fragmento foi criada.
    // É o lugar ideal para configurar as views.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa as views usando a 'view' que foi criada
        searchBar = view.findViewById(R.id.searchBar)
        recyclerView = view.findViewById(R.id.recyclerViewProdutos)
        val fabAdicionar: FloatingActionButton = view.findViewById(R.id.fab_add_produto)

        // Configura o Adapter e o RecyclerView
        adapter = ProdutoAdapter(listaFiltrada,
            onSalvarClick = { produto ->
                // TODO: Lógica para salvar alterações em um produto
            },
            onDeletarClick = { produto ->
                listaCompleta.remove(produto)
                listaFiltrada.remove(produto)
                adapter.notifyDataSetChanged()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Configura o listener do botão FAB
        fabAdicionar.setOnClickListener {
            // Lógica para ir para a tela de adicionar novo produto.
            Toast.makeText(context, "Abrir tela de cadastro de produto...", Toast.LENGTH_SHORT).show()
        }

        // Carrega os dados e configura a busca
        carregarProdutosFalsos()
        configurarBusca()
    }

    private fun carregarProdutosFalsos() {
        listaCompleta.clear() // Limpa para não duplicar dados
        listaCompleta.addAll(
            listOf(
                Produto("Combo Hamburguer de Costela", 57.0, "Ativo"),
                Produto("Pizza Calabresa", 42.0, "Inativo"),
                Produto("Suco Natural", 10.0, "Ativo"),
                Produto("Refrigerante", 8.0, "Ativo")
            )
        )
        listaFiltrada.clear()
        listaFiltrada.addAll(listaCompleta)
        adapter.notifyDataSetChanged()
    }

    private fun configurarBusca() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                listaFiltrada.clear()
                if (query.isEmpty()) {
                    listaFiltrada.addAll(listaCompleta)
                } else {
                    listaFiltrada.addAll(listaCompleta.filter {
                        it.nome.lowercase().contains(query)
                    })
                }
                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}