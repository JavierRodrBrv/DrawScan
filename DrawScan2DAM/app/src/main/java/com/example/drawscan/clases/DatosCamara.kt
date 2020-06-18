package com.example.drawscan.clases

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Clase DatosCamara que modela los datos que va a recibir la comparación.
 * @author Javier Rodríguez
 * @constructor Este es el constructor principal de la clase.
 */
class DatosCamara(
    var tituloImagen: String, //Este es el titulo que va a recibir la comparación.
    var porcentaje: Double?, //Este es el porcentaje que recibe la comparación.
    var dias: String = SimpleDateFormat("dd-MM-yyyy '-' HH:mm").format(Calendar.getInstance().time), //La fecha en la que se ha realizado la comparación.
    var favorito: Boolean = false //Es el que va a decidir si un elemento es favorito o no. De predeterminado lo ponemos a false, ya que un nuevo elemento no va a crearse siendo favorito.
) : Serializable {
    constructor():this("",null)
}