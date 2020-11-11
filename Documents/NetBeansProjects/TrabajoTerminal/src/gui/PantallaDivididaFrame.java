/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import analisis.Codigo;
import controladores.FiltroDoc;
import javax.swing.text.AbstractDocument;

/**
 *
 * @author Miguel
 */
public class PantallaDivididaFrame extends javax.swing.JFrame {

    /**
     * Creates new form PantallaDivididaFrame
     * @param c1
     * @param c2
     */
    public PantallaDivididaFrame(Codigo c1,Codigo c2) {
        this.setUndecorated(true);
        initComponents();
        //Define el contenido de los elementos 
        this.codigo1lbl.setText(c1.getNombre().split("\\.")[0]);
        this.codigo2lbl.setText(c2.getNombre().split("\\.")[0]);
        this.codigo1Texto.setText(c1.getOriginal());
        this.codigo2Texto.setText(c2.getOriginal());
        ((AbstractDocument)codigo1Texto.getDocument()).setDocumentFilter(new FiltroDoc());
        ((AbstractDocument)codigo2Texto.getDocument()).setDocumentFilter(new FiltroDoc());
        
    }
    public PantallaDivididaFrame(){
        initComponents();
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        codigo1lbl = new javax.swing.JLabel();
        codigo2lbl = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        codigo1Texto = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        codigo2Texto = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(new java.awt.Dimension(1300, 700));

        codigo1lbl.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        codigo1lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo1lbl.setText("jLabel1");

        codigo2lbl.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        codigo2lbl.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        codigo2lbl.setText("jLabel2");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pantalla dividida");

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        codigo1Texto.setColumns(20);
        codigo1Texto.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        codigo1Texto.setRows(5);
        jScrollPane2.setViewportView(codigo1Texto);

        codigo2Texto.setColumns(20);
        codigo2Texto.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        codigo2Texto.setRows(5);
        jScrollPane3.setViewportView(codigo2Texto);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
                    .addComponent(codigo1lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addComponent(codigo2lbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(23, 23, 23))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(546, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(610, 610, 610))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(527, 527, 527))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codigo1lbl)
                    .addComponent(codigo2lbl))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea codigo1Texto;
    private javax.swing.JLabel codigo1lbl;
    private javax.swing.JTextArea codigo2Texto;
    private javax.swing.JLabel codigo2lbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] args) {
        new PantallaDivididaFrame().setVisible(true);
    }
}
