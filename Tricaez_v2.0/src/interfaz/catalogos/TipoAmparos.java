package interfaz.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.TipoTramitesBl;
import entitys.SesionEntity;
import entitys.TipoTramiteEntity;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class TipoAmparos extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    TipoTramitesBl bl = new TipoTramitesBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form Destinos
     */
    public TipoAmparos(){
        initComponents();
    }
    
    public TipoAmparos(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatosCampos();
        mapeoTeclas();
        inicializaCombo();
    }
    
    private void formatosCampos(){
        txtNombreNew.setDocument(new JTextMascara(290,true,"*"));
        txtPersonaNew.setDocument(new JTextMascara(90,true,"*"));
        txtAutoridadNew.setDocument(new JTextMascara(90,true,"*"));
        txtDocumentoNew.setDocument(new JTextMascara(90,true,"*"));
        txtObservacionesNew.setDocument(new JTextMascaraTextArea());
        txtNombreUpdate.setDocument(new JTextMascara(290,true,"*"));
        txtPersonaUpdate.setDocument(new JTextMascara(90,true,"*"));
        txtAutoridadUpdate.setDocument(new JTextMascara(90,true,"*"));
        txtDocumentoUpdate.setDocument(new JTextMascara(90,true,"*"));
        txtObservacionesUpdate.setDocument(new JTextMascaraTextArea());
    }
    
    private void inicializaCombo(){
        u.inicializaCombo(cmbDestino, new TipoTramiteEntity(), bl.getTipoAmparos());
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones Botones">
    private void botonEsc(){
        if(jTabbedPane1.getSelectedIndex()==0){
            txtNombreNew.setText("");
            txtPersonaNew.setText("");
            txtAutoridadNew.setText("");
            txtDocumentoNew.setText("");
            rbtnSiNew.setSelected(true);
            cbxDefaultNew.setSelected(false);
            txtObservacionesNew.setText("");
            txtNombreNew.requestFocus();
        }else{
            cmbDestino.setSelectedIndex(0);
        }
        
    }
    
    private void botonF2(){
        if(jTabbedPane1.getSelectedIndex()==0){
            if(txtNombreNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreNew.requestFocus();
            }else if(txtPersonaNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Persona es obligatorio", "Alerta", 2);
                txtPersonaNew.requestFocus();
            }else if(txtAutoridadNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Autoridad es obligatorio", "Alerta", 2);
                txtAutoridadNew.requestFocus();
            }else if(txtDocumentoNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Documento es obligatorio", "Alerta", 2);
                txtDocumentoNew.requestFocus();
            }else{
                TipoTramiteEntity tipo = new TipoTramiteEntity();
                tipo.setNombre(txtNombreNew.getText());
                tipo.setPersona(txtPersonaNew.getText());
                tipo.setAutoridad(txtAutoridadNew.getText());
                tipo.setDocumento(txtDocumentoNew.getText());
                tipo.setSolicitaPartes(rbtnSiNew.isSelected());
                tipo.setObservaciones(txtObservacionesNew.getText());
                tipo.setSeleccionar(cbxDefaultNew.isSelected());
                ErrorEntity error = bl.saveTipoAmparo(tipo, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    inicializaCombo();
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF4(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbDestino.getItemCount()>1){
            if(cmbDestino.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún elemento", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else if(txtNombreUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else if(txtAutoridadUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Autoridad es obligatorio", "Alerta", 2);
                txtAutoridadUpdate.requestFocus();
            }else if(txtDocumentoUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Documento es obligatorio", "Alerta", 2);
                txtDocumentoUpdate.requestFocus();
            }else{
                TipoTramiteEntity tipo = (TipoTramiteEntity)cmbDestino.getSelectedItem();
                tipo.setNombre(txtNombreUpdate.getText());
                tipo.setPersona(txtPersonaUpdate.getText());
                tipo.setAutoridad(txtAutoridadUpdate.getText());
                tipo.setDocumento(txtDocumentoUpdate.getText());
                tipo.setSolicitaPartes(rbtnSiUpdate.isSelected());
                tipo.setObservaciones(txtObservacionesUpdate.getText());
                tipo.setSeleccionar(cbxDefaultUpdate.isSelected());
                ErrorEntity error = bl.updateTipoAmparo(tipo, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                    inicializaCombo();
                    cmbDestino.requestFocus();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF6(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbDestino.getItemCount()>1){
            if(cmbDestino.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún elemento", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else{
                if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar el elemento seleccionado?", "Alerta", 0)==0){
                    ErrorEntity error = bl.deleteTipoAmparo(((TipoTramiteEntity)cmbDestino.getSelectedItem()).getId_tipo_amparo());
                    if(!error.getError()){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                        inicializaCombo();
                        cmbDestino.requestFocus();
                    }else{
                        if(error.getNumError().equals(1451)){
                            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar ya que el elemento seleccionado está siendo utilizado por otra información", "Aviso", 1);
                            cmbDestino.requestFocus();
                        }else{
                            javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                        }
                    }
                }
            }
        }
    }
    
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
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0),"accion_F6"); mapaAccion.put("accion_F6",Accion_F6());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0),"accion_F12"); mapaAccion.put("accion_F12",Accion_F12());
    }
    public AbstractAction Accion_Esc(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF12();}};}
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtNombreNew = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservacionesNew = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txtPersonaNew = new javax.swing.JTextField();
        txtAutoridadNew = new javax.swing.JTextField();
        txtDocumentoNew = new javax.swing.JTextField();
        rbtnSiNew = new javax.swing.JRadioButton();
        rbtnNoNew = new javax.swing.JRadioButton();
        cbxDefaultNew = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox();
        txtNombreUpdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacionesUpdate = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtAutoridadUpdate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtPersonaUpdate = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtDocumentoUpdate = new javax.swing.JTextField();
        rbtnSiUpdate = new javax.swing.JRadioButton();
        rbtnNoUpdate = new javax.swing.JRadioButton();
        cbxDefaultUpdate = new javax.swing.JCheckBox();
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Catálogo - Tipo de Ámparos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cat_tipo_amparo.jpg"))); // NOI18N

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Nombre del Tipo de Ámparo");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 30, 190, 30);

        txtNombreNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNombreNew.setNextFocusableComponent(txtPersonaNew);
        jPanel1.add(txtNombreNew);
        txtNombreNew.setBounds(10, 60, 250, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Observaciones");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 250, 134, 30);

        txtObservacionesNew.setColumns(20);
        txtObservacionesNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservacionesNew.setLineWrap(true);
        txtObservacionesNew.setRows(4);
        txtObservacionesNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesNewKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtObservacionesNew);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 280, 510, 100);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("*");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 30, 20, 30);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("*");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(270, 30, 30, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("Solicitante");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(280, 30, 70, 30);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("*");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(10, 90, 30, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setText("Otorgante");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(20, 90, 120, 30);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 0, 0));
        jLabel23.setText("*");
        jPanel1.add(jLabel23);
        jLabel23.setBounds(270, 90, 30, 30);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Tipo de Documento");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(280, 90, 140, 30);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 0, 0));
        jLabel25.setText("*");
        jPanel1.add(jLabel25);
        jLabel25.setBounds(10, 160, 30, 30);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Solicita Partes al iniciar trámites en Secretaría de Acuerdos");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(20, 160, 410, 30);

        txtPersonaNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtPersonaNew.setNextFocusableComponent(txtAutoridadNew);
        jPanel1.add(txtPersonaNew);
        txtPersonaNew.setBounds(270, 60, 250, 30);

        txtAutoridadNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtAutoridadNew.setNextFocusableComponent(txtDocumentoNew);
        jPanel1.add(txtAutoridadNew);
        txtAutoridadNew.setBounds(10, 120, 250, 30);

        txtDocumentoNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtDocumentoNew);
        txtDocumentoNew.setBounds(270, 120, 250, 30);

        buttonGroup2.add(rbtnSiNew);
        rbtnSiNew.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnSiNew.setSelected(true);
        rbtnSiNew.setText("Si");
        jPanel1.add(rbtnSiNew);
        rbtnSiNew.setBounds(420, 160, 50, 30);

        buttonGroup2.add(rbtnNoNew);
        rbtnNoNew.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnNoNew.setText("No");
        jPanel1.add(rbtnNoNew);
        rbtnNoNew.setBounds(470, 160, 50, 30);

        cbxDefaultNew.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxDefaultNew.setText("Seleccionar siempre por Default");
        jPanel1.add(cbxDefaultNew);
        cbxDefaultNew.setBounds(10, 210, 260, 25);

        jTabbedPane1.addTab("Nuevo", jPanel1);

        jPanel2.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Tipo de Ámparos");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(16, 30, 120, 30);

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbDestinoActionPerformed(evt);
            }
        });
        jPanel3.add(cmbDestino);
        cmbDestino.setBounds(140, 30, 340, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 11, 510, 80);

        txtNombreUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNombreUpdate);
        txtNombreUpdate.setBounds(10, 120, 250, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Observaciones");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 300, 134, 30);

        txtObservacionesUpdate.setColumns(20);
        txtObservacionesUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservacionesUpdate.setRows(4);
        txtObservacionesUpdate.setNextFocusableComponent(jTabbedPane1);
        txtObservacionesUpdate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesUpdateKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(txtObservacionesUpdate);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(10, 330, 500, 80);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Nombre del Tipo de Ámparo");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(20, 90, 230, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("Solicitante");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(280, 90, 80, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setText("Otorgante");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(20, 150, 120, 30);

        txtAutoridadUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtAutoridadUpdate);
        txtAutoridadUpdate.setBounds(10, 180, 250, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("*");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 90, 30, 30);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 0, 0));
        jLabel14.setText("*");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(270, 90, 30, 30);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 0, 0));
        jLabel16.setText("*");
        jPanel2.add(jLabel16);
        jLabel16.setBounds(10, 150, 30, 30);

        txtPersonaUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtPersonaUpdate);
        txtPersonaUpdate.setBounds(270, 120, 250, 30);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 0, 0));
        jLabel15.setText("*");
        jPanel2.add(jLabel15);
        jLabel15.setBounds(270, 150, 30, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 0, 0));
        jLabel17.setText("*");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(10, 220, 30, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Tipo de Documento");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(280, 150, 170, 30);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Solicita Partes al iniciar trámites en Secretaría de Acuerdos");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(20, 220, 400, 30);

        txtDocumentoUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtDocumentoUpdate);
        txtDocumentoUpdate.setBounds(270, 180, 250, 30);

        buttonGroup1.add(rbtnSiUpdate);
        rbtnSiUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnSiUpdate.setSelected(true);
        rbtnSiUpdate.setText("Si");
        jPanel2.add(rbtnSiUpdate);
        rbtnSiUpdate.setBounds(420, 220, 50, 30);

        buttonGroup1.add(rbtnNoUpdate);
        rbtnNoUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        rbtnNoUpdate.setText("No");
        jPanel2.add(rbtnNoUpdate);
        rbtnNoUpdate.setBounds(470, 220, 50, 30);

        cbxDefaultUpdate.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxDefaultUpdate.setText("Seleccionar siempre por Default");
        jPanel2.add(cbxDefaultUpdate);
        cbxDefaultUpdate.setBounds(10, 260, 260, 25);

        jTabbedPane1.addTab("Modificar/Eliminar", jPanel2);

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
        btnF2.setText("F2 - Guardar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnF4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF4.setText("F4 - Modificar");
        btnF4.setFocusable(false);
        btnF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF4ActionPerformed(evt);
            }
        });

        btnF6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF6.setText("F6 - Eliminar");
        btnF6.setFocusable(false);
        btnF6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 215, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtObservacionesNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesNewKeyPressed
        u.focus(evt, txtObservacionesNew);
    }//GEN-LAST:event_txtObservacionesNewKeyPressed

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF4ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF4ActionPerformed

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void cmbDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbDestinoActionPerformed
        if(cmbDestino.getItemCount()>0){
            txtNombreUpdate.setText("");
            txtPersonaUpdate.setText("");
            txtAutoridadUpdate.setText("");
            txtDocumentoUpdate.setText("");
            txtObservacionesUpdate.setText("");
            if(cmbDestino.getSelectedIndex()==0){
                u.enabledComponet(false, txtNombreUpdate,txtPersonaUpdate,txtAutoridadUpdate,txtDocumentoUpdate,rbtnSiUpdate,rbtnNoUpdate,cbxDefaultUpdate,txtObservacionesUpdate);
                cbxDefaultUpdate.setSelected(false);
            }else{
                u.enabledComponet(true, txtNombreUpdate,txtPersonaUpdate,txtAutoridadUpdate,txtDocumentoUpdate,rbtnSiUpdate,rbtnNoUpdate,cbxDefaultUpdate,txtObservacionesUpdate);
                TipoTramiteEntity tipo = (TipoTramiteEntity)cmbDestino.getSelectedItem();
                txtNombreUpdate.setText(tipo.getNombre());
                txtPersonaUpdate.setText(tipo.getPersona());
                txtAutoridadUpdate.setText(tipo.getAutoridad());
                txtDocumentoUpdate.setText(tipo.getDocumento());
                u.selectedJRadioButton(tipo.getSolicitaPartes(), rbtnSiUpdate, rbtnNoUpdate);
                cbxDefaultUpdate.setSelected(tipo.getSeleccionar());
                txtObservacionesUpdate.setText(tipo.getObservaciones());
            }
        }
    }//GEN-LAST:event_cmbDestinoActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        txtNombreNew.requestFocus();
    }//GEN-LAST:event_formComponentShown

    private void txtObservacionesUpdateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesUpdateKeyPressed
        u.focus(evt, txtObservacionesUpdate);
    }//GEN-LAST:event_txtObservacionesUpdateKeyPressed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if(jTabbedPane1.getSelectedIndex()==0){
            btnF2.setEnabled(true);
            btnF4.setEnabled(false);
            btnF6.setEnabled(false);
        }else{
            btnF2.setEnabled(false);
            btnF4.setEnabled(true);
            btnF6.setEnabled(true);
        }
    }//GEN-LAST:event_jTabbedPane1StateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JCheckBox cbxDefaultNew;
    private javax.swing.JCheckBox cbxDefaultUpdate;
    private javax.swing.JComboBox cmbDestino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rbtnNoNew;
    private javax.swing.JRadioButton rbtnNoUpdate;
    private javax.swing.JRadioButton rbtnSiNew;
    private javax.swing.JRadioButton rbtnSiUpdate;
    private javax.swing.JTextField txtAutoridadNew;
    private javax.swing.JTextField txtAutoridadUpdate;
    private javax.swing.JTextField txtDocumentoNew;
    private javax.swing.JTextField txtDocumentoUpdate;
    private javax.swing.JTextField txtNombreNew;
    private javax.swing.JTextField txtNombreUpdate;
    private javax.swing.JTextArea txtObservacionesNew;
    private javax.swing.JTextArea txtObservacionesUpdate;
    private javax.swing.JTextField txtPersonaNew;
    private javax.swing.JTextField txtPersonaUpdate;
    // End of variables declaration//GEN-END:variables
}
