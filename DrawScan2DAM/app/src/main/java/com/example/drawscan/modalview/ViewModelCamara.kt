package com.example.drawscan.modalview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drawscan.clases.DatosCamara

class ViewModelCamara : ViewModel() {
    private var camaraLiveData: MutableLiveData<ArrayList<DatosCamara>> = MutableLiveData()

    fun setCamaraLiveData(actualizarDatosCamara: ArrayList<DatosCamara>) {
        camaraLiveData.value = actualizarDatosCamara
    }

    fun getCamaraLiveDara(): LiveData<ArrayList<DatosCamara>> {
        return camaraLiveData
    }

}