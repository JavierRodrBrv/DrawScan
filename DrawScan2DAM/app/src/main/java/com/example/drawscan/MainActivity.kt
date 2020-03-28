package com.example.drawscan

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN = 123 // Código de permiso para iniciar sesión con Google
    private var mFirebase : FirebaseAuth? = null // Autenticación con Firebase
    private var gsic : GoogleSignInClient? = null // Cliente de cuentas de Google
    private var miLayout: ConstraintLayout? = null
    private var animationDrawable: AnimationDrawable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN


        )
        setContentView(R.layout.activity_main)
        miLayout = findViewById<View>(R.id.miFondo) as ConstraintLayout
        animationDrawable = miLayout!!.getBackground() as AnimationDrawable?
        animationDrawable?.setEnterFadeDuration(4000)
        animationDrawable?.setExitFadeDuration(4000)
        animationDrawable?.start()


        // Permite elegir con que cuenta de google quiere iniciar sesion
        val gsio = GoogleSignInOptions.Builder().requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsic = GoogleSignIn.getClient(applicationContext, gsio)



    }


    override fun onResume() {
        super.onResume()
        if (this.animationDrawable!=null && !animationDrawable!!.isRunning) {
            animationDrawable!!.start();
        }
    }

    override fun onPause() {
        super.onPause()
        if (this.animationDrawable!=null && !animationDrawable!!.isRunning) {
            animationDrawable!!.stop();
        }
    }

    fun iniciarSesionGoogle(view: View) {
        val intentIniciarSesion = gsic!!.signInIntent
        startActivityForResult(intentIniciarSesion,GOOGLE_SIGN)
    }

    /**
     * Función que obtiene un resultado de la actividad, a través de startActivityForResult()
     * @param requestCode Código de permiso
     * @param resultCode Código de resultado
     * @param data Intent del activity
     */
    override fun onActivityResult(requestCode: Int,resultCode: Int,data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN) {

            val resultado = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Toast.makeText(applicationContext,resultado.toString(),Toast.LENGTH_LONG).show()
            if (resultado.isSuccess) {
                val cuentaGoogle = resultado.signInAccount
                if (cuentaGoogle!!.email != null) {
                    autenticacionFirebase(cuentaGoogle.email)
                }
            } else {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Este error saldrá si alguién ha programando a las 1 de la noche y no ha hecho las cosas bien",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Función que verifica la cuenta elegida de Google con Firebase
     * @param emailGoogle cuenta de Google que ha elegido el usuario
     */
    fun autenticacionFirebase(emailGoogle: String?) {
        mFirebase!!.signInWithEmailAndPassword(emailGoogle!!, emailGoogle)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    val intentPaginaPrincipal =Intent(applicationContext, PantallaFragments::class.java)
                    startActivity(intentPaginaPrincipal)

                    //barraProgreso.setVisibility(View.INVISIBLE)
                } else {
                    crearInicioSesion(emailGoogle)
                }
            }
    }

    /**
     * Función que crea la cuenta en caso de que no exista a la hora de verificar en Firebase
     * @param emailGoogle
     */
    fun crearInicioSesion(emailGoogle: String?) {
        mFirebase!!.createUserWithEmailAndPassword(emailGoogle!!, emailGoogle)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    val intentPaginaPrincipal =
                        Intent(applicationContext, MainActivity::class.java)
                    startActivity(intentPaginaPrincipal)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Error a la hora de iniciar sesíon",
                        Toast.LENGTH_LONG
                    ).show()
                }
                //barraProgreso.setVisibility(View.INVISIBLE)
            }
    }


    /**
     * Función que ejecuta justo al abrir la aplicación
     */
    override fun onStart() {
        mFirebase = FirebaseAuth.getInstance()
        if (mFirebase!!.getCurrentUser() != null) { // Si el usuario ya ha iniciado sesión anteriormente, ira directamente a la página principal
            val irDirectamente = Intent(this, MainActivity::class.java)
            irDirectamente.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(irDirectamente)
            Toast.makeText(
                applicationContext,
                "Iniciado sesión como: " + mFirebase!!.getCurrentUser()!!.email,
                Toast.LENGTH_LONG
            ).show()
        }
        super.onStart()
    }

    /**
     * Función que sobreescribe la funcionalidad al dar el botón de atrás
     */
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}
