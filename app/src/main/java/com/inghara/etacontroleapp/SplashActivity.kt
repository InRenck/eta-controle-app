package com.inghara.etacontroleapp

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout // Importe o FrameLayout
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val continuarButton = findViewById<FrameLayout>(R.id.arrow_button_wrapper)


        continuarButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
}