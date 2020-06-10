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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ActividadResultadoDetallado : AppCompatActivity() {

    private lateinit var titulo: TextView
    private lateinit var porcentaje: TextView
    private lateinit var fecha: TextView
    private lateinit var imagen1: ImageView
    private lateinit var imagen2: ImageView
    private var mStorage: StorageReference? = null
    private var mStorage2: StorageReference? = null
    private lateinit var sharedPreferences: SharedPref
    private val baseDeDatos by lazy { FirebaseFirestore.getInstance() }
    private val usuarioLogeado by lazy { FirebaseAuth.getInstance().currentUser }
    private lateinit var adaptadorListView:AdaptadorListView
    private var posicionLista:Int=0

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

    override fun onBackPressed() {
        val i = Intent(this, PantallaFragments::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(i)
        super.onBackPressed()
    }
    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (sharedPreferences.loadNightModeState()) {
            theme.applyStyle(R.style.NightMode, true)
        } else {
            theme.applyStyle(R.style.AppTheme, true)
        }
        return theme
    }

    fun eliminarElemento(view: View) {
        ListaDatos.listaDatos.removeAt(posicionLista)
        actualizarLista()
        finish()
    }
    fun actualizarLista(){
        baseDeDatos.collection("usuarios")
            .document(usuarioLogeado!!.uid).set(hashMapOf("lista" to ListaDatos.listaDatos))
            .addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(databaseTask: Task<Void>) {
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
