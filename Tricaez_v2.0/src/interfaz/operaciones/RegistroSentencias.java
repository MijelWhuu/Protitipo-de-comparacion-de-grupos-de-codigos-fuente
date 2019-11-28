package interfaz.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextDecimal;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.TipoDocumentosAsrBl;
import bl.catalogos.TipoSentenciasBl;
import bl.utilidades.CalendarioOficialBl;
import bl.operaciones.RegistroSentenciasBl;
import entitys.ExpedienteEntity;
import entitys.RegistroSentenciaEntity;
import entitys.SesionEntity;
import entitys.TipoDocumentoAsrEntity;
import entitys.TipoSentenciaEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class RegistroSentencias extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    RegistroSentenciasBl bl = new RegistroSentenciasBl();
    TipoDocumentosAsrBl blDoc = new TipoDocumentosAsrBl();
    TipoSentenciasBl blTipo = new TipoSentenciasBl();
    Utilidades u = new Utilidades();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public RegistroSentencias(){
        initComponents();
    }
    
    public RegistroSentencias(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblAuto.setVisible(false);
        lblResolucion.setVisible(false);
        mapeoTeclas();
        formatearCampos();
        inicializaComboDocumento();
        inicializaComboTipo();
        inicializarCampos();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void formatearCampos(){
        txtBuscarFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtBuscarAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        u.formatoJDateChooser(dateAudiencia);
        u.formatoJDateChooser(dateSentencia);
        u.formatoJDateChooser(dateAcuerdos);
        dateAudiencia.setDate(cal.getTime());
        dateSentencia.setDate(cal.getTime());
        dateAcuerdos.setDate(cal.getTime());
        txtCantidad.setDocument(new JTextDecimal(10,4));
        txtObservaciones.setDocument(new JTextMascaraTextArea());
    }
    
    private void validaFolioAuto() throws ParseException{
        u.cleanComponent(lblResolucion,lblAuto,txtFolio,txtAnio,txtExpediente,dateAudiencia,dateSentencia,
                dateAcuerdos,cmbDocumento,cmbSentido,txtCantidad,txtObservaciones);
        if(txtBuscarFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio del Auto a buscar", "Alerta", 2);
            txtBuscarFolio.requestFocus();
        }else{
            Integer folio = new Integer(txtBuscarFolio.getText());
            Integer anio = cal.get(Calendar.YEAR);
            if(txtBuscarAnio.getText().length()!=0){
                Integer num = u.verificaInteger(txtBuscarAnio.getText());
                if(num!=null)
                    anio = num;
            }
            RegistroSentenciaEntity reg = bl.getSentencia(folio, anio);
            if(reg!=null){
                cargaFolioAuto(reg);
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio no existe", "Alerta", 2);
                txtBuscarFolio.requestFocus();
            }
        }
    }
    
    private void inicializarCampos(){
        txtAnio.setText(""+cal.get(Calendar.YEAR));
        txtFolio.setText(bl.getFolio(new Integer(txtAnio.getText())).toString());
    }
    
    private void cargaFolioAuto(RegistroSentenciaEntity reg) throws ParseException{
        lblResolucion.setText(reg.getId_reg_sentencia());
        if(reg.getExp()!=null)
            txtExpediente.setText(reg.getExp().getExpediente());
        
        Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_audiencia());
        Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_sentencia());
        Date date3 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_acuerdo());
        dateAudiencia.setDate(date1);
        dateSentencia.setDate(date2);
        dateAcuerdos.setDate(date3);
        //dateAudiencia.setDate(reg.getFecha_audiencia());
        //dateSentencia.setDate(reg.getFecha_sentencia());
        //dateAcuerdos.setDate(reg.getFecha_acuerdo());
        if(reg.getDoc()!=null)
            cmbDocumento.setSelectedItem(reg.getDoc());
        if(reg.getTipo()!=null)
            cmbSentido.setSelectedItem(reg.getTipo());
        txtCantidad.setText(reg.getCantidad().toString());
        txtObservaciones.setText(reg.getObservaciones());
        txtFolio.setText(reg.getFolio().toString());
        txtAnio.setText(reg.getAnio().toString());
    }
    
    private void inicializaComboDocumento(){
        u.inicializaCombo(cmbDocumento, new TipoDocumentoAsrEntity(), blDoc.getTipoDocumentos());
        seleccionarDefaultDocumento();
    }
    
    private void seleccionarDefaultDocumento(){
        /*for(int i=0; i<cmbSentido.getItemCount(); i++){
            if(((SentidoEjecutoriaEntity)cmbSentido.getItemAt(i)).getSeleccionar()){
                cmbSentido.setSelectedIndex(i);
                //i=cmbSentido.getItemCount()+5;
            }
        }*/
    }
    
    private void inicializaComboTipo(){
        u.inicializaCombo(cmbSentido, new TipoSentenciaEntity(), blTipo.getTipoSentencias());
        seleccionarDefaultTipo();
    }
    
    private void seleccionarDefaultTipo(){
        /*for(int i=0; i<cmbSentido.getItemCount(); i++){
            if(((SentidoEjecutoriaEntity)cmbSentido.getItemAt(i)).getSeleccionar()){
                cmbSentido.setSelectedIndex(i);
                //i=cmbSentido.getItemCount()+5;
            }
        }*/
    }
    
    private RegistroSentenciaEntity obtenerDatos(){
        RegistroSentenciaEntity reg = new RegistroSentenciaEntity();        
        reg.setId_reg_sentencia(lblResolucion.getText());
        reg.setExp(new ExpedienteEntity(txtExpediente.getText()));
        reg.setFecha_audiencia(dateAudiencia.getDate().toString());
        reg.setFecha_sentencia(dateSentencia.getDate().toString());
        reg.setFecha_acuerdo(dateAcuerdos.getDate().toString());
        reg.setDoc(null);
        if(cmbDocumento.getItemCount()>0 && cmbDocumento.getSelectedIndex()!=0)
            reg.setDoc((TipoDocumentoAsrEntity)cmbDocumento.getSelectedItem());
        reg.setTipo(null);
        if(cmbSentido.getItemCount()>0 && cmbSentido.getSelectedIndex()!=0)
            reg.setTipo((TipoSentenciaEntity)cmbSentido.getSelectedItem());
        reg.setCantidad(0.0);
        if(txtCantidad.getText().length()>0 && u.isDouble(txtCantidad.getText())){
            reg.setCantidad(new Double(txtCantidad.getText()));
        }
        reg.setObservaciones(txtObservaciones.getText());
        reg.setFolio(new Integer(txtFolio.getText()));
        reg.setAnio(cal.get(Calendar.YEAR));
        if(txtAnio.getText().length()>0)
            reg.setAnio(new Integer(txtAnio.getText()));
        return reg;
    }
    
    private Boolean validarRegistro(){
        Boolean flag = false;
        if(txtFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Folio del Auto", "Alerta", 2);
            txtFolio.requestFocus();
        }else if(txtExpediente.getText().length()!=0 && !bl.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El expediente ingresado no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(!blCale.isfechaValida(u.dateCastString(dateAudiencia.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha de la Audiencia es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(!blCale.isfechaValida(u.dateCastString(dateSentencia.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha de Sentencia-Resolución es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(!blCale.isfechaValida(u.dateCastString(dateAcuerdos.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha de la Lista de Acuerdos es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(txtCantidad.getText().length()>0 && !u.isDouble(txtCantidad.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La cantidad de Pretensión Autorizada es incorrecta", "Alerta", 2);
            txtCantidad.requestFocus();
        }else{
            flag = true;
        }
        return flag;
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
        u.cleanComponent(txtBuscarFolio,txtBuscarAnio,lblResolucion,lblAuto,txtFolio,txtAnio,txtExpediente,
                dateAudiencia,dateSentencia,dateAcuerdos,cmbDocumento,cmbSentido,txtCantidad,txtObservaciones);
        inicializarCampos();
        txtExpediente.requestFocus();
    }
    
    private void botonF2(){
        if(validarRegistro()){
            if(lblResolucion.getText().length()==0){
                ErrorEntity error = bl.saveRegistro(obtenerDatos(), sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }else{
                ErrorEntity error = bl.updateRegistro(obtenerDatos(), sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){
        if(lblResolucion.getText().length()!=0){
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar la Resolución seleccionada?", "Alerta", 0)==0){
                ErrorEntity error = bl.deleteRegistro(lblResolucion.getText());
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado una Resolución para eliminar", "Alerta", 2);
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
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarFolio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        txtBuscarAnio = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        lblAuto = new javax.swing.JLabel();
        lblResolucion = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cmbDocumento = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        cmbSentido = new javax.swing.JComboBox();
        txtCantidad = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        dateAudiencia = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        dateSentencia = new com.toedter.calendar.JDateChooser();
        jLabel12 = new javax.swing.JLabel();
        dateAcuerdos = new com.toedter.calendar.JDateChooser();
        jLabel100 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Registro de Sentencias - Resoluciones de Mesas de Trámite");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Registro_sentencias.jpg"))); // NOI18N

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

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("<html><center>F2 - Guardar<br>Resolución</center></html>");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("<html><center>F6 - Eliminar<br>Resolución</center></html>");
        btnF6.setFocusable(false);
        btnF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF6ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Folio");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(140, 20, 40, 30);

        txtBuscarFolio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtBuscarFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(180, 20, 130, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Año");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(340, 20, 40, 30);

        btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBuscar.setText("Buscar Auto");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar);
        btnBuscar.setBounds(520, 20, 130, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarAnioKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(380, 20, 80, 30);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Folio");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(50, 30, 40, 30);

        txtFolio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtFolio);
        txtFolio.setBounds(100, 30, 140, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Año");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(250, 30, 40, 30);

        txtAnio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtAnio);
        txtAnio.setBounds(300, 30, 80, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expediente");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 70, 80, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpedienteKeyPressed(evt);
            }
        });
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(100, 70, 140, 30);

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
        jScrollPane3.setViewportView(txtObservaciones);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(20, 400, 630, 60);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Observaciones");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(20, 380, 100, 20);

        lblAuto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblAuto);
        lblAuto.setBounds(620, 30, 30, 30);

        lblResolucion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblResolucion);
        lblResolucion.setBounds(570, 30, 30, 30);

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 0, 0));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("*");
        jPanel2.add(jLabel95);
        jLabel95.setBounds(0, 70, 30, 30);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setText("Tipo de Documento");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(20, 210, 130, 20);

        cmbDocumento.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbDocumento);
        cmbDocumento.setBounds(20, 230, 630, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setText("Pretensión Autorizada (Cantidad)");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(20, 340, 230, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setText("Sentido de la Sentencia-Resolución");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(20, 270, 270, 20);

        cmbSentido.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbSentido);
        cmbSentido.setBounds(20, 290, 630, 30);

        txtCantidad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtCantidad);
        txtCantidad.setBounds(250, 340, 140, 30);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fechas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        dateAudiencia.setDateFormatString("dd/MMM/yyyy");
        dateAudiencia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel3.add(dateAudiencia);
        dateAudiencia.setBounds(20, 40, 180, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setText("Audiencia");
        jPanel3.add(jLabel15);
        jLabel15.setBounds(30, 20, 150, 20);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Sentencia-Resolución");
        jPanel3.add(jLabel18);
        jLabel18.setBounds(240, 20, 160, 20);

        dateSentencia.setDateFormatString("dd/MMM/yyyy");
        dateSentencia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel3.add(dateSentencia);
        dateSentencia.setBounds(230, 40, 180, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("Lista de Acuerdos");
        jPanel3.add(jLabel12);
        jLabel12.setBounds(450, 20, 160, 20);

        dateAcuerdos.setDateFormatString("dd/MMM/yyyy");
        dateAcuerdos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel3.add(dateAcuerdos);
        dateAcuerdos.setBounds(440, 40, 180, 30);

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 0, 0));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel100.setText("*");
        jPanel3.add(jLabel100);
        jLabel100.setBounds(10, 20, 20, 20);

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 0, 0));
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel99.setText("*");
        jPanel3.add(jLabel99);
        jLabel99.setBounds(210, 20, 30, 20);

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 0, 0));
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel101.setText("*");
        jPanel3.add(jLabel101);
        jLabel101.setBounds(410, 20, 40, 20);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 110, 640, 90);

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 0, 0));
        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel96.setText("*");
        jPanel2.add(jLabel96);
        jLabel96.setBounds(40, 30, 30, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(360, 360, 360)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        setBounds(0, 0, 845, 650);
    }// </editor-fold>//GEN-END:initComponents

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                validaFolioAuto();
            } catch (ParseException ex) {
                Logger.getLogger(RegistroSentencias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            validaFolioAuto();
        } catch (ParseException ex) {
            Logger.getLogger(RegistroSentencias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        u.focus(evt, txtObservaciones);
    }//GEN-LAST:event_txtObservacionesKeyPressed

    private void txtBuscarAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAnioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                validaFolioAuto();
            } catch (ParseException ex) {
                Logger.getLogger(RegistroSentencias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtBuscarAnioKeyPressed

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

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtExpediente.requestFocus();
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF6;
    private javax.swing.JComboBox cmbDocumento;
    private javax.swing.JComboBox cmbSentido;
    private com.toedter.calendar.JDateChooser dateAcuerdos;
    private com.toedter.calendar.JDateChooser dateAudiencia;
    private com.toedter.calendar.JDateChooser dateSentencia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JLabel lblResolucion;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtBuscarAnio;
    private javax.swing.JTextField txtBuscarFolio;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextArea txtObservaciones;
    // End of variables declaration//GEN-END:variables

    
    
}
