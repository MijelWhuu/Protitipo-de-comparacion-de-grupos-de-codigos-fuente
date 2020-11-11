/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import analisis.Similares;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;

/**
 *
 * @author Miguel
 */
public class ListSelecModel extends DefaultListSelectionModel{
    private int i0 = -1; 
    private int i1 = -1; 

    private int ultimoSeleccionado=-1;
    private int seleccionado=-1;
    private DefaultListModel lista;
    public ListSelecModel(DefaultListModel lis) {
        super();
        this.lista=lis;
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
     if(i0 == index0 && i1 == index1){
      if(getValueIsAdjusting()){ 
       setValueIsAdjusting(false); 
       if(index0==seleccionado)
           seleccionado=ultimoSeleccionado;
       ultimoSeleccionado=-1;
       setSelection(index0, index1); 
      } 
     }else{ 
      i0 = index0; 
      i1 = index1;
      setValueIsAdjusting(false);
      if(lista.getElementAt(index0) instanceof Similares){
            clearSelection();
            seleccionado=-1;
            ultimoSeleccionado=-1;
        }else
        if(ultimoSeleccionado!=-1 ) setSelection(ultimoSeleccionado, ultimoSeleccionado);
      ultimoSeleccionado= seleccionado!=-1?seleccionado:-1;
      seleccionado= index0;
      setSelection(index0, index1);
     }
    } 
    private void setSelection(int index0, int index1){ 
     if(super.isSelectedIndex(index0)) { 
      super.removeSelectionInterval(index0, index1); 
     }else { 
      super.addSelectionInterval(index0, index1); 
     } 
    } 
}
