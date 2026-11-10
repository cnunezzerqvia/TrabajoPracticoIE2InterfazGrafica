package com.mycompany.trabajopractico.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Contiene las tres poblaciones y aplica las reglas de cada turno.
 *
 * No interactúa con el usuario (sin Scanner, sin System.out, sin Swing):
 * lo que pasa en el turno queda guardado en una lista de eventos para que
 * el controlador la pida y la vista la muestre.
 */
public class Ecosistema {

    public static final int MAX_LOBOS = 5;

    private final List<Planta> plantas = new ArrayList<>();
    private final List<Conejo> conejos = new ArrayList<>();
    private final List<Lobo> lobos = new ArrayList<>();

    private Clima climaActual;
    private int turnoActual;

    /** Eventos legibles del último turno (quién comió a quién, quién nació, quién murió). */
    private final List<String> eventosTurno = new ArrayList<>();

    /** Entidades que murieron comidas/cazadas en este turno (para no repetir el evento). */
    private final Set<Entidad> presasDelTurno = new HashSet<>();

    /** Contadores para que los nombres no se repitan aunque mueran entidades. */
    private int ultimoIdPlanta;
    private int ultimoIdConejo;
    private int ultimoIdLobo;

    private final Random random = new Random();

    public Ecosistema(Clima clima) {
        this.climaActual = clima;
        this.turnoActual = 0;
    }

    // ===================== Armado inicial =====================

    /** Crea las poblaciones iniciales con energía aleatoria entre 40 y 80. */
    public void poblar(int cantidadPlantas, int cantidadConejos, int cantidadLobos) {
        for (int i = 0; i < cantidadPlantas; i++) {
            agregarPlanta(new Planta(generarNombre(Planta.TIPO),
                    energiaInicial(), 1 + random.nextInt(5)));
        }
        for (int i = 0; i < cantidadConejos; i++) {
            agregarConejo(new Conejo(generarNombre(Conejo.TIPO), energiaInicial()));
        }
        for (int i = 0; i < cantidadLobos; i++) {
            agregarLobo(new Lobo(generarNombre(Lobo.TIPO), energiaInicial()));
        }

        eventosTurno.clear();
        registrarEvento("Se creó el ecosistema con " + plantas.size() + " plantas, "
                + conejos.size() + " conejos y " + lobos.size() + " lobos. Clima: "
                + climaActual.getNombre() + ".");
    }

    private double energiaInicial() {
        return 40 + random.nextInt(41);
    }

    /** Devuelve un nombre único para el tipo pedido (Planta-1, Planta-2, ...). */
    public String generarNombre(String tipo) {
        switch (tipo) {
            case Planta.TIPO:
                ultimoIdPlanta++;
                return tipo + "-" + ultimoIdPlanta;
            case Conejo.TIPO:
                ultimoIdConejo++;
                return tipo + "-" + ultimoIdConejo;
            default:
                ultimoIdLobo++;
                return tipo + "-" + ultimoIdLobo;
        }
    }

    public void agregarPlanta(Planta planta) {
        plantas.add(planta);
    }

    public void agregarConejo(Conejo conejo) {
        conejos.add(conejo);
    }

    /** Agrega un lobo solo si no se supera el máximo de 5. */
    public boolean agregarLobo(Lobo lobo) {
        if (!puedeAgregarLobo()) {
            return false;
        }
        lobos.add(lobo);
        return true;
    }

    public boolean puedeAgregarLobo() {
        return lobos.size() < MAX_LOBOS;
    }

    // ===================== Intervención del jugador =====================

    /** Agrega una entidad elegida por el jugador. Devuelve false si no se pudo. */
    public boolean agregarEntidadJugador(String tipo) {
        Entidad nueva;

        switch (tipo) {
            case Planta.TIPO:
                Planta planta = new Planta(generarNombre(tipo), energiaInicial(),
                        1 + random.nextInt(5));
                agregarPlanta(planta);
                nueva = planta;
                break;
            case Conejo.TIPO:
                Conejo conejo = new Conejo(generarNombre(tipo), energiaInicial());
                agregarConejo(conejo);
                nueva = conejo;
                break;
            case Lobo.TIPO:
                if (!puedeAgregarLobo()) {
                    return false;
                }
                Lobo lobo = new Lobo(generarNombre(tipo), energiaInicial());
                agregarLobo(lobo);
                nueva = lobo;
                break;
            default:
                return false;
        }

        registrarEvento("El jugador agregó a " + nueva.getNombre() + " al ecosistema.");
        return true;
    }

    public void cambiarClima(Clima nuevoClima) {
        climaActual = nuevoClima;
        registrarEvento("El jugador cambió el clima a " + nuevoClima.getNombre() + ".");
    }

    // ===================== Turno =====================

    /**
     * Procesa un turno completo en el orden de la consigna y devuelve
     * la lista de eventos que ocurrieron.
     */
    public List<String> procesarTurno() {
        turnoActual++;
        eventosTurno.clear();
        presasDelTurno.clear();

        // 1. Plantas: con suficiente energía se reproducen
        for (Planta planta : new ArrayList<>(plantas)) {
            if (planta.isViva()) {
                aplicarClimaPlanta(planta);
                planta.actuar(this);
            }
        }

        // 2. Conejos: buscan plantas y comen
        for (Conejo conejo : new ArrayList<>(conejos)) {
            if (conejo.estaVivo()) {
                aplicarClimaConejo(conejo);
                conejo.actuar(this);
            }
        }

        // 3. Lobos: intentan cazar
        for (Lobo lobo : new ArrayList<>(lobos)) {
            if (lobo.estaVivo()) {
                aplicarClimaLobo(lobo);
                lobo.actuar(this);
            }
        }

        // 4. Todas envejecen y gastan energía base
        envejecerEntidades();

        // 5. Las entidades sin energía mueren
        eliminarMuertos(plantas);
        eliminarMuertos(conejos);
        eliminarMuertos(lobos);

        return getEventosTurno();
    }

    private void aplicarClimaPlanta(Planta planta) {
        switch (climaActual) {
            case LLUVIOSO:
                planta.setEnergia(planta.getEnergia() + 5);
                break;
            case SEQUIA:
                planta.setEnergia(planta.getEnergia() - 5);
                break;
            default:
                break;
        }
    }

    private void aplicarClimaConejo(Conejo conejo) {
        switch (climaActual) {
            case SOLEADO:
                conejo.setEnergia(conejo.getEnergia() + 5);
                break;
            case LLUVIOSO:
                conejo.setEnergia(conejo.getEnergia() + 3);
                break;
            case SEQUIA:
                conejo.setEnergia(conejo.getEnergia() - 5);
                break;
            case INVIERNO:
                conejo.setEnergia(conejo.getEnergia() - 8);
                break;
        }
    }

    private void aplicarClimaLobo(Lobo lobo) {
        if (climaActual == Clima.LLUVIOSO) {
            lobo.setEnergia(lobo.getEnergia() - 5);
        }
    }

    private void envejecerEntidades() {
        for (Planta planta : plantas) {
            if (planta.isViva()) {
                planta.envejecer();
            }
        }
        for (Conejo conejo : conejos) {
            if (conejo.estaVivo()) {
                conejo.envejecer();
                conejo.verificarMuerte();
            }
        }
        for (Lobo lobo : lobos) {
            if (lobo.estaVivo()) {
                lobo.envejecer();
                lobo.verificarMuerte();
            }
        }
    }

    /** Saca de la lista a los muertos y registra el evento si murieron de hambre. */
    private void eliminarMuertos(List<? extends Entidad> lista) {
        Iterator<? extends Entidad> it = lista.iterator();

        while (it.hasNext()) {
            Entidad entidad = it.next();

            if (!entidad.isViva()) {
                if (!presasDelTurno.contains(entidad)) {
                    registrarEvento(entidad.getNombre() + " murió por falta de energía.");
                }
                it.remove();
            }
        }
    }

    // ===================== Registro de eventos =====================

    public void registrarEvento(String evento) {
        eventosTurno.add(evento);
    }

    /** Lo llaman Conejo y Lobo cuando comen/cazan a otra entidad. */
    public void registrarDepredacion(Entidad presa, String descripcion) {
        presasDelTurno.add(presa);
        registrarEvento(descripcion);
    }

    /** Lo llaman Planta y Conejo cuando se reproducen. */
    public void registrarNacimiento(Entidad cria, String nombrePadre) {
        if (cria instanceof Planta) {
            plantas.add((Planta) cria);
        } else if (cria instanceof Conejo) {
            conejos.add((Conejo) cria);
        } else {
            return;
        }
        registrarEvento(nombrePadre + " se reprodujo: nació " + cria.getNombre() + ".");
    }

    // ===================== Búsquedas =====================

    public Planta obtenerPlantaViva() {
        List<Planta> vivas = new ArrayList<>();
        for (Planta p : plantas) {
            if (p.isViva()) {
                vivas.add(p);
            }
        }
        if (vivas.isEmpty()) {
            return null;
        }
        return vivas.get(random.nextInt(vivas.size()));
    }

    public Conejo obtenerConejoVivo() {
        List<Conejo> vivos = new ArrayList<>();
        for (Conejo c : conejos) {
            if (c.estaVivo()) {
                vivos.add(c);
            }
        }
        if (vivos.isEmpty()) {
            return null;
        }
        return vivos.get(random.nextInt(vivos.size()));
    }

    /** Colapso: se extinguió alguna de las tres poblaciones. */
    public boolean estaColapsado() {
        return plantas.isEmpty() || conejos.isEmpty() || lobos.isEmpty();
    }

    // ===================== Getters =====================

    public Clima getClimaActual() {
        return climaActual;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    /** Las listas se devuelven de solo lectura para cuidar el encapsulamiento. */
    public List<Planta> getPlantas() {
        return Collections.unmodifiableList(plantas);
    }

    public List<Conejo> getConejos() {
        return Collections.unmodifiableList(conejos);
    }

    public List<Lobo> getLobos() {
        return Collections.unmodifiableList(lobos);
    }

    /** Todas las entidades vivas juntas (polimorfismo: se tratan como Entidad). */
    public List<Entidad> getEntidades() {
        List<Entidad> todas = new ArrayList<>();
        todas.addAll(plantas);
        todas.addAll(conejos);
        todas.addAll(lobos);
        return todas;
    }

    public List<String> getEventosTurno() {
        return new ArrayList<>(eventosTurno);
    }
}
