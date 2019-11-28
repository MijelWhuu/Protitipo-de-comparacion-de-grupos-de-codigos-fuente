package interfaz.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.AutoridadesNotificacionesBl;
import bl.utilidades.CalendarioOficialBl;
import bl.notificaciones.CargaNotificacionesExtraBl;
import entitys.SesionEntity;
import entitys.AutoridadNotificacionEntity;
import entitys.ExpedienteEntity;
import entitys.CargaNotificacionExtraEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class CargaNotificacionesExtra extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    CargaNotificacionesExtraBl bl = new CargaNotificacionesExtraBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    AutoridadesNotificacionesBl blAuto = new AutoridadesNotificacionesBl();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public CargaNotificacionesExtra(){
        initComponents();
    }
    
    public CargaNotificacionesExtra(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        inicializarPantalla();
        mapeoTeclas();
        btnMas.setVisible(false);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void inicializarPantalla(){
        txtOficio.setDocument(new JTextMascara(29));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        u.formatoJDateChooser(dateFecha);
        txtObservaciones.setDocument(new JTextMascaraTextArea());
        dateFecha.setDate(cal.getTime());
        u.inicializaCombo(cmbAutoridad, new AutoridadNotificacionEntity(), blAuto.getAutoridades());
        txtAnio.setText(""+cal.get(Calendar.YEAR));
    }
        
    private Boolean validar(){
        if(txtOficio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Número de Oficio", "Alerta", 2);
            txtOficio.requestFocus();
        }else if(txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Número de Expediente", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(!bl.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Número de Expediente de expediente no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(!blCale.isfechaValida(u.dateCastString(dateFecha.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(cmbAutoridad.getItemCount()>0 && cmbAutoridad.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado la Autoridad", "Alerta", 2);
            cmbAutoridad.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private CargaNotificacionExtraEntity getDatos(){
        CargaNotificacionExtraEntity noti = new CargaNotificacionExtraEntity();
        noti.setOficio(txtOficio.getText());
        Integer anio = cal.get(Calendar.YEAR);
        if(txtAnio.getText().length()!=0){
            Integer num = u.verificaInteger(txtAnio.getText());
            if(num!=null)
                anio = num;
        }
        noti.setAnio(anio);
        noti.setExpe(new ExpedienteEntity(txtExpediente.getText()));
        noti.setFecha_recibida(dateFecha.getDate());
        noti.setAuto((AutoridadNotificacionEntity)cmbAutoridad.getSelectedItem());
        noti.setTipo_notificacion("OFICIO");
        if(cbxCorreo.isSelected())
            noti.setTipo_notificacion("CORREO");
        if(cbxLista.isSelected())
            noti.setTipo_notificacion("LISTA");
        noti.setObservaciones(txtObservaciones.getText());
        return noti;
    }
    
    private void guardar(){
        ErrorEntity error = bl.saveNotificacion(getDatos(), sesion);
        if(!error.getError()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
            botonEsc();
            txtOficio.requestFocus();
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
        }
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Mapeo de teclas para los botones">
    public final void mapeoTeclas(){
        ActionMap mapaAccion = this.getActionMap();
        InputMap map = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"accion_Esc"); mapaAccion.put("accion_Esc",Accion_Esc());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),"accion_F2"); mapaAccion.put("accion_F2",Accion_F2());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4,0),"accion_F4"); mapaAccion.put("accion_F4",Accion_F4());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0),"accion_F5"); mapaAccion.put("accion_F5",Accion_F5());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0),"accion_F6"); mapaAccion.put("accion_F6",Accion_F6());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0),"accion_F7"); mapaAccion.put("accion_F7",Accion_F7());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0),"accion_F12"); mapaAccion.put("accion_F12",Accion_F12());        
    }
    public AbstractAction Accion_Esc(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F5(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF5();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F7(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF7();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF12();}};}
    
    //Para cambiar el focus cuando se encuentra en una tabla
    public AbstractAction Accion_tab(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();  
    }};}
    /////
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        u.cleanComponent(txtOficio,txtAnio,cmbAutoridad,txtExpediente,dateFecha,cbxCorreo,cbxLista,txtObservaciones);
        txtOficio.requestFocus();
        txtAnio.setText(""+cal.get(Calendar.YEAR));
    }
    
    private void botonF2(){
        if(validar()){
            guardar();
        }       
    }
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){}
    
    private void botonF7(){}
    
    private void botonF12(){
        dispose();
    }
    // </editor-fold>
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        btnF2 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbAutoridad = new javax.swing.JComboBox();
        txtOficio = new javax.swing.JTextField();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnMas = new javax.swing.JButton();
        cbxCorreo = new javax.swing.JCheckBox();
        cbxLista = new javax.swing.JCheckBox();

        setClosable(true);
        setTitle("Carga Extra de Notificaciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/carga_extra_notificaciones.jpg"))); // NOI18N

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F2 - Guardar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnEsc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnEsc.setText("Esc - Cancelar");
        btnEsc.setFocusable(false);
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSalir.setText("F12 - Salir");
        btnSalir.setFocusable(false);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Observaciones");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(30, 290, 110, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Fecha de Recibida");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(10, 180, 130, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Oficio");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(70, 20, 70, 30);

        cmbAutoridad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbAutoridad);
        cmbAutoridad.setBounds(150, 100, 580, 30);

        txtOficio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtOficio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtOficio);
        txtOficio.setBounds(150, 20, 130, 30);

        dateFecha.setDateFormatString("dd/MMM/yyyy");
        dateFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(dateFecha);
        dateFecha.setBounds(150, 180, 130, 30);

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservaciones.setRows(2);
        txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtObservaciones);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(150, 290, 620, 70);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expediente");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(40, 140, 100, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtExpediente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExpedienteFocusLost(evt);
            }
        });
        txtExpediente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpedienteKeyPressed(evt);
            }
        });
        jPanel1.add(txtExpediente);
        txtExpediente.setBounds(150, 140, 130, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Año");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(70, 60, 70, 30);

        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtAnio);
        txtAnio.setBounds(150, 60, 80, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Autoridad");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(40, 100, 100, 30);

        btnMas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnMas.setText("+");
        btnMas.setFocusable(false);
        btnMas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasActionPerformed(evt);
            }
        });
        jPanel1.add(btnMas);
        btnMas.setBounds(730, 100, 40, 30);

        cbxCorreo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxCorreo.setText("Notificación por Correo");
        jPanel1.add(cbxCorreo);
        cbxCorreo.setBounds(150, 220, 210, 25);

        cbxLista.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxLista.setText("Notificación por Lista");
        jPanel1.add(cbxLista);
        cbxLista.setBounds(150, 250, 210, 25);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnEsc)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        botonF12();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        u.focus(evt, txtObservaciones);
    }//GEN-LAST:event_txtObservacionesKeyPressed

    private void txtExpedienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExpedienteFocusLost

    }//GEN-LAST:event_txtExpedienteFocusLost

    private void btnMasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasActionPerformed
        AutoridadNotificacionEntity auto = new AutoridadNotificacionEntity("-1","","");
        AgregarAutoridad agrega = new AgregarAutoridad(null,true,auto,sesion);
        agrega.setLocationRelativeTo(null);
        agrega.show();
        if(!auto.getId_autoridad().equals("-1")){
            u.inicializaCombo(cmbAutoridad, new AutoridadNotificacionEntity(), blAuto.getAutoridades());
            cmbAutoridad.setSelectedItem(auto);
        }
    }//GEN-LAST:event_btnMasActionPerformed

    private void txtExpedienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpedienteKeyPressed
        /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(txtExpediente.getText().length()>0){
                String expediente = u.completaNumeroExpediente(txtExpediente.getText());
                if(expediente!=null){
                    txtExpediente.setText(expediente);
                }
            }
        }*/
    }//GEN-LAST:event_txtExpedienteKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnMas;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cbxCorreo;
    private javax.swing.JCheckBox cbxLista;
    private javax.swing.JComboBox cmbAutoridad;
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtOficio;
    // End of variables declaration//GEN-END:variables
    
}

