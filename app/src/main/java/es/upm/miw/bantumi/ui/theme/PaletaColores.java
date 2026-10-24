package es.upm.miw.bantumi.ui.theme;

public class PaletaColores {
    private final String id;
    private final String nombre;
    private final int[] colores;

    public PaletaColores(String id, String nombre, int[] colores) {
        this.id = id;
        this.nombre = nombre;
        this.colores = colores;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int[] getColores() {
        return colores;
    }
}
