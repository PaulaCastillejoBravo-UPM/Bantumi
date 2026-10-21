package es.upm.miw.bantumi.datos.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PuntuacionPartidaDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(PuntuacionPartida puntuacionPartida);

    @Query("SELECT * FROM " + PuntuacionPartida.TABLA
            + " WHERE semillasAlmacen1 > semillasAlmacen2"
            + " ORDER BY semillasAlmacen1 DESC"
            + " LIMIT 10")
    List<PuntuacionPartida> getTopTen();

    @Query("DELETE FROM " + PuntuacionPartida.TABLA)
    void deleteAll();
}