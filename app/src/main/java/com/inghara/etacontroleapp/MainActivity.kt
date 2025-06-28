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

        // Encontra o NavController a partir do container no XML
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // Encontra os ícones da sua barra
        val navHome = findViewById<ImageView>(R.id.nav_home)
        val navVendas = findViewById<ImageView>(R.id.nav_vendas)
        val navEstoque = findViewById<ImageView>(R.id.nav_estoque)
        val navPedidos = findViewById<ImageView>(R.id.nav_pedidos)
        val navProdutos = findViewById<ImageView>(R.id.nav_produtos)

        navIcons = listOf(navHome, navVendas, navEstoque, navPedidos, navProdutos)

        // Configura os cliques para usar o NavController
        navHome.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
        navVendas.setOnClickListener {
            navController.navigate(R.id.vendasFragment)
        }
        navEstoque.setOnClickListener {
            navController.navigate(R.id.estoqueFragment)
        }
        navPedidos.setOnClickListener {
            navController.navigate(R.id.pedidosFragment)
        }
        navProdutos.setOnClickListener {
            navController.navigate(R.id.produtosFragment)
        }

        // Sincroniza a cor do ícone com a tela atual
        navController.addOnDestinationChangedListener { _, destination, _ ->
            selecionarIconePeloDestino(destination.id)
        }
    }

    /**
     * Atualiza a cor dos ícones com base no destino atual.
     */
    private fun selecionarIconePeloDestino(destinationId: Int) {
        val iconIdToSelect = when (destinationId) {
            R.id.homeFragment -> R.id.nav_home
            R.id.vendasFragment -> R.id.nav_vendas
            R.id.estoqueFragment -> R.id.nav_estoque
            R.id.pedidosFragment -> R.id.nav_pedidos
            R.id.produtosFragment -> R.id.nav_produtos
            else -> 0 // Não seleciona nenhum se não for uma tela principal
        }

        if (iconIdToSelect != 0) {
            for (icon in navIcons) {
                icon.isSelected = (icon.id == iconIdToSelect)
            }
        }
    }
}