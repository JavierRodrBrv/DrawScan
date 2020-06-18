package com.example.drawscan.modalview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drawscan.clases.DatosCamara

/**
 * Clase ViewModelCamara que modela un ViewModel para la lista historial y favoritos.
 * @author Javi Rodriguez
 */
class ViewModelCamara : ViewModel() {
    private var camaraLiveData: MutableLiveData<ArrayList<DatosCamara>> = MutableLiveData() //Variable camaraLiveData de tipo MutableLiveData
    private var listaHistorial:MutableLiveData<ArrayList<DatosCamara>> = MutableLiveData() //Variable listaHistorial de tipo MutableLiveData
    private var listaFavoritos:MutableLiveData<ArrayList<DatosCamara>> = MutableLiveData() //Variable listaFavoritos de tipo MutableLiveData
/*
    fun setCamaraLiveData(actualizarDatosCamara: ArrayList<DatosCamara>) {
        camaraLiveData.value = actualizarDatosCamara
    }*/
    /**
     * Funcion que obtiene la lista LiveData
     * @return camaraLiveData
     */
    fun getCamaraLiveDara(): LiveData<ArrayList<DatosCamara>> {
        return camaraLiveData
    }

    /**
     * Funcion que actualiza la lista historial
     * @param actualizarListaHistorial: ArrayList , contiene la lista nueva de historial.
     */
    fun setListaHistorial(actualizarListaHistorial: ArrayList<DatosCamara>){
        listaHistorial.value=actualizarListaHistorial
    }

    /**
     * Funcion que retorna la lista historial del viewModel
     * @return listaHistorial
     */
    fun getListaHistorial() : LiveData<ArrayList<DatosCamara>>{
        return listaHistorial
    }
    /**
     * Funcion que actualiza la lista favoritos
     * @param actualizarListaFavoritos: ArrayList , contiene la lista nueva de favoritos.
     */
    fun setListaFavoritos(actualizarListaFavoritos: ArrayList<DatosCamara>){
        listaFavoritos.value=actualizarListaFavoritos
    }
    /**
     * Funcion que retorna la lista favoritos del viewModel
     * @return listaFavoritos
     */
    fun getListaFavoritos() : LiveData<ArrayList<DatosCamara>>{
        return listaFavoritos
    }


}