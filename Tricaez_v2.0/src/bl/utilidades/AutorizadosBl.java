package bl.utilidades;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.utilidades.AutorizadosDao;
import entitys.AutorizadoEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class AutorizadosBl {

    AutorizadosDao dao = new AutorizadosDao();
    
    public AutorizadosBl() {
    }
    
    public ArrayList getAutorizadores(){
        return dao.getAutorizadores();
    }
        
    public ErrorEntity saveAutorizador(AutorizadoEntity auto, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        auto.setId_autorizado(dao.getId());
        ErrorEntity error =dao.saveAutorizador(con, auto, sesion);
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
    
    public ErrorEntity updateAutorizador(AutorizadoEntity auto, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateAutorizador(con, auto, sesion);
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
    
    public ErrorEntity deleteAutorizador(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteAutorizador(con,id);
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
