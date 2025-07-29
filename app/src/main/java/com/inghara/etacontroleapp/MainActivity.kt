package com.inghara.etacontroleapp

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navIcons: List<ImageView>
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navVendas = findViewById<ImageView>(R.id.nav_vendas)
        val navEstoque = findViewById<ImageView>(R.id.nav_estoque)
        val navPedidos = findViewById<ImageView>(R.id.nav_pedidos)
        val navProdutos = findViewById<ImageView>(R.id.nav_produtos)

        navIcons = listOf(navHome, navVendas, navEstoque, navPedidos, navProdutos)

        navHome.setOnClickListener { navController.navigate(R.id.action_global_homeFragment) }
        navVendas.setOnClickListener { navController.navigate(R.id.action_global_vendasFragment) }
        navEstoque.setOnClickListener { navController.navigate(R.id.action_global_estoqueFragment) }
        navPedidos.setOnClickListener { navController.navigate(R.id.action_global_pedidosFragment) }
        navProdutos.setOnClickListener { navController.navigate(R.id.action_global_produtosFragment) }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val iconIdToSelect = when (destination.id) {
                R.id.homeFragment -> R.id.nav_home
                R.id.vendasFragment -> R.id.nav_vendas
                R.id.estoqueFragment, R.id.cadastroEstoqueFragment -> R.id.nav_estoque
                R.id.pedidosFragment, R.id.selecaoProdutoFragment -> R.id.nav_pedidos
                R.id.produtosFragment, R.id.cadastroProdutoFragment -> R.id.nav_produtos
                else -> 0
            }
            selecionarIcone(iconIdToSelect)
        }
    }

    private fun selecionarIcone(iconIdToSelect: Int) {
        if (iconIdToSelect != 0) {
            for (icon in navIcons) {
                icon.isSelected = (icon.id == iconIdToSelect)
            }
        }
    }
}