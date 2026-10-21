package es.upm.miw.bantumi.ui.fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.actividades.MainActivity;

public class RecoverDialog extends DialogFragment {

    private final String partidaGuardada;

    public RecoverDialog(String partidaGuardada) {
        this.partidaGuardada = partidaGuardada;
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) requireActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(main);

        builder
                .setTitle(R.string.txtDialogoRecuperacionTitulo)
                .setMessage(R.string.txtDialogoRecuperacionPregunta)
                .setPositiveButton(
                        getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                main.juegoBantumi.deserializa(partidaGuardada);

                                Snackbar.make(
                                        main.findViewById(android.R.id.content),
                                        getString(R.string.txtPartidaRecuperada),
                                        Snackbar.LENGTH_SHORT
                                ).show();

                            }
                        }
                )
                .setNegativeButton(
                        getString(android.R.string.cancel),
                        null
                );


        return builder.create();
    }
}
