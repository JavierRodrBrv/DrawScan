package com.example.drawscan.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import com.example.drawscan.actividad.AdaptadorListView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.databinding.FragmentFavoritosBinding
import com.example.drawscan.globales.ListaDatos

/**
 * A simple [Fragment] subclass.
 */
class FragmentFavoritos : Fragment() {
    private lateinit var listaFav: ListView
    private lateinit var adaptador: AdaptadorListView
    private var binding: FragmentFavoritosBinding? = null
    private val bindingObtener get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFavoritosBinding.inflate(inflater,container,false)
        val view =binding!!.root
        adaptador = AdaptadorListView(
            context!!,
            ListaDatos.listaDatos
        )
        adaptador.setListener(object :AdaptadorListView.ModificarLista{
            override fun agregarFav(lista: ArrayList<DatosCamara>) {

                var listaFav= arrayListOf<DatosCamara>()
                for (datos in lista){
                    if(datos.favorito){
                        listaFav.add(datos)
                    }
                }
                ListaDatos.setListaDatosFav(listaFav)
                ListaDatos.setLista(lista)
            }
        })
        ListaDatos.setListener(object : ListaDatos.ListaModificada{
            override fun listaFavoritoModificado(lista: ArrayList<DatosCamara>) {
                adaptador.setLista(lista)
                adaptador.notifyDataSetChanged()
            }

            override fun listaDatosModificado(listaModificada: ArrayList<DatosCamara>) {

            }
        })

        binding!!.idListaFavoritos.adapter=adaptador
        return view
    }


}
