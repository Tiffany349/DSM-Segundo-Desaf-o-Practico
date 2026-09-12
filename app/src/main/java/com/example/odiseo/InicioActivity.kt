package com.example.odiseo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.FirebaseAuth

class InicioActivity : AppCompatActivity() {

    private lateinit var cardVerDestinos: MaterialCardView
    private lateinit var cardAgregarDestino: MaterialCardView
    private lateinit var cardEditarDestino: MaterialCardView
    private lateinit var btnCerrarSesion: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        auth = FirebaseAuth.getInstance()

        cardVerDestinos = findViewById(R.id.cardVerDestinos)
        cardAgregarDestino = findViewById(R.id.cardAgregarDestino)
        cardEditarDestino = findViewById(R.id.cardEditarDestino)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)

        cardVerDestinos.setOnClickListener {

            val intent = Intent(
                this,
                ListaDestinosActivity::class.java
            )

            startActivity(intent)
        }

        cardAgregarDestino.setOnClickListener {

            val intent = Intent(
                this,
                AgregarDestinoActivity::class.java
            )

            startActivity(intent)
        }

        cardEditarDestino.setOnClickListener {

            val intent = Intent(
                this,
                EditarDestinosActivity::class.java
            )

            startActivity(intent)
        }

        btnCerrarSesion.setOnClickListener {

            auth.signOut()

            val intent = Intent(
                this,
                MainActivity::class.java
            )

            startActivity(intent)
            finish()
        }
    }
}