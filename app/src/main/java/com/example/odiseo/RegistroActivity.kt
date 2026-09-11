package com.example.odiseo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.odiseo.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        binding.tvVolverLogin.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario() {

        val correo = binding.etCorreoRegistro.text.toString().trim()
        val password = binding.etPasswordRegistro.text.toString()
        val confirmarPassword = binding.etConfirmarPassword.text.toString()

        if (correo.isEmpty() ||
            password.isEmpty() ||
            confirmarPassword.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Todos los campos son obligatorios",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (password.length < 6) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 6 caracteres",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (password != confirmarPassword) {
            Toast.makeText(
                this,
                "Las contraseñas no coinciden",
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        auth.createUserWithEmailAndPassword(correo, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Cuenta creada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )

                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "No se pudo crear la cuenta",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}