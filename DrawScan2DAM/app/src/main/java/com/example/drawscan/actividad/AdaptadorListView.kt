package com.example.drawscan.actividad

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.drawscan.R
import com.example.drawscan.clases.DatosCamara
import com.example.drawscan.clases.GlideApp
import com.example.drawscan.clases.SharedPref
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sackcentury.shinebuttonlib.ShineButton

class AdaptadorListView(contexto: Context, lista: ArrayList<DatosCamara>,private var esListaHistorial:Boolean) :
    ArrayAdapter<DatosCamara>(contexto, 0, lista), Filterable {
    private var contextoAplicacion = contexto //Variable que contiene el contexto de la aplicación.
    private lateinit var pantallaFragments: PantallaFragments//Actividad donde estan los fragments
    private lateinit var textoTituloFoto: TextView//Textview con el titulo de la foto.
    private lateinit var textoFecha: TextView//Textview con la fecha.
    private lateinit var porcentajeFoto: TextView//Textview donde muestra el porcentaje de similitud.
    private lateinit var fotoImagen: ImageView // Esta es la imagen realizada por la camara.
    private lateinit var main: MainActivity // Esta es la actividad inicial.
    private var listaDatos: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados
    private var fullLista: ArrayList<DatosCamara>? = null// ArrayList de datos escaneados, se usa para el filtro de busqueda.
    private lateinit var sharedPreferences: SharedPref //Variable que contiene la preferencia.
    private lateinit var botonFavorito:ShineButton // Variable de tipo ShineButton para utilizarlo como botón favorito.
    private val usuarioLogeado by lazy { FirebaseAuth.getInstance().currentUser } //Variable que recoge el usuario.
    private val baseDeDatos by lazy { FirebaseFirestore.getInstance() } //Variable que recoge la base de datos.
    private lateinit var listener: ModificarLista //Variable de tipo listener para modificar la lista
    private var mStorage: StorageReference? = null //Variable de tipo Storage para mostrar la imagen en la lista.

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
        mStorage = FirebaseStorage.getInstance().getReference().child(usuarioLogeado!!.uid+"/"+listaDatos!!.get(position).tituloImagen+"1")
        sharedPreferences = SharedPref(contextoAplicacion)
        main = MainActivity()
        textoTituloFoto = vistaElemento!!.findViewById(R.id.idTituloFoto) as TextView
        textoFecha = vistaElemento.findViewById(R.id.idFecha) as TextView
        porcentajeFoto = vistaElemento.findViewById(R.id.idPorcentaje) as TextView
        fotoImagen=vistaElemento.findViewById(R.id.idImagenFoto) as ImageView
        botonFavorito=vistaElemento.findViewById(R.id.botonFavorito) as ShineButton
        vistaElemento.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {//Función onclick que elimina el elemento.
                listener.eliminarElemento(position)
            }
        })
        textoTituloFoto.setText(listaDatos!!.get(position).tituloImagen)
        porcentajeFoto.setText(contextoAplicacion.resources.getString(R.string.porcentajeExacto)+" "+listaDatos!!.get(position).porcentaje.toString()+" %")
        textoFecha.setText(contextoAplicacion.resources.getString(R.string.fechaConcreta)+" "+listaDatos!!.get(position).dias)
        //Para rellenar con la foto de la referencia en el imageView de elementoLista
        GlideApp.with(vistaElemento.context).load(mStorage).into(fotoImagen)
        //Si es true, cambia el estilo de texto para modo oscuro, sino, deja el estilo por defecto.
        cargarModoOscuro()
        sharedPreferences.setModoNocheListener(object : SharedPref.ModoOscuro {
            override fun cambiarModoOscuro() {//Esta función realiza el cambio en la preferencia de modo oscuro.
                cargarModoOscuro()
            }
        })
        //Si el elemento es true, el boton favorito se marca check, haciendo asi cambiar de color.
        if(listaDatos!!.get(position).favorito){
            botonFavorito.isChecked=true
        }

        botonFavorito.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {//Función que agrega a favorito al elemento.
                if(listaDatos!!.get(position).favorito){
                    Toast.makeText(
                        contextoAplicacion,
                        "Se ha quitado de favoritos",
                        Toast.LENGTH_LONG
                    ).show()

                    listaDatos!!.get(position).favorito=false

                }else{
                    Toast.makeText(
                        contextoAplicacion,
                        "Se ha agregado a favoritos",
                        Toast.LENGTH_LONG
                    ).show()

                    listaDatos!!.get(position).favorito=true
                }

                if(esListaHistorial){
                    actualizarLista()
                }else{
                    listener.agregarFav(listaDatos!!)
                }

            }
        })
        return vistaElemento
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

    /**
     * Función que establece la lista nueva.
     */
    fun setLista(nuevaLista:ArrayList<DatosCamara>){
        listaDatos=nuevaLista
        fullLista=nuevaLista
    }

    /**
     * Función que añade la lista que hay en el momento a base de datos.
     */
    fun actualizarLista(){
        baseDeDatos.collection("usuarios")
            .document(usuarioLogeado!!.uid).set(hashMapOf("lista" to listaDatos))
            .addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(databaseTask: Task<Void>) {
                    if (databaseTask.isSuccessful) {
                        listener.agregarFav(listaDatos!!)
                    } else {
                        Toast.makeText(
                            contextoAplicacion,
                            databaseTask.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    /**
     * Función que inicializa el listener
     * @param newListener: Variable de tipo ModificarLista
     */
    fun setListener(newListener:ModificarLista){
        listener=newListener
    }

    /**
     * Interfaz para el adaptador ListView.
     * agregarFav: Agrega el elemento a favoritos.
     * eliminarElemento: Elmimina el elemento de la lista.
     */
    interface ModificarLista{
        fun agregarFav(lista: ArrayList<DatosCamara>)
        fun eliminarElemento(posicion:Int)
    }

    /**
     * Función que carga de preferencias el modo oscuro.
     */
    fun cargarModoOscuro(){
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

}