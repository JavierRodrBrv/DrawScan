package com.example.drawscan

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.TextView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.fragmentos.PantallaFragments
import com.like.LikeButton
import java.util.*

class AdaptadorListView(context: Context, resource: Int, lista: MutableList<DatosCamara>) :
    ArrayAdapter<DatosCamara>(context, 0, lista), Filterable {


    private lateinit var pantallaFragments: PantallaFragments//Actividad donde estan los fragments
    private lateinit var botonFavorito: LikeButton//Boton que agrega el elemento en favorito.
    private lateinit var textoTituloFoto: TextView//Textview con el titulo de la foto.
    private lateinit var textoFecha: TextView//Textview con la fecha.
    private lateinit var porcentajeFoto: TextView//Textview donde muestra el porcentaje de similitud.

    private val lista: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados
    private var fullLista: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados, se usa para el filtro de busqueda.


    init {
        fullLista = ArrayList<DatosCamara>(lista)
    }


}