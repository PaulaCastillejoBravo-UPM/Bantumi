
package es.upm.miw.bantumi.datos.data;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import es.upm.miw.bantumi.ui.theme.PaletaColores;

public class PaletasDisponibles {

    public static List<PaletaColores> obtenerPaletas() {
        List<PaletaColores> paletas = new ArrayList<>();

        paletas.add(new PaletaColores(
                "original", "Original",
                new int[]{
                        Color.parseColor("#EFB565"),
                        Color.parseColor("#FF018786"),
                        Color.parseColor("#FF33B5E5"),
                        Color.parseColor("#FFDCBE"),
                        Color.parseColor("#3B2A1A")
                }
        ));

        paletas.add(new PaletaColores(
                "lima", "Lima eléctrica",
                new int[]{
                        Color.parseColor("#B8F34A"),
                        Color.parseColor("#28C7B7"),
                        Color.parseColor("#2764DC"),
                        Color.parseColor("#18233B"),
                        Color.parseColor("#F4F7E9")
                }
        ));

        paletas.add(new PaletaColores(
                "jardin", "Jardín de algodón",
                new int[]{
                        Color.parseColor("#BDE8D2"),
                        Color.parseColor("#C5DDF7"),
                        Color.parseColor("#DCCAF5"),
                        Color.parseColor("#F5C9DD"),
                        Color.parseColor("#26332D")
                }
        ));

        paletas.add(new PaletaColores(
                "carmesi", "Carmesí urbano",
                new int[]{
                        Color.parseColor("#171717"),
                        Color.parseColor("#414141"),
                        Color.parseColor("#8B1E32"),
                        Color.parseColor("#D94A5A"),
                        Color.parseColor("#F4F0EB")
                }
        ));

        paletas.add(new PaletaColores(
                "confeti", "Confeti tropical",
                new int[]{
                        Color.parseColor("#17BEBB"),
                        Color.parseColor("#F05D5E"),
                        Color.parseColor("#F7B32B"),
                        Color.parseColor("#A77BCA"),
                        Color.parseColor("#172B2B")
                }
        ));

        paletas.add(new PaletaColores(
                "lavanda", "Lavanda y hielo",
                new int[]{
                        Color.parseColor("#A9C9E8"),
                        Color.parseColor("#B8B5E3"),
                        Color.parseColor("#D8C8F0"),
                        Color.parseColor("#EAC9D8"),
                        Color.parseColor("#292638")
                }
        ));

        paletas.add(new PaletaColores(
                "matcha", "Té matcha",
                new int[]{
                        Color.parseColor("#344E41"),
                        Color.parseColor("#588157"),
                        Color.parseColor("#A3B18A"),
                        Color.parseColor("#DAD7BD"),
                        Color.parseColor("#F5F1E7")
                }
        ));

        paletas.add(new PaletaColores(
                "neon", "Neón digital",
                new int[]{
                        Color.parseColor("#315BFF"),
                        Color.parseColor("#18C9E8"),
                        Color.parseColor("#8D65FF"),
                        Color.parseColor("#B7F34A"),
                        Color.parseColor("#FFFFFF")
                }
        ));

        paletas.add(new PaletaColores(
                "sakura", "Sakura nocturno",
                new int[]{
                        Color.parseColor("#4A263D"),
                        Color.parseColor("#8F425E"),
                        Color.parseColor("#D98BA7"),
                        Color.parseColor("#F0C7D5"),
                        Color.parseColor("#F8F0E8")
                }
        ));

        paletas.add(new PaletaColores(
                "frutos_rojos", "Frutos rojos",
                new int[]{
                        Color.parseColor("#F06B73"),
                        Color.parseColor("#D73568"),
                        Color.parseColor("#A88AE8"),
                        Color.parseColor("#F7B9D0"),
                        Color.parseColor("#351B24")
                }
        ));

        paletas.add(new PaletaColores(
                "grafito", "Grafito y menta",
                new int[]{
                        Color.parseColor("#202329"),
                        Color.parseColor("#454B52"),
                        Color.parseColor("#AAB2B5"),
                        Color.parseColor("#69D5C2"),
                        Color.parseColor("#FFFFFF")
                }
        ));

        paletas.add(new PaletaColores(
                "japon", "Japón tradicional",
                new int[]{
                        Color.parseColor("#253858"),
                        Color.parseColor("#C94343"),
                        Color.parseColor("#78916C"),
                        Color.parseColor("#E8D8BA"),
                        Color.parseColor("#F7F1E5")
                }
        ));

        return paletas;
    }
}
