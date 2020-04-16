package com.example.drawscan

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class ActividadCamara : AppCompatActivity() {
    private lateinit var bundle: Bundle //Este bundle es para devolver los datos a la otra actividad.
    private var permisosCamara: Array<String> = arrayOf() // Permisos necesarios para la cámara
    private var uri_imagen: Uri? = null // Uri de la imagen
    private lateinit var imagenCamaraAux: ImageView
    private val camaraIDPermision = 300 // Código para los permisos de la cámara
    private val cogerImagenCamaraID = 300 // Código para los permisos de recortar el imagen
    private lateinit var barraProgreso: ProgressBar // La barra de progreso

    //Aqui viene las variables de firebase.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actividad_camara)
        permisosCamara = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        imagenCamaraAux = findViewById(R.id.idImagenAux)
        barraProgreso = findViewById(R.id.idProgressBar)
        //aqui va firebase..

    }

    /**
     * Función de tipo boolean que retorna si tenemos permisos para la camara y el almacenamiento
     * Retorna true si lo tenemos, false si no
     * Para conseguir una imagen de alta calidad, tendiramos que guardar la imagen al almacenamiento externo, para ello el requisito de su permision
     */
    fun comprobarPermisosCamara(): Boolean {
        val permisoCamara = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        val permisoAlmacentamiento = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        return permisoCamara && permisoAlmacentamiento
    }

    /**
     * Función que pide los permisos necesarios para llevar a cabo la actividad de la camara
     */
    fun pedirPermisosCamara() {
        ActivityCompat.requestPermissions(this, permisosCamara, camaraIDPermision)
    }

    /**
     * Funcion que realiza una función u otra dependiendo de las permisiones dadas por el usuario
     * @param requestCode Código del permiso que solicitamos
     * @param permissions Permisos que solicitamos
     * @param grantResults Resultado del diálogo de los permisos
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == camaraIDPermision) {
            if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    intentCamara()
                } else {
                    Toast.makeText(
                        this,
                        "Permiso DENEGADO. Por favor, proporcionanos con los permisos necesarios",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Función que dependiendo de los permisos dados, activa el intent de la camara o no
     */
    fun activarCamara() {
        if (!comprobarPermisosCamara()) {
            // Si no tenemos los permisos necesarios para llevar a cabo la actividad, se lo pedimos
            pedirPermisosCamara()
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            }
        } else {
            // Si tenemos los permisos necesarios para llevar a cabo la camara, hacemos un intent de la camara
            intentCamara()
        }
    }

    /**
     * Función que hace un intent para coger la imagen de la camara
     * También será guardada en el almacenamiento externo, para una imagen con mayor calidad
     */
    fun intentCamara() {
        val cv = ContentValues()
        cv.put(MediaStore.Images.Media.TITLE, "Imagen") // Título de la imagen
        cv.put(MediaStore.Images.Media.DESCRIPTION, "Imagen a Texto") // Descripción de la imagen
        uri_imagen = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv)
        val camara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camara.putExtra(MediaStore.EXTRA_OUTPUT, uri_imagen)
        startActivityForResult(camara, cogerImagenCamaraID)
    }

    //Hacer el activityOnResult, donde haremos el calculo del porcentaje.


    /**
     * Función que muestra un diálogo de error en el caso de que no encuentra la clase en el texto esacaneado
     */
    fun errorEscaneando() {
        val dialogo =
            AlertDialog.Builder(this)
        dialogo.setIcon(R.drawable.icono_alerta)
        dialogo.setTitle(resources.getString(R.string.tituloEscaner))
        dialogo.setMessage(resources.getString(R.string.mensajeEscaner))
        dialogo.setPositiveButton(
            resources.getString(R.string.botonOK)
        ) { dialog, which ->
            val paginaInicio =
                Intent(applicationContext, MainActivity::class.java)
            paginaInicio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(paginaInicio)
        }
        dialogo.show()
    }


}
