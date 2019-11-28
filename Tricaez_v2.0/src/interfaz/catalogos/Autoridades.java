package interfaz.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.AutoridadBl;
import bl.catalogos.MunicipiosBl;
import bl.catalogos.TipoAutoridadesBl;
import entitys.SesionEntity;
import entitys.AutoridadEntity;
import entitys.DomicilioEntity;
import entitys.MunicipioEntity;
import entitys.TipoAutoridadEntity;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class Autoridades extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    AutoridadBl bl = new AutoridadBl();
    TipoAutoridadesBl blTipo = new TipoAutoridadesBl();
    MunicipiosBl blMunicipio = new MunicipiosBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form Destinos
     */
    public Autoridades(){
        initComponents();
    }
    
    public Autoridades(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatosCampos();
        mapeoTeclas();
        inicializaComboTipo();
        inicializaComboMunicipio();
        inicializaComboTipoBuscar();
        iniciaCmbBusqueda();
    }
    
    private void formatosCampos(){
        txtNombreNew.setDocument(new JTextMascara(290,true,"*"));
        txtCalleNew.setDocument(new JTextMascara(69,true,"*"));
        txtNumExtNew.setDocument(new JTextMascara(19,true,"*"));
        txtNumIntNew.setDocument(new JTextMascara(19,true,"*"));
        txtColoniaNew.setDocument(new JTextMascara(69,true,"*"));
        txtCPNew.setDocument(new JTextMascara(5,true,"numerico"));
        txtTelefonoNew.setDocument(new JTextMascara(19,true,"*"));
        txtEmailNew.setDocument(new JTextMascara(69,true,"email"));
        txtObservacionesNew.setDocument(new JTextMascaraTextArea());
        txtNombreUpdate.setDocument(new JTextMascara(290,true,"*"));
        txtCalleUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtNumExtUpdate.setDocument(new JTextMascara(19,true,"*"));
        txtNumIntUpdate.setDocument(new JTextMascara(19,true,"*"));
        txtColoniaUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtCPUpdate.setDocument(new JTextMascara(5,true,"numerico"));
        txtTelefonoUpdate.setDocument(new JTextMascara(19,true,"*"));
        txtEmailUpdate.setDocument(new JTextMascara(69,true,"email"));
        txtObservacionesUpdate.setDocument(new JTextMascaraTextArea());
        lblAsteriscoNew.setVisible(false);
    }

    // <editor-fold defaultstate="collapsed" desc="Inicializacion de Combos">
    private void inicializaComboTipo(){
        u.inicializaCombo(cmbTipoCapNew, new TipoAutoridadEntity(), blTipo.getTipoAutoridades());
        u.inicializaCombo(cmbTipoCapUpdate, new TipoAutoridadEntity(), blTipo.getTipoAutoridades());
    }
    
    private void inicializaComboMunicipio(){
        u.inicializaCombo(cmbMunicipioCapNew, new MunicipioEntity(), blMunicipio.getMunicipios());
        u.inicializaCombo(cmbMunicipioCapUpdate, new MunicipioEntity(), blMunicipio.getMunicipios());
    }
    
    private void inicializaComboTipoBuscar(){
        u.inicializaCombo(cmbTipoUpdate, new TipoAutoridadEntity(), blTipo.getTipoAutoridades());
    }
    
    private void inicializaComboMunicipioBuscar(String id_tipo){
        u.inicializaCombo(cmbMunicipioUpdate, new MunicipioEntity(), bl.getMunicipiosTipoAutoridad(id_tipo));
    }
    
    private void inicializaComboAutoridad(String id_tipo){
        u.inicializaCombo(cmbAutoridadUpdate, new AutoridadEntity(), bl.getAutoridades(id_tipo));
    }
    
    private void inicializaComboAutoridad(String id_tipo, Integer id_mun){
        u.inicializaCombo(cmbAutoridadUpdate, new AutoridadEntity(), bl.getAutoridades(id_tipo, id_mun));
    }
    
    private void iniciaCmbBusqueda(){
        u.inicializaCombo(cmbAutoridadUpdate, new AutoridadEntity());
        cmbAutoridadUpdate.setEnabled(false);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funciones Botones">
    private void botonEsc(){
        if(jTabbedPane1.getSelectedIndex()==0){
            txtNombreNew.setText("");
            if(cmbTipoCapNew.getItemCount()>0)
                cmbTipoCapNew.setSelectedIndex(0);
            if(cmbMunicipioCapNew.getItemCount()>0)
                cmbMunicipioCapNew.setSelectedIndex(0);
            txtCalleNew.setText("");
            txtNumExtNew.setText("");
            txtNumIntNew.setText("");
            txtColoniaNew.setText("");
            txtCPNew.setText("");
            txtTelefonoNew.setText("");
            txtEmailNew.setText("");
            txtObservacionesNew.setText("");
            txtNombreNew.requestFocus();
        }else{
            cmbTipoUpdate.setSelectedIndex(0);
        }
    }
    
    private void botonF2(){
        if(jTabbedPane1.getSelectedIndex()==0){
            if(txtNombreNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreNew.requestFocus();
            }else if(cmbTipoCapNew.getItemCount()>0 && cmbTipoCapNew.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún Tipo de Autoridad", "Alerta", 2);
                cmbTipoCapNew.requestFocus();
            }else if(cmbMunicipioCapNew.isEnabled() && cmbMunicipioCapNew.getItemCount()>0 && cmbMunicipioCapNew.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún Municipio", "Alerta", 2);
                cmbMunicipioCapNew.requestFocus();
            }else{
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoCapNew.getSelectedItem();
                MunicipioEntity mun = (MunicipioEntity)cmbMunicipioCapNew.getSelectedItem();
                DomicilioEntity dom = new DomicilioEntity(txtCalleNew.getText(), txtNumExtNew.getText(), txtNumIntNew.getText(),
                        txtColoniaNew.getText(), txtCPNew.getText(), null, txtTelefonoNew.getText(), txtEmailNew.getText());
                AutoridadEntity auto = new AutoridadEntity(tipo, mun, txtNombreNew.getText(), dom, txtObservacionesNew.getText());
                ErrorEntity error = bl.saveAutoridad(auto, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    txtNombreNew.setText("");
                    inicializaComboTipo();
                    inicializaComboMunicipio();
                    inicializaComboTipoBuscar();
                    iniciaCmbBusqueda();
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF4(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbAutoridadUpdate.getItemCount()>0 && cmbAutoridadUpdate.getSelectedIndex()>0){
            if(txtNombreUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else if(cmbTipoCapUpdate.getItemCount()>0 && cmbTipoCapUpdate.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún Tipo de Autoridad", "Alerta", 2);
                cmbTipoCapUpdate.requestFocus();
            }else if(cmbMunicipioCapUpdate.isEnabled() && cmbMunicipioCapUpdate.getItemCount()>0 && cmbMunicipioCapUpdate.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún Municipio", "Alerta", 2);
                cmbMunicipioCapUpdate.requestFocus();
            }else{
                AutoridadEntity auto = (AutoridadEntity)cmbAutoridadUpdate.getSelectedItem();
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoCapUpdate.getSelectedItem();
                MunicipioEntity mun = (MunicipioEntity)cmbMunicipioCapUpdate.getSelectedItem();
                DomicilioEntity dom = new DomicilioEntity(txtCalleUpdate.getText(), txtNumExtUpdate.getText(), txtNumIntUpdate.getText(), 
                        txtColoniaUpdate.getText(), txtCPUpdate.getText(), null, txtTelefonoUpdate.getText(), txtEmailUpdate.getText());
                auto.setTipo(tipo);
                auto.setMun(mun);
                auto.setNombre(txtNombreUpdate.getText());
                auto.setDom(dom);
                auto.setObservaciones(txtObservacionesUpdate.getText());
                ErrorEntity error = bl.updateAutoridad(auto, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                    inicializaComboTipo();
                    inicializaComboMunicipio();
                    inicializaComboTipoBuscar();
                    iniciaCmbBusqueda();
                    cmbTipoUpdate.requestFocus();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF6(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbAutoridadUpdate.getItemCount()>0){
            if(cmbAutoridadUpdate.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún elemento", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else{                
                if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar el elemento seleccionado?", "Alerta", 0)==0){
                    ErrorEntity error = bl.deleteAutoridad(((AutoridadEntity)cmbAutoridadUpdate.getSelectedItem()).getId_autoridad());
                    if(!error.getError()){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                        inicializaComboTipo();
                        inicializaComboMunicipio();
                        inicializaComboTipoBuscar();
                        iniciaCmbBusqueda();
                        cmbTipoUpdate.requestFocus();
                    }else{
                        if(error.getNumError().equals(1451)){
                            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar ya que el elemento seleccionado está siendo utilizado por otra información", "Aviso", 1);
                            cmbTipoUpdate.requestFocus();
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
        jLabel21 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNombreNew = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblAsteriscoNew = new javax.swing.JLabel();
        cmbTipoCapNew = new javax.swing.JComboBox();
        cmbMunicipioCapNew = new javax.swing.JComboBox();
        jLabel26 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        txtNumIntNew = new javax.swing.JTextField();
        txtNumExtNew = new javax.swing.JTextField();
        txtCalleNew = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtColoniaNew = new javax.swing.JTextField();
        txtCPNew = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        txtTelefonoNew = new javax.swing.JTextField();
        txtEmailNew = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtObservacionesNew = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbMunicipioUpdate = new javax.swing.JComboBox();
        jLabel27 = new javax.swing.JLabel();
        cmbAutoridadUpdate = new javax.swing.JComboBox();
        cmbTipoUpdate = new javax.swing.JComboBox();
        txtNombreUpdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacionesUpdate = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCalleUpdate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        lblAsteriscoUpdate = new javax.swing.JLabel();
        txtNumExtUpdate = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtColoniaUpdate = new javax.swing.JTextField();
        txtNumIntUpdate = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtCPUpdate = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtTelefonoUpdate = new javax.swing.JTextField();
        txtEmailUpdate = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        cmbMunicipioCapUpdate = new javax.swing.JComboBox();
        cmbTipoCapUpdate = new javax.swing.JComboBox();
        jLabel20 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Catálogo - Autoridades");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Cat_autoridades.jpg"))); // NOI18N

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        jPanel1.setLayout(null);

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("*");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(10, 40, 30, 30);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setText("Nombre de la Autoridad");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(20, 40, 210, 30);

        txtNombreNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNombreNew);
        txtNombreNew.setBounds(10, 70, 620, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setText("Tipo de Autoridad");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(20, 110, 120, 30);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 0, 0));
        jLabel23.setText("*");
        jPanel1.add(jLabel23);
        jLabel23.setBounds(10, 110, 30, 30);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Municipio");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(330, 110, 80, 30);

        lblAsteriscoNew.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAsteriscoNew.setForeground(new java.awt.Color(255, 0, 0));
        lblAsteriscoNew.setText("*");
        jPanel1.add(lblAsteriscoNew);
        lblAsteriscoNew.setBounds(390, 110, 30, 30);

        cmbTipoCapNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTipoCapNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoCapNewActionPerformed(evt);
            }
        });
        jPanel1.add(cmbTipoCapNew);
        cmbTipoCapNew.setBounds(10, 140, 300, 30);

        cmbMunicipioCapNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbMunicipioCapNew);
        cmbMunicipioCapNew.setBounds(330, 140, 300, 30);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Calle");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(20, 180, 120, 30);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel35.setText("No. Ext.");
        jPanel1.add(jLabel35);
        jLabel35.setBounds(410, 180, 80, 30);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel36.setText("No. Int.");
        jPanel1.add(jLabel36);
        jLabel36.setBounds(520, 180, 80, 30);

        txtNumIntNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNumIntNew);
        txtNumIntNew.setBounds(520, 210, 110, 30);

        txtNumExtNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNumExtNew);
        txtNumExtNew.setBounds(400, 210, 110, 30);

        txtCalleNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtCalleNew);
        txtCalleNew.setBounds(10, 210, 380, 30);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel38.setText("Colonia");
        jPanel1.add(jLabel38);
        jLabel38.setBounds(20, 250, 170, 30);

        txtColoniaNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtColoniaNew);
        txtColoniaNew.setBounds(10, 280, 250, 30);

        txtCPNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtCPNew);
        txtCPNew.setBounds(270, 280, 80, 30);

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel39.setText("C.P.");
        jPanel1.add(jLabel39);
        jLabel39.setBounds(270, 250, 50, 30);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel40.setText("Teléfono");
        jPanel1.add(jLabel40);
        jLabel40.setBounds(360, 250, 90, 30);

        txtTelefonoNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtTelefonoNew);
        txtTelefonoNew.setBounds(360, 280, 110, 30);

        txtEmailNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtEmailNew);
        txtEmailNew.setBounds(480, 280, 150, 30);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel41.setText("E-mail");
        jPanel1.add(jLabel41);
        jLabel41.setBounds(480, 250, 60, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Observaciones");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(10, 320, 134, 30);

        txtObservacionesNew.setColumns(20);
        txtObservacionesNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtObservacionesNew.setRows(4);
        txtObservacionesNew.setNextFocusableComponent(jTabbedPane1);
        txtObservacionesNew.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtObservacionesNewKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(txtObservacionesNew);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 350, 620, 80);

        jTabbedPane1.addTab("Nuevo", jPanel1);

        jPanel2.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de Autoridades");
        jPanel3.add(jLabel4);
        jLabel4.setBounds(0, 20, 140, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Municipios");
        jPanel3.add(jLabel5);
        jLabel5.setBounds(20, 60, 120, 30);

        cmbMunicipioUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbMunicipioUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbMunicipioUpdateActionPerformed(evt);
            }
        });
        jPanel3.add(cmbMunicipioUpdate);
        cmbMunicipioUpdate.setBounds(150, 60, 460, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Autoridades");
        jPanel3.add(jLabel27);
        jLabel27.setBounds(20, 100, 120, 30);

        cmbAutoridadUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbAutoridadUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAutoridadUpdateActionPerformed(evt);
            }
        });
        jPanel3.add(cmbAutoridadUpdate);
        cmbAutoridadUpdate.setBounds(150, 100, 460, 30);

        cmbTipoUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTipoUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoUpdateActionPerformed(evt);
            }
        });
        jPanel3.add(cmbTipoUpdate);
        cmbTipoUpdate.setBounds(150, 20, 460, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 11, 620, 150);

        txtNombreUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNombreUpdate);
        txtNombreUpdate.setBounds(10, 190, 620, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Observaciones");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 400, 134, 30);

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
        jScrollPane2.setBounds(10, 430, 620, 80);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Nombre de la Autoridad");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(20, 160, 170, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("No. Ext.");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(410, 280, 80, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setText("Municipio");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(330, 220, 80, 30);

        txtCalleUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtCalleUpdate);
        txtCalleUpdate.setBounds(10, 310, 380, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("*");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 160, 30, 30);

        lblAsteriscoUpdate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblAsteriscoUpdate.setForeground(new java.awt.Color(255, 0, 0));
        lblAsteriscoUpdate.setText("*");
        jPanel2.add(lblAsteriscoUpdate);
        lblAsteriscoUpdate.setBounds(390, 220, 30, 30);

        txtNumExtUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNumExtUpdate);
        txtNumExtUpdate.setBounds(400, 310, 110, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Colonia");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(20, 340, 170, 30);

        txtColoniaUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtColoniaUpdate);
        txtColoniaUpdate.setBounds(10, 370, 250, 30);

        txtNumIntUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNumIntUpdate);
        txtNumIntUpdate.setBounds(520, 310, 110, 30);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("No. Int.");
        jPanel2.add(jLabel29);
        jLabel29.setBounds(520, 280, 80, 30);

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel30.setText("C.P.");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(270, 340, 50, 30);

        txtCPUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtCPUpdate);
        txtCPUpdate.setBounds(270, 370, 80, 30);

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel32.setText("Teléfono");
        jPanel2.add(jLabel32);
        jLabel32.setBounds(360, 340, 90, 30);

        txtTelefonoUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtTelefonoUpdate);
        txtTelefonoUpdate.setBounds(360, 370, 110, 30);

        txtEmailUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtEmailUpdate);
        txtEmailUpdate.setBounds(480, 370, 150, 30);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel33.setText("E-mail");
        jPanel2.add(jLabel33);
        jLabel33.setBounds(480, 340, 60, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setText("Calle");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(20, 280, 120, 30);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel19.setText("Tipo de Autoridad");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(20, 220, 120, 30);

        cmbMunicipioCapUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbMunicipioCapUpdate);
        cmbMunicipioCapUpdate.setBounds(330, 250, 300, 30);

        cmbTipoCapUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbTipoCapUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoCapUpdateActionPerformed(evt);
            }
        });
        jPanel2.add(cmbTipoCapUpdate);
        cmbTipoCapUpdate.setBounds(10, 250, 300, 30);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 0, 0));
        jLabel20.setText("*");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(10, 220, 30, 30);

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
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void cmbMunicipioUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbMunicipioUpdateActionPerformed
        if(cmbMunicipioUpdate.getItemCount()>0 && cmbMunicipioUpdate.isEnabled()){
            if(cmbMunicipioUpdate.getSelectedIndex()==0){
                iniciaCmbBusqueda();
            }else{
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoUpdate.getSelectedItem();
                MunicipioEntity mun = (MunicipioEntity)cmbMunicipioUpdate.getSelectedItem();
                inicializaComboAutoridad(tipo.getId_tipo_autoridad(),mun.getId_municipio());
                cmbAutoridadUpdate.setEnabled(true);
            }
        }
    }//GEN-LAST:event_cmbMunicipioUpdateActionPerformed

    private void cmbTipoUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoUpdateActionPerformed
        if(cmbTipoUpdate.getItemCount()>0){
            if(cmbTipoUpdate.getSelectedIndex()==0){
                u.inicializaCombo(cmbMunicipioUpdate, new MunicipioEntity());
                cmbMunicipioUpdate.setEnabled(false);
            }else{    
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoUpdate.getSelectedItem();
                if(tipo.getRegla()){
                    inicializaComboMunicipioBuscar(tipo.getId_tipo_autoridad());
                    cmbMunicipioUpdate.setEnabled(true);
                    cmbAutoridadUpdate.setEnabled(false);
                    iniciaCmbBusqueda();
                }else{
                    u.inicializaCombo(cmbMunicipioUpdate, new MunicipioEntity());
                    cmbMunicipioUpdate.setEnabled(false);
                    inicializaComboAutoridad(tipo.getId_tipo_autoridad());
                    cmbAutoridadUpdate.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_cmbTipoUpdateActionPerformed

    private void cmbTipoCapUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoCapUpdateActionPerformed
        if(cmbTipoCapUpdate.getItemCount()>0){
            if(cmbMunicipioCapUpdate.getItemCount()>0)
                cmbMunicipioCapUpdate.setSelectedIndex(0);            
            if(cmbTipoCapUpdate.getSelectedIndex()==0){
                cmbMunicipioCapUpdate.setEnabled(false);
            }else{    
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoCapUpdate.getSelectedItem();
                if(tipo.getRegla()){
                    cmbMunicipioCapUpdate.setEnabled(true);
                }else{    
                    cmbMunicipioCapUpdate.setEnabled(false);
                }
            }
        }
    }//GEN-LAST:event_cmbTipoCapUpdateActionPerformed

    private void cmbTipoCapNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoCapNewActionPerformed
        if(cmbTipoCapNew.getItemCount()>0){
            if(cmbMunicipioCapNew.getItemCount()>0)
                cmbMunicipioCapNew.setSelectedIndex(0);
            
            if(cmbTipoCapNew.getSelectedIndex()==0){
                cmbMunicipioCapNew.setEnabled(false);
                lblAsteriscoNew.setVisible(false);
            }else{    
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoCapNew.getSelectedItem();
                if(tipo.getRegla()){
                    cmbMunicipioCapNew.setEnabled(true);
                    lblAsteriscoNew.setVisible(true);
                }else{    
                    cmbMunicipioCapNew.setEnabled(false);
                    lblAsteriscoNew.setVisible(false);
                }
            }
        }
    }//GEN-LAST:event_cmbTipoCapNewActionPerformed

    private void txtObservacionesNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesNewKeyPressed
        u.focus(evt, txtObservacionesNew);
    }//GEN-LAST:event_txtObservacionesNewKeyPressed

    private void cmbAutoridadUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAutoridadUpdateActionPerformed
        if(cmbAutoridadUpdate.getItemCount()>0){
            Boolean flag;
            if(cmbAutoridadUpdate.getSelectedIndex()==0){
                flag = false;
                txtNombreUpdate.setText("");
                if(cmbTipoCapUpdate.getItemCount()>0)
                    cmbTipoCapUpdate.setSelectedIndex(0);
                if(cmbMunicipioCapUpdate.getItemCount()>0)
                    cmbMunicipioCapUpdate.setSelectedIndex(0);
                txtCalleUpdate.setText("");
                txtNumExtUpdate.setText("");
                txtNumIntUpdate.setText("");
                txtColoniaUpdate.setText("");
                txtCPUpdate.setText("");
                txtTelefonoUpdate.setText("");
                txtEmailUpdate.setText("");
                txtObservacionesUpdate.setText("");
                cmbMunicipioCapUpdate.setEnabled(false);
            }else{
                flag = true;
                AutoridadEntity auto = (AutoridadEntity)cmbAutoridadUpdate.getSelectedItem();
                txtNombreUpdate.setText(auto.getNombre());
                txtCalleUpdate.setText(auto.getDom().getCalle());
                txtNumExtUpdate.setText(auto.getDom().getNum_ext());
                txtNumIntUpdate.setText(auto.getDom().getNum_int());
                txtColoniaUpdate.setText(auto.getDom().getColonia());
                txtCPUpdate.setText(auto.getDom().getCp());
                txtTelefonoUpdate.setText(auto.getDom().getTelefono());
                txtEmailUpdate.setText(auto.getDom().getEmail());
                txtObservacionesUpdate.setText(auto.getObservaciones());                
                cmbTipoCapUpdate.setSelectedItem(auto.getTipo());
                TipoAutoridadEntity tipo = (TipoAutoridadEntity)cmbTipoCapUpdate.getSelectedItem();
                cmbMunicipioCapUpdate.setEnabled(false);
                if(cmbMunicipioCapUpdate.getItemCount()>0)
                    cmbMunicipioCapUpdate.setSelectedIndex(0);
                if(tipo.getRegla()){
                    cmbMunicipioCapUpdate.setSelectedItem(auto.getMun());
                    cmbMunicipioCapUpdate.setEnabled(true);
                }
            }
            u.enabledComponet(flag,txtNombreUpdate,cmbTipoCapUpdate,txtCalleUpdate,txtNumExtUpdate,txtNumIntUpdate,txtColoniaUpdate,txtCPUpdate,
                    txtTelefonoUpdate,txtEmailUpdate,txtObservacionesUpdate);
        }
    }//GEN-LAST:event_cmbAutoridadUpdateActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbAutoridadUpdate;
    private javax.swing.JComboBox cmbMunicipioCapNew;
    private javax.swing.JComboBox cmbMunicipioCapUpdate;
    private javax.swing.JComboBox cmbMunicipioUpdate;
    private javax.swing.JComboBox cmbTipoCapNew;
    private javax.swing.JComboBox cmbTipoCapUpdate;
    private javax.swing.JComboBox cmbTipoUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAsteriscoNew;
    private javax.swing.JLabel lblAsteriscoUpdate;
    private javax.swing.JTextField txtCPNew;
    private javax.swing.JTextField txtCPUpdate;
    private javax.swing.JTextField txtCalleNew;
    private javax.swing.JTextField txtCalleUpdate;
    private javax.swing.JTextField txtColoniaNew;
    private javax.swing.JTextField txtColoniaUpdate;
    private javax.swing.JTextField txtEmailNew;
    private javax.swing.JTextField txtEmailUpdate;
    private javax.swing.JTextField txtNombreNew;
    private javax.swing.JTextField txtNombreUpdate;
    private javax.swing.JTextField txtNumExtNew;
    private javax.swing.JTextField txtNumExtUpdate;
    private javax.swing.JTextField txtNumIntNew;
    private javax.swing.JTextField txtNumIntUpdate;
    private javax.swing.JTextArea txtObservacionesNew;
    private javax.swing.JTextArea txtObservacionesUpdate;
    private javax.swing.JTextField txtTelefonoNew;
    private javax.swing.JTextField txtTelefonoUpdate;
    // End of variables declaration//GEN-END:variables
}
