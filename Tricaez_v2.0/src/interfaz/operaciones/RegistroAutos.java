package interfaz.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.AutosBl;
import bl.catalogos.SuspensionesBl;
import bl.utilidades.CalendarioOficialBl;
import bl.operaciones.RegistroAutosBl;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entitys.AutoEntity;
import entitys.ExpedienteEntity;
import entitys.PromocionEntity;
import entitys.RegistroAutoEntity;
import entitys.SesionEntity;
import entitys.SuspensionEntity;
import entitys.TramiteEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mavg
 */
public class RegistroAutos extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    RegistroAutosBl bl = new RegistroAutosBl();
    AutosBl blTipo = new AutosBl();
    SuspensionesBl blSus = new SuspensionesBl();
    Utilidades u = new Utilidades();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    AutoCompleteSupport autocomplete;
    
    /**
     * Creates new form Destinos
     */
    public RegistroAutos(){
        initComponents();
    }
    
    public RegistroAutos(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblAuto.setVisible(false);
        mapeoTeclas();
        formatearCampos();
        inicializaComboSuspension();
        inicializaComboTipo();
        inicializarCampos();
        btnMenos.setVisible(false);
        jButton2.setVisible(false);
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones">
    private void formatearCampos(){
        txtBuscarFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        txtFolioPromocion.setDocument(new JTextMascara(29));
        txtAnioPromocion.setDocument(new JTextMascara(4,true,"numerico"));
        txtFolioAuto.setDocument(new JTextMascara(5,true,"numerico"));
        txtAnioAuto.setDocument(new JTextMascara(4,true,"numerico"));
        u.formatoJDateChooser(dateAuto);
        u.formatoJDateChooser(dateAcuerdos);
        dateAuto.setDate(cal.getTime());
        dateAcuerdos.setDate(cal.getTime());
        txtMotivo.setDocument(new JTextMascaraTextArea());
        txtEfectos.setDocument(new JTextMascaraTextArea());
        txtObservaciones.setDocument(new JTextMascaraTextArea());
    }
        
    private void validaFolioAuto() throws ParseException{
        u.cleanComponent(txtFolioPromocion,txtAnioPromocion,txtExpediente,cmbTipo,cbxOrdenar,dateAuto,dateAcuerdos,cbxPrueba1,cbxPrueba2,
                cbxPrueba3,cbxPrueba4,cbxPrueba5,cbxPrueba6,txtMotivo,cmbSuspension,txtEfectos,txtObservaciones,lblAuto);
        cmbPromocion.removeAllItems();
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
            if(bl.existeFolio(folio, anio)){
                cargaFolioAuto(folio,anio);
                txtBuscarFolio.setText("");
                txtBuscarAnio.setText("");
                txtFolioPromocion.requestFocus();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio del Auto no existe", "Alerta", 2);
                txtBuscarFolio.requestFocus();
            }
        }
    }
    
    private void cargaFolioAuto(Integer folio, Integer anio) throws ParseException{
        RegistroAutoEntity reg = bl.getAuto(folio, anio);
        lblAuto.setText(reg.getId_registro_auto());
        txtFolioAuto.setText(reg.getFolio().toString());
        txtAnioAuto.setText(reg.getAnio().toString());
        
        ArrayList array = bl.getPromociones(reg.getId_registro_auto());
        if(!array.isEmpty()){
            for(Object array1 : array) {
                cmbPromocion.addItem(array1);
            }
        }
        
        if(reg.getExp()!=null)
            txtExpediente.setText(reg.getExp().getExpediente());
        seleccionarComboTipo(reg.getTipo_auto());
        cbxOrdenar.setSelected(reg.getOrdenar());
        
        Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_auto());
        Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(reg.getFecha_acuerdo());
        dateAuto.setDate(date1);
        dateAcuerdos.setDate(date2);
        /*dateAuto.setDate(reg.getFecha_auto());
        dateAcuerdos.setDate(reg.getFecha_acuerdo());*/
        cbxPrueba1.setSelected(reg.getPrueba1());
        cbxPrueba2.setSelected(reg.getPrueba2());
        cbxPrueba3.setSelected(reg.getPrueba3());
        cbxPrueba4.setSelected(reg.getPrueba4());
        cbxPrueba5.setSelected(reg.getPrueba5());
        cbxPrueba6.setSelected(reg.getPrueba6());
        txtMotivo.setText(reg.getMotivo());
        cmbSuspension.setSelectedIndex(0);
        if(reg.getSuspe()!=null)
            cmbSuspension.setSelectedItem(reg.getSuspe());
        txtEfectos.setText(reg.getEfectos());
        txtObservaciones.setText(reg.getObservaciones());
    }
    
    private void cargaPromocion(PromocionEntity pro,TramiteEntity tra){
        Boolean existe = false;
        for(int i=0; i<cmbPromocion.getItemCount(); i++){
            PromoTramiteEntity proE = (PromoTramiteEntity) cmbPromocion.getItemAt(i);
            if(pro.getFolio().equals(proE.getPro().getFolio())&& pro.getAnio().equals(proE.getPro().getAnio()))
                existe = true;
        }
        if(!existe){
            cmbPromocion.addItem(new PromoTramiteEntity(pro,tra));
            cmbPromocion.setSelectedIndex(cmbPromocion.getItemCount()-1);
        }
        txtFolioPromocion.setText("");
        txtAnioPromocion.setText("");
    }
    
    private void validaFolioPromocion(){
        if(txtFolioPromocion.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio de la Promoción a buscar", "Alerta", 2);
            txtFolioPromocion.requestFocus();
        }else{
            Integer folio = new Integer(txtFolioPromocion.getText());
            Integer anio = cal.get(Calendar.YEAR);
            if(txtAnioPromocion.getText().length()!=0){
                Integer num = u.verificaInteger(txtAnioPromocion.getText());
                if(num!=null)
                    anio = num;
            }
            PromocionEntity pro = bl.getPromocion(folio, anio);
            if(pro==null){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio de la Promoción no existe", "Alerta", 2);
                txtFolioPromocion.requestFocus();
            }else{
                cargaPromocion(pro,bl.getTramite(folio, anio));
                txtFolioPromocion.requestFocus();
            }
        }
    }
    
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
    
    private void seleccionarComboTipo(AutoEntity tipo){
        for(int i=0; i<cmbTipo.getItemCount(); i++){
            AutoEntity promo = (AutoEntity)cmbTipo.getItemAt(i);
            if(promo.equals(tipo)){
                cmbTipo.setSelectedIndex(i);
                i=cmbTipo.getItemCount()+5;
            }
        }
    }
    
    private void inicializaComboSuspension(){
        u.inicializaCombo(cmbSuspension, new SuspensionEntity(), blSus.getSuspensiones());
        seleccionarDefaultSuspension();
    }
    
    private void seleccionarDefaultSuspension(){
        for(int i=0; i<cmbSuspension.getItemCount(); i++){
            if(((SuspensionEntity)cmbSuspension.getItemAt(i)).getSeleccionar()){
                cmbSuspension.setSelectedIndex(i);
            }
        }
    }
    
    private void inicializarCampos(){
        txtAnioAuto.setText(""+cal.get(Calendar.YEAR));
        txtFolioAuto.setText(bl.getFolio(new Integer(txtAnioAuto.getText())).toString());
    }
    
    private RegistroAutoEntity obtenerDatos(){
        RegistroAutoEntity reg = new RegistroAutoEntity();
        reg.setId_registro_auto(lblAuto.getText());
        
        reg.setProtra(null);
        if(cmbPromocion.getItemCount()>0){
            ArrayList array = new ArrayList();
            for(int i=0; i<cmbPromocion.getItemCount(); i++){
                array.add(cmbPromocion.getItemAt(i));
            }
            reg.setProtra(array);
        }
        reg.setExp(null);
        if(txtExpediente.getText().length()>0)
            reg.setExp(new ExpedienteEntity(txtExpediente.getText()));
        reg.setFolio(new Integer(txtFolioAuto.getText()));
        reg.setAnio(cal.get(Calendar.YEAR));
        if(txtAnioAuto.getText().length()!=0)
            reg.setAnio(new Integer(txtAnioAuto.getText()));
        reg.setTipo_auto((AutoEntity)cmbTipo.getSelectedItem());
        reg.setOrdenar(cbxOrdenar.isSelected());
        reg.setFecha_auto(dateAuto.getDate().toString());
        reg.setFecha_acuerdo(dateAcuerdos.getDate().toString());
        /*reg.setFecha_auto(dateAuto.getDate());
        reg.setFecha_acuerdo(dateAcuerdos.getDate());*/
        reg.setPrueba1(cbxPrueba1.isSelected());
        reg.setPrueba2(cbxPrueba2.isSelected());
        reg.setPrueba3(cbxPrueba3.isSelected());
        reg.setPrueba4(cbxPrueba4.isSelected());
        reg.setPrueba5(cbxPrueba5.isSelected());
        reg.setPrueba6(cbxPrueba6.isSelected());
        reg.setMotivo(txtMotivo.getText());
        reg.setSuspe(null);
        if(cmbSuspension.getSelectedIndex()!=0)
            reg.setSuspe((SuspensionEntity)cmbSuspension.getSelectedItem());
        reg.setEfectos(txtEfectos.getText());
        reg.setObservaciones(txtObservaciones.getText());
        return reg;
    }
    
    private Boolean verificarFolioAuto(){
        Integer folio = new Integer(txtFolioAuto.getText());
        Integer anio = cal.get(Calendar.YEAR);
        if(txtAnioAuto.getText().length()!=0){
            Integer num = u.verificaInteger(txtAnioAuto.getText());
            if(num!=null)
                anio = num;
        }
        if(lblAuto.getText().length()==0){
            return bl.existeFolioAuto(folio, anio);
        }else{
            return bl.existeFolioAuto(folio, anio, lblAuto.getText());
        }
    }
    
    private Boolean validarAuto(){
        Boolean flag = false;
        if(txtFolioAuto.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingresado el Folio del Auto", "Alerta", 2);
            txtFolioAuto.requestFocus();
        }else if(verificarFolioAuto()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio del Auto ya existe", "Alerta", 2);
            txtFolioAuto.requestFocus();
        }/*else if(txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el No. de Expediente", "Alerta", 2);
            txtExpediente.requestFocus();
        }*/else if(txtExpediente.getText().length()>0&&!bl.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El No. de Expediente no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(cmbTipo.getItemCount()>0 && cmbTipo.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el Tipo de Auto", "Alerta", 2);
            cmbTipo.requestFocus();
        }else if(!blCale.isfechaValida(u.dateCastString(dateAuto.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha del Auto es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
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
        inicializaComboSuspension();
        inicializarCampos();
        u.cleanComponent(txtBuscarFolio,txtExpediente,cmbTipo,
                cbxOrdenar,dateAuto,dateAcuerdos,cbxPrueba1,cbxPrueba2,cbxPrueba3,cbxPrueba4,cbxPrueba5,cbxPrueba6,txtMotivo,
                cmbSuspension,txtEfectos,txtObservaciones,lblAuto);
        cmbPromocion.removeAllItems();
    }
    
    private void botonF2(){
        if(validarAuto()){
            if(lblAuto.getText().length()==0){
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
        if(lblAuto.getText().length()!=0){
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar el Auto seleccionado?", "Alerta", 0)==0){
                ErrorEntity error = bl.deleteRegistro(lblAuto.getText());
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    if(error.getNumError().equals(1451)){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar el Auto seleccionado ya que está siendo utilizado por otra información", "Aviso", 1);
                        txtBuscarFolio.requestFocus();
                    }else{
                        javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                    }
                }
            }
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Auto para eliminar", "Alerta", 2);
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
        jLabel2 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMotivo = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        txtFolioAuto = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtAnioAuto = new javax.swing.JTextField();
        cbxOrdenar = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        cbxPrueba2 = new javax.swing.JCheckBox();
        cbxPrueba1 = new javax.swing.JCheckBox();
        cbxPrueba4 = new javax.swing.JCheckBox();
        cbxPrueba3 = new javax.swing.JCheckBox();
        cbxPrueba5 = new javax.swing.JCheckBox();
        cbxPrueba6 = new javax.swing.JCheckBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jLabel19 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        dateAuto = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dateAcuerdos = new com.toedter.calendar.JDateChooser();
        jLabel97 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        cmbSuspension = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtEfectos = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        txtExpediente = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cmbPromocion = new javax.swing.JComboBox();
        btnMenos = new javax.swing.JButton();
        txtFolioPromocion = new javax.swing.JTextField();
        txtAnioPromocion = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        lblAuto = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Registro de Autos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_Registro_autos.jpg"))); // NOI18N

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
        btnF2.setText("<html><center>F2 - Guardar<br>y/o Actualizar<br>Auto</center></html>");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("<html><center>F6 - Eliminar<br>Auto</center></html>");
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
        jLabel4.setBounds(270, 20, 40, 30);

        txtBuscarFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        jPanel1.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(310, 20, 140, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Año");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(470, 20, 40, 30);

        btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBuscar.setText("Buscar Auto");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar);
        btnBuscar.setBounds(630, 20, 150, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(510, 20, 80, 30);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel2.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Auto");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(10, 210, 40, 30);

        cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbTipo);
        cmbTipo.setBounds(60, 210, 830, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setText("Motivo");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 450, 60, 30);

        txtMotivo.setColumns(20);
        txtMotivo.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtMotivo.setLineWrap(true);
        txtMotivo.setRows(1);
        txtMotivo.setWrapStyleWord(true);
        txtMotivo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMotivoKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtMotivo);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(470, 480, 460, 70);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Folio del Auto");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(20, 30, 100, 30);

        txtFolioAuto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtFolioAuto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtFolioAuto);
        txtFolioAuto.setBounds(130, 30, 110, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Año");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(240, 30, 40, 30);

        txtAnioAuto.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtAnioAuto.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtAnioAuto);
        txtAnioAuto.setBounds(290, 30, 70, 30);

        cbxOrdenar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxOrdenar.setText("¿Ordenar a archivo?");
        jPanel2.add(cbxOrdenar);
        cbxOrdenar.setBounds(240, 260, 160, 30);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pruebas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel3.setLayout(null);

        cbxPrueba2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxPrueba2.setText("Testimoniales");
        jPanel3.add(cbxPrueba2);
        cbxPrueba2.setBounds(10, 40, 120, 20);

        cbxPrueba1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxPrueba1.setText("Documentales");
        jPanel3.add(cbxPrueba1);
        cbxPrueba1.setBounds(10, 20, 120, 20);

        cbxPrueba4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxPrueba4.setText("Informes de Autoridad");
        jPanel3.add(cbxPrueba4);
        cbxPrueba4.setBounds(10, 80, 170, 20);

        cbxPrueba3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxPrueba3.setText("Confesionales");
        jPanel3.add(cbxPrueba3);
        cbxPrueba3.setBounds(10, 60, 120, 20);

        cbxPrueba5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxPrueba5.setText("Inspección");
        jPanel3.add(cbxPrueba5);
        cbxPrueba5.setBounds(10, 100, 100, 20);

        cbxPrueba6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxPrueba6.setText("Pericial");
        jPanel3.add(cbxPrueba6);
        cbxPrueba6.setBounds(10, 120, 80, 20);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(300, 300, 190, 150);

        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
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
        jScrollPane3.setBounds(10, 480, 450, 70);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Observaciones");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(470, 450, 110, 30);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Fechas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel4.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("Emisión del Auto");
        jPanel4.add(jLabel12);
        jLabel12.setBounds(30, 40, 120, 30);

        dateAuto.setDateFormatString("d/MMM/yyyy");
        dateAuto.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(dateAuto);
        dateAuto.setBounds(150, 40, 130, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Lista de Acuerdos");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(20, 80, 130, 30);

        dateAcuerdos.setDateFormatString("d/MMM/yyyy");
        dateAcuerdos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(dateAcuerdos);
        dateAcuerdos.setBounds(150, 80, 130, 30);

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 0, 0));
        jLabel97.setText("*");
        jPanel4.add(jLabel97);
        jLabel97.setBounds(10, 80, 40, 30);

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 0, 0));
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel99.setText("*");
        jPanel4.add(jLabel99);
        jLabel99.setBounds(-10, 40, 40, 30);

        jPanel2.add(jPanel4);
        jPanel4.setBounds(10, 300, 290, 150);

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 0, 0));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("*");
        jPanel2.add(jLabel95);
        jLabel95.setBounds(20, 30, 20, 30);

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 0, 0));
        jLabel96.setText("*");
        jPanel2.add(jLabel96);
        jLabel96.setBounds(10, 210, 40, 30);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Suspensión", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel6.setLayout(null);

        cmbSuspension.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(cmbSuspension);
        cmbSuspension.setBounds(90, 20, 340, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Suspensión");
        jPanel6.add(jLabel18);
        jLabel18.setBounds(0, 20, 80, 30);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("<html><body><DIV align=RIGHT>Efectos de<br>Suspensión</DIV></body></html>");
        jPanel6.add(jLabel20);
        jLabel20.setBounds(0, 60, 80, 40);

        txtEfectos.setColumns(20);
        txtEfectos.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txtEfectos.setLineWrap(true);
        txtEfectos.setRows(1);
        txtEfectos.setWrapStyleWord(true);
        txtEfectos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEfectosKeyPressed(evt);
            }
        });
        jScrollPane4.setViewportView(txtEfectos);

        jPanel6.add(jScrollPane4);
        jScrollPane4.setBounds(90, 60, 340, 80);

        jPanel2.add(jPanel6);
        jPanel6.setBounds(490, 300, 440, 150);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Expediente");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(10, 260, 80, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpedienteKeyPressed(evt);
            }
        });
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(100, 260, 130, 30);

        jButton2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jButton2.setText("+");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);
        jButton2.setBounds(890, 210, 41, 30);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Promociones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel5.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel10.setText("Folio");
        jPanel5.add(jLabel10);
        jLabel10.setBounds(20, 30, 40, 30);

        cmbPromocion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbPromocion.setToolTipText("");
        jPanel5.add(cmbPromocion);
        cmbPromocion.setBounds(20, 70, 840, 30);

        btnMenos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnMenos.setText("-");
        btnMenos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenosActionPerformed(evt);
            }
        });
        jPanel5.add(btnMenos);
        btnMenos.setBounds(860, 70, 40, 30);

        txtFolioPromocion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFolioPromocion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFolioPromocion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFolioPromocionKeyPressed(evt);
            }
        });
        jPanel5.add(txtFolioPromocion);
        txtFolioPromocion.setBounds(60, 30, 90, 30);

        txtAnioPromocion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnioPromocion.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnioPromocion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtAnioPromocionKeyPressed(evt);
            }
        });
        jPanel5.add(txtAnioPromocion);
        txtAnioPromocion.setBounds(210, 30, 80, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText("Año");
        jPanel5.add(jLabel11);
        jLabel11.setBounds(170, 30, 40, 30);

        btnAgregar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAgregar.setText("Buscar y Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jPanel5.add(btnAgregar);
        btnAgregar.setBounds(310, 30, 170, 30);

        jPanel2.add(jPanel5);
        jPanel5.setBounds(10, 80, 920, 120);

        lblAuto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblAuto);
        lblAuto.setBounds(370, 30, 20, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1096, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 940, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnF2)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(376, 376, 376)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 39, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
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

    private void txtMotivoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMotivoKeyPressed
        u.focus(evt, txtMotivo);
    }//GEN-LAST:event_txtMotivoKeyPressed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                validaFolioAuto();
            } catch (ParseException ex) {
                Logger.getLogger(RegistroAutos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            validaFolioAuto();
        } catch (ParseException ex) {
            Logger.getLogger(RegistroAutos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesKeyPressed
        u.focus(evt, txtObservaciones);
    }//GEN-LAST:event_txtObservacionesKeyPressed

    private void txtEfectosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEfectosKeyPressed
        u.focus(evt, txtEfectos);
    }//GEN-LAST:event_txtEfectosKeyPressed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        AutoEntity auto = new AutoEntity("-1","","",false,false);
        AgregarAuto agrega = new AgregarAuto(null,true,auto,sesion);
        agrega.setLocationRelativeTo(null);
        agrega.show();
        if(!auto.getId_tipo_auto().equals("-1")){
            autocomplete.uninstall();
            EventList<AutoEntity> items = new BasicEventList<>();
            ArrayList array = blTipo.getAutos();
            items.add(new AutoEntity());
            for(Object obj : array)
                items.add((AutoEntity)obj);
            autocomplete = AutoCompleteSupport.install(cmbTipo, items);
            autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);
            seleccionarComboTipo(auto);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        validaFolioPromocion();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnMenosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenosActionPerformed
        if(cmbPromocion.getItemCount()>0){
            cmbPromocion.removeItemAt(cmbPromocion.getSelectedIndex());
        }
    }//GEN-LAST:event_btnMenosActionPerformed

    private void txtFolioPromocionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolioPromocionKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(txtFolioPromocion.getText().length()>0){
                validaFolioPromocion();
            }
        }
    }//GEN-LAST:event_txtFolioPromocionKeyPressed

    private void txtAnioPromocionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtAnioPromocionKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(txtFolioPromocion.getText().length()>0){
                validaFolioPromocion();
            }
        }
    }//GEN-LAST:event_txtAnioPromocionKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF6;
    private javax.swing.JButton btnMenos;
    private javax.swing.JCheckBox cbxOrdenar;
    private javax.swing.JCheckBox cbxPrueba1;
    private javax.swing.JCheckBox cbxPrueba2;
    private javax.swing.JCheckBox cbxPrueba3;
    private javax.swing.JCheckBox cbxPrueba4;
    private javax.swing.JCheckBox cbxPrueba5;
    private javax.swing.JCheckBox cbxPrueba6;
    private javax.swing.JComboBox cmbPromocion;
    private javax.swing.JComboBox cmbSuspension;
    private javax.swing.JComboBox cmbTipo;
    private com.toedter.calendar.JDateChooser dateAcuerdos;
    private com.toedter.calendar.JDateChooser dateAuto;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JTextField txtAnioAuto;
    private javax.swing.JTextField txtAnioPromocion;
    private javax.swing.JTextField txtBuscarAnio;
    private javax.swing.JTextField txtBuscarFolio;
    private javax.swing.JTextArea txtEfectos;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFolioAuto;
    private javax.swing.JTextField txtFolioPromocion;
    private javax.swing.JTextArea txtMotivo;
    private javax.swing.JTextArea txtObservaciones;
    // End of variables declaration//GEN-END:variables

    
    
}
