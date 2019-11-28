package baseGeneral;

import javax.swing.text.*;

/**
 *
 * @author GLP
 */

public class JTextMascara extends PlainDocument {
    
    private int limit;
    private String mask;
    private boolean toUppercase = true  ;
    
    public JTextMascara(int limit) {
        super();
        this.limit = limit;
        this.mask = "*";
        toUppercase = true;
    }
        
    public JTextMascara(int limit, boolean upper, String mask) {
        super();
        this.limit = limit;
        this.mask = mask;
        toUppercase = upper;
    }
    
    public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
        if(str == null) 
            return;
        if((getLength() + str.length()) <= limit && validaCaracter(str)){
            if(toUppercase)
                if(!mask.equals("email")){
                    str = str.toUpperCase();
                }else{
                    str = str.toLowerCase();
                }
                super.insertString(offset, str, attr);
        }
    }
    
    public boolean validaCaracter(String car) throws BadLocationException { 
        for(int i=0; i<car.charAt(i); i++){
            if(mask.equals("alfanumerico")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i))){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("alfanumericopunto")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i)) && car.charAt(i)!='.'){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("alfanumericoespacio")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i)) && car.charAt(i)!=' '){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("alfanumericopuntoespacio")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i)) && car.charAt(i)!='.' && car.charAt(i)!=' '){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("numerico")){
                if(!Character.isDigit(car.charAt(i))){
                    return false; 
                }
                return true;
            }else if(mask.equals("decimal")){
                if(!Character.isDigit(car.charAt(i)) && car.charAt(i)!='.'){
                    if(!isDouble(this.getText(0, getLength()+1)))
                        return false;
                    else
                        return true;
                }
                return true;
            }else if(mask.equals("letranumeroguion")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i)) && car.charAt(i) != '-'){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("email")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i)) && car.charAt(i) != '-' && car.charAt(i) != '_' && car.charAt(i) != '.' && car.charAt(i) != '@'){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("*")){
                if(!Character.isLetter(car.charAt(i)) && !Character.isDigit(car.charAt(i)) && car.charAt(i)!=' ' && car.charAt(i)!='.'){ 
                    return false; 
                }
                return true;
            }else if(mask.equals("todo")){
                return true;
            }
        }
        return false;
    }
    
    public Boolean isDouble(Object numero){
        if(numero!=null && java.util.regex.Pattern.matches("((([0-9]*)?[.]?[0-9]+))",numero.toString()))
            return true;
        return false;
    }
    
}
