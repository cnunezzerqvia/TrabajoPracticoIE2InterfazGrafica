package com.mycompany.trabajopractico.dao;

import com.mycompany.trabajopractico.modelo.Clima;
import com.mycompany.trabajopractico.modelo.Conejo;
import com.mycompany.trabajopractico.modelo.Ecosistema;
import com.mycompany.trabajopractico.modelo.Estadisticas;
import com.mycompany.trabajopractico.modelo.Lobo;
import com.mycompany.trabajopractico.modelo.Planta;
import com.mycompany.trabajopractico.modelo.Simulacion;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda y lee la simulación en un archivo de texto plano (.txt).
 *
 * Formato: una línea por dato, con el tipo de línea primero y los valores
 * separados por punto y coma. Ejemplo:
 * <pre>
 * CONFIG;20;6;SOLEADO;false      (turnos totales; turno actual; clima; intervención usada)
 * IDS;14;9;3                     (últimos números usados en los nombres)
 * TIPO;Planta;4;6;Planta-2;6     (tipo; nacimientos; muertes; más longeva; edad)
 * RECORDS;Lobo-1;3;4;12          (mejor lobo; cacerías; turno más activo; eventos)
 * HISTORIAL;6;12;5;2             (turno; plantas; conejos; lobos)
 * PLANTA;Planta-7;45.0;2;3       (nombre; energía; edad; tamaño)
 * CONEJO;Conejo-3;60.0;4         (nombre; energía; edad)
 * LOBO;Lobo-1;70.0;6;3           (nombre; energía; edad; cacerías)
 * EVENTO;Conejo-3 comió a ...    (eventos del último turno)
 * </pre>
 */
public class SimulacionDAOTxt implements SimulacionDAO {

    private static final String SEP = ";";

    private final File archivo;
    private String ultimoError = "";

    /** La ruta es relativa a la carpeta del proyecto (sin rutas absolutas). */
    public SimulacionDAOTxt(String rutaRelativa) {
        this.archivo = new File(rutaRelativa);
    }

    @Override
    public boolean existePartidaGuardada() {
        return archivo.isFile();
    }

    @Override
    public String getUltimoError() {
        return ultimoError;
    }

    // ===================== Guardar =====================

    @Override
    public boolean guardar(Simulacion simulacion) {
        File carpeta = archivo.getAbsoluteFile().getParentFile();
        if (carpeta != null && !carpeta.exists() && !carpeta.mkdirs()) {
            ultimoError = "No se pudo crear la carpeta \"" + carpeta.getName() + "\".";
            return false;
        }

        Ecosistema eco = simulacion.getEcosistema();
        Estadisticas est = eco.getEstadisticas();

        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(archivo), StandardCharsets.UTF_8))) {

            out.println("# Simulador de Ecosistema - partida guardada");
            out.println("CONFIG" + SEP + simulacion.getTurnosTotales() + SEP + eco.getTurnoActual()
                    + SEP + eco.getClimaActual().name() + SEP + simulacion.isIntervencionUsada());
            out.println("IDS" + SEP + eco.getUltimoIdPlanta() + SEP + eco.getUltimoIdConejo()
                    + SEP + eco.getUltimoIdLobo());

            for (int i = 0; i < Estadisticas.TIPOS.length; i++) {
                out.println("TIPO" + SEP + Estadisticas.TIPOS[i] + SEP + est.getNacimientos(i)
                        + SEP + est.getMuertes(i) + SEP + est.getNombreMasLongeva(i)
                        + SEP + est.getEdadMasLongeva(i));
            }
            out.println("RECORDS" + SEP + est.getLoboMasCacerias() + SEP + est.getMaxCacerias()
                    + SEP + est.getTurnoMayorActividad() + SEP + est.getEventosMayorActividad());

            for (int[] fila : est.getHistorial()) {
                out.println("HISTORIAL" + SEP + fila[0] + SEP + fila[1] + SEP + fila[2] + SEP + fila[3]);
            }
            for (Planta p : eco.getPlantas()) {
                out.println("PLANTA" + SEP + p.getNombre() + SEP + p.getEnergia() + SEP + p.getEdad()
                        + SEP + p.getTamanio());
            }
            for (Conejo c : eco.getConejos()) {
                out.println("CONEJO" + SEP + c.getNombre() + SEP + c.getEnergia() + SEP + c.getEdad());
            }
            for (Lobo l : eco.getLobos()) {
                out.println("LOBO" + SEP + l.getNombre() + SEP + l.getEnergia() + SEP + l.getEdad()
                        + SEP + l.getExitosCaza());
            }
            for (String evento : eco.getEventosTurno()) {
                out.println("EVENTO" + SEP + evento);
            }

            if (out.checkError()) {
                ultimoError = "Ocurrió un error al escribir \"" + archivo.getName() + "\".";
                return false;
            }
            ultimoError = "";
            return true;

        } catch (IOException e) {
            ultimoError = "No se pudo escribir \"" + archivo.getName() + "\": " + e.getMessage();
            return false;
        }
    }

    // ===================== Leer =====================

    @Override
    public Simulacion cargar() {
        if (!existePartidaGuardada()) {
            ultimoError = "No hay ninguna simulación guardada.";
            return null;
        }

        int numeroLinea = 0;

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8))) {

            Ecosistema eco = null;
            int turnosTotales = 0;
            int turno = 0;
            boolean intervencionUsada = false;
            int[] ids = null;
            List<String> eventos = new ArrayList<>();

            String linea;
            while ((linea = in.readLine()) != null) {
                numeroLinea++;
                linea = linea.trim();

                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split(SEP, -1);

                if (eco == null && !partes[0].equals("CONFIG")) {
                    throw new IllegalArgumentException("falta la línea CONFIG al principio");
                }

                switch (partes[0]) {
                    case "CONFIG":
                        exigir(partes, 5);
                        turnosTotales = Integer.parseInt(partes[1]);
                        turno = Integer.parseInt(partes[2]);
                        eco = new Ecosistema(Clima.valueOf(partes[3]));
                        intervencionUsada = Boolean.parseBoolean(partes[4]);
                        break;
                    case "IDS":
                        exigir(partes, 4);
                        ids = new int[]{Integer.parseInt(partes[1]),
                            Integer.parseInt(partes[2]), Integer.parseInt(partes[3])};
                        break;
                    case "TIPO":
                        exigir(partes, 6);
                        eco.getEstadisticas().restaurarTipo(Estadisticas.indice(partes[1]),
                                Integer.parseInt(partes[2]), Integer.parseInt(partes[3]),
                                partes[4], Integer.parseInt(partes[5]));
                        break;
                    case "RECORDS":
                        exigir(partes, 5);
                        eco.getEstadisticas().restaurarRecords(partes[1],
                                Integer.parseInt(partes[2]), Integer.parseInt(partes[3]),
                                Integer.parseInt(partes[4]));
                        break;
                    case "HISTORIAL":
                        exigir(partes, 5);
                        eco.getEstadisticas().agregarRegistroHistorial(
                                Integer.parseInt(partes[1]), Integer.parseInt(partes[2]),
                                Integer.parseInt(partes[3]), Integer.parseInt(partes[4]));
                        break;
                    case "PLANTA":
                        exigir(partes, 5);
                        eco.agregarPlanta(new Planta(partes[1], Double.parseDouble(partes[2]),
                                Integer.parseInt(partes[3]), Integer.parseInt(partes[4])));
                        break;
                    case "CONEJO":
                        exigir(partes, 4);
                        eco.agregarConejo(new Conejo(partes[1], Double.parseDouble(partes[2]),
                                Integer.parseInt(partes[3])));
                        break;
                    case "LOBO":
                        exigir(partes, 5);
                        boolean agregado = eco.agregarLobo(new Lobo(partes[1],
                                Double.parseDouble(partes[2]), Integer.parseInt(partes[3]),
                                Integer.parseInt(partes[4])));
                        if (!agregado) {
                            throw new IllegalArgumentException("hay más de "
                                    + Ecosistema.MAX_LOBOS + " lobos");
                        }
                        break;
                    case "EVENTO":
                        eventos.add(linea.substring(("EVENTO" + SEP).length()));
                        break;
                    default:
                        throw new IllegalArgumentException("tipo de línea desconocido \""
                                + partes[0] + "\"");
                }
            }

            if (eco == null) {
                throw new IllegalArgumentException("el archivo está vacío");
            }
            if (ids == null) {
                throw new IllegalArgumentException("falta la línea IDS");
            }
            if (turnosTotales < 10 || turnosTotales > 50 || turno < 0 || turno > turnosTotales) {
                throw new IllegalArgumentException("los turnos guardados no son válidos");
            }

            eco.restaurarEstado(turno, ids[0], ids[1], ids[2], eventos);
            ultimoError = "";
            return new Simulacion(eco, turnosTotales, intervencionUsada);

        } catch (IOException e) {
            ultimoError = "No se pudo leer \"" + archivo.getName() + "\": " + e.getMessage();
            return null;
        } catch (IllegalArgumentException e) {
            // También atrapa NumberFormatException (números mal escritos)
            ultimoError = "El archivo \"" + archivo.getName() + "\" tiene datos inválidos (línea "
                    + numeroLinea + "): " + e.getMessage();
            return null;
        }
    }

    /** Verifica que la línea tenga la cantidad de valores esperada. */
    private void exigir(String[] partes, int cantidad) {
        if (partes.length != cantidad) {
            throw new IllegalArgumentException("se esperaban " + cantidad
                    + " valores en la línea " + partes[0] + " y hay " + partes.length);
        }
    }
}
