import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inghara.etacontroleapp.Produto
import com.inghara.etacontroleapp.R

class ProdutoAdapter(
    private val listaProdutos: List<Produto>,
    private val onSalvarClick: (Produto) -> Unit,
    private val onDeletarClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeText: TextView = itemView.findViewById(R.id.tvNomeProduto)
        val precoText: TextView = itemView.findViewById(R.id.tvPreco)
        val statusText: TextView = itemView.findViewById(R.id.tvStatus)
        val btnSalvar: Button = itemView.findViewById(R.id.tvSalvar)
        val btnDeletar: Button = itemView.findViewById(R.id.tvDeletar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cardapio, parent, false)
        return ProdutoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder.nomeText.text = produto.nome
        holder.precoText.text = "Total: R$%.2f".format(produto.preco)
        holder.statusText.text = "Status: ${produto.status}"

        holder.btnSalvar.setOnClickListener { onSalvarClick(produto) }
        holder.btnDeletar.setOnClickListener { onDeletarClick(produto) }
    }

    override fun getItemCount(): Int = listaProdutos.size
}
