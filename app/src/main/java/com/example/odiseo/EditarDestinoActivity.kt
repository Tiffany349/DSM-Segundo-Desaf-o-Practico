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
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID
import java.util.concurrent.Executors

class EditarDestinoActivity : AppCompatActivity() {

    private lateinit var etEditarNombre: TextInputEditText
    private lateinit var actvEditarPais: AutoCompleteTextView
    private lateinit var etEditarPrecio: TextInputEditText
    private lateinit var etEditarDescripcion: TextInputEditText
    private lateinit var btnSeleccionarImagenEditar: Button
    private lateinit var ivImagenEditarDestino: ImageView
    private lateinit var tvImagenEditarSeleccionada: TextView
    private lateinit var btnGuardarCambios: Button
    private lateinit var btnVolverMenu: Button

    private val db = FirebaseFirestore.getInstance()

    private var destinoId: String = ""
    private var imagenActualUrl: String = ""
    private var imagenSeleccionadaUri: Uri? = null

    companion object {
        private const val REQUEST_CODE_IMAGEN = 300
        private const val REQUEST_CODE_PERMISO_IMAGENES = 400

        private const val CLOUDINARY_CLOUD_NAME = "yptipn1x"
        private const val CLOUDINARY_UPLOAD_PRESET = "odiseo_images"
    }

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

        btnSeleccionarImagenEditar = findViewById(
            R.id.btnSeleccionarImagenEditar
        )

        ivImagenEditarDestino = findViewById(
            R.id.ivImagenEditarDestino
        )

        tvImagenEditarSeleccionada = findViewById(
            R.id.tvImagenEditarSeleccionada
        )

        btnGuardarCambios = findViewById(
            R.id.btnGuardarCambios
        )

        btnVolverMenu = findViewById(
            R.id.btnVolverMenu
        )

        destinoId = intent.getStringExtra(
            "destinoId"
        ) ?: ""

        configurarPaises()

        cargarDestino()

        btnSeleccionarImagenEditar.setOnClickListener {
            verificarPermisoYSeleccionarImagen()
        }

        btnGuardarCambios.setOnClickListener {
            guardarCambios()
        }

        btnVolverMenu.setOnClickListener {
            volverAlMenu()
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

                    imagenActualUrl =
                        document.getString(
                            "imagenUrl"
                        ) ?: ""

                    if (imagenActualUrl.isNotEmpty()) {

                        Glide.with(this)
                            .load(imagenActualUrl)
                            .centerCrop()
                            .into(ivImagenEditarDestino)

                        ivImagenEditarDestino.visibility =
                            ImageView.VISIBLE
                    }

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
            requestCode ==
            REQUEST_CODE_PERMISO_IMAGENES
        ) {

            if (
                grantResults.isNotEmpty() &&
                grantResults[0] ==
                PackageManager.PERMISSION_GRANTED
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

            ivImagenEditarDestino.setImageURI(
                imagenSeleccionadaUri
            )

            ivImagenEditarDestino.visibility =
                ImageView.VISIBLE

            tvImagenEditarSeleccionada.text =
                getString(
                    R.string.nueva_imagen_seleccionada
                )
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

        btnGuardarCambios.isEnabled = false
        btnSeleccionarImagenEditar.isEnabled = false

        if (imagenSeleccionadaUri != null) {

            Toast.makeText(
                this,
                R.string.subiendo_imagen,
                Toast.LENGTH_SHORT
            ).show()

            subirImagenCloudinary()

        } else {

            actualizarDestinoFirestore(
                imagenActualUrl
            )
        }
    }

    private fun subirImagenCloudinary() {

        val uri =
            imagenSeleccionadaUri ?: return

        val executor =
            Executors.newSingleThreadExecutor()

        executor.execute {

            try {

                val url = URL(
                    "https://api.cloudinary.com/v1_1/" +
                            "$CLOUDINARY_CLOUD_NAME/image/upload"
                )

                val boundary =
                    "----OdiseoBoundary${UUID.randomUUID()}"

                val connection =
                    url.openConnection()
                            as HttpURLConnection

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

                if (
                    responseCode in 200..299
                ) {

                    val response =
                        connection.inputStream
                            .bufferedReader()
                            .use {
                                it.readText()
                            }

                    val imageUrl =
                        obtenerSecureUrl(
                            response
                        )

                    runOnUiThread {

                        actualizarDestinoFirestore(
                            imageUrl
                        )
                    }

                } else {

                    runOnUiThread {

                        btnGuardarCambios.isEnabled =
                            true

                        btnSeleccionarImagenEditar.isEnabled =
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

                    btnGuardarCambios.isEnabled =
                        true

                    btnSeleccionarImagenEditar.isEnabled =
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

    private fun actualizarDestinoFirestore(
        imageUrl: String
    ) {

        val nombre =
            etEditarNombre.text.toString().trim()

        val pais =
            actvEditarPais.text.toString().trim()

        val precio =
            etEditarPrecio.text.toString()
                .trim()
                .toDouble()

        val descripcion =
            etEditarDescripcion.text.toString().trim()

        val cambios =
            hashMapOf<String, Any>(
                "nombre" to nombre,
                "pais" to pais,
                "precio" to precio,
                "descripcion" to descripcion,
                "imagenUrl" to imageUrl
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

                btnGuardarCambios.isEnabled =
                    true

                btnSeleccionarImagenEditar.isEnabled =
                    true

                Toast.makeText(
                    this,
                    R.string.error_guardar_cambios,
                    Toast.LENGTH_LONG
                ).show()
            }
    }

    private fun volverAlMenu() {

        val intent = Intent(
            this,
            InicioActivity::class.java
        )

        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP

        startActivity(intent)
        finish()
    }
}