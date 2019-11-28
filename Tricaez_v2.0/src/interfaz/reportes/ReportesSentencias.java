package interfaz.reportes;

import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.operaciones.RegistroSentenciasBl;
import bl.promociones.PromocionesBl;
import bl.reportes.ReportesSentenciasBl;
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
public class ReportesSentencias extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    ReportesSentenciasBl bl = new ReportesSentenciasBl();
    RegistroSentenciasBl blNew = new RegistroSentenciasBl();
    Utilidades u = new Utilidades();
    String jasper1 = "sentenciaSimple.jasper";
    String jasper2 = "sentenciaGrupo.jasper";
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public ReportesSentencias() {
        initComponents();
    }

    public ReportesSentencias(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        seleccion();
        txtFolio.setDocument(new JTextMascara(7,true,"numerico"));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtAnioListado.setDocument(new JTextMascara(4,true,"numerico"));
    }
    
    private void seleccion(){
        txtFolio.setText("");
        txtAnio.setText("");
        txtAnioListado.setText("");
        if(rbtnPromocion.isSelected()){
            txtAnioListado.setEnabled(false);
            txtFolio.setEnabled(true);
            txtAnio.setEnabled(true);
            txtFolio.requestFocus();
        }else{
            txtFolio.setEnabled(false);
            txtAnio.setEnabled(false);
            txtAnioListado.setEnabled(true);
            txtAnioListado.requestFocus();
        }
    }
    
    private Boolean valida(){
        if(rbtnPromocion.isSelected()){
            if(txtFolio.getText().length()==0){
                JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el número de Folio","Aviso",JOptionPane.INFORMATION_MESSAGE);
                txtFolio.requestFocus();
                return false;
            }else if(blNew.getSentencia(new Integer(txtFolio.getText()), getAnio())==null){
                JOptionPane.showInternalMessageDialog(this,"El número de Sentencia no existe","Aviso",JOptionPane.INFORMATION_MESSAGE);
                return false;
            }else{
                return true;
            }
        }
        return true;
    }
    
    private Integer getAnio(){
        Integer anio = cal.get(Calendar.YEAR);
        if(rbtnPromocion.isSelected()){
            if(txtAnio.getText().length()!=0){
                Integer num = u.verificaInteger(txtAnio.getText());
                if(num!=null)
                    anio = num;
            }
        }else{
            if(txtAnioListado.getText().length()!=0){
                Integer num = u.verificaInteger(txtAnioListado.getText());
                if(num!=null)
                    anio = num;
            }
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
        if(valida()){
            if(rbtnPromocion.isSelected()){
                bl.reporteVisualizarSimple(jasper1,new Integer(txtFolio.getText()),getAnio());
            }else{
                bl.reporteVisualizarGrupo(jasper2,getAnio());
            }
        }
    }
    
    private void botonF4(){
        if(valida()){
            if(rbtnPromocion.isSelected()){
                try{
                    String dir = u.callVentanaGuardarPDF();
                    if(!dir.equals("")){
                        if(bl.reporteToPDFSimple(jasper1,dir,new Integer(txtFolio.getText()),getAnio())){
                            JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el reporte","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                try{
                    String dir = u.callVentanaGuardarPDF();
                    if(!dir.equals("")){
                        if(bl.reporteToPDFGrupo(jasper2,dir,getAnio())){
                            JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el reporte","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    private void botonF5(){}
    
    private void botonF6(){
        if(valida()){
            if(rbtnPromocion.isSelected()){
                bl.reporteImprimirSimple(jasper1,new Integer(txtFolio.getText()),getAnio());
            }else{
                bl.reporteImprimirGrupo(jasper2,getAnio());
            }
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        rbtnPromocion = new javax.swing.JRadioButton();
        rbtnListado = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtAnioListado = new javax.swing.JTextField();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Reportes de Sentencias - Resoluciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/repoSentencias.jpg"))); // NOI18N
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

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel10.setLayout(null);

        buttonGroup1.add(rbtnPromocion);
        rbtnPromocion.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnPromocion.setSelected(true);
        rbtnPromocion.setText("Información de Sentencia - Resolución");
        rbtnPromocion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnPromocionActionPerformed(evt);
            }
        });
        jPanel10.add(rbtnPromocion);
        rbtnPromocion.setBounds(10, 30, 300, 30);

        buttonGroup1.add(rbtnListado);
        rbtnListado.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnListado.setText("Listado de Sentencias - Resoluciones por Año");
        rbtnListado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnListadoActionPerformed(evt);
            }
        });
        jPanel10.add(rbtnListado);
        rbtnListado.setBounds(10, 60, 330, 30);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Folio");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Año");

        txtFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAnio, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel1);
        jPanel1.setBounds(20, 100, 320, 60);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Año");

        txtAnioListado.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAnioListado, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(187, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAnioListado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel2);
        jPanel2.setBounds(20, 170, 320, 60);

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
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 591, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnF6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
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

    private void rbtnPromocionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnPromocionActionPerformed
        seleccion();
    }//GEN-LAST:event_rbtnPromocionActionPerformed

    private void rbtnListadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnListadoActionPerformed
        seleccion();
    }//GEN-LAST:event_rbtnListadoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton rbtnListado;
    private javax.swing.JRadioButton rbtnPromocion;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtAnioListado;
    private javax.swing.JTextField txtFolio;
    // End of variables declaration//GEN-END:variables
}
