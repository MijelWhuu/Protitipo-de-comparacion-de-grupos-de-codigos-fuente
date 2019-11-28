package interfaz.promociones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.utilidades.CalendarioOficialBl;
import bl.catalogos.DestinosBl;
import bl.catalogos.TipoPromocionesBl;
import bl.promociones.PromocionesBl;
import entitys.DestinoEntity;
import entitys.ExpedienteEntity;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import entitys.TipoPromocionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
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
public class Promociones extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    PromocionesBl bl = new PromocionesBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    TipoPromocionesBl blTipo = new TipoPromocionesBl();
    DestinosBl blDestino = new DestinosBl();
   
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public Promociones(){
        initComponents();
    }
    
    public Promociones(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatearCampos();
        obtenerFechaTiempo();
        inicializarCampos();
        cbxAcreditacion();
        mapeoTeclas();
        inicializaCombos();
    }
    
    private void formatearCampos(){
        txtFolio.setDocument(new JTextMascara(7,true,"numerico"));
        txtAnio.setDocument(new JTextMascara(4,true,"numerico"));
        txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        txtAnexoOriginales.setDocument(new JTextMascara(5,true,"numerico"));
        txtAnexoCopias.setDocument(new JTextMascara(5,true,"numerico"));
        txtHojas.setDocument(new JTextMascara(5,true,"numerico"));
        txtObservaciones.setDocument(new JTextMascaraTextArea());
        u.formatoJDateChooser(dateFecha);
    }
    
    private void obtenerFechaTiempo(){
        dateFecha.setDate(cal.getTime());
        cmbHoras.setSelectedIndex(cal.get(Calendar.HOUR_OF_DAY));
        cmbMinutos.setSelectedIndex(cal.get(Calendar.MINUTE));
    }
    
    private void inicializaCombos(){
        u.inicializaCombo(cmbTipo, new TipoPromocionEntity(), blTipo.getTipoPromociones());
        seleccionarDefaultTipo();
        u.inicializaCombo(cmbDesti, new DestinoEntity(), blDestino.getDestinos());
        seleccionarDefaultDestino();
        
    }
        
    private void seleccionarDefaultTipo(){
        for(int i=0; i<cmbTipo.getItemCount(); i++){
            if(((TipoPromocionEntity)cmbTipo.getItemAt(i)).getSeleccionar()){
                cmbTipo.setSelectedIndex(i);
                i=cmbTipo.getItemCount()+5;
            }
        }
    }
    
    private void seleccionarDefaultDestino(){
        for(int i=0; i<cmbDesti.getItemCount(); i++){
            if(((DestinoEntity)cmbDesti.getItemAt(i)).getSeleccionar()){
                cmbDesti.setSelectedIndex(i);
            }
        }
    }
    
    private void inicializarCampos(){
        txtAnio.setText(""+cal.get(Calendar.YEAR));
        txtFolio.setText(bl.getFolio(new Integer(txtAnio.getText())).toString());
    }
    
    private Boolean validar(){
        if(txtFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Campo Folio es obligatorio", "Alerta", 2);
            txtFolio.requestFocus();
        }else if(txtAnio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Campo Año es obligatorio", "Alerta", 2);
            txtAnio.requestFocus();
        }else if(txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Campo No. de Expediente es obligatorio", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(!bl.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El No. de Expediente no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(bl.existeFolio(new Integer(txtFolio.getText()), new Integer(txtAnio.getText()))){
             javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio ya existe", "Alerta", 2);
            txtFolio.requestFocus();
        }else if(cmbTipo.getItemCount()>0 && cmbTipo.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Tipo de Promoción", "Alerta", 2);
            cmbTipo.requestFocus();
        }else if(cmbDesti.getItemCount()>0 && cmbDesti.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Destino", "Alerta", 2);
            cmbDesti.requestFocus();
        }else if(!blCale.isfechaValida(new SimpleDateFormat("yyyy-MM-dd").format(dateFecha.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(cbxAcreditacion.isSelected() && txtHojas.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Número de Hojas es obligatorio", "Alerta", 2);
            txtHojas.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private void cbxAcreditacion(){
        txtHojas.setText("");
        if(cbxAcreditacion.isSelected()){
            lblAsterisco.setVisible(true);
            txtHojas.setEnabled(true);
        }else{
            lblAsterisco.setVisible(false);
            txtHojas.setEnabled(false);
        }
    }
    
    private PromocionEntity obtenerDatos(){
        PromocionEntity promo = new PromocionEntity();
        promo.setExpe(new ExpedienteEntity(txtExpediente.getText()));
        promo.setFolio(new Integer(txtFolio.getText()));
        promo.setAnio(new Integer(txtAnio.getText()));
        promo.setTipoPromocion((TipoPromocionEntity)cmbTipo.getSelectedItem());
        promo.setDestino((DestinoEntity)cmbDesti.getSelectedItem());
        promo.setFechaR(dateFecha.getDate().toString());
        promo.setHoraR(""+cmbHoras.getSelectedItem().toString()+":"+cmbMinutos.getSelectedItem()+":00");
        promo.setaOriginales((txtAnexoOriginales.getText().length()==0)?0:new Integer(txtAnexoOriginales.getText()));
        promo.setaCopias((txtAnexoCopias.getText().length()==0)?0:new Integer(txtAnexoCopias.getText()));
        promo.setAcreditacion(cbxAcreditacion.isSelected());
        promo.setHojas((txtHojas.getText().length()==0)?0:new Integer(txtHojas.getText()));
        promo.setObservaciones(txtObservaciones.getText());
        return promo;
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
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        obtenerFechaTiempo();
        inicializaCombos();
        inicializarCampos();
        cbxAcreditacion.setSelected(false);
        cbxAcreditacion();
        u.cleanComponent(txtAnexoOriginales,txtAnexoCopias,txtTotal,txtObservaciones,txtExpediente);
        txtExpediente.requestFocus();
    }
    
    private void botonF2(){
        if(validar()){
            ErrorEntity error = bl.savePromocion(obtenerDatos(), sesion);
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                botonEsc();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
            }
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

        jLabel1 = new javax.swing.JLabel();
        btnF2 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        cmbDesti = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        dateFecha = new com.toedter.calendar.JDateChooser();
        cmbHoras = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cmbMinutos = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        txtAnexoCopias = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtAnexoOriginales = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        cbxAcreditacion = new javax.swing.JCheckBox();
        lblAsterisco = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtHojas = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();

        setClosable(true);
        setTitle("Nueva Promoción");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_nuevas_promociones.jpg"))); // NOI18N

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información de la Promoción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 0, 0));
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel93.setText("*");
        jPanel2.add(jLabel93);
        jLabel93.setBounds(30, 110, 20, 30);

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel94.setText("Folio de la promoción");
        jPanel2.add(jLabel94);
        jLabel94.setBounds(20, 30, 140, 30);

        txtFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtFolio);
        txtFolio.setBounds(170, 30, 170, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Año de la Promoción");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(20, 70, 140, 30);

        txtAnio.setEditable(false);
        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.setFocusable(false);
        jPanel2.add(txtAnio);
        txtAnio.setBounds(170, 70, 100, 30);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("No. de Expediente");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(20, 110, 140, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExpedienteFocusLost(evt);
            }
        });
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(170, 110, 170, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("*");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(30, 150, 20, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Tipo de promoción");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(30, 150, 130, 30);

        cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbTipo);
        cmbTipo.setBounds(170, 150, 610, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Destino");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(100, 190, 60, 30);

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 0, 0));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("*");
        jPanel2.add(jLabel95);
        jLabel95.setBounds(10, 30, 20, 30);

        cmbDesti.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbDesti);
        cmbDesti.setBounds(170, 190, 320, 30);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Datos de la Recepción", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Fecha de Recepción");
        jPanel3.add(jLabel9);
        jLabel9.setBounds(10, 30, 140, 30);

        dateFecha.setDateFormatString("d/MMM/yyyy");
        dateFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel3.add(dateFecha);
        dateFecha.setBounds(160, 30, 130, 30);

        cmbHoras.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbHoras.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        jPanel3.add(cmbHoras);
        cmbHoras.setBounds(160, 80, 60, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Hora de Recepción");
        jPanel3.add(jLabel11);
        jLabel11.setBounds(20, 80, 130, 30);

        cmbMinutos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbMinutos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        jPanel3.add(cmbMinutos);
        cmbMinutos.setBounds(230, 80, 60, 30);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Anexos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel5.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("No. de Copias");
        jPanel5.add(jLabel12);
        jLabel12.setBounds(10, 50, 120, 30);

        txtAnexoCopias.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnexoCopias.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAnexoCopias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAnexoCopiasKeyReleased(evt);
            }
        });
        jPanel5.add(txtAnexoCopias);
        txtAnexoCopias.setBounds(140, 50, 90, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("No. de Originales");
        jPanel5.add(jLabel13);
        jLabel13.setBounds(10, 20, 120, 30);

        txtAnexoOriginales.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnexoOriginales.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtAnexoOriginales.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtAnexoOriginalesKeyReleased(evt);
            }
        });
        jPanel5.add(txtAnexoOriginales);
        txtAnexoOriginales.setBounds(140, 20, 90, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Total");
        jPanel5.add(jLabel14);
        jLabel14.setBounds(10, 80, 120, 30);

        txtTotal.setEditable(false);
        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtTotal.setFocusable(false);
        jPanel5.add(txtTotal);
        txtTotal.setBounds(140, 80, 90, 30);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Acreditación", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel6.setLayout(null);

        cbxAcreditacion.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxAcreditacion.setText("Doc. Acredita Personalidad");
        cbxAcreditacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxAcreditacionActionPerformed(evt);
            }
        });
        jPanel6.add(cbxAcreditacion);
        cbxAcreditacion.setBounds(10, 30, 220, 30);

        lblAsterisco.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblAsterisco.setForeground(new java.awt.Color(255, 0, 0));
        lblAsterisco.setText("*");
        jPanel6.add(lblAsterisco);
        lblAsterisco.setBounds(10, 70, 20, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setText("Número de Hojas");
        jPanel6.add(jLabel16);
        jLabel16.setBounds(20, 70, 120, 30);

        txtHojas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHojas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel6.add(txtHojas);
        txtHojas.setBounds(140, 70, 90, 30);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setLayout(null);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setText("Observaciones");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(10, 10, 160, 35);

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setRows(2);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtObservaciones);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 40, 780, 74);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
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

    private void txtAnexoCopiasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnexoCopiasKeyReleased
        Integer ori = (txtAnexoOriginales.getText().length()>0) ? new Integer(txtAnexoOriginales.getText()) : 0;
        Integer cop = (txtAnexoCopias.getText().length()>0) ? new Integer(txtAnexoCopias.getText()) : 0;
        txtTotal.setText(""+(ori+cop));
    }//GEN-LAST:event_txtAnexoCopiasKeyReleased

    private void txtAnexoOriginalesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnexoOriginalesKeyReleased
        Integer ori = (txtAnexoOriginales.getText().length()>0) ? new Integer(txtAnexoOriginales.getText()) : 0;
        Integer cop = (txtAnexoCopias.getText().length()>0) ? new Integer(txtAnexoCopias.getText()) : 0;
        txtTotal.setText(""+(ori+cop));
    }//GEN-LAST:event_txtAnexoOriginalesKeyReleased

    private void cbxAcreditacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxAcreditacionActionPerformed
        cbxAcreditacion();
    }//GEN-LAST:event_cbxAcreditacionActionPerformed

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        u.focus(evt, txtObservaciones);
    }//GEN-LAST:event_txtObservacionesKeyPressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtExpediente.requestFocus();
    }//GEN-LAST:event_formComponentShown

    private void txtExpedienteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExpedienteFocusLost
        /*if(txtExpediente.getText().length()>0){
            String expediente = u.completaNumeroExpediente(txtExpediente.getText());
            if(expediente!=null){
                txtExpediente.setText(expediente);
            }
        }*/
    }//GEN-LAST:event_txtExpedienteFocusLost

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox cbxAcreditacion;
    private javax.swing.JComboBox cmbDesti;
    private javax.swing.JComboBox cmbHoras;
    private javax.swing.JComboBox cmbMinutos;
    private javax.swing.JComboBox cmbTipo;
    private com.toedter.calendar.JDateChooser dateFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAsterisco;
    private javax.swing.JTextField txtAnexoCopias;
    private javax.swing.JTextField txtAnexoOriginales;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtHojas;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
    
}
