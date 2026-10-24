package es.upm.miw.bantumi.datos.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.ui.theme.PaletaColores;

public class PaletaListAdapter extends RecyclerView.Adapter<PaletaListAdapter.PaletaViewHolder> {
    private final List<PaletaColores> paletas;
    public PaletaListAdapter(List<PaletaColores> paletas, Context context) {
        this.paletas = paletas;

        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(context);

        String idGuardado = preferencias.getString(
                context.getString(R.string.key_PaletaColores),
                paletas.get(0).getId()
        );

        for (int i = 0; i < paletas.size(); i++) {
            if (paletas.get(i).getId().equals(idGuardado)) {
                posicionSeleccionada = i;
                break;
            }
        }
    }
    private int posicionSeleccionada = 0;

    @NonNull
    @Override
    public PaletaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_colour_palette_item, parent, false);

        return new PaletaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PaletaViewHolder holder, int position) {
        PaletaColores paleta = paletas.get(position);

        holder.tvNombrePaleta.setText(paleta.getNombre());

        holder.contenedorMuestras.removeAllViews();

        for (int color : paleta.getColores()) {
            View muestra = new View(holder.itemView.getContext());

            LinearLayout.LayoutParams parametros =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1
                    );

            muestra.setLayoutParams(parametros);
            muestra.setBackgroundColor(color);

            holder.contenedorMuestras.addView(muestra);
        }

        if (position == posicionSeleccionada) {
            holder.tvPaletaSeleccionada.setVisibility(View.VISIBLE);
        } else {
            holder.tvPaletaSeleccionada.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            int nuevaPosicion = holder.getBindingAdapterPosition();

            if (nuevaPosicion == RecyclerView.NO_POSITION) {
                return;
            }

            int antiguaPosicion = posicionSeleccionada;
            posicionSeleccionada = nuevaPosicion;

            SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(holder.itemView.getContext());

            preferencias.edit().putString(
                            holder.itemView.getContext().getString(R.string.key_PaletaColores),
                            paletas.get(nuevaPosicion).getId()
                        ).apply();

            if (antiguaPosicion != -1) {
                notifyItemChanged(antiguaPosicion);
            }

            notifyItemChanged(posicionSeleccionada);
        });
    }

    @Override
    public int getItemCount() {
        return paletas.size();
    }

    static class PaletaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombrePaleta;
        LinearLayout contenedorMuestras;
        TextView tvPaletaSeleccionada;

        public PaletaViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombrePaleta = itemView.findViewById(R.id.tvNombrePaleta);

            contenedorMuestras = itemView.findViewById(R.id.contenedorMuestras);

            tvPaletaSeleccionada = itemView.findViewById(R.id.tvPaletaSeleccionada);
        }
    }
}