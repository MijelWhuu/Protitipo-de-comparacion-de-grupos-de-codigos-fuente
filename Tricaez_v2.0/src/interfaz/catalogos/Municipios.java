package interfaz.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.catalogos.MunicipiosBl;
import entitys.SesionEntity;
import entitys.MunicipioEntity;
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
public class Municipios extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    MunicipiosBl bl = new MunicipiosBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form Destinos
     */
    public Municipios(){
        initComponents();
    }
    
    public Municipios(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatosCampos();
        mapeoTeclas();
        inicializaCombo();
    }
    
    private void formatosCampos(){
        txtNombreNew.setDocument(new JTextMascara(90,true,"*"));
        txtNombreUpdate.setDocument(new JTextMascara(90,true,"*"));
    }
    
    private void inicializaCombo(){
        u.inicializaCombo(cmbDestino, new MunicipioEntity(), bl.getMunicipios());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones Botones">
    private void botonEsc(){
        if(jTabbedPane1.getSelectedIndex()==0){
            txtNombreNew.setText("");
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
                ErrorEntity error = bl.saveMunicipio(new MunicipioEntity(0,txtNombreNew.getText()), sesion);
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
                MunicipioEntity mun = (MunicipioEntity)cmbDestino.getSelectedItem();
                mun.setNombre(txtNombreUpdate.getText());
                ErrorEntity error = bl.updateMunicipio(mun, sesion);
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
                    ErrorEntity error = bl.deleteMunicipio(((MunicipioEntity)cmbDestino.getSelectedItem()).getId_municipio());
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
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        txtNombreUpdate = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Catálogo - Municipios");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cat_municipios.jpg"))); // NOI18N

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Nombre del Municipio");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 40, 390, 30);

        txtNombreNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNombreNew);
        txtNombreNew.setBounds(10, 70, 460, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 40, 20, 30);

        jTabbedPane1.addTab("Nuevo", jPanel1);

        jPanel2.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Municipios");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(16, 30, 70, 30);

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDestinoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbDestino);
        cmbDestino.setBounds(90, 30, 350, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 11, 460, 80);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Nombre del Municipio");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(20, 110, 250, 30);

        txtNombreUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNombreUpdate);
        txtNombreUpdate.setBounds(10, 140, 460, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("*");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(10, 110, 30, 30);

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
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            if(cmbDestino.getSelectedIndex()==0){    
                txtNombreUpdate.setEnabled(false);
            }else{
                txtNombreUpdate.setEnabled(true);
                MunicipioEntity mun = (MunicipioEntity)cmbDestino.getSelectedItem();
                txtNombreUpdate.setText(mun.getNombre());
            }
        }
    }//GEN-LAST:event_cmbDestinoActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtNombreNew.requestFocus();
    }//GEN-LAST:event_formComponentShown

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtNombreNew;
    private javax.swing.JTextField txtNombreUpdate;
    // End of variables declaration//GEN-END:variables
}
