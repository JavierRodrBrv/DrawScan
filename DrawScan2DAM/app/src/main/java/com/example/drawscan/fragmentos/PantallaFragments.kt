package com.example.drawscan.fragmentos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.drawscan.ActividadCamara
import com.example.drawscan.R
import com.example.drawscan.modalview.ViewModelCamara
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

class PantallaFragments : AppCompatActivity() {
    private lateinit var adapter: AdapterParaFragmentos// Adapter utilizado para los fragmentos
    private lateinit var viewPager: ViewPager // Despalzar los fragmentos deslizando hacia la izquierda o derecha
    private lateinit var tb: TabLayout // Layout del tab
    private val botonCamara by lazy { findViewById<FloatingActionButton>(R.id.floatingActionButton) }
    private var modoDark : Boolean = false// Switch para cambiar el modo osucro
    private lateinit var camaraLiveData: ViewModelCamara //Este View-Model es el que utilizamos para rellenar la lista con los datos de la camara.




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_fragments)
        adapter = AdapterParaFragmentos(
            supportFragmentManager
        )
        viewPager = findViewById(R.id.viewPager)
        tb = findViewById(R.id.Tabs)
        crearViewPager(viewPager)
        tb.setupWithViewPager(viewPager) // Mete el viewPager creado dentro del TabLayout

        camaraLiveData=ViewModelProvider(this).get(ViewModelCamara::class.java)


        // Meter iconos del tab
        tb.getTabAt(0)!!.icon = getDrawable(R.drawable.icono_historial)
        tb.getTabAt(1)!!.icon = getDrawable(R.drawable.icono_favoritos)
        tb.getTabAt(2)!!.icon = getDrawable(R.drawable.icono_ajustes)

        botonCamara.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {

                val intentCamara =Intent(applicationContext, ActividadCamara::class.java)
                startActivity(intentCamara)
            }
        })


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
