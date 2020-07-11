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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_cadastrar.*

class Cadastrar : AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var auth: FirebaseAuth? = null

    private var TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        initFirebase()
    }

    private fun initFirebase() {
        val email = findViewById(R.id.emailcadastro) as EditText?
        val senha = findViewById(R.id.senhacadastro) as EditText?
        val btn = findViewById(R.id.btncadastro) as Button

        database = FirebaseDatabase.getInstance()
        databaseReference = database!!.reference.child("Users")
        auth = FirebaseAuth.getInstance()

        btn.setOnClickListener{ createAccount(email, senha) }
    }

    fun createAccount(email: TextView?, senha: TextView?) {
        val email_ = email?.text.toString()
        val senha_ = senha?.text.toString()

        if(!TextUtils.isEmpty(email_) && !TextUtils.isEmpty(senha_)) {
            Toast.makeText(this, "Cadastro realizado!", Toast.LENGTH_SHORT).show()

            auth!!.createUserWithEmailAndPassword(email_, senha_).addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.d(TAG,"Criar usuário: Success")

                    val userId = auth!!.currentUser!!.uid

                    val userDB = databaseReference!!.child(userId)
                    userDB.child("email").setValue(email_)
                    userDB.child("senha").setValue(senha_)

                    updateUi()
                } else {
                    Log.w(TAG, "Criar usuário: Failure")
                    Toast.makeText(this@Cadastrar, "Autenticação falhou", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Algum campo não preenchido!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUi() {
        val intent = Intent(this@Cadastrar, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
}