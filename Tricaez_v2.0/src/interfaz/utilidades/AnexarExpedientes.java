package interfaz.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.utilidades.AnexarExpedientesBl;
import entitys.ExpedientesAnexoEntity;
import entitys.SesionEntity;
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
public class AnexarExpedientes extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    AnexarExpedientesBl bl = new AnexarExpedientesBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public AnexarExpedientes() {
        initComponents();
    }

    public AnexarExpedientes(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        formatearCampos();
        botonEsc();
    }
    
    private void formatearCampos(){
        txtPrincipal.setDocument(new JTextMascara(29,true,"todo"));
        txtSecundario.setDocument(new JTextMascara(29,true,"todo"));
        dateFecha.getDateEditor().setEnabled(false);
        ((com.toedter.calendar.JTextFieldDateEditor)dateFecha.getDateEditor()).setDisabledTextColor(java.awt.Color.darkGray);
    }
    
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
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de botones">
    private void botonEsc(){
        txtPrincipal.setText("");
        txtSecundario.setText("");
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        dateFecha.setDate(cal.getTime());
        txtPrincipal.requestFocus();
    }
    
    private Boolean validarExpedientes(){
        Boolean flag = false;
        if(txtPrincipal.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Expediente Principal es obligatorio", "Alerta", 2);
            txtPrincipal.requestFocus();
        }else if(txtSecundario.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Expediente Secundario es obligatorio", "Alerta", 2);
            txtSecundario.requestFocus();
        }else if(!bl.existeExpediente(txtPrincipal.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El número de Expediente Principal no existe o ha sido anexado anteriormente", "Alerta", 2);
            txtPrincipal.requestFocus();
        }else if(!bl.existeExpediente(txtSecundario.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El número de Expediente Secundario no existe o ha sido anexado anteriormente", "Alerta", 2);
            txtSecundario.requestFocus();
        }else{
            flag = true;
        }
        return flag;
    }
    
    private void botonF2(){
        if(validarExpedientes()){
            ExpedientesAnexoEntity exp = new ExpedientesAnexoEntity(txtPrincipal.getText(),txtSecundario.getText(),dateFecha.getDate());
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea acumular el Expediente: "+exp.getSecundario()
                    +" al Expediente: "+exp.getPrincipal()+"?", "Alerta", 0)==0){
                ErrorEntity error = bl.saveAnexos(exp, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"El Expediente: "+exp.getSecundario()+" se acumuló correctamente "
                            + "al Expediente: "+exp.getPrincipal(), "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
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

        jLabel1 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtSecundario = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel9 = new javax.swing.JLabel();
        txtPrincipal = new javax.swing.JTextField();

        setClosable(true);
        setTitle("Acumulación de Expedientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Anexar_expedientes.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Esc - Cancelar");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton6.setText("F2 - Acumular");
        jButton6.setFocusable(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Expedientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel10.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Fecha de Anexo");
        jPanel10.add(jLabel10);
        jLabel10.setBounds(20, 130, 140, 30);

        txtSecundario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtSecundario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSecundario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSecundarioKeyPressed(evt);
            }
        });
        jPanel10.add(txtSecundario);
        txtSecundario.setBounds(170, 80, 160, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expediente a Anexar");
        jPanel10.add(jLabel11);
        jLabel11.setBounds(20, 80, 140, 30);

        dateFecha.setDateFormatString("dd/MMM/yyyy");
        dateFecha.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jPanel10.add(dateFecha);
        dateFecha.setBounds(170, 130, 160, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Expediente Principal");
        jPanel10.add(jLabel9);
        jLabel9.setBounds(20, 30, 140, 30);

        txtPrincipal.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtPrincipal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPrincipal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPrincipalKeyPressed(evt);
            }
        });
        jPanel10.add(txtPrincipal);
        txtPrincipal.setBounds(170, 30, 160, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnF12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        botonEsc();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        botonF2();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void txtPrincipalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrincipalKeyPressed
        /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(txtPrincipal.getText().length()>0){
                String expediente = u.completaNumeroExpediente(txtPrincipal.getText());
                if(expediente!=null){
                    txtPrincipal.setText(expediente);
                }
            }
        }*/
    }//GEN-LAST:event_txtPrincipalKeyPressed

    private void txtSecundarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSecundarioKeyPressed
        /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(txtSecundario.getText().length()>0){
                String expediente = u.completaNumeroExpediente(txtSecundario.getText());
                if(expediente!=null){
                    txtSecundario.setText(expediente);
                }
            }
        }*/
    }//GEN-LAST:event_txtSecundarioKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JTextField txtPrincipal;
    private javax.swing.JTextField txtSecundario;
    // End of variables declaration//GEN-END:variables
}
