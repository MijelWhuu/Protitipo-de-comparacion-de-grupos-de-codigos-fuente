package interfaz.promociones;

import baseGeneral.ErrorEntity;
import baseSistema.Utilidades;
import bl.utilidades.CalendarioOficialBl;
import bl.catalogos.DestinosBl;
import bl.catalogos.TipoPromocionesBl;
import bl.promociones.PromocionesDeleteBl;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
public class PromocionesDelete extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    PromocionesDeleteBl bl = new PromocionesDeleteBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    TipoPromocionesBl blTipo = new TipoPromocionesBl();
    DestinosBl blDestino = new DestinosBl();
   
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form Destinos
     */
    public PromocionesDelete(){
        initComponents();
    }
    
    public PromocionesDelete(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblBuscar.setVisible(false);
        mapeoTeclas();
        cargarComboAnio();
    }
    
    private void cargarComboAnio(){
        cmbAnio.removeAllItems();
        cmbAnio.addItem("[Elija un año]");
        cmbAnio.addItem(""+cal.get(Calendar.YEAR));
        ArrayList array = bl.getAnios();
        if(!array.isEmpty()){
            for (Object array1 : array){
                if(!array1.toString().equals(""+cal.get(Calendar.YEAR))){
                    cmbAnio.addItem(array1.toString());
                }
            }
        }
    }
    
    private Boolean validar(){
        if(lblBuscar.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Folio para eliminar", "Alerta", 2);
            txtFolioBuscar.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private void validaFolio(){
        if(txtFolioBuscar.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio a buscar", "Alerta", 2);
            txtFolioBuscar.requestFocus();
        }else{
            Integer folio = new Integer(txtFolioBuscar.getText());
            Integer anio = cal.get(Calendar.YEAR);
            if(cmbAnio.getItemCount()>2 && cmbAnio.getSelectedIndex()>1){
                anio = new Integer(cmbAnio.getSelectedItem().toString());
            }
            if(bl.existeFolio(folio, anio)){
                cargaFolio(folio,anio);
                txtFolioBuscar.setText("");
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio no existe", "Alerta", 2);
                txtFolioBuscar.requestFocus();
            }
        }
    }
    
    private void cargaFolio(Integer folio, Integer anio){
        PromocionEntity promo = bl.getPromocion(folio, anio);
        lblBuscar.setText(promo.getId_promocion());
        txtFolio.setText(promo.getFolio().toString());
        txtAnio.setText(promo.getAnio().toString());
        txtExpediente.setText(promo.getExpe().getExpediente());
        txtTipo.setText(promo.getTipoPromocion().getNombre());
        txtDestino.setText(promo.getDestino().getNombre());
        txtFecha.setText(promo.getFechaR());
        txtHora.setText(promo.getHoraR());
        txtAnexoOriginales.setText((promo.getaOriginales()==0)?"":promo.getaOriginales().toString());
        txtAnexoCopias.setText((promo.getaCopias()==0)?"":promo.getaCopias().toString());
        txtAcredita.setText((promo.getAcreditacion())?"SI":"NO");
        if(promo.getAcreditacion())
            txtHojas.setText(promo.getHojas().toString());
        txtObservaciones.setText(promo.getObservaciones());
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
        u.cleanComponent(lblBuscar,txtFolio,txtAnio,txtExpediente,txtTipo,txtDestino,txtFecha,txtHora,txtAnexoOriginales,txtAnexoCopias,txtAcredita,txtHojas,txtObservaciones);
        cargarComboAnio();
        txtFolioBuscar.requestFocus();
    }
    
    private void botonF2(){}
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){
        if(validar()){
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar la Promoción seleccionada?", "Alerta", 0)==0){
                ErrorEntity error = bl.deletePromocion(lblBuscar.getText());
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
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtTipo = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtAnexoOriginales = new javax.swing.JTextField();
        txtAnexoCopias = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtAcredita = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtHojas = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel96 = new javax.swing.JLabel();
        txtFolioBuscar = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        cmbAnio = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        lblBuscar = new javax.swing.JLabel();
        lblBuscarAnio = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Eliminar promoción");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_eliminar_promociones.jpg"))); // NOI18N

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("<html><body><center>F6 - Eliminar<br>Promoción</center></body></html>");
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

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel94.setText("Folio de la promoción");
        jPanel2.add(jLabel94);
        jLabel94.setBounds(40, 30, 140, 30);

        txtFolio.setEditable(false);
        txtFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFolio.setFocusable(false);
        jPanel2.add(txtFolio);
        txtFolio.setBounds(190, 30, 170, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Año de la Promoción");
        jPanel2.add(jLabel27);
        jLabel27.setBounds(370, 30, 140, 30);

        txtAnio.setEditable(false);
        txtAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnio.setFocusable(false);
        jPanel2.add(txtAnio);
        txtAnio.setBounds(520, 30, 100, 30);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("No. de Expediente");
        jPanel2.add(jLabel28);
        jLabel28.setBounds(40, 70, 140, 30);

        txtExpediente.setEditable(false);
        txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtExpediente.setFocusable(false);
        jPanel2.add(txtExpediente);
        txtExpediente.setBounds(190, 70, 170, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Tipo de promoción");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(50, 110, 130, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Destino");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(120, 150, 60, 30);

        txtTipo.setEditable(false);
        txtTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtTipo.setFocusable(false);
        jPanel2.add(txtTipo);
        txtTipo.setBounds(190, 110, 590, 30);

        txtDestino.setEditable(false);
        txtDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtDestino.setFocusable(false);
        jPanel2.add(txtDestino);
        txtDestino.setBounds(190, 150, 340, 30);

        txtFecha.setEditable(false);
        txtFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFecha.setFocusable(false);
        jPanel2.add(txtFecha);
        txtFecha.setBounds(190, 190, 170, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Fecha de Recepción");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(40, 190, 140, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Hora de Recepción");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(50, 230, 130, 30);

        txtHora.setEditable(false);
        txtHora.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHora.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHora.setFocusable(false);
        jPanel2.add(txtHora);
        txtHora.setBounds(190, 230, 170, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("No. de Anexos Originales");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 270, 170, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("No. de Anexos Copias");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(10, 310, 170, 30);

        txtAnexoOriginales.setEditable(false);
        txtAnexoOriginales.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnexoOriginales.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnexoOriginales.setFocusable(false);
        jPanel2.add(txtAnexoOriginales);
        txtAnexoOriginales.setBounds(190, 270, 90, 30);

        txtAnexoCopias.setEditable(false);
        txtAnexoCopias.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAnexoCopias.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAnexoCopias.setFocusable(false);
        jPanel2.add(txtAnexoCopias);
        txtAnexoCopias.setBounds(190, 310, 90, 30);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Acredita Personalidad");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(20, 350, 160, 30);

        txtAcredita.setEditable(false);
        txtAcredita.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAcredita.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAcredita.setFocusable(false);
        jPanel2.add(txtAcredita);
        txtAcredita.setBounds(190, 350, 50, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("No. de Hojas");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(250, 350, 90, 30);

        txtHojas.setEditable(false);
        txtHojas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtHojas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtHojas.setFocusable(false);
        jPanel2.add(txtHojas);
        txtHojas.setBounds(350, 350, 90, 30);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Observaciones");
        jPanel2.add(jLabel21);
        jLabel21.setBounds(20, 390, 160, 30);

        txtObservaciones.setEditable(false);
        txtObservaciones.setColumns(20);
        txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setRows(2);
        txtObservaciones.setWrapStyleWord(true);
        txtObservaciones.setFocusable(false);
        jScrollPane3.setViewportView(txtObservaciones);

        jPanel2.add(jScrollPane3);
        jScrollPane3.setBounds(190, 390, 590, 150);

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

        cmbAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbAnioKeyPressed(evt);
            }
        });
        jPanel4.add(cmbAnio);
        cmbAnio.setBounds(510, 20, 120, 30);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel4.add(jButton1);
        jButton1.setBounds(650, 20, 110, 30);
        jPanel4.add(lblBuscar);
        lblBuscar.setBounds(90, 20, 60, 30);
        jPanel4.add(lblBuscarAnio);
        lblBuscarAnio.setBounds(20, 20, 60, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(490, 490, 490)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtFolioBuscar.requestFocus();
    }//GEN-LAST:event_formComponentShown

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        validaFolio();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtFolioBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFolioBuscarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            validaFolio();
        }
    }//GEN-LAST:event_txtFolioBuscarKeyPressed

    private void cmbAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbAnioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            validaFolio();
        }
    }//GEN-LAST:event_cmbAnioKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cmbAnio;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblBuscarAnio;
    private javax.swing.JTextField txtAcredita;
    private javax.swing.JTextField txtAnexoCopias;
    private javax.swing.JTextField txtAnexoOriginales;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtExpediente;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtFolio;
    private javax.swing.JTextField txtFolioBuscar;
    private javax.swing.JTextField txtHojas;
    private javax.swing.JTextField txtHora;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextField txtTipo;
    // End of variables declaration//GEN-END:variables
    
}
