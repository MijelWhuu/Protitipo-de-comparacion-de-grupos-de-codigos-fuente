package interfaz.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.AutoridadesNotificacionesBl;
import entitys.SesionEntity;
import entitys.AutoridadNotificacionEntity;
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
public class AutoridadesNotificaciones extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    AutoridadesNotificacionesBl bl = new AutoridadesNotificacionesBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form Destinos
     */
    public AutoridadesNotificaciones(){
        initComponents();
    }
    
    public AutoridadesNotificaciones(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatosCampos();
        mapeoTeclas();
        inicializaCombo();
    }
    
    private void formatosCampos(){
        txtNombreNew.setDocument(new JTextMascara(290,true,"*"));
        txtObservacionesNew.setDocument(new JTextMascaraTextArea());
        txtNombreUpdate.setDocument(new JTextMascara(290,true,"*"));
        txtObservacionesUpdate.setDocument(new JTextMascaraTextArea());
    }
    
    private void inicializaCombo(){
        u.inicializaCombo(cmbDestino, new AutoridadNotificacionEntity(), bl.getAutoridades());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones Botones">
    private void botonEsc(){
        if(jTabbedPane1.getSelectedIndex()==0){
            txtNombreNew.setText("");
            cbxDefaultNew.setSelected(false);
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
            }else{
                ErrorEntity error = bl.saveAutoridad(new AutoridadNotificacionEntity(txtNombreNew.getText(), txtObservacionesNew.getText(),cbxDefaultNew.isSelected()),sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    inicializaCombo();
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
            }else{
                AutoridadNotificacionEntity tipo = (AutoridadNotificacionEntity)cmbDestino.getSelectedItem();
                tipo.setNombre(txtNombreUpdate.getText());
                tipo.setObservaciones(txtObservacionesUpdate.getText());
                tipo.setSeleccionar(cbxDefaultUpdate.isSelected());
                ErrorEntity error = bl.updateAutoridad(tipo,sesion);
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
                    ErrorEntity error = bl.deleteAutoridad(((AutoridadNotificacionEntity)cmbDestino.getSelectedItem()).getId_autoridad());
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
        cbxDefaultNew = new javax.swing.JCheckBox();
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
        cbxDefaultUpdate = new javax.swing.JCheckBox();
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Catálogo - Autoridades de Notificaciones Extra");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cat_autoridades_extra.jpg"))); // NOI18N

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Nombre de la Autoridad");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 40, 390, 30);

        txtNombreNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNombreNew);
        txtNombreNew.setBounds(10, 70, 700, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Observaciones");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 160, 134, 30);

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
        jScrollPane3.setBounds(10, 190, 700, 80);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 40, 20, 30);

        cbxDefaultNew.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxDefaultNew.setText("Seleccionar siempre por Default");
        jPanel1.add(cbxDefaultNew);
        cbxDefaultNew.setBounds(10, 120, 260, 25);

        jTabbedPane1.addTab("Nuevo", jPanel1);

        jPanel2.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Autoridad");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(16, 30, 70, 30);

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDestinoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbDestino);
        cmbDestino.setBounds(90, 30, 590, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 10, 700, 80);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Nombre de la Autoridad");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(20, 90, 430, 30);

        txtNombreUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNombreUpdate);
        txtNombreUpdate.setBounds(10, 120, 700, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Observaciones");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 210, 134, 30);

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
        jScrollPane2.setBounds(10, 240, 700, 80);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 90, 30, 30);

        cbxDefaultUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxDefaultUpdate.setText("Seleccionar siempre por Default");
        jPanel2.add(cbxDefaultUpdate);
        cbxDefaultUpdate.setBounds(10, 170, 260, 25);

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
        btnEsc.setText("Esc - Limpiar");
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 727, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacionesNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesNewKeyPressed
        u.focus(evt, txtObservacionesNew);
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
            txtObservacionesUpdate.setText("");
            if(cmbDestino.getSelectedIndex()==0){
                txtNombreUpdate.setEnabled(false);
                cbxDefaultUpdate.setSelected(false);
                cbxDefaultUpdate.setEnabled(false);
                txtObservacionesUpdate.setEnabled(false);
            }else{
                txtNombreUpdate.setEnabled(true);
                cbxDefaultUpdate.setEnabled(true);
                txtObservacionesUpdate.setEnabled(true);
                AutoridadNotificacionEntity tipo = (AutoridadNotificacionEntity)cmbDestino.getSelectedItem();
                txtNombreUpdate.setText(tipo.getNombre());
                cbxDefaultUpdate.setSelected(tipo.getSeleccionar());
                txtObservacionesUpdate.setText(tipo.getObservaciones());
            }
        }
    }//GEN-LAST:event_cmbDestinoActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtNombreNew.requestFocus();
    }//GEN-LAST:event_formComponentShown

    private void txtObservacionesUpdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesUpdateKeyPressed
        u.focus(evt, txtObservacionesUpdate);
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
    private javax.swing.JCheckBox cbxDefaultNew;
    private javax.swing.JCheckBox cbxDefaultUpdate;
    private javax.swing.JComboBox cmbDestino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtNombreNew;
    private javax.swing.JTextField txtNombreUpdate;
    private javax.swing.JTextArea txtObservacionesNew;
    private javax.swing.JTextArea txtObservacionesUpdate;
    // End of variables declaration//GEN-END:variables
}
