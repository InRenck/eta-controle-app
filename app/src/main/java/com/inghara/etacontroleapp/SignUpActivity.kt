package com.inghara.etacontroleapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val createAccountBtn = findViewById<Button>(R.id.btnCreateAccount)
        val loginLink = findViewById<TextView>(R.id.tvLogin)
        val emailInput = findViewById<EditText>(R.id.editEmail)
        val phoneInput = findViewById<EditText>(R.id.editPhone)
        val passwordInput = findViewById<EditText>(R.id.editPassword)
        val confirmPasswordInput = findViewById<EditText>(R.id.editConfirmPassword)


        createAccountBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            if (email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não conferem.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(email, password)
        }

        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Registra um novo usuário no Firebase Authentication e cria um documento no Firestore.
     */
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val db = Firebase.firestore
                        val userRef = db.collection("users").document(it.uid)

                        val userData = hashMapOf(
                            "email" to email,
                            "isAdmin" to false
                        )
                        userRef.set(userData)
                            .addOnSuccessListener {
                                Log.d("SignUpActivity", "createUserWithEmail:success")
                                Toast.makeText(baseContext, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.w("SignUpActivity", "Error adding user document", e)
                                Toast.makeText(baseContext, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(baseContext, "Este e-mail já está em uso.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}