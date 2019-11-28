package bl.operaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.operaciones.RegistroSentenciasDao;
import entitys.RegistroSentenciaEntity;
import entitys.SesionEntity;
import java.sql.Connection;

/**
 *
 * @author mavg
 */
public class RegistroSentenciasBl {

    RegistroSentenciasDao dao = new RegistroSentenciasDao();
    
    public RegistroSentenciasBl() {
    }
    
    public RegistroSentenciaEntity getSentencia(Integer folio, Integer anio){
        return dao.getSentencia(folio, anio);
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public Integer getFolio(Integer anio){
        return dao.getFolio(anio);
    }
    
    public ErrorEntity saveRegistro(RegistroSentenciaEntity re, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        re.setId_reg_sentencia(dao.getIdRegistroAuto(con));
        ErrorEntity error = dao.saveRegistro(con, re, sesion);
        if(error.getError())
            return error;
        error = dao.saveRelacion(con, re.getId_reg_sentencia(), re.getFolio(), re.getAnio());
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
    
    public ErrorEntity updateRegistro(RegistroSentenciaEntity re, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateRegistro(con, re, sesion);
        if(error.getError())
            return error;
        error = dao.updateRelacion(con, re.getId_reg_sentencia(), re.getFolio(), re.getAnio());
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
    
    public ErrorEntity deleteRegistro(String id_sentencia){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteRelacion(con, id_sentencia);
        if(error.getError())
            return error;
        
        error = dao.deleteRegistro(con, id_sentencia);
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
    
}
