/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * @author Miguel
 * Ésta clase es creada para que las JTextArea no se puedan editar
 * ya que si se desactivan la barra de navegación horizontal no esta
 * dispobible.
 */
public class FiltroDoc extends DocumentFilter {
    

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
    
    }
    
}
