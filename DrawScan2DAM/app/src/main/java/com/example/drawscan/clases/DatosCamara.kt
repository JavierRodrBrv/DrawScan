package com.example.drawscan.clases

import java.text.SimpleDateFormat
import java.util.*

class DatosCamara(var tituloImagen: String, var porcentaje: Int?) {
    var dias: String
    var favorito: Boolean? = null

    init {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy")
        this.dias = df.format(c)
        this.favorito = false
    }

}