package bl.utilidades;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.utilidades.UsuariosDao;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class UsuariosBl {

    UsuariosDao dao = new UsuariosDao();
    
    public UsuariosBl() {
    }
    
    public ArrayList getEmpleados(){
        return dao.getEmpleados();
    }
    
    public Boolean existeUsuario(String usuario){
        return dao.existeUsuario(usuario);
    }
    
    public ErrorEntity saveUsuario(SesionEntity usuario, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        usuario.setId_usuario(dao.getIdUsuario(con));
        ErrorEntity error = dao.saveUsuario(con, usuario, sesion);
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
    
    public ErrorEntity updateUsuario(SesionEntity usuario){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateUsuario(con, usuario);
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
    
    public ErrorEntity deleteUsuario(String id_usuario){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteUsuario(con, id_usuario);
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
