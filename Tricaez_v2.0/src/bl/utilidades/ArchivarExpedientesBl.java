package bl.utilidades;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.utilidades.ArchivarExpedientesDao;
import entitys.ExpedienteEntity;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class ArchivarExpedientesBl {

    ArchivarExpedientesDao dao = new ArchivarExpedientesDao();
    
    public ArchivarExpedientesBl() {
    }

    public Boolean existeCaja(String caja){
        return dao.existeCaja(caja);
    }
    
    public ArrayList getCajas(){
        return dao.getCajas();
    }
    
    public ErrorEntity saveCaja(String caja, String usuario, Date fechaActual){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.saveCaja(con, caja, usuario, fechaActual);
        if(error.getError()) return error;
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
    
    public ErrorEntity updateCaja(String oldCaja, String newCaja, String usuario, Date fechaActual){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.updateCaja(con, oldCaja, newCaja, usuario, fechaActual);
        if(error.getError()) return error;
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al modificar los datos");
        }
        return error;
    }
    
    public ErrorEntity deleteCaja(String caja){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteCaja(con, caja);
        if(error.getError()) return error;
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al eliminar los datos");
        }
        return error;
    }
    
    public ArrayList getExpedientes(String caja){
        return dao.getExpedientes(caja);
    }
    
    public ArrayList getExpedientesSinArchivar(){
        return dao.getExpedientesSinArchivar();
    }
    
    public ErrorEntity saveCajaExpediente(String caja, String usuario, Date fechaActual){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteCaja(con, caja);
        if(error.getError()) return error;
        
        error = dao.saveCaja(con, caja, usuario, fechaActual);
        if(error.getError()) return error;
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
    
    public ErrorEntity saveExpedientes(String caja, ArrayList array, String usuario, Date fechaActual){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
       
        ErrorEntity error = dao.deleteCaja(con, caja);
        if(error.getError()) return error;
        
        for(Object array1: array){
            ExpedienteEntity expe = (ExpedienteEntity) array1;
            error = dao.saveExpedientes(con, expe, usuario, fechaActual);
            if(error.getError()) return error;
        }
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
    
}
