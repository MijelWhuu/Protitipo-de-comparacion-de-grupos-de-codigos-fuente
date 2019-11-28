package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.TipoAmparosDao;
import entitys.SesionEntity;
import entitys.TipoTramiteEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class TipoTramitesBl {

    TipoAmparosDao dao = new TipoAmparosDao();
    
    public TipoTramitesBl() {
    }
    
    public ArrayList getTipoAmparos(){
        return dao.getTipoAmparos();
    }
    
    public ErrorEntity saveTipoAmparo(TipoTramiteEntity tipo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        tipo.setId_tipo_amparo(dao.getId());
        ErrorEntity error;
        if(tipo.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.saveTipoAmparo(con, tipo, sesion);
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
    
    public ErrorEntity updateTipoAmparo(TipoTramiteEntity tipo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error;
        if(tipo.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.updateTipoAmparo(con, tipo, sesion);
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
    
    public ErrorEntity deleteTipoAmparo(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteTipoAmparo(con,id);
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
