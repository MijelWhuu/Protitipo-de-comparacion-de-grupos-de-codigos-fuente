package interfaz.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.utilidades.UsuariosBl;
import entitys.NombreEntity;
import entitys.SesionEntity;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class Usuarios extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    UsuariosBl bl = new UsuariosBl();
    javax.swing.JCheckBox[] cbxT;
    Utilidades u = new Utilidades();
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public Usuarios() {
        initComponents();
    }

    public Usuarios(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        formatearCampos();
        inicioV();
        inicializaComboSentido();
    }
    
    private void formatearCampos(){
        txtNombre.setDocument(new JTextMascara(90,true,"todo"));
        txtPaterno.setDocument(new JTextMascara(90,true,"todo"));
        txtMaterno.setDocument(new JTextMascara(90,true,"todo"));
    }
    
    private void inicioV(){
        javax.swing.JCheckBox[] cbxTmp={
            menuPrincipal,
            cbxM01,cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04,
            cbxM02,
            cbxM03,
            cbxM04,
            cbxM05,
            cbxM06,
            cbxM07,cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06,
            cbxM08,
            cbxM09,
            cbxM11_04,
            menuCatalogos,cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,cbxM11_10,
                cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17,
            menuReportes,cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12,
            menuUtilerias,cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05
        };
        cbxT = cbxTmp;
    }
    
    private void inicializaComboSentido(){
        u.inicializaCombo(cmbUsuario, new SesionEntity(), bl.getEmpleados());
    }
    
    private void habilitaPadre(javax.swing.JCheckBox cbx,javax.swing.JCheckBox... checkboxes){
        for(javax.swing.JCheckBox chekbox: checkboxes)
            chekbox.setSelected((cbx.isSelected()));
    }
    
    private Boolean verificaHijos(javax.swing.JCheckBox... checkboxes){
        for(javax.swing.JCheckBox chekbox: checkboxes)
            if(!chekbox.isSelected())
                return true;
        return true;
    }
    
    private void desSeleccionarCheckboxes(){
        for(int i=0; i<cbxT.length; i++)
            cbxT[i].setSelected(false);
    }
    
    private void seleccionarCheckboxes(String permisos){
        if(permisos.length()==cbxT.length){
            for(int i=0; i<cbxT.length; i++)
                cbxT[i].setSelected((permisos.charAt(i)=='1'));
        }else{
            System.out.println("Número no coincide");
        }
    }
    
    private String getPermisos(){
        String permisos="";
        for (JCheckBox cbx : cbxT)
            permisos+=(cbx.isSelected())?"1":"0";
        return permisos;
    }
    
    private SesionEntity getDatos(){
        SesionEntity usuario = new SesionEntity();
        if(cmbUsuario.getItemCount()>0 && cmbUsuario.getSelectedIndex()!=0)
            usuario.setId_usuario(((SesionEntity)cmbUsuario.getSelectedItem()).getId_usuario());
        usuario.setUsuario(txtUsuario.getText());
        usuario.setNombre(new NombreEntity(txtNombre.getText(),txtPaterno.getText(),txtMaterno.getText()));
        usuario.setPass(new String(txtPassword1.getPassword()));
        usuario.setPantallas(getPermisos());
        return usuario;
    }
    
    private Boolean validaPass(){
        char passArray1[] = txtPassword1.getPassword();
        char passArray2[] = txtPassword2.getPassword();
        if(passArray1.length>0 && passArray2.length>0)
            if(new String(passArray1).equals(new String(passArray2)))
                return true;
        return false;
    }
    
    private Boolean validaCampos(){
        if(txtUsuario.getText().length()==0){
            javax.swing.JOptionPane.showMessageDialog(this,"El campo Usuario es obligatorio","Aviso",1);
            txtUsuario.requestFocus();
        }else if(txtNombre.getText().length()==0){
            javax.swing.JOptionPane.showMessageDialog(this,"El campo Nombre es obligatorio","Aviso",1);
            txtNombre.requestFocus();
        }else if(txtPaterno.getText().length()==0){
            javax.swing.JOptionPane.showMessageDialog(this,"El campo A. Paterno es obligatorio","Aviso",1);
            txtPaterno.requestFocus();
        }else if(txtMaterno.getText().length()==0){
            javax.swing.JOptionPane.showMessageDialog(this,"El campo A. Materno es obligatorio","Aviso",1);
            txtMaterno.requestFocus();
        }else if(!validaPass()){
            javax.swing.JOptionPane.showMessageDialog(this,"El Password es incorrecto","Aviso",1);
            txtPassword1.requestFocus();
        }else{
            return true;
        }
        return false;
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
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de botones">
    private void botonEsc(){
        txtUsuario.setText("");
        txtNombre.setText("");
        txtPaterno.setText("");
        txtMaterno.setText("");
        txtPassword1.setText("");
        txtPassword2.setText("");
        desSeleccionarCheckboxes();
        inicializaComboSentido();
        cmbUsuario.requestFocus();
    }
    
    private void botonF2(){
        if(validaCampos()){
            if(cmbUsuario.getSelectedIndex()==0){
                ErrorEntity error = bl.saveUsuario(getDatos(), sesion);
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }else{
                ErrorEntity error = bl.updateUsuario(getDatos());
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
        if(cmbUsuario.getSelectedIndex()>0){
            if(javax.swing.JOptionPane.showInternalConfirmDialog(this, "¿Desea eliminar eliminar el Usuario seleccionado?", "Alerta", 0)==0){
                ErrorEntity error = bl.deleteUsuario(((SesionEntity)cmbUsuario.getSelectedItem()).getId_usuario());
                if(!error.getError()){
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                    botonEsc();
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
                }
            }
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Usuario para eliminar", "Alerta", 2);
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
        panelBusqueda = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cmbUsuario = new javax.swing.JComboBox();
        btnF2 = new javax.swing.JButton();
        panelDatos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtPaterno = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtMaterno = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtPassword2 = new javax.swing.JPasswordField();
        txtPassword1 = new javax.swing.JPasswordField();
        lblPass = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panelCheckbox = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        menuPrincipal = new javax.swing.JCheckBox();
        cbxM01 = new javax.swing.JCheckBox();
        cbxM01_01 = new javax.swing.JCheckBox();
        cbxM01_02 = new javax.swing.JCheckBox();
        cbxM01_03 = new javax.swing.JCheckBox();
        cbxM02 = new javax.swing.JCheckBox();
        cbxM03 = new javax.swing.JCheckBox();
        cbxM04 = new javax.swing.JCheckBox();
        cbxM05 = new javax.swing.JCheckBox();
        cbxM06 = new javax.swing.JCheckBox();
        cbxM07 = new javax.swing.JCheckBox();
        cbxM07_01 = new javax.swing.JCheckBox();
        cbxM07_02 = new javax.swing.JCheckBox();
        cbxM07_04 = new javax.swing.JCheckBox();
        cbxM07_05 = new javax.swing.JCheckBox();
        cbxM07_06 = new javax.swing.JCheckBox();
        cbxM08 = new javax.swing.JCheckBox();
        cbxM09 = new javax.swing.JCheckBox();
        cbxM07_03 = new javax.swing.JCheckBox();
        cbxM01_04 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        cbxM11_17 = new javax.swing.JCheckBox();
        cbxM11_16 = new javax.swing.JCheckBox();
        cbxM11_15 = new javax.swing.JCheckBox();
        cbxM11_14 = new javax.swing.JCheckBox();
        cbxM11_13 = new javax.swing.JCheckBox();
        cbxM11_12 = new javax.swing.JCheckBox();
        cbxM11_11 = new javax.swing.JCheckBox();
        cbxM11_10 = new javax.swing.JCheckBox();
        cbxM11_09 = new javax.swing.JCheckBox();
        cbxM11_08 = new javax.swing.JCheckBox();
        cbxM11_07 = new javax.swing.JCheckBox();
        cbxM11_06 = new javax.swing.JCheckBox();
        cbxM11_05 = new javax.swing.JCheckBox();
        cbxM11_04 = new javax.swing.JCheckBox();
        cbxM11_03 = new javax.swing.JCheckBox();
        cbxM11_02 = new javax.swing.JCheckBox();
        cbxM11_01 = new javax.swing.JCheckBox();
        menuCatalogos = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        menuReportes = new javax.swing.JCheckBox();
        cbxM12_01 = new javax.swing.JCheckBox();
        cbxM12_02 = new javax.swing.JCheckBox();
        cbxM12_03 = new javax.swing.JCheckBox();
        cbxM12_04 = new javax.swing.JCheckBox();
        cbxM12_05 = new javax.swing.JCheckBox();
        cbxM12_06 = new javax.swing.JCheckBox();
        cbxM12_07 = new javax.swing.JCheckBox();
        cbxM12_08 = new javax.swing.JCheckBox();
        cbxM12_09 = new javax.swing.JCheckBox();
        cbxM12_10 = new javax.swing.JCheckBox();
        cbxM12_11 = new javax.swing.JCheckBox();
        cbxM12_12 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        menuUtilerias = new javax.swing.JCheckBox();
        cbxM13_01 = new javax.swing.JCheckBox();
        cbxM13_02 = new javax.swing.JCheckBox();
        cbxM13_03 = new javax.swing.JCheckBox();
        cbxM13_04 = new javax.swing.JCheckBox();
        cbxM13_05 = new javax.swing.JCheckBox();
        btnF6 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Usuarios");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Usuarios.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

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

        panelBusqueda.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtro de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelBusqueda.setNextFocusableComponent(panelDatos);
        panelBusqueda.setLayout(null);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Nombre del Empleado");
        panelBusqueda.add(jLabel5);
        jLabel5.setBounds(10, 30, 140, 30);

        cmbUsuario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbUsuario.setNextFocusableComponent(txtUsuario);
        cmbUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbUsuarioActionPerformed(evt);
            }
        });
        panelBusqueda.add(cmbUsuario);
        cmbUsuario.setBounds(10, 60, 340, 30);

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F2 - Guardar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        panelDatos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Empleado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelDatos.setNextFocusableComponent(panelCheckbox);
        panelDatos.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Usuario");
        panelDatos.add(jLabel2);
        jLabel2.setBounds(20, 30, 90, 30);

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        panelDatos.add(txtUsuario);
        txtUsuario.setBounds(120, 30, 220, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("A. Paterno");
        panelDatos.add(jLabel3);
        jLabel3.setBounds(20, 110, 90, 30);

        txtNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        panelDatos.add(txtNombre);
        txtNombre.setBounds(120, 70, 220, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("A. Materno");
        panelDatos.add(jLabel4);
        jLabel4.setBounds(20, 150, 90, 30);

        txtPaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        panelDatos.add(txtPaterno);
        txtPaterno.setBounds(120, 110, 220, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Nombre");
        panelDatos.add(jLabel6);
        jLabel6.setBounds(20, 70, 90, 30);

        txtMaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        panelDatos.add(txtMaterno);
        txtMaterno.setBounds(120, 150, 220, 30);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Contraseña");
        panelDatos.add(jLabel7);
        jLabel7.setBounds(20, 190, 90, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("<html><div align=\"right\">Confirmar<br>contraseña</div></html>");
        panelDatos.add(jLabel8);
        jLabel8.setBounds(20, 230, 90, 30);

        txtPassword2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtPassword2.setNextFocusableComponent(cbxM01);
        txtPassword2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPassword2KeyReleased(evt);
            }
        });
        panelDatos.add(txtPassword2);
        txtPassword2.setBounds(120, 230, 220, 30);

        txtPassword1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtPassword1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPassword1KeyReleased(evt);
            }
        });
        panelDatos.add(txtPassword1);
        txtPassword1.setBounds(120, 190, 220, 30);
        panelDatos.add(lblPass);
        lblPass.setBounds(120, 270, 250, 20);

        jScrollPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jScrollPane1FocusGained(evt);
            }
        });
        jScrollPane1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jScrollPane1PropertyChange(evt);
            }
        });

        panelCheckbox.setNextFocusableComponent(panelBusqueda);
        panelCheckbox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                panelCheckboxFocusGained(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Principal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        menuPrincipal.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        menuPrincipal.setText("Menú Principal");
        menuPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrincipalActionPerformed(evt);
            }
        });

        cbxM01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM01.setText("Expedientes");
        cbxM01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM01ActionPerformed(evt);
            }
        });

        cbxM01_01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM01_01.setText("Nuevo Expediente");
        cbxM01_01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM01_01ActionPerformed(evt);
            }
        });

        cbxM01_02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM01_02.setText("Modificar Expediente");
        cbxM01_02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM01_02ActionPerformed(evt);
            }
        });

        cbxM01_03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM01_03.setText("Eliminar Expediente");
        cbxM01_03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM01_03ActionPerformed(evt);
            }
        });

        cbxM02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM02.setText("Registro de Promociones");
        cbxM02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM02ActionPerformed(evt);
            }
        });

        cbxM03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM03.setText("Inicio de Trámites");

        cbxM04.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM04.setText("Registro de Autos");

        cbxM05.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM05.setText("Registro de Resoluciones de la Secretaría de Acuerdos");

        cbxM06.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM06.setText("Registro Sentencias - Resoluciones de Mesas de Trámite");

        cbxM07.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07.setText("Notificaciones");
        cbxM07.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxM07FocusGained(evt);
            }
        });
        cbxM07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07ActionPerformed(evt);
            }
        });

        cbxM07_01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07_01.setText("Carga de Notificaciones");
        cbxM07_01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07_01ActionPerformed(evt);
            }
        });

        cbxM07_02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07_02.setText("Descarga de Notificaciones");
        cbxM07_02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07_02ActionPerformed(evt);
            }
        });

        cbxM07_04.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07_04.setText("Carga Extra de Notificaciones");
        cbxM07_04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07_04ActionPerformed(evt);
            }
        });

        cbxM07_05.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07_05.setText("Descarga Extra de Notificaciones");
        cbxM07_05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07_05ActionPerformed(evt);
            }
        });

        cbxM07_06.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07_06.setText("Buscar y Eliminar Notificaciones Extra");
        cbxM07_06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07_06ActionPerformed(evt);
            }
        });

        cbxM08.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM08.setText("Archivar Expedientes");

        cbxM09.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM09.setText("Acumulación de Expedientes");

        cbxM07_03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM07_03.setText("Buscar y Eliminar Notificaciones");
        cbxM07_03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM07_03ActionPerformed(evt);
            }
        });

        cbxM01_04.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM01_04.setText("Ubicar Expediente");
        cbxM01_04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM01_04ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxM01_01, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM01_02, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM01_03, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM01_04, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(cbxM01, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(menuPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cbxM04, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxM03, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxM08, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxM09, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxM07, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxM05, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxM02, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(21, 21, 21)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbxM07_03, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbxM07_01, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM07_02, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM07_04, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbxM07_05, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(cbxM07_06, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(cbxM06, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuPrincipal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM01)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM01_01, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM01_02, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM01_03, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(cbxM01_04, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbxM02)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM03, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM04)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM05)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM06, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07_01, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07_02)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07_03)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07_04)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07_05, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM07_06)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM08, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM09)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Catálogos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        cbxM11_17.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_17.setText("Tipo de Sentencias");
        cbxM11_17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_17ActionPerformed(evt);
            }
        });

        cbxM11_16.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_16.setText("Tipo de Promociones");
        cbxM11_16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_16ActionPerformed(evt);
            }
        });

        cbxM11_15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_15.setText("Tipo de Pretensiones");
        cbxM11_15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_15ActionPerformed(evt);
            }
        });

        cbxM11_14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_14.setText("Tipo de Documentos");
        cbxM11_14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_14ActionPerformed(evt);
            }
        });

        cbxM11_13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_13.setText("Tipo de Autos");
        cbxM11_13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_13ActionPerformed(evt);
            }
        });

        cbxM11_12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_12.setText("Tipo de Autoridades");
        cbxM11_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_12ActionPerformed(evt);
            }
        });

        cbxM11_11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_11.setText("Tipo de Ámparos");
        cbxM11_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_11ActionPerformed(evt);
            }
        });

        cbxM11_10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_10.setText("Suspensiones");
        cbxM11_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_10ActionPerformed(evt);
            }
        });

        cbxM11_09.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_09.setText("Supuestos");
        cbxM11_09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_09ActionPerformed(evt);
            }
        });

        cbxM11_08.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_08.setText("Sentidos de Ejecutoria");
        cbxM11_08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_08ActionPerformed(evt);
            }
        });

        cbxM11_07.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_07.setText("Procedimientos");
        cbxM11_07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_07ActionPerformed(evt);
            }
        });

        cbxM11_06.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_06.setText("Municipios");
        cbxM11_06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_06ActionPerformed(evt);
            }
        });

        cbxM11_05.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_05.setText("Destinos");
        cbxM11_05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_05ActionPerformed(evt);
            }
        });

        cbxM11_04.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_04.setText("Autorizados");
        cbxM11_04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_04ActionPerformed(evt);
            }
        });

        cbxM11_03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_03.setText("Autoridades de Notificaciones Extra");
        cbxM11_03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_03ActionPerformed(evt);
            }
        });

        cbxM11_02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_02.setText("Autoridades");
        cbxM11_02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_02ActionPerformed(evt);
            }
        });

        cbxM11_01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM11_01.setText("Actos Impugnados");
        cbxM11_01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM11_01ActionPerformed(evt);
            }
        });

        menuCatalogos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        menuCatalogos.setText("Catálogos");
        menuCatalogos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCatalogosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxM11_05, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_06, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_07, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_08, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_09, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_10, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_11, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_13, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_14, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_15, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_16, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_17, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_02, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_01, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_03, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxM11_04, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(menuCatalogos, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuCatalogos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_01, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_02)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_03)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_04)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_05, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_06)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_07, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_08, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_09, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_10, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_11, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_12, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_16, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM11_17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Reportes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        menuReportes.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        menuReportes.setText("Reportes");
        menuReportes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuReportesActionPerformed(evt);
            }
        });

        cbxM12_01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_01.setText("Listado de Reportes de Catálogo");
        cbxM12_01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_01ActionPerformed(evt);
            }
        });

        cbxM12_02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_02.setText("Lista de Acuerdos");
        cbxM12_02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_02ActionPerformed(evt);
            }
        });

        cbxM12_03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_03.setText("Información de Expediente");
        cbxM12_03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_03ActionPerformed(evt);
            }
        });

        cbxM12_04.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_04.setText("Reportes de Promociones");
        cbxM12_04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_04ActionPerformed(evt);
            }
        });

        cbxM12_05.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_05.setText("Reportes de Autos");
        cbxM12_05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_05ActionPerformed(evt);
            }
        });

        cbxM12_06.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_06.setText("Reportes de Resoluciones");
        cbxM12_06.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_06ActionPerformed(evt);
            }
        });

        cbxM12_07.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_07.setText("Reportes de Sentencias");
        cbxM12_07.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_07ActionPerformed(evt);
            }
        });

        cbxM12_08.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_08.setText("Estado Procesal");
        cbxM12_08.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_08ActionPerformed(evt);
            }
        });

        cbxM12_09.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_09.setText("Reportes de Notificaciones");
        cbxM12_09.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_09ActionPerformed(evt);
            }
        });

        cbxM12_10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_10.setText("Reportes Estadísticos");
        cbxM12_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_10ActionPerformed(evt);
            }
        });

        cbxM12_11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_11.setText("Reportes Estadísticos de Notificaciones");
        cbxM12_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_11ActionPerformed(evt);
            }
        });

        cbxM12_12.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM12_12.setText("Reportes de Expedientes Acumulados");
        cbxM12_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM12_12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(menuReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxM12_11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxM12_12)
                                    .addComponent(cbxM12_02, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM12_05, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM12_03, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbxM12_06, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbxM12_04, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbxM12_07, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(cbxM12_08, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cbxM12_09, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cbxM12_01, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbxM12_10, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(menuReportes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_01, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_02)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_03)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_04)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_05)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_06)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_07)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_08)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_09)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxM12_12))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Utilerias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        jPanel4.setLayout(null);

        menuUtilerias.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        menuUtilerias.setText("Utilerias");
        menuUtilerias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuUtileriasActionPerformed(evt);
            }
        });
        jPanel4.add(menuUtilerias);
        menuUtilerias.setBounds(12, 17, 260, 23);

        cbxM13_01.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM13_01.setText("Calendario Oficial");
        cbxM13_01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM13_01ActionPerformed(evt);
            }
        });
        jPanel4.add(cbxM13_01);
        cbxM13_01.setBounds(33, 40, 230, 23);

        cbxM13_02.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM13_02.setText("Búsqueda de Personas");
        cbxM13_02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM13_02ActionPerformed(evt);
            }
        });
        jPanel4.add(cbxM13_02);
        cbxM13_02.setBounds(33, 63, 230, 20);

        cbxM13_03.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM13_03.setText("Usuarios");
        cbxM13_03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM13_03ActionPerformed(evt);
            }
        });
        jPanel4.add(cbxM13_03);
        cbxM13_03.setBounds(33, 83, 174, 23);

        cbxM13_04.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM13_04.setText("Respaldo de Base de Datos");
        cbxM13_04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM13_04ActionPerformed(evt);
            }
        });
        jPanel4.add(cbxM13_04);
        cbxM13_04.setBounds(33, 106, 239, 23);

        cbxM13_05.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbxM13_05.setText("Búsqueda de Autoridades");
        cbxM13_05.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxM13_05ActionPerformed(evt);
            }
        });
        jPanel4.add(cbxM13_05);
        cbxM13_05.setBounds(33, 130, 238, 23);

        javax.swing.GroupLayout panelCheckboxLayout = new javax.swing.GroupLayout(panelCheckbox);
        panelCheckbox.setLayout(panelCheckboxLayout);
        panelCheckboxLayout.setHorizontalGroup(
            panelCheckboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCheckboxLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelCheckboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCheckboxLayout.setVerticalGroup(
            panelCheckboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCheckboxLayout.createSequentialGroup()
                .addGroup(panelCheckboxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelCheckboxLayout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 96, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(panelCheckbox);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                    .addComponent(panelBusqueda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 664, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnF2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnF6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(panelBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void cbxM01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM01ActionPerformed
        habilitaPadre(cbxM01,cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04);
        menuPrincipal.setSelected(verificaHijos(cbxM01,cbxM02,cbxM03,cbxM04,cbxM05,cbxM06,cbxM07,cbxM08,cbxM09));
    }//GEN-LAST:event_cbxM01ActionPerformed

    private void cbxM01_01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM01_01ActionPerformed
        cbxM01.setSelected(verificaHijos(cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04));
    }//GEN-LAST:event_cbxM01_01ActionPerformed

    private void cbxM01_02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM01_02ActionPerformed
        cbxM01.setSelected(verificaHijos(cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04));
    }//GEN-LAST:event_cbxM01_02ActionPerformed

    private void cbxM01_03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM01_03ActionPerformed
        cbxM01.setSelected(verificaHijos(cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04));
    }//GEN-LAST:event_cbxM01_03ActionPerformed

    private void cbxM02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM02ActionPerformed
        cbxM01.setSelected(verificaHijos(cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04));
    }//GEN-LAST:event_cbxM02ActionPerformed

    private void cbxM07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07ActionPerformed
        habilitaPadre(cbxM07,cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06);
    }//GEN-LAST:event_cbxM07ActionPerformed

    private void cbxM07_01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07_01ActionPerformed
        cbxM07.setSelected(verificaHijos(cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06));
    }//GEN-LAST:event_cbxM07_01ActionPerformed

    private void cbxM07_02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07_02ActionPerformed
        cbxM07.setSelected(verificaHijos(cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06));
    }//GEN-LAST:event_cbxM07_02ActionPerformed

    private void cbxM07_04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07_04ActionPerformed
        cbxM07.setSelected(verificaHijos(cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06));
    }//GEN-LAST:event_cbxM07_04ActionPerformed

    private void cbxM07_05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07_05ActionPerformed
        cbxM07.setSelected(verificaHijos(cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06));
    }//GEN-LAST:event_cbxM07_05ActionPerformed

    private void cbxM07_06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07_06ActionPerformed
        cbxM07.setSelected(verificaHijos(cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06));
    }//GEN-LAST:event_cbxM07_06ActionPerformed

    private void menuCatalogosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCatalogosActionPerformed
        habilitaPadre(menuCatalogos,cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,cbxM11_10,
                cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17);
    }//GEN-LAST:event_menuCatalogosActionPerformed

    private void cbxM11_01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_01ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_01ActionPerformed

    private void cbxM11_02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_02ActionPerformed
       menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_02ActionPerformed

    private void cbxM11_03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_03ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_03ActionPerformed

    private void cbxM11_05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_05ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_05ActionPerformed

    private void cbxM11_14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_14ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_14ActionPerformed

    private void cbxM11_06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_06ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_06ActionPerformed

    private void cbxM11_07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_07ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_07ActionPerformed

    private void cbxM11_08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_08ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_08ActionPerformed

    private void cbxM11_09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_09ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_09ActionPerformed

    private void cbxM11_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_10ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_10ActionPerformed

    private void cbxM11_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_11ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_11ActionPerformed

    private void cbxM11_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_12ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_12ActionPerformed

    private void cbxM11_13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_13ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_13ActionPerformed

    private void cbxM11_15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_15ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_15ActionPerformed

    private void cbxM11_16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_16ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_16ActionPerformed

    private void cbxM11_17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_17ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_17ActionPerformed

    private void txtPassword1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassword1KeyReleased
        char passArray1[] = txtPassword1.getPassword();
        char passArray2[] = txtPassword2.getPassword();
        if(passArray1.length>0 && passArray2.length>0){
            String pass1 = new String(passArray1);
            String pass2 = new String(passArray2);
            if(pass1.equals(pass2)){
                lblPass.setText("");
            }else{
                lblPass.setText("* Las contraseñas no corresponden");
                lblPass.setForeground(Color.RED);
            }
        }else{
            lblPass.setText("");
        }
    }//GEN-LAST:event_txtPassword1KeyReleased

    private void txtPassword2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPassword2KeyReleased
        char passArray1[] = txtPassword1.getPassword();
        char passArray2[] = txtPassword2.getPassword();
        if(passArray1.length>0 && passArray2.length>0){
            String pass1 = new String(passArray1);
            String pass2 = new String(passArray2);
            if(pass1.equals(pass2)){
                lblPass.setText("");
            }else{
                lblPass.setText("* Las contraseñas no corresponden");
                lblPass.setForeground(Color.RED);
            }
        }else{
            lblPass.setText("");
        }
    }//GEN-LAST:event_txtPassword2KeyReleased

    private void btnF6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF6ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF6ActionPerformed

    private void cmbUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbUsuarioActionPerformed
        if(cmbUsuario.getItemCount()>0){
            desSeleccionarCheckboxes();
            if(cmbUsuario.getSelectedIndex()==0){
                txtUsuario.setText("");
                txtNombre.setText("");
                txtPaterno.setText("");
                txtMaterno.setText("");
                txtPassword1.setText("");
                txtPassword2.setText("");
            }else{
                SesionEntity usuario = (SesionEntity)cmbUsuario.getSelectedItem();
                txtUsuario.setText(usuario.getUsuario());
                txtNombre.setText(usuario.getNombre().getNombre());
                txtPaterno.setText(usuario.getNombre().getPaterno());
                txtMaterno.setText(usuario.getNombre().getMaterno());
                txtPassword1.setText(usuario.getPass());
                txtPassword2.setText(usuario.getPass());
                seleccionarCheckboxes(usuario.getPantallas());
            }
        }
    }//GEN-LAST:event_cmbUsuarioActionPerformed

    private void panelCheckboxFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_panelCheckboxFocusGained
        //JComponent focused = (JComponent) evt.getComponent();
        //jScrollPane1.scrollRectToVisible(focused.getBounds());
           
    }//GEN-LAST:event_panelCheckboxFocusGained

    private void jScrollPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jScrollPane1FocusGained
        //JComponent focused = (JComponent) evt.getComponent();
        //jScrollPane1.scrollRectToVisible(focused.getBounds());
    }//GEN-LAST:event_jScrollPane1FocusGained

    private void jScrollPane1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jScrollPane1PropertyChange
        //JComponent focused = (JComponent) evt.getComponent();
        //jScrollPane1.scrollRectToVisible(focused.getBounds());
    }//GEN-LAST:event_jScrollPane1PropertyChange

    private void cbxM07FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxM07FocusGained
        //JComponent focused = (JComponent) evt.getComponent();
        //jScrollPane1.scrollRectToVisible(cbxM08.getBounds());
    }//GEN-LAST:event_cbxM07FocusGained

    private void menuPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrincipalActionPerformed
        habilitaPadre(menuPrincipal,cbxM01,cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04,cbxM02,cbxM03,cbxM04,cbxM05,cbxM06,cbxM07,
                cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06,cbxM08,cbxM09);
    }//GEN-LAST:event_menuPrincipalActionPerformed

    private void menuReportesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuReportesActionPerformed
        habilitaPadre(menuReportes,cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12);
    }//GEN-LAST:event_menuReportesActionPerformed

    private void cbxM12_01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_01ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_01ActionPerformed

    private void cbxM12_02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_02ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_02ActionPerformed

    private void menuUtileriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuUtileriasActionPerformed
        habilitaPadre(menuUtilerias,cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05);
    }//GEN-LAST:event_menuUtileriasActionPerformed

    private void cbxM13_01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM13_01ActionPerformed
        menuUtilerias.setSelected(verificaHijos(cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05));
    }//GEN-LAST:event_cbxM13_01ActionPerformed

    private void cbxM13_02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM13_02ActionPerformed
        menuUtilerias.setSelected(verificaHijos(cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05));
    }//GEN-LAST:event_cbxM13_02ActionPerformed

    private void cbxM13_03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM13_03ActionPerformed
        menuUtilerias.setSelected(verificaHijos(cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05));
    }//GEN-LAST:event_cbxM13_03ActionPerformed

    private void cbxM11_04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM11_04ActionPerformed
        menuCatalogos.setSelected(verificaHijos(cbxM11_01,cbxM11_02,cbxM11_03,cbxM11_04,cbxM11_05,cbxM11_06,cbxM11_07,cbxM11_08,cbxM11_09,
                cbxM11_10,cbxM11_11,cbxM11_12,cbxM11_13,cbxM11_14,cbxM11_15,cbxM11_16,cbxM11_17));
    }//GEN-LAST:event_cbxM11_04ActionPerformed

    private void cbxM12_03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_03ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_03ActionPerformed

    private void cbxM12_04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_04ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_04ActionPerformed

    private void cbxM12_05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_05ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_05ActionPerformed

    private void cbxM12_06ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_06ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_06ActionPerformed

    private void cbxM12_07ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_07ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_07ActionPerformed

    private void cbxM07_03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM07_03ActionPerformed
        cbxM07.setSelected(verificaHijos(cbxM07_01,cbxM07_02,cbxM07_03,cbxM07_04,cbxM07_05,cbxM07_06));
    }//GEN-LAST:event_cbxM07_03ActionPerformed

    private void cbxM12_08ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_08ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_08ActionPerformed

    private void cbxM12_09ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_09ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_09ActionPerformed

    private void cbxM13_04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM13_04ActionPerformed
         menuUtilerias.setSelected(verificaHijos(cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05));
    }//GEN-LAST:event_cbxM13_04ActionPerformed

    private void cbxM12_10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_10ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_10ActionPerformed

    private void cbxM12_11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_11ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_11ActionPerformed

    private void cbxM12_12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM12_12ActionPerformed
        menuReportes.setSelected(verificaHijos(cbxM12_01,cbxM12_02,cbxM12_03,cbxM12_04,cbxM12_05,cbxM12_06,cbxM12_07,cbxM12_08,cbxM12_09,cbxM12_10,cbxM12_11,cbxM12_12));
    }//GEN-LAST:event_cbxM12_12ActionPerformed

    private void cbxM13_05ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM13_05ActionPerformed
        menuUtilerias.setSelected(verificaHijos(cbxM13_01,cbxM13_02,cbxM13_03,cbxM13_04,cbxM13_05));
    }//GEN-LAST:event_cbxM13_05ActionPerformed

    private void cbxM01_04ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxM01_04ActionPerformed
        cbxM01.setSelected(verificaHijos(cbxM01_01,cbxM01_02,cbxM01_03,cbxM01_04));
    }//GEN-LAST:event_cbxM01_04ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnF6;
    private javax.swing.JCheckBox cbxM01;
    private javax.swing.JCheckBox cbxM01_01;
    private javax.swing.JCheckBox cbxM01_02;
    private javax.swing.JCheckBox cbxM01_03;
    private javax.swing.JCheckBox cbxM01_04;
    private javax.swing.JCheckBox cbxM02;
    private javax.swing.JCheckBox cbxM03;
    private javax.swing.JCheckBox cbxM04;
    private javax.swing.JCheckBox cbxM05;
    private javax.swing.JCheckBox cbxM06;
    private javax.swing.JCheckBox cbxM07;
    private javax.swing.JCheckBox cbxM07_01;
    private javax.swing.JCheckBox cbxM07_02;
    private javax.swing.JCheckBox cbxM07_03;
    private javax.swing.JCheckBox cbxM07_04;
    private javax.swing.JCheckBox cbxM07_05;
    private javax.swing.JCheckBox cbxM07_06;
    private javax.swing.JCheckBox cbxM08;
    private javax.swing.JCheckBox cbxM09;
    private javax.swing.JCheckBox cbxM11_01;
    private javax.swing.JCheckBox cbxM11_02;
    private javax.swing.JCheckBox cbxM11_03;
    private javax.swing.JCheckBox cbxM11_04;
    private javax.swing.JCheckBox cbxM11_05;
    private javax.swing.JCheckBox cbxM11_06;
    private javax.swing.JCheckBox cbxM11_07;
    private javax.swing.JCheckBox cbxM11_08;
    private javax.swing.JCheckBox cbxM11_09;
    private javax.swing.JCheckBox cbxM11_10;
    private javax.swing.JCheckBox cbxM11_11;
    private javax.swing.JCheckBox cbxM11_12;
    private javax.swing.JCheckBox cbxM11_13;
    private javax.swing.JCheckBox cbxM11_14;
    private javax.swing.JCheckBox cbxM11_15;
    private javax.swing.JCheckBox cbxM11_16;
    private javax.swing.JCheckBox cbxM11_17;
    private javax.swing.JCheckBox cbxM12_01;
    private javax.swing.JCheckBox cbxM12_02;
    private javax.swing.JCheckBox cbxM12_03;
    private javax.swing.JCheckBox cbxM12_04;
    private javax.swing.JCheckBox cbxM12_05;
    private javax.swing.JCheckBox cbxM12_06;
    private javax.swing.JCheckBox cbxM12_07;
    private javax.swing.JCheckBox cbxM12_08;
    private javax.swing.JCheckBox cbxM12_09;
    private javax.swing.JCheckBox cbxM12_10;
    private javax.swing.JCheckBox cbxM12_11;
    private javax.swing.JCheckBox cbxM12_12;
    private javax.swing.JCheckBox cbxM13_01;
    private javax.swing.JCheckBox cbxM13_02;
    private javax.swing.JCheckBox cbxM13_03;
    private javax.swing.JCheckBox cbxM13_04;
    private javax.swing.JCheckBox cbxM13_05;
    private javax.swing.JComboBox cmbUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblPass;
    private javax.swing.JCheckBox menuCatalogos;
    private javax.swing.JCheckBox menuPrincipal;
    private javax.swing.JCheckBox menuReportes;
    private javax.swing.JCheckBox menuUtilerias;
    private javax.swing.JPanel panelBusqueda;
    private javax.swing.JPanel panelCheckbox;
    private javax.swing.JPanel panelDatos;
    private javax.swing.JTextField txtMaterno;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JPasswordField txtPassword1;
    private javax.swing.JPasswordField txtPassword2;
    private javax.swing.JTextField txtPaterno;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
