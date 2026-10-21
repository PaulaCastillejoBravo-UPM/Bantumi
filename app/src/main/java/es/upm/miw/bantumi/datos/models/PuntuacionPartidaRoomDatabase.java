
package es.upm.miw.bantumi.datos.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PuntuacionPartida.class}, version = 1, exportSchema = false)
public abstract class PuntuacionPartidaRoomDatabase extends RoomDatabase {
    public static final String BASE_DATOS = PuntuacionPartida.TABLA + ".db";
    public abstract PuntuacionPartidaDAO puntuacionPartidaDAO();
    private static volatile PuntuacionPartidaRoomDatabase INSTANCE;

    public static PuntuacionPartidaRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (PuntuacionPartidaRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    PuntuacionPartidaRoomDatabase.class,
                                    BASE_DATOS
                            )
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
