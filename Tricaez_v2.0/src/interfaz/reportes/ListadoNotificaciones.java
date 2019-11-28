package interfaz.reportes;

import baseSistema.Utilidades;
import bl.reportes.ListadoNotificacionesBl;
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
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class ListadoNotificaciones extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    ListadoNotificacionesBl bl = new ListadoNotificacionesBl();
    Utilidades u = new Utilidades();
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    String [] jasper = {
        "-1",
        "cargaAnual.jasper",
        "cargaMensual.jasper",
        "cargaExtraAnual.jasper",
        "cargaExtraMensual.jasper",
        "descargaAnual.jasper",
        "descargaMensual.jasper",
        "descargaExtraAnual.jasper",
        "descargaExtraMensual.jasper"
    };
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public ListadoNotificaciones() {
        initComponents();
    }

    public ListadoNotificaciones(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        Integer anio = cal.get(Calendar.YEAR);
        txtAnio.setText(anio.toString());
        txtAnio.setEnabled(false);
        cmbMes.setEnabled(false);
    }
    
    private Integer getAnio(){
        Integer anio = cal.get(Calendar.YEAR);
        if(txtAnio.getText().length()!=0){
            Integer num = u.verificaInteger(txtAnio.getText());
            if(num!=null)
                anio = num;
        }
        return anio;
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
    private void botonEsc(){}
        
    private void botonF2(){
        if(cmbCatalogo.getSelectedIndex()!=0){
            if(cmbCatalogo.getSelectedIndex()%2!=0){
                bl.reporteVisualizar(jasper[cmbCatalogo.getSelectedIndex()],getAnio());
            }else{
                if(cmbMes.getSelectedIndex()!=0)
                    bl.reporteVisualizar(jasper[cmbCatalogo.getSelectedIndex()],getAnio(),cmbMes.getSelectedIndex());
                else
                    JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el mes","Aviso",JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un reporte","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void botonF4(){
        if(cmbCatalogo.getSelectedIndex()!=0){
            try{
                String dir = u.callVentanaGuardarPDF();
                if(!dir.equals("")){
                    if(cmbCatalogo.getSelectedIndex()%2!=0){
                        if(bl.reporteToPDF(jasper[cmbCatalogo.getSelectedIndex()],dir,getAnio())){
                            JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el listado","Error",JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el listado","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }else{
                        if(cmbMes.getSelectedIndex()!=0){
                            if(bl.reporteToPDF(jasper[cmbCatalogo.getSelectedIndex()],dir,getAnio(),cmbMes.getSelectedIndex())){
                                JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el listado","Error",JOptionPane.ERROR_MESSAGE);
                            }else{
                                JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el listado","Aviso",JOptionPane.INFORMATION_MESSAGE);
                            }
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el mes","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else{
            JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un reporte","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void botonF5(){}
    
    private void botonF6(){
        if(cmbCatalogo.getSelectedIndex()!=0){
            if(cmbCatalogo.getSelectedIndex()%2!=0){
                bl.reporteImprimir(jasper[cmbCatalogo.getSelectedIndex()],getAnio());
            }else{
                if(cmbMes.getSelectedIndex()!=0)
                    bl.reporteImprimir(jasper[cmbCatalogo.getSelectedIndex()],getAnio(),cmbMes.getSelectedIndex());
                else
                    JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el mes","Aviso",JOptionPane.INFORMATION_MESSAGE);
            }
        }else{
            JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un reporte","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
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
        btnF2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cmbCatalogo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbMes = new javax.swing.JComboBox<>();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Reportes Estadísticos de Notificaciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/repor_esta_noti.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F2 - Visualizar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reportes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel10.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Lista de Reportes");
        jPanel10.add(jLabel9);
        jLabel9.setBounds(10, 40, 180, 30);

        cmbCatalogo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbCatalogo.setMaximumRowCount(10);
        cmbCatalogo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Elija una opción", "Carga (Anual)", "Carga (Mensual)", "Carga Extra (Anual)", "Carga Extra (Mensual)", "Descarga (Anual)", "Descarga (Mensual)", "Descarga Extra (Anual)", "Descarga Extra (Mensual)" }));
        cmbCatalogo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbCatalogoActionPerformed(evt);
            }
        });
        jPanel10.add(cmbCatalogo);
        cmbCatalogo.setBounds(10, 70, 430, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Mes");
        jPanel10.add(jLabel2);
        jLabel2.setBounds(130, 130, 40, 30);

        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel10.add(txtAnio);
        txtAnio.setBounds(50, 130, 70, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Año");
        jPanel10.add(jLabel3);
        jLabel3.setBounds(10, 130, 40, 30);

        cmbMes.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbMes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Elija una opción", "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE" }));
        jPanel10.add(cmbMes);
        cmbMes.setBounds(170, 130, 150, 30);

        btnF4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF4.setText("F4 - Guardar como PDF");
        btnF4.setFocusable(false);
        btnF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF4ActionPerformed(evt);
            }
        });

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("F6 - Imprimir");
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
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnF6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 687, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void btnF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF4ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF4ActionPerformed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void cmbCatalogoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbCatalogoActionPerformed
        if(cmbCatalogo.getSelectedIndex()==0){
            txtAnio.setEnabled(false);
            cmbMes.setEnabled(false);
        }else{
            txtAnio.setEnabled(true);
            cmbMes.setEnabled(true);
            if(cmbCatalogo.getSelectedIndex()%2!=0)
                cmbMes.setEnabled(false);
        }
    }//GEN-LAST:event_cmbCatalogoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.JComboBox cmbCatalogo;
    private javax.swing.JComboBox<String> cmbMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JTextField txtAnio;
    // End of variables declaration//GEN-END:variables
}
