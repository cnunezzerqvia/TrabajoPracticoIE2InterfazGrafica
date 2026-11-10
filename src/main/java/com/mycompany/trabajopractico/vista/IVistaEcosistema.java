package com.mycompany.trabajopractico.vista;

import java.util.List;

/**
 * Lo que el controlador le puede pedir a la vista.
 * Solo usa tipos simples (String, int, listas): el controlador no conoce
 * ningún componente Swing y la vista no conoce el modelo.
 */
public interface IVistaEcosistema {

    // ----- Datos para los combos -----
    void cargarClimas(List<String> climas);

    void cargarTiposEntidad(List<String> tipos);

    // ----- Navegación -----
    void mostrarPantallaConfiguracion(boolean puedeVolver);

    void mostrarPantallaSimulacion();

    // ----- Configuración -----
    void mostrarErroresConfiguracion(List<String> errores);

    void limpiarErroresConfiguracion();

    // ----- Simulación en curso -----
    void actualizarContadores(int turno, int turnosTotales, String clima,
            int plantas, int conejos, int lobos);

    void actualizarTablaEntidades(List<Object[]> filas);

    void actualizarEventos(int turno, List<String> eventos);

    void habilitarAvance(boolean habilitado);

    void actualizarIntervencion(boolean habilitada, String mensaje);

    // ----- Mensajes -----
    void mostrarMensaje(String mensaje);

    void mostrarError(String mensaje);

    boolean confirmar(String mensaje);

    void cerrar();
}
