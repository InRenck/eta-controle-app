import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.Produto
import com.inghara.etacontroleapp.R

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
        val view = inflater.inflate(R.layout.fragment_produtos, container, false)

        searchBar = view.findViewById(R.id.searchBar)
        recyclerView = view.findViewById(R.id.recyclerViewProdutos)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProdutoAdapter(listaFiltrada,
            onSalvarClick = { produto ->
                // TODO: handle salvar
            },
            onDeletarClick = { produto ->
                listaCompleta.remove(produto)
                listaFiltrada.remove(produto)
                adapter.notifyDataSetChanged()
            }
        )
        recyclerView.adapter = adapter

        carregarProdutosFalsos()
        configurarBusca()

        return view
    }

    private fun carregarProdutosFalsos() {
        listaCompleta.addAll(
            listOf(
                Produto("Combo Hamburguer de Costela", 57.0, "Ativo"),
                Produto("Pizza Calabresa", 42.0, "Inativo"),
                Produto("Suco Natural", 10.0, "Ativo"),
                Produto("Refrigerante", 8.0, "Ativo")
            )
        )
        listaFiltrada.addAll(listaCompleta)
        adapter.notifyDataSetChanged()
    }

    private fun configurarBusca() {
        searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                listaFiltrada.clear()
                listaFiltrada.addAll(listaCompleta.filter {
                    it.nome.lowercase().contains(query)
                })
                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
