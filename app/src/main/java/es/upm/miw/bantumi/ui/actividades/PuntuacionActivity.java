package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.models.PuntuacionPartida;
import es.upm.miw.bantumi.datos.models.PuntuacionPartidaRoomDatabase;
import es.upm.miw.bantumi.datos.views.PuntuacionPartidaListAdapter;
import es.upm.miw.bantumi.ui.fragmentos.DeleteDialog;

public class PuntuacionActivity extends AppCompatActivity {
    PuntuacionPartidaRoomDatabase puntuacionPartidaRoomDatabase;
    List<PuntuacionPartida> puntuacionPartidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        puntuacionPartidaRoomDatabase = PuntuacionPartidaRoomDatabase.getDatabase(this);

        puntuacionPartidas = puntuacionPartidaRoomDatabase.puntuacionPartidaDAO().getTopTen();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        final PuntuacionPartidaListAdapter adapter = new PuntuacionPartidaListAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new androidx.recyclerview.widget.DividerItemDecoration(
                        this,
                        LinearLayoutManager.VERTICAL
                )
        );

        adapter.setPuntuacionPartida(puntuacionPartidas);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.puntuacion_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        }

        if (itemId == R.id.opcEliminarPuntuaciones) {
            new DeleteDialog().show(
                    getSupportFragmentManager(),
                    "DELETE_DIALOG"
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void deleteAll() {
        puntuacionPartidaRoomDatabase.puntuacionPartidaDAO().deleteAll();

        puntuacionPartidas = puntuacionPartidaRoomDatabase.puntuacionPartidaDAO().getTopTen();

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        PuntuacionPartidaListAdapter adapter = (PuntuacionPartidaListAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.setPuntuacionPartida(puntuacionPartidas);
        }
    }
}