package interfaz.utilidades;

import baseSistema.Utilidades;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class RespaldarBase extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    Utilidades u = new Utilidades();
    
    private int BUFFER = 10485760;  
    //para guardar en memmoria
    private StringBuffer temp = null;
    //para guardar el archivo SQL
    private FileWriter  fichero = null;
    private PrintWriter pw = null;
    
    /**
     * Creates new form ArchivarExpedientes
     */
    public RespaldarBase() {
        initComponents();
    }

    public RespaldarBase(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        botonEsc();
    }
    
    private static void backup(){
        try{
            Process p = Runtime.getRuntime().exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump -u root -marco tribunal");

      InputStream is = p.getInputStream();
      FileOutputStream fos = new FileOutputStream("c:/marco/Tribunal.sql");
      byte[] buffer = new byte[1000];

      int leido = is.read(buffer);
      while (leido > 0) {
         fos.write(buffer, 0, leido);
         leido = is.read(buffer);
      }

      fos.close();

   } catch (Exception e) {
      e.printStackTrace();
   }
}
    
    private  boolean CrearBackup(String host, String port, String user, String password, String db, String file_backup){
    
        boolean ok=false;
    try{       
        //sentencia para crear el BackUp
         Process run = Runtime.getRuntime().exec(
        "C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump --host=" + host + " --port=" + port +
        " --user=" + user + " --password=" + password +
        " --complete-insert --extended-insert " + db);
        //se guarda en memoria el backup
        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        temp = new StringBuffer();
        int count;
        char[] cbuf = new char[BUFFER];
        while ((count = br.read(cbuf, 0, BUFFER)) != -1)
            temp.append(cbuf, 0, count);
        br.close();
        in.close();        
        /* se crea y escribe el archivo SQL */
        fichero = new FileWriter(file_backup);
        pw = new PrintWriter(fichero);                                                    
        pw.println(temp.toString());  
        ok=true;
   }
    catch (Exception ex){
            ex.printStackTrace();
    } finally {
       try {           
         if (null != fichero)
              fichero.close();
       } catch (Exception e2) {
           e2.printStackTrace();
       }
    }   
    return ok; 
 }  
    
    private void crearBackup(String archivo){
        try{
            Runtime runtime = Runtime.getRuntime();
            FileWriter fw = new FileWriter(new File(archivo));
            Process child = runtime.exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump --opt --complete-insert --extended-insert "
                    + "--password=marco --user=root --databases tribunal -R");
            //Process child = runtime.exec("C:/Program Files/MySQL/MySQL Server 5.7/bin/mysqldump --opt --complete-insert --password=marco --user=root --databases tribunal -R");
            InputStreamReader irs = new InputStreamReader(child.getInputStream());
            BufferedReader br = new BufferedReader(irs);
            String line;
            while( (line=br.readLine()) != null ) {
                fw.write(line + "\n");
            }
            fw.close();
            irs.close();
            br.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error no se genero el archivo por el siguiente motivo:"+e.getMessage(), "Verificar",JOptionPane.ERROR_MESSAGE);
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
    private void botonEsc(){}
    
    private void botonF2(){
        if(txtPath.getText().length()>0){
            if(new File(txtPath.getText()).isDirectory()){
                String fileBD = txtPath.getText()+"\\Tribunal"+u.getFormatoFechaString()+".sql";
                crearBackup(fileBD);
                //CrearBackup("localhost", "3306", "root", "marco", "tribunal","c:/marco/Tribunal.sql");
                //backup();
            }else{
                //error
            }
        }else{
            //error
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
        jPanel1 = new javax.swing.JPanel();
        txtPath = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setClosable(true);
        setTitle("Respaldo de Base de Datos");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/respaldo_bd.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ubicación donde se guardará el respaldo de la base de datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel1.setLayout(null);

        txtPath.setEditable(false);
        txtPath.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtPath.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jPanel1.add(txtPath);
        txtPath.setBounds(20, 30, 560, 30);

        jButton3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton3.setText("...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(583, 30, 50, 30);

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton2.setText("F2 - Respaldar");
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
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 645, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        botonF2();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setCurrentDirectory(new File("c:/windows/"));
        int respuesta = fc.showOpenDialog(this);
        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivoElegido = fc.getSelectedFile();
            txtPath.setText(archivoElegido.getAbsolutePath());
        }
    }//GEN-LAST:event_jButton3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnF12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtPath;
    // End of variables declaration//GEN-END:variables
}
