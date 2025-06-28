package com.inghara.etacontroleapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        // Referências para os componentes da tela (Views)
        val loginBtn = findViewById<Button>(R.id.login_button)
        val signupLink = findViewById<TextView>(R.id.signup_link)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val rememberMeCheck = findViewById<CheckBox>(R.id.remember_me)
        val forgotPasswordLink = findViewById<TextView>(R.id.forgot_password) // NOVO

        loadPreferences(emailInput, rememberMeCheck)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signInUser(email, password, rememberMeCheck.isChecked)
        }

        signupLink.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // --- LÓGICA PARA "ESQUECEU A SENHA" --- // NOVO
        forgotPasswordLink.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                // Pede para o usuário digitar o email primeiro
                Toast.makeText(this, "Digite seu e-mail no campo acima para redefinir a senha.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // Envia o e-mail de redefinição usando o Firebase
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "E-mail de redefinição enviado para $email", Toast.LENGTH_LONG).show()
                    } else {
                        // Mostra um erro genérico para não vazar informação se o email existe ou não
                        Toast.makeText(this, "Falha ao enviar e-mail. Verifique se o endereço está correto.", Toast.LENGTH_LONG).show()
                        Log.e("ForgotPassword", "Error: ", task.exception)
                    }
                }
        }
    }

    private fun signInUser(email: String, password: String, rememberMe: Boolean) {
        // ... (código da função de login continua o mesmo)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("LoginActivity", "signInWithEmail:success")
                    Toast.makeText(baseContext, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                    savePreferences(email, rememberMe)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Falha na autenticação. Verifique suas credenciais.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun savePreferences(email: String, rememberMe: Boolean) {
        // ... (código da função de salvar preferências continua o mesmo)
        val editor = sharedPreferences.edit()
        if (rememberMe) {
            editor.putString("saved_email", email)
            editor.putBoolean("remember_me_checked", true)
        } else {
            editor.clear()
        }
        editor.apply()
    }

    private fun loadPreferences(emailInput: EditText, rememberMeCheck: CheckBox) {
        // ... (código da função de carregar preferências continua o mesmo)
        val rememberMeChecked = sharedPreferences.getBoolean("remember_me_checked", false)
        if (rememberMeChecked) {
            val savedEmail = sharedPreferences.getString("saved_email", "")
            emailInput.setText(savedEmail)
            rememberMeCheck.isChecked = true
        }
    }
}