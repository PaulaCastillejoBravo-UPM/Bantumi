package es.upm.miw.bantumi.ui.actividades;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.datos.data.PaletasDisponibles;
import es.upm.miw.bantumi.datos.models.PuntuacionPartida;
import es.upm.miw.bantumi.datos.models.PuntuacionPartidaRoomDatabase;
import es.upm.miw.bantumi.datos.views.PuntuacionPartidaListAdapter;
import es.upm.miw.bantumi.ui.fragmentos.DeleteDialog;
import es.upm.miw.bantumi.ui.theme.PaletaColores;
import es.upm.miw.bantumi.ui.viewmodel.PuntuacionViewModel;

public class PuntuacionActivity extends AppCompatActivity {
    PuntuacionPartidaRoomDatabase puntuacionPartidaRoomDatabase;
    List<PuntuacionPartida> puntuacionPartidas;

    private PuntuacionViewModel puntuacionViewModel;

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


        puntuacionViewModel = new ViewModelProvider(this).get(PuntuacionViewModel.class);
        android.widget.Button buttonFecha = findViewById(R.id.buttonFecha);
        buttonFecha.setOnClickListener(v -> mostrarCalendario());
        aplicarColoresPaleta();
        String fecha = puntuacionViewModel.getFechaSeleccionada().getValue();
        if (fecha != null) {
            buttonFecha.setText(fecha);
        }


        cargarJugadores();

        android.widget.ImageButton buttonAplicarFiltros = findViewById(R.id.buttonAplicarFiltros);
        buttonAplicarFiltros.setOnClickListener(v -> aplicarFiltros());

        android.widget.ImageButton buttonLimpiarFiltros = findViewById(R.id.buttonLimpiarFiltros);
        buttonLimpiarFiltros.setOnClickListener(v -> limpiarFiltros());
    }

    @Override
    protected void onResume() {
        super.onResume();
        aplicarColoresPaleta();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
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

    private void mostrarCalendario() {
        java.util.Calendar calendario = java.util.Calendar.getInstance();

        int anio = calendario.get(java.util.Calendar.YEAR);
        int mes = calendario.get(java.util.Calendar.MONTH);
        int dia = calendario.get(java.util.Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog selectorFecha = new android.app.DatePickerDialog(
            this,
            (view, anioElegido, mesElegido, diaElegido) -> {
                String fecha = String.format(
                        java.util.Locale.ROOT,
                        "%04d-%02d-%02d",
                        anioElegido,
                        mesElegido + 1,
                        diaElegido
                );

                puntuacionViewModel.setFechaSeleccionada(fecha);
                android.widget.Button buttonFecha = findViewById(R.id.buttonFecha);
                buttonFecha.setText(fecha);
            },
            anio, mes, dia
        );

        selectorFecha.show();
    }


    private void actualizarPuntuaciones(String nombreJugador, String fecha, Integer semillasAlmacen1) {
        puntuacionPartidas = puntuacionPartidaRoomDatabase.puntuacionPartidaDAO().getTopTenFiltered(nombreJugador, fecha, semillasAlmacen1);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        PuntuacionPartidaListAdapter adapter = (PuntuacionPartidaListAdapter) recyclerView.getAdapter();

        if (adapter != null) {
            adapter.setPuntuacionPartida(puntuacionPartidas);
        }
    }


    private void cargarJugadores() {
        List<String> jugadores = new java.util.ArrayList<>();

        jugadores.add("Todos");
        jugadores.addAll(puntuacionPartidaRoomDatabase.puntuacionPartidaDAO().getDistinctPlayers());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                jugadores
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerJugador = findViewById(R.id.spinnerJugador);
        spinnerJugador.setAdapter(adapter);
    }

    private void aplicarFiltros() {
        Spinner spinnerJugador = findViewById(R.id.spinnerJugador);
        String nombreJugador = null;
        if (spinnerJugador.getSelectedItemPosition() > 0) {
            nombreJugador = spinnerJugador.getSelectedItem().toString();
        }

        String fechaFiltro = puntuacionViewModel.getFechaSeleccionada().getValue();

        android.widget.EditText editTextSemillas = findViewById(R.id.editTextSemillas);
        String textoSemillas = editTextSemillas.getText().toString().trim();
        Integer semillasAlmacen1 = null;
        if (!textoSemillas.isEmpty()) {
            semillasAlmacen1 = Integer.parseInt(textoSemillas);
        }

        actualizarPuntuaciones(nombreJugador, fechaFiltro, semillasAlmacen1);
    }

    private void limpiarFiltros() {
        Spinner spinnerJugador = findViewById(R.id.spinnerJugador);
        spinnerJugador.setSelection(0);

        puntuacionViewModel.setFechaSeleccionada(null);
        android.widget.Button buttonFecha = findViewById(R.id.buttonFecha);
        buttonFecha.setText(R.string.txtFechaSelector);

        android.widget.EditText editTextSemillas = findViewById(R.id.editTextSemillas);
        editTextSemillas.setText("");

        actualizarPuntuaciones(null, null, null);
    }

    private void aplicarColoresPaleta() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        String idPaleta = preferencias.getString(
                getString(R.string.key_PaletaColores),
                PaletasDisponibles.obtenerPaletas().get(0).getId()
        );

        PaletaColores paletaSeleccionada = PaletasDisponibles.obtenerPaletas().get(0);

        for (PaletaColores paleta : PaletasDisponibles.obtenerPaletas()) {
            if (paleta.getId().equals(idPaleta)) {
                paletaSeleccionada = paleta;
                break;
            }
        }

        int[] colores = paletaSeleccionada.getColores();

        int colorCasillas = colores[0];
        int colorCasillaSeleccionada = colores[3];
        int colorTexto = colores[4];

        Button buttonFecha = findViewById(R.id.buttonFecha);
        buttonFecha.setBackgroundTintList(
                ColorStateList.valueOf(colorCasillaSeleccionada)
        );

        View encabezadosTabla = findViewById(R.id.encabezadosTabla);
        encabezadosTabla.setBackgroundColor(colorCasillas);
        buttonFecha.setTextColor(colorTexto);

        TextView textoJugador = encabezadosTabla.findViewById(R.id.tvEncabezadoJugador);
        TextView textoPuntuacion = encabezadosTabla.findViewById(R.id.tvEncabezadoPuntuacion);
        TextView textoOrden = encabezadosTabla.findViewById(R.id.tvEncabezadOrden);

        textoJugador.setTextColor(colorTexto);
        textoPuntuacion.setTextColor(colorTexto);
        textoOrden.setTextColor(colorTexto);
    }
}