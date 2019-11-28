package interfaz.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.AutoridadesNotificacionesBl;
import bl.catalogos.DestinosBl;
import bl.catalogos.MunicipiosBl;
import bl.utilidades.CalendarioOficialBl;
import bl.notificaciones.DescargaNotificacionesExtraBl;
import entitys.SesionEntity;
import entitys.ExpedienteEntity;
import entitys.CargaNotificacionExtraEntity;
import entitys.DescargaNotificacionExtraEntity;
import entitys.DestinoEntity;
import entitys.MunicipioEntity;
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
public class DescargaNotificacionesExtra extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    DescargaNotificacionesExtraBl bl = new DescargaNotificacionesExtraBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    AutoridadesNotificacionesBl blAuto = new AutoridadesNotificacionesBl();
    MunicipiosBl blMunicipio = new MunicipiosBl();
    DestinosBl blDestino = new DestinosBl();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public DescargaNotificacionesExtra(){
        initComponents();
    }
    
    public DescargaNotificacionesExtra(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        inicializarPantalla();
        mapeoTeclas();
        u.inicializaCombo(cmbMunicipio, new MunicipioEntity(), blMunicipio.getMunicipios());
        u.inicializaCombo(cmbDestino, new DestinoEntity(), blDestino.getDestinos());
        u.inicializaCombo(cmbOficios, new CargaNotificacionExtraEntity(), bl.getOficios());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void inicializarPantalla(){
        txtOficio.setDocument(new JTextMascara(29));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        u.formatoJDateChooser(dateNotificacion);
        txtSpm.setDocument(new JTextMascara(29,true,"todo"));
        u.formatoJDateChooser(dateDestino);
        txtObservaciones.setDocument(new JTextMascaraTextArea());
    }
    
    private void eventoRadioButtons(){
        u.cleanComponent(cmbMunicipio,txtSpm);
        dateDeposito.setDate(null);
        dateRecepcion.setDate(null);
        if(rbtnCorreo.isSelected()){
            cmbMunicipio.setEnabled(true);
            dateDeposito.setEnabled(true);
            txtSpm.setEnabled(true);
            dateRecepcion.setEnabled(true);
        }else{
            cmbMunicipio.setEnabled(false);
            dateDeposito.setEnabled(false);
            txtSpm.setEnabled(false);
            dateRecepcion.setEnabled(false);
        }
    }
    
    private void cargaOficio(){
        u.cleanComponent(txtOficio,txtAnio,txtFechaCarga,txtAutoridad,txtExpediente,dateNotificacion,cmbMunicipio,txtSpm,txtObservaciones,cmbDestino,dateDestino);
        dateDeposito.setDate(null);
        dateRecepcion.setDate(null);
        CargaNotificacionExtraEntity noti = (CargaNotificacionExtraEntity)cmbOficios.getSelectedItem();
        txtOficio.setText(noti.getOficio());
        txtAnio.setText(noti.getAnio().toString());
        txtFechaCarga.setText(u.dateToString(noti.getFecha_recibida()));
        txtAutoridad.setText(noti.getAuto().getNombre());
        txtExpediente.setText(noti.getExpe().getExpediente());
        switch (noti.getTipo_notificacion()) {
            case "OFICIO": rbtnOficio.setSelected(true); break;
            case "CORREO": rbtnCorreo.setSelected(true); break;
            default: rbtnLista.setSelected(true); break;
        }
        eventoRadioButtons();
    }
    
    private DescargaNotificacionExtraEntity getDatos(){
        DescargaNotificacionExtraEntity noti = new DescargaNotificacionExtraEntity();
        CargaNotificacionExtraEntity carga = (CargaNotificacionExtraEntity)cmbOficios.getSelectedItem();
        noti.setId_carga_extra(carga.getId_carga_extra());
        noti.setAuto(carga.getAuto());
        noti.setExpe(new ExpedienteEntity(txtExpediente.getText()));
        noti.setOficio(txtOficio.getText());
        noti.setAnio(new Integer(txtAnio.getText()));
        noti.setMun(null);
        noti.setFecha_deposito(null);
        noti.setSpm("");
        noti.setFecha_recepcion(null);
        if(rbtnOficio.isSelected()){
            noti.setTipo_notificacion("OFICIO");
        }else if(rbtnCorreo.isSelected()){
            noti.setTipo_notificacion("CORREO");
            if(cmbMunicipio.getItemCount()>0 && cmbMunicipio.getSelectedIndex()>0)
                noti.setMun((MunicipioEntity)cmbMunicipio.getSelectedItem());
            noti.setFecha_deposito(dateDeposito.getDate());
            noti.setSpm(txtSpm.getText());
            noti.setFecha_recepcion(dateRecepcion.getDate());
        }else{
            noti.setTipo_notificacion("LISTA");
        }
        noti.setFecha_notificacion(dateNotificacion.getDate());
        noti.setDesti((DestinoEntity)cmbDestino.getSelectedItem());
        noti.setFecha_destino(dateDestino.getDate());
        noti.setObservaciones(txtObservaciones.getText());
        return noti;
    }
    
    private void guardar(){
        ErrorEntity error = bl.saveNotificacion(getDatos(), sesion);
        if(!error.getError()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
            botonEsc();
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
        }
    }
    
    private Boolean validar(){
        if(cmbOficios.getItemCount()>0 && cmbOficios.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Oficio", "Alerta", 2);
            cmbOficios.requestFocus();
        }else if(txtOficio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Número de Oficio", "Alerta", 2);
            txtOficio.requestFocus();
        }else if(txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Número de Expediente", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(!bl.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Número de Expediente de expediente no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(cmbDestino.getItemCount()>0 && cmbDestino.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el Destino", "Alerta", 2);
            cmbDestino.requestFocus();
        }else{
            return true;
        }
        return false;
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
        if(cmbOficios.getItemCount()>0)
            cmbOficios.setSelectedIndex(0);
        cmbOficios.requestFocus();
        u.inicializaCombo(cmbOficios, new CargaNotificacionExtraEntity(), bl.getOficios());
    }
    
    private void botonF2(){
        if(validar()){
            guardar();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        btnF2 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtSpm = new javax.swing.JTextField();
        dateRecepcion = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jLabel15 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        dateDestino = new com.toedter.calendar.JDateChooser();
        dateDeposito = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        txtOficio = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        dateNotificacion = new com.toedter.calendar.JDateChooser();
        jLabel21 = new javax.swing.JLabel();
        cmbMunicipio = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAutoridad = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtFechaCarga = new javax.swing.JTextField();
        rbtnOficio = new javax.swing.JRadioButton();
        rbtnCorreo = new javax.swing.JRadioButton();
        rbtnLista = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        cmbOficios = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Descarga Extra de Notificaciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/descarga_extra_notificaciones.jpg"))); // NOI18N

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F2 - Guardar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnEsc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnEsc.setText("Esc - Cancelar");
        btnEsc.setFocusable(false);
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnSalir.setText("F12 - Salir");
        btnSalir.setFocusable(false);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de Notificación");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(20, 160, 130, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Observaciones");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(40, 360, 110, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Oficio");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(40, 20, 110, 30);

        txtSpm.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtSpm.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtSpm);
        txtSpm.setBounds(510, 280, 260, 30);

        dateRecepcion.setDateFormatString("dd/MMM/yyyy");
        dateRecepcion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(dateRecepcion);
        dateRecepcion.setBounds(160, 320, 150, 30);

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservaciones.setRows(2);
        txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtObservaciones);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(160, 360, 610, 50);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Año");
        jPanel1.add(jLabel15);
        jLabel15.setBounds(320, 20, 40, 30);

        txtAnio.setEditable(false);
        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.setFocusable(false);
        jPanel1.add(txtAnio);
        txtAnio.setBounds(370, 20, 70, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("No. de Identificación (SPM)");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(320, 280, 180, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Municipio");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(30, 240, 120, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Fecha de Depósito");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(30, 280, 120, 30);

        dateDestino.setDateFormatString("dd/MMM/yyyy");
        dateDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(dateDestino);
        dateDestino.setBounds(620, 420, 150, 30);

        dateDeposito.setDateFormatString("dd/MMM/yyyy");
        dateDeposito.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(dateDeposito);
        dateDeposito.setBounds(160, 280, 150, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Fecha de Recepción");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(10, 320, 140, 30);

        txtOficio.setEditable(false);
        txtOficio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtOficio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOficio.setFocusable(false);
        jPanel1.add(txtOficio);
        txtOficio.setBounds(160, 20, 150, 30);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Fecha de Entrega al Destino");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(430, 420, 190, 30);

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbDestino);
        cmbDestino.setBounds(160, 420, 260, 30);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Destino");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(20, 420, 130, 30);

        dateNotificacion.setDateFormatString("dd/MMM/yyyy");
        dateNotificacion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(dateNotificacion);
        dateNotificacion.setBounds(160, 200, 150, 30);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Fecha de Notificación");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(0, 200, 150, 30);

        cmbMunicipio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbMunicipio);
        cmbMunicipio.setBounds(160, 240, 500, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Autoridad");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(70, 60, 80, 30);

        txtAutoridad.setEditable(false);
        txtAutoridad.setColumns(20);
        txtAutoridad.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtAutoridad.setLineWrap(true);
        txtAutoridad.setRows(2);
        txtAutoridad.setWrapStyleWord(true);
        txtAutoridad.setFocusable(false);
        jScrollPane1.setViewportView(txtAutoridad);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(160, 60, 610, 50);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expediente");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(70, 120, 80, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtExpediente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExpedienteFocusLost(evt);
            }
        });
        txtExpediente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpedienteKeyPressed(evt);
            }
        });
        jPanel1.add(txtExpediente);
        txtExpediente.setBounds(160, 120, 150, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Fecha de Carga");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(500, 20, 130, 30);

        txtFechaCarga.setEditable(false);
        txtFechaCarga.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFechaCarga.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtFechaCarga);
        txtFechaCarga.setBounds(640, 20, 130, 30);

        buttonGroup1.add(rbtnOficio);
        rbtnOficio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnOficio.setSelected(true);
        rbtnOficio.setText("Oficio");
        rbtnOficio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnOficioActionPerformed(evt);
            }
        });
        jPanel1.add(rbtnOficio);
        rbtnOficio.setBounds(160, 160, 80, 30);

        buttonGroup1.add(rbtnCorreo);
        rbtnCorreo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnCorreo.setText("Correo");
        rbtnCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnCorreoActionPerformed(evt);
            }
        });
        jPanel1.add(rbtnCorreo);
        rbtnCorreo.setBounds(240, 160, 80, 30);

        buttonGroup1.add(rbtnLista);
        rbtnLista.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnLista.setText("Lista");
        rbtnLista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnListaActionPerformed(evt);
            }
        });
        jPanel1.add(rbtnLista);
        rbtnLista.setBounds(320, 160, 80, 30);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda por Oficios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel4.setLayout(null);

        cmbOficios.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbOficios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOficiosActionPerformed(evt);
            }
        });
        jPanel4.add(cmbOficios);
        cmbOficios.setBounds(70, 30, 700, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Oficios");
        jPanel4.add(jLabel10);
        jLabel10.setBounds(10, 30, 50, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 785, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEsc)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        botonF12();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        u.focus(evt, txtObservaciones);
    }//GEN-LAST:event_txtObservacionesKeyPressed

    private void cmbOficiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbOficiosActionPerformed
        if(cmbOficios.getItemCount()>0){
            if(cmbOficios.getSelectedIndex()==0){
                u.cleanComponent(txtOficio,txtAnio,txtFechaCarga,txtAutoridad,txtExpediente,dateNotificacion,cmbMunicipio,txtSpm,txtObservaciones,cmbDestino,dateDestino);
                dateDeposito.setDate(null);
                dateRecepcion.setDate(null);
                rbtnOficio.setSelected(true);
                eventoRadioButtons();
            }else{
                cargaOficio();
            }
        }
        
    }//GEN-LAST:event_cmbOficiosActionPerformed

    private void txtExpedienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExpedienteFocusLost
        
    }//GEN-LAST:event_txtExpedienteFocusLost

    private void rbtnOficioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnOficioActionPerformed
        eventoRadioButtons();
    }//GEN-LAST:event_rbtnOficioActionPerformed

    private void rbtnCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnCorreoActionPerformed
        eventoRadioButtons();
    }//GEN-LAST:event_rbtnCorreoActionPerformed

    private void rbtnListaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnListaActionPerformed
        eventoRadioButtons();
    }//GEN-LAST:event_rbtnListaActionPerformed

    private void txtExpedienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtExpedienteKeyPressed
        /*if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(txtExpediente.getText().length()>0){
                String expediente = u.completaNumeroExpediente(txtExpediente.getText());
                if(expediente!=null){
                    txtExpediente.setText(expediente);
                }
            }
        }*/
    }//GEN-LAST:event_txtExpedienteKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cmbDestino;
    private javax.swing.JComboBox cmbMunicipio;
    private javax.swing.JComboBox cmbOficios;
    private com.toedter.calendar.JDateChooser dateDeposito;
    private com.toedter.calendar.JDateChooser dateDestino;
    private com.toedter.calendar.JDateChooser dateNotificacion;
    private com.toedter.calendar.JDateChooser dateRecepcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbtnCorreo;
    private javax.swing.JRadioButton rbtnLista;
    private javax.swing.JRadioButton rbtnOficio;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextArea txtAutoridad;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFechaCarga;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtOficio;
    private javax.swing.JTextField txtSpm;
    // End of variables declaration//GEN-END:variables
    
}

