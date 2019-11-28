package interfaz.principal;

import baseGeneral.Utilerias;
import bl.principal.InicioSesionBl;
import entitys.SesionEntity;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 *
 * @author mavg
 */
public class InicioSesion extends javax.swing.JFrame {

    Utilerias u = new Utilerias();
    InicioSesionBl bl = new InicioSesionBl();
    
    public InicioSesion() {
        initComponents();
    }

    private void LimpiaTxt(){
        u.LimpiaTxt(jPanel2);
        txtUsuario.requestFocus();
    }

    private void BloqMayus(){
        Boolean BloqMayus = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        if(BloqMayus){
            lblBloqMayus.setForeground(Color.black);
            lblBloqMayus.setIcon(new ImageIcon(getClass().getResource("/imagenes/alerta.png")));
            lblBloqMayus.setIconTextGap(6);
            lblBloqMayus.setText("Bloq Mayus Activado");
        }else{
            lblBloqMayus.setIcon(null);
            lblBloqMayus.setText("");
        }
    }
    
    private void showMDI(SesionEntity sesion){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Main.mdi = new MDI(sesion);
        Main.mdi.setSize(screenSize);
        Main.mdi.setBounds(0, 0, screenSize.width, screenSize.height);
        Main.mdi.setVisible(true);
        Main.mdi.toFront();
        this.dispose();
    }
    
    public void VerificaUsuario(){
        String usuario = txtUsuario.getText();
        char passArray1[] = txtPassword.getPassword();
        if(usuario.length()>0 && passArray1.length>0){
            SesionEntity sesion = bl.VerificaUsuario(usuario, new String(passArray1));
            if(sesion != null){
                try{
                    /*java.sql.Date fechaCero = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01").getTime());
                    java.sql.Date fechaActual = new java.sql.Date(new java.util.Date().getTime());
                    SesionEntity sesion = new SesionEntity();
                    sesion.setId_usuario("U0001");
                    sesion.setNombre(new NombreEntity("admin","admin","admin"));
                    sesion.setUsuario("admin");
                    sesion.setPass("admin");
                    sesion.setFechaActual(fechaActual);
                    sesion.setFechaCero(fechaCero);
                    sesion.setF_alta(fechaActual);
                    sesion.setF_cambio(fechaCero);*/
                    showMDI(sesion);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                lblBloqMayus.setIcon(new ImageIcon(getClass().getResource("/imagenes/error.png")));
                lblBloqMayus.setForeground(Color.red);
                lblBloqMayus.setText("Usuario o Contraseña Incorrecto");
                txtUsuario.requestFocus();
            }
        }else{
            lblBloqMayus.setIcon(new ImageIcon(getClass().getResource("/imagenes/error.png")));
            lblBloqMayus.setForeground(Color.red);
            lblBloqMayus.setText("Ingresar Usuario y Contraseña");
            txtUsuario.requestFocus();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        lblBloqMayus = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnAcceder = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel2.setLayout(null);

        lblBloqMayus.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel2.add(lblBloqMayus);
        lblBloqMayus.setBounds(150, 300, 150, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Usario");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(230, 220, 85, 30);

        txtUsuario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusGained(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });
        jPanel2.add(txtUsuario);
        txtUsuario.setBounds(320, 220, 160, 30);

        txtPassword.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPasswordFocusGained(evt);
            }
        });
        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPasswordKeyPressed(evt);
            }
        });
        jPanel2.add(txtPassword);
        txtPassword.setBounds(320, 260, 160, 30);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/inicio2.jpg"))); // NOI18N
        jPanel2.add(jLabel1);
        jLabel1.setBounds(0, 0, 500, 220);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Contraseña");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(230, 260, 85, 30);

        btnAcceder.setText("Acceder");
        btnAcceder.setBorder(null);
        btnAcceder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccederActionPerformed(evt);
            }
        });
        jPanel2.add(btnAcceder);
        btnAcceder.setBounds(310, 300, 80, 30);

        btnSalir.setText("Salir");
        btnSalir.setBorder(null);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jPanel2.add(btnSalir);
        btnSalir.setBounds(400, 300, 80, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        BloqMayus();
        LimpiaTxt();
    }//GEN-LAST:event_formComponentShown

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_CAPS_LOCK){
            BloqMayus();
        }else if(txtUsuario.getText().length()>0){
            lblBloqMayus.setIcon(null);
            lblBloqMayus.setText("");
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtPasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_CAPS_LOCK){
            BloqMayus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            lblBloqMayus.setIcon(new ImageIcon(getClass().getResource("/imagenes/cargando.gif")));
            lblBloqMayus.setForeground(Color.black);
            lblBloqMayus.setIconTextGap(6);
            lblBloqMayus.setText("Accesando");
            VerificaUsuario();
        }
    }//GEN-LAST:event_txtPasswordKeyPressed

    private void txtUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusGained
         if(txtUsuario.getText().length()>0){
            txtUsuario.selectAll();
        }
    }//GEN-LAST:event_txtUsuarioFocusGained

    private void txtPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPasswordFocusGained
        if(txtPassword.getText().length()>0){
            txtPassword.selectAll();
        }
        if(txtPassword.getText().length()>0){
            lblBloqMayus.setIcon(null);
            lblBloqMayus.setText("");
        }
    }//GEN-LAST:event_txtPasswordFocusGained

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        LimpiaTxt();
    }//GEN-LAST:event_formWindowClosed

    private void btnAccederActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccederActionPerformed
        lblBloqMayus.setIcon(new ImageIcon(getClass().getResource("/imagenes/cargando.gif")));
        lblBloqMayus.setForeground(Color.black);
        lblBloqMayus.setIconTextGap(6);
        lblBloqMayus.setText("Accesando");
        VerificaUsuario();
    }//GEN-LAST:event_btnAccederActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAcceder;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblBloqMayus;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
