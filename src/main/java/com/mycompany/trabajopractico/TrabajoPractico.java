package com.mycompany.trabajopractico;

import com.mycompany.trabajopractico.modelo.Clima;
import com.mycompany.trabajopractico.modelo.Ecosistema;
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

        for (int turno = 1; turno <= 10 && !eco.estaColapsado(); turno++) {
            System.out.println("===== Turno " + turno + " =====");
            imprimir(eco.procesarTurno());
            System.out.println("Plantas: " + eco.getPlantas().size() + " | Conejos: "
                    + eco.getConejos().size() + " | Lobos: " + eco.getLobos().size());
        }
    }

    private static void imprimir(List<String> eventos) {
        for (String evento : eventos) {
            System.out.println("  - " + evento);
        }
    }
}
