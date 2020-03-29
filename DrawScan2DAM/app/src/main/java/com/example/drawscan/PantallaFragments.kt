package com.example.drawscan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PantallaFragments : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_fragments)
    }


    /**
     * Función que sobreescribe la funcionalidad al dar el botón de atrás
     */
    override fun onBackPressed() {
        finishAffinity()
    }
}
