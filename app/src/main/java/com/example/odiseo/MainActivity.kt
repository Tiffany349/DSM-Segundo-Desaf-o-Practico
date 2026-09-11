package com.example.odiseo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var etCorreoLogin: TextInputEditText
    private lateinit var etPasswordLogin: TextInputEditText
    private lateinit var btnIniciarSesion: Button
    private lateinit var tvIrRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        etCorreoLogin = findViewById(R.id.etCorreoLogin)
        etPasswordLogin = findViewById(R.id.etPasswordLogin)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        tvIrRegistro = findViewById(R.id.tvIrRegistro)

        btnIniciarSesion.setOnClickListener {
            iniciarSesion()
        }

        tvIrRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun iniciarSesion() {

        val correo = etCorreoLogin.text.toString().trim()
        val password = etPasswordLogin.text.toString()

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this,
                R.string.error_login_campos,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        auth.signInWithEmailAndPassword(correo, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        R.string.login_exitoso,
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, AgregarDestinoActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        R.string.error_login,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}