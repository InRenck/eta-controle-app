package com.inghara.etacontroleapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Enable back button on the action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set click listener to go to LoginActivity
        val loginLink = findViewById<TextView>(R.id.tvLogin)
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: prevents returning to Sign Up screen
        }
    }
}
