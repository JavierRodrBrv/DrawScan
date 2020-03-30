package com.example.drawscan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*


    /**
     * Constructor de FragmentPageAdapter
     * @param fm
     */
    class AdapterParaFragmentos (fm: FragmentManager) :FragmentPagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val listaFragmentos = ArrayList<Fragment>() // Lista de fragmentos

        /**
         * Funcion que retorna un fragmento en específico
         * @param position Entero para retornar ese fragmento
         * @return Fragmento
         */
        override fun getItem(position: Int): Fragment {
            return listaFragmentos[position]
        }

        /**
         * Funcón que cuenta el nº de fragmentos que hay en total
         * @return El tamaño del ArrayList listaFragmentos
         */
        override fun getCount(): Int {
            return listaFragmentos.size
        }

        /**
         * Función que añade un nuevo fragmento a la lista
         * @param f Fragmento a añadir
         */
        fun nuevoFragmento(f: Fragment) {
            listaFragmentos.add(f)
        }
    }
