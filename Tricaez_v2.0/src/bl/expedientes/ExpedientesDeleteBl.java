package bl.expedientes;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.expedientes.ExpedienteDeleteDao;
import entitys.ExpedienteEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class ExpedientesDeleteBl {

    ExpedienteDeleteDao dao = new ExpedienteDeleteDao();
    
    public ExpedientesDeleteBl() {
    }
       
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
        
    public ErrorEntity deleteExpediente(String expediente){
    
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = deleteTablas(con, expediente);
        if(error.getError()) return error;
        
        error = dao.deleteExpediente(con, expediente);
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
    
    public ExpedienteEntity getExpediente(String expediente){
        return dao.getExpediente(expediente);
    }
        
    public ArrayList getActores(String expediente){
        return dao.getActores(expediente);
    }
    
    public ArrayList getAutoridades(String expediente){
        return dao.getAutoridades(expediente);
    }
    
    public ArrayList getAutoridadesTercero(String expediente){
        return dao.getAutoridadesTercero(expediente);
    }
    
    public ArrayList getActoresTercero(String expediente){
        return dao.getActoresTercero(expediente);
    }
    
    public ArrayList getAutorizados(String expediente){
        return dao.getAutorizados(expediente);
    }
    
    public ArrayList getAutoridadesAjenas(String expediente){
        return dao.getAutoridadesAjenas(expediente);
    }
    
    public ArrayList getPersonasAjenas(String expediente){
        return dao.getPersonasAjenas(expediente);
    }
    
    public ErrorEntity deleteTablas(Connection conx, String expediente){
        ErrorEntity error = dao.deleteExpedienteTerceroAutoridad(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteAutoridadAjena(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteAutorizado(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteAutoridad(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedientePersonaAjena(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteTerceroActor(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteActor(conx,expediente);
        if(error.getError()) return error;
        return error;
    }

}
