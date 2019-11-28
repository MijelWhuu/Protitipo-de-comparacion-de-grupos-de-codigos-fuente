package interfaz.reportes;

import baseSistema.Utilidades;
import bl.catalogos.AutosBl;
import bl.reportes.ReportesDinamicosAutosBl;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entitys.AutoEntity;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.JOptionPane;

/**
 *
 * @author mavg
 */
public class ReportesDinamicosAutos extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    AutosBl blTipo = new AutosBl();
    ReportesDinamicosAutosBl bl = new ReportesDinamicosAutosBl();
    Utilidades u = new Utilidades();
    AutoCompleteSupport autocomplete;
    String jasper1 = "reporteDinamicoAuto.jasper";
    String jasper2 = "reporteDinamicoListaAutos.jasper";
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public ReportesDinamicosAutos(){
        initComponents();
    }
    
    public ReportesDinamicosAutos(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        inicializaComboTipo();
        u.formatoJDateChooser(dateInicio);
        u.formatoJDateChooser(dateFin);
        dateInicio.setDate(cal.getTime());
        dateFin.setDate(cal.getTime());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void inicializaComboTipo(){
        EventList<AutoEntity> items = new BasicEventList<>();
        ArrayList array = blTipo.getAutos();
        items.add(new AutoEntity());
        for(Object obj : array)
            items.add((AutoEntity)obj);
        autocomplete = AutoCompleteSupport.install(cmbTipo, items);
        autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);
        cmbTipo.setSelectedIndex(0);
    }
    
    private ArrayList getSeleccionados(){
        ArrayList array = new ArrayList();
        for(int i=0; i<cmbSeleccion.getItemCount(); i++){
            AutoEntity auto = (AutoEntity)cmbSeleccion.getItemAt(i);
            array.add(auto.getId_tipo_auto());
        }
        return array;
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
        u.cleanComponent(dateInicio,dateFin,cmbTipo);
        rbtnCifras.setSelected(true);
        cmbSeleccion.removeAllItems();
    }
    
    private void botonF2(){
        if(cmbSeleccion.getItemCount()>0){
            if(rbtnCifras.isSelected()){
                bl.reporteVisualizarCifra(jasper1, dateInicio.getDate(), dateFin.getDate(), getSeleccionados());
            }else{
                bl.reporteVisualizarListado(jasper2, dateInicio.getDate(), dateFin.getDate(), getSeleccionados());
            }
        }else{
            JOptionPane.showInternalMessageDialog(this,"No hay Autos Seleccionados","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void botonF4(){
        if(cmbSeleccion.getItemCount()>0){
            if(rbtnCifras.isSelected()){
                try{
                    String dir = u.callVentanaGuardarPDF();
                    if(!dir.equals("")){
                        if(bl.reporteToPDFCifra(jasper1,dir,dateInicio.getDate(), dateFin.getDate(), getSeleccionados())){
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
                        if(bl.reporteToPDFListado(jasper2,dir,dateInicio.getDate(), dateFin.getDate(), getSeleccionados())){
                            JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el reporte","Error",JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el reporte","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }else{
            JOptionPane.showInternalMessageDialog(this,"No hay Autos Seleccionados","Aviso",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void botonF5(){}
    
    private void botonF6(){
        if(cmbSeleccion.getItemCount()>0){
            if(rbtnCifras.isSelected()){
                bl.reporteImprimirCifra(jasper1, dateInicio.getDate(), dateFin.getDate(), getSeleccionados());
            }else{
                bl.reporteImprimirListado(jasper2, dateInicio.getDate(), dateFin.getDate(), getSeleccionados());
            }
        }else{
            JOptionPane.showInternalMessageDialog(this,"No hay Autos Seleccionados","Aviso",JOptionPane.INFORMATION_MESSAGE);
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
        btnEsc = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        cmbSeleccion = new javax.swing.JComboBox();
        btnMenos = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        rbtnCifras = new javax.swing.JRadioButton();
        rbtnListado = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        dateFin = new com.toedter.calendar.JDateChooser();
        dateInicio = new com.toedter.calendar.JDateChooser();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Estado Procesal");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/estadoPro.jpg"))); // NOI18N

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Reporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Autos Seleccionados");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 260, 150, 30);

        cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTipo.setMaximumRowCount(15);
        jPanel2.add(cmbTipo);
        cmbTipo.setBounds(10, 180, 920, 30);

        cmbSeleccion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbSeleccion.setToolTipText("");
        jPanel2.add(cmbSeleccion);
        cmbSeleccion.setBounds(10, 290, 920, 30);

        btnMenos.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnMenos.setText("Quitar Auto");
        btnMenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenosActionPerformed(evt);
            }
        });
        jPanel2.add(btnMenos);
        btnMenos.setBounds(760, 330, 170, 30);

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAgregar.setText("Seleccionar Auto");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel2.add(btnAgregar);
        btnAgregar.setBounds(760, 220, 170, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Lista de Autos");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(10, 150, 110, 30);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo de Reporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        buttonGroup1.add(rbtnCifras);
        rbtnCifras.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnCifras.setSelected(true);
        rbtnCifras.setText("Cifras");

        buttonGroup1.add(rbtnListado);
        rbtnListado.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnListado.setText("Listado");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtnCifras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbtnListado, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(rbtnCifras)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtnListado)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel1);
        jPanel1.setBounds(10, 30, 190, 110);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Periodo de Evaluación del Reporte", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Fecha Inicial");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Fecha Final");

        dateFin.setDateFormatString("dd/MMM/yyyy");
        dateFin.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        dateInicio.setDateFormatString("dd/MMM/yyyy");
        dateInicio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateFin, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel3);
        jPanel3.setBounds(210, 30, 300, 110);

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F2 - Visualizar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

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
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnF6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnF4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnF12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if(cmbTipo.getItemCount()>0 && cmbTipo.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Auto", "Alerta", 2);
            cmbTipo.requestFocus();
        }else{
            cmbSeleccion.addItem(cmbTipo.getSelectedItem());
            cmbSeleccion.setSelectedIndex(cmbSeleccion.getItemCount()-1);
            cmbTipo.setSelectedIndex(0);
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnMenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenosActionPerformed
        if(cmbSeleccion.getItemCount()>0){
            cmbSeleccion.removeItemAt(cmbSeleccion.getSelectedIndex());
        }
    }//GEN-LAST:event_btnMenosActionPerformed

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF4ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF4ActionPerformed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.JButton btnMenos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbSeleccion;
    private javax.swing.JComboBox cmbTipo;
    private com.toedter.calendar.JDateChooser dateFin;
    private com.toedter.calendar.JDateChooser dateInicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JRadioButton rbtnCifras;
    private javax.swing.JRadioButton rbtnListado;
    // End of variables declaration//GEN-END:variables

    
    
}
