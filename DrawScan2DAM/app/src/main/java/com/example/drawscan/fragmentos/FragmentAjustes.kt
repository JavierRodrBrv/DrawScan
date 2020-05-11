package com.example.drawscan.fragmentos

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.drawscan.MainActivity
import com.example.drawscan.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class FragmentAjustes : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var botonCerrarSesion: Button
    private lateinit var uFirebase: FirebaseAuth
    private lateinit var gsic: GoogleSignInClient
    private lateinit var animacion: AnimationDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentView = LayoutInflater.from(context).inflate(R.layout.fragment_ajustes,null)
        val constraintLayout: ConstraintLayout = fragmentView.findViewById(R.id.idConstAjustes)
        animacion = constraintLayout.background as AnimationDrawable
        animacion.setEnterFadeDuration(2000)
        animacion.setExitFadeDuration(4000)
        animacion.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uFirebase = FirebaseAuth.getInstance()
        val gsio = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        gsic = GoogleSignIn.getClient(fragmentView.context, gsio)
        botonCerrarSesion = fragmentView.findViewById(R.id.botonCerrarSesion)
        botonCerrarSesion.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val dialogo = AlertDialog.Builder(fragmentView.context,R.style.estiloDialogoCerrarSesion)
                dialogo.setIcon(R.drawable.icono_alerta)
                dialogo.setTitle(resources.getString(R.string.tituloDialogo))
                dialogo.setMessage(resources.getString(R.string.mensajeDialogo))

                // Configuración del botón negativo ('No')
                dialogo.setNegativeButton(
                    resources.getString(R.string.botonNegativoDialogo)
                ) { dialog, which -> }

                // Configuración del botón positivo ('Si')
                // Si el botón se llega a pulsar el botón 'Si', se va a cerrar la sesión de la cuenta de Google y redirigir a la pantalla inicial
                dialogo.setPositiveButton(
                    resources.getString(R.string.botonPositivoDialogo)
                ) { dialog, which ->
                    Toast.makeText(
                        fragmentView.context,
                        resources.getString(R.string.textoSesionCerrada) + " con ${uFirebase.getCurrentUser()!!.email}",
                        Toast.LENGTH_LONG
                    ).show()
                    uFirebase.signOut()
                    gsic.signOut()
                        .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                            if (task.isSuccessful) {

                                val paginaInicio = Intent(
                                    fragmentView.context,
                                    MainActivity::class.java
                                )
                                paginaInicio.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                startActivity(paginaInicio)
                            }
                        })
                }
                dialogo.show()
            }
        })
        return fragmentView
    }

}
