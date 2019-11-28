package interfaz.notificaciones;

import baseSistema.TablaEspecial;
import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.notificaciones.BorrarNotificacionesExtraBl;
import entitys.CargaNotificacionExtraEntity;
import entitys.DescargaNotificacionExtraEntity;
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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mavg
 */
public class BorrarNotificacionesExtra extends javax.swing.JInternalFrame {

    BorrarNotificacionesExtraBl bl = new BorrarNotificacionesExtraBl();
    SesionEntity sesion;
    Utilidades u = new Utilidades();
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    TablaEspecial tabla1;
    
    /**
     * Creates new form CargaNotificaciones
     * @param sesion
     */
    public BorrarNotificacionesExtra(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        incializaTabla1();
        txtOficio.setDocument(new JTextMascara(5,true,"numerico"));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void buscarOficio(){
        if(txtOficio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Oficio a buscar", "Alerta", 2);
            txtOficio.requestFocus();
        }else{
            Integer anio = cal.get(Calendar.YEAR);
            if(txtAnio.getText().length()!=0){
                Integer num = u.verificaInteger(txtAnio.getText());
                if(num!=null)
                    anio = num;
            }
            CargaNotificacionExtraEntity carga = bl.getCarga(txtOficio.getText(), anio);
            DescargaNotificacionExtraEntity descarga = bl.getDescarga(txtOficio.getText(), anio);
            if(carga!=null){
                cargaInfo(carga,descarga);
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se encontro Notificaciones con el Oficio señalado", "Alerta", 2);
                tabla1.clearTabla();
                txtOficio.requestFocus();
            }
        }
    }
    
    private void cargaInfo(CargaNotificacionExtraEntity carga, DescargaNotificacionExtraEntity descarga){
        tabla1.clearTabla();
        Object []datos = new Object[8];
        datos[0] = carga.getAuto().getNombre()+"\nOficio: "+carga.getOficio()+"\nAño: "+carga.getAnio();
        datos[1] = carga.getAuto().getId_autoridad();
        datos[2] = false;
        datos[3] = infoCarga(carga);
        datos[4] = carga.getId_carga_extra();
        datos[5] = false;
        if(descarga==null){
            datos[6] = "PENDIENTE";
            datos[7] = "";
        }else{
            datos[6] = infoDesCarga(descarga);
            datos[7] = descarga.getId_descarga_extra();
        }
        tabla1.addRow(datos);
        tabla1.crearCheckbox(tabla1.getRenglones(),2);
        tabla1.crearCheckbox(tabla1.getRenglones(),5);
        if(tabla1.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se han realizado Notificaciones con el Oficio señalado", "Alerta", 2);
        }
    }
  
    private void incializaTabla1(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col==2 || col==5;
            }
        };
        Object []titulos = new Object[] {
            "Información","id_autoridad",//0-1
            "Carga","Información de la Carga","id_carga",//2-4
            "Descarga","Información de la Descarga","id_descarga"};//5-7
        
        dm.setColumnIdentifiers(titulos);
        jTable1.setModel(dm);
        tabla1 = new TablaEspecial(jTable1);
        tabla1.cellLineWrap(0);
        tabla1.cellLineWrap(3);
        tabla1.cellLineWrap(6);
        tabla1.ocultarColumnas(1,4,7);
        tabla1.setAnchoColumnas(300,20,50,180,20,80,250,20);
    }
    
    private String infoCarga(CargaNotificacionExtraEntity carga){
        return "Expediente: "+carga.getExpe().getExpediente()+"\n"
                + "Tipo de Notificación:   "+carga.getTipo_notificacion()+"\n"
                + "Fecha de Recepción:   "+u.dateToString(carga.getFecha_recibida());
    }
    
    private String infoDesCarga(DescargaNotificacionExtraEntity descarga){
        String cad = "Expediente: "+descarga.getExpe().getExpediente()+"\n"
                + "Tipo de Notificación:   "+descarga.getTipo_notificacion()+"\n"
                + "Fecha de Notificación:   "+u.dateToString(descarga.getFecha_notificacion());
        if(descarga.getFecha_deposito()!=null){
            cad = cad.concat("\nFecha de Depósito:   "+u.dateToString(descarga.getFecha_deposito()));
        }
        if(descarga.getSpm()!=null && descarga.getSpm().length()!=0){
            cad = cad.concat("\nNo. de Identificación (SPM):   "+descarga.getSpm());
        }
        if(descarga.getMun()!=null){
            cad = cad.concat("\nMunicipio:   "+descarga.getMun().getNombre());
        }
        if(descarga.getFecha_recepcion()!=null){
            cad = cad.concat("\nFecha de Recepción:   "+u.dateToString(descarga.getFecha_recepcion()));
        }
        cad = cad.concat("\nDestino:   "+descarga.getDesti().getNombre());
        cad = cad.concat("\nFecha de entrega al Destino:   "+u.dateToString(descarga.getFecha_destino()));
        return cad;
    }
    
    private Boolean existeRowWithTrue(){
        if(tabla1.getRenglones()>0){
            for(int row=0; row<tabla1.getRenglones(); row++){
                if(Boolean.valueOf(tabla1.getElement(2,row).toString())){
                    return true;
                }else if(Boolean.valueOf(tabla1.getElement(5,row).toString())&&!tabla1.getElement(6, row).toString().equals("PENDIENTE")){
                    return true;
                }
            }
        }
        return false;
    }
    
    private Boolean validarTablas(){
        if(tabla1.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay notificaciones que eliminar", "Aviso", 1);
            return false;
        }else if(!existeRowWithTrue()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay notificaciones marcadass para eliminar", "Aviso", 1);
            return false;
        }else{
            return true;
        }
    }
    
    private String[] getDatos(){
        String ids[] = {"",""};   
        if(Boolean.valueOf(tabla1.getElement(2,0).toString())){
            ids[0]=tabla1.getElement(4,0).toString();
        }
        if(Boolean.valueOf(tabla1.getElement(5,0).toString())&&!tabla1.getElement(6,0).toString().equals("PENDIENTE")){
            ids[1]=tabla1.getElement(7,0).toString();
        }
        return ids;
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
        jTable1.getActionMap().put("Accion_tab",Accion_tab());
        jTable1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        /////
    }
    public AbstractAction Accion_Esc(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F5(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF5();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F7(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF7();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF12();}};}
    
    //Para cambiar el focus cuando se encuentra en una tabla
    public AbstractAction Accion_tab(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();  
    }};}
    /////
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        tabla1.clearTabla();
        txtOficio.setText("");
        txtAnio.setText("");
        txtOficio.requestFocus();
    }
    
    private void botonF2(){}
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){
        if(validarTablas()){
            ErrorEntity error = bl.deleteNotificacion(getDatos());
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                botonEsc();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
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
        panelBuscar = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtOficio = new javax.swing.JTextField();
        btnBuscarFolio = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        btnF6 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF12 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setTitle("Buscar y Eliminar Notificaciones Extra");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/notificacionesExtra.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        panelBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelBuscar.setLayout(null);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Oficio");
        panelBuscar.add(jLabel20);
        jLabel20.setBounds(70, 60, 50, 30);

        txtOficio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtOficio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOficio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtOficioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtOficio);
        txtOficio.setBounds(120, 60, 120, 30);

        btnBuscarFolio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBuscarFolio.setText("Buscar Oficio");
        btnBuscarFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFolioActionPerformed(evt);
            }
        });
        panelBuscar.add(btnBuscarFolio);
        btnBuscarFolio.setBounds(400, 60, 140, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Año");
        panelBuscar.add(jLabel27);
        jLabel27.setBounds(260, 60, 40, 30);

        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAnioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtAnio);
        txtAnio.setBounds(300, 60, 80, 30);

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("<html><center>F6 - Eliminar Notificaciones<br>Seleccionadas</center></html>");
        btnF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF6ActionPerformed(evt);
            }
        });

        btnEsc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnEsc.setText("Esc - Cancelar");
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
        jTable1.setRowHeight(30);
        jTable1.setRowSelectionAllowed(false);
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(panelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnF12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnF6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 900, 492);
    }// </editor-fold>//GEN-END:initComponents

    private void txtOficioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOficioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            buscarOficio();
    }//GEN-LAST:event_txtOficioKeyPressed

    private void txtAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            buscarOficio();
    }//GEN-LAST:event_txtAnioKeyPressed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if(tabla1.getRenglones()>0){
            int row = jTable1.getSelectedRow();
            int col = jTable1.getSelectedColumn();
            if(row!=-1 && col!=-1){
                if(col==2){
                    if(Boolean.valueOf(tabla1.getElement(2, row).toString())){
                        if(!tabla1.getElement(6, 0).toString().equals("PENDIENTE") && tabla1.getElement(7, 0).toString().length()!=0){
                            tabla1.setElement(5, 0, true);
                        }
                    }else{
                        if(!tabla1.getElement(6, 0).toString().equals("PENDIENTE") && tabla1.getElement(7, 0).toString().length()!=0){
                            tabla1.setElement(5, 0, false);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnBuscarFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFolioActionPerformed
        buscarOficio();
    }//GEN-LAST:event_btnBuscarFolioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarFolio;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelBuscar;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtOficio;
    // End of variables declaration//GEN-END:variables
}
