package interfaz.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.SentidoEjecucionesBl;
import bl.utilidades.CalendarioOficialBl;
import bl.operaciones.RegistroResolucionesBl;
import entitys.ExpedienteEntity;
import entitys.ExpedienteTramiteEntity;
import entitys.RegistroAutoEntity;
import entitys.RegistroResolucionEntity;
import entitys.SentidoEjecutoriaEntity;
import entitys.SesionEntity;
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
public class RegistroResoluciones extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    RegistroResolucionesBl bl = new RegistroResolucionesBl();
    SentidoEjecucionesBl blSen = new SentidoEjecucionesBl();
    Utilidades u = new Utilidades();
    CalendarioOficialBl blCale = new CalendarioOficialBl();

    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public RegistroResoluciones(){
        initComponents();
    }
    
    public RegistroResoluciones(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblAuto.setVisible(false);
        lblTramite.setVisible(false);
        lblResolucion.setVisible(false);
        mapeoTeclas();
        formatearCampos();
        inicializaComboTramite();
        inicializaComboSentido();
        inicializarCampos();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void formatearCampos(){
        txtBuscarFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtBuscarAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtInfoTramite.setDocument(new JTextMascara(300));
        txtInfoFecha.setDocument(new JTextMascara(29,true,"todo"));
        txtInfoRazon.setDocument(new JTextMascaraTextArea());
        txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        u.formatoJDateChooser(dateResolucion);
        u.formatoJDateChooser(dateAcuerdos);
        dateResolucion.setDate(cal.getTime());
        dateAcuerdos.setDate(cal.getTime());
        txtMotivo.setDocument(new JTextMascaraTextArea());
    }
    
    private void inicializarCampos(){
        txtAnio.setText(""+cal.get(Calendar.YEAR));
        txtFolio.setText(bl.getFolio(new Integer(txtAnio.getText())).toString());
    }
    
    private void inicializaComboTramite(){
        u.inicializaCombo(cmbTramite, new ExpedienteTramiteEntity(), bl.getExpedientesTramites());
        seleccionarDefaultSentido();
    }
    
    private void validaFolioAuto() throws ParseException{
        u.cleanComponent(lblResolucion,lblAuto,lblTramite,txtFolio,txtAnio,txtInfoTramite,
                txtInfoFecha,txtInfoRazon,txtExpediente,dateResolucion,dateAcuerdos,cbxActuaria,cmbSentido,cbxArchivo,txtMotivo);
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
            RegistroResolucionEntity reg = bl.getResolucion(folio, anio);
            if(reg!=null){
                cargaFolioAuto(reg);
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio no existe", "Alerta", 2);
                txtBuscarFolio.requestFocus();
            }
        }
    }
    
    private void cargaFolioAuto(RegistroResolucionEntity reg) throws ParseException{
        lblResolucion.setText(reg.getId_reg_resolucion());
        cmbTramite.setSelectedIndex(0);
        if(reg.getTramite()!=null)
            cmbTramite.setSelectedItem(reg.getTramite());
        txtExpediente.setText(reg.getExp().getExpediente());
        Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_resolucion());
        Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_acuerdo());
        dateResolucion.setDate(date1);
        dateAcuerdos.setDate(date2);
        /*dateResolucion.setDate(reg.getFecha_resolucion());
        dateAcuerdos.setDate(reg.getFecha_acuerdo());*/
        
        cbxActuaria.setSelected(reg.getActuaria());
        cmbSentido.setSelectedIndex(0);
        if(reg.getSentido()!=null)
            cmbSentido.setSelectedItem(reg.getSentido());
        cbxArchivo.setSelected(reg.getArchivar());
        txtMotivo.setText(reg.getMotivo());
        txtFolio.setText(reg.getFolio().toString());
        txtAnio.setText(reg.getAnio().toString());
    }
    
    private void inicializaComboSentido(){
        u.inicializaCombo(cmbSentido, new SentidoEjecutoriaEntity(), blSen.getSentidoEjecuciones());
        seleccionarDefaultSentido();
    }
    
    private void seleccionarDefaultSentido(){
        /*for(int i=0; i<cmbSentido.getItemCount(); i++){
            if(((SentidoEjecutoriaEntity)cmbSentido.getItemAt(i)).getSeleccionar()){
                cmbSentido.setSelectedIndex(i);
                //i=cmbSentido.getItemCount()+5;
            }
        }*/
    }
    
    private RegistroResolucionEntity obtenerDatos(){
        RegistroResolucionEntity reg = new RegistroResolucionEntity();        
        reg.setId_reg_resolucion(lblResolucion.getText());
        reg.setTramite(null);
        if(cmbTramite.getItemCount()>0 && cmbTramite.getSelectedIndex()!=0)
            reg.setTramite((ExpedienteTramiteEntity)cmbTramite.getSelectedItem());
        reg.setExp(new ExpedienteEntity(txtExpediente.getText()));
        reg.setFecha_resolucion(dateResolucion.getDate().toString());
        reg.setFecha_acuerdo(dateAcuerdos.getDate().toString());
        reg.setActuaria(cbxActuaria.isSelected());
        reg.setSentido(null);
        if(cmbSentido.getItemCount()>0 && cmbSentido.getSelectedIndex()!=0)
            reg.setSentido((SentidoEjecutoriaEntity)cmbSentido.getSelectedItem());
        reg.setArchivar(cbxArchivo.isSelected());
        reg.setMotivo(txtMotivo.getText());
        reg.setFolio(new Integer(txtFolio.getText()));
        reg.setAnio(cal.get(Calendar.YEAR));
        if(txtAnio.getText().length()>0)
            reg.setAnio(new Integer(txtAnio.getText()));
        return reg;
    }
    
    private Boolean validarRegistro(){
        Boolean flag = false;
        if(txtFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Folio", "Alerta", 2);
            txtFolio.requestFocus();
        }else if(txtExpediente.getText().length()!=0 && !bl.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El expediente ingresado no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(!blCale.isfechaValida(u.dateCastString(dateResolucion.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha de la Resolución es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(!blCale.isfechaValida(u.dateCastString(dateAcuerdos.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha de la Lista de Acuerdos es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
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
        u.cleanComponent(txtBuscarFolio,txtBuscarAnio,lblResolucion,lblAuto,lblTramite,txtFolio,txtAnio,txtInfoTramite,cmbTramite,
                txtInfoFecha,txtInfoRazon,txtExpediente,dateResolucion,dateAcuerdos,cbxActuaria,cmbSentido,cbxArchivo,txtMotivo);
        cmbTramite.requestFocus();
        inicializarCampos();
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
        jScrollPane2 = new javax.swing.JScrollPane();
        txtInfoRazon = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        lblAuto = new javax.swing.JLabel();
        lblTramite = new javax.swing.JLabel();
        lblResolucion = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        txtInfoTramite = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtInfoFecha = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cbxArchivo = new javax.swing.JCheckBox();
        cmbSentido = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        dateResolucion = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        dateAcuerdos = new com.toedter.calendar.JDateChooser();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        cbxActuaria = new javax.swing.JCheckBox();
        jLabel22 = new javax.swing.JLabel();
        cmbTramite = new javax.swing.JComboBox();
        jLabel23 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Registro de Resoluciones de la Secretaría de Acuerdos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/registro_resoluciones.jpg"))); // NOI18N

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

        txtBuscarFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(180, 20, 140, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Año");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(370, 20, 40, 30);

        btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBuscar.setText("Buscar Auto");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar);
        btnBuscar.setBounds(580, 20, 130, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarAnioKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(410, 20, 80, 30);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        txtInfoRazon.setEditable(false);
        txtInfoRazon.setColumns(20);
        txtInfoRazon.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtInfoRazon.setLineWrap(true);
        txtInfoRazon.setRows(1);
        txtInfoRazon.setWrapStyleWord(true);
        txtInfoRazon.setFocusable(false);
        txtInfoRazon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInfoRazonKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtInfoRazon);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(110, 190, 630, 50);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Folio");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(60, 30, 40, 30);

        txtFolio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtFolio);
        txtFolio.setBounds(110, 30, 140, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Año");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(270, 30, 40, 30);

        txtAnio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtAnio);
        txtAnio.setBounds(320, 30, 80, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Expediente");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(10, 250, 90, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(110, 250, 140, 30);

        txtMotivo.setColumns(20);
        txtMotivo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtMotivo.setLineWrap(true);
        txtMotivo.setRows(1);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMotivoKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtMotivo);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(110, 410, 630, 60);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Motivo");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(0, 410, 100, 30);

        lblAuto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblAuto);
        lblAuto.setBounds(650, 30, 30, 30);

        lblTramite.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblTramite);
        lblTramite.setBounds(700, 30, 30, 30);

        lblResolucion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblResolucion);
        lblResolucion.setBounds(600, 30, 30, 30);

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 0, 0));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("*");
        jPanel2.add(jLabel95);
        jLabel95.setBounds(10, 250, 30, 30);

        txtInfoTramite.setEditable(false);
        txtInfoTramite.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtInfoTramite.setFocusable(false);
        jPanel2.add(txtInfoTramite);
        txtInfoTramite.setBounds(110, 110, 630, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("<html><div align=\"right\">Razón por<br>la que se<br>promueve</div></html>");
        jLabel15.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jPanel2.add(jLabel15);
        jLabel15.setBounds(0, 190, 100, 50);

        txtInfoFecha.setEditable(false);
        txtInfoFecha.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtInfoFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtInfoFecha.setFocusable(false);
        jPanel2.add(txtInfoFecha);
        txtInfoFecha.setBounds(110, 150, 140, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Trámite");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(40, 110, 60, 30);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("<html><div align=\"right\">Sentido de la<br>Ejecutoria</div></html>");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(10, 370, 90, 30);

        cbxArchivo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxArchivo.setText("Ordenar a Archivo");
        jPanel2.add(cbxArchivo);
        cbxArchivo.setBounds(420, 250, 150, 30);

        cmbSentido.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbSentido);
        cmbSentido.setBounds(110, 370, 630, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("<html><div align=\"right\">Fecha de Lista<br>de Acuerdos</div></html>");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(0, 330, 100, 30);

        dateResolucion.setDateFormatString("dd/MMM/yyyy");
        dateResolucion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(dateResolucion);
        dateResolucion.setBounds(110, 290, 140, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("<html><div align=\"right\">Fecha de<br>Resolución</div></html>");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(10, 290, 90, 30);

        dateAcuerdos.setDateFormatString("dd/MMM/yyyy");
        dateAcuerdos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(dateAcuerdos);
        dateAcuerdos.setBounds(110, 330, 140, 30);

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 0, 0));
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel99.setText("*");
        jPanel2.add(jLabel99);
        jLabel99.setBounds(-10, 330, 30, 20);

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 0, 0));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel100.setText("*");
        jPanel2.add(jLabel100);
        jLabel100.setBounds(10, 290, 40, 20);

        cbxActuaria.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxActuaria.setText("Enviar a Actuaría");
        jPanel2.add(cbxActuaria);
        cbxActuaria.setBounds(270, 250, 150, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("<html><div align=\"right\">Expedientes<br>en Trámite</div></html>");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(10, 70, 90, 30);

        cmbTramite.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTramite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTramiteActionPerformed(evt);
            }
        });
        jPanel2.add(cmbTramite);
        cmbTramite.setBounds(110, 70, 630, 30);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("<html><div align=\"right\">Fecha de Inicio<br>del Trámite</div></html>");
        jPanel2.add(jLabel23);
        jLabel23.setBounds(10, 150, 90, 30);

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 0, 0));
        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel96.setText("*");
        jPanel2.add(jLabel96);
        jLabel96.setBounds(50, 30, 30, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 964, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        setBounds(0, 0, 940, 668);
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

    private void txtInfoRazonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInfoRazonKeyPressed
        u.focus(evt, txtInfoRazon);
    }//GEN-LAST:event_txtInfoRazonKeyPressed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                validaFolioAuto();
            } catch (ParseException ex) {
                Logger.getLogger(RegistroResoluciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            validaFolioAuto();
        } catch (ParseException ex) {
            Logger.getLogger(RegistroResoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtMotivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMotivoKeyPressed
        u.focus(evt, txtMotivo);
    }//GEN-LAST:event_txtMotivoKeyPressed

    private void txtBuscarAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAnioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                validaFolioAuto();
            } catch (ParseException ex) {
                Logger.getLogger(RegistroResoluciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtBuscarAnioKeyPressed

    private void cmbTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTramiteActionPerformed
        if(cmbTramite.getItemCount()>0){
            if(cmbTramite.getSelectedIndex()==0){
                u.cleanComponent(lblTramite,txtInfoTramite,txtInfoFecha,txtInfoRazon,txtExpediente);
            }else{
                u.cleanComponent(lblTramite,txtInfoTramite,txtInfoFecha,txtInfoRazon,txtExpediente);
                ExpedienteTramiteEntity tra = (ExpedienteTramiteEntity)cmbTramite.getSelectedItem();
                lblTramite.setText(tra.getId_inicio_tramite());
                txtInfoTramite.setText(tra.getNombre());
                txtInfoFecha.setText(u.dateToString(tra.getFecha()));
                txtInfoRazon.setText(tra.getRazon());
                txtExpediente.setText(tra.getExpediente());
            }
        }
    }//GEN-LAST:event_cmbTramiteActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        cmbTramite.requestFocus();
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF6;
    private javax.swing.JCheckBox cbxActuaria;
    private javax.swing.JCheckBox cbxArchivo;
    private javax.swing.JComboBox cmbSentido;
    private javax.swing.JComboBox cmbTramite;
    private com.toedter.calendar.JDateChooser dateAcuerdos;
    private com.toedter.calendar.JDateChooser dateResolucion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JLabel lblResolucion;
    private javax.swing.JLabel lblTramite;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtBuscarAnio;
    private javax.swing.JTextField txtBuscarFolio;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtInfoFecha;
    private javax.swing.JTextArea txtInfoRazon;
    private javax.swing.JTextField txtInfoTramite;
    private javax.swing.JTextArea txtMotivo;
    // End of variables declaration//GEN-END:variables

    
    
}
