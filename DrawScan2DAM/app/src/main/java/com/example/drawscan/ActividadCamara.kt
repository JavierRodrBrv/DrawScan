package com.example.drawscan

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.drawscan.globales.Imagenes
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.lang.Math.abs

class ActividadCamara : AppCompatActivity() {
    private lateinit var bundle: Bundle //Este bundle es para devolver los datos a la otra actividad.
    private var permisosCamara: Array<String> = arrayOf() // Permisos necesarios para la cámara
    private var uri_imagen: Uri? = null // Uri de la imagen
    private lateinit var imagenCamaraAux: ImageView
    private val camaraIDPermision = 300 // Código para los permisos de la cámara
    private val cogerImagenCamaraID = 300 // Código para los permisos de recortar el imagen
    private lateinit var barraProgreso: ProgressBar // La barra de progreso
    private var capturaImagen2 = false

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

        activarCamara()
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
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                ) ||
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {

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


    /**
     * Devuelve el resultado del intent
     * @param requestCode Código del permiso que hemos solicitado
     * @param resultCode Código del resultado de la actividad
     * @param data No nos hace falta para este caso
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // Vamos a recibir la imagen de la cámara
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == cogerImagenCamaraID) {
                // Cuando cogemos la imagen de la cámara, le hacemos crop a la imagen
                CropImage.activity(uri_imagen)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val imagenCrop = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val imagenCropUri = imagenCrop.uri // Conseguir la uri de la imagen cropeada
                // Ponemos esa imagen cropeada en ImageView
                imagenCamaraAux.setImageURI(imagenCropUri)

                //Para el reconocimineto de imagen, necesitamos convertirlo en una imagen drawable Bitmap
                val bmd: BitmapDrawable = imagenCamaraAux.drawable as BitmapDrawable

                if(!capturaImagen2){
                    Imagenes.imagen1=bmd.bitmap
                    capturaImagen2=true
                }else{
                    Imagenes.imagen2=bmd.bitmap
                    capturaImagen2=false
                }

                if(Imagenes.imagen1!=null && Imagenes.imagen2!=null){

                    val img1 = Imagenes.imagen1
                    val img2 = Imagenes.imagen2
                    var p:Double? = getDifferencePercent(img1!!, img2!!)

                    if(p != null){
                        p=100-p
                        val numeroRedondeado=Math.round((p)*100.00)/100.00
                        Toast.makeText(this,numeroRedondeado.toString()+"%",Toast.LENGTH_LONG).show()
                        println("${img1.width},${img1.height} ${img2.width},${img2.height}")
                    }

                    Imagenes.imagen1=null
                    Imagenes.imagen2=null
                    finish()
                }else{
                    Toast.makeText(this,"Vamos a por la segunda foto",Toast.LENGTH_LONG).show()
                    activarCamara()
                }


            }else{

                // Esto se ejecuta en el caso de que en la opcion de recortar la imagen, el usuario quiere volver a la camara
                finish()
                val bundleParaReiniciarActividad = Bundle()
                val reiniciarActividad = Intent(this, ActividadCamara::class.java)
                reiniciarActividad.putExtras(bundleParaReiniciarActividad)
                startActivity(reiniciarActividad)
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * Esta función es la que calcula las dos imagenes.
     */
    fun getDifferencePercent(img1: Bitmap, img2: Bitmap): Double? {


        val nuevaDimension=getResizedBitmap(img1,768,768)
        val nuevaDimension2=getResizedBitmap(img2,768,768)
        println("${nuevaDimension!!.width},${nuevaDimension.height} ${nuevaDimension2!!.width},${nuevaDimension2.height}")


        val width = nuevaDimension!!.width
        val height = nuevaDimension.height
        val width2 = nuevaDimension2!!.width
        val height2 = nuevaDimension2.height
        if (width != width2 || height != height2) {
            val f = "(%d,%d) vs. (%d,%d)".format(width, height, width2, height2)
            Toast.makeText(this,"Las dimensiones de las imagenes tienen que ser iguales $f",Toast.LENGTH_LONG).show()
            println("Las dimensiones de las imagenes tienen que ser iguales $f")
        }else{
            var diff = 0L

            for (y in 0 until height) {
                for (x in 0 until width) {

                    diff += pixelDiff(nuevaDimension.getPixel(x,y),nuevaDimension2.getPixel(x,y))
                }
            }
            val maxDiff = 3L * 255 * width * height
            return 100.0 * diff / maxDiff
        }
        return null
    }

    fun getResizedBitmap(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)

        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }


    /**
     * Función que clasifica los pixeles con sus respectivos colores.
     */
    fun pixelDiff(rgb1: Int, rgb2: Int): Int {
        val r1 = (rgb1 shr 16) and 0xff
        val g1 = (rgb1 shr 8)  and 0xff
        val b1 =  rgb1         and 0xff
        val r2 = (rgb2 shr 16) and 0xff
        val g2 = (rgb2 shr 8)  and 0xff
        val b2 =  rgb2         and 0xff
        return abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2)
    }


}
