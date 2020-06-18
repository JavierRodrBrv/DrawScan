package com.example.drawscan.actividad

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.drawscan.R
import com.example.drawscan.clases.GlideApp
import com.example.drawscan.clases.SharedPref
import com.example.drawscan.globales.ListaDatos
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Esta clase modela la ActividadResultadoDetallado, la cual muestra los detalles de la comparación.
 * @author Javier Rodríguez
 */
class ActividadResultadoDetallado : AppCompatActivity() {
    private lateinit var titulo: TextView // Variable titulo de tiulo textView para almacenar el String.
    private lateinit var porcentaje: TextView //Variable porcentaje de tipo textview para almacenar el porcentaje.
    private lateinit var fecha: TextView //Variable fecha de tipo textview para almacenar la fecha la cual se realizó la comparación.
    private lateinit var imagen1: ImageView //En esta variable se almacena la primera fotografia.
    private lateinit var imagen2: ImageView //Esta variable almacena la segunda fotografia.
    private var mStorage: StorageReference? = null //Variable que permite recuperar la primera fotografia de Storage.
    private var mStorage2: StorageReference? = null //Variable que permite recuperar la segunda fotografía de Storage.
    private lateinit var sharedPreferences: SharedPref //Variable que permite guardar las preferencias.
    private val baseDeDatos by lazy { FirebaseFirestore.getInstance() } //Variable que recoge la base de datos.
    private val usuarioLogeado by lazy { FirebaseAuth.getInstance().currentUser } //Variable que recoge el usuario.
    private var posicionLista:Int=0 //Variable que guardamos para determinar en que posicion en la lista se encuentra el elemento.

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = SharedPref(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resultado_detallado)
        val b:Bundle=intent.extras!!
        titulo=findViewById(R.id.idTituloDetallado)
        porcentaje=findViewById(R.id.idPorcentajeDetallado)
        fecha=findViewById(R.id.idFechaDetallada)
        imagen1=findViewById(R.id.idImagen1)
        imagen2=findViewById(R.id.idImagen2)
        titulo.setText(b.getString("tituloFoto"))
        porcentaje.setText(b.getString("porcentajeFoto"))
        fecha.setText(b.getString("fechaFoto"))
        posicionLista=b.getInt("posicion")

        mStorage = FirebaseStorage.getInstance().getReference().child(usuarioLogeado!!.uid+"/"+titulo.text+"1")
        mStorage2 = FirebaseStorage.getInstance().getReference().child(usuarioLogeado!!.uid+"/"+titulo.text+"2")

        GlideApp.with(this).load(mStorage).into(imagen1)
        GlideApp.with(this).load(mStorage2).into(imagen2)
    }

    /**
     * Esta función te redirige a PantallaFragments si presionas atrás con el movil.
     */
    override fun onBackPressed() {
        val i = Intent(this, PantallaFragments::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        super.onBackPressed()
    }

    /**
     * Esta función recupera el tema que hay en ese momento, ya sea modo oscuro o el normal.
     * @return retorna el tema seleccionado.
     */
    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        //Comprueba si la preferencia esta true, si es asi, se activa el modo oscuro, sino, se aplica el tema normal.
        if (sharedPreferences.loadNightModeState()) {
            theme.applyStyle(R.style.NightMode, true)
        } else {
            theme.applyStyle(R.style.AppTheme, true)
        }
        return theme
    }

    /**
     * Función que permite eliminar el elemento de la lista.
     * @param view: se genera con el Onclick, pero no se usa.
     */
    fun eliminarElemento(view: View) {
        ListaDatos.listaDatos.removeAt(posicionLista)
        actualizarLista()
        finish()
    }

    /**
     * Función que recupera los datos de la base de datos y lo actualiza en la lista.
     */
    fun actualizarLista(){
        baseDeDatos.collection("usuarios")
            .document(usuarioLogeado!!.uid).set(hashMapOf("lista" to ListaDatos.listaDatos))
            .addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(databaseTask: Task<Void>) {
                    //Si la consulta es true, se eliminar las dos imagenes de base de datos Storage, sino, saca exception.
                    if (databaseTask.isSuccessful) {
                        mStorage!!.delete()
                        mStorage2!!.delete()

                    } else {
                        Toast.makeText(
                            this@ActividadResultadoDetallado,
                            databaseTask.exception.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }
}
