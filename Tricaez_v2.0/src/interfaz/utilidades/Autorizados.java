package interfaz.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.utilidades.AutorizadosBl;
import bl.catalogos.MunicipiosBl;
import entitys.SesionEntity;
import entitys.AutorizadoEntity;
import entitys.DomicilioEntity;
import entitys.MunicipioEntity;
import entitys.NombreEntity;
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
public class Autorizados extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    AutorizadosBl bl = new AutorizadosBl();
    MunicipiosBl blMunicipio = new MunicipiosBl();
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form Destinos
     */
    public Autorizados(){
        initComponents();
    }
    
    public Autorizados(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        formatosCampos();
        mapeoTeclas();
        inicializaComboMunicipio();
        inicializaComboTipoBuscar();
    }
    
    private void formatosCampos(){
        txtNombreNew.setDocument(new JTextMascara(69,true,"*"));
        txtPaternoNew.setDocument(new JTextMascara(69,true,"*"));
        txtMaternoNew.setDocument(new JTextMascara(69,true,"*"));
        txtCalleNew.setDocument(new JTextMascara(69,true,"*"));
        txtNumExtNew.setDocument(new JTextMascara(19,true,"*"));
        txtNumIntNew.setDocument(new JTextMascara(19,true,"*"));
        txtColoniaNew.setDocument(new JTextMascara(69,true,"*"));
        txtCPNew.setDocument(new JTextMascara(5,true,"numerico"));
        txtTelefonoNew.setDocument(new JTextMascara(19,true,"*"));
        txtEmailNew.setDocument(new JTextMascara(99,true,"email"));
        txtObservacionesNew.setDocument(new JTextMascara(490,true,"*"));
        
        txtNombreUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtPaternoUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtMaternoUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtCalleUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtNumExtUpdate.setDocument(new JTextMascara(19,true,"*"));
        txtNumIntUpdate.setDocument(new JTextMascara(19,true,"*"));
        txtColoniaUpdate.setDocument(new JTextMascara(69,true,"*"));
        txtCPUpdate.setDocument(new JTextMascara(5,true,"numerico"));
        txtTelefonoUpdate.setDocument(new JTextMascara(19,true,"*"));
        txtEmailUpdate.setDocument(new JTextMascara(99,true,"email"));
        txtObservacionesUpdate.setDocument(new JTextMascara(490,true,"*"));
    }
        
    private void inicializaComboMunicipio(){
        u.inicializaCombo(cmbMunicipioNew, new MunicipioEntity(), blMunicipio.getMunicipios());
        u.inicializaCombo(cmbMunicipioUpdate, new MunicipioEntity(), blMunicipio.getMunicipios());
    }
    
    private void inicializaComboTipoBuscar(){
        u.inicializaCombo(cmbAutorizadores, new AutorizadoEntity(), bl.getAutorizadores());
    }
    
    private void limpiar(){
        txtNombreNew.setText("");
        txtPaternoNew.setText("");
        txtMaternoNew.setText("");
        if(cmbMunicipioNew.getItemCount()>0)
            cmbMunicipioNew.setSelectedIndex(0);
        txtCalleNew.setText("");
        txtNumExtNew.setText("");
        txtNumIntNew.setText("");
        txtColoniaNew.setText("");
        txtCPNew.setText("");
        txtTelefonoNew.setText("");
        txtEmailNew.setText("");
        txtObservacionesNew.setText("");
        txtNombreNew.requestFocus();
        u.inicializaCombo(cmbAutorizadores, new AutorizadoEntity(), bl.getAutorizadores());
        cmbAutorizadores.setSelectedIndex(0);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones Botones">
    private void botonEsc(){
        if(jTabbedPane1.getSelectedIndex()==0){
            txtNombreNew.setText("");
            if(cmbMunicipioNew.getItemCount()>0)
                cmbMunicipioNew.setSelectedIndex(0);
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
            if(cmbAutorizadores.getItemCount()>0)
                cmbAutorizadores.setSelectedIndex(0);
        }
    }
    
    private void botonF2(){
        if(jTabbedPane1.getSelectedIndex()==0){
            if(txtNombreNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreNew.requestFocus();
            }else if(txtPaternoNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Paterno es obligatorio", "Alerta", 2);
                txtPaternoNew.requestFocus();
            }else if(txtMaternoNew.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Materno es obligatorio", "Alerta", 2);
                txtMaternoNew.requestFocus();
            }else{                
                NombreEntity nom = new NombreEntity(txtNombreNew.getText(), txtPaternoNew.getText(), txtMaternoNew.getText());
                MunicipioEntity mun = null;
                if(cmbMunicipioNew.getSelectedIndex()>0)
                    mun = (MunicipioEntity)cmbMunicipioNew.getSelectedItem();
                DomicilioEntity dom = new DomicilioEntity(txtCalleNew.getText(), txtNumExtNew.getText(), txtNumIntNew.getText(), 
                        txtColoniaNew.getText(), txtCPNew.getText(), mun, txtTelefonoNew.getText(), txtEmailNew.getText());
                ErrorEntity error = bl.saveAutorizador(new AutorizadoEntity(nom,dom,txtObservacionesNew.getText()),sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    inicializaComboMunicipio();
                    inicializaComboTipoBuscar();
                    botonEsc();
                    limpiar();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF4(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbAutorizadores.getItemCount()>0 && cmbAutorizadores.getSelectedIndex()>0){
            if(txtNombreUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else if(txtPaternoUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Paterno es obligatorio", "Alerta", 2);
                txtPaternoUpdate.requestFocus();
            }else if(txtMaternoUpdate.getText().length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Materno es obligatorio", "Alerta", 2);
                txtMaternoUpdate.requestFocus();
            }else{
                NombreEntity nom = new NombreEntity(txtNombreUpdate.getText(), txtPaternoUpdate.getText(), txtMaternoUpdate.getText());
                 MunicipioEntity mun = null;
                if(cmbMunicipioUpdate.getSelectedIndex()>0)
                    mun = (MunicipioEntity)cmbMunicipioUpdate.getSelectedItem();
                DomicilioEntity dom = new DomicilioEntity(txtCalleUpdate.getText(), txtNumExtUpdate.getText(), txtNumIntUpdate.getText(), 
                        txtColoniaUpdate.getText(), txtCPUpdate.getText(), mun, txtTelefonoUpdate.getText(), txtEmailUpdate.getText());
                AutorizadoEntity auto = (AutorizadoEntity)cmbAutorizadores.getSelectedItem();
                auto.setNombre(nom);
                auto.setDom(dom);
                auto.setObservaciones(txtObservacionesUpdate.getText());
                ErrorEntity error = bl.updateAutorizador(auto, sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha modificado correctamente", "Aviso", 1);
                    inicializaComboMunicipio();
                    inicializaComboTipoBuscar();
                    cmbAutorizadores.requestFocus();
                    limpiar();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }
    }
    
    private void botonF6(){
        if(jTabbedPane1.getSelectedIndex()==1 && cmbAutorizadores.getItemCount()>0){
            if(cmbAutorizadores.getSelectedIndex()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado ningún elemento", "Alerta", 2);
                txtNombreUpdate.requestFocus();
            }else{
                if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar el elemento seleccionado?", "Alerta", 0)==0){
                    AutorizadoEntity tipo = (AutorizadoEntity)cmbAutorizadores.getSelectedItem();
                    ErrorEntity error = bl.deleteAutorizador(tipo.getId_autorizado());
                    if(!error.getError()){
                        javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                        inicializaComboMunicipio();
                        inicializaComboTipoBuscar();
                        cmbAutorizadores.requestFocus();
                        limpiar();
                    }else{
                        if(error.getNumError().equals(1451)){
                            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se puede eliminar ya que el elemento seleccionado está siendo utilizado por otra información", "Aviso", 1);
                            cmbAutorizadores.requestFocus();
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
        jLabel24 = new javax.swing.JLabel();
        cmbMunicipioNew = new javax.swing.JComboBox();
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
        jLabel16 = new javax.swing.JLabel();
        txtPaternoNew = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        txtMaternoNew = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        cmbAutorizadores = new javax.swing.JComboBox();
        txtNombreUpdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtObservacionesUpdate = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCalleUpdate = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
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
        cmbMunicipioUpdate = new javax.swing.JComboBox();
        txtPaternoUpdate = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtMaternoUpdate = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnF4 = new javax.swing.JButton();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Autorizados");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_autorizados.jpg"))); // NOI18N

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
        jLabel12.setText("Nombre");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(20, 40, 110, 30);

        txtNombreNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNombreNew);
        txtNombreNew.setBounds(10, 70, 200, 30);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Municipio");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(20, 260, 80, 30);

        cmbMunicipioNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(cmbMunicipioNew);
        cmbMunicipioNew.setBounds(100, 260, 300, 30);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Calle");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(20, 110, 120, 30);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel35.setText("No. Ext.");
        jPanel1.add(jLabel35);
        jLabel35.setBounds(410, 110, 80, 30);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel36.setText("No. Int.");
        jPanel1.add(jLabel36);
        jLabel36.setBounds(520, 110, 80, 30);

        txtNumIntNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNumIntNew);
        txtNumIntNew.setBounds(520, 140, 110, 30);

        txtNumExtNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtNumExtNew);
        txtNumExtNew.setBounds(400, 140, 110, 30);

        txtCalleNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtCalleNew);
        txtCalleNew.setBounds(10, 140, 380, 30);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel38.setText("Colonia");
        jPanel1.add(jLabel38);
        jLabel38.setBounds(20, 180, 170, 30);

        txtColoniaNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtColoniaNew);
        txtColoniaNew.setBounds(10, 210, 250, 30);

        txtCPNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtCPNew);
        txtCPNew.setBounds(270, 210, 80, 30);

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel39.setText("C.P.");
        jPanel1.add(jLabel39);
        jLabel39.setBounds(270, 180, 50, 30);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel40.setText("Teléfono");
        jPanel1.add(jLabel40);
        jLabel40.setBounds(360, 180, 90, 30);

        txtTelefonoNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtTelefonoNew);
        txtTelefonoNew.setBounds(360, 210, 110, 30);

        txtEmailNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtEmailNew);
        txtEmailNew.setBounds(480, 210, 150, 30);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel41.setText("E-mail");
        jPanel1.add(jLabel41);
        jLabel41.setBounds(480, 180, 60, 30);

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

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel16.setText("Apellido Paterno");
        jPanel1.add(jLabel16);
        jLabel16.setBounds(230, 40, 140, 30);

        txtPaternoNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtPaternoNew);
        txtPaternoNew.setBounds(220, 70, 200, 30);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 0, 0));
        jLabel25.setText("*");
        jPanel1.add(jLabel25);
        jLabel25.setBounds(220, 40, 30, 30);

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel42.setText("Apellido Materno");
        jPanel1.add(jLabel42);
        jLabel42.setBounds(440, 40, 130, 30);

        txtMaternoNew.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtMaternoNew);
        txtMaternoNew.setBounds(430, 70, 200, 30);

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 0, 0));
        jLabel43.setText("*");
        jPanel1.add(jLabel43);
        jLabel43.setBounds(430, 40, 30, 30);

        jTabbedPane1.addTab("Nuevo", jPanel1);

        jPanel2.setLayout(null);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel3.setLayout(null);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel27.setText("Autorizados");
        jPanel3.add(jLabel27);
        jLabel27.setBounds(20, 30, 110, 30);

        cmbAutorizadores.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbAutorizadores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAutorizadoresActionPerformed(evt);
            }
        });
        jPanel3.add(cmbAutorizadores);
        cmbAutorizadores.setBounds(110, 30, 480, 30);

        jPanel2.add(jPanel3);
        jPanel3.setBounds(10, 11, 620, 90);

        txtNombreUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNombreUpdate);
        txtNombreUpdate.setBounds(10, 150, 200, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Observaciones");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(10, 390, 134, 30);

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
        jScrollPane2.setBounds(10, 420, 620, 80);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Nombre");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(20, 120, 190, 30);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("No. Ext.");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(410, 190, 80, 30);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setText("Municipio");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(20, 340, 80, 30);

        txtCalleUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtCalleUpdate);
        txtCalleUpdate.setBounds(10, 220, 380, 30);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 0, 0));
        jLabel13.setText("*");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(10, 120, 30, 30);

        txtNumExtUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNumExtUpdate);
        txtNumExtUpdate.setBounds(400, 220, 110, 30);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel18.setText("Colonia");
        jPanel2.add(jLabel18);
        jLabel18.setBounds(20, 260, 170, 30);

        txtColoniaUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtColoniaUpdate);
        txtColoniaUpdate.setBounds(10, 290, 250, 30);

        txtNumIntUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtNumIntUpdate);
        txtNumIntUpdate.setBounds(520, 220, 110, 30);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("No. Int.");
        jPanel2.add(jLabel29);
        jLabel29.setBounds(520, 190, 80, 30);

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel30.setText("C.P.");
        jPanel2.add(jLabel30);
        jLabel30.setBounds(270, 260, 50, 30);

        txtCPUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtCPUpdate);
        txtCPUpdate.setBounds(270, 290, 80, 30);

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel32.setText("Teléfono");
        jPanel2.add(jLabel32);
        jLabel32.setBounds(360, 260, 90, 30);

        txtTelefonoUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtTelefonoUpdate);
        txtTelefonoUpdate.setBounds(360, 290, 110, 30);

        txtEmailUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtEmailUpdate);
        txtEmailUpdate.setBounds(480, 290, 150, 30);

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel33.setText("E-mail");
        jPanel2.add(jLabel33);
        jLabel33.setBounds(480, 260, 60, 30);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel17.setText("Calle");
        jPanel2.add(jLabel17);
        jLabel17.setBounds(20, 190, 120, 30);

        cmbMunicipioUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(cmbMunicipioUpdate);
        cmbMunicipioUpdate.setBounds(100, 340, 300, 30);

        txtPaternoUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtPaternoUpdate);
        txtPaternoUpdate.setBounds(220, 150, 200, 30);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 0, 0));
        jLabel19.setText("*");
        jPanel2.add(jLabel19);
        jLabel19.setBounds(220, 120, 30, 30);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Apellido Paterno");
        jPanel2.add(jLabel20);
        jLabel20.setBounds(230, 120, 150, 30);

        txtMaternoUpdate.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel2.add(txtMaternoUpdate);
        txtMaternoUpdate.setBounds(430, 150, 200, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 0, 0));
        jLabel22.setText("*");
        jPanel2.add(jLabel22);
        jLabel22.setBounds(430, 120, 30, 30);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setText("Apellido Materno");
        jPanel2.add(jLabel23);
        jLabel23.setBounds(440, 120, 150, 30);

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
        btnEsc.setText("Esc - Cancelar");
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
        if(evt.getKeyChar()==KeyEvent.VK_TAB){
            evt.consume();
            jTabbedPane1.requestFocus();
        }
        if(evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB){
            cmbMunicipioUpdate.requestFocus();
        }
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

    private void txtObservacionesNewKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtObservacionesNewKeyPressed
        if(evt.getKeyChar()==KeyEvent.VK_TAB){
            evt.consume();
            jTabbedPane1.requestFocus();
        }
        if(evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB){
            cmbMunicipioNew.requestFocus();
        }
    }//GEN-LAST:event_txtObservacionesNewKeyPressed

    private void cmbAutorizadoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAutorizadoresActionPerformed
        if(cmbAutorizadores.getItemCount()>0){
            Boolean flag;
            if(cmbAutorizadores.getSelectedIndex()==0){
                flag = false;
                txtNombreUpdate.setText("");
                txtPaternoUpdate.setText("");
                txtMaternoUpdate.setText("");
                if(cmbMunicipioUpdate.getItemCount()>0)
                    cmbMunicipioUpdate.setSelectedIndex(0);
                txtCalleUpdate.setText("");
                txtNumExtUpdate.setText("");
                txtNumIntUpdate.setText("");
                txtColoniaUpdate.setText("");
                txtCPUpdate.setText("");
                txtTelefonoUpdate.setText("");
                txtEmailUpdate.setText("");
                txtObservacionesUpdate.setText("");
            }else{
                flag = true;
                AutorizadoEntity auto = (AutorizadoEntity)cmbAutorizadores.getSelectedItem();
                txtNombreUpdate.setText(auto.getNombre().getNombre());
                txtPaternoUpdate.setText(auto.getNombre().getPaterno());
                txtMaternoUpdate.setText(auto.getNombre().getMaterno());
                txtCalleUpdate.setText(auto.getDom().getCalle());
                txtNumExtUpdate.setText(auto.getDom().getNum_ext());
                txtNumIntUpdate.setText(auto.getDom().getNum_int());
                txtColoniaUpdate.setText(auto.getDom().getColonia());
                txtCPUpdate.setText(auto.getDom().getCp());
                txtTelefonoUpdate.setText(auto.getDom().getTelefono());
                txtEmailUpdate.setText(auto.getDom().getEmail());
                txtObservacionesUpdate.setText(auto.getObservaciones());
                if(auto.getDom().getMun()!=null)
                    cmbMunicipioUpdate.setSelectedItem(auto.getDom().getMun());
            }
            u.enabledComponet(flag, txtNombreUpdate,txtPaternoUpdate,txtMaternoUpdate,txtCalleUpdate,txtNumExtUpdate,txtNumIntUpdate,
                    txtColoniaUpdate,txtCPUpdate,txtTelefonoUpdate,txtEmailUpdate,txtObservacionesUpdate,cmbMunicipioUpdate);
        }
    }//GEN-LAST:event_cmbAutorizadoresActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF4;
    private javax.swing.JButton btnF6;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbAutorizadores;
    private javax.swing.JComboBox cmbMunicipioNew;
    private javax.swing.JComboBox cmbMunicipioUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
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
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtCPNew;
    private javax.swing.JTextField txtCPUpdate;
    private javax.swing.JTextField txtCalleNew;
    private javax.swing.JTextField txtCalleUpdate;
    private javax.swing.JTextField txtColoniaNew;
    private javax.swing.JTextField txtColoniaUpdate;
    private javax.swing.JTextField txtEmailNew;
    private javax.swing.JTextField txtEmailUpdate;
    private javax.swing.JTextField txtMaternoNew;
    private javax.swing.JTextField txtMaternoUpdate;
    private javax.swing.JTextField txtNombreNew;
    private javax.swing.JTextField txtNombreUpdate;
    private javax.swing.JTextField txtNumExtNew;
    private javax.swing.JTextField txtNumExtUpdate;
    private javax.swing.JTextField txtNumIntNew;
    private javax.swing.JTextField txtNumIntUpdate;
    private javax.swing.JTextArea txtObservacionesNew;
    private javax.swing.JTextArea txtObservacionesUpdate;
    private javax.swing.JTextField txtPaternoNew;
    private javax.swing.JTextField txtPaternoUpdate;
    private javax.swing.JTextField txtTelefonoNew;
    private javax.swing.JTextField txtTelefonoUpdate;
    // End of variables declaration//GEN-END:variables
}
