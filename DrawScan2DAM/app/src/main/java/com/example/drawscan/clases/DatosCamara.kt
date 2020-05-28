package com.example.drawscan.clases

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class DatosCamara(
    var tituloImagen: String,
    var porcentaje: Double?,
    var dias: String = SimpleDateFormat("dd-MM-yyyy '-' HH:mm").format(Calendar.getInstance().time),
    var favorito: Boolean = false
) : Serializable {

    constructor():this("",null)


}