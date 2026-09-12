package com.example.odiseo

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class EditarDestinoActivity : AppCompatActivity() {

    private lateinit var etEditarNombre: TextInputEditText
    private lateinit var actvEditarPais: AutoCompleteTextView
    private lateinit var etEditarPrecio: TextInputEditText
    private lateinit var etEditarDescripcion: TextInputEditText
    private lateinit var btnGuardarCambios: Button

    private val db = FirebaseFirestore.getInstance()

    private var destinoId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_editar_destino
        )

        etEditarNombre = findViewById(
            R.id.etEditarNombre
        )

        actvEditarPais = findViewById(
            R.id.actvEditarPais
        )

        etEditarPrecio = findViewById(
            R.id.etEditarPrecio
        )

        etEditarDescripcion = findViewById(
            R.id.etEditarDescripcion
        )

        btnGuardarCambios = findViewById(
            R.id.btnGuardarCambios
        )

        destinoId = intent.getStringExtra(
            "destinoId"
        ) ?: ""

        configurarPaises()

        cargarDestino()

        btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }
    }

    private fun configurarPaises() {

        val paises = resources.getStringArray(
            R.array.paises_destino
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            paises
        )

        actvEditarPais.setAdapter(adapter)
    }

    private fun cargarDestino() {

        if (destinoId.isEmpty()) {
            Toast.makeText(
                this,
                R.string.error_cargar_destino,
                Toast.LENGTH_LONG
            ).show()

            finish()
            return
        }

        db.collection("destinos")
            .document(destinoId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {

                    etEditarNombre.setText(
                        document.getString("nombre")
                            ?: ""
                    )

                    actvEditarPais.setText(
                        document.getString("pais")
                            ?: "",
                        false
                    )

                    val precio =
                        document.getDouble("precio")
                            ?: 0.0

                    etEditarPrecio.setText(
                        precio.toString()
                    )

                    etEditarDescripcion.setText(
                        document.getString(
                            "descripcion"
                        ) ?: ""
                    )

                } else {

                    Toast.makeText(
                        this,
                        R.string.error_cargar_destino,
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                }
            }
            .addOnFailureListener {

                Toast.makeText(
                    this,
                    R.string.error_cargar_destino,
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
    }

    private fun guardarCambios() {

        val nombre =
            etEditarNombre.text.toString().trim()

        val pais =
            actvEditarPais.text.toString().trim()

        val precioTexto =
            etEditarPrecio.text.toString().trim()

        val descripcion =
            etEditarDescripcion.text.toString().trim()

        if (
            nombre.isEmpty() ||
            pais.isEmpty() ||
            precioTexto.isEmpty() ||
            descripcion.isEmpty()
        ) {

            Toast.makeText(
                this,
                R.string.error_campos_destino,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        val precio =
            precioTexto.toDoubleOrNull()

        if (precio == null || precio <= 0) {

            Toast.makeText(
                this,
                R.string.error_precio_destino,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (descripcion.length < 20) {

            Toast.makeText(
                this,
                R.string.error_descripcion_destino,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        btnGuardarCambios.isEnabled = false

        val cambios = hashMapOf<String, Any>(
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precio,
            "descripcion" to descripcion
        )

        db.collection("destinos")
            .document(destinoId)
            .update(cambios)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    R.string.cambios_guardados,
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
            .addOnFailureListener {

                btnGuardarCambios.isEnabled = true

                Toast.makeText(
                    this,
                    R.string.error_guardar_cambios,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}