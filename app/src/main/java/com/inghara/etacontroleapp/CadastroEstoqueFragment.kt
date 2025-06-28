package com.inghara.etacontroleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.inghara.etacontroleapp.viewmodel.EstoqueViewModel

class CadastroEstoqueFragment : Fragment() {

    private val viewModel: EstoqueViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cadastro_estoque, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nomeInput = view.findViewById<EditText>(R.id.editNomeItemEstoque)
        val quantidadeInput = view.findViewById<EditText>(R.id.editQuantidadeInicial)
        val valorInput = view.findViewById<EditText>(R.id.editValor) // Pega a referência do novo campo
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvarItemEstoque)

        btnSalvar.setOnClickListener {
            val nome = nomeInput.text.toString().trim()
            val quantidadeStr = quantidadeInput.text.toString().trim()
            val valorStr = valorInput.text.toString().trim() // Pega o texto do valor

            if (nome.isBlank() || quantidadeStr.isBlank() || valorStr.isBlank()) { // Valida todos os campos
                Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantidade = quantidadeStr.toInt()
            val valor = valorStr.toDouble()

            viewModel.adicionarItem(nome, quantidade, valor)
            Toast.makeText(context, "Item salvo no estoque!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}