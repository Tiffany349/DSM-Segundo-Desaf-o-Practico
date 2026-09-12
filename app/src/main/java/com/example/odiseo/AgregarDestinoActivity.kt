package com.example.odiseo

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText

class AgregarDestinoActivity : AppCompatActivity() {

    private lateinit var etNombreDestino: TextInputEditText
    private lateinit var actvPaisDestino: AutoCompleteTextView
    private lateinit var etPrecioDestino: TextInputEditText
    private lateinit var etDescripcionDestino: TextInputEditText
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var ivImagenDestino: ImageView
    private lateinit var tvImagenSeleccionada: TextView
    private lateinit var btnGuardarDestino: Button

    private var imagenSeleccionadaUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_IMAGEN = 100
        private const val REQUEST_CODE_PERMISO_IMAGENES = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_agregar_destino)

        etNombreDestino = findViewById(R.id.etNombreDestino)
        actvPaisDestino = findViewById(R.id.actvPaisDestino)
        etPrecioDestino = findViewById(R.id.etPrecioDestino)
        etDescripcionDestino = findViewById(R.id.etDescripcionDestino)
        btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen)
        ivImagenDestino = findViewById(R.id.ivImagenDestino)
        tvImagenSeleccionada = findViewById(R.id.tvImagenSeleccionada)
        btnGuardarDestino = findViewById(R.id.btnGuardarDestino)

        configurarPaises()

        btnSeleccionarImagen.setOnClickListener {
            verificarPermisoYSeleccionarImagen()
        }

        btnGuardarDestino.setOnClickListener {
            validarDestino()
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

        actvPaisDestino.setAdapter(adapter)
    }

    private fun verificarPermisoYSeleccionarImagen() {

        val permisoNecesario = if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        val permisoConcedido =
            ContextCompat.checkSelfPermission(
                this,
                permisoNecesario
            ) == PackageManager.PERMISSION_GRANTED

        if (permisoConcedido) {

            abrirGaleria()

        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(permisoNecesario),
                REQUEST_CODE_PERMISO_IMAGENES
            )
        }
    }

    private fun abrirGaleria() {

        val intent = Intent(
            Intent.ACTION_OPEN_DOCUMENT
        )

        intent.type = "image/*"

        intent.addCategory(
            Intent.CATEGORY_OPENABLE
        )

        startActivityForResult(
            intent,
            REQUEST_CODE_IMAGEN
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (
            requestCode == REQUEST_CODE_PERMISO_IMAGENES
        ) {

            if (
                grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {

                abrirGaleria()

            } else {

                Toast.makeText(
                    this,
                    R.string.permiso_imagenes_denegado,
                    Toast.LENGTH_LONG
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (
            requestCode == REQUEST_CODE_IMAGEN &&
            resultCode == Activity.RESULT_OK &&
            data?.data != null
        ) {

            imagenSeleccionadaUri = data.data

            ivImagenDestino.setImageURI(
                imagenSeleccionadaUri
            )

            ivImagenDestino.visibility =
                ImageView.VISIBLE

            tvImagenSeleccionada.text =
                getString(
                    R.string.imagen_seleccionada
                )
        }
    }

    private fun validarDestino() {

        val nombre =
            etNombreDestino.text.toString().trim()

        val pais =
            actvPaisDestino.text.toString().trim()

        val precioTexto =
            etPrecioDestino.text.toString().trim()

        val descripcion =
            etDescripcionDestino.text.toString().trim()

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

        if (
            precio == null ||
            precio <= 0
        ) {

            Toast.makeText(
                this,
                R.string.error_precio_destino,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (
            descripcion.length < 20
        ) {

            Toast.makeText(
                this,
                R.string.error_descripcion_destino,
                Toast.LENGTH_SHORT
            ).show()

            return
        }

        if (
            imagenSeleccionadaUri == null
        ) {

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