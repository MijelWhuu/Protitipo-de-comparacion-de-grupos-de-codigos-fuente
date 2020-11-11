/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import abrir_archivos.AbrirCodigos;
import analisis.Codigo;
import analisis.ComparacionesJava;
import analisis.Similares;
import guardar_archivos.GuardarCodigos;
import gui.PantallaDivididaFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 *
 * @author Miguel
 */
public class Botones {

    private AbrirCodigos abrirCodigos;
    private JLabel lblLenguaje;
    private ControladorLista codigosAbiertos;
    private ControladorListaResultados resultados;
    private GuardarCodigos guardar_codigos;
    private ArrayList<Similares> similares;

    public Botones(AbrirCodigos abrirCodigos, JLabel lblLenguaje, ControladorLista codigosAbiertos, ControladorListaResultados resultados, GuardarCodigos gc) {
        this.abrirCodigos = abrirCodigos;
        this.lblLenguaje = lblLenguaje;
        this.codigosAbiertos = codigosAbiertos;
        this.resultados = resultados;
        this.guardar_codigos=gc;
    }

    
       
    public void abrirCodigos(JButton boton){
        try {
            this.abrirCodigos.abrirArchivos();
            switch(this.abrirCodigos.getLenguaje()){
                case "java":
                    this.lblLenguaje.setText("Java");
                    break;
                case "c":
                    this.lblLenguaje.setText("C");
                    break;
                case "scilab":
                    this.lblLenguaje.setText("Scilab");
                    break;
                 
            }
            this.codigosAbiertos.agregarElementos(this.abrirCodigos.getCodigos());
            boton.setEnabled(true);
        } catch (IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(null,"Error en la apertura de archivos", "Aviso", 0);
        }
    }
    
    public void verSimilares(int nivel, JList jList1){
        //Debe obtener  los similares de ese nivel
        ArrayList<Similares> si = Similares.getNivel(nivel, similares);
        //Debe obtener de el analizador los similares, en la variable si
        this.resultados.AgregarElementos(si);
        jList1.getSelectionModel().clearSelection();
    }
    
    public void guardarCodigos(){
        this.guardar_codigos.setNombreCarpeta("temporal");
        Object recibo= this.resultados.getSeleccionados();
        if(recibo instanceof Similares)
            if(this.guardar_codigos.guardarArchivos(((Similares)recibo).getCodigos()))
                javax.swing.JOptionPane.showMessageDialog(null,"Se guardaron con éxito", "Aviso", 1);
    }
    
    public void verPantallaDividida(){
       Object recibo= this.resultados.getSeleccionados();
       if(!(recibo instanceof Similares)){
           List lista= (List)recibo;
           if(lista.size()==2){
               if(lista.get(0) instanceof Codigo && lista.get(1) instanceof Codigo){
                   new PantallaDivididaFrame((Codigo)lista.get(0), (Codigo)lista.get(1)).setVisible(true);
               }
           }else{
               javax.swing.JOptionPane.showMessageDialog(null,"Seleccione dos códigos", "Aviso", 0);
           }
           
       }else{
           javax.swing.JOptionPane.showMessageDialog(null,"Seleccione dos códigos", "Aviso", 0);
       }
       
    }
    
    public void comenzarAnalisis(int seleccion, JButton panDiv, JButton guardar, JComboBox box, JLabel lenguaje){
        //Ve que niveles quiere analizar, luego comienza todo el pex mijo
        if(lenguaje.getText().toLowerCase().equals("java")){
            ComparacionesJava c= new ComparacionesJava();
            c.clasificarCodigos(seleccion, this.abrirCodigos.getCodigos());
            this.similares=c.getSimilares();
        }else if(lenguaje.getText().toLowerCase().equals("c")){
            
        }else if(lenguaje.getText().toLowerCase().equals("java")){
        
        }
        panDiv.setEnabled(true);
        guardar.setEnabled(true);
        box.setEnabled(true);
    }
}
