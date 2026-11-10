package com.mycompany.trabajopractico.controlador;

import com.mycompany.trabajopractico.dao.SimulacionDAO;
import com.mycompany.trabajopractico.modelo.Clima;
import com.mycompany.trabajopractico.modelo.Conejo;
import com.mycompany.trabajopractico.modelo.Ecosistema;
import com.mycompany.trabajopractico.modelo.Entidad;
import com.mycompany.trabajopractico.modelo.Lobo;
import com.mycompany.trabajopractico.modelo.Planta;
import com.mycompany.trabajopractico.modelo.Simulacion;
import com.mycompany.trabajopractico.vista.IVistaEcosistema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Recibe las acciones de la vista, valida los datos ingresados, coordina al
 * modelo y al DAO, y le entrega a la vista los datos actualizados.
 * No tiene código Swing: habla con la vista a través de IVistaEcosistema.
 */
public class ControladorSimulacion {

    // Rangos de la configuración inicial (los mismos de la IE1)
    public static final int MIN_PLANTAS = 5;
    public static final int MAX_PLANTAS = 30;
    public static final int MIN_CONEJOS = 2;
    public static final int MAX_CONEJOS = 15;
    public static final int MIN_LOBOS = 1;
    public static final int MIN_TURNOS = 10;
    public static final int MAX_TURNOS = 50;

    private final IVistaEcosistema vista;
    private final SimulacionDAO dao;

    /** Simulación actual (null si todavía no hay ninguna). */
    private Simulacion simulacion;

    public ControladorSimulacion(IVistaEcosistema vista, SimulacionDAO dao) {
        this.vista = vista;
        this.dao = dao;
    }

    /** Se llama una sola vez, al abrir la aplicación. */
    public void iniciar() {
        vista.cargarClimas(nombresDeClimas());
        vista.cargarTiposEntidad(Arrays.asList(Planta.TIPO, Conejo.TIPO, Lobo.TIPO));

        // Si hay una partida guardada, se retoma donde estaba
        if (dao.existePartidaGuardada()) {
            Simulacion guardada = dao.cargar();

            if (guardada != null) {
                simulacion = guardada;
                actualizarVista();
                vista.mostrarPantallaSimulacion();
                vista.mostrarMensaje("Se retomó la simulación guardada (turno "
                        + guardada.getEcosistema().getTurnoActual() + " de "
                        + guardada.getTurnosTotales() + ").");
                return;
            }

            vista.mostrarError("No se pudo retomar la simulación guardada.\n"
                    + dao.getUltimoError() + "\nPodés configurar una simulación nueva.");
        }
        vista.mostrarPantallaConfiguracion(false);
    }

    // ===================== Configuración inicial =====================

    /**
     * La vista manda los textos tal cual los escribió la persona.
     * Acá se valida formato, rango y campos vacíos.
     */
    public void iniciarSimulacion(String textoPlantas, String textoConejos, String textoLobos,
            String nombreClima, String textoTurnos) {

        List<String> errores = new ArrayList<>();

        int plantas = validarEntero(textoPlantas, "Plantas", MIN_PLANTAS, MAX_PLANTAS, errores);
        int conejos = validarEntero(textoConejos, "Conejos", MIN_CONEJOS, MAX_CONEJOS, errores);
        int lobos = validarEntero(textoLobos, "Lobos", MIN_LOBOS, Ecosistema.MAX_LOBOS, errores);
        int turnos = validarEntero(textoTurnos, "Turnos", MIN_TURNOS, MAX_TURNOS, errores);

        Clima clima = Clima.desdeNombre(nombreClima);
        if (clima == null) {
            errores.add("Clima: elegí un clima de la lista.");
        }

        if (!errores.isEmpty()) {
            vista.mostrarErroresConfiguracion(errores);
            return;
        }

        vista.limpiarErroresConfiguracion();

        Ecosistema ecosistema = new Ecosistema(clima);
        ecosistema.poblar(plantas, conejos, lobos);
        simulacion = new Simulacion(ecosistema, turnos);

        guardar();
        actualizarVista();
        vista.mostrarPantallaSimulacion();
        vista.mostrarMensaje("Simulación creada. Presioná \"Avanzar turno\" para empezar.");
    }

    /** Devuelve el número o -1 si hay error (y agrega el mensaje a la lista). */
    private int validarEntero(String texto, String campo, int minimo, int maximo,
            List<String> errores) {

        if (texto == null || texto.trim().isEmpty()) {
            errores.add(campo + ": el campo está vacío.");
            return -1;
        }

        try {
            int valor = Integer.parseInt(texto.trim());

            if (valor < minimo || valor > maximo) {
                errores.add(campo + ": tiene que estar entre " + minimo + " y " + maximo
                        + " (ingresaste " + valor + ").");
                return -1;
            }
            return valor;

        } catch (NumberFormatException e) {
            errores.add(campo + ": \"" + texto.trim() + "\" no es un número entero.");
            return -1;
        }
    }

    private List<String> nombresDeClimas() {
        List<String> nombres = new ArrayList<>();
        for (Clima clima : Clima.values()) {
            nombres.add(clima.getNombre());
        }
        return nombres;
    }

    // ===================== Turnos =====================

    public void avanzarTurno() {
        if (simulacion == null) {
            vista.mostrarError("Todavía no hay una simulación en curso. Configurá una para empezar.");
            return;
        }
        if (simulacion.estaTerminada()) {
            vista.mostrarMensaje("La simulación ya terminó. " + simulacion.getCausaFin());
            return;
        }

        List<String> eventos = simulacion.avanzarTurno();
        guardar();
        actualizarVista();
        vista.mostrarMensaje("Turno " + simulacion.getEcosistema().getTurnoActual()
                + " procesado: " + eventos.size() + " eventos.");

        if (simulacion.estaTerminada()) {
            vista.mostrarMensaje("Fin de la simulación. " + simulacion.getCausaFin());
        }
    }

    // ===================== Intervención del jugador =====================

    public void cambiarClima(String nombreClima) {
        if (!validarIntervencion()) {
            return;
        }

        Clima nuevo = Clima.desdeNombre(nombreClima);
        if (nuevo == null) {
            vista.mostrarError("Elegí un clima de la lista.");
            return;
        }
        if (nuevo == simulacion.getEcosistema().getClimaActual()) {
            vista.mostrarError("El clima ya es " + nuevo.getNombre() + ". Elegí uno distinto.");
            return;
        }

        simulacion.cambiarClima(nuevo);
        guardar();
        actualizarVista();
        vista.mostrarMensaje("El clima cambió a " + nuevo.getNombre() + ".");
    }

    public void agregarEntidad(String tipo) {
        if (!validarIntervencion()) {
            return;
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            vista.mostrarError("Elegí qué tipo de entidad querés agregar.");
            return;
        }
        if (Lobo.TIPO.equals(tipo) && !simulacion.getEcosistema().puedeAgregarLobo()) {
            vista.mostrarError("Ya hay " + Ecosistema.MAX_LOBOS
                    + " lobos en el ecosistema: es el máximo permitido.");
            return;
        }
        if (!simulacion.agregarEntidad(tipo)) {
            vista.mostrarError("No se pudo agregar la entidad \"" + tipo + "\".");
            return;
        }

        guardar();
        actualizarVista();
        vista.mostrarMensaje("Se agregó una entidad de tipo " + tipo + ".");
    }

    private boolean validarIntervencion() {
        if (simulacion == null) {
            vista.mostrarError("Todavía no hay una simulación en curso.");
            return false;
        }
        if (!simulacion.puedeIntervenir()) {
            vista.mostrarError(mensajeIntervencion());
            return false;
        }
        return true;
    }

    private String mensajeIntervencion() {
        if (simulacion.estaTerminada()) {
            return "La simulación terminó: ya no se puede intervenir.";
        }
        if (simulacion.puedeIntervenir()) {
            return "¡Podés intervenir! Elegí UNA acción (cambiar el clima o agregar una entidad) "
                    + "o simplemente avanzá.";
        }
        if (simulacion.esTurnoDeIntervencion()) {
            return "Ya usaste la intervención de este turno. Avanzá para continuar.";
        }
        int faltan = simulacion.turnosHastaIntervencion();
        return "Podés intervenir cada 3 turnos. Próxima intervención en " + faltan
                + (faltan == 1 ? " turno." : " turnos.");
    }

    // ===================== Nueva / guardar / salir =====================

    public void nuevaSimulacion() {
        if (simulacion != null && !simulacion.estaTerminada()) {
            boolean confirma = vista.confirmar("Hay una simulación en curso (turno "
                    + simulacion.getEcosistema().getTurnoActual() + " de "
                    + simulacion.getTurnosTotales() + ").\n"
                    + "Si empezás una nueva, la actual se reemplaza al confirmar la configuración.\n"
                    + "¿Querés continuar?");
            if (!confirma) {
                return;
            }
        }
        vista.limpiarErroresConfiguracion();
        vista.mostrarPantallaConfiguracion(simulacion != null);
    }

    public void volverASimulacion() {
        if (simulacion == null) {
            vista.mostrarError("Todavía no hay una simulación en curso.");
            return;
        }
        vista.mostrarPantallaSimulacion();
    }

    public void guardarAhora() {
        if (simulacion == null) {
            vista.mostrarError("Todavía no hay una simulación para guardar.");
            return;
        }
        if (guardar()) {
            vista.mostrarMensaje("Simulación guardada correctamente.");
        }
    }

    public void salir() {
        if (simulacion != null) {
            guardar();
        }
        vista.cerrar();
    }

    // ===================== Comunes =====================

    /** Guarda con el DAO; si falla, avisa a la vista. */
    private boolean guardar() {
        if (dao.guardar(simulacion)) {
            return true;
        }
        vista.mostrarError("No se pudo guardar la simulación.\n" + dao.getUltimoError());
        return false;
    }

    /** Le entrega a la vista todos los datos actuales para que la pantalla esté al día. */
    private void actualizarVista() {
        Ecosistema eco = simulacion.getEcosistema();

        vista.actualizarContadores(eco.getTurnoActual(), simulacion.getTurnosTotales(),
                eco.getClimaActual().getNombre(), eco.getPlantas().size(),
                eco.getConejos().size(), eco.getLobos().size());
        vista.actualizarTablaEntidades(armarFilasEntidades(eco));
        vista.actualizarEventos(eco.getTurnoActual(), eco.getEventosTurno());
        vista.habilitarAvance(!simulacion.estaTerminada());
        vista.actualizarIntervencion(simulacion.puedeIntervenir(), mensajeIntervencion());
    }

    /** Convierte las entidades del modelo en filas simples para la tabla. */
    private List<Object[]> armarFilasEntidades(Ecosistema eco) {
        List<Object[]> filas = new ArrayList<>();
        for (Entidad entidad : eco.getEntidades()) {
            filas.add(new Object[]{
                entidad.getTipo(),
                entidad.getNombre(),
                (int) Math.round(entidad.getEnergia()),
                entidad.getEdad(),
                entidad.getDetalle()
            });
        }
        return filas;
    }
}
