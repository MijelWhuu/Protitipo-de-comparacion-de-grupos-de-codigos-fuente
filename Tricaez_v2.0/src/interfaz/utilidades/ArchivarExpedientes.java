package interfaz.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.Tabla;
import baseSistema.Utilidades;
import bl.utilidades.ArchivarExpedientesBl;
import entitys.ExpedienteEntity;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class ArchivarExpedientes extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    ArchivarExpedientesBl bl = new ArchivarExpedientesBl();
    Utilidades u = new Utilidades();
    
    Tabla tabla;
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public ArchivarExpedientes() {
        initComponents();
    }

    public ArchivarExpedientes(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        formatearCampos();
        declaracionTabla();
        cargarCombosCajas();
        obtenerFechaTiempoT2();
        cargarComboExpedientes();
    }
    
    private void formatearCampos(){
        t1_txtCajaAlta.setDocument(new JTextMascara(29,true,"todo"));
        t1_txtCajaModificar.setDocument(new JTextMascara(29,true,"todo"));
        t2_dateFecha.getDateEditor().setEnabled(false);
        ((com.toedter.calendar.JTextFieldDateEditor)t2_dateFecha.getDateEditor()).setDisabledTextColor(java.awt.Color.darkGray);
    }
    
    private void cargarCombosCajas(){
        ArrayList array = bl.getCajas();
        u.inicializaCombo(t1_cmbCajaModificar, "[Elija una Caja]", array);
        u.inicializaCombo(t1_cmbCajaEliminar, "[Elija una Caja]", array);
        u.inicializaCombo(t2_cmbCaja, "[Elija una Caja]", array);
    }
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane1">
    private void limpiarCampoT1(){
        t1_txtCajaAlta.setText("");
        t1_txtCajaModificar.setText("");
    }
    
    private void saveCaja(){
        if(t1_txtCajaAlta.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el número o nombre de caja", "Alerta", 2);
            t1_txtCajaAlta.requestFocus();
        }else if(bl.existeCaja(t1_txtCajaAlta.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El número o nombre de caja ya existe, ingrese uno diferente", "Alerta", 2);
            t1_txtCajaAlta.requestFocus();
        }else{
            ErrorEntity error = bl.saveCaja(t1_txtCajaAlta.getText(), sesion.getId_usuario(), sesion.getFechaActual());
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                limpiarCampoT1();
                tabla.clearRows();
                cargarComboExpedientes();
                obtenerFechaTiempoT2();
                cargarCombosCajas();
                t1_txtCajaAlta.requestFocus();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
            }
        }
    }
    
    private void updateCaja(){
        if(t1_cmbCajaModificar.getItemCount()>0 && t1_cmbCajaModificar.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No ha seleccionado una caja", "Alerta", 2);
            t1_cmbCajaModificar.requestFocus();
        }else if(t1_txtCajaModificar.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el nuevo número o nombre de caja", "Alerta", 2);
            t1_txtCajaModificar.requestFocus();
        }else if(!t1_cmbCajaModificar.getSelectedItem().toString().equals(t1_txtCajaModificar.getText()) && bl.existeCaja(t1_txtCajaModificar.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El número o nombre de caja ya existe, ingrese uno diferente", "Alerta", 2);
            t1_txtCajaModificar.requestFocus();
        }else{
            ErrorEntity error = bl.updateCaja(t1_cmbCajaModificar.getSelectedItem().toString(), 
                    t1_txtCajaModificar.getText(), sesion.getId_usuario(), sesion.getFechaActual());
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                limpiarCampoT1();
                tabla.clearRows();
                cargarComboExpedientes();
                obtenerFechaTiempoT2();
                cargarCombosCajas();
                t1_txtCajaAlta.requestFocus();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
            }
        }
    }
    
    private void deleteCaja(){
        if(t1_cmbCajaEliminar.getItemCount()>0 && t1_cmbCajaEliminar.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No ha seleccionado una caja", "Alerta", 2);
            t1_cmbCajaEliminar.requestFocus();
        }else{
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "Los expedientes contenidos en la caja quedarán sin archivar\n¿Desea eliminar la caja seleccionada?", "Alerta", 0)==0){
                ErrorEntity error = bl.deleteCaja(t1_cmbCajaEliminar.getSelectedItem().toString());
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha elminado correctamente", "Aviso", 1);
                    limpiarCampoT1();
                    tabla.clearRows();
                    cargarComboExpedientes();
                    obtenerFechaTiempoT2();
                    cargarCombosCajas();
                    t1_txtCajaAlta.requestFocus();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    // </editor-fold>
        
    // <editor-fold defaultstate="collapsed" desc="TabbedPane2">
    private void obtenerFechaTiempoT2(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        t2_dateFecha.setDate(cal.getTime());
    }
    
    private void declaracionTabla(){
        tabla = new Tabla(t2_Table1);
        String[][] titulos={
            {"no","expediente","caja","fechatxt","fecha"}
           ,{"No","Expediente","Caja","Fecha de Archivo","fecha"}
        };
        tabla.setTabla(titulos);
        tabla.setDefaultValues("","","","","");
        tabla.ocultarColumnas(4);
        tabla.setAnchoColumnas(30,100,100,100,20);
        tabla.setAlturaRow(30);
        tabla.alinear(0,2);
        tabla.alinear(1,2);
        tabla.alinear(2,2);
        tabla.alinear(3,2);
        tabla.cebraTabla();
    }
    
    private void renumerarTabla(){
        if(tabla.getRenglones()>0){
            for(int row=0; row<tabla.getRenglones(); row++){
                tabla.setElement("no",row,row+1);
            }
        }
    }
    
    private void quitarRenglonTabla(){
        if(tabla.getRenglones()>0){
            int row = tabla.getSelectedRow();
            if(row!=-1){
                String expediente = tabla.getElement("expediente", row);
                tabla.removeRow(row);
                 t2_cmbExpediente.addItem(expediente);
                renumerarTabla();
            }
        }
    }
    
    private void agregarExpediente(){
        if(t2_cmbCaja.getItemCount()==0){
        }else if(t2_cmbCaja.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No ha seleccionado una caja para archivar expedientes", "Alerta", 2);
            t2_cmbCaja.requestFocus();
        }else{
            Object []obj = new Object[5];
            obj[0] = tabla.getRenglones()+1;
            obj[1] = t2_cmbExpediente.getSelectedItem().toString();
            obj[2] = t2_cmbCaja.getSelectedItem().toString();
            obj[3] = new SimpleDateFormat("dd-MMM-yyyy").format(t2_dateFecha.getDate());
            obj[4] = new SimpleDateFormat("yyyy-MM-dd").format(t2_dateFecha.getDate());
            tabla.addRenglonArray(obj);
            t2_cmbExpediente.removeItem(t2_cmbExpediente.getSelectedItem());
        }
    }
    
    private void cargarComboExpedientes(){
        u.inicializaCombo(t2_cmbExpediente, "[Elija un Expediente]", bl.getExpedientesSinArchivar());
    }
    
    private void saveExpedientes(){
        String caja = t2_cmbCaja.getSelectedItem().toString();
        ArrayList array = new ArrayList();
        for(int i=0; i<tabla.getRenglones(); i++){
            ExpedienteEntity expe = new ExpedienteEntity();
            expe.setExpediente(tabla.getElement("expediente", i));
            expe.setCaja(tabla.getElement("caja", i));
            try{
                expe.setFechaArchivo(new SimpleDateFormat("yyyy-MM-dd").parse(tabla.getElement("fecha", i)));
            }catch(Exception e){}
            array.add(expe);
        }
        ErrorEntity error = bl.saveExpedientes(caja, array, sesion.getId_usuario(), sesion.getFechaActual());
        if(!error.getError()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
            limpiarCampoT1();
            tabla.clearRows();
            cargarComboExpedientes();
            obtenerFechaTiempoT2();
            cargarCombosCajas();
            t2_cmbCaja.requestFocus();
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
        }
        
    }
    
    private void saveCajaExpediente(){
        String caja = t2_cmbCaja.getSelectedItem().toString();
        ErrorEntity error = bl.saveCajaExpediente(caja, sesion.getId_usuario(), sesion.getFechaActual());
        if(!error.getError()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
            limpiarCampoT1();
            tabla.clearRows();
            cargarComboExpedientes();
            obtenerFechaTiempoT2();
            cargarCombosCajas();
            t2_cmbCaja.requestFocus();
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
        }
    }
    
    private void saveTabbedPane2(){
        if(t2_cmbCaja.getItemCount()==0){
        }else if(t2_cmbCaja.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No ha seleccionado una caja para archivar expedientes", "Alerta", 2);
            t2_cmbCaja.requestFocus();
        }else{
            if(tabla.getRenglones()==0){
                saveCajaExpediente();
            }else{
                saveExpedientes();
            }
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
        
        /////Para cambiar el focus cuando se encuentra en una tabla
        t2_Table1.getActionMap().put("Accion_tab",Accion_tab());
        t2_Table1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
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
        limpiarCampoT1();
        tabla.clearRows();
        cargarComboExpedientes();
        obtenerFechaTiempoT2();
        cargarCombosCajas();
        if(jTabbedPane1.getSelectedIndex()==0){
            t1_txtCajaAlta.requestFocus();
        }else{
            t2_cmbCaja.requestFocus();
        }
    }
    
    private void botonF2(){
        if(jTabbedPane1.getSelectedIndex()==0){
            saveCaja();
        }else{
            saveTabbedPane2();
        }
    }
    
    private void botonF4(){
        if(jTabbedPane1.getSelectedIndex()==0){
            updateCaja();
        }
    }
    
    private void botonF5(){
        agregarExpediente();
    }
    
    private void botonF6(){
        if(jTabbedPane1.getSelectedIndex()==0){
            deleteCaja();
        }
    }
    
    private void botonF7(){
        quitarRenglonTabla();
    }
    
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        t1_txtCajaAlta = new javax.swing.JTextField();
        t1_btnF2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        t1_txtCajaModificar = new javax.swing.JTextField();
        t1_btnF4 = new javax.swing.JButton();
        t1_cmbCajaModificar = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        t1_btnF6 = new javax.swing.JButton();
        t1_cmbCajaEliminar = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        t2_cmbCaja = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        t2_Table1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        t2_cmbExpediente = new javax.swing.JComboBox();
        jButton4 = new javax.swing.JButton();
        t2_dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        btnF12 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Archivar Expedientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Archivar_expedientes.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        jPanel1.setLayout(null);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nueva Caja", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Número o Nombre");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(20, 30, 120, 30);

        t1_txtCajaAlta.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtCajaAlta.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(t1_txtCajaAlta);
        t1_txtCajaAlta.setBounds(140, 30, 120, 30);

        t1_btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_btnF2.setText("F2 - Guardar Caja");
        t1_btnF2.setFocusable(false);
        t1_btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1_btnF2ActionPerformed(evt);
            }
        });
        jPanel2.add(t1_btnF2);
        t1_btnF2.setBounds(520, 30, 160, 30);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(10, 10, 692, 80);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Modificar Caja", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Caja");
        jPanel3.add(jLabel3);
        jLabel3.setBounds(16, 30, 40, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Nuevo Número o Nombre");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(210, 30, 170, 30);

        t1_txtCajaModificar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtCajaModificar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel3.add(t1_txtCajaModificar);
        t1_txtCajaModificar.setBounds(380, 30, 120, 30);

        t1_btnF4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_btnF4.setText("F4 - Modificar Caja");
        t1_btnF4.setFocusable(false);
        t1_btnF4.setPreferredSize(new java.awt.Dimension(129, 30));
        t1_btnF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1_btnF4ActionPerformed(evt);
            }
        });
        jPanel3.add(t1_btnF4);
        t1_btnF4.setBounds(520, 30, 160, 30);

        t1_cmbCajaModificar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel3.add(t1_cmbCajaModificar);
        t1_cmbCajaModificar.setBounds(50, 30, 150, 30);

        jPanel1.add(jPanel3);
        jPanel3.setBounds(10, 130, 690, 90);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Eliminar Caja", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel4.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Caja");
        jPanel4.add(jLabel4);
        jLabel4.setBounds(16, 29, 40, 30);

        t1_btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_btnF6.setText("F6 - Eliminar Caja");
        t1_btnF6.setFocusable(false);
        t1_btnF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1_btnF6ActionPerformed(evt);
            }
        });
        jPanel4.add(t1_btnF6);
        t1_btnF6.setBounds(520, 30, 160, 30);

        t1_cmbCajaEliminar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t1_cmbCajaEliminar);
        t1_cmbCajaEliminar.setBounds(60, 30, 150, 30);

        jPanel1.add(jPanel4);
        jPanel4.setBounds(10, 260, 692, 80);

        jTabbedPane1.addTab("Cajas", jPanel1);

        jPanel5.setLayout(null);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selección de Caja", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Caja");

        t2_cmbCaja.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t2_cmbCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2_cmbCajaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(t2_cmbCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t2_cmbCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6);
        jPanel6.setBounds(10, 11, 320, 0);

        t2_Table1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t2_Table1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t2_Table1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t2_Table1.setFocusable(false);
        t2_Table1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(t2_Table1);

        jPanel5.add(jScrollPane1);
        jScrollPane1.setBounds(350, 10, 350, 300);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Expedientes sin archivar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel7.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Fecha de Archivado");
        jPanel7.add(jLabel7);
        jLabel7.setBounds(10, 70, 140, 30);

        t2_cmbExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel7.add(t2_cmbExpediente);
        t2_cmbExpediente.setBounds(150, 30, 160, 30);

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton4.setText("F5 - Agregar Expediente a Caja");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton4);
        jButton4.setBounds(60, 130, 250, 30);

        t2_dateFecha.setFocusable(false);
        jPanel7.add(t2_dateFecha);
        t2_dateFecha.setBounds(150, 70, 160, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Expediente");
        jPanel7.add(jLabel8);
        jLabel8.setBounds(60, 30, 90, 30);

        jPanel5.add(jPanel7);
        jPanel7.setBounds(10, 100, 320, 180);

        jButton5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton5.setText("F7 - Quitar Expediente Seleccionado de Caja");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton5);
        jButton5.setBounds(350, 320, 350, 30);

        jButton6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton6.setText("F2 - Guardar Cambios en Caja");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton6);
        jButton6.setBounds(10, 320, 240, 30);

        jTabbedPane1.addTab("Archivar Expedientes", jPanel5);

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
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 394, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF12, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(4, 4, 4))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void t1_btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_t1_btnF2ActionPerformed

    private void t1_btnF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1_btnF4ActionPerformed
        botonF4();
    }//GEN-LAST:event_t1_btnF4ActionPerformed

    private void t1_btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_t1_btnF6ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        botonF5();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        botonF2();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        botonF7();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void t2_cmbCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2_cmbCajaActionPerformed
        if(t2_cmbCaja.getItemCount()>0){
            cargarComboExpedientes();
            tabla.clearRows();
            if(t2_cmbCaja.getSelectedIndex()>0){
                ArrayList array = bl.getExpedientes(t2_cmbCaja.getSelectedItem().toString());
                if(!array.isEmpty()){
                    for (Object array1 : array) {
                        ExpedienteEntity expe = (ExpedienteEntity) array1;
                        Object []obj = new Object[5];
                        obj[0] = tabla.getRenglones()+1;
                        obj[1] = expe.getExpediente();
                        obj[2] = expe.getCaja();
                        obj[3] =  new SimpleDateFormat("dd-MMM-yyyy").format(expe.getFechaArchivo());
                        obj[4] = new SimpleDateFormat("yyyy-MM-dd").format(expe.getFechaArchivo());
                        tabla.addRenglonArray(obj);
                    }
                }
            }
        }
    }//GEN-LAST:event_t2_cmbCajaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        botonEsc();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton t1_btnF2;
    private javax.swing.JButton t1_btnF4;
    private javax.swing.JButton t1_btnF6;
    private javax.swing.JComboBox t1_cmbCajaEliminar;
    private javax.swing.JComboBox t1_cmbCajaModificar;
    private javax.swing.JTextField t1_txtCajaAlta;
    private javax.swing.JTextField t1_txtCajaModificar;
    private javax.swing.JTable t2_Table1;
    private javax.swing.JComboBox t2_cmbCaja;
    private javax.swing.JComboBox t2_cmbExpediente;
    private com.toedter.calendar.JDateChooser t2_dateFecha;
    // End of variables declaration//GEN-END:variables
}
