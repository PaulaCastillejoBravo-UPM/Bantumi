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
}