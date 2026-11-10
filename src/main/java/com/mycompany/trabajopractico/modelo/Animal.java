package com.mycompany.trabajopractico.modelo;

/**
 * Clase abstracta intermedia: todo animal es una Entidad que además es Mortal
 * y sabe comer. Conejo y Lobo la extienden.
 */
public abstract class Animal extends Entidad implements Mortal {

    private int velocidad;
    private double peso;

    public Animal(String nombre, double energia, int velocidad, double peso) {
        super(nombre, energia);
        this.velocidad = velocidad;
        this.peso = peso;
    }

    /** Constructor para reconstruir un animal guardado (con su edad). */
    public Animal(String nombre, double energia, int edad, int velocidad, double peso) {
        super(nombre, energia, edad);
        this.velocidad = velocidad;
        this.peso = peso;
    }

    public abstract void comer(Ecosistema eco);

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        if (velocidad >= 0) {
            this.velocidad = velocidad;
        }
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        if (peso >= 0) {
            this.peso = peso;
        }
    }

    @Override
    public boolean estaVivo() {
        return isViva();
    }

    @Override
    public void morir() {
        setViva(false);
        setEnergia(0);
    }

    /** Si se quedó sin energía, muere (el ecosistema registra el evento). */
    @Override
    public void verificarMuerte() {
        if (getEnergia() <= 0 && estaVivo()) {
            morir();
        }
    }
}
