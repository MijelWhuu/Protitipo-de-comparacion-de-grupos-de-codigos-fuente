package interfaz.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.Tabla;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.utilidades.CalendarioOficialBl;
import bl.catalogos.TipoTramitesBl;
import bl.operaciones.InicioTramiteBl;
import entitys.ExpedienteEntity;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import entitys.TramiteEntity;
import entitys.TipoTramiteEntity;
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
public class InicioTramite extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    InicioTramiteBl bl = new InicioTramiteBl();
    TipoTramitesBl blTram = new TipoTramitesBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    Utilidades u = new Utilidades();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    Tabla tabla1;
    private final String promovedores[] = {
        "---------ACTORES---------",
        "---------AUTORIDADES---------",
        "---------TERCEROS AUTORIDAD---------",
        "---------TERCEROS PERSONA---------",
        "---------AUTORIDADES AJENAS---------",
        "---------PERSONAS AJENAS---------"};

    /**
     * Creates new form Destinos
     */
    public InicioTramite(){
        initComponents();
    }
    
    public InicioTramite(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblTramite.setVisible(false);
        lblPromocion.setVisible(false);
        mapeoTeclas();
        formatearCampos();
        declaracionTabla();
        inicializaCombo();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void formatearCampos(){
        txtBuscarFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtBuscarAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtExpediente.setDocument(new JTextMascara(29));
        txtFolio.setDocument(new JTextMascara(7,true,"numerico"));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtNombre.setDocument(new JTextMascara(300));
        txtRazon.setDocument(new JTextMascaraTextArea());
        txtObservaciones.setDocument(new JTextMascaraTextArea());
        u.formatoJDateChooser(dateFecha);
        dateFecha.setDate(cal.getTime());
    }
    
    private void declaracionTabla(){
        tabla1 = new Tabla(jTable1);
        String[][] titulos={
            {"cbx","nombre","id","numero"}
           ,{"Seleccion","Nombre","id","numero"}
        };
        tabla1.setTabla(titulos);
        tabla1.setDefaultValues("","","","");
        tabla1.ocultarColumnas(2,3);
        tabla1.setAnchoColumnas(65,725,20,20);
        tabla1.setAlturaRow(30);
        tabla1.alinear(0,2);
        tabla1.dibujarCheckBox(0);
        tabla1.cebraTabla();
    }
    
    private void inicializaCombo(){
        u.inicializaCombo(cmbTramite, new TipoTramiteEntity(), blTram.getTipoAmparos());
    }
    
    private void validaFolio(){
        u.cleanComponent(lblTramite,lblPromocion,txtNombre,txtExpediente,txtFolio,txtAnio,dateFecha,txtRazon,txtObservaciones);
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
            if(bl.existeFolio(folio, anio)){
                cargaFolio(folio,anio);
                txtBuscarFolio.setText("");
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio no existe", "Alerta", 2);
                txtBuscarFolio.requestFocus();
            }
        }
    }
    
    private void cargaFolio(Integer folio, Integer anio){
        tabla1.clearRows();
        TramiteEntity tra = bl.getTramite(folio, anio);
        lblPromocion.setText(tra.getPromocion().getId_promocion());
        if(tra.getPromocion().getExpe()!=null)
            txtExpediente.setText(tra.getPromocion().getExpe().getExpediente());
        txtFolio.setText(tra.getPromocion().getFolio().toString());
        txtAnio.setText(tra.getPromocion().getAnio().toString());
        txtNombre.setText(tra.getPromocion().getTipoPromocion().getNombre());
        cmbTramite.setSelectedIndex(0);
        if(tra.getId_inicio_tramite()!=null){
            lblTramite.setText(tra.getId_inicio_tramite());
            cmbTramite.setSelectedItem(tra.getTramite());
            dateFecha.setDate(tra.getFecha());
            txtRazon.setText(tra.getRazon());
            txtObservaciones.setText(tra.getObservaciones());
        }
        if(cmbTramite.getSelectedIndex()!=0){
            TipoTramiteEntity tipo = (TipoTramiteEntity)cmbTramite.getSelectedItem();
            if(tipo.getSolicitaPartes()){
                seleccionarPromovedores(lblTramite.getText());
            }
        }cmbTramite.requestFocus();
    }
    
    private void seleccionarPromovedores(String id_tramite){
        if(bl.existeTramite(id_tramite)){
            ArrayList actor = bl.getPromovedoresActor(id_tramite);
            ArrayList auto = bl.getPromovedoresAutoridad(id_tramite);
            ArrayList terceroAuto = bl.getPromovedoresTerceroAutoridad(id_tramite);
            ArrayList terceroActor = bl.getPromovedoresTerceroActor(id_tramite);
            ArrayList autoridadAjena = bl.getPromovedoresAutoridadAjena(id_tramite);
            ArrayList personaAjena = bl.getPromovedoresPersonaAjena(id_tramite);
            if(!actor.isEmpty())seleccionarPartes("00",actor);
            if(!auto.isEmpty()) seleccionarPartes("01",auto);
            if(!terceroAuto.isEmpty()) seleccionarPartes("02",terceroAuto);
            if(!terceroActor.isEmpty()) seleccionarPartes("03",terceroActor);
            if(!autoridadAjena.isEmpty()) seleccionarPartes("04",autoridadAjena);
            if(!personaAjena.isEmpty()) seleccionarPartes("05",personaAjena);
        }
    }
    
    private void seleccionarPartes(String num, ArrayList array){
        for(Object array1 : array){
            for(int i=0; i<tabla1.getRenglones(); i++){
                if(tabla1.getElement("numero", i).equals(num) && tabla1.getElement("id", i).equals(array1.toString())){
                    tabla1.setElement("cbx", i, true);
                    i=tabla1.getRenglones()+5;
                }
            }
        }        
    }
    
    private void agregarPromovedoresTabla(String expediente){       
        for(int i=0; i<promovedores.length; i++){
            ArrayList array = bl.getPromovedores(expediente, i);
            if(!array.isEmpty()){
                Object []obj = new Object[4];//{"cbx","nombre","id","numero"}
                obj[0] = false;
                obj[1] = promovedores[i];
                obj[2] = "";
                obj[3] = ""+i;
                tabla1.addRenglon(obj);
                for(Object array1 : array){
                    String []datos = (String[]) array1;
                    Object []obj2 = new Object[4];
                    obj2[0] = false;
                    obj2[1] = datos[1];
                    obj2[2] = datos[0];
                    obj2[3] = datos[2];
                    tabla1.addRenglon(obj2);
                }
            }
        }
    }
    
    private ArrayList obtenerPromovedores(){
        ArrayList array = new ArrayList();
        for(int i=0; i<tabla1.getRenglones(); i++){
            if(tabla1.getElement("numero",i).length()==2 && Boolean.valueOf(tabla1.getElement("cbx",i))){
                String []datos = {tabla1.getElement("id",i),tabla1.getElement("numero",i)};
                array.add(datos);
            }
        }
        return array;
    }
    
    private Boolean validarTramite(){
        Boolean flag = false;
        if(txtFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado la promoción para realizar el trámite", "Alerta", 2);
            txtBuscarFolio.requestFocus();
        }else if(txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La promoción no tiene No. de Expediente.\nFavor de ir a la pantalla de Registro de\n"
                    + "Promociones e ingresar los datos faltantes de la Promoción", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(cmbTramite.getItemCount()>0 && cmbTramite.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el Tipo de Trámite", "Alerta", 2);
            cmbTramite.requestFocus();
        }else if(!blCale.isfechaValida(new SimpleDateFormat("yyyy-MM-dd").format(dateFecha.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else{
            flag = true;
        }
        return flag;
    }
    
    private TramiteEntity obtenerDatosTramite(){
        TramiteEntity tra = new TramiteEntity();
        tra.setId_inicio_tramite(lblTramite.getText());
        tra.setPromocion(new PromocionEntity(lblPromocion.getText()));
        tra.setTramite((TipoTramiteEntity)cmbTramite.getSelectedItem());
        tra.setFecha(dateFecha.getDate());
        tra.setRazon(txtRazon.getText());
        tra.setObservaciones(txtObservaciones.getText());
        tra.setExp(new ExpedienteEntity(txtExpediente.getText()));
        return tra;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        tabla1.clearRows();
        u.cleanComponent(lblTramite,lblPromocion,txtBuscarFolio,txtBuscarAnio,txtNombre,txtExpediente,txtFolio,txtAnio,dateFecha,txtRazon,txtObservaciones);
        inicializaCombo();
        txtBuscarFolio.requestFocus();
    }
    
    private void botonF2(){
        if(validarTramite()){
            ArrayList array = obtenerPromovedores();
            if(((TipoTramiteEntity) cmbTramite.getSelectedItem()).getSolicitaPartes() && array.isEmpty()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado la parte que promueve el trámite", "Alerta", 2);
            }else{
                if(lblTramite.getText().length()==0){
                    ErrorEntity error = bl.saveTramite(obtenerDatosTramite(), sesion, array);
                    if(!error.getError()){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                        botonEsc();
                    }else{
                        javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                    }
                }else{
                    ErrorEntity error = bl.updateTramite(obtenerDatosTramite(), sesion, array);
                    if(!error.getError()){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                        botonEsc();
                    }else{
                        javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                    }
                }
            }
        }
    }
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){
        if(lblTramite.getText().length()!=0){
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar el trámite seleccionado?", "Alerta", 0)==0){
                ErrorEntity error = bl.deleteTramite(lblTramite.getText());
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    if(error.getNumError().equals(1451)){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar el Trámite seleccionado ya que está siendo utilizado por otra información", "Aviso", 1);
                        txtBuscarFolio.requestFocus();
                    }else{
                        javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                    }
                }
            }
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un trámite para eliminar", "Alerta", 2);
        }
    }
    
    private void botonF7(){}
    
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
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0),"accion_F5"); mapaAccion.put("accion_F5",Accion_F5());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0),"accion_F6"); mapaAccion.put("accion_F6",Accion_F6());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0),"accion_F7"); mapaAccion.put("accion_F7",Accion_F7());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0),"accion_F12"); mapaAccion.put("accion_F12",Accion_F12());
        
        /////Para cambiar el focus cuando se encuentra en una tabla
        jTable1.getActionMap().put("Accion_tab",Accion_tab());
        jTable1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
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
        btnEsc = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarFolio = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarAnio = new javax.swing.JTextField();
        lblTramite = new javax.swing.JLabel();
        lblPromocion = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        cmbTramite = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtRazon = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Inicio de trámites");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Inicio_tramites.jpg"))); // NOI18N

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

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("<html><center>F6 - Eliminar<br>Trámite</center></html>");
        btnF6.setFocusable(false);
        btnF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF6ActionPerformed(evt);
            }
        });

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("<html><center>F2 - Guardar<br>y/o Actualizar<br>Trámite</center></html>");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda de la Promoción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Año");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(520, 30, 40, 30);

        txtBuscarFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(390, 30, 120, 30);

        btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar);
        btnBuscar.setBounds(670, 30, 130, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Folio de la Promoción");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(220, 30, 160, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(570, 30, 70, 30);

        lblTramite.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(lblTramite);
        lblTramite.setBounds(20, 30, 30, 30);

        lblPromocion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.add(lblPromocion);
        lblPromocion.setBounds(60, 30, 30, 30);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Trámite", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Fecha del Trámite");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(10, 140, 130, 30);

        dateFecha.setDateFormatString("dd/MMM/yyyy");
        dateFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(dateFecha);
        dateFecha.setBounds(150, 140, 190, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Expediente");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(60, 60, 80, 30);

        cmbTramite.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTramite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTramiteActionPerformed(evt);
            }
        });
        jPanel2.add(cmbTramite);
        cmbTramite.setBounds(150, 100, 660, 30);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.setFocusable(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable1);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(10, 310, 810, 230);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Observaciones");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(10, 230, 130, 40);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Partes que lo promueven");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(10, 280, 180, 30);

        txtNombre.setEditable(false);
        txtNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNombre.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtNombre.setFocusable(false);
        jPanel2.add(txtNombre);
        txtNombre.setBounds(150, 20, 660, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tipos de Trámite");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(0, 100, 140, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("<html><body><DIV ALIGN=right>Razón por la que se<br>promueve</DIV></body></html>");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(0, 180, 140, 40);

        txtRazon.setColumns(20);
        txtRazon.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtRazon.setLineWrap(true);
        txtRazon.setRows(1);
        txtRazon.setWrapStyleWord(true);
        txtRazon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtRazonKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtRazon);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(150, 180, 660, 40);

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setRows(1);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(txtObservaciones);

        jPanel2.add(jScrollPane4);
        jScrollPane4.setBounds(150, 230, 660, 40);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("Año");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(590, 60, 40, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Promoción");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(60, 20, 80, 30);

        txtExpediente.setEditable(false);
        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.setFocusable(false);
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(150, 60, 140, 30);

        txtAnio.setEditable(false);
        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.setFocusable(false);
        jPanel2.add(txtAnio);
        txtAnio.setBounds(630, 60, 80, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Folio de la Promoción");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(310, 60, 140, 30);

        txtFolio.setEditable(false);
        txtFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFolio.setFocusable(false);
        jPanel2.add(txtFolio);
        txtFolio.setBounds(460, 60, 120, 30);

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 0, 0));
        jLabel96.setText("*");
        jPanel2.add(jLabel96);
        jLabel96.setBounds(20, 140, 20, 30);

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 0, 0));
        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel97.setText("*");
        jPanel2.add(jLabel97);
        jLabel97.setBounds(20, 100, 20, 30);

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 0, 0));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("*");
        jPanel2.add(jLabel95);
        jLabel95.setBounds(50, 60, 30, 30);

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 0, 0));
        jLabel98.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel98.setText("*");
        jPanel2.add(jLabel98);
        jLabel98.setBounds(60, 20, 20, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 830, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 986, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 420, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        validaFolio();
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void cmbTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTramiteActionPerformed
        if(cmbTramite.getItemCount()>0){
            tabla1.clearRows();
            if(cmbTramite.getSelectedIndex()!=0){
                TipoTramiteEntity tipo = (TipoTramiteEntity)cmbTramite.getSelectedItem();
                if(tipo.getSolicitaPartes() && txtExpediente.getText().length()>0){
                    agregarPromovedoresTabla(txtExpediente.getText());
                }
            }
        }
    }//GEN-LAST:event_cmbTramiteActionPerformed

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            validaFolio();
        }
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void txtRazonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRazonKeyPressed
        u.focus(evt, txtRazon);
    }//GEN-LAST:event_txtRazonKeyPressed

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        u.focus(evt, txtObservaciones);
    }//GEN-LAST:event_txtObservacionesKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF6;
    private javax.swing.JComboBox cmbTramite;
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblPromocion;
    private javax.swing.JLabel lblTramite;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtBuscarAnio;
    private javax.swing.JTextField txtBuscarFolio;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextArea txtRazon;
    // End of variables declaration//GEN-END:variables

    
    
}
