package com.mycompany.trabajopractico.dao;

import com.mycompany.trabajopractico.modelo.Simulacion;

/**
 * Operaciones de persistencia de la simulación.
 * El DAO no muestra mensajes: informa el resultado (true/false o null)
 * y deja el detalle del error en getUltimoError() para que el controlador decida.
 */
public interface SimulacionDAO {

    /** Guarda la simulación. Devuelve true si pudo guardar. */
    boolean guardar(Simulacion simulacion);

    /** Lee la simulación guardada. Devuelve null si no existe o tiene datos inválidos. */
    Simulacion cargar();

    boolean existePartidaGuardada();

    /** Descripción del último error de guardado o lectura. */
    String getUltimoError();
}
