package com.inghara.etacontroleapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.inghara.etacontroleapp.databinding.ActivityHomeBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var navIcons: List<ImageView>
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        createNotificationChannel()
        scheduleLowStockWorker()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        val navHome = binding.navHome
        val navVendas = binding.navVendas
        val navEstoque = binding.navEstoque
        val navPedidos = binding.navPedidos
        val navProdutos = binding.navProdutos

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

    private fun createPedidosNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Novos Pedidos"
            val descriptionText = "Notificações para novos pedidos."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("pedidos_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun selecionarIcone(iconIdToSelect: Int) {
        if (iconIdToSelect != 0) {
            for (icon in navIcons) {
                icon.isSelected = (icon.id == iconIdToSelect)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alerta de Estoque"
            val descriptionText = "Notificações para itens com estoque baixo."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("low_stock_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleLowStockWorker() {
        val startOfWorkDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 5)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val initialDelayStart = startOfWorkDay.timeInMillis - Calendar.getInstance().timeInMillis

        val lowStockWorkStart = PeriodicWorkRequestBuilder<LowStockWorker>(
            1, TimeUnit.DAYS
        ).setInitialDelay(initialDelayStart, TimeUnit.MILLISECONDS)
            .addTag("low_stock_worker_start")
            .build()

        WorkManager.getInstance(this).enqueue(lowStockWorkStart)

        val endOfWorkDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        val initialDelayEnd = endOfWorkDay.timeInMillis - Calendar.getInstance().timeInMillis

        val lowStockWorkEnd = PeriodicWorkRequestBuilder<LowStockWorker>(
            1, TimeUnit.DAYS
        ).setInitialDelay(initialDelayEnd, TimeUnit.MILLISECONDS)
            .addTag("low_stock_worker_end")
            .build()

        WorkManager.getInstance(this).enqueue(lowStockWorkEnd)
    }
}