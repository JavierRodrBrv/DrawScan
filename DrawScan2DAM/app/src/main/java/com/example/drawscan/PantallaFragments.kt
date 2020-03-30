package com.example.drawscan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class PantallaFragments : AppCompatActivity() {
    private var adapter: AdapterParaFragmentos? = null// Adapter utilizado para los fragmentos
    private var viewPager: ViewPager? = null // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private var botonCamara: FloatingActionButton? = null //Boton que dirige a la camara
    private var tb: TabLayout? = null // Layout del tab
    private var modoDark : Boolean? = null// Switch para cambiar el modo osucro



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_fragments)
        adapter = AdapterParaFragmentos(supportFragmentManager)
        viewPager = findViewById(R.id.viewPager)
        botonCamara = findViewById(R.id.floatingActionButton)
    }

    /**
     * Función que mete el adapter al viewPager
     *
     * @param vp el viewPager donde le queremos meter el adapter
     */
    fun crearViewPager(vp: ViewPager) {
        //adapter!!.nuevoFragmento(TabHistorial())
        //adapter!!.nuevoFragmento(TabFavoritos())
        //adapter!!.nuevoFragmento(TabSoporte())
        vp.adapter = adapter
    }


    /**
     * Función que sobreescribe la funcionalidad al dar el botón de atrás
     */
    override fun onBackPressed() {
        finishAffinity()
    }
}
