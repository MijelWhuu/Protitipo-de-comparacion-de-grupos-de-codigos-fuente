package baseGeneral;

import java.sql.SQLException;
import java.util.logging.Level;

/**
 * @author marco
 */

public class ErroresClase {

    private SQLException e;
    private Exception ex;
    private final String mensajeDefault;
        
    public ErroresClase(SQLException e, String mensajeDefault){
        this.e = e;
        this.mensajeDefault = mensajeDefault;
    }
    
    public ErroresClase(Exception ex, String mensajeDefault){
        this.ex = ex;
        this.mensajeDefault = mensajeDefault;
    }
    
    public String getErrorSql(){
        System.out.println("CODIGO SQL: " + e.getErrorCode());
        System.out.println("ERROR  SQL: " + e.getMessage()+getMensaje(e));
        if(e.getErrorCode() == 0) return "Error 0.- Valores Nulos";
        else if(e.getErrorCode() == 1054) return "Error 1054.- Columna desconocida";
        else if(e.getErrorCode() == 1048) return "Error 1048.- Invalid Value Null";
        else if(e.getErrorCode() == 1451) return "Error 1451.- Datos ligados";
        else if(e.getErrorCode() == 1452) return "Error 1452.- Valores inexistentes";
        else if(e.getErrorCode() == 1062) return "Error 1062.- Claves repetidas";
        else{
            LogFile.log.log(Level.SEVERE, "Error: {0}", e.getMessage()+getMensaje(e));
            return this.mensajeDefault;
        } 
    }
    
    public String getErrorJava(){
        System.out.println("ERROR JAVA: " + ex.getMessage()+getMensaje(ex));
        LogFile.log.log(Level.SEVERE, "Error: {0}", ex.getMessage()+getMensaje(ex));
        return this.mensajeDefault;
    }
    
    private String getMensaje(Exception err){
        StackTraceElement[] info = err.getStackTrace();
        String messageError = "  { FILE: "+info[0].getFileName()+", CLASS: "+info[0].getClassName()+", METHOD: "+info[0].getMethodName()+", LINE: "
                + info[0].getLineNumber()+" }";
        System.out.println("ERROR JAVA: " + messageError);
        return messageError;
    }
    
    private String getMensaje(SQLException err){
        StackTraceElement[] info = err.getStackTrace();
        String messageError = "  { FILE: "+info[0].getFileName()+", CLASS: "+info[0].getClassName()+", METHOD: "+info[0].getMethodName()+", LINE: "
                + info[0].getLineNumber()+" }";
        System.out.println("ERROR JAVA: " + messageError);
        return messageError;
    }
    
}
