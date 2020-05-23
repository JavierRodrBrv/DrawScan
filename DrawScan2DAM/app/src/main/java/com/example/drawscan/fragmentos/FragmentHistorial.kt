package com.example.drawscan.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drawscan.R
import com.example.drawscan.actividad.AdaptadorListView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.clases.InicializarInterfaz
import com.example.drawscan.databinding.FragmentHistorialBinding
import com.example.drawscan.globales.ListaDatos
import com.example.drawscan.modalview.ViewModelCamara

/**
 * A simple [Fragment] subclass.
 */
class FragmentHistorial : Fragment() {

    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var adaptador: AdaptadorListView
    private lateinit var listaHistorial: ListView
    private var binding: FragmentHistorialBinding? = null
    private val bindingObtener get() = binding!!
    private lateinit var barraDeBusqueda:SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHistorialBinding.inflate(inflater,container,false)
        val view =binding!!.root
        adaptador = AdaptadorListView(
            context!!,
            ListaDatos.listaDatos
        )
        binding!!.idListaHistorial.adapter=adaptador
        barraDeBusqueda=view.findViewById(R.id.idBusquedaHistorial) as SearchView

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        camaraLiveData = ViewModelProvider(this).get(ViewModelCamara::class.java)
        InicializarInterfaz.setListener(object : InicializarInterfaz.InsertarDatosEnModelView {
            override fun getLista(listaRellenar: ArrayList<DatosCamara>) {
                camaraLiveData.setCamaraLiveData(listaRellenar)
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        camaraLiveData.getCamaraLiveDara()
            .observe(viewLifecycleOwner, object : Observer<ArrayList<DatosCamara>> {
                override fun onChanged(t: ArrayList<DatosCamara>?) {
                    //Cada vez que se realiza un cambio en la lista, el adaptador se cambia solo.
                    adaptador.notifyDataSetChanged()

                }

            })
    }


}
