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
            + " WHERE semillasAlmacen1 >= semillasAlmacen2"
            + " ORDER BY semillasAlmacen1 DESC"
            + " LIMIT 10")
    List<PuntuacionPartida> getTopTen();

    @Query("SELECT * FROM " + PuntuacionPartida.TABLA
            + " WHERE semillasAlmacen1 >= semillasAlmacen2"
            + " AND (:nombreJugador IS NULL OR NombreJugador = :nombreJugador)"
            + " AND (:fecha IS NULL OR substr(fechaHora, 1, 10) = :fecha)"
            + " AND (:semillasAlmacen1 IS NULL OR semillasAlmacen1 = :semillasAlmacen1)"
            + " ORDER BY semillasAlmacen1 DESC"
            + " LIMIT 10")
    List<PuntuacionPartida> getTopTenFiltered(String nombreJugador, String fecha, Integer semillasAlmacen1);

    @Query("DELETE FROM " + PuntuacionPartida.TABLA)
    void deleteAll();

    @Query("SELECT DISTINCT NombreJugador FROM " + PuntuacionPartida.TABLA
            + " WHERE NombreJugador != 'Jugador 2 (Ordenador)'"
            + " ORDER BY NombreJugador ASC")
    List<String> getDistinctPlayers();

}