package interfaz.utilidades;

import baseGeneral.JTextMascara;
import baseGeneral.Tabla;
import bl.utilidades.BusquedaPersonasBl;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
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
public class BusquedaPersonas extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    BusquedaPersonasBl bl = new BusquedaPersonasBl();
    
    Tabla tabla;
    
    String [][]tablas = {
        {"ACTORES","pri_actores"},
        {"TERCEROS ACTORES","pri_terceros_actores"},
        {"PERSONAS AJENAS","pri_personas_ajenas"}};
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public BusquedaPersonas() {
        initComponents();
    }

    public BusquedaPersonas(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        formatearCampos();
        declaracionTabla();
        botonEsc();
    }
    
    private void formatearCampos(){
        txtFiltro1.setDocument(new JTextMascara(90,true,"todo"));
        txtFiltro2.setDocument(new JTextMascara(90,true,"todo"));
        txtFiltro3.setDocument(new JTextMascara(90,true,"todo"));
    }
    
    private void declaracionTabla(){
        tabla = new Tabla(jTable1);
        String[][] titulos={
            {"nombre","paterno","materno","tabla","expediente"}
           ,{"Nombre(s)","A. Paterno","A. Materno","Lugar en que aparece","expediente"}
        };
        tabla.setTabla(titulos);
        tabla.setDefaultValues("","","","","");
        tabla.setAnchoColumnas(180,180,180,130,100);
        tabla.setAlturaRow(30);
        tabla.alinear(3,2);
        tabla.alinear(4,2);
        tabla.cebraTabla();
    }
    
    private String seleccionCombo(javax.swing.JComboBox combo, javax.swing.JTextField txt){
        String bus = "";
        /*
        0 -----------------
        1 ES IGUAL A    campo = 'texto'
        2 EMPIEZA CON   campo like texto%
        3 TERMINA CON   campo like %texto
        4 CONTIENE      campo like %texto%
        */
        switch(combo.getSelectedIndex()){
            case 0: bus = ""; break;
            case 1: bus = txt.getText();break;
            case 2: bus = txt.getText()+"%";break;
            case 3: bus = "%"+txt.getText();break;
            case 4: bus = "%"+txt.getText()+"%";break;  
        }
        return bus;
    }
    
    private String getOperador(javax.swing.JComboBox combo){
        String bus = "";
        switch(combo.getSelectedIndex()){
            case 0: bus = ""; break;
            case 1: bus = "=";break;
            case 2: bus = "LIKE";break;
            case 3: bus = "LIKE";break;
            case 4: bus = "LIKE";break;  
        }
        return bus;
    }
    
    private void combos(){
        tabla.clearRows();
        
        String valor1 = seleccionCombo(cmbFiltro1,txtFiltro1);
        String valor2 = seleccionCombo(cmbFiltro2,txtFiltro2);
        String valor3 = seleccionCombo(cmbFiltro3,txtFiltro3);
        
        ArrayList array1 = bl.getNombres(tablas[0][0], tablas[0][1], valor1, valor2, valor3, 
                    getOperador(cmbFiltro1), getOperador(cmbFiltro2), getOperador(cmbFiltro3));
        ArrayList array2 = bl.getNombres(tablas[1][0], tablas[1][1], valor1, valor2, valor3, 
                    getOperador(cmbFiltro1), getOperador(cmbFiltro2), getOperador(cmbFiltro3));
        ArrayList array3 = bl.getNombres(tablas[2][0], tablas[2][1], valor1, valor2, valor3, 
                    getOperador(cmbFiltro1), getOperador(cmbFiltro2), getOperador(cmbFiltro3));
        
        for(int i=0; i<array1.size(); i++){
            tabla.addRenglonArray((Object[])array1.get(i));
        }
        
        for(int i=0; i<array2.size(); i++){
            tabla.addRenglonArray((Object[])array2.get(i));
        }
        
        for(int i=0; i<array3.size(); i++){
            tabla.addRenglonArray((Object[])array3.get(i));
        }
        
        if(tabla.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se encontraron datos coincididentes de acuerdo a los filtros de búsqueda", "Aviso", 1);
            txtFiltro1.requestFocus();
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
        tabla.clearRows();
        cmbFiltro1.setSelectedIndex(0);
        cmbFiltro2.setSelectedIndex(0);
        cmbFiltro3.setSelectedIndex(0);
        txtFiltro1.setText("");
        txtFiltro2.setText("");
        txtFiltro3.setText("");
        txtFiltro1.requestFocus();
    }
    
    private void botonF2(){
        if(cmbFiltro1.getSelectedIndex()==0 && cmbFiltro2.getSelectedIndex()==0 && cmbFiltro3.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un tipo de filtro para la búsqueda", "Aviso", 2);
            cmbFiltro1.requestFocus();
        }else{
            combos();
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
        btnF12 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltro1 = new javax.swing.JTextField();
        cmbFiltro1 = new javax.swing.JComboBox();
        cmbFiltro2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtFiltro2 = new javax.swing.JTextField();
        cmbFiltro3 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        txtFiltro3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Búsqueda de  personas");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Busqueda_personas.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Esc - Cancelar");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtro de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nombre");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(80, 30, 90, 30);

        txtFiltro1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtFiltro1);
        txtFiltro1.setBounds(320, 30, 250, 30);

        cmbFiltro1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbFiltro1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-----------------", "ES IGUAL A", "EMPIEZA CON", "TERMINA CON", "CONTIENE" }));
        jPanel1.add(cmbFiltro1);
        cmbFiltro1.setBounds(180, 30, 130, 30);

        cmbFiltro2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbFiltro2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-----------------", "ES IGUAL A", "EMPIEZA CON", "TERMINA CON", "CONTIENE" }));
        jPanel1.add(cmbFiltro2);
        cmbFiltro2.setBounds(180, 70, 130, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("A. Paterno");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(80, 70, 90, 30);

        txtFiltro2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtFiltro2);
        txtFiltro2.setBounds(320, 70, 250, 30);

        cmbFiltro3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbFiltro3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-----------------", "ES IGUAL A", "EMPIEZA CON", "TERMINA CON", "CONTIENE" }));
        jPanel1.add(cmbFiltro3);
        cmbFiltro3.setBounds(180, 110, 130, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("A. Materno");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(80, 110, 90, 30);

        txtFiltro3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(txtFiltro3);
        txtFiltro3.setBounds(320, 110, 250, 30);

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
        jTable1.setFocusable(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("F2 - Buscar");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 802, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        botonEsc();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        botonF2();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JComboBox cmbFiltro1;
    private javax.swing.JComboBox cmbFiltro2;
    private javax.swing.JComboBox cmbFiltro3;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtFiltro1;
    private javax.swing.JTextField txtFiltro2;
    private javax.swing.JTextField txtFiltro3;
    // End of variables declaration//GEN-END:variables
}
