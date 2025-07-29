package com.inghara.etacontroleapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.inghara.etacontroleapp.databinding.FragmentProdutosBinding
import com.inghara.etacontroleapp.viewmodel.CardapioViewModel

class ProdutosFragment : Fragment() {

    private var _binding: FragmentProdutosBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ProdutoAdapter
    private val cardapioViewModel: CardapioViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProdutosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearch()

        cardapioViewModel.listaDeProdutos.observe(viewLifecycleOwner) { produtos ->
            if (produtos.isNullOrEmpty()) {
                binding.recyclerViewProdutos.visibility = View.GONE
                binding.tvEmptyList.visibility = View.VISIBLE
            } else {
                binding.recyclerViewProdutos.visibility = View.VISIBLE
                binding.tvEmptyList.visibility = View.GONE
            }
            adapter.submitList(produtos)
        }

        binding.fabAddProduto.setOnClickListener {
            findNavController().navigate(R.id.action_produtosFragment_to_cadastroProdutoFragment)
        }
    }

    private fun setupRecyclerView() {
        adapter = ProdutoAdapter(
            onEditarClick = { produto ->
                val bundle = bundleOf("produtoId" to produto.id)
                findNavController().navigate(R.id.action_produtosFragment_to_cadastroProdutoFragment, bundle)
            },
            onDeletarClick = { produto ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Deletar Produto")
                    .setMessage("Tem certeza que deseja deletar o produto '${produto.nome}'?")
                    .setPositiveButton("Sim") { _, _ ->
                        cardapioViewModel.deletarProduto(produto)
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }
        )

        binding.recyclerViewProdutos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProdutos.adapter = adapter
    }

    private fun setupSearch() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}