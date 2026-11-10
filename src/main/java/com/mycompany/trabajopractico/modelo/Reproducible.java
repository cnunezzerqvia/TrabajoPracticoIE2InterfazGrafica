package com.mycompany.trabajopractico.modelo;

/**
 * Comportamiento de lo que puede reproducirse (plantas y conejos).
 */
public interface Reproducible {

    void reproducirse(Ecosistema eco);

    boolean puedeReproducirse();

    /** Método default: solo se reproduce si cumple la condición. */
    default void intentarReproduccion(Ecosistema eco) {
        if (puedeReproducirse()) {
            reproducirse(eco);
        }
    }
}
