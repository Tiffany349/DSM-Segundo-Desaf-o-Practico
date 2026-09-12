package com.example.odiseo

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Intent
import android.widget.Button

class ListaDestinosActivity : AppCompatActivity() {

    private lateinit var rvDestinos: RecyclerView
    private lateinit var progressBarDestinos: ProgressBar

    private val db = FirebaseFirestore.getInstance()

    private val destinos = mutableListOf<Destino>()

    private lateinit var destinoAdapter: DestinoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_lista_destinos
        )

        findViewById<Button>(R.id.btnVolverMenu).setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        rvDestinos = findViewById(
            R.id.rvDestinos
        )

        progressBarDestinos = findViewById(
            R.id.progressBarDestinos
        )

        configurarRecyclerView()

        cargarDestinos()
    }

    private fun configurarRecyclerView() {

        destinoAdapter = DestinoAdapter(
            destinos
        )

        rvDestinos.layoutManager =
            LinearLayoutManager(this)

        rvDestinos.adapter =
            destinoAdapter
    }

    private fun cargarDestinos() {

        progressBarDestinos.visibility =
            View.VISIBLE

        db.collection("destinos")
            .get()
            .addOnSuccessListener { result ->

                destinos.clear()

                for (document in result) {

                    val destino = Destino(
                        id = document.id,
                        nombre = document.getString(
                            "nombre"
                        ) ?: "",
                        pais = document.getString(
                            "pais"
                        ) ?: "",
                        precio = document.getDouble(
                            "precio"
                        ) ?: 0.0,
                        descripcion = document.getString(
                            "descripcion"
                        ) ?: "",
                        imagenUrl = document.getString(
                            "imagenUrl"
                        ) ?: ""
                    )

                    destinos.add(destino)
                }

                destinoAdapter.notifyDataSetChanged()

                progressBarDestinos.visibility =
                    View.GONE
            }
            .addOnFailureListener { error ->

                progressBarDestinos.visibility =
                    View.GONE

                Toast.makeText(
                    this,
                    getString(
                        R.string.error_cargar_destinos,
                        error.message ?: ""
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}