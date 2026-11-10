package com.mycompany.trabajopractico.vista;

import com.mycompany.trabajopractico.controlador.ControladorSimulacion;
import java.awt.CardLayout;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana principal. Los componentes se arman con el diseñador visual de
 * NetBeans (archivo VentanaPrincipal.form). Las pantallas son "cartas" de un
 * CardLayout.
 *
 * La vista no tiene lógica de negocio: muestra los datos que le manda el
 * controlador y le pasa las acciones de la persona (clics y textos).
 */
public class VentanaPrincipal extends javax.swing.JFrame implements IVistaEcosistema {

    private static final String PANTALLA_CONFIGURACION = "configuracion";
    private static final String PANTALLA_SIMULACION = "simulacion";

    private ControladorSimulacion controlador;

    public VentanaPrincipal() {
        initComponents();
    }

    public void setControlador(ControladorSimulacion controlador) {
        this.controlador = controlador;
    }

    // ===================== Navegación =====================

    private void cambiarPantalla(String nombre) {
        CardLayout cartas = (CardLayout) getContentPane().getLayout();
        cartas.show(getContentPane(), nombre);
    }

    @Override
    public void mostrarPantallaConfiguracion(boolean puedeVolver) {
        btnVolverSimulacion.setEnabled(puedeVolver);
        itemGuardar.setEnabled(puedeVolver);
        cambiarPantalla(PANTALLA_CONFIGURACION);
        txtPlantas.requestFocusInWindow();
    }

    @Override
    public void mostrarPantallaSimulacion() {
        itemGuardar.setEnabled(true);
        cambiarPantalla(PANTALLA_SIMULACION);
    }

    // ===================== Combos =====================

    @Override
    public void cargarClimas(List<String> climas) {
        cmbClima.setModel(new DefaultComboBoxModel<>(climas.toArray(new String[0])));
        cmbNuevoClima.setModel(new DefaultComboBoxModel<>(climas.toArray(new String[0])));
    }

    @Override
    public void cargarTiposEntidad(List<String> tipos) {
        cmbTipoEntidad.setModel(new DefaultComboBoxModel<>(tipos.toArray(new String[0])));
    }

    // ===================== Configuración =====================

    @Override
    public void mostrarErroresConfiguracion(List<String> errores) {
        StringBuilder texto = new StringBuilder("<html><b>Revisá estos datos:</b>");
        for (String error : errores) {
            texto.append("<br>• ").append(error);
        }
        texto.append("</html>");
        lblErroresConfig.setText(texto.toString());
    }

    @Override
    public void limpiarErroresConfiguracion() {
        lblErroresConfig.setText(" ");
    }

    // ===================== Simulación =====================

    @Override
    public void actualizarContadores(int turno, int turnosTotales, String clima,
            int plantas, int conejos, int lobos) {
        lblTurno.setText(turno + " de " + turnosTotales);
        lblClima.setText(clima);
        lblPlantas.setText(String.valueOf(plantas));
        lblConejos.setText(String.valueOf(conejos));
        lblLobos.setText(String.valueOf(lobos));
    }

    @Override
    public void actualizarTablaEntidades(List<Object[]> filas) {
        cargarTabla(tablaEntidades, filas);
    }

    @Override
    public void actualizarEventos(int turno, List<String> eventos) {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        modelo.addElement(turno == 0 ? "— Inicio de la simulación —" : "— Turno " + turno + " —");
        if (eventos.isEmpty()) {
            modelo.addElement("(No hubo eventos en este turno)");
        }
        for (String evento : eventos) {
            modelo.addElement(evento);
        }
        listaEventos.setModel(modelo);
    }

    @Override
    public void habilitarAvance(boolean habilitado) {
        btnAvanzar.setEnabled(habilitado);
    }

    @Override
    public void actualizarIntervencion(boolean habilitada, String mensaje) {
        cmbNuevoClima.setEnabled(habilitada);
        btnCambiarClima.setEnabled(habilitada);
        cmbTipoEntidad.setEnabled(habilitada);
        btnAgregarEntidad.setEnabled(habilitada);

        String color = habilitada ? "#1b7f3b" : "#666666";
        String texto = habilitada ? "<b>" + mensaje + "</b>" : mensaje;
        lblIntervencion.setText("<html><span style='color:" + color + "'>" + texto + "</span></html>");
    }

    /** Vacía la tabla (creada en el diseñador) y le agrega las filas recibidas. */
    private void cargarTabla(JTable tabla, List<Object[]> filas) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
    }

    // ===================== Mensajes =====================

    @Override
    public void mostrarMensaje(String mensaje) {
        lblEstado.setText(mensaje);
    }

    @Override
    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atención", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean confirmar(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return respuesta == JOptionPane.YES_OPTION;
    }

    @Override
    public void cerrar() {
        dispose();
        System.exit(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraMenu = new javax.swing.JMenuBar();
        menuSimulacion = new javax.swing.JMenu();
        itemNueva = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        separadorMenu = new javax.swing.JPopupMenu.Separator();
        itemSalir = new javax.swing.JMenuItem();
        panelConfiguracion = new javax.swing.JPanel();
        panelEncabezadoConfig = new javax.swing.JPanel();
        lblTituloConfig = new javax.swing.JLabel();
        lblSubtituloConfig = new javax.swing.JLabel();
        panelCentroConfig = new javax.swing.JPanel();
        panelFormulario = new javax.swing.JPanel();
        lblCampoPlantas = new javax.swing.JLabel();
        txtPlantas = new javax.swing.JTextField();
        lblRangoPlantas = new javax.swing.JLabel();
        lblCampoConejos = new javax.swing.JLabel();
        txtConejos = new javax.swing.JTextField();
        lblRangoConejos = new javax.swing.JLabel();
        lblCampoLobos = new javax.swing.JLabel();
        txtLobos = new javax.swing.JTextField();
        lblRangoLobos = new javax.swing.JLabel();
        lblCampoClima = new javax.swing.JLabel();
        cmbClima = new javax.swing.JComboBox<>();
        lblAyudaClima = new javax.swing.JLabel();
        lblCampoTurnos = new javax.swing.JLabel();
        txtTurnos = new javax.swing.JTextField();
        lblRangoTurnos = new javax.swing.JLabel();
        panelAccionesConfig = new javax.swing.JPanel();
        lblErroresConfig = new javax.swing.JLabel();
        panelBotonesConfig = new javax.swing.JPanel();
        btnVolverSimulacion = new javax.swing.JButton();
        btnIniciar = new javax.swing.JButton();
        panelSimulacion = new javax.swing.JPanel();
        panelContadores = new javax.swing.JPanel();
        lblTurno = new javax.swing.JLabel();
        lblClima = new javax.swing.JLabel();
        lblPlantas = new javax.swing.JLabel();
        lblConejos = new javax.swing.JLabel();
        lblLobos = new javax.swing.JLabel();
        scrollEntidades = new javax.swing.JScrollPane();
        tablaEntidades = new javax.swing.JTable();
        scrollEventos = new javax.swing.JScrollPane();
        listaEventos = new javax.swing.JList<>();
        panelSur = new javax.swing.JPanel();
        panelIntervencion = new javax.swing.JPanel();
        lblIntervencion = new javax.swing.JLabel();
        panelControlesIntervencion = new javax.swing.JPanel();
        lblNuevoClima = new javax.swing.JLabel();
        cmbNuevoClima = new javax.swing.JComboBox<>();
        btnCambiarClima = new javax.swing.JButton();
        lblSeparadorIntervencion = new javax.swing.JLabel();
        lblTipoEntidad = new javax.swing.JLabel();
        cmbTipoEntidad = new javax.swing.JComboBox<>();
        btnAgregarEntidad = new javax.swing.JButton();
        panelBotonesSimulacion = new javax.swing.JPanel();
        btnAvanzar = new javax.swing.JButton();
        lblEstado = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Simulador de Ecosistema");
        setMinimumSize(new java.awt.Dimension(1050, 700));
        getContentPane().setLayout(new java.awt.CardLayout());

        panelConfiguracion.setBorder(javax.swing.BorderFactory.createEmptyBorder(25, 30, 20, 30));
        panelConfiguracion.setLayout(new java.awt.BorderLayout(0, 15));
        panelEncabezadoConfig.setLayout(new java.awt.GridLayout(0, 1, 0, 6));
        lblTituloConfig.setFont(lblTituloConfig.getFont().deriveFont(lblTituloConfig.getFont().getStyle() | java.awt.Font.BOLD, lblTituloConfig.getFont().getSize()+10));
        lblTituloConfig.setText("Configurar un nuevo ecosistema");
        panelEncabezadoConfig.add(lblTituloConfig);

        lblSubtituloConfig.setForeground(new java.awt.Color(85, 85, 85));
        lblSubtituloConfig.setText("Completá los datos iniciales. A la derecha de cada campo figura el rango permitido.");
        panelEncabezadoConfig.add(lblSubtituloConfig);

        panelConfiguracion.add(panelEncabezadoConfig, java.awt.BorderLayout.NORTH);

        panelCentroConfig.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 10));
        panelFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos iniciales"));
        panelFormulario.setLayout(new java.awt.GridLayout(0, 3, 15, 12));
        lblCampoPlantas.setText("Plantas iniciales");
        panelFormulario.add(lblCampoPlantas);

        txtPlantas.setColumns(6);
        txtPlantas.setText("10");
        txtPlantas.setToolTipText("Cantidad de plantas al empezar");
        panelFormulario.add(txtPlantas);

        lblRangoPlantas.setForeground(new java.awt.Color(102, 102, 102));
        lblRangoPlantas.setText("Entre 5 y 30");
        panelFormulario.add(lblRangoPlantas);

        lblCampoConejos.setText("Conejos iniciales");
        panelFormulario.add(lblCampoConejos);

        txtConejos.setColumns(6);
        txtConejos.setText("5");
        txtConejos.setToolTipText("Cantidad de conejos al empezar");
        panelFormulario.add(txtConejos);

        lblRangoConejos.setForeground(new java.awt.Color(102, 102, 102));
        lblRangoConejos.setText("Entre 2 y 15");
        panelFormulario.add(lblRangoConejos);

        lblCampoLobos.setText("Lobos iniciales");
        panelFormulario.add(lblCampoLobos);

        txtLobos.setColumns(6);
        txtLobos.setText("2");
        txtLobos.setToolTipText("Cantidad de lobos al empezar");
        panelFormulario.add(txtLobos);

        lblRangoLobos.setForeground(new java.awt.Color(102, 102, 102));
        lblRangoLobos.setText("Entre 1 y 5");
        panelFormulario.add(lblRangoLobos);

        lblCampoClima.setText("Clima inicial");
        panelFormulario.add(lblCampoClima);

        panelFormulario.add(cmbClima);

        lblAyudaClima.setForeground(new java.awt.Color(102, 102, 102));
        lblAyudaClima.setText("Afecta la energía de cada especie");
        panelFormulario.add(lblAyudaClima);

        lblCampoTurnos.setText("Cantidad de turnos");
        panelFormulario.add(lblCampoTurnos);

        txtTurnos.setColumns(6);
        txtTurnos.setText("20");
        txtTurnos.setToolTipText("Duración total de la simulación");
        panelFormulario.add(txtTurnos);

        lblRangoTurnos.setForeground(new java.awt.Color(102, 102, 102));
        lblRangoTurnos.setText("Entre 10 y 50");
        panelFormulario.add(lblRangoTurnos);

        panelCentroConfig.add(panelFormulario);

        panelConfiguracion.add(panelCentroConfig, java.awt.BorderLayout.CENTER);

        panelAccionesConfig.setLayout(new java.awt.BorderLayout(15, 0));
        lblErroresConfig.setForeground(new java.awt.Color(176, 0, 32));
        lblErroresConfig.setText(" ");
        panelAccionesConfig.add(lblErroresConfig, java.awt.BorderLayout.CENTER);

        panelBotonesConfig.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 10, 5));
        btnVolverSimulacion.setText("Volver a la simulación");
        btnVolverSimulacion.setEnabled(false);
        btnVolverSimulacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverSimulacionActionPerformed(evt);
            }
        });
        panelBotonesConfig.add(btnVolverSimulacion);

        btnIniciar.setFont(btnIniciar.getFont().deriveFont(btnIniciar.getFont().getStyle() | java.awt.Font.BOLD, btnIniciar.getFont().getSize()+2));
        btnIniciar.setText("Iniciar simulación");
        btnIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarActionPerformed(evt);
            }
        });
        panelBotonesConfig.add(btnIniciar);

        panelAccionesConfig.add(panelBotonesConfig, java.awt.BorderLayout.EAST);

        panelConfiguracion.add(panelAccionesConfig, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panelConfiguracion, "configuracion");

        panelSimulacion.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSimulacion.setLayout(new java.awt.BorderLayout(10, 10));
        panelContadores.setLayout(new java.awt.GridLayout(1, 0, 10, 0));
        lblTurno.setFont(lblTurno.getFont().deriveFont(lblTurno.getFont().getStyle() | java.awt.Font.BOLD, lblTurno.getFont().getSize()+6));
        lblTurno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTurno.setText("0 de 0");
        lblTurno.setBorder(javax.swing.BorderFactory.createTitledBorder("Turno"));
        panelContadores.add(lblTurno);

        lblClima.setFont(lblClima.getFont().deriveFont(lblClima.getFont().getStyle() | java.awt.Font.BOLD, lblClima.getFont().getSize()+6));
        lblClima.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblClima.setText("-");
        lblClima.setBorder(javax.swing.BorderFactory.createTitledBorder("Clima"));
        panelContadores.add(lblClima);

        lblPlantas.setFont(lblPlantas.getFont().deriveFont(lblPlantas.getFont().getStyle() | java.awt.Font.BOLD, lblPlantas.getFont().getSize()+6));
        lblPlantas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPlantas.setText("0");
        lblPlantas.setBorder(javax.swing.BorderFactory.createTitledBorder("Plantas"));
        panelContadores.add(lblPlantas);

        lblConejos.setFont(lblConejos.getFont().deriveFont(lblConejos.getFont().getStyle() | java.awt.Font.BOLD, lblConejos.getFont().getSize()+6));
        lblConejos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblConejos.setText("0");
        lblConejos.setBorder(javax.swing.BorderFactory.createTitledBorder("Conejos"));
        panelContadores.add(lblConejos);

        lblLobos.setFont(lblLobos.getFont().deriveFont(lblLobos.getFont().getStyle() | java.awt.Font.BOLD, lblLobos.getFont().getSize()+6));
        lblLobos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLobos.setText("0");
        lblLobos.setBorder(javax.swing.BorderFactory.createTitledBorder("Lobos"));
        panelContadores.add(lblLobos);

        panelSimulacion.add(panelContadores, java.awt.BorderLayout.NORTH);

        scrollEntidades.setBorder(javax.swing.BorderFactory.createTitledBorder("Entidades vivas (clic en una columna para ordenar)"));
        scrollEntidades.setPreferredSize(new java.awt.Dimension(600, 380));
        tablaEntidades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo", "Nombre", "Energía", "Edad (turnos)", "Detalle"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEntidades.setFillsViewportHeight(true);
        tablaEntidades.setRowHeight(22);
        scrollEntidades.setViewportView(tablaEntidades);

        panelSimulacion.add(scrollEntidades, java.awt.BorderLayout.CENTER);

        scrollEventos.setBorder(javax.swing.BorderFactory.createTitledBorder("Eventos del turno"));
        scrollEventos.setPreferredSize(new java.awt.Dimension(380, 380));
        scrollEventos.setViewportView(listaEventos);

        panelSimulacion.add(scrollEventos, java.awt.BorderLayout.EAST);

        panelSur.setLayout(new java.awt.BorderLayout(10, 6));
        panelIntervencion.setBorder(javax.swing.BorderFactory.createTitledBorder("Intervención del jugador (cada 3 turnos)"));
        panelIntervencion.setLayout(new java.awt.BorderLayout(0, 4));
        lblIntervencion.setText("Podés intervenir cada 3 turnos.");
        panelIntervencion.add(lblIntervencion, java.awt.BorderLayout.NORTH);

        panelControlesIntervencion.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 2));
        lblNuevoClima.setText("Nuevo clima:");
        panelControlesIntervencion.add(lblNuevoClima);

        cmbNuevoClima.setEnabled(false);
        panelControlesIntervencion.add(cmbNuevoClima);

        btnCambiarClima.setText("Cambiar clima");
        btnCambiarClima.setEnabled(false);
        btnCambiarClima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarClimaActionPerformed(evt);
            }
        });
        panelControlesIntervencion.add(btnCambiarClima);

        lblSeparadorIntervencion.setForeground(new java.awt.Color(102, 102, 102));
        lblSeparadorIntervencion.setText("   o bien   ");
        panelControlesIntervencion.add(lblSeparadorIntervencion);

        lblTipoEntidad.setText("Agregar:");
        panelControlesIntervencion.add(lblTipoEntidad);

        cmbTipoEntidad.setEnabled(false);
        panelControlesIntervencion.add(cmbTipoEntidad);

        btnAgregarEntidad.setText("Agregar entidad");
        btnAgregarEntidad.setEnabled(false);
        btnAgregarEntidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarEntidadActionPerformed(evt);
            }
        });
        panelControlesIntervencion.add(btnAgregarEntidad);

        panelIntervencion.add(panelControlesIntervencion, java.awt.BorderLayout.CENTER);

        panelSur.add(panelIntervencion, java.awt.BorderLayout.CENTER);

        panelBotonesSimulacion.setLayout(new java.awt.GridLayout(0, 1, 0, 6));
        btnAvanzar.setFont(btnAvanzar.getFont().deriveFont(btnAvanzar.getFont().getStyle() | java.awt.Font.BOLD, btnAvanzar.getFont().getSize()+3));
        btnAvanzar.setText("Avanzar turno  ▶");
        btnAvanzar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAvanzarActionPerformed(evt);
            }
        });
        panelBotonesSimulacion.add(btnAvanzar);

        panelSur.add(panelBotonesSimulacion, java.awt.BorderLayout.EAST);

        lblEstado.setForeground(new java.awt.Color(51, 51, 51));
        lblEstado.setText("Listo.");
        panelSur.add(lblEstado, java.awt.BorderLayout.SOUTH);

        panelSimulacion.add(panelSur, java.awt.BorderLayout.SOUTH);

        getContentPane().add(panelSimulacion, "simulacion");

        menuSimulacion.setText("Simulación");
        itemNueva.setText("Nueva simulación...");
        itemNueva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevaActionPerformed(evt);
            }
        });
        menuSimulacion.add(itemNueva);

        itemGuardar.setText("Guardar ahora");
        itemGuardar.setEnabled(false);
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuSimulacion.add(itemGuardar);

        menuSimulacion.add(separadorMenu);

        itemSalir.setText("Salir");
        itemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSalirActionPerformed(evt);
            }
        });
        menuSimulacion.add(itemSalir);

        barraMenu.add(menuSimulacion);

        setJMenuBar(barraMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ===================== Eventos: todos le pasan la acción al controlador =====================

    private void btnVolverSimulacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverSimulacionActionPerformed
        controlador.volverASimulacion();
    }//GEN-LAST:event_btnVolverSimulacionActionPerformed

    private void btnIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarActionPerformed
        controlador.iniciarSimulacion(txtPlantas.getText(), txtConejos.getText(),
                txtLobos.getText(), (String) cmbClima.getSelectedItem(), txtTurnos.getText());
    }//GEN-LAST:event_btnIniciarActionPerformed

    private void btnCambiarClimaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarClimaActionPerformed
        controlador.cambiarClima((String) cmbNuevoClima.getSelectedItem());
    }//GEN-LAST:event_btnCambiarClimaActionPerformed

    private void btnAgregarEntidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarEntidadActionPerformed
        controlador.agregarEntidad((String) cmbTipoEntidad.getSelectedItem());
    }//GEN-LAST:event_btnAgregarEntidadActionPerformed

    private void btnAvanzarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAvanzarActionPerformed
        controlador.avanzarTurno();
    }//GEN-LAST:event_btnAvanzarActionPerformed

    private void itemNuevaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevaActionPerformed
        controlador.nuevaSimulacion();
    }//GEN-LAST:event_itemNuevaActionPerformed

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        controlador.guardarAhora();
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSalirActionPerformed
        controlador.salir();
    }//GEN-LAST:event_itemSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JButton btnAgregarEntidad;
    private javax.swing.JButton btnAvanzar;
    private javax.swing.JButton btnCambiarClima;
    private javax.swing.JButton btnIniciar;
    private javax.swing.JButton btnVolverSimulacion;
    private javax.swing.JComboBox<String> cmbClima;
    private javax.swing.JComboBox<String> cmbNuevoClima;
    private javax.swing.JComboBox<String> cmbTipoEntidad;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemNueva;
    private javax.swing.JMenuItem itemSalir;
    private javax.swing.JLabel lblAyudaClima;
    private javax.swing.JLabel lblCampoClima;
    private javax.swing.JLabel lblCampoConejos;
    private javax.swing.JLabel lblCampoLobos;
    private javax.swing.JLabel lblCampoPlantas;
    private javax.swing.JLabel lblCampoTurnos;
    private javax.swing.JLabel lblClima;
    private javax.swing.JLabel lblConejos;
    private javax.swing.JLabel lblErroresConfig;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblIntervencion;
    private javax.swing.JLabel lblLobos;
    private javax.swing.JLabel lblNuevoClima;
    private javax.swing.JLabel lblPlantas;
    private javax.swing.JLabel lblRangoConejos;
    private javax.swing.JLabel lblRangoLobos;
    private javax.swing.JLabel lblRangoPlantas;
    private javax.swing.JLabel lblRangoTurnos;
    private javax.swing.JLabel lblSeparadorIntervencion;
    private javax.swing.JLabel lblSubtituloConfig;
    private javax.swing.JLabel lblTipoEntidad;
    private javax.swing.JLabel lblTituloConfig;
    private javax.swing.JLabel lblTurno;
    private javax.swing.JList<String> listaEventos;
    private javax.swing.JMenu menuSimulacion;
    private javax.swing.JPanel panelAccionesConfig;
    private javax.swing.JPanel panelBotonesConfig;
    private javax.swing.JPanel panelBotonesSimulacion;
    private javax.swing.JPanel panelCentroConfig;
    private javax.swing.JPanel panelConfiguracion;
    private javax.swing.JPanel panelContadores;
    private javax.swing.JPanel panelControlesIntervencion;
    private javax.swing.JPanel panelEncabezadoConfig;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelIntervencion;
    private javax.swing.JPanel panelSimulacion;
    private javax.swing.JPanel panelSur;
    private javax.swing.JScrollPane scrollEntidades;
    private javax.swing.JScrollPane scrollEventos;
    private javax.swing.JPopupMenu.Separator separadorMenu;
    private javax.swing.JTable tablaEntidades;
    private javax.swing.JTextField txtConejos;
    private javax.swing.JTextField txtLobos;
    private javax.swing.JTextField txtPlantas;
    private javax.swing.JTextField txtTurnos;
    // End of variables declaration//GEN-END:variables
}
