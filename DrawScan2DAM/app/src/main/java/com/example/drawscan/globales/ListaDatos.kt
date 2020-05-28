package com.example.drawscan.globales

import com.example.drawscan.clases.DatosCamara

object ListaDatos{
    var listaDatos: ArrayList<DatosCamara> = arrayListOf()// ArrayList de datos escaneados
    private var listaDatosFav: ArrayList<DatosCamara> = arrayListOf()// ArrayList de datos escaneados
    private lateinit var listener:ListaModificada

    fun setListaDatosFav(nuevaListaFav:ArrayList<DatosCamara>){
        listaDatosFav=nuevaListaFav
        listener.listaFavoritoModificado(listaDatosFav)
    }

    fun setLista(nuevaListaDatos:ArrayList<DatosCamara>){
        listaDatos=nuevaListaDatos
        listener.listaDatosModificado(listaDatos)
    }

    interface ListaModificada{
        fun listaFavoritoModificado(lista:ArrayList<DatosCamara>)
        fun listaDatosModificado(listaModificada:ArrayList<DatosCamara>)
    }

    fun setListener(nuevoListener:ListaModificada){
        listener=nuevoListener
    }


}

