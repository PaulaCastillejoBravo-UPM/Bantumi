package es.upm.miw.bantumi.datos.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {PuntuacionPartida.class}, version = 1)
public abstract class PuntuacionPartidaRepositorio extends RoomDatabase {
    public static final String BASE_DATOS = PuntuacionPartida.TABLA + ".db";
    public abstract PuntuacionPartidaDAO puntuacionPartidaDAO();
}