package com.example.drawscan.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Clase que modela un fragment de Fragment Favoritos
 */
class FragmentFavoritos : Fragment() {
    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var adaptador: AdaptadorListView //Adaptador que contiene los elementos de la lista.
    private var binding: FragmentFavoritosBinding? = null //Para encontrar los componentes del layout FragmentsFavoritos
    private lateinit var barraDeBusqueda: SearchView //Barra de busqueda para buscar los elementos.
    private val bindingObtener get() = binding!!
    private val usuarioLogeado by lazy { FirebaseAuth.getInstance().currentUser } //Variable FirebaseAuth donde almacenamos el usuario
    private val baseDeDatos by lazy { FirebaseFirestore.getInstance() } //Variable Firestore donde instanciamos la base de datos.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        val view = binding!!.root
        adaptador = AdaptadorListView(
            context!!,
            ListaDatos.listaDatos,
            false
        )

        binding!!.idListaFavoritos.adapter = adaptador
        barraDeBusqueda = view.findViewById(R.id.idBusquedaFavoritos) as SearchView
        barraDeBusqueda.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            /**
             * Funcion que permite filtrar los elementos mediante a lo que se le introduzca en en campo de la barra de busqueda.
             * @param newText: de tipo string que se compara si existe en la lista.
             */
            override fun onQueryTextChange(newText: String?): Boolean {
                val lista = arrayListOf<DatosCamara>()
                for (datoCamara in ListaDatos.listaDatos) {
                    if (datoCamara.tituloImagen.contains(newText.toString())) {//Si la lista contiene ese titulo, se añade a la lista.
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
                for (datoCamara in ListaDatos.listaDatos){
                    for (datoCamaraFav in lista){
                        if(datoCamara.tituloImagen==datoCamaraFav.tituloImagen&&datoCamara.favorito!=datoCamaraFav.favorito){
                            datoCamara.favorito=datoCamaraFav.favorito
                            break
                        }
                    }
                }
                camaraLiveData.setListaHistorial(ListaDatos.listaDatos)
                actualizarLista()
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

    /**
     * Funcion que realiza un intent a la actividad ActividadResultadoDetallado, pasando todos los datos.
     * @param datosCamara: los datos de la camara
     * @param posicion: la posicion de ese elemento
     */
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

    /**
     * Función que actualiza la base de datos y la lista.
     */
    fun actualizarLista(){
        baseDeDatos.collection("usuarios")
            .document(usuarioLogeado!!.uid).set(hashMapOf("lista" to ListaDatos.listaDatos))
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        camaraLiveData.getListaFavoritos().observe(viewLifecycleOwner,
            Observer<ArrayList<DatosCamara>> { listaFav -> //Cada vez que se realiza un cambio en la lista, el adaptador se cambia solo.
                adaptador.setLista(listaFav!!)
                adaptador.notifyDataSetChanged()
            })
    }


}
