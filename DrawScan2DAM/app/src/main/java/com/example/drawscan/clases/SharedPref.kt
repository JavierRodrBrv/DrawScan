package com.example.drawscan.clases

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private var preferencia: SharedPreferences = context.getSharedPreferences("nombreEjemplo", Context.MODE_PRIVATE)
    private lateinit var listenerMOscuro:ModoNoche

    /**
     * Este metodo guarda el modo oscuro en True o False
     */
    fun setNightModeState(state: Boolean) {
        val editor: SharedPreferences.Editor = preferencia.edit()
        editor.putBoolean("ModoNoche", state)
        editor.apply()
        listenerMOscuro.cambiarModoNoche()
    }
    /**
     * Este metodo guarda si la alerta se queda activa en True o False
     */
    fun setAlertaState(state: Boolean) {
        val editor: SharedPreferences.Editor = preferencia.edit()
        editor.putBoolean("AlertaInicial", state)
        editor.apply()
    }

    /**
     * Este metodo carga el estado modo oscuro.
     */
    fun loadNightModeState(): Boolean {
        return preferencia.getBoolean("ModoNoche", false)
    }
    /**
     * Este metodo activa o desactiva la alerta inicial.
     */
    fun loadAlertaState(): Boolean {
        return preferencia.getBoolean("AlertaInicial", false)
    }


    interface ModoNoche{
        fun cambiarModoNoche()

    }

    fun setModoNocheListener(listener:ModoNoche){
        this.listenerMOscuro=listener
    }


}

