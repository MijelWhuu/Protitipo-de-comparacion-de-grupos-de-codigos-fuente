package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.OficinasDao;
import entitys.OficinaEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class OficinasBl {

    OficinasDao dao = new OficinasDao();
    
    public OficinasBl() {
    }
    
    public ArrayList getOficinas(){
        return dao.getOficinas();
    }
    
    public ErrorEntity saveOficina(OficinaEntity ofi, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ofi.setId_oficina(dao.getId());
        ErrorEntity error = dao.saveOficina(con, ofi, sesion);
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
    
    public ErrorEntity updateOficina(OficinaEntity ofi, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateOficina(con, ofi, sesion);
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
    
    public ErrorEntity deleteOficina(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteOficina(con,id);
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
