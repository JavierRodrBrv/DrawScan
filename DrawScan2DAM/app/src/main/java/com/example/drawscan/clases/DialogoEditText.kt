package com.example.drawscan.clases

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.example.drawscan.R
import com.google.android.material.textfield.TextInputLayout

/**
 * Clase DialogoEditText que modela el dialogo para ingresar el titulo de la comparación.
 * @author Javier Rodríguez.
 */
class DialogoEditText(private val contexto: Context) : DialogFragment() {
    private var editTextTitulo: TextInputLayout? = null //Variable editText que almacena el titulo introducido.
    private var listener: EditTextTituloListener? = null //listener de tipo editText para modificar el titulo.
    private var vista: View? = null //Variable view de tipo view inicializada a null para que no de problemas.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vista = LayoutInflater.from(contexto).inflate(R.layout.layout_dialog, null)
        editTextTitulo = vista!!.findViewById(R.id.editTextTituloFoto)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_AppCompat_Light_Dialog)
    }

    /**
     * Funcion que crea el dialogo introduciendole el mensaje, titulo y botones.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(contexto, R.style.estiloDialogo)
        builder.setView(vista)
            .setMessage(R.string.descripcionDialogo)
            .setPositiveButton(" ", null)
            .setNegativeButton(" ",null)
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        val dialogo = dialog as AlertDialog?
        dialogo!!.setOnCancelListener(object : DialogInterface.OnCancelListener {
            override fun onCancel(dialog: DialogInterface?) {
                listener!!.acabarActividad()
            }
        })
        val botonAceptar =
            dialogo.getButton(AlertDialog.BUTTON_POSITIVE)
        botonAceptar.setBackgroundResource(R.drawable.boton_redondo_dialogo_aceptar)
        botonAceptar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_aceptar,0,0,0)
        botonAceptar.setPadding(40,0,0,0)
        botonAceptar.setOnClickListener(View.OnClickListener {
            if (!validarTitulo()) {//Si es falso, se aplica el titulo de la comparación.
                return@OnClickListener
            } else {
                val tituloFoto =
                    editTextTitulo!!.editText!!.text.toString()
                listener!!.aplicarTitulo(tituloFoto)
            }
        })
        val botonCancelar=dialogo.getButton(AlertDialog.BUTTON_NEGATIVE)
        botonCancelar.setBackgroundResource(R.drawable.boton_redondo_dialogo_cancelar)
        botonCancelar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icono_cancelar,0,0,0)
        botonCancelar.setPadding(38,0,0,0)
        botonCancelar.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {//Funcion que acaba con la actividad
                listener!!.acabarActividad()
            }
        })
        val layoutParamsCancelar =botonCancelar.getLayoutParams() as LinearLayout.LayoutParams
        val layoutParamsAceptar =botonAceptar.getLayoutParams() as LinearLayout.LayoutParams
        layoutParamsCancelar.setMargins(0,0,490,15)
        layoutParamsAceptar.setMargins(0,0,15,15)
        botonCancelar.setLayoutParams(layoutParamsCancelar)
        botonAceptar.setLayoutParams(layoutParamsAceptar)
    }

    /**
     * Funcion onAttach sirve para inicializar el listener. Ya que es lo primero que se ejecuta.
     * @param context: contexto de la aplicación.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as EditTextTituloListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context deberia de implementar el escuchador del dialogo")
        }
    }

    /**
     * Interfaz del dialogo
     * aplicarTitulo: aplica el titulo de la comparacion
     * acabarActividad: finaliza la actividad.
     */
    interface EditTextTituloListener {
        fun aplicarTitulo(tituloFoto: String?)
        fun acabarActividad()
    }

    /**
     * Función de tipo boolean que valida el titulo, si es correcto o no es correcto.
     */
    private fun validarTitulo(): Boolean {
        val tituloFoto = editTextTitulo!!.editText!!.text.toString()
        return if (tituloFoto.isEmpty()) {//Si el campo esta vacio o tiene mas de 10 caracteres, devuelve false, en lo contrario true.
            editTextTitulo!!.error = contexto.resources.getString(R.string.errorDialogoVacio)
            false
        } else if (tituloFoto.length > 10) {
            editTextTitulo!!.error = contexto.resources.getString(R.string.errorDialogoMax10)
            false
        } else {
            editTextTitulo!!.error = null
            true
        }
    }
}