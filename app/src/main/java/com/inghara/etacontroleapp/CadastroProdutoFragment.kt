package com.inghara.etacontroleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.inghara.etacontroleapp.viewmodel.CardapioViewModel

class CadastroProdutoFragment : Fragment() {

    private val viewModel: CardapioViewModel by activityViewModels()
    private var produtoIdParaEditar: String? = null

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
        val btnSalvar = view.findViewById<Button>(R.id.btnSalvarProduto)


        produtoIdParaEditar?.let { id ->

            titulo.text = "Editar Produto"
            btnSalvar.text = "Atualizar"

            val produto = viewModel.getProdutoPorId(id)
            produto?.let {
                nomeInput.setText(it.nome)
                precoInput.setText(it.preco.toString())
                statusSwitch.isChecked = it.status == "Ativo"
            }
        }

        btnSalvar.setOnClickListener {
            val nome = nomeInput.text.toString()
            val precoStr = precoInput.text.toString()

            if (nome.isBlank() || precoStr.isBlank()) {
                Toast.makeText(context, "Preencha nome e preço", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val preco = precoStr.toDoubleOrNull()
            if (preco == null) {
                Toast.makeText(context, "Preço inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val isAtivo = statusSwitch.isChecked

            if (produtoIdParaEditar != null) {
                viewModel.atualizarProduto(produtoIdParaEditar!!, nome, preco, isAtivo)
                Toast.makeText(context, "Produto atualizado!", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.adicionarProduto(nome, preco, isAtivo)
                Toast.makeText(context, "Produto salvo!", Toast.LENGTH_SHORT).show()
            }

            findNavController().popBackStack()
        }
    }
}