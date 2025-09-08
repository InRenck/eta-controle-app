package com.inghara.etacontroleapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var loginBtn: Button
    private lateinit var loginProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)

        loginBtn = findViewById(R.id.login_button)
        loginProgressBar = findViewById(R.id.login_progress_bar)

        val signupLink = findViewById<TextView>(R.id.signup_link)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val rememberMeCheck = findViewById<CheckBox>(R.id.remember_me)
        val forgotPasswordLink = findViewById<TextView>(R.id.forgot_password)

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

        forgotPasswordLink.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Digite seu e-mail no campo acima para redefinir a senha.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "E-mail de redefinição enviado para $email", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Falha ao enviar e-mail. Verifique se o endereço está correto.", Toast.LENGTH_LONG).show()
                        Log.e("ForgotPassword", "Error: ", task.exception)
                    }
                }
        }
    }

    private fun saveFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            val db = Firebase.firestore
            val user = auth.currentUser
            user?.uid?.let { uid ->
                val tokenMap = hashMapOf("token" to token)
                db.collection("fcmTokens").document(uid).set(tokenMap)
                    .addOnSuccessListener {
                        Log.d("FCM", "Token salvo para o usuário $uid")
                    }
                    .addOnFailureListener { e ->
                        Log.w("FCM", "Erro ao salvar o token", e)
                    }
            }
        }
    }

    private fun signInUser(email: String, password: String, rememberMe: Boolean) {
        showLoading(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)

                if (task.isSuccessful) {
                    Log.d("LoginActivity", "signInWithEmail:success")
                    Toast.makeText(baseContext, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()

                    savePreferences(email, password, rememberMe)
                    saveFCMToken()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Falha na autenticação. Verifique suas credenciais.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loginBtn.text = ""
            loginBtn.isEnabled = false
            loginProgressBar.visibility = View.VISIBLE
        } else {
            loginBtn.text = "Login"
            loginBtn.isEnabled = true
            loginProgressBar.visibility = View.GONE
        }
    }

    private fun savePreferences(email: String, password: String, rememberMe: Boolean) {
        val editor = sharedPreferences.edit()
        if (rememberMe) {
            editor.putString("saved_email", email)
            editor.putString("saved_password", password)
            editor.putBoolean("remember_me_checked", true)
        } else {
            editor.clear()
        }
        editor.apply()
    }

    private fun loadPreferences(emailInput: EditText, rememberMeCheck: CheckBox) {
        val rememberMeChecked = sharedPreferences.getBoolean("remember_me_checked", false)
        if (rememberMeChecked) {
            val savedEmail = sharedPreferences.getString("saved_email", "")
            emailInput.setText(savedEmail)
            rememberMeCheck.isChecked = true
        }
    }
}