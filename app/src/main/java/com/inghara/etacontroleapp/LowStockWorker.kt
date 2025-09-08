package com.inghara.etacontroleapp

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class LowStockWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private val db = Firebase.firestore
    private val estoqueCollection = db.collection("estoque")

    override suspend fun doWork(): Result {
        return try {
            val querySnapshot = estoqueCollection.get().await()
            val listaEstoque = querySnapshot.toObjects(EstoqueItem::class.java)

            val itensEmEstoqueBaixo = listaEstoque.filter {
                it.statusCalculado == StatusEstoque.PERTO_DE_ACABAR ||
                        it.statusCalculado == StatusEstoque.FALTANDO
            }

            if (itensEmEstoqueBaixo.isNotEmpty()) {
                val totalItens = itensEmEstoqueBaixo.size
                val mensagem = if (totalItens == 1) {
                    "Um item está com estoque baixo!"
                } else {
                    "$totalItens itens estão com estoque baixo!"
                }

                showNotification(mensagem)
            }

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "low_stock_channel")
            .setSmallIcon(R.drawable.ic_estoque)
            .setContentTitle("Alerta de Estoque")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}