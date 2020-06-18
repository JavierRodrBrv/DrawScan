package com.example.drawscan.actividad

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.drawscan.R
import com.example.drawscan.clases.SharedPref
import com.example.drawscan.fragmentos.*
import com.example.drawscan.globales.BooleanPopup
import com.example.drawscan.modalview.ViewModelCamara
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
/**
 * Clase que modela la actividad PantallaFragments.
 * @author Javier Rodríguez.
 */
class PantallaFragments : AppCompatActivity() {
    private lateinit var adapter: AdapterParaFragmentos// Adapter utilizado para los fragmentos
    private lateinit var viewPager: ViewPager // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private lateinit var tb: TabLayout // Layout del tab
    private val botonCamara by lazy { findViewById<FloatingActionButton>(R.id.floatingActionButton) } //Variable de tipo FloatingActionButton que permite ir a la actividad de la camara.
    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var dialogoPopup: Dialog //Este es el dialogo Pop-Up de informacion sobre el correcto uso de la aplicación.
    private lateinit var imagenCerrarPopup: ImageView //Esta es la imagen X para cerrar el dialogo Pop-up
    private lateinit var botonEntendido: Button //Este boton cierra el dialogo Pop-up
    private lateinit var sharedPreferences: SharedPref //Variable que almacena la preferencia.

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = SharedPref(this)
        super.onCreate(savedInstanceState)
        sharedPreferences.setModoNocheListener(object : SharedPref.ModoOscuro {
            override fun cambiarModoOscuro() {//Función que cambia al modo oscuro
                reiniciarApp()
            }
        })
        dialogoPopup = Dialog(this)
        setContentView(R.layout.activity_pantalla_fragments)
        adapter = AdapterParaFragmentos(
            supportFragmentManager
        )
        viewPager = findViewById(R.id.viewPager)
        tb = findViewById(R.id.Tabs)
        crearViewPager(viewPager)
        tb.setupWithViewPager(viewPager) // Mete el viewPager creado dentro del TabLayout
        camaraLiveData = ViewModelProvider(this).get(ViewModelCamara::class.java)
        // Meter iconos del tab
        tb.getTabAt(0)!!.icon = getDrawable(R.drawable.icono_historial)
        tb.getTabAt(1)!!.icon = getDrawable(R.drawable.icono_favoritos)
        tb.getTabAt(2)!!.icon = getDrawable(R.drawable.icono_ajustes)
        botonCamara.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {//Función que lleva a la actividad camara
                val intentCamara = Intent(applicationContext, ActividadCamara::class.java)
                startActivity(intentCamara)
            }
        })
        if (BooleanPopup.boolPopup && sharedPreferences.loadAlertaState()) {//Si la variable es true y la preferencia, muestra el dialogo
            mostrarDialogoPopUp()
            BooleanPopup.boolPopup = false
        }
    }
    /**
     * Función que mete el adapter al viewPager
     * @param vp el viewPager donde le queremos meter el adapter
     */
    fun crearViewPager(vp: ViewPager) {
        adapter.nuevoFragmento(FragmentHistorial())
        adapter.nuevoFragmento(FragmentFavoritos())
        adapter.nuevoFragmento(FragmentAjustes())
        vp.adapter = adapter
    }

    /**
     * Función que sobreescribe la funcionalidad al dar el botón de atrás
     */
    override fun onBackPressed() {
        finishAffinity()
    }

    /**
     * Función que muestra el dialogo Pop-up sobre el correcto funcionamiento de la aplicación.
     */
    fun mostrarDialogoPopUp() {
        dialogoPopup.setContentView(R.layout.custom_popup_dialog)
        imagenCerrarPopup = dialogoPopup.findViewById(R.id.cerrarPopup) as ImageView
        botonEntendido = dialogoPopup.findViewById(R.id.botonEntendidoPopup) as Button
        imagenCerrarPopup.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {//Si se pulsa en la X, el cialogo se cierra
                dialogoPopup.dismiss()
            }
        })
        botonEntendido.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {//Si se pulsa en el boton, el dialogo se cierra.
                dialogoPopup.dismiss()
            }
        })
        dialogoPopup.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogoPopup.show()
    }

    /**
     * Función que permite realizar la aplicación.
     */
    fun reiniciarApp() {
        val i = Intent(this, PantallaFragments::class.java)
        startActivity(i)
        finish()
    }

    /**
     * Funcion getPreferences, obtiene la preferencia.
     */
    fun getPreferences(): SharedPref {
        return sharedPreferences
    }

    /**
     * Esta función recupera el tema que hay en ese momento, ya sea modo oscuro o el normal.
     * @return retorna el tema seleccionado.
     */
    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if (sharedPreferences.loadNightModeState()) {
            theme.applyStyle(R.style.NightMode, true)
        } else {
            theme.applyStyle(R.style.AppTheme, true)
        }
        return theme
    }


}
