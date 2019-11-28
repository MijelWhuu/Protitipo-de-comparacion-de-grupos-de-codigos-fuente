/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package baseGeneral;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author mavg
 */
public class JTextMascaraTextArea extends PlainDocument {
    
    private boolean toUppercase = true;

    public JTextMascaraTextArea() {
        super();
    }
    
    public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
        if(str == null) {
        } else{
            str = str.toUpperCase();
            super.insertString(offset, str, attr);
        }
    }
    
}
