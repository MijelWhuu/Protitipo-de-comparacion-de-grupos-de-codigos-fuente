package baseGeneral;

import javax.swing.text.*;
import java.awt.*;

/**
 *
 * @author GLP
 */

public class JTextDecimal extends PlainDocument {

     protected int limit = 0;
     protected int decimalPrecision = 0;
     protected boolean allowNegative = false;

     public JTextDecimal(int limit, int decimalPrecision){
         super();
         this.limit = limit;
         this.decimalPrecision = decimalPrecision;
         this.allowNegative = false;
     }
     
     public JTextDecimal(int decimalPrecision, boolean allowNegative) {
          super();
          this.decimalPrecision = decimalPrecision;
          this.allowNegative = allowNegative;
     }
   
     public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
          if (str != null && (super.getLength()<this.limit + this.decimalPrecision + 1)){
               if (isNumeric(str) == false && str.equals(".") == false && str.equals("-") == false){ 
                    Toolkit.getDefaultToolkit().beep();
                    return;
               }
               else if (isNumeric(str) == true && super.getText(0, super.getLength()).indexOf(".") == -1 && super.getLength() == limit){
                    Toolkit.getDefaultToolkit().beep();
                    return;
               }
               else if (str.equals(".") == true && super.getText(0, super.getLength()).contains(".") == true){ 
                    Toolkit.getDefaultToolkit().beep();
                    return;
               }
               else if (isNumeric(str) == true && super.getText(0, super.getLength()).indexOf(".") != -1 && offset>super.getText(0, super.getLength()).indexOf(".") && super.getLength()-super.getText(0, super.getLength()).indexOf(".")>decimalPrecision && decimalPrecision > 0){ 
                    Toolkit.getDefaultToolkit().beep();
                    return;
               }
               else if (str.equals("-") == true && (offset != 0 || allowNegative == false)){ 
                    Toolkit.getDefaultToolkit().beep();
                    return;
               }

               super.insertString(offset, str, attr);
          }
          return;
     }
     
     public static boolean isNumeric(String str){ 
        try{ 
            Double.parseDouble(str);
            return true; 
        }catch(NumberFormatException nFE){ 
            return false; 
        } 
    }
}