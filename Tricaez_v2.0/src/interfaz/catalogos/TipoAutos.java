package interfaz.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.catalogos.TipoAutosBl;
import bl.catalogos.TipoNotificacionesBl;
import entitys.SesionEntity;
import entitys.TipoAutoMesaTramiteEntity;
import entitys.TipoNotificacionEntity;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class TipoAutos extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    TipoAutosBl bl = new TipoAutosBl();
    TipoNotificacionesBl blNoti = new TipoNotificacionesBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form Destinos
     */
    public TipoAutos(){
        initComponents();
    }
    
    public TipoAutos(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatosCampos();
        mapeoTeclas();
        inicializaCombo();
        inicializaCombosNoti();
    }
    
    private void formatosCampos(){
        txtNombreNew.setDocument(new JTextMascara(290,true,"*"));
        txtDiasNew.setDocument(new JTextMascara(3,true,"numerico"));
        txtObservacionesNew.setDocument(new JTextMascara(490,true,"*"));
        txtNombreUpdate.setDocument(new JTextMascara(290,true,"*"));
        txtDiasUpdate.setDocument(new JTextMascara(3,true,"numerico"));
        txtObservacionesUpdate.setDocument(new JTextMascara(490,true,"*"));
    }
    
    private void inicializaCombo(){
        u.inicializaCombo(cmbDestino, new TipoAutoMesaTramiteEntity(), bl.getTipoAutos());
    }
    
    private void inicializaCombosNoti(){
        u.inicializaCombo(cmbNotificacionNew, new TipoNotificacionEntity(), blNoti.getTipoNotificaciones());
        u.inicializaCombo(cmbNotificacionUpdate, new TipoNotificacionEntity(), blNoti.getTipoNotificaciones());
    }

    // <editor-fold defaultstate="collapsed" desc="Funciones Botones">
    private void botonEsc(){
        if(jTabbedPane1.getSelectedIndex()==0){
            txtNombreNew.setText("");
            if(cmbNotificacionNew.getItemCount()>0)
                cmbNotificacionNew.setSelectedIndex(0);
            txtDiasNew.setText("");
            txtObservacionesNew.setText("");
            txtNombreNew.requestFocus();
        }else{
            cmbDestino.setSelectedIndex(0);
        }
    }
    
    private void botonF2(){
        if(jTabbedPane1.getSelectedIndex()==0){
            if(txtNombreNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreNew.requestFocus();
            }else if(cmbNotificacionNew.getItemCount()>0 && cmbNotificacionNew.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún Tipo de Notificación", "Alerta", 2);
                cmbNotificacionNew.requestFocus();
            }else if(txtDiasNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Número de días es obligatorio", "Alerta", 2);
                txtDiasNew.requestFocus();
            }else{
                TipoAutoMesaTramiteEntity tipo = new TipoAutoMesaTramiteEntity();
                tipo.setNombre(txtNombreNew.getText());
                tipo.setDias(new Integer(txtDiasNew.getText()));
                tipo.setObservaciones(txtObservacionesNew.getText());
                tipo.setNoti((TipoNotificacionEntity)cmbNotificacionNew.getSelectedItem());
                ErrorEntity error = bl.saveTipoAuto(tipo, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    inicializaCombo();
                    inicializaCombosNoti();
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF4(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbDestino.getItemCount()>1){
            if(cmbDestino.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún elemento", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else if(txtNombreUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else if(cmbNotificacionUpdate.getItemCount()>0 && cmbNotificacionUpdate.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún Tipo de Notificación", "Alerta", 2);
                cmbNotificacionUpdate.requestFocus();
            }else if(txtDiasUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Número de días es obligatorio", "Alerta", 2);
                txtDiasUpdate.requestFocus();
            }else{
                TipoAutoMesaTramiteEntity tipo = (TipoAutoMesaTramiteEntity)cmbDestino.getSelectedItem();
                tipo.setNombre(txtNombreUpdate.getText());
                tipo.setDias(new Integer(txtDiasUpdate.getText()));
                tipo.setObservaciones(txtObservacionesUpdate.getText());
                tipo.setNoti((TipoNotificacionEntity)cmbNotificacionUpdate.getSelectedItem());
                ErrorEntity error = bl.updateTipoAuto(tipo, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                    inicializaCombo();
                    cmbDestino.requestFocus();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF6(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbDestino.getItemCount()>1){
            if(cmbDestino.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún elemento", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else{
                if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar el elemento seleccionado?", "Alerta", 0)==0){
                    ErrorEntity error = bl.deleteTipoAuto(((TipoAutoMesaTramiteEntity)cmbDestino.getSelectedItem()).getId_tipo_auto_mesa_tramite());
                    if(!error.getError()){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                        inicializaCombo();
                        cmbDestino.requestFocus();
                    }else{
                        if(error.getNumError().equals(1451)){
                            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar ya que el elemento seleccionado está siendo utilizado por otra información", "Aviso", 1);
                            cmbDestino.requestFocus();
                        }else{
                            javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                        }
                    }
                }
            }
        }
    }
    
    private void botonF12(){
        dispose();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Mapeo de teclas para los botones">
    public final void mapeoTeclas(){
        ActionMap mapaAccion = this.getActionMap();
        InputMap map = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"accion_Esc"); mapaAccion.put("accion_Esc",Accion_Esc());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),"accion_F2"); mapaAccion.put("accion_F2",Accion_F2());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4,0),"accion_F4"); mapaAccion.put("accion_F4",Accion_F4());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0),"accion_F6"); mapaAccion.put("accion_F6",Accion_F6());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0),"accion_F12"); mapaAccion.put("accion_F12",Accion_F12());
    }
    public AbstractAction Accion_Esc(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF12();}};}
    // </editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtNombreNew = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservacionesNew = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtDiasNew = new javax.swing.JTextField();
        cmbNotificacionNew = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtNombreUpdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacionesUpdate = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cmbNotificacionUpdate = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtDiasUpdate = new javax.swing.JTextField();
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Catálogo - Tipo de Autos de las Mesas de Trámites");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cat_tipo_autos.jpg"))); // NOI18N

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Nombre del Tipo de Auto");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 40, 260, 30);

        txtNombreNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNombreNew);
        txtNombreNew.setBounds(10, 70, 620, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Observaciones");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 180, 134, 30);

        txtObservacionesNew.setColumns(20);
        txtObservacionesNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservacionesNew.setLineWrap(true);
        txtObservacionesNew.setRows(4);
        txtObservacionesNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesNewKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtObservacionesNew);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 210, 620, 90);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 40, 20, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("*");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(10, 110, 30, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setText("Tipo de Notificación");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(20, 110, 140, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("*");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(300, 140, 30, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setText("No. de días límite para contestar el Auto");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(310, 140, 270, 30);

        txtDiasNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtDiasNew.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtDiasNew);
        txtDiasNew.setBounds(580, 140, 50, 30);

        cmbNotificacionNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbNotificacionNew);
        cmbNotificacionNew.setBounds(10, 140, 280, 30);

        jTabbedPane1.addTab("Nuevo", jPanel1);

        jPanel2.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Tipo de Autos");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(20, 20, 210, 30);

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDestinoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbDestino);
        cmbDestino.setBounds(20, 50, 580, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 11, 620, 100);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Nombre del Tipo de Auto");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(20, 110, 250, 30);

        txtNombreUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNombreUpdate);
        txtNombreUpdate.setBounds(10, 140, 620, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Observaciones");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 230, 134, 30);

        txtObservacionesUpdate.setColumns(20);
        txtObservacionesUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservacionesUpdate.setRows(4);
        txtObservacionesUpdate.setNextFocusableComponent(jTabbedPane1);
        txtObservacionesUpdate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesUpdateKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtObservacionesUpdate);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(10, 260, 620, 80);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 110, 30, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Tipo de Notificación");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(20, 170, 140, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 0, 0));
        jLabel10.setText("*");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(10, 170, 30, 30);

        cmbNotificacionUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbNotificacionUpdate);
        cmbNotificacionUpdate.setBounds(10, 200, 270, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setText("No. de días límite para contestar el Auto");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(310, 200, 270, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 0, 0));
        jLabel12.setText("*");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(300, 200, 30, 30);

        txtDiasUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtDiasUpdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtDiasUpdate);
        txtDiasUpdate.setBounds(580, 200, 50, 30);

        jTabbedPane1.addTab("Modificar/Eliminar", jPanel2);

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
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

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F2 - Guardar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnF4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF4.setText("F4 - Modificar");
        btnF4.setFocusable(false);
        btnF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF4ActionPerformed(evt);
            }
        });

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("F6 - Eliminar");
        btnF6.setFocusable(false);
        btnF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnF4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacionesNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesNewKeyPressed
        if(evt.getKeyChar()==KeyEvent.VK_TAB){
            evt.consume();
            jTabbedPane1.requestFocus();
        }
        if(evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB){
            txtDiasNew.requestFocus();
        }
    }//GEN-LAST:event_txtObservacionesNewKeyPressed

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF4ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF4ActionPerformed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void cmbDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDestinoActionPerformed
        if(cmbDestino.getItemCount()>0){
            txtNombreUpdate.setText("");
            txtDiasUpdate.setText("");
            txtObservacionesUpdate.setText("");
            if(cmbNotificacionUpdate.getItemCount()>0)
                cmbNotificacionUpdate.setSelectedIndex(0);
            if(cmbDestino.getSelectedIndex()==0){
                u.enabledComponet(false, txtNombreUpdate,cmbNotificacionUpdate,txtDiasUpdate,txtObservacionesUpdate);
            }else{
                u.enabledComponet(true, txtNombreUpdate,cmbNotificacionUpdate,txtDiasUpdate,txtObservacionesUpdate);
                TipoAutoMesaTramiteEntity tipo = (TipoAutoMesaTramiteEntity)cmbDestino.getSelectedItem();
                txtNombreUpdate.setText(tipo.getNombre());
                txtDiasUpdate.setText(tipo.getDias().toString());
                txtObservacionesUpdate.setText(tipo.getObservaciones());
                cmbNotificacionUpdate.setSelectedItem(tipo.getNoti());
            }
        }
    }//GEN-LAST:event_cmbDestinoActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtNombreNew.requestFocus();
    }//GEN-LAST:event_formComponentShown

    private void txtObservacionesUpdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesUpdateKeyPressed
        if(evt.getKeyChar()==KeyEvent.VK_TAB){
            evt.consume();
            jTabbedPane1.requestFocus();
        }
        if(evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB){
            txtDiasUpdate.requestFocus();
        }
    }//GEN-LAST:event_txtObservacionesUpdateKeyPressed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if(jTabbedPane1.getSelectedIndex()==0){
            btnF2.setEnabled(true);
            btnF4.setEnabled(false);
            btnF6.setEnabled(false);
        }else{
            btnF2.setEnabled(false);
            btnF4.setEnabled(true);
            btnF6.setEnabled(true);
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.JComboBox cmbDestino;
    private javax.swing.JComboBox cmbNotificacionNew;
    private javax.swing.JComboBox cmbNotificacionUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtDiasNew;
    private javax.swing.JTextField txtDiasUpdate;
    private javax.swing.JTextField txtNombreNew;
    private javax.swing.JTextField txtNombreUpdate;
    private javax.swing.JTextArea txtObservacionesNew;
    private javax.swing.JTextArea txtObservacionesUpdate;
    // End of variables declaration//GEN-END:variables
}
