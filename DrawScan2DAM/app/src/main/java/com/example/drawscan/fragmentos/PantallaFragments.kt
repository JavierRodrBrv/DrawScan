package com.example.drawscan.fragmentos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.drawscan.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class PantallaFragments : AppCompatActivity() {
    private lateinit var adapter: AdapterParaFragmentos// Adapter utilizado para los fragmentos
    private lateinit var viewPager: ViewPager // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private lateinit var botonCamara: FloatingActionButton //Boton que dirige a la camara
    private lateinit var tb: TabLayout // Layout del tab
    private var modoDark : Boolean = false// Switch para cambiar el modo osucro



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_fragments)
        adapter = AdapterParaFragmentos(
            supportFragmentManager
        )
        viewPager = findViewById(R.id.viewPager)
        botonCamara = findViewById(R.id.floatingActionButton)
        tb = findViewById(R.id.Tabs)
        crearViewPager(viewPager)
        tb.setupWithViewPager(viewPager) // Mete el viewPager creado dentro del TabLayout

        // Meter iconos del tab
        tb.getTabAt(0)!!.icon = getDrawable(R.drawable.icono_historial)
        tb.getTabAt(1)!!.icon = getDrawable(R.drawable.icono_favoritos)
        tb.getTabAt(2)!!.icon = getDrawable(R.drawable.icono_ajustes)

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
}
