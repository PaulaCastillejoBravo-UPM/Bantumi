package es.upm.miw.bantumi.datos.models;

import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = PuntuacionPartida.TABLA)
public class PuntuacionPartida {
    static public final String TABLA = "puntuacionPartida";
    @PrimaryKey(autoGenerate = true)
    protected int uid;
    protected String NombreJugador;
    protected String fechaHora;
    protected int semillasAlmacen1;
    protected int semillasAlmacen2;

    public PuntuacionPartida(String NombreJugador, String fechaHora, int semillasAlmacen1, int semillasAlmacen2 ) {
        this.NombreJugador = NombreJugador;
        this.fechaHora = fechaHora;
        this.semillasAlmacen1 = semillasAlmacen1;
        this.semillasAlmacen2 = semillasAlmacen2;
    }

    public int getUid() {
        return uid;
    }

    public String getNombreJugador() {
        return NombreJugador;
    }

    public void setNombreJugador(String NombreJugador) {
        this.NombreJugador = NombreJugador;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getSemillasAlmacen1() {
        return semillasAlmacen1;
    }

    public void setSemillasAlmacen1(int semillasAlmacen1) {
        this.semillasAlmacen1 = semillasAlmacen1;
    }

    public int getSemillasAlmacen2() {
        return semillasAlmacen2;
    }

    public void setSemillasAlmacen2(int semillasAlmacen2) {
        this.semillasAlmacen2 = semillasAlmacen2;
    }
}
