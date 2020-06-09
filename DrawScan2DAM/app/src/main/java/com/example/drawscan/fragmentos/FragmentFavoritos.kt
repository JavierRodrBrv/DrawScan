package com.example.drawscan.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drawscan.R
import com.example.drawscan.actividad.ActividadResultadoDetallado
import com.example.drawscan.actividad.AdaptadorListView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.databinding.FragmentFavoritosBinding
import com.example.drawscan.globales.ListaDatos
import com.example.drawscan.modalview.ViewModelCamara

/**
 * A simple [Fragment] subclass.
 */
class FragmentFavoritos : Fragment() {

    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var listaFav: ListView
    private lateinit var adaptador: AdaptadorListView
    private var binding: FragmentFavoritosBinding? = null
    private lateinit var barraDeBusqueda: SearchView
    private val bindingObtener get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        val view = binding!!.root
        adaptador = AdaptadorListView(
            context!!,
            ListaDatos.listaDatos
        )

        binding!!.idListaFavoritos.adapter = adaptador
        barraDeBusqueda = view.findViewById(R.id.idBusquedaFavoritos) as SearchView
        barraDeBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val lista = arrayListOf<DatosCamara>()
                for (datoCamara in ListaDatos.listaDatos) {
                    if (datoCamara.tituloImagen.contains(newText.toString())) {
                        lista.add(datoCamara)
                    }
                }
                adaptador.setLista(lista)
                adaptador.notifyDataSetChanged()
                return false
            }
        })

        adaptador.setListener(object : AdaptadorListView.ModificarLista {
            override fun agregarFav(lista: ArrayList<DatosCamara>) {
                camaraLiveData.setListaHistorial(camaraLiveData.getListaHistorial().value!!)
            }

            override fun eliminarElemento(posicion: Int) {
                mostrarResultadoDetallado(ListaDatos.listaDatos.get(posicion), posicion)

            }
        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        camaraLiveData = ViewModelProvider(requireActivity()).get(ViewModelCamara::class.java)

    }

    fun mostrarResultadoDetallado(datosCamara: DatosCamara, posicion: Int) {
        val intent = Intent(context, ActividadResultadoDetallado::class.java)
        val b: Bundle = Bundle()
        b.putString("tituloFoto", datosCamara.tituloImagen)
        b.putString("porcentajeFoto", datosCamara.porcentaje.toString() + " %")
        b.putString("fechaFoto", datosCamara.dias)
        b.putInt("posicion", posicion)
        intent.putExtras(b)
        startActivity(intent)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        camaraLiveData.getListaFavoritos().observe(viewLifecycleOwner,
            Observer<ArrayList<DatosCamara>> { listaFav -> //Cada vez que se realiza un cambio en la lista, el adaptador se cambia solo.
                adaptador.setLista(listaFav!!)
                adaptador.notifyDataSetChanged()
            })
    }


}
