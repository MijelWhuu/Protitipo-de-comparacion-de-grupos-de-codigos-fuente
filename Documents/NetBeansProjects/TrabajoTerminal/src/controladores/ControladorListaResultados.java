/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import analisis.Codigo;
import analisis.Similares;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author Miguel
 */
public class ControladorListaResultados {
    private JList lista;
    private DefaultListModel modelo;
    private ListSelecModel modeloSeleccion;
    public ControladorListaResultados(JList lista) {
        this.lista = lista;
        this.modelo= new DefaultListModel();
        this.lista.setModel(this.modelo);
        this.modeloSeleccion= new ListSelecModel(modelo);
        this.lista.setSelectionModel(modeloSeleccion);
    }
    
    public void AgregarElementos(ArrayList grupos){
        this.modelo.removeAllElements();
        for(Object element: grupos){
            this.modelo.addElement(element);
            for(Codigo codigo: ((Similares)element).getCodigos()){
                this.modelo.addElement(codigo);
            }
        }
        
        this.lista.setSelectedIndices(new int[]{1,1});
    }
    
     public Object getSeleccionados(){
        return (this.lista.getSelectedValue() instanceof Similares)? this.lista.getSelectedValue():lista.getSelectedValuesList();
    }
}
