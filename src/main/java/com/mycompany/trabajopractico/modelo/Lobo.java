package com.mycompany.trabajopractico.modelo;

import java.util.Random;

/**
 * Lobo: intenta cazar un conejo con una probabilidad que depende de su energía
 * (máximo 90 %, +20 % en invierno). Cuenta sus cacerías exitosas.
 */
public class Lobo extends Animal {

    public static final String TIPO = "Lobo";

    private int exitosCaza;
    private final Random random = new Random();

    public Lobo(String nombre, double energia) {
        super(nombre, energia, 8, 35.0);
        this.exitosCaza = 0;
    }

    @Override
    public void actuar(Ecosistema eco) {
        if (!estaVivo()) {
            return;
        }

        comer(eco);
    }

    @Override
    public void comer(Ecosistema eco) {
        Conejo conejo = eco.obtenerConejoVivo();

        if (conejo == null) {
            eco.registrarEvento(getNombre() + " no encontró conejos para cazar");
            return;
        }

        double probabilidad = getEnergia() / 100.0;

        if (probabilidad > 0.90) {
            probabilidad = 0.90;
        }

        if (eco.getClimaActual() == Clima.INVIERNO) {
            probabilidad += 0.20;

            if (probabilidad > 1) {
                probabilidad = 1;
            }
        }

        if (random.nextDouble() < probabilidad) {
            conejo.morir();
            setEnergia(getEnergia() + 30);
            exitosCaza++;

            eco.registrarDepredacion(conejo,
                    getNombre() + " cazó a " + conejo.getNombre()
                    + " (+30 de energía, cacería n.º " + exitosCaza + ")");
        } else {
            setEnergia(getEnergia() - 5);

            eco.registrarEvento(getNombre() + " intentó cazar a "
                    + conejo.getNombre() + " pero falló (-5 de energía)");
        }
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getDetalle() {
        return "Cacerías exitosas: " + exitosCaza;
    }

    public int getExitosCaza() {
        return exitosCaza;
    }
}
