package com.example.drawscan.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drawscan.R
import com.example.drawscan.actividad.AdaptadorListView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.clases.InicializarInterfaz
import com.example.drawscan.clases.ListaDatosCamara
import com.example.drawscan.databinding.FragmentHistorialBinding
import com.example.drawscan.globales.ListaDatos
import com.example.drawscan.modalview.ViewModelCamara
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

/**
 * A simple [Fragment] subclass.
 */
class FragmentHistorial : Fragment() {

    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var adaptador: AdaptadorListView
    private var binding: FragmentHistorialBinding? = null
    private val bindingObtener get() = binding!!
    private lateinit var barraDeBusqueda:SearchView
    private val baseDeDatos by lazy { FirebaseFirestore.getInstance() }
    private val usuarioLogeado by lazy { FirebaseAuth.getInstance().currentUser }


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
        barraDeBusqueda.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adaptador.filter.filter(newText)
                return false
            }
        })
        adaptador.setListener(object : AdaptadorListView.ModificarLista{
            override fun agregarFav(lista: ArrayList<DatosCamara>) {
                var listaFav= arrayListOf<DatosCamara>()
                for (datos in lista){
                    if(datos.favorito){
                        listaFav.add(datos)
                    }
                }
                ListaDatos.setListaDatosFav(listaFav)

                adaptador.setLista(lista)
                adaptador.notifyDataSetChanged()
            }
        })
        ListaDatos.setListener(object :ListaDatos.ListaModificada{
            override fun listaFavoritoModificado(lista: ArrayList<DatosCamara>) {

                Toast.makeText(context,"Quitado",Toast.LENGTH_LONG).show()

            }

            override fun listaDatosModificado(listaModificada: ArrayList<DatosCamara>) {
                Toast.makeText(context,"Quitado",Toast.LENGTH_LONG).show()

                adaptador.setLista(listaModificada)
                adaptador.notifyDataSetChanged()
            }
        })
        getListaDB()
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

    fun getListaDB(){
        var listaDatosCamara:ListaDatosCamara?=ListaDatosCamara(arrayListOf())
        baseDeDatos.collection("usuarios")
            .document(usuarioLogeado!!.uid).get()
            .addOnCompleteListener(object : OnCompleteListener<DocumentSnapshot>{
                override fun onComplete(task: Task<DocumentSnapshot>) {
                    task.result!!.toObject(ListaDatosCamara::class.java)?.let {
                        listaDatosCamara=it
                    }
                    if(listaDatosCamara != null){
                        ListaDatos.listaDatos=listaDatosCamara!!.lista
                        adaptador.setLista(listaDatosCamara!!.lista)
                        adaptador.notifyDataSetChanged()
                    }else{
                        ListaDatos.listaDatos= arrayListOf()
                        adaptador.setLista(ListaDatos.listaDatos)
                        adaptador.notifyDataSetChanged()
                    }

                }

            })
    }


}
