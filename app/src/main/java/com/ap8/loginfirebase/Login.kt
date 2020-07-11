package com.ap8.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private var auth: FirebaseAuth? = null

    private var TAG = "Login"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initFirebase()
    }

    fun initFirebase() {
        val email = findViewById(R.id.emaillogin) as EditText?
        val senha = findViewById(R.id.senhalogin) as EditText?
        val btnLogin = findViewById(R.id.btnlogin) as Button
        val btnRegister = findViewById(R.id.btnregister) as Button

        auth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener{ startActivity(Intent(this@Login, Cadastrar::class.java)) }

        btnLogin.setOnClickListener{ login(email, senha) }
    }

    private fun login(email: TextView?, senha: TextView?) {
        val email_ = email?.text.toString()
        val senha_ = senha?.text.toString()

        if(!TextUtils.isEmpty(email_) && !TextUtils.isEmpty(senha_)) {
            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show()

            auth!!.signInWithEmailAndPassword(email_!!, senha_!!).addOnCompleteListener(this){ task ->
                if(task.isSuccessful) {
                    Log.d(TAG,"Login usuário: Success")
                    updateUi()
                } else {
                    Log.e(TAG, "Login usuário: Failure", task.exception)
                    Toast.makeText(this@Login, "Autenticação falhou", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Algum campo não preenchido!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUi() {
        val intent = Intent(this@Login, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}