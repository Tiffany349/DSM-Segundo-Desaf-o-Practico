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
import com.google.firebase.firestore.FirebaseFirestore
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import java.util.concurrent.Executors

class AgregarDestinoActivity : AppCompatActivity() {

    private lateinit var etNombreDestino: TextInputEditText
    private lateinit var actvPaisDestino: AutoCompleteTextView
    private lateinit var etPrecioDestino: TextInputEditText
    private lateinit var etDescripcionDestino: TextInputEditText
    private lateinit var btnSeleccionarImagen: Button
    private lateinit var ivImagenDestino: ImageView
    private lateinit var tvImagenSeleccionada: TextView
    private lateinit var btnGuardarDestino: Button

    private val db = FirebaseFirestore.getInstance()

    private var imagenSeleccionadaUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_IMAGEN = 100
        private const val REQUEST_CODE_PERMISO_IMAGENES = 200

        private const val CLOUDINARY_CLOUD_NAME = "yptipn1x"
        private const val CLOUDINARY_UPLOAD_PRESET = "odiseo_images"
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
            validarYGuardarDestino()
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
                ).show()
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

    private fun validarYGuardarDestino() {

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

        btnGuardarDestino.isEnabled = false

        Toast.makeText(
            this,
            R.string.subiendo_imagen,
            Toast.LENGTH_SHORT
        ).show()

        subirImagenCloudinary()
    }

    private fun subirImagenCloudinary() {

        val uri = imagenSeleccionadaUri ?: return

        val executor = Executors.newSingleThreadExecutor()

        executor.execute {

            try {

                val url = URL(
                    "https://api.cloudinary.com/v1_1/" +
                            "$CLOUDINARY_CLOUD_NAME/image/upload"
                )

                val boundary =
                    "----OdiseoBoundary${UUID.randomUUID()}"

                val connection =
                    url.openConnection() as HttpURLConnection

                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.doInput = true

                connection.setRequestProperty(
                    "Content-Type",
                    "multipart/form-data; boundary=$boundary"
                )

                val outputStream =
                    DataOutputStream(
                        connection.outputStream
                    )

                outputStream.writeBytes(
                    "--$boundary\r\n"
                )

                outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"upload_preset\"\r\n\r\n"
                )

                outputStream.writeBytes(
                    "$CLOUDINARY_UPLOAD_PRESET\r\n"
                )

                outputStream.writeBytes(
                    "--$boundary\r\n"
                )

                outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"file\"; filename=\"odiseo_image.jpg\"\r\n"
                )

                outputStream.writeBytes(
                    "Content-Type: image/jpeg\r\n\r\n"
                )

                val inputStream =
                    contentResolver.openInputStream(uri)

                if (inputStream == null) {
                    throw Exception(
                        "No se pudo leer la imagen"
                    )
                }

                inputStream.use { input ->

                    val buffer =
                        ByteArray(4096)

                    var bytesRead: Int

                    while (
                        input.read(buffer).also {
                            bytesRead = it
                        } != -1
                    ) {

                        outputStream.write(
                            buffer,
                            0,
                            bytesRead
                        )
                    }
                }

                outputStream.writeBytes(
                    "\r\n"
                )

                outputStream.writeBytes(
                    "--$boundary--\r\n"
                )

                outputStream.flush()
                outputStream.close()

                val responseCode =
                    connection.responseCode

                if (responseCode in 200..299) {

                    val response =
                        connection.inputStream
                            .bufferedReader()
                            .use {
                                it.readText()
                            }

                    val imageUrl =
                        obtenerSecureUrl(response)

                    runOnUiThread {
                        guardarDestinoFirestore(
                            imageUrl
                        )
                    }

                } else {

                    runOnUiThread {

                        btnGuardarDestino.isEnabled =
                            true

                        Toast.makeText(
                            this,
                            R.string.error_subir_imagen,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                connection.disconnect()

            } catch (e: Exception) {

                runOnUiThread {

                    btnGuardarDestino.isEnabled =
                        true

                    Toast.makeText(
                        this,
                        R.string.error_subir_imagen,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun obtenerSecureUrl(
        response: String
    ): String {

        val marcador =
            "\"secure_url\":\""

        val inicio =
            response.indexOf(marcador)

        if (inicio == -1) {
            throw Exception(
                "No se encontró la URL de Cloudinary"
            )
        }

        val inicioUrl =
            inicio + marcador.length

        val fin =
            response.indexOf(
                "\"",
                inicioUrl
            )

        if (fin == -1) {
            throw Exception(
                "No se encontró el final de la URL"
            )
        }

        return response.substring(
            inicioUrl,
            fin
        )
    }

    private fun guardarDestinoFirestore(
        imageUrl: String
    ) {

        val nombre =
            etNombreDestino.text.toString().trim()

        val pais =
            actvPaisDestino.text.toString().trim()

        val precio =
            etPrecioDestino.text.toString()
                .trim()
                .toDouble()

        val descripcion =
            etDescripcionDestino.text.toString().trim()

        val destino = hashMapOf(
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precio,
            "descripcion" to descripcion,
            "imagenUrl" to imageUrl
        )

        db.collection("destinos")
            .add(destino)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    R.string.destino_guardado,
                    Toast.LENGTH_LONG
                ).show()

                limpiarFormulario()
            }
            .addOnFailureListener {

                btnGuardarDestino.isEnabled =
                    true

                Toast.makeText(
                    this,
                    R.string.error_guardar_destino,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun limpiarFormulario() {

        etNombreDestino.text?.clear()

        actvPaisDestino.setText(
            "",
            false
        )

        etPrecioDestino.text?.clear()

        etDescripcionDestino.text?.clear()

        imagenSeleccionadaUri = null

        ivImagenDestino.setImageDrawable(
            null
        )

        ivImagenDestino.visibility =
            ImageView.GONE

        tvImagenSeleccionada.text =
            getString(
                R.string.ninguna_imagen
            )

        btnGuardarDestino.isEnabled =
            true
    }
}