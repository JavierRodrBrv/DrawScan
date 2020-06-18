package com.example.drawscan.actividad

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.drawscan.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

/**
 * Clase principal que modela la actividad MainActivity.
 * @author Javier Rodríguez.
 */
class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN = 123 // Código de permiso para iniciar sesión con Google
    private var mFirebase: FirebaseAuth? = null // Autenticación con Firebase
    private var gsic: GoogleSignInClient? = null // Cliente de cuentas de Google
    private var miLayout: ConstraintLayout? = null //El layout de mainActivity
    private var animationDrawable: AnimationDrawable? = null //Variable de tipo AnimationDrawable para meter animación en los colores.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        mFirebase = FirebaseAuth.getInstance()
        miLayout = findViewById<View>(R.id.miFondo) as ConstraintLayout
        animationDrawable = miLayout!!.getBackground() as AnimationDrawable?
        animationDrawable?.setEnterFadeDuration(2000)
        animationDrawable?.setExitFadeDuration(4000)
        animationDrawable?.start()

        // Permite elegir con que cuenta de google quiere iniciar sesion
        val gsio = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        gsic = GoogleSignIn.getClient(this, gsio)

    }
    override fun onResume() {
        super.onResume()
        if (this.animationDrawable != null && !animationDrawable!!.isRunning) {
            animationDrawable!!.start();
        }
    }
    override fun onPause() {
        super.onPause()
        if (this.animationDrawable != null && !animationDrawable!!.isRunning) {
            animationDrawable!!.stop();
        }
    }
    /**
     * Función que permite iniciar sesion con Google.
     * @param view: Es del onClick pero no se usa.
     */
    fun iniciarSesionGoogle(view: View) {
        val intentIniciarSesion = gsic!!.signInIntent
        startActivityForResult(intentIniciarSesion, GOOGLE_SIGN)
    }
    /**
     * Función que obtiene un resultado de la actividad, a través de startActivityForResult()
     * @param requestCode : Se utiliza para obtener una respuesta en la consulta.
     * @param resultCode : No se utiliza.
     * @param data : No se utiliza.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN) {
            try {
                val taskSignIn = GoogleSignIn.getSignedInAccountFromIntent(data)
                val cuentaGoogle = taskSignIn.result
                autenticacionFirebase(cuentaGoogle!!)
            } catch (e: ApiException) {
                Toast.makeText(this, e.statusCode, Toast.LENGTH_LONG).show()

            } catch (runtimeException: RuntimeExecutionException) {
                Log.d("errorException", runtimeException.message!!)
            }
        }
    }
    /**
     * Función que verifica la cuenta elegida de Google con Firebase
     * @param googleAccount: Variable que almacena la cuenta de Google.
     */
    fun autenticacionFirebase(googleAccount: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mFirebase!!.signInWithCredential(credential)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful) {
                        val currentUser = mFirebase!!.currentUser
                        val irDirectamente =
                            Intent(this@MainActivity, PantallaFragments::class.java)
                        irDirectamente.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(irDirectamente)
                        Toast.makeText(
                            this@MainActivity,
                            "Iniciado sesión como:\n${mFirebase!!.getCurrentUser()!!.displayName}\n${mFirebase!!.getCurrentUser()!!.email}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            })
    }
    /**
     * Función que sobreescribe la funcionalidad al dar el botón de atrás
     */
    override fun onBackPressed() {
        finishAffinity()
    }
}
