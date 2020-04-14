package com.example.drawscan

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.fragmentos.PantallaFragments
import com.like.LikeButton

class AdaptadorListView(contexto: Context, resource: Int, lista: ArrayList<DatosCamara>) :
    ArrayAdapter<DatosCamara>(contexto, 0, lista), Filterable {

    private var contextoAplicacion = contexto
    private lateinit var pantallaFragments: PantallaFragments//Actividad donde estan los fragments
    private lateinit var botonFavorito: LikeButton//Boton que agrega el elemento en favorito.
    private lateinit var textoTituloFoto: TextView//Textview con el titulo de la foto.
    private lateinit var textoFecha: TextView//Textview con la fecha.
    private lateinit var porcentajeFoto: TextView//Textview donde muestra el porcentaje de similitud.
    private lateinit var fotoImagen: ImageView//
    private lateinit var main: MainActivity // Esta es la actividad inicial.

    private var listaDatos: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados
    private var fullLista: ArrayList<DatosCamara>? =
        null// ArrayList de datos escaneados, se usa para el filtro de busqueda.


    init {
        listaDatos = lista
        fullLista = ArrayList<DatosCamara>(lista)
    }

    /**
     * Devuelve el view de cada elemento del listview
     * @param i Posición del elemento
     * @param view view del elemento
     * @param viewGroup conjunto de view
     * @return view del elemento
     */
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val inflater = (contextoAplicacion as Activity).layoutInflater
        val vistaElemento = inflater.inflate(R.layout.elemento_lista, null)
        main = MainActivity()
        textoTituloFoto = view!!.findViewById(R.id.idTituloFoto) as TextView
        textoFecha = view.findViewById(R.id.idFecha) as TextView
        porcentajeFoto = view.findViewById(R.id.idPorcentaje) as TextView
        fotoImagen=view.findViewById(R.id.idImagenFoto) as ImageView
        botonFavorito=view.findViewById(R.id.botonFavorito) as LikeButton

        textoTituloFoto.setText(listaDatos!!.get(position).tituloImagen)
        textoFecha.setText(listaDatos!!.get(position).dias)



        return vistaElemento
    }
}