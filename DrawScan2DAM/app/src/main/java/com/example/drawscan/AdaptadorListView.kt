package com.example.drawscan

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.clases.SharedPref
import com.example.drawscan.fragmentos.PantallaFragments
import com.sackcentury.shinebuttonlib.ShineButton

class AdaptadorListView(contexto: Context, lista: ArrayList<DatosCamara>) :
    ArrayAdapter<DatosCamara>(contexto, 0, lista), Filterable {

    private var contextoAplicacion = contexto
    private lateinit var pantallaFragments: PantallaFragments//Actividad donde estan los fragments
    private lateinit var textoTituloFoto: TextView//Textview con el titulo de la foto.
    private lateinit var textoFecha: TextView//Textview con la fecha.
    private lateinit var porcentajeFoto: TextView//Textview donde muestra el porcentaje de similitud.
    private lateinit var fotoImagen: ImageView // Esta es la imagen realizada por la camara.
    private lateinit var main: MainActivity // Esta es la actividad inicial.
    private var listaDatos: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados
    private var fullLista: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados, se usa para el filtro de busqueda.
    private lateinit var sharedPreferences: SharedPref
    private lateinit var botonFavorito:ShineButton // Este es el boton fav


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

        sharedPreferences = SharedPref(contextoAplicacion)

        val inflater = (contextoAplicacion as Activity).layoutInflater
        val vistaElemento = inflater.inflate(R.layout.elemento_lista, null)
        main = MainActivity()
        textoTituloFoto = vistaElemento!!.findViewById(R.id.idTituloFoto) as TextView
        textoFecha = vistaElemento.findViewById(R.id.idFecha) as TextView
        porcentajeFoto = vistaElemento.findViewById(R.id.idPorcentaje) as TextView
        fotoImagen=vistaElemento.findViewById(R.id.idImagenFoto) as ImageView
        botonFavorito=vistaElemento.findViewById(R.id.botonFavorito) as ShineButton


        textoTituloFoto.setText(listaDatos!!.get(position).tituloImagen)
        porcentajeFoto.setText(contextoAplicacion.resources.getString(R.string.porcentajeExacto)+listaDatos!!.get(position).porcentaje.toString())
        textoFecha.setText(contextoAplicacion.resources.getString(R.string.fechaConcreta)+listaDatos!!.get(position).dias)
        //Para rellenar con la foto de la referencia en el imageView de elementoLista
        Glide.with(vistaElemento.context).load(listaDatos!!.get(position).fotoReferencia).into(fotoImagen)

        if (sharedPreferences.loadNightModeState()) {
            textoFecha.setTextAppearance(R.style.estiloTextoModoOscuro)
            porcentajeFoto.setTextAppearance(R.style.estiloTextoModoOscuro)
            textoTituloFoto.setTextAppearance(R.style.estiloTextoModoOscuro)

        } else {
            textoFecha.setTextAppearance(R.style.estiloTextoNegro)
            porcentajeFoto.setTextAppearance(R.style.estiloTextoNegro)
            textoTituloFoto.setTextAppearance(R.style.estiloTextoNegro)
        }

        sharedPreferences.setModoNocheListener(object : SharedPref.ModoNoche {
            override fun cambiarModoNoche() {
                if (sharedPreferences.loadNightModeState()) {
                    textoFecha.setTextAppearance(R.style.estiloTextoModoOscuro)
                    porcentajeFoto.setTextAppearance(R.style.estiloTextoModoOscuro)
                    textoTituloFoto.setTextAppearance(R.style.estiloTextoModoOscuro)

                } else {
                    textoFecha.setTextAppearance(R.style.estiloTextoNegro)
                    porcentajeFoto.setTextAppearance(R.style.estiloTextoNegro)
                    textoTituloFoto.setTextAppearance(R.style.estiloTextoNegro)
                }
            }
        })


        return vistaElemento
    }


    override fun getFilter(): Filter {
        return super.getFilter()
    }

    private val filertBueno: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val results = FilterResults()
            val lista2: ArrayList<DatosCamara> = arrayListOf()
            if (constraint == null || constraint.length == 0) {
                lista2.addAll(fullLista!!)
            } else {
                val filter = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (date in lista) {
                    if (date.tituloImagen.toLowerCase().contains(filter)) {
                        lista2.add(date)
                    }
                }
                results.values = lista2
                return results
            }
            results.values = lista2
            return results
        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            lista.clear()
            lista.addAll(results.values as ArrayList<DatosCamara>)
            notifyDataSetChanged()
        }
    }


    /**
     * Función que cuenta el nº de elementos que contiene el listview
     * @return Nº de elementos del listview
     */
    override fun getCount(): Int {
        return listaDatos!!.size
    }

    /**
     * Función que devuelve el id del elemento del listview
     * @param i Posicion del elemento
     * @return 0
     */
    override fun getItemId(i: Int): Long {
        return 0
    }

}