package com.example.drawscan.clases

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private var myShared: SharedPreferences = context.getSharedPreferences("nombreEjemplo", Context.MODE_PRIVATE)
    private lateinit var listener:ModoNoche

    /**
     * Este metodo guarda el modo oscuro en True o False
     */
    fun setNightModeState(state: Boolean) {
        val editor: SharedPreferences.Editor = myShared.edit()
        editor.putBoolean("ModoNoche", state)
        editor.apply()
        listener.cambiarModoNoche()
    }

    /**
     * Este metodo carga el estado modo oscuro
     */
    fun loadNightModeState(): Boolean {
        return myShared.getBoolean("ModoNoche", false)
    }
    interface ModoNoche{
        fun cambiarModoNoche()

    }
    fun setModoNocheListener(listener:ModoNoche){
        this.listener=listener
    }

}

