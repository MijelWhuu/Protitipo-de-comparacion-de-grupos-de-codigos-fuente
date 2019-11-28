/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz.expedientes;

import baseSistema.Utilidades;
import bl.catalogos.ActosBl;
import bl.catalogos.ProcedimientosBl;
import bl.catalogos.UbicacionBl;
import bl.expedientes.ExpedientesUpdateBl;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entitys.BASE_Combos;
import entitys.ExpedienteEntity;
import entitys.ProcedimientoEntity;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author raul
 */
public class ExpedienteUbicacion extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    ExpedientesUpdateBl bl = new ExpedientesUpdateBl();
    Utilidades u = new Utilidades();
    
    AutoCompleteSupport t1_autocomplete;
    ActosBl blActo = new ActosBl();
    UbicacionBl blUbicacion = new UbicacionBl();
    ProcedimientosBl blPro = new ProcedimientosBl();
    
    /**
     * Creates new form ExpedienteUbicacion
     */
    public ExpedienteUbicacion() {
        initComponents();
    }

    
    public ExpedienteUbicacion(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
    }
    
    private void botonF12(){
        dispose();
    }
    
    public final void mapeoTeclas(){
        ActionMap mapaAccion = this.getActionMap();
        InputMap map = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),"accion_F2"); mapaAccion.put("accion_F2",Accion_F2());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0),"accion_F12"); mapaAccion.put("accion_F12",Accion_F12());
    }
    public AbstractAction Accion_F2(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF12();}};}
    
    //Para cambiar el focus cuando se encuentra en una tabla
    public AbstractAction Accion_tab(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();  
    }};}
    
    void InicializaCombo(javax.swing.JComboBox cmb){
        cmb.removeAllItems();
        cmb.addItem(new BASE_Combos("-1","-- ELIGE UNA OPCION --",""));
    }
    
    public void cargaCmbNombre(String expe){
        String id_exp = blUbicacion.existeExpediente(expe);
        if(id_exp.isEmpty()){
            setTitle("Ubicación de Expedientes");
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El expediente no esta resguardado por ninguna persona", "Aviso", 1);
            lblNombre.setText("");
        }else{
            String nombre = blUbicacion.dameNombre(id_exp);
            lblNombre.setText(nombre);
            lblNombre.setVisible(true);
        }
    }
    
    public void cargaCmbNombre(){
        BASE_Combos cmb = new BASE_Combos();
        ArrayList array = new ArrayList();
        array = blUbicacion.CargaCombo();
        u.inicializaCombo(t0_cmbNombre, cmb, array);
    }
    
    private void botonF2(){
        BASE_Combos e = (BASE_Combos)t0_cmbNombre.getSelectedItem();
        if(e.getId().equals("-1")){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No has seleccionado el nombre la persona a quien se asignará el resguardo", "Aviso", 2);
        }else{
            if(lblExpediente.getText().equals("")){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No has seleccionado el expediente a resguardar", "Aviso", 2);
            }else{
                String exp = lblExpediente.getText();
                String id = e.getId();
                String prestador = sesion.getId_usuario();
                if(blUbicacion.GuardaUbicacion(exp, id, prestador)){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Los datos se han guardado correctamente", "Aviso", 1);
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Ha ocurrido un error al tratar de guardar los datos", "Aviso", 0);
                }
            }
        }
    }
    
    private void cargaTabbedPane0(String expediente){
        ExpedienteEntity expe = bl.getExpediente(expediente);
        cargaCmbNombre(expe.getExpediente());
        lblExpediente.setText(expe.getExpediente());
        t0_txtExpediente.setText(expe.getExpediente());
        t0_dateFecha.setDate(expe.getFecha());
        t0_cmbTipo.setSelectedItem(expe.getTipoProcedimiento());
        t0_cmbNombre.setSelectedIndex(0);
    }
    
   
    
    private void seleccionarDefaultProcedimiento(){
        for(int i=0; i<t0_cmbTipo.getItemCount(); i++){
            if(((ProcedimientoEntity)t0_cmbTipo.getItemAt(i)).getSeleccionar()){
                t0_cmbTipo.setSelectedIndex(i);
                i=t0_cmbTipo.getItemCount()+5;
            }
        }
    }
    
     private void inicializaCmbProcedimientoT0(){
        u.inicializaCombo(t0_cmbTipo, new ProcedimientoEntity(), blPro.getProcedimientos());
        seleccionarDefaultProcedimiento();
    }
     
     private void limpiarT0(){
        u.cleanComponent(lblExpediente,t0_txtExpediente,t0_dateFecha);
        inicializaCmbProcedimientoT0();
    }
    
    private void cargaExpediente(String expediente){
        limpiarT0();
        if(expediente.length()==0){   
            setTitle("Ubicación de Expedientes");
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el número de Expediente.\nPor favor ingrese un número de expediente", "Aviso", 1);
            t0_txtBuscar.requestFocus();
        }else{
            if(!bl.existeExpediente(expediente)){
                setTitle("Ubicación de Expedientes");
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El número de Expediente no existe.\nPor favor verifique el número de expediente o ingrese uno nuevo", "Aviso", 1);
                t0_txtBuscar.requestFocus();
            }else{
                setTitle("Ubicación de Expedientes - Ubicando el expediente: "+expediente);
                lblExpediente.setText(expediente);
                t0_txtBuscar.setText("");
                t0_txtExpediente.requestFocus();
                cargaTabbedPane0(expediente);
            }
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        t0_txtBuscar = new javax.swing.JTextField();
        t0_btnBuscar = new javax.swing.JButton();
        lblExpediente = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        t0_txtExpediente = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        t0_dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        t0_cmbTipo = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        t0_cmbNombre = new javax.swing.JComboBox<>();

        setClosable(true);
        setTitle("Ubicar Expediente");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_ubicar_expediente.jpg"))); // NOI18N

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
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

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel10.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("Número de Expediente a buscar");
        jPanel10.add(jLabel10);
        jLabel10.setBounds(80, 20, 220, 30);

        t0_txtBuscar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t0_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t0_txtBuscarKeyPressed(evt);
            }
        });
        jPanel10.add(t0_txtBuscar);
        t0_txtBuscar.setBounds(300, 20, 150, 30);

        t0_btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t0_btnBuscar.setText("Buscar Expediente");
        t0_btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t0_btnBuscarActionPerformed(evt);
            }
        });
        jPanel10.add(t0_btnBuscar);
        t0_btnBuscar.setBounds(470, 20, 170, 30);

        lblExpediente.setEnabled(false);
        jPanel10.add(lblExpediente);
        lblExpediente.setBounds(660, 20, 80, 40);

        jTextField1.setText("jTextField1");
        jPanel10.add(jTextField1);
        jTextField1.setBounds(450, 80, 59, 20);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de Expediente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Número de Expediente");

        t0_txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        t0_txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Fecha");

        t0_dateFecha.setDateFormatString("dd/MMM/yyyy");
        t0_dateFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de Procedimiento");

        t0_cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(t0_txtExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(t0_dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(t0_cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(43, 43, 43))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(t0_txtExpediente, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t0_dateFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t0_cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resguardo Actual", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        lblNombre.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblNombre.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Se Resguardará Por", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nombre");

        t0_cmbNombre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(t0_cmbNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(t0_cmbNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 3, Short.MAX_VALUE))))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void t0_txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t0_txtBuscarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
        cargaExpediente(t0_txtBuscar.getText());
    }//GEN-LAST:event_t0_txtBuscarKeyPressed

    private void t0_btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t0_btnBuscarActionPerformed
        cargaExpediente(t0_txtBuscar.getText());
    }//GEN-LAST:event_t0_btnBuscarActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        lblExpediente.setVisible(false);
        lblNombre.setText("");
        cargaCmbNombre();
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblExpediente;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JButton t0_btnBuscar;
    private javax.swing.JComboBox<String> t0_cmbNombre;
    private javax.swing.JComboBox t0_cmbTipo;
    private com.toedter.calendar.JDateChooser t0_dateFecha;
    private javax.swing.JTextField t0_txtBuscar;
    private javax.swing.JTextField t0_txtExpediente;
    // End of variables declaration//GEN-END:variables
}
