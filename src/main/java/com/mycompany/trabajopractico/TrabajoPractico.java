package com.mycompany.trabajopractico;

import com.mycompany.trabajopractico.controlador.ControladorSimulacion;
import com.mycompany.trabajopractico.dao.SimulacionDAO;
import com.mycompany.trabajopractico.dao.SimulacionDAOTxt;
import com.mycompany.trabajopractico.vista.VentanaPrincipal;

/**
 * Paquete principal: crea las piezas (vista, DAO y controlador),
 * las conecta y muestra la ventana.
 */
public class TrabajoPractico {

    /** Ruta RELATIVA a la carpeta del proyecto (NetBeans ejecuta desde ahí). */
    private static final String ARCHIVO_PARTIDA = "datos/simulacion.txt";

    public static void main(String[] args) {
        // Look and Feel Nimbus (el mismo que propone NetBeans en sus plantillas)
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // Si Nimbus no está disponible se usa el aspecto por defecto
        }

        java.awt.EventQueue.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            SimulacionDAO dao = new SimulacionDAOTxt(ARCHIVO_PARTIDA);
            ControladorSimulacion controlador = new ControladorSimulacion(ventana, dao);

            ventana.setControlador(controlador);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);

            controlador.iniciar();
        });
    }
}
