package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.DatosLaDao;
import entitys.DatoLaEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class DatosLaBl {

    DatosLaDao dao = new DatosLaDao();
    
    public DatosLaBl() {
    }
    
    public ArrayList getDatosLa(){
        return dao.getDatosLa();
    }
    
    public ErrorEntity saveDatoLa(DatoLaEntity dato, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        dato.setId_dato_la(dao.getId());
        ErrorEntity error = dao.saveDatoLa(con, dato, sesion);
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
    
    public ErrorEntity updateDatoLa(DatoLaEntity dato, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateDatoLa(con, dato, sesion);
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
    
    public ErrorEntity deleteDatoLa(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteDatoLa(con,id);
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
