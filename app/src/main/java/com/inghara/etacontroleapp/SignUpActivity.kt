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

class SignUpActivity : AppCompatActivity() {

    // 1. Declaração da instância do Firebase Auth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // 2. Inicialização do Firebase Auth
        auth = FirebaseAuth.getInstance()

        // 3. Referências para os componentes da tela
        val createAccountBtn = findViewById<Button>(R.id.btnCreateAccount)
        val loginLink = findViewById<TextView>(R.id.tvLogin)
        val emailInput = findViewById<EditText>(R.id.editEmail)
        val phoneInput = findViewById<EditText>(R.id.editPhone)
        val passwordInput = findViewById<EditText>(R.id.editPassword)
        val confirmPasswordInput = findViewById<EditText>(R.id.editConfirmPassword)


        // 4. Configurar o clique no botão "Criar Conta"
        createAccountBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val confirmPassword = confirmPasswordInput.text.toString().trim()

            // --- VALIDAÇÃO DOS DADOS ---
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

            // Se todas as validações passarem, crie o usuário
            registerUser(email, password)
        }

        // 5. Configurar o clique no link "Login" para voltar
        loginLink.setOnClickListener {
            // Leva de volta para a tela de Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Finaliza a tela de Sign Up
        }
    }

    /**
     * Registra um novo usuário no Firebase Authentication.
     */
    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Cadastro bem-sucedido
                    Log.d("SignUpActivity", "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                    // O Firebase já faz o login automático após o cadastro.
                    // Então, podemos ir direto para a tela principal.
                    val intent = Intent(this, MainActivity::class.java)
                    // Limpa todas as telas anteriores (Login, Splash) da pilha de navegação
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish() // Garante que o usuário não volte para a tela de cadastro

                } else {
                    // Se o cadastro falhar, exibe uma mensagem.
                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.exception)

                    // Verifica o tipo de erro para dar uma mensagem mais útil
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(baseContext, "Este e-mail já está em uso.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(baseContext, "Falha no cadastro. Tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}