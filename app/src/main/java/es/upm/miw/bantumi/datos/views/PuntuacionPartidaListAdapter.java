package es.upm.miw.bantumi.datos.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.models.PuntuacionPartida;

public class PuntuacionPartidaListAdapter extends RecyclerView.Adapter<PuntuacionPartidaListAdapter.PuntuacionPartidaViewHolder> {
    class PuntuacionPartidaViewHolder extends RecyclerView.ViewHolder {
        private final TextView nombreJugadorItemView;
        private final TextView mejorPuntuacionItemView;

        private PuntuacionPartidaViewHolder(View itemView) {
            super(itemView);
            nombreJugadorItemView = itemView.findViewById(R.id.tvNombreJugador);
            mejorPuntuacionItemView = itemView.findViewById(R.id.tvMejorPuntuacion);
        }
    }

    private final LayoutInflater mInflater;
    private List<PuntuacionPartida> mPuntuacionPartida;

    public PuntuacionPartidaListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PuntuacionPartidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new PuntuacionPartidaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PuntuacionPartidaViewHolder holder, int position) {
        if (mPuntuacionPartida != null) {
            PuntuacionPartida current = mPuntuacionPartida.get(position);
            holder.nombreJugadorItemView.setText(current.getNombreJugador());

            holder.mejorPuntuacionItemView.setText(String.valueOf(current.getMejorPuntuacion()));
        }
    }

    public void setPuntuacionPartida(List<PuntuacionPartida> puntuacionPartidas){
        mPuntuacionPartida = puntuacionPartidas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPuntuacionPartida == null
                ? 0
                : mPuntuacionPartida.size();
    }
}
