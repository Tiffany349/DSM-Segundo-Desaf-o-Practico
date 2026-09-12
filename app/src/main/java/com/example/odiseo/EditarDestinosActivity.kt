package com.example.odiseo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class EditarDestinosActivity : AppCompatActivity() {

    private lateinit var rvEditarDestinos: RecyclerView
    private lateinit var progressBarEditar: ProgressBar

    private val db = FirebaseFirestore.getInstance()

    private val destinos = mutableListOf<Destino>()

    private lateinit var adapter: EditarDestinoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editar_destinos)

        rvEditarDestinos = findViewById(R.id.rvEditarDestinos)

        progressBarEditar = findViewById(R.id.progressBarEditar)

        configurarRecyclerView()
        cargarDestinos()
    }

    private fun configurarRecyclerView() {

        adapter = EditarDestinoAdapter(
            destinos,
            onEditarClick = { destino ->

                val intent = Intent(
                    this,
                    EditarDestinoActivity::class.java
                )

                intent.putExtra(
                    "destinoId",
                    destino.id
                )

                startActivity(intent)
            },
            onEliminarClick = { destino ->

                mostrarDialogoEliminar(destino)
            }
        )

        rvEditarDestinos.layoutManager =
            LinearLayoutManager(this)

        rvEditarDestinos.adapter = adapter
    }

    private fun cargarDestinos() {

        progressBarEditar.visibility = View.VISIBLE

        db.collection("destinos")
            .addSnapshotListener { result, error ->

                if (error != null) {

                    progressBarEditar.visibility = View.GONE

                    Toast.makeText(
                        this,
                        getString(
                            R.string.error_cargar_destinos,
                            error.message ?: ""
                        ),
                        Toast.LENGTH_LONG
                    ).show()

                    return@addSnapshotListener
                }

                if (result != null) {

                    destinos.clear()

                    for (document in result.documents) {

                        val destino = Destino(
                            id = document.id,
                            nombre = document.getString("nombre") ?: "",
                            pais = document.getString("pais") ?: "",
                            precio = document.getDouble("precio") ?: 0.0,
                            descripcion = document.getString("descripcion") ?: "",
                            imagenUrl = document.getString("imagenUrl") ?: ""
                        )

                        destinos.add(destino)
                    }

                    adapter.notifyDataSetChanged()
                }

                progressBarEditar.visibility = View.GONE
            }
    }

    private fun mostrarDialogoEliminar(destino: Destino) {

        AlertDialog.Builder(this)
            .setTitle(R.string.confirmar_eliminacion)
            .setMessage(
                getString(
                    R.string.mensaje_eliminar_destino,
                    destino.nombre
                )
            )
            .setNegativeButton(R.string.cancelar, null)
            .setPositiveButton(R.string.eliminar) { _, _ ->

                eliminarDestino(destino)
            }
            .show()
    }

    private fun eliminarDestino(destino: Destino) {

        db.collection("destinos")
            .document(destino.id)
            .delete()
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    R.string.destino_eliminado,
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    R.string.error_eliminar_destino,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}