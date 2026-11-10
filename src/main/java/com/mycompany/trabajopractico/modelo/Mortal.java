package com.mycompany.trabajopractico.modelo;

/**
 * Comportamiento de lo que puede morir (lo implementan los animales).
 */
public interface Mortal {

    boolean estaVivo();

    void morir();

    /** Por defecto no hace nada; Animal la sobrescribe. */
    default void verificarMuerte() {
        if (!estaVivo()) {
            return;
        }
    }
}
