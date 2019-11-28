package interfaz.utilidades;

import baseGeneral.Tabla;
import baseSistema.Utilidades;
import bl.catalogos.AutoridadBl;
import bl.utilidades.BusquedaAutoridadesBl;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entitys.AutoridadEntity;
import entitys.BuscarAutoridadEntity;
import entitys.MunicipioEntity;
import entitys.SesionEntity;
import entitys.TipoAutoridadEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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
public class BusquedaAutoridades extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    AutoridadBl blAuto = new AutoridadBl();
    BusquedaAutoridadesBl bl = new BusquedaAutoridadesBl();
    Tabla tabla;
    AutoCompleteSupport autocomplete;
    boolean buscar = false;
      
    /**
     * Creates new form ArchivarExpedientes
     */
    public BusquedaAutoridades() {
        initComponents();
    }

    public BusquedaAutoridades(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        declaracionTabla();
        inicializaCmbAutoridad();
        botonEsc();
        lblAutoridad.setVisible(false);
    }
       
    private void declaracionTabla(){
        jTable1.getTableHeader().setResizingAllowed(false);
        tabla = new Tabla(jTable1);
        String[][] titulos={
            {"no","expediente","fecha","procedimiento","acto"}
           ,{"No","Expediente","Fecha","Tipo de procedimiento","Acto Impugnado"}
        };
        tabla.setTabla(titulos);
        tabla.setDefaultValues("","","","","");
        tabla.setAnchoColumnas(25,120,100,300,1000);
        tabla.setAlturaRow(30);
        tabla.alinear(0,2);
        tabla.alinear(1,2);
        tabla.alinear(2,2);
        //tabla.cellLineWrap(4);
        tabla.cebraTabla();
    }
    
    private void inicializaCmbAutoridad(){
        if(autocomplete != null && autocomplete.isInstalled())
            autocomplete.uninstall();
        EventList<AutoridadEntity> items = new BasicEventList<>();
        ArrayList array = blAuto.getAutoridadesComplete();
        items.add(new AutoridadEntity());
        for(Object obj : array)
            items.add((AutoridadEntity)obj);
        autocomplete = AutoCompleteSupport.install(cmbAutoridad, items);
        autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);        
    }
    
    private Boolean validar(){
        if(cmbAutoridad.getItemCount()>0 && cmbAutoridad.getSelectedIndex()<=0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado una Autoridad", "Alerta", 2);
            cmbAutoridad.requestFocus();
            return false;
        }
        return true;
    }
    
    private void llenarTabla(ArrayList array){
        for (Object array1 : array) {
            BuscarAutoridadEntity bae = (BuscarAutoridadEntity)array1;
            Object obj[] = new Object[5];
            obj[0] = tabla.getRenglones()+1;
            obj[1] = bae.getExpediente();
            obj[2] = u.dateToStringTabla(bae.getFecha());
            obj[3] = bae.getProcedimiento();
            obj[4] = bae.getActo();
            tabla.addRenglonArray(obj);
        }        
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
        
        /////Para cambiar el focus cuando se encuentra en una tabla
        jTable1.getActionMap().put("Accion_tab",Accion_tab());
        jTable1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        /////
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
        txtAutoridad.setText("");
        txtMunicipio.setText("");
        txtTipo.setText("");
        lblAutoridad.setText("");
        tabla.clearRows();
        cmbAutoridad.requestFocus();
        buscar = false;
    }
    
    private void botonF2(){
        if(validar()){
            tabla.clearRows();
            AutoridadEntity auto = (AutoridadEntity) cmbAutoridad.getSelectedItem();
            txtAutoridad.setText(auto.getNombre());
            txtMunicipio.setText(auto.getMun().getNombre());
            txtTipo.setText(auto.getTipo().getNombre());
            lblAutoridad.setText(auto.getId_autoridad());
            if(bl.existeAutoridad(auto.getId_autoridad())>0){
                ArrayList array = bl.getAutoridades(auto.getId_autoridad());
                if(array != null && !array.isEmpty()){
                    llenarTabla(array);
                }
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay un expediente con la Autoridad señalada", "Alerta", 2);
            }
            inicializaCmbAutoridad();
        }
    }
    
    private void botonF4(){
        if(tabla.getRenglones()>0){
            try{
                String dir = u.callVentanaGuardarPDF();
                if(!dir.equals("")){
                    if(bl.reporteToPDF("busquedaAutoridades.jasper",dir,lblAutoridad.getText())){
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
    
    private void botonF5(){}
    
    private void botonF6(){
        if(tabla.getRenglones()>0){
            if(bl.reporteImprimir("busquedaAutoridades.jasper",lblAutoridad.getText())){
                JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al imprimir el reporte","Error",JOptionPane.ERROR_MESSAGE);
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

        jLabel1 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbAutoridad = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTipo = new javax.swing.JTextField();
        txtAutoridad = new javax.swing.JTextField();
        txtMunicipio = new javax.swing.JTextField();
        lblAutoridad = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Búsqueda de Autoridades");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/busquedaAutoridades.jpg"))); // NOI18N
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtro de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Autoridades");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(10, 30, 90, 30);

        cmbAutoridad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbAutoridad);
        cmbAutoridad.setBounds(100, 30, 750, 30);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("F2 - Buscar");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(860, 30, 130, 30);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setFocusable(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autoridad", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Tipo de Autoridad");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 110, 130, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Autoridad");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 30, 130, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Municipio");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(10, 70, 130, 30);

        txtTipo.setEditable(false);
        txtTipo.setBackground(new java.awt.Color(255, 255, 255));
        txtTipo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel2.add(txtTipo);
        txtTipo.setBounds(150, 110, 840, 30);

        txtAutoridad.setEditable(false);
        txtAutoridad.setBackground(new java.awt.Color(255, 255, 255));
        txtAutoridad.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jPanel2.add(txtAutoridad);
        txtAutoridad.setBounds(150, 30, 840, 30);

        txtMunicipio.setEditable(false);
        txtMunicipio.setBackground(new java.awt.Color(255, 255, 255));
        txtMunicipio.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel2.add(txtMunicipio);
        txtMunicipio.setBounds(150, 70, 840, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblAutoridad, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 316, Short.MAX_VALUE)
                        .addComponent(btnF4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAutoridad, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        botonEsc();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        botonF2();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF4ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF4ActionPerformed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.JComboBox cmbAutoridad;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAutoridad;
    private javax.swing.JTextField txtAutoridad;
    private javax.swing.JTextField txtMunicipio;
    private javax.swing.JTextField txtTipo;
    // End of variables declaration//GEN-END:variables
}
