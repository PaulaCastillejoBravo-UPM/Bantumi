package es.upm.miw.bantumi.ui.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.upm.miw.bantumi.datos.data.PaletasDisponibles;
import es.upm.miw.bantumi.datos.models.PuntuacionPartida;
import es.upm.miw.bantumi.datos.models.PuntuacionPartidaRoomDatabase;
import es.upm.miw.bantumi.ui.fragmentos.FinalAlertDialog;
import es.upm.miw.bantumi.R;
import es.upm.miw.bantumi.dominio.logica.JuegoBantumi;
import es.upm.miw.bantumi.ui.fragmentos.ListSavedGamesDialog;
import es.upm.miw.bantumi.ui.fragmentos.RecoverDialog;
import es.upm.miw.bantumi.ui.fragmentos.ResetDialog;
import es.upm.miw.bantumi.ui.theme.PaletaColores;
import es.upm.miw.bantumi.ui.viewmodel.BantumiViewModel;

public class MainActivity extends AppCompatActivity {

    protected final String LOG_TAG = "MiW";
    public JuegoBantumi juegoBantumi;
    private BantumiViewModel bantumiVM;
    int numInicialSemillas;

    private PuntuacionPartidaRoomDatabase puntuacionPartidaRoomDatabase;
    private String nombreJugador;

    private View ultimaCasillaSeleccionada = null;

    private JuegoBantumi.Turno turnoInicial;

    private int colorCasillas;
    private int colorAlmacenes;
    private int colorTurnoActivo;
    private int colorCasillaSeleccionada;
    private int colorTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        actualizarNombreJugador();
        actualizarSemillasIniciales();
        actualizarTurnoInicial();
        actualizarPaletaColores();

        // Instancia el ViewModel y el juego, y asigna observadores a los huecos
        bantumiVM = new ViewModelProvider(this).get(BantumiViewModel.class);
        juegoBantumi = new JuegoBantumi(bantumiVM, turnoInicial, numInicialSemillas);
        crearObservadores();

        puntuacionPartidaRoomDatabase = PuntuacionPartidaRoomDatabase.getDatabase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarNombreJugador();

        int semillasAnteriores = numInicialSemillas;
        actualizarSemillasIniciales();

        JuegoBantumi.Turno turnoAnterior = turnoInicial;
        actualizarTurnoInicial();

        if (juegoBantumi != null && (semillasAnteriores != numInicialSemillas || turnoAnterior != turnoInicial)) {
            reiniciarPartida();
        }

        actualizarPaletaColores();
    }

    /**
     * Crea y subscribe los observadores asignados a las posiciones del tablero.
     * Si se modifica el contenido del tablero -> se actualiza la vista.
     */
    private void crearObservadores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            int finalI = i;
            bantumiVM.getNumSemillas(i).observe(    // Huecos y almacenes
                    this,
                    new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            mostrarValor(finalI, juegoBantumi.getSemillas(finalI));
                        }
                    });
        }
        bantumiVM.getTurno().observe(   // Turno
                this,
                new Observer<JuegoBantumi.Turno>() {
                    @Override
                    public void onChanged(JuegoBantumi.Turno turno) {
                        marcarTurno(juegoBantumi.turnoActual());
                    }
                }
        );
    }

    /**
     * Indica el turno actual cambiando el color del texto
     *
     * @param turnoActual turno actual
     */
    private void marcarTurno(@NonNull JuegoBantumi.Turno turnoActual) {
        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        TextView tvJugador2 = findViewById(R.id.tvPlayer2);

        tvJugador1.setTextColor(Color.BLACK);
        tvJugador2.setTextColor(Color.BLACK);
        tvJugador1.setBackgroundColor(Color.TRANSPARENT);
        tvJugador2.setBackgroundColor(Color.TRANSPARENT);

        switch (turnoActual) {
            case turnoJ1:
                tvJugador1.setBackgroundColor(colorTurnoActivo);
                break;
            case turnoJ2:
                tvJugador2.setBackgroundColor(colorTurnoActivo);
                break;
            default:
                break;
        }
    }

    /**
     * Muestra el valor <i>valor</i> en la posición <i>pos</i>
     *
     * @param pos posición a actualizar
     * @param valor valor a mostrar
     */
    private void mostrarValor(int pos, int valor) {
        String num2digitos = String.format(Locale.getDefault(), "%02d", pos);
        // Los identificadores de los huecos tienen el formato casilla_XX
        int idBoton = getResources().getIdentifier("casilla_" + num2digitos, "id", getPackageName());
        if (0 != idBoton) {
            TextView viewHueco = findViewById(idBoton);
            viewHueco.setText(String.valueOf(valor));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.opciones_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.opcAcercaDe) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.aboutTitle)
                    .setMessage(R.string.aboutMessage)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        } else if (itemId == R.id.opcAjustes) {
            startActivity(new Intent(this, BantumiPrefs.class));
        } else if (itemId == R.id.opcReiniciarPartida) {
            new ResetDialog().show(getSupportFragmentManager(), "RESET_DIALOG");
        } else if (itemId == R.id.opcGuardarPartida) {
            String partidaSerializada = juegoBantumi.serializa();

            FileOutputStream fos;

            String nombreFichero = generarNombreFicheroPartida();

            try {
                fos = openFileOutput(nombreFichero, Context.MODE_PRIVATE);

                fos.write(partidaSerializada.getBytes());
                fos.close();

                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtPartidaGuardada),
                        Snackbar.LENGTH_SHORT
                ).show();
            } catch (Exception e) {
                Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
            }

            Log.i("MiW", "Partida guardada");
        } else if (itemId == R.id.opcRecuperarPartida) {
            String[] ficheros = obtenerFicherosPartidas();

            if (ficheros.length == 0) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtFicheroVacio),
                        Snackbar.LENGTH_SHORT
                ).show();
                return true;
            }

            List<String> partidasGuardadas = new ArrayList<>();

            for (String fichero : ficheros) {
                partidasGuardadas.add(obtenerFechaPartida(fichero));
            }

            new ListSavedGamesDialog(partidasGuardadas, ficheros).show(getSupportFragmentManager(), "LIST_SAVED_GAMES_DIALOG");

        } else if (itemId == R.id.opcMejoresResultados) {
            Intent intent = new Intent(MainActivity.this, PuntuacionActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.txtSinImplementar),
                    Snackbar.LENGTH_LONG
            ).show();
        }
        return true;
    }

    /**
     * Acción que se ejecuta al pulsar sobre cualquier hueco
     *
     * @param v Vista pulsada (hueco)
     */
    public void huecoPulsado(@NonNull View v) {
        String resourceName = getResources().getResourceEntryName(v.getId()); // pXY
        int num = Integer.parseInt(resourceName.substring(resourceName.length() - 2));
        Log.i(LOG_TAG, "huecoPulsado(" + resourceName + ") num=" + num);
        switch (juegoBantumi.turnoActual()) {
            case turnoJ1:
                Log.i(LOG_TAG, "* Juega Jugador");
                resaltarCasillaSeleccionada(v);
                juegoBantumi.jugar(num);
                break;
            case turnoJ2:
                Log.i(LOG_TAG, "* Juega Computador");
                int posComputador = juegoBantumi.juegaComputador();
                String idCasilla = String.format(
                        Locale.getDefault(),
                        "casilla_%02d",
                        posComputador
                );

                int id = getResources().getIdentifier(
                        idCasilla, "id", getPackageName()
                );

                View casillaComputador = findViewById(id);

                if (casillaComputador != null) {
                    resaltarCasillaSeleccionada(casillaComputador);
                }
                break;
            default:    // JUEGO TERMINADO
                finJuego();
        }
        if (juegoBantumi.juegoTerminado()) {
            finJuego();
        }
    }

    /**
     * El juego ha terminado. Volver a jugar?
     */
    private void finJuego() {
        quitarResaltado();

        String texto = (juegoBantumi.getSemillas(6) > 6 * numInicialSemillas)
                ? "Gana " + nombreJugador
                : "Gana Jugador 2";
        if (juegoBantumi.getSemillas(6) == 6 * numInicialSemillas) {
            texto = "¡¡¡ EMPATE !!!";
        }

        String fechaHora = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()
        ).format(new Date());

        int semillasJugador1 = juegoBantumi.getSemillas(7 - 1);
        int semillasJugador2 = juegoBantumi.getSemillas(14 - 1);

        String nombreMejorJugador;

        if (semillasJugador1 > semillasJugador2) {
            nombreMejorJugador = nombreJugador;
        } else if (semillasJugador2 > semillasJugador1) {
            nombreMejorJugador = "Jugador 2 (Ordenador)";
        } else {
            nombreMejorJugador = nombreJugador;
        }

        puntuacionPartidaRoomDatabase.puntuacionPartidaDAO().insert(new PuntuacionPartida(
                                                nombreMejorJugador,
                                                fechaHora,
                                                semillasJugador1,
                                                semillasJugador2
                                        ));

        // terminar
        new FinalAlertDialog(texto).show(getSupportFragmentManager(), "ALERT_DIALOG");
    }

    public void reiniciarPartida() {
        actualizarSemillasIniciales();
        actualizarTurnoInicial();
        quitarResaltado();
        juegoBantumi = new JuegoBantumi(bantumiVM, turnoInicial, numInicialSemillas);
        juegoBantumi.inicializar(turnoInicial);
    }


    private String generarNombreFicheroPartida() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.ROOT);

        String fechaHora = formatoFecha.format(new Date());

        return "partida_" + fechaHora + ".txt";
    }

    private String[] obtenerFicherosPartidas() {
        String[] ficheros = fileList();

        return java.util.Arrays.stream(ficheros).filter(fichero -> fichero.startsWith("partida_")).sorted(java.util.Comparator.reverseOrder()).toArray(String[]::new);
    }

    private String obtenerFechaPartida(String nombreFichero) {
        String fecha = nombreFichero.replace("partida_", "").replace(".txt", "");

        SimpleDateFormat formatoNombre = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS", Locale.ROOT);

        SimpleDateFormat formatoVisible = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

        try {
            Date fechaHora = formatoNombre.parse(fecha);

            return formatoVisible.format(fechaHora);
        } catch (java.text.ParseException e) {
            Log.e(LOG_TAG, "Error al interpretar la fecha", e);
            return nombreFichero;
        }
    }

    public void recuperarPartida(String nombreFichero) {
        boolean hayContenido = false;
        String partidaRecuperada = "";

        try (
                BufferedReader fin = new BufferedReader(
                        new InputStreamReader(openFileInput(nombreFichero))
                )
        ) {
            String linea = fin.readLine();

            while (linea != null) {
                hayContenido = true;
                partidaRecuperada = linea;
                linea = fin.readLine();
            }

            Log.i(LOG_TAG, "Partida recuperada: " + partidaRecuperada);

            if (partidaRecuperada.equals(juegoBantumi.serializa())) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.txtPartidasIdenticas),
                        Snackbar.LENGTH_SHORT
                ).show();
            } else {
                new RecoverDialog(partidaRecuperada)
                        .show(getSupportFragmentManager(), "RECOVER_DIALOG");
            }

            Log.i(LOG_TAG, "Partida recuperada");

        } catch (Exception e) {
            Log.e(LOG_TAG, "FILE I/O ERROR: " + e.getMessage());
        }

        if (!hayContenido) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    getString(R.string.txtFicheroVacio),
                    Snackbar.LENGTH_SHORT
            ).show();
        }
    }

    private void actualizarNombreJugador() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        nombreJugador = preferencias.getString(
                getString(R.string.key_NombreJugador),
                getString(R.string.txtPlayer1)
        );

        TextView tvJugador1 = findViewById(R.id.tvPlayer1);
        tvJugador1.setText(nombreJugador);
    }

    private void actualizarSemillasIniciales() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        numInicialSemillas = Integer.parseInt(
                preferencias.getString(
                        getString(R.string.key_SemillasIniciales),
                        String.valueOf(
                                getResources().getInteger(R.integer.intNumInicialSemillas)
                        )
                )
        );
    }

    private void actualizarTurnoInicial() {
        SharedPreferences preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        boolean empiezaMaquina = preferencias.getBoolean(
                getString(R.string.key_JugadorInicial),
                false
        );

        turnoInicial = empiezaMaquina ? JuegoBantumi.Turno.turnoJ2 : JuegoBantumi.Turno.turnoJ1;
    }

    private void actualizarPaletaColores() {
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

        colorCasillas = colores[0];
        colorAlmacenes = colores[1];
        colorTurnoActivo = colores[2];
        colorCasillaSeleccionada = colores[3];
        colorTexto = colores[4];

        aplicarPaletaColores();

        if (juegoBantumi != null) {
            marcarTurno(juegoBantumi.turnoActual());
        }
    }

    private void resaltarCasillaSeleccionada(@NonNull View casilla) {
        if (ultimaCasillaSeleccionada != null) {
            ultimaCasillaSeleccionada.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(colorCasillas));
        }

        casilla.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(colorCasillaSeleccionada)
        );

        ultimaCasillaSeleccionada = casilla;
    }

    public void quitarResaltado() {
        if (ultimaCasillaSeleccionada != null) {
            ultimaCasillaSeleccionada.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(colorCasillas)
            );

            ultimaCasillaSeleccionada = null;
        }
    }


    private void aplicarPaletaColores() {
        for (int i = 0; i < JuegoBantumi.NUM_POSICIONES; i++) {
            if (i == 6 || i == 13) {
                continue;
            }

            String nombre = String.format(Locale.ROOT, "casilla_%02d", i);
            int id = getResources().getIdentifier(
                    nombre, "id", getPackageName()
            );

            Button casilla = findViewById(id);

            if (casilla != null) {
                casilla.setBackgroundTintList(
                        android.content.res.ColorStateList.valueOf(colorCasillas)
                );
                casilla.setTextColor(colorTexto);
            }
        }


        TextView almacenJ1 = findViewById(R.id.casilla_06);
        TextView almacenJ2 = findViewById(R.id.casilla_13);

        almacenJ1.setBackgroundColor(Color.TRANSPARENT);
        almacenJ2.setBackgroundColor(Color.TRANSPARENT);
        almacenJ1.setTextColor(colorAlmacenes);
        almacenJ2.setTextColor(colorAlmacenes);


        TextView jugador1 = findViewById(R.id.tvPlayer1);
        TextView jugador2 = findViewById(R.id.tvPlayer2);

        jugador1.setTextColor(colorTexto);
        jugador2.setTextColor(colorTexto);
    }

}