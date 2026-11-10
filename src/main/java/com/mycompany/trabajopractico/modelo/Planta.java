package com.mycompany.trabajopractico.modelo;

import java.util.Random;

/**
 * Planta: se reproduce si tiene más de 50 de energía y sirve de alimento
 * a los conejos (aporta tamaño x 10 de energía).
 */
public class Planta extends Entidad implements Reproducible {

    public static final String TIPO = "Planta";

    private int tamanio;
    private final Random random = new Random();

    public Planta(String nombre, double energia, int tamanio) {
        super(nombre, energia);
        this.tamanio = limitarTamanio(tamanio);
    }

    private int limitarTamanio(int valor) {
        if (valor < 1) {
            return 1;
        }
        if (valor > 5) {
            return 5;
        }
        return valor;
    }

    @Override
    public void actuar(Ecosistema eco) {
        intentarReproduccion(eco);
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getDetalle() {
        return "Tamaño " + tamanio + " (+" + (tamanio * 10) + " al ser comida)";
    }

    /** La planta muere al ser comida y devuelve la energía que aporta. */
    public int serComida() {
        int valorNutritivo = tamanio * 10;

        setEnergia(0);
        setViva(false);

        return valorNutritivo;
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() > 50;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        Planta nueva = new Planta(
                eco.generarNombre(TIPO),
                40,
                random.nextInt(5) + 1
        );

        eco.registrarNacimiento(nueva, getNombre());
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        if (tamanio >= 1 && tamanio <= 5) {
            this.tamanio = tamanio;
        }
    }
}
