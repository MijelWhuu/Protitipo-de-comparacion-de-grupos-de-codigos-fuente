package bl.operaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.operaciones.RegistroResolucionesDao;
import entitys.RegistroAutoEntity;
import entitys.RegistroResolucionEntity;
import entitys.SesionEntity;
import entitys.TramiteEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class RegistroResolucionesBl {

    RegistroResolucionesDao dao = new RegistroResolucionesDao();
    
    public RegistroResolucionesBl() {
    }
    
    public RegistroResolucionEntity getResolucion(Integer folio, Integer anio){
        return dao.getResolucion(folio, anio);
    }
    
    public Integer getFolio(Integer anio){
        return dao.getFolio(anio);
    }
    
    public ArrayList getExpedientesTramites(){
        return dao.getExpedientesTramites();
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public ErrorEntity saveRegistro(RegistroResolucionEntity re, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        re.setId_reg_resolucion(dao.getIdRegistroAuto(con));
        ErrorEntity error = dao.saveRegistro(con, re, sesion);
        if(error.getError())
            return error;
        error = dao.saveRelacion(con, re.getId_reg_resolucion(), re.getFolio(), re.getAnio());
        if(error.getError())
            return error;
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
    
    public ErrorEntity updateRegistro(RegistroResolucionEntity re, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateRegistro(con, re, sesion);
        if(error.getError())
            return error;
        error = dao.updateRelacion(con, re.getId_reg_resolucion(), re.getFolio(), re.getAnio());
        if(error.getError())
            return error;
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
    
    public ErrorEntity deleteRegistro(String id_resolucion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteRelacion(con, id_resolucion);
        if(error.getError())
            return error;
        
        error = dao.deleteRegistro(con, id_resolucion);
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
    
}
