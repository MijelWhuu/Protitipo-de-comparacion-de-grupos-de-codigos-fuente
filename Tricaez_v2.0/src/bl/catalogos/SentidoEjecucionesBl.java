package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.SentidoEjecucionesDao;
import entitys.SentidoEjecutoriaEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class SentidoEjecucionesBl {

    SentidoEjecucionesDao dao = new SentidoEjecucionesDao();
    
    public SentidoEjecucionesBl() {
    }
    
    public ArrayList getSentidoEjecuciones(){
        return dao.getSentidoEjecuciones();
    }
    
    public ErrorEntity saveSentidoEjecucion(SentidoEjecutoriaEntity sen, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        sen.setId_sentido_ejecutoria(dao.getId());
        ErrorEntity error;
        if(sen.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.saveSentidoEjecucion(con, sen, sesion);
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
    
    public ErrorEntity updateSentidoEjecucion(SentidoEjecutoriaEntity sen, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error;
        if(sen.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.updateSentidoEjecucion(con, sen, sesion);
        if(error.getError())
            return error;
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al modificar los datos");
        }
        return error;
    }
    
    public ErrorEntity deleteSentidoEjecucion(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteSentidoEjecucion(con,id);
        if(error.getError())
            return error;
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
