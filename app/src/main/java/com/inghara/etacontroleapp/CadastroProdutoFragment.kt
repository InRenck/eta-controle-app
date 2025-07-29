package com.inghara.etacontroleapp

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.inghara.etacontroleapp.viewmodel.CardapioViewModel
import com.inghara.etacontroleapp.viewmodel.EstoqueViewModel

class CadastroProdutoFragment : Fragment() {

    private val cardapioViewModel: CardapioViewModel by activityViewModels()
    private val estoqueViewModel: EstoqueViewModel by activityViewModels() // Para buscar os itens de estoque

    private var produtoIdParaEditar: String? = null
    private var produtoParaEditar: Produto? = null

    private val ingredientesDaReceita = mutableListOf<Ingrediente>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        produtoIdParaEditar = arguments?.getString("produtoId")
        return inflater.inflate(R.layout.fragment_cadastro_produto, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titulo = view.findViewById<TextView>(R.id.tvTituloCadastro)
        val nomeInput = view.findViewById<EditText>(R.id.editNomeProduto)
        val precoInput = view.findViewById<EditText>(R.id.editPrecoProduto)
        val statusSwitch = view.findViewById<SwitchMaterial>(R.id.switchStatusProduto)
        val btnGerenciarIngredientes = view.findViewById<Button>(R.id.btnGerenciarIngredientes)
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvarProduto)

        produtoIdParaEditar?.let { id ->
            titulo.text = "Editar Produto"
            btnSalvar.text = "Atualizar"

            produtoParaEditar = cardapioViewModel.listaDeProdutos.value?.find { it.id == id }
            produtoParaEditar?.let {
                nomeInput.setText(it.nome)
                precoInput.setText(it.preco.toString())
                statusSwitch.isChecked = it.status == "Ativo"
                ingredientesDaReceita.addAll(it.ingredientes)
            }
        }

        btnGerenciarIngredientes.setOnClickListener {
            mostrarDialogoSelecaoIngrediente()
        }

        btnSalvar.setOnClickListener {
            val nome = nomeInput.text.toString()
            val precoStr = precoInput.text.toString()

            if (nome.isBlank() || precoStr.isBlank()) {
                Toast.makeText(context, "Preencha nome e preço", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val preco = precoStr.toDoubleOrNull() ?: 0.0
            val isAtivo = statusSwitch.isChecked

            if (produtoIdParaEditar != null) {
                cardapioViewModel.atualizarProduto(produtoIdParaEditar!!, nome, preco, isAtivo, ingredientesDaReceita)
                Toast.makeText(context, "Produto atualizado!", Toast.LENGTH_SHORT).show()
            } else {
                cardapioViewModel.adicionarProduto(nome, preco, isAtivo, ingredientesDaReceita)
                Toast.makeText(context, "Produto salvo!", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }

    private fun mostrarDialogoSelecaoIngrediente() {
        val itensDeEstoque = estoqueViewModel.listaEstoque.value ?: emptyList()
        if (itensDeEstoque.isEmpty()) {
            Toast.makeText(context, "Não há itens no estoque para adicionar como ingrediente.", Toast.LENGTH_SHORT).show()
            return
        }

        val nomesItensEstoque = itensDeEstoque.map { it.nome }.toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("Selecione um Ingrediente")
            .setItems(nomesItensEstoque) { _, which ->
                val itemSelecionado = itensDeEstoque[which]
                mostrarDialogoQuantidadeIngrediente(itemSelecionado)
            }
            .show()
    }

    private fun mostrarDialogoQuantidadeIngrediente(itemEstoque: EstoqueItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Quantidade de ${itemEstoque.nome}")
        builder.setMessage("Digite a quantidade deste ingrediente usada na receita:")

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        builder.setView(input)

        builder.setPositiveButton("Adicionar") { _, _ ->
            val quantidade = input.text.toString().toDoubleOrNull()
            if (quantidade != null && quantidade > 0) {
                val novoIngrediente = Ingrediente(
                    estoqueItemId = itemEstoque.id!!,
                    nome = itemEstoque.nome,
                    quantidadeUsada = quantidade
                )
                val index = ingredientesDaReceita.indexOfFirst { it.estoqueItemId == novoIngrediente.estoqueItemId }
                if (index != -1) {
                    ingredientesDaReceita[index] = novoIngrediente
                } else {
                    ingredientesDaReceita.add(novoIngrediente)
                }
                Toast.makeText(context, "${itemEstoque.nome} adicionado à receita.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}