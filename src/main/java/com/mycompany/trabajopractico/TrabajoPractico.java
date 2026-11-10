package com.mycompany.trabajopractico;

import com.mycompany.trabajopractico.dao.SimulacionDAO;
import com.mycompany.trabajopractico.dao.SimulacionDAOTxt;
import com.mycompany.trabajopractico.modelo.Clima;
import com.mycompany.trabajopractico.modelo.Ecosistema;
import com.mycompany.trabajopractico.modelo.Estadisticas;
import com.mycompany.trabajopractico.modelo.Simulacion;
import java.util.List;

/**
 * PRUEBA TEMPORAL por consola del modelo refactorizado.
 * Se va a reemplazar por la ventana Swing cuando esté la vista.
 * (Los System.out están acá, en el paquete principal, NO en el modelo).
 */
public class TrabajoPractico {

    public static void main(String[] args) {
        Ecosistema eco = new Ecosistema(Clima.SOLEADO);
        eco.poblar(10, 5, 2);
        imprimir(eco.getEventosTurno());

        Simulacion simulacion = new Simulacion(eco, 12);
        while (!simulacion.estaTerminada()) {
            System.out.println("===== Turno " + (eco.getTurnoActual() + 1) + " =====");
            imprimir(simulacion.avanzarTurno());
            System.out.println("Plantas: " + eco.getPlantas().size() + " | Conejos: "
                    + eco.getConejos().size() + " | Lobos: " + eco.getLobos().size());
        }

        System.out.println();
        System.out.println(simulacion.getCausaFin());
        Estadisticas est = eco.getEstadisticas();
        for (int i = 0; i < Estadisticas.TIPOS.length; i++) {
            System.out.println(Estadisticas.TIPOS[i] + ": nacimientos=" + est.getNacimientos(i)
                    + ", muertes=" + est.getMuertes(i) + ", más longeva="
                    + est.getNombreMasLongeva(i) + " (" + est.getEdadMasLongeva(i) + ")");
        }
        System.out.println("Lobo con más cacerías: " + est.getLoboMasCacerias()
                + " (" + est.getMaxCacerias() + ")");
        System.out.println("Turno de mayor actividad: " + est.getTurnoMayorActividad());

        SimulacionDAO dao = new SimulacionDAOTxt("datos/simulacion.txt");
        System.out.println("¿Se guardó? " + dao.guardar(simulacion));
        Simulacion cargada = dao.cargar();
        if (cargada == null) {
            System.out.println("Error al cargar: " + dao.getUltimoError());
        } else {
            System.out.println("Cargada OK: turno " + cargada.getEcosistema().getTurnoActual()
                    + ", " + cargada.getEcosistema().getEntidades().size() + " entidades vivas.");
        }
    }

    private static void imprimir(List<String> eventos) {
        for (String evento : eventos) {
            System.out.println("  - " + evento);
        }
    }
}
