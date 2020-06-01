package com.example.drawscan.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

                camaraLiveData.setListaHistorial(camaraLiveData.getListaHistorial().value!!)

            }
        })

        binding!!.idListaFavoritos.adapter=adaptador
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        camaraLiveData = ViewModelProvider(requireActivity()).get(ViewModelCamara::class.java)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        camaraLiveData.getListaFavoritos().observe(viewLifecycleOwner, object :
            Observer<ArrayList<DatosCamara>> {
            override fun onChanged(listaFav: ArrayList<DatosCamara>?) {
                //Cada vez que se realiza un cambio en la lista, el adaptador se cambia solo.
                adaptador.setLista(listaFav!!)
                adaptador.notifyDataSetChanged()


            }

        })
    }


}
