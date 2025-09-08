package com.inghara.etacontroleapp

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.inghara.etacontroleapp.viewmodel.EstoqueViewModel

class CadastroEstoqueFragment : Fragment() {

    private val viewModel: EstoqueViewModel by activityViewModels()
    private var itemParaEditar: EstoqueItem? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        itemParaEditar = arguments?.getParcelable("estoqueItemParaEditar", EstoqueItem::class.java)
        return inflater.inflate(R.layout.fragment_cadastro_estoque, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titulo = view.findViewById<TextView>(R.id.tvTituloCadastro)
        val nomeInput = view.findViewById<EditText>(R.id.editNomeItemEstoque)
        val quantidadeInput = view.findViewById<EditText>(R.id.editQuantidadeInicial)
        val valorInput = view.findViewById<EditText>(R.id.editValor)
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvarItemEstoque)

        itemParaEditar?.let { item ->
            titulo.text = "Editar Item do Estoque"
            btnSalvar.text = "Atualizar"

            nomeInput.setText(item.nome)
            quantidadeInput.setText(item.estoque.toString())
            valorInput.setText(item.valor.toString())
        }

        btnSalvar.setOnClickListener {
            val nome = nomeInput.text.toString().trim()
            val quantidadeStr = quantidadeInput.text.toString().trim()
            val valorStr = valorInput.text.toString().trim()

            if (nome.isBlank() || quantidadeStr.isBlank() || valorStr.isBlank()) {
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantidade = quantidadeStr.toIntOrNull()
            val valor = valorStr.toDoubleOrNull()

            if (quantidade == null || valor == null) {
                Toast.makeText(context, "Quantidade ou valor inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (itemParaEditar != null) {
                viewModel.atualizarItem(itemParaEditar!!.id!!, nome, quantidade, valor)
                Toast.makeText(context, "Item atualizado!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.adicionarItem(nome, quantidade, valor)
                Toast.makeText(context, "Item salvo no estoque!", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }
}