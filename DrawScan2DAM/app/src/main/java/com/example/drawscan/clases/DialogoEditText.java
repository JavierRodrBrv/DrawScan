package com.example.drawscan.clases;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.drawscan.R;

public class DialogoEditText extends AppCompatDialogFragment {
    private EditText editTextTitulo;
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
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setView(view)
                .setTitle(R.string.tituloAlerta)
                .setMessage("Introduce el titulo que quieras ponerle a esta comparacion.")
                .setPositiveButton(R.string.opcionAceptarDialogo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tituloFoto = editTextTitulo.getText().toString();
                        listener.aplicarTitulo(tituloFoto);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.acabarActividad();
                    }
                });
        return builder.create();
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
}
