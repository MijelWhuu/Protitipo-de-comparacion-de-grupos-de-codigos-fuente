package interfaz.promociones;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.utilidades.CalendarioOficialBl;
import bl.catalogos.DestinosBl;
import bl.catalogos.TipoPromocionesBl;
import bl.promociones.PromocionesBl;
import bl.promociones.PromocionesDeleteBl;
import bl.promociones.PromocionesUpdateBl;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entitys.DestinoEntity;
import entitys.ExpedienteEntity;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import entitys.TipoPromocionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class PromocionesUpdate extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    PromocionesBl blNew = new PromocionesBl();
    PromocionesUpdateBl bl = new PromocionesUpdateBl();
    PromocionesDeleteBl blDel = new PromocionesDeleteBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    TipoPromocionesBl blTipo = new TipoPromocionesBl();
    DestinosBl blDestino = new DestinosBl();
   
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public PromocionesUpdate(){
        initComponents();
    }
    
    public PromocionesUpdate(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblPromocion.setVisible(false);
        formatearCampos();
        obtenerFechaTiempo();
        cbxAcreditacion();
        mapeoTeclas();
        inicializaCombos();
        inicializarCampos();
    }
    
    private void formatearCampos(){
        txtFolioBuscar.setDocument(new JTextMascara(7,true,"numerico"));
        txtAnioBuscar.setDocument(new JTextMascara(4,true,"numerico"));
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
        EventList<TipoPromocionEntity> items = new BasicEventList<>();
        ArrayList array = blTipo.getTipoPromociones();
        items.add(new TipoPromocionEntity());
        for(Object obj : array)
            items.add((TipoPromocionEntity)obj);
        AutoCompleteSupport autocomplete = AutoCompleteSupport.install(cmbTipo, items);
        autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);
        cmbTipo.setSelectedIndex(0);
        
        u.inicializaCombo(cmbDestino, new DestinoEntity(), blDestino.getDestinos());
    }
    
    private void inicializarCampos(){
        txtAnio.setText(""+cal.get(Calendar.YEAR));
        txtFolio.setText(blNew.getFolio(new Integer(txtAnio.getText())).toString());
    }
    
    private Boolean verificarFolioPromocion(){
        Integer folio = new Integer(txtFolio.getText());
        Integer anio = cal.get(Calendar.YEAR);
        if(txtAnio.getText().length()!=0){
            Integer num = u.verificaInteger(txtAnio.getText());
            if(num!=null)
                anio = num;
        }
        if(lblPromocion.getText().length()==0){
            return blNew.existeFolio(folio, anio);
        }else{
            return blNew.existeFolio(folio, anio, lblPromocion.getText());
        }
    }
    
    private Boolean validar(){
        if(txtFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio de la promoción", "Alerta", 2);
            txtFolio.requestFocus();
        }else if(verificarFolioPromocion()){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio ya existe", "Alerta", 2);
            txtFolio.requestFocus();
        }else if(txtExpediente.getText().length()!=0 && !blNew.existeExpediente(txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El No. de Expediente no existe", "Alerta", 2);
            txtExpediente.requestFocus();
        }else if(cmbTipo.getItemCount()>0 && cmbTipo.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el Tipo de Promoción", "Alerta", 2);
            cmbTipo.requestFocus();
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
    
    private void validaFolio() throws ParseException{
        u.cleanComponent(lblPromocion,txtFolio,txtAnio,txtExpediente,cmbDestino,dateFecha,cmbHoras,cmbMinutos,txtAnexoOriginales,
                txtAnexoCopias,txtTotal,txtObservaciones,cbxAcreditacion,txtHojas);
        
        if(txtFolioBuscar.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio a buscar", "Alerta", 2);
            txtFolioBuscar.requestFocus();
        }else{
            Integer folio = new Integer(txtFolioBuscar.getText());
            Integer anio = cal.get(Calendar.YEAR);
            
            if(txtAnioBuscar.getText().length()!=0){
                Integer num = u.verificaInteger(txtAnioBuscar.getText());
                if(num!=null)
                    anio = num;
            }
            if(bl.existeFolio(folio, anio)){
                cargaFolio(folio,anio);
                txtFolioBuscar.setText("");
                txtAnioBuscar.setText("");
                txtFolio.requestFocus();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio no existe", "Alerta", 2);
                txtFolioBuscar.requestFocus();
            }
        }
    }
    
    private void seleccionarComboTipo(TipoPromocionEntity tipo){
        for(int i=0; i<cmbTipo.getItemCount(); i++){
            TipoPromocionEntity promo = (TipoPromocionEntity)cmbTipo.getItemAt(i);
            if(promo.equals(tipo)){
                cmbTipo.setSelectedIndex(i);
                i=cmbTipo.getItemCount()+5;
            }
        }
    }
    
    private void cargaFolio(Integer folio, Integer anio) throws ParseException{
        PromocionEntity promo = bl.getPromocion(folio, anio);
        lblPromocion.setText(promo.getId_promocion());
        txtFolio.setText(promo.getFolio().toString());
        txtAnio.setText(promo.getAnio().toString());
        if(promo.getExpe()!=null)
            txtExpediente.setText(promo.getExpe().getExpediente());
        seleccionarComboTipo(promo.getTipoPromocion());
        cmbDestino.setSelectedItem(promo.getDestino());
        
        Date date1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(promo.getFechaR().toString());
        dateFecha.setDate(date1);
        
        //dateFecha.setDate(promo.getFechaR());
        String [] cadHora = promo.getHoraR().split(":");
        cmbHoras.setSelectedIndex(new Integer(cadHora[0]));
        cmbMinutos.setSelectedIndex(new Integer(cadHora[1]));
        txtAnexoOriginales.setText((promo.getaOriginales()==0)?"":promo.getaOriginales().toString());
        txtAnexoCopias.setText((promo.getaCopias()==0)?"":promo.getaCopias().toString());
        Integer total = promo.getaOriginales()+promo.getaCopias();
        txtTotal.setText((total==0)?"":total.toString());
        cbxAcreditacion.setSelected(promo.getAcreditacion());
        cbxAcreditacion();
        if(promo.getAcreditacion())
            txtHojas.setText(promo.getHojas().toString());
        txtObservaciones.setText(promo.getObservaciones());
    }
    
    private PromocionEntity obtenerDatos(){
        PromocionEntity promo = new PromocionEntity();
        promo.setId_promocion("");
        if(lblPromocion.getText().length()>0)
            promo.setId_promocion(lblPromocion.getText());
        promo.setFolio(new Integer(txtFolio.getText()));
        Integer anio = cal.get(Calendar.YEAR);   
        if(txtAnio.getText().length()!=0){
            Integer num = u.verificaInteger(txtAnio.getText());
            if(num!=null)
                anio = num;
        }
        promo.setAnio(anio);
        
        /*----------------------------------------------------------------------
                         CÓDIGO DE MARCO
        ----------------------------------------------------------------------*/
        /*promo.setExpe(null);
        if(txtExpediente.getText().length()!=0 
            promo.setExpe(new ExpedienteEntity(txtExpediente.getText()));*/
        
        promo.setExpediente("");
        if(txtExpediente.getText().length()!=0)
            promo.setExpediente(txtExpediente.
                    getText());
        promo.setTipoPromocion((TipoPromocionEntity)cmbTipo.getSelectedItem());
        promo.setDestino(null);
        if(cmbDestino.getSelectedIndex()>0)
            promo.setDestino((DestinoEntity)cmbDestino.getSelectedItem());
        promo.setFechaR(dateFecha.getDate().toString());
        
        /*----------------------------------------------------------------------
                     CODIGO DE MARCO
        -----------------------------------------------------------------------*/
        //promo.setFechaR(dateFecha.getDate());
        
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
        u.cleanComponent(lblPromocion,txtFolioBuscar,txtAnioBuscar,txtFolio,txtAnio,txtExpediente,cmbDestino,dateFecha,cmbHoras,cmbMinutos,
                txtAnexoOriginales,txtAnexoCopias,txtTotal,txtObservaciones,cbxAcreditacion,txtHojas,cmbTipo);
        obtenerFechaTiempo();
        cbxAcreditacion();
        mapeoTeclas();
        u.inicializaCombo(cmbDestino, new DestinoEntity(), blDestino.getDestinos());
        inicializarCampos();
        txtExpediente.requestFocus();
    }
    
    private void botonF2(){}
    
    private void botonF4(){
        if(validar()){
            if(lblPromocion.getText().length()==0){
                ErrorEntity error = blNew.savePromocion(obtenerDatos(), sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }else{
                ErrorEntity error = bl.updatePromocion(obtenerDatos(), sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF5(){}
    
    private void botonF6(){
        if(lblPromocion.getText().length()>0){
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar la Promoción seleccionada?", "Alerta", 0)==0){
                ErrorEntity error = blDel.deletePromocion(lblPromocion.getText());
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    if(error.getNumError().equals(1451)){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar la Promoción seleccionada ya que está siendo utilizado por otra información", "Aviso", 1);
                        txtFolioBuscar.requestFocus();
                    }else{
                        javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                    }
                }
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
        btnF2 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
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
        cmbDestino = new javax.swing.JComboBox();
        lblPromocion = new javax.swing.JLabel();
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
        jPanel4 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        txtFolioBuscar = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        btnBuscar = new javax.swing.JButton();
        txtAnioBuscar = new javax.swing.JTextField();
        btnF3 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Registro de promociones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_modificar_promociones.jpg"))); // NOI18N

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("<html><body><center>F4 - Guardar<br>y/o Actualizar<br>Promoción</center></body></html>");
        btnF2.setActionCommand("<html><body><center>F4 - Guardar y/o<br>Actualizar<br>Promoción</center></body></html>");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
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

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel94.setText("Folio");
        jPanel2.add(jLabel94);
        jLabel94.setBounds(100, 30, 40, 30);

        txtFolio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtFolio);
        txtFolio.setBounds(150, 30, 130, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Año");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(110, 70, 30, 30);

        txtAnio.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(txtAnio);
        txtAnio.setBounds(150, 70, 80, 30);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("No. de Expediente");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(0, 110, 140, 30);

        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtExpedienteKeyPressed(evt);
            }
        });
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(150, 110, 170, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("*");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(10, 150, 20, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Tipo de promoción");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(10, 150, 130, 30);

        cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbTipo);
        cmbTipo.setBounds(150, 150, 630, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Destino");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(80, 190, 60, 30);

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(255, 0, 0));
        jLabel95.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel95.setText("*");
        jPanel2.add(jLabel95);
        jLabel95.setBounds(90, 30, 30, 30);

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbDestino);
        cmbDestino.setBounds(150, 190, 320, 30);

        lblPromocion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.add(lblPromocion);
        lblPromocion.setBounds(330, 30, 60, 30);

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
        jLabel21.setBounds(10, 0, 160, 35);

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
        jScrollPane3.setBounds(10, 30, 780, 90);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda de Promociones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel4.setLayout(null);

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel96.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel96.setText("Año");
        jPanel4.add(jLabel96);
        jLabel96.setBounds(460, 20, 40, 30);

        txtFolioBuscar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFolioBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFolioBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFolioBuscarKeyPressed(evt);
            }
        });
        jPanel4.add(txtFolioBuscar);
        txtFolioBuscar.setBounds(320, 20, 130, 30);

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel97.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel97.setText("Folio de la promoción");
        jPanel4.add(jLabel97);
        jLabel97.setBounds(170, 20, 140, 30);

        btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel4.add(btnBuscar);
        btnBuscar.setBounds(650, 20, 110, 30);

        txtAnioBuscar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnioBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel4.add(txtAnioBuscar);
        txtAnioBuscar.setBounds(510, 20, 90, 30);

        btnF3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF3.setText("<html><body><center>F6 - Eliminar<br>Promoción</center></body></html>");
        btnF3.setFocusable(false);
        btnF3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF3ActionPerformed(evt);
            }
        });

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnF3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnF2)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF4();
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

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        try {
            validaFolio();
        } catch (ParseException ex) {
            Logger.getLogger(PromocionesUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtFolioBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolioBuscarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                validaFolio();
            } catch (ParseException ex) {
                Logger.getLogger(PromocionesUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_txtFolioBuscarKeyPressed

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

    private void btnF3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF3ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF3;
    private javax.swing.JButton btnSalir;
    private javax.swing.JCheckBox cbxAcreditacion;
    private javax.swing.JComboBox cmbDestino;
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
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblAsterisco;
    private javax.swing.JLabel lblPromocion;
    private javax.swing.JTextField txtAnexoCopias;
    private javax.swing.JTextField txtAnexoOriginales;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtAnioBuscar;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtFolioBuscar;
    private javax.swing.JTextField txtHojas;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
    
}
