package com.example.drawscan.fragmentos

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drawscan.R
import com.example.drawscan.actividad.ActividadResultadoDetallado
import com.example.drawscan.actividad.AdaptadorListView
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.clases.ListaDatosCamara
import com.example.drawscan.databinding.FragmentHistorialBinding
import com.example.drawscan.globales.ListaDatos
import com.example.drawscan.modalview.ViewModelCamara
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

/**
 * Clase que modela un fragment de FragmentHistorial
 * @author Javi Rodriguez
 */
class FragmentHistorial : Fragment() {
    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var adaptador: AdaptadorListView //adaptador que contiene la lista de la aplicacion.
    private var binding: FragmentHistorialBinding? = null //encuentra los elementos del layout de FragmentHistorial.
    private val bindingObtener get() = binding!!
    private lateinit var barraDeBusqueda:SearchView //Variable que contiene la barra de busqueda del layout.
    private val baseDeDatos by lazy { FirebaseFirestore.getInstance() } //Variable Firestore donde instanciamos la base de datos.
    private val usuarioLogeado by lazy { FirebaseAuth.getInstance().currentUser } //Variable FirebaseAuth donde almacenamos el usuario
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHistorialBinding.inflate(inflater,container,false)
        val view =binding!!.root
        adaptador = AdaptadorListView(
            context!!,
            ListaDatos.listaDatos,
            true
        )
        binding!!.idListaHistorial.adapter=adaptador
        barraDeBusqueda=view.findViewById(R.id.idBusquedaHistorial) as SearchView
        barraDeBusqueda.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            /**
             * Funcion que permite filtrar los elementos mediante a lo que se le introduzca en en campo de la barra de busqueda.
             * @param newText: de tipo string que se compara si existe en la lista.
             */
            override fun onQueryTextChange(newText: String?): Boolean {
                val lista= arrayListOf<DatosCamara>()
                for(datoCamara in ListaDatos.listaDatos){
                    if(datoCamara.tituloImagen.contains(newText.toString())){
                        lista.add(datoCamara)
                    }
                }
                adaptador.setLista(lista)
                adaptador.notifyDataSetChanged()
                return false
            }
        })
        adaptador.setListener(object : AdaptadorListView.ModificarLista{
            override fun agregarFav(lista: ArrayList<DatosCamara>) {//Establece la lista en el fragment historial
                camaraLiveData.setListaHistorial(lista)
            }

            override fun eliminarElemento(posicion: Int) {//Elimina el elemento de la lista.
                mostrarResultadoDetallado(ListaDatos.listaDatos.get(posicion),posicion)
            }
        })
        actualizacionAutomatica()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        camaraLiveData = ViewModelProvider(requireActivity()).get(ViewModelCamara::class.java)
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
        camaraLiveData.getListaHistorial().observe(viewLifecycleOwner,object :Observer<ArrayList<DatosCamara>>{
            override fun onChanged(lista: ArrayList<DatosCamara>?) {
                adaptador.setLista(lista!!)
                adaptador.notifyDataSetChanged()
                val listaDeFavoritos= arrayListOf<DatosCamara>()
                for (elemento in lista){
                    if(elemento.favorito){
                        listaDeFavoritos.add(elemento)
                    }
                }
                camaraLiveData.setListaFavoritos(listaDeFavoritos)
                actualizarLista()
            }
        })
    }

    /**
     * Funcion que actualiza la lista historial y la base de datos.
     */
    fun actualizarLista(){
        baseDeDatos.collection("usuarios")
            .document(usuarioLogeado!!.uid).set(hashMapOf("lista" to camaraLiveData.getListaHistorial().value))
            .addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(databaseTask: Task<Void>) {
                    if (databaseTask.isSuccessful) {

                    } else {
                        Toast.makeText(
                            context,
                            databaseTask.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    /**
     * Funcion que realiza un intent a la actividad ActividadResultadoDetallado, pasando todos los datos.
     * @param datosCamara: los datos de la camara
     * @param posicion: la posicion de ese elemento
     */
    fun mostrarResultadoDetallado(datosCamara: DatosCamara,posicion:Int) {
        val intent= Intent(context, ActividadResultadoDetallado::class.java)
        val b = Bundle()
        b.putString("tituloFoto",datosCamara.tituloImagen)
        b.putString("porcentajeFoto",datosCamara.porcentaje.toString()+" %")
        b.putString("fechaFoto",datosCamara.dias)
        b.putInt("posicion",posicion)
        intent.putExtras(b)
        startActivity(intent)

    }

    /**
     * Funcion que actualiza automaticamente la lista cuando recibe algun cambio la base de datos.
     */
    fun actualizacionAutomatica(){
        baseDeDatos.collection("usuarios").document(usuarioLogeado!!.uid).addSnapshotListener(object :EventListener<DocumentSnapshot>{
            override fun onEvent(snap: DocumentSnapshot?, p1: FirebaseFirestoreException?) {
                if(snap!!.exists()){
                    val listaActualizada=snap.toObject(ListaDatosCamara::class.java)
                    if(listaActualizada != null){
                        camaraLiveData.setListaHistorial(listaActualizada.lista)
                        ListaDatos.listaDatos=camaraLiveData.getListaHistorial().value!!
                        adaptador.setLista(listaActualizada.lista)
                        adaptador.notifyDataSetChanged()
                    }else{
                        adaptador.setLista(arrayListOf())
                        adaptador.notifyDataSetChanged()
                    }
                }
            }
        })
    }
}
