package com.mycompany.trabajopractico.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Acumula los datos de toda la simulación para calcular el reporte final:
 * nacimientos y muertes por tipo, entidad más longeva de cada tipo, lobo con
 * más cacerías, turno de mayor actividad e historial de población.
 */
public class Estadisticas {

    /** Orden fijo de los tipos: 0 = Planta, 1 = Conejo, 2 = Lobo. */
    public static final String[] TIPOS = {Planta.TIPO, Conejo.TIPO, Lobo.TIPO};

    private final int[] nacimientos = new int[3];
    private final int[] muertes = new int[3];

    private final String[] nombreMasLongeva = {"-", "-", "-"};
    private final int[] edadMasLongeva = new int[3];

    private String loboMasCacerias = "-";
    private int maxCacerias = 0;

    private int turnoMayorActividad = 0;
    private int eventosMayorActividad = 0;

    /** Cada fila es {turno, plantas, conejos, lobos}. */
    private final List<int[]> historial = new ArrayList<>();

    /** Posición del tipo dentro de los arreglos. */
    public static int indice(String tipo) {
        for (int i = 0; i < TIPOS.length; i++) {
            if (TIPOS[i].equals(tipo)) {
                return i;
            }
        }
        throw new IllegalArgumentException("tipo de entidad desconocido: " + tipo);
    }

    public void registrarNacimiento(String tipo) {
        nacimientos[indice(tipo)]++;
    }

    public void registrarMuerte(String tipo) {
        muertes[indice(tipo)]++;
    }

    public void revisarLongevidad(Entidad entidad) {
        int i = indice(entidad.getTipo());
        if (entidad.getEdad() > edadMasLongeva[i]) {
            edadMasLongeva[i] = entidad.getEdad();
            nombreMasLongeva[i] = entidad.getNombre();
        }
    }

    public void revisarCacerias(Lobo lobo) {
        if (lobo.getExitosCaza() > maxCacerias) {
            maxCacerias = lobo.getExitosCaza();
            loboMasCacerias = lobo.getNombre();
        }
    }

    public void registrarActividad(int turno, int cantidadEventos) {
        if (cantidadEventos > eventosMayorActividad) {
            eventosMayorActividad = cantidadEventos;
            turnoMayorActividad = turno;
        }
    }

    public void registrarPoblacion(int turno, int plantas, int conejos, int lobos) {
        historial.add(new int[]{turno, plantas, conejos, lobos});
    }

    /** Devuelve {cantidad, turno} de la población máxima del tipo. */
    public int[] maximoPoblacion(int indiceTipo) {
        int[] resultado = {0, 0};
        boolean primero = true;
        for (int[] fila : historial) {
            int cantidad = fila[indiceTipo + 1];
            if (primero || cantidad > resultado[0]) {
                resultado[0] = cantidad;
                resultado[1] = fila[0];
                primero = false;
            }
        }
        return resultado;
    }

    /** Devuelve {cantidad, turno} de la población mínima del tipo. */
    public int[] minimoPoblacion(int indiceTipo) {
        int[] resultado = {0, 0};
        boolean primero = true;
        for (int[] fila : historial) {
            int cantidad = fila[indiceTipo + 1];
            if (primero || cantidad < resultado[0]) {
                resultado[0] = cantidad;
                resultado[1] = fila[0];
                primero = false;
            }
        }
        return resultado;
    }

    // ===================== Getters =====================

    public int getNacimientos(int indiceTipo) {
        return nacimientos[indiceTipo];
    }

    public int getMuertes(int indiceTipo) {
        return muertes[indiceTipo];
    }

    public String getNombreMasLongeva(int indiceTipo) {
        return nombreMasLongeva[indiceTipo];
    }

    public int getEdadMasLongeva(int indiceTipo) {
        return edadMasLongeva[indiceTipo];
    }

    public String getLoboMasCacerias() {
        return loboMasCacerias;
    }

    public int getMaxCacerias() {
        return maxCacerias;
    }

    public int getTurnoMayorActividad() {
        return turnoMayorActividad;
    }

    public int getEventosMayorActividad() {
        return eventosMayorActividad;
    }

    public List<int[]> getHistorial() {
        List<int[]> copia = new ArrayList<>();
        for (int[] fila : historial) {
            copia.add(fila.clone());
        }
        return copia;
    }
}
