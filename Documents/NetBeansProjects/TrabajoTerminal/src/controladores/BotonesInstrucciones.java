/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author Miguel
 */
public class BotonesInstrucciones {
    private JLabel imagen;
    private int imActual;
    private JButton anterior;
    private JButton siguiente;
    private static final Icon[] IMAGENES={new ImageIcon("/imagenes/0.jpg"), new ImageIcon("/imagenes/1.jpg"),new ImageIcon("/imagenes/2.jpg"),
    new ImageIcon("/imagenes/3.jpg"), new ImageIcon("/imagenes/4.jpg")};

    public BotonesInstrucciones(JLabel imagen, JButton anterior, JButton siguiente) {
        this.imagen = imagen;
        this.anterior = anterior;
        this.siguiente = siguiente;
        this.anterior.setEnabled(false);
        this.imActual=0;
        Icon icon = new ImageIcon("/imagenes/0.jpg");
        this.imagen.setIcon(icon);
    }

    
    public void siguiente(){
        if(this.imActual++<IMAGENES.length){
            this.imagen.setIcon(IMAGENES[this.imActual]);
            if(!this.anterior.isEnabled())
                this.anterior.setEnabled(true);
        }
        else{
            this.imActual--;
            this.siguiente.setEnabled(false);
        }
    }
    
    public void anterior(){
        if((this.imActual--)==0)
            this.anterior.setEnabled(false);
        this.imagen.setIcon(IMAGENES[this.imActual]);
        if(!this.siguiente.isEnabled())
            this.siguiente.setEnabled(true);
    }
}
