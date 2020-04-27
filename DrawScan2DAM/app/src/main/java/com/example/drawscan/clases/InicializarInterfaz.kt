package com.example.drawscan.clases

class InicializarInterfaz {
    //Esta manera de kotlin es equivalente a ponerlo static en java, haciendo asi que se pueda instanciar en otras clases.
    companion object {

        private var listener: InsertarDatosEnModelView? = null
        private var array: ArrayList<DatosCamara> = arrayListOf()


        fun setListener(listener: InsertarDatosEnModelView) {
            this.listener = listener
        }

        fun getArray(): ArrayList<DatosCamara> {
            return array
        }

        fun setArray(array: ArrayList<DatosCamara>) {
            this.array = array
            if (listener != null) {
                listener!!.getLista(array)

            }
        }


    }

    interface InsertarDatosEnModelView {
        fun getLista(listaRellenar: ArrayList<DatosCamara>)
    }


}