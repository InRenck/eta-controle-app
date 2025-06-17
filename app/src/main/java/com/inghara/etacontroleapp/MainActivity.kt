package com.inghara.etacontroleapp

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.inghara.etacontroleapp.ui.vendas.VendasFragment
import com.inghara.etacontroleapp.EstoqueFragment
import com.inghara.etacontroleapp.PedidosFragment
import com.inghara.etacontroleapp.ProdutosFragment




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, VendasFragment())
            .commit()

        findViewById<ImageView>(R.id.nav_vendas).setOnClickListener {
            openFragment(VendasFragment())
        }

        findViewById<ImageView>(R.id.nav_estoque).setOnClickListener {
            openFragment(EstoqueFragment())
        }

        findViewById<ImageView>(R.id.nav_pedidos).setOnClickListener {
            openFragment(PedidosFragment())
        }

        findViewById<ImageView>(R.id.nav_produtos).setOnClickListener {
            openFragment(ProdutosFragment())
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Allow back navigation
            .commit()
    }
}
