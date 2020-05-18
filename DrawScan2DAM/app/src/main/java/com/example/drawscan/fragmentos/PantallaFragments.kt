package com.example.drawscan.fragmentos

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.drawscan.ActividadCamara
import com.example.drawscan.R
import com.example.drawscan.clases.SharedPref
import com.example.drawscan.globales.BooleanPopup
import com.example.drawscan.modalview.ViewModelCamara
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout


class PantallaFragments : AppCompatActivity() {
    private lateinit var adapter: AdapterParaFragmentos// Adapter utilizado para los fragmentos
    private lateinit var viewPager: ViewPager // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private lateinit var tb: TabLayout // Layout del tab
    private val botonCamara by lazy { findViewById<FloatingActionButton>(R.id.floatingActionButton) }
    private var modoDark: Boolean = false// Switch para cambiar el modo osucro
    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.
    private lateinit var dialogoPopup: Dialog //Este es el dialogo Pop-Up de informacion sobre el correcto uso de la aplicación.
    private lateinit var imagenCerrarPopup: ImageView //Esta es la imagen X para cerrar el dialogo Pop-up
    private lateinit var botonEntendido: Button //Este boton cierra el dialogo Pop-up
    private lateinit var sharedPreferences: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences=SharedPref(this)
        super.onCreate(savedInstanceState)
        sharedPreferences.setModoNocheListener(object : SharedPref.ModoNoche{
            override fun cambiarModoNoche() {
                if(sharedPreferences.loadNightModeState()){
                    Toast.makeText(applicationContext,"hola",Toast.LENGTH_LONG).show()
                    reiniciarApp()
                }else{
                    reiniciarApp()
                }
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
            override fun onClick(v: View?) {
                val intentCamara = Intent(applicationContext, ActividadCamara::class.java)
                startActivity(intentCamara)
            }
        })
        if (BooleanPopup.boolPopup) {
            mostrarDialogoPopUp()
            BooleanPopup.boolPopup = false
        }

    }

    /**
     * Función que mete el adapter al viewPager
     *
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

    fun mostrarDialogoPopUp() {
        dialogoPopup.setContentView(R.layout.custom_popup_dialog)
        imagenCerrarPopup = dialogoPopup.findViewById(R.id.cerrarPopup) as ImageView
        botonEntendido = dialogoPopup.findViewById(R.id.botonEntendidoPopup) as Button

        imagenCerrarPopup.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialogoPopup.dismiss()
            }
        })
        botonEntendido.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialogoPopup.dismiss()
            }
        })
        dialogoPopup.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogoPopup.show()
    }

    fun reiniciarApp() {
        val i = Intent(this, PantallaFragments::class.java)
        startActivity(i)
        finish()

    }

    fun getPreferences():SharedPref{
        return sharedPreferences
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        if(sharedPreferences.loadNightModeState()){
            theme.applyStyle(R.style.NightMode, true)
        }else{
            theme.applyStyle(R.style.AppTheme, true)
        }
        return theme
    }

}
