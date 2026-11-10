package com.mycompany.trabajopractico.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Una partida: el ecosistema más la configuración de turnos.
 * Aplica las reglas de fin de la simulación y de intervención cada 3 turnos.
 */
public class Simulacion {

    public static final int INTERVALO_INTERVENCION = 3;

    private final Ecosistema ecosistema;
    private final int turnosTotales;

    /** true si el jugador ya usó la intervención del turno actual. */
    private boolean intervencionUsada;

    public Simulacion(Ecosistema ecosistema, int turnosTotales) {
        this(ecosistema, turnosTotales, false);
    }

    public Simulacion(Ecosistema ecosistema, int turnosTotales, boolean intervencionUsada) {
        this.ecosistema = ecosistema;
        this.turnosTotales = turnosTotales;
        this.intervencionUsada = intervencionUsada;
    }

    /** Avanza un turno si la simulación no terminó. */
    public List<String> avanzarTurno() {
        if (estaTerminada()) {
            return new ArrayList<>();
        }
        intervencionUsada = false;
        return ecosistema.procesarTurno();
    }

    /** Termina por completar los turnos o por colapso del ecosistema. */
    public boolean estaTerminada() {
        return ecosistema.getTurnoActual() >= turnosTotales || ecosistema.estaColapsado();
    }

    /** Turnos 3, 6, 9... (mientras la simulación siga en curso). */
    public boolean esTurnoDeIntervencion() {
        int turno = ecosistema.getTurnoActual();
        return turno > 0 && turno % INTERVALO_INTERVENCION == 0 && !estaTerminada();
    }

    /** Se puede intervenir una sola vez por turno de intervención. */
    public boolean puedeIntervenir() {
        return esTurnoDeIntervencion() && !intervencionUsada;
    }

    public int turnosHastaIntervencion() {
        int resto = ecosistema.getTurnoActual() % INTERVALO_INTERVENCION;
        return INTERVALO_INTERVENCION - resto;
    }

    public boolean cambiarClima(Clima nuevoClima) {
        if (!puedeIntervenir() || nuevoClima == null) {
            return false;
        }
        ecosistema.cambiarClima(nuevoClima);
        intervencionUsada = true;
        return true;
    }

    public boolean agregarEntidad(String tipo) {
        if (!puedeIntervenir()) {
            return false;
        }
        boolean agregada = ecosistema.agregarEntidadJugador(tipo);
        if (agregada) {
            intervencionUsada = true;
        }
        return agregada;
    }

    /** Texto con la causa de fin (turnos completos o qué población se extinguió). */
    public String getCausaFin() {
        if (!estaTerminada()) {
            return "La simulación sigue en curso.";
        }

        List<String> extinguidas = new ArrayList<>();
        if (ecosistema.getPlantas().isEmpty()) {
            extinguidas.add("las plantas");
        }
        if (ecosistema.getConejos().isEmpty()) {
            extinguidas.add("los conejos");
        }
        if (ecosistema.getLobos().isEmpty()) {
            extinguidas.add("los lobos");
        }

        if (!extinguidas.isEmpty()) {
            return "Colapso del ecosistema: se extinguieron " + String.join(" y ", extinguidas)
                    + " (turno " + ecosistema.getTurnoActual() + ").";
        }
        return "Se completaron los " + turnosTotales + " turnos configurados.";
    }

    public Ecosistema getEcosistema() {
        return ecosistema;
    }

    public int getTurnosTotales() {
        return turnosTotales;
    }

    public boolean isIntervencionUsada() {
        return intervencionUsada;
    }
}
