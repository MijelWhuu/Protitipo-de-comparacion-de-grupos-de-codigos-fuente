/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Miguel
 */
public class ControladorLista {
    private JList lista;
    private DefaultListModel modelo;

    public ControladorLista(JList lista) {
        this.lista = lista;
        this.modelo= new DefaultListModel();
        this.lista.setModel(this.modelo);
    }
    
    public void agregarElementos(ArrayList lista){
        this.modelo.removeAllElements();
        for(Object element: lista){
            this.modelo.addElement(element);
        }
    }
}
