package com.mycompany.trabajopractico.modelo;

/**
 * Climas posibles del ecosistema (se mantienen los 4 de la IE1).
 * Cada clima tiene un nombre legible para mostrar en pantalla.
 */
public enum Clima {

    SOLEADO("Soleado"),
    LLUVIOSO("Lluvioso"),
    SEQUIA("Sequía"),
    INVIERNO("Invierno");

    private final String nombre;

    Clima(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Busca un clima a partir de su nombre legible (por ejemplo "Sequía").
     * Devuelve null si el texto no corresponde a ningún clima.
     */
    public static Clima desdeNombre(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Clima clima : values()) {
            if (clima.nombre.equalsIgnoreCase(nombre.trim())
                    || clima.name().equalsIgnoreCase(nombre.trim())) {
                return clima;
            }
        }
        return null;
    }
}
