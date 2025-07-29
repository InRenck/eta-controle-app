package com.inghara.etacontroleapp

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.inghara.etacontroleapp.databinding.FragmentEstoqueBinding
import com.inghara.etacontroleapp.viewmodel.EstoqueViewModel
import java.util.Locale
import androidx.core.os.bundleOf

class EstoqueFragment : Fragment(), EstoqueAdapter.OnItemClickListener {

    private var _binding: FragmentEstoqueBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: EstoqueAdapter
    private val viewModel: EstoqueViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEstoqueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()

        viewModel.listaEstoque.observe(viewLifecycleOwner) { lista ->
            aplicarFiltros(lista)
        }
    }

    private fun setupRecyclerView() {
        adapter = EstoqueAdapter(this)
        binding.recyclerEstoque.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEstoque.adapter = adapter
    }

    private fun setupListeners() {
        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                aplicarFiltros(viewModel.listaEstoque.value ?: emptyList())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.filterChipGroup.setOnCheckedStateChangeListener { _, _ ->
            aplicarFiltros(viewModel.listaEstoque.value ?: emptyList())
        }

        binding.fabAddEstoque.setOnClickListener {
            findNavController().navigate(R.id.action_estoqueFragment_to_cadastroEstoqueFragment)
        }
    }

    private fun aplicarFiltros(listaOriginal: List<EstoqueItem>) {
        var listaFiltrada = listaOriginal

        val textoBusca = binding.searchBar.text.toString().lowercase(Locale.getDefault()).trim()
        if (textoBusca.isNotEmpty()) {
            listaFiltrada = listaFiltrada.filter {
                it.nome.lowercase(Locale.getDefault()).contains(textoBusca)
            }
        }

        val selectedChipId = binding.filterChipGroup.checkedChipId
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

        if (listaFiltrada.isEmpty()) {
            binding.recyclerEstoque.visibility = View.GONE
            binding.tvEmptyList.visibility = View.VISIBLE
        } else {
            binding.recyclerEstoque.visibility = View.VISIBLE
            binding.tvEmptyList.visibility = View.GONE
        }

        adapter.submitList(listaFiltrada)
    }

    override fun onItemClick(item: EstoqueItem) {
        val bundle = bundleOf("estoqueItemParaEditar" to item)
        findNavController().navigate(R.id.action_estoqueFragment_to_cadastroEstoqueFragment, bundle)
    }

    override fun onAdicionarClick(item: EstoqueItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Adicionar ao Estoque")
        builder.setMessage("Digite a quantidade de '${item.nome}' a ser adicionada:")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Adicionar") { dialog, _ ->
            val quantidadeStr = input.text.toString()
            val quantidade = quantidadeStr.toIntOrNull()

            if (quantidade != null && quantidade > 0) {
                viewModel.adicionarQuantidade(item, quantidade)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    override fun onRemoverClick(item: EstoqueItem) {
        viewModel.decrementarEstoque(item)
    }

    override fun onExcluirClick(item: EstoqueItem) {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Item")
            .setMessage("Tem certeza que deseja excluir '${item.nome}' do estoque permanentemente? Esta ação não pode ser desfeita.")
            .setPositiveButton("Sim, Excluir") { _, _ ->
                viewModel.deletarItem(item)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}