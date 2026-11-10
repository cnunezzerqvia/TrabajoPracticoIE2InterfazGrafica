package com.mycompany.trabajopractico.modelo;

/**
 * Conejo: busca plantas para comer (si no encuentra pierde 15 de energía)
 * y se reproduce cuando tiene más de 60 de energía.
 */
public class Conejo extends Animal implements Reproducible {

    public static final String TIPO = "Conejo";

    public Conejo(String nombre, double energia) {
        super(nombre, energia, 5, 3.0);
    }

    /** Constructor para reconstruir un conejo guardado. */
    public Conejo(String nombre, double energia, int edad) {
        super(nombre, energia, edad, 5, 3.0);
    }

    @Override
    public void actuar(Ecosistema eco) {
        if (!estaVivo()) {
            return;
        }

        comer(eco);

        if (estaVivo()) {
            intentarReproduccion(eco);
        }
    }

    @Override
    public void comer(Ecosistema eco) {
        Planta planta = eco.obtenerPlantaViva();

        if (planta != null) {
            int energiaGanada = planta.serComida();
            setEnergia(getEnergia() + energiaGanada);

            eco.registrarDepredacion(planta,
                    getNombre() + " comió a " + planta.getNombre()
                    + " (+" + energiaGanada + " de energía)");
        } else {
            setEnergia(getEnergia() - 15);

            eco.registrarEvento(getNombre()
                    + " no encontró plantas para comer (-15 de energía)");
        }
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public String getDetalle() {
        if (getEnergia() < 20) {
            return "¡En peligro! (poca energía)";
        }
        return "Velocidad " + getVelocidad();
    }

    @Override
    public boolean puedeReproducirse() {
        return estaVivo() && getEnergia() > 60;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        Conejo cria = new Conejo(eco.generarNombre(TIPO), 35);

        setEnergia(getEnergia() - 20);

        eco.registrarNacimiento(cria, getNombre());
    }
}
