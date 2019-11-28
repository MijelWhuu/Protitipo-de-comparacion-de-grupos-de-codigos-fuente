package interfaz.utilidades;

import baseSistema.EachRowEditor;
import baseSistema.Utilidades;
//import bl.notificaciones.NotificacionesNewBl;
import bl.utilidades.BusquedaPersonasBl;
import entitys.RegistroAutoEntity;
import entitys.SesionEntity;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author mavg
 */
public class PruebaComponentes extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    //BusquedaPersonasBl bl = new BusquedaPersonasBl();
    Utilidades u = new Utilidades();
    //NotificacionesNewBl bl = new NotificacionesNewBl();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public PruebaComponentes() {
        initComponents();
    }

    public PruebaComponentes(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblAuto.setVisible(false);
        jXCollapsiblePane1.setLayout(new BorderLayout());
        jXCollapsiblePane1.add(jPanel2, BorderLayout.CENTER);
        btnColapsar.addActionListener(jXCollapsiblePane1.getActionMap().get(org.jdesktop.swingx.JXCollapsiblePane.TOGGLE_ACTION));
        //mapeoTeclas();
    }
    
    private void llenaTabla(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 1;
            }
        };
        
        //ArrayList array = bl.getPartesTemporal();
        ArrayList array = new ArrayList();
        Object [][]datos = generaObjectBidi(array);
        
        Object []titulos = new Object[] {"Notificar","Nombre","Tipo de Notificación","fecha_notificacion","Observaciones","id_notificacion","id_parte","tipo_parte"};
        dm.setDataVector(datos, titulos);        
        jTable1.setModel(dm);
        javax.swing.JComboBox comboBox = new javax.swing.JComboBox();
        comboBox.addItem("[Elija una opción]");
        comboBox.addItem("PERSONAL");
        comboBox.addItem("OFICIO");
        comboBox.addItem("CORREO");
        comboBox.addItem("LISTA");
        /*comboBox.addComponentListener(new ComponentAdapter(){
            @Override
            public void componentShown(ComponentEvent e){
                final JComponent c = (JComponent) e.getSource();
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run(){
                        c.requestFocus();
                        System.out.println(c);
                        if(c instanceof javax.swing.JComboBox){
                            System.out.println("a");
                        }
                    }
                });
            }
        });*/
        
        javax.swing.JCheckBox cbx = new javax.swing.JCheckBox();
        
        EachRowEditor rowEditor = new EachRowEditor(jTable1);
        for(int i=0; i<array.size(); i++){
            rowEditor.setEditorAt(i, new DefaultCellEditor(comboBox));
        }
        jTable1.getColumn("Tipo de Notificación").setCellEditor(rowEditor);
        
        
        jTable1.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());
        EachRowEditor rowEditor2 = new EachRowEditor(jTable1);
        for(int i=0; i<array.size(); i++){
            rowEditor2.setEditorAt(i, new DefaultCellEditor(cbx));
        }
        jTable1.getColumn("Notificar").setCellEditor(rowEditor2);
        
        
        jTable1.getColumnModel().getColumn(1).setCellRenderer(new TableCellLongTextRenderer());
        
        ocultarColumnas(5,6,7);
        setAnchoColumnas(60,300,130,130,300,50,50,50);
        //jTable1.setColumnSelectionAllowed(false);
        jTable1.setColumnSelectionAllowed(false);
        jTable1.setRowSelectionAllowed(true);
        
    }
    
    public void ocultarColumnas(Integer ...columnas){
        for(int i=0; i<columnas.length; i++){
            jTable1.getColumnModel().getColumn(columnas[i]).setWidth(0);
            jTable1.getColumnModel().getColumn(columnas[i]).setMinWidth(0);
            jTable1.getColumnModel().getColumn(columnas[i]).setMaxWidth(0);
        }
    }
    
    public void setAnchoColumnas(Integer ...columnas){
        jTable1.setAutoResizeMode(0);
        if (jTable1.getColumnCount()>0){
            for (int i=0;i<columnas.length;i++){
                if (jTable1.getColumnCount()>i)
                    jTable1.getColumnModel().getColumn(i).setPreferredWidth(columnas[i]);
                else break;
            }
        }
    }
    
    private Object[][] generaObjectBidi(ArrayList array){
        
        Object [][]datos = new Object[array.size()][8];
        for(int j=0; j<array.size(); j++){
            String []info = (String[]) array.get(j);
            datos[j][0] = false;
            datos[j][1] = info[1];
            datos[j][2] = "[Elija una opción]";
            datos[j][3] = "";
            datos[j][4] = "";
            datos[j][5] = "";
            datos[j][6] = info[0];
            datos[j][7] = info[2];
        }
        return datos;
    }
    
    
    private void validaBusquedaFolio(){
        u.cleanComponent(lblAuto,txtInfoFolio,txtInfoAnio,txtInfoExpediente,txtInfoFAcuerdos,txtInfoFAuto,txtInfoFAuto,txtInfoNombre);
        //limpiar la tabla
        if(txtBuscarFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio del Auto a buscar", "Alerta", 2);
            txtBuscarFolio.requestFocus();
        }else{
            Integer folio = new Integer(txtBuscarFolio.getText());
            Integer anio = cal.get(Calendar.YEAR);
            RegistroAutoEntity reg = null;//bl.getAuto(folio, anio);
            if(reg!=null){
                cargaInfoAuto(reg);
                txtBuscarFolio.setText("");
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio del Auto no existe", "Alerta", 2);
                txtBuscarFolio.requestFocus();
            }
        }
    }
    
    private void cargaInfoAuto(RegistroAutoEntity reg){
        lblAuto.setText(reg.getId_registro_auto());
        txtInfoFolio.setText(reg.getFolio().toString());
        txtInfoAnio.setText(reg.getAnio().toString());
        txtInfoExpediente.setText(reg.getExp().getExpediente());
        txtInfoFAcuerdos.setText(reg.getFecha_acuerdo());
        txtInfoFAuto.setText(reg.getFecha_auto());
        /*txtInfoFAcuerdos.setText(u.dateToString(reg.getFecha_acuerdo()));
        txtInfoFAuto.setText(u.dateToString(reg.getFecha_auto()));*/
        txtInfoNombre.setText(reg.getTipo_auto().getNombre());
        llenaTabla();
        if(jXCollapsiblePane1.isCollapsed()){
            btnColapsar.doClick();
        }
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
    
        /////Para cambiar el focus cuando se encuentra en una tabla
        jTable1.getActionMap().put("Accion_tab",Accion_tab());
        jTable1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        /////
    }
    
    public AbstractAction Accion_Esc(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F5(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF5();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F7(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF7();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF12();}};}
    
    //Para cambiar el focus cuando se encuentra en una tabla
    public AbstractAction Accion_tab(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();  
    }};}
    /////
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de botones">
    private void botonEsc(){}
    
    private void botonF2(){}
    
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

        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtInfoFolio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtInfoAnio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtInfoExpediente = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtInfoFAuto = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtInfoFAcuerdos = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtInfoNombre = new javax.swing.JTextArea();
        lblAuto = new javax.swing.JLabel();
        jXCollapsiblePane1 = new org.jdesktop.swingx.JXCollapsiblePane();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtBuscarFolio = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btnColapsar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Auto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Folio");

        txtInfoFolio.setEditable(false);
        txtInfoFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtInfoFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Año");

        txtInfoAnio.setEditable(false);
        txtInfoAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtInfoAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Expediente");

        txtInfoExpediente.setEditable(false);
        txtInfoExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtInfoExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("F. del Auto");

        txtInfoFAuto.setEditable(false);
        txtInfoFAuto.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtInfoFAuto.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("F. de Lista de Acuerdos");

        txtInfoFAcuerdos.setEditable(false);
        txtInfoFAcuerdos.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtInfoFAcuerdos.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Tipo de Auto");

        txtInfoNombre.setEditable(false);
        txtInfoNombre.setColumns(20);
        txtInfoNombre.setRows(5);
        txtInfoNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInfoNombreKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(txtInfoNombre);

        lblAuto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addComponent(txtInfoExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(84, 84, 84)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtInfoFAuto, javax.swing.GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE)
                            .addComponent(txtInfoFAcuerdos))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(174, 174, 174)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtInfoFolio, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(txtInfoAnio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtInfoFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInfoAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInfoExpediente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInfoFAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInfoFAcuerdos, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setClosable(true);
        setTitle("Búsqueda de  personas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        setMinimumSize(new java.awt.Dimension(814, 555));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jXCollapsiblePane1.setCollapsed(true);
        getContentPane().add(jXCollapsiblePane1, java.awt.BorderLayout.PAGE_END);

        jPanel3.setLayout(new org.jdesktop.swingx.VerticalLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Busqueda_personas.jpg"))); // NOI18N
        jLabel2.setText("jLabel2");
        jLabel2.setPreferredSize(new java.awt.Dimension(34, 50));
        jPanel3.add(jLabel2);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel5.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Folio  de la Promoción");
        jPanel5.add(jLabel3);
        jLabel3.setBounds(20, 30, 150, 30);

        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        jPanel5.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(170, 30, 100, 30);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Buscar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel5.add(jButton1);
        jButton1.setBounds(290, 30, 140, 30);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel4);

        btnColapsar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnColapsar.setText("Mostrar Información de la Promoción");
        btnColapsar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnColapsar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColapsarActionPerformed(evt);
            }
        });
        jPanel3.add(btnColapsar);

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });
        jPanel1.setLayout(null);

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setRowHeight(30);
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jTable1);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(10, 20, 780, 340);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 814, 551);
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel1ComponentResized

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(jXCollapsiblePane1.isCollapsed()){
            jPanel2.setPreferredSize(jPanel1.getSize());
        }else{
            int width = getContentPane().getSize().width;
            int height = getContentPane().getSize().height - jPanel3.getHeight();
            jPanel2.setPreferredSize(new Dimension(width,height));
        }
    }//GEN-LAST:event_formComponentResized

    private void txtInfoNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInfoNombreKeyPressed
        if(evt.getKeyChar()==KeyEvent.VK_TAB){
            evt.consume();
            txtBuscarFolio.requestFocus();
        }
        if(evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB){
            txtInfoNombre.transferFocusBackward();
        }
    }//GEN-LAST:event_txtInfoNombreKeyPressed

    private void btnColapsarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColapsarActionPerformed
        if(jXCollapsiblePane1.isCollapsed()){
            btnColapsar.setText("Mostrar Información del Auto");
        }else{
            btnColapsar.setText("Ocultar Información del Auto");
            
        }
    }//GEN-LAST:event_btnColapsarActionPerformed

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            validaBusquedaFolio();
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        validaBusquedaFolio();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnColapsar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private org.jdesktop.swingx.JXCollapsiblePane jXCollapsiblePane1;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JTextField txtBuscarFolio;
    private javax.swing.JTextField txtInfoAnio;
    private javax.swing.JTextField txtInfoExpediente;
    private javax.swing.JTextField txtInfoFAcuerdos;
    private javax.swing.JTextField txtInfoFAuto;
    private javax.swing.JTextField txtInfoFolio;
    private javax.swing.JTextArea txtInfoNombre;
    // End of variables declaration//GEN-END:variables
}

class CheckBoxRenderer extends javax.swing.JCheckBox implements TableCellRenderer {

    CheckBoxRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(isSelected){
            setForeground(table.getSelectionForeground());
            //super.setBackground(table.getSelectionBackground());
            setBackground(table.getSelectionBackground());
        }else{
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        setSelected((value != null && ((Boolean) value)));
        //setSelected((value != null && ((Boolean) value).booleanValue()));
        return this;
    }
}

class TableCellLongTextRenderer extends javax.swing.JTextArea implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        this.setText((String)value);
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
        this.setFont(new Font("Tahoma", Font.PLAIN, 13));
        if(isSelected){
            this.setBackground((Color)UIManager.get("Table.selectionBackground"));
        }
        //set the JTextArea to the width of the table column
        setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
        //if(table.getRowHeight(row) != getPreferredSize().height){
        if(table.getRowHeight(row) < getPreferredSize().height){
            //set the height of the table row to the calculated height of the JTextArea
            table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
}
