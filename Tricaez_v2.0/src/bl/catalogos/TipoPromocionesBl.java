package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.TipoPromocionesDao;
import entitys.SesionEntity;
import entitys.TipoPromocionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class TipoPromocionesBl {

    TipoPromocionesDao dao = new TipoPromocionesDao();
    
    public TipoPromocionesBl() {
    }
    
    public ArrayList getTipoPromociones(){
        return dao.getTipoPromociones();
    }
    
    public ErrorEntity saveTipoPromocion(TipoPromocionEntity tipo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        tipo.setId_tipo_promocion(dao.getId());
        ErrorEntity error;
        if(tipo.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.saveTipoPromocion(con, tipo, sesion);
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
    
    public ErrorEntity updateTipoPromocion(TipoPromocionEntity tipo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error;
        if(tipo.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.updateTipoPromocion(con, tipo, sesion);
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
    
    public ErrorEntity deleteTipoPromocion(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteTipoPromocion(con,id);
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
