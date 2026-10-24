package es.upm.miw.bantumi.ui.actividades;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.data.PaletasDisponibles;
import es.upm.miw.bantumi.datos.views.PaletaListAdapter;

public class PaletasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_palette);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Paleta de colores");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView rvPaletas = findViewById(R.id.rvPaletas);

        rvPaletas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        PaletaListAdapter adaptador = new PaletaListAdapter(PaletasDisponibles.obtenerPaletas(), this);

        rvPaletas.setAdapter(adaptador);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
