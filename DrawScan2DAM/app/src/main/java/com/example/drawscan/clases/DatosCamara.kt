package com.example.drawscan.clases

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.*

class DatosCamara(var tituloImagen: String, var porcentaje: Double?,var fotoReferencia:Uri) {
    var dias: String
    var favorito: Boolean? = null

    init {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MM-yyyy '-' HH:mm")
        this.dias = df.format(c)
        this.favorito = false
    }

}