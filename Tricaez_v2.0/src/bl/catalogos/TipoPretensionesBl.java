package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.TipoPretensionesDao;
import entitys.SesionEntity;
import entitys.TipoPretensionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class TipoPretensionesBl {

    TipoPretensionesDao dao = new TipoPretensionesDao();
    
    public TipoPretensionesBl() {
    }
    
    public ArrayList getTipoPretensiones(){
        return dao.getTipoPretensiones();
    }
    
    public ErrorEntity saveTipoPretension(TipoPretensionEntity tipo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        tipo.setId_tipo_pretension(dao.getId());
        ErrorEntity error;
        if(tipo.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.saveTipoPretension(con, tipo, sesion);
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
    
    public ErrorEntity updateTipoPretension(TipoPretensionEntity tipo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error;
        if(tipo.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.updateTipoPretension(con, tipo, sesion);
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
    
    public ErrorEntity deleteTipoPretension(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteTipoPretension(con,id);
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
    
    public TipoPretensionEntity getTipoPretensionNombre(String nombre){
        return dao.getTipoPretensionNombre(nombre);
    }
}
