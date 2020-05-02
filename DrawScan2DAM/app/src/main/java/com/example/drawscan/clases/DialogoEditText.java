package com.example.drawscan.clases;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.drawscan.R;
import com.google.android.material.textfield.TextInputLayout;

public class DialogoEditText extends AppCompatDialogFragment {
    private TextInputLayout editTextTitulo;
    private EditTextTituloListener listener;
    private Context contexto;
    private View view;

    public DialogoEditText(Context context) {
        contexto = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(contexto).inflate(R.layout.layout_dialog, null);
        editTextTitulo = view.findViewById(R.id.editTextTituloFoto);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable final Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setView(view)
                .setTitle(R.string.tituloAlerta)
                .setMessage(R.string.descripcionDialogo)
                .setPositiveButton(R.string.opcionAceptarDialogo, null)
                .setNegativeButton(R.string.opcionCancelarDialogo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.acabarActividad();
                    }
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog dialogo = (AlertDialog) getDialog();
        Button botonAceptar=dialogo.getButton(AlertDialog.BUTTON_POSITIVE);
        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validarTitulo()){
                    return;
                }else{
                    String tituloFoto = editTextTitulo.getEditText().getText().toString();
                    listener.aplicarTitulo(tituloFoto);
                }
            }
        });

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (EditTextTituloListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " deberia de implementar el escuchador del dialogo");
        }
    }

    public interface EditTextTituloListener {
        void aplicarTitulo(String tituloFoto);
        void acabarActividad();
    }

    private boolean validarTitulo(){
        String tituloFoto=editTextTitulo.getEditText().getText().toString();

        if(tituloFoto.isEmpty()){
            editTextTitulo.setError("Este campo no puede estar vacio");
            return false;
        }else if(tituloFoto.length()>10){
            editTextTitulo.setError("No puede ser mas de 10 caracteres");
            return false;
        }else{
            editTextTitulo.setError(null);
            return true;
        }

    }

}
