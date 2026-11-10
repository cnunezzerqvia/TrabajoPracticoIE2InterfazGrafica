package com.mycompany.trabajopractico.modelo;

/**
 * Clase base abstracta de todo lo que vive en el ecosistema.
 * Encapsula los datos comunes: nombre, energía, edad y si está viva.
 */
public abstract class Entidad {

    private String nombre;
    private double energia;
    private int edad;
    private boolean viva;

    public Entidad(String nombre, double energia) {
        this.nombre = nombre;
        this.energia = Math.max(0, energia);
        this.edad = 0;
        this.viva = this.energia > 0;
    }

    /**
     * Constructor usado por el DAO para volver a armar una entidad guardada
     * (con la edad que tenía al momento de guardar).
     */
    public Entidad(String nombre, double energia, int edad) {
        this(nombre, energia);
        this.edad = Math.max(0, edad);
    }

    /** Lo que hace la entidad en su turno (cada subclase lo resuelve distinto). */
    public abstract void actuar(Ecosistema eco);

    /** Tipo legible: "Planta", "Conejo" o "Lobo". */
    public abstract String getTipo();

    /**
     * Dato propio de cada subclase para mostrar en la tabla.
     * Reemplaza al mostrarEstado() de la IE1, que imprimía por consola.
     */
    public abstract String getDetalle();

    /** Cada turno la entidad cumple un turno más y gasta energía base. */
    public void envejecer() {
        edad++;
        energia -= 5;

        if (energia <= 0) {
            energia = 0;
            viva = false;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        }
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = Math.max(0, energia);

        if (this.energia <= 0) {
            viva = false;
        }
    }

    public int getEdad() {
        return edad;
    }

    public boolean isViva() {
        return viva;
    }

    public void setViva(boolean viva) {
        this.viva = viva;
    }
}
