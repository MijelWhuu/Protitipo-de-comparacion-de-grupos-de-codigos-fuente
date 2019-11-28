package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.AutoridadDao;
import entitys.AutoridadEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class AutoridadBl {

    AutoridadDao dao = new AutoridadDao();
    
    public AutoridadBl() {
    }
    
    public ArrayList getAutoridades(String id_tipo){
        return dao.getAutoridades(id_tipo);
    }
    
    public ArrayList getAutoridades(String id_tipo, int id_municipio){
        return dao.getAutoridades(id_tipo, id_municipio);
    }
    
    public ArrayList getMunicipiosTipoAutoridad(String id_tipo){
        return dao.getMunicipiosTipoAutoridad(id_tipo);
    }
    
    public ErrorEntity saveAutoridad(AutoridadEntity auto, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        auto.setId_autoridad(dao.getId());
        ErrorEntity error = dao.saveAutoridad(con, auto, sesion);
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
    
    public ErrorEntity updateAutoridad(AutoridadEntity auto, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateAutoridad(con, auto, sesion);
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
    
    public ErrorEntity deleteAutoridad(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteAutoridad(con,id);
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
    
    public ArrayList getAutoridadesComplete(){
        return dao.getAutoridadesComplete();
    }
}
