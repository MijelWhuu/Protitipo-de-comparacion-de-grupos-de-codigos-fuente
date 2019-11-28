package interfaz.notificaciones;

import baseSistema.TablaEspecial;
import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.catalogos.DestinosBl;
import bl.notificaciones.BorrarNotificacionesBl;
import bl.utilidades.CalendarioOficialBl;
import entitys.CargaNotificacionEntity;
import entitys.DescargaNotificacionEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class BorrarNotificaciones extends javax.swing.JInternalFrame {

    BorrarNotificacionesBl bl = new BorrarNotificacionesBl();
    SesionEntity sesion;
    Utilidades u = new Utilidades();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    DestinosBl blDestino = new DestinosBl();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    TablaEspecial tabla1, tabla0;
    
    /**
     * Creates new form CargaNotificaciones
     * @param sesion
     */
    public BorrarNotificaciones(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblAuto.setVisible(false);
        formatearCampos();
        mapeoTeclas();
        incializaTabla0();
        incializaTabla1();
        txtBuscarAnio.setText(""+cal.get(Calendar.YEAR));
    }
    
    private void formatearCampos(){
        txtBuscarFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtBuscarAnio.setDocument(new JTextMascara(4,true,"numerico"));
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de Buscar y Cargar">   
    private void cargaInfoAuto(RelacionEntity rel){
        lblAuto.setText(rel.getId_relacion());
        tabla0.clearTabla();
        tabla1.clearTabla();
        ArrayList arrayDatos = new ArrayList();
        String expediente = "";
        if(rel.getId_registro_autos()!=null){
            arrayDatos = bl.getAuto(rel.getId_registro_autos());
            expediente = bl.getAutoExpediente(rel.getId_registro_autos());
        }else if(rel.getId_reg_resolucion()!=null){
            arrayDatos = bl.getResolucion(rel.getId_reg_resolucion());
            expediente = bl.getResolucionExpediente(rel.getId_reg_resolucion());
        }else if(rel.getId_reg_sentencia()!=null){
            arrayDatos = bl.getSentencia(rel.getId_reg_sentencia());
            expediente = bl.getSentenciaExpediente(rel.getId_reg_sentencia());
        }
        
        for(Object array1 : arrayDatos){            
            tabla0.addRow((Object[])array1);
        }
        
        
        llenaTabla(bl.getActores(rel.getId_relacion()), "ACTORES",1);
        llenaTabla(bl.getAutoridades(rel.getId_relacion()), "AUTORIDADES",2);
        llenaTabla(bl.getTerceroAutoridades(rel.getId_relacion()), "TERCEROS AUTORIDADES",3);
        llenaTabla(bl.getTerceroActores(rel.getId_relacion()), "TERCEROS ACTORES",4);
        llenaTabla(bl.getAutoridadesAjenas(rel.getId_relacion()), "AUTORIDADES AJENAS",5);
        llenaTabla(bl.getPersonasAjenas(rel.getId_relacion()), "PERSONAS AJENAS",6);
        
        if(tabla1.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se han realizado Notificaciones con el Folio señalado", "Alerta", 2);
        }
    }
    
    private void validaBusquedaFolio(){
        u.cleanComponent(lblAuto);
        if(txtBuscarFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio a buscar", "Alerta", 2);
            txtBuscarFolio.requestFocus();
        }else{
            Integer folio = new Integer(txtBuscarFolio.getText());
            Integer anio = cal.get(Calendar.YEAR);
            if(txtBuscarAnio.getText().length()!=0){
                Integer num = u.verificaInteger(txtBuscarAnio.getText());
                if(num!=null)
                    anio = num;
            }
            RelacionEntity rel = bl.getRelacion(folio, anio);
            if(rel!=null){
                cargaInfoAuto(rel);
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se encontro Notificaciones con el Folio señalado", "Alerta", 2);
                tabla0.clearTabla();
                tabla1.clearTabla();
                txtBuscarFolio.requestFocus();
            }
        }
    }    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Inicializar tablas">
    private void incializaTabla0(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        Object []titulos = new Object[] {"Dato","Valor"};
        dm.setColumnIdentifiers(titulos);
        jTable0.setModel(dm);
        tabla0 = new TablaEspecial(jTable0);
        tabla0.setAnchoColumnas(200,600);
    }
    
    private void incializaTabla1(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col==3 || col==6;
            }
        };
        Object []titulos = new Object[] {
            "Nombre","id_parte","Tipo de Parte",//0-2
            "Carga","Información de la Carga","id_carga",//3-5
            "Descarga","Información de la Descarga","id_descarga"};//6-8
        dm.setColumnIdentifiers(titulos);
        jTable1.setModel(dm);
        tabla1 = new TablaEspecial(jTable1);
        tabla1.cellLineWrap(0);
        tabla1.cellLineWrap(4);
        tabla1.cellLineWrap(7);
        tabla1.ocultarColumnas(1,5,8);
        tabla1.setAnchoColumnas(250,20,145,45,190,20,60,270,20);
    }
    
    private void llenaTabla(ArrayList array, String tipo_parte, int tipo){
        for(Object info:array){
            CargaNotificacionEntity carga = (CargaNotificacionEntity)info;
            Object []datos = new Object[9];
            datos[0] = carga.getNombre();
            datos[1] = carga.getId_parte();
            datos[2] = tipo_parte;
            datos[3] = false;
            datos[4] = infoCarga(carga,tipo);
            datos[5] = carga.getId_carga();
            datos[6] = false;
            if(carga.getDescarga()==null){
                datos[7] = "PENDIENTE";
                datos[8] = "";
            }else{
                datos[7] = infoDesCarga(carga.getDescarga(),tipo);
                datos[8] = carga.getDescarga().getId_descarga();
            }
            tabla1.addRow(datos);
        }
        tabla1.crearCheckbox(tabla1.getRenglones(),3);
        tabla1.crearCheckbox(tabla1.getRenglones(),6);
    }
    
    private String infoCarga(CargaNotificacionEntity carga, int tipo){
        if(tipo==1 || tipo==4 || tipo==6){
            return "Tipo de Notificación:   "+carga.getTipo_notificacion()+"\n"
                   + "Fecha de Recepción:   "+u.dateToString(carga.getFecha_recibida());
        }else{
            return "Tipo de Notificación:   "+carga.getTipo_notificacion()+"\n"
                   + "Fecha de Recepción:   "+u.dateToString(carga.getFecha_recibida())+"\n"
                   + "Número de Oficio:   "+carga.getOficio();
        }
    }
    
    private String infoDesCarga(DescargaNotificacionEntity descarga, int tipo){
        String cad;
        if(tipo==1 || tipo==4 || tipo==6){
            cad = "Tipo de Notificación:   "+descarga.getTipo_notificacion()+"\n"
                + "Fecha de Notificación:   "+u.dateToString(descarga.getFecha_notificacion());
        }else{
            cad = "Tipo de Notificación:   "+descarga.getTipo_notificacion()+"\n"
                + "Fecha de Notificación:   "+u.dateToString(descarga.getFecha_notificacion())+"\n"
                + "Número de Oficio:   "+descarga.getOficio();
        }
        
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
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Varias Funciones">
    private Boolean existeRowWithTrue(){
        if(tabla1.getRenglones()>0){
            for(int row=0; row<tabla1.getRenglones(); row++){
                if(Boolean.valueOf(tabla1.getElement(3,row).toString())){
                    return true;
                }else if(Boolean.valueOf(tabla1.getElement(6,row).toString())&&!tabla1.getElement(7, row).toString().equals("PENDIENTE")){
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
    
    private ArrayList getDatos(){
        ArrayList array = new ArrayList();
        for(int row=0; row<tabla1.getRenglones(); row++){
            DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
            if(Boolean.valueOf(tabla1.getElement(3,row).toString())){
                descarga.setId_carga(tabla1.getElement(5,row).toString());
            }else{
                descarga.setId_carga("");
            }
            if(Boolean.valueOf(tabla1.getElement(6,row).toString())&&!tabla1.getElement(7, row).toString().equals("PENDIENTE")){
                descarga.setId_descarga(tabla1.getElement(8,row).toString());
            }else{
                descarga.setId_descarga("");
            }
            array.add(descarga);
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
        u.cleanComponent(lblAuto);
        tabla0.clearTabla();
        tabla1.clearTabla();
        txtBuscarFolio.setText("");
        txtBuscarAnio.setText(""+cal.get(Calendar.YEAR));
        txtBuscarFolio.requestFocus();
    }
    
    private void botonF2(){}
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){
        if(lblAuto.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha realizado la búsqueda del Auto", "Aviso", 1);
            txtBuscarFolio.requestFocus();
        }else if(validarTablas()){
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
        panelInformacion = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable0 = new javax.swing.JTable();
        panelBuscar = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtBuscarFolio = new javax.swing.JTextField();
        btnBusarFolio = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtBuscarAnio = new javax.swing.JTextField();
        lblAuto = new javax.swing.JLabel();
        btnF6 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF12 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setTitle("Buscar y Eliminar Notificaciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/BorrarPromociones.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        panelInformacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelInformacion.setLayout(null);

        jTable0.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable0.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable0.setRowHeight(30);
        jTable0.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jTable0);

        panelInformacion.add(jScrollPane2);
        jScrollPane2.setBounds(10, 20, 820, 100);

        panelBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelBuscar.setLayout(null);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Folio del Auto");
        panelBuscar.add(jLabel20);
        jLabel20.setBounds(140, 20, 100, 30);

        txtBuscarFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(240, 20, 120, 30);

        btnBusarFolio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBusarFolio.setText("Buscar Folio");
        btnBusarFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusarFolioActionPerformed(evt);
            }
        });
        panelBuscar.add(btnBusarFolio);
        btnBusarFolio.setBounds(590, 20, 140, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Año del Auto");
        panelBuscar.add(jLabel27);
        jLabel27.setBounds(380, 20, 100, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarAnioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(480, 20, 80, 30);

        lblAuto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelBuscar.add(lblAuto);
        lblAuto.setBounds(790, 20, 30, 30);

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("<html><center>F6 - Eliminar<br>Notificaciones<br>Seleccionadas</center></html>");
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
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 992, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
                            .addComponent(panelInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnF12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEsc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnF6))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addComponent(btnF6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        setBounds(0, 0, 1008, 720);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            validaBusquedaFolio();
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void txtBuscarAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAnioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            validaBusquedaFolio();
    }//GEN-LAST:event_txtBuscarAnioKeyPressed

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
                if(col==3){
                    if(Boolean.valueOf(tabla1.getElement(3, row).toString())){
                        if(!tabla1.getElement(7, row).toString().equals("PENDIENTE") && tabla1.getElement(8, row).toString().length()!=0){
                            tabla1.setElement(6, row, true);
                        }
                    }else{
                        if(!tabla1.getElement(7, row).toString().equals("PENDIENTE") && tabla1.getElement(8, row).toString().length()!=0){
                            tabla1.setElement(6, row, false);
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnBusarFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusarFolioActionPerformed
        validaBusquedaFolio();
    }//GEN-LAST:event_btnBusarFolioActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBusarFolio;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable0;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JPanel panelBuscar;
    private javax.swing.JPanel panelInformacion;
    private javax.swing.JTextField txtBuscarAnio;
    private javax.swing.JTextField txtBuscarFolio;
    // End of variables declaration//GEN-END:variables
}
