package com.example.drawscan.clases

import android.content.Context
import android.content.SharedPreferences

/**
 * Clase que modela las preferencias de la aplicacion
 * @author Javi Rodríguez
 * @param context: es el contexto de la aplicacion.
 */
class SharedPref(context: Context) {
    private var preferencia: SharedPreferences = context.getSharedPreferences("Preferencias", Context.MODE_PRIVATE) //Variable de tipo SharedPreferences la cual guardar las preferencias.
    private lateinit var listenerMOscuro:ModoOscuro //listener de modoOscuro

    /**
     * Este metodo guarda el modo oscuro en True o False
     * @param state: de tipo boolean, si es true: establece el modo oscuro.
     */
    fun setNightModeState(state: Boolean) {
        val editor: SharedPreferences.Editor = preferencia.edit()
        editor.putBoolean("ModoNoche", state)
        editor.apply()
        listenerMOscuro.cambiarModoOscuro()
    }
    /**
     * Este metodo guarda si la alerta se queda activa en True o False
     * @param state: de tipo boolean, si es true, la alerta se inicia.
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
        return preferencia.getBoolean("AlertaInicial", true)
    }

    /**
     * Interfaz de la clase SharedPref sobre modo oscuro.
     * cambiarModoOscuro: establece el modo oscuro a la aplicación.
     */
    interface ModoOscuro{
        fun cambiarModoOscuro()

    }

    /**
     * Funcion que realiza la inicializacion de la interfaz del modo oscuro.
     */
    fun setModoNocheListener(listener:ModoOscuro){
        this.listenerMOscuro=listener
    }


}

