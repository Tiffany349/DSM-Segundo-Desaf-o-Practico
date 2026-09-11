package com.example.odiseo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AgregarDestinoActivity : AppCompatActivity() {

    private lateinit var etNombreDestino: TextInputEditText
    private lateinit var actvPaisDestino: AutoCompleteTextView
    private lateinit var etPrecioDestino: TextInputEditText
    private lateinit var etDescripcionDestino: TextInputEditText
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var tvImagenSeleccionada: TextView
    private lateinit var btnGuardarDestino: Button

    private var imagenSeleccionadaUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_IMAGEN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_destino)

        etNombreDestino = findViewById(R.id.etNombreDestino)
        actvPaisDestino = findViewById(R.id.actvPaisDestino)
        etPrecioDestino = findViewById(R.id.etPrecioDestino)
        etDescripcionDestino = findViewById(R.id.etDescripcionDestino)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada)
        btnGuardarDestino = findViewById(R.id.btnGuardarDestino)

        configurarPaises()

        btnSeleccionarImagen.setOnClickListener {
            seleccionarImagen()
        }

        btnGuardarDestino.setOnClickListener {
            validarDestino()
        }
    }

    private fun configurarPaises() {

        val paises = arrayOf(
            "El Salvador",
            "Guatemala",
            "Honduras",
            "Nicaragua",
            "Costa Rica",
            "Panamá",
            "México",
            "Colombia",
            "España",
            "Estados Unidos"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            paises
        )

        actvPaisDestino.setAdapter(adapter)
    }

    private fun seleccionarImagen() {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        startActivityForResult(
            intent,
            REQUEST_CODE_IMAGEN
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (
            requestCode == REQUEST_CODE_IMAGEN &&
            resultCode == Activity.RESULT_OK &&
            data?.data != null
        ) {

            imagenSeleccionadaUri = data.data

            tvImagenSeleccionada.text =
                imagenSeleccionadaUri.toString()
        }
    }

    private fun validarDestino() {

        val nombre = etNombreDestino.text.toString().trim()
        val pais = actvPaisDestino.text.toString().trim()
        val precioTexto = etPrecioDestino.text.toString().trim()
        val descripcion = etDescripcionDestino.text.toString().trim()

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

        val precio = precioTexto.toDoubleOrNull()

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

        if (imagenSeleccionadaUri == null) {

            Toast.makeText(
                this,
                R.string.error_imagen_destino,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        Toast.makeText(
            this,
            R.string.destino_validado,
            Toast.LENGTH_SHORT
        ).show()
    }
}