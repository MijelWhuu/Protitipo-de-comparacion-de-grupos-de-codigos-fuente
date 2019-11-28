package bl.catalogos;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.catalogos.ProcedimientosDao;
import entitys.ProcedimientoEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class ProcedimientosBl {

    ProcedimientosDao dao = new ProcedimientosDao();
    
    public ProcedimientosBl() {
    }
    
    public ArrayList getProcedimientos(){
        return dao.getProcedimientos();
    }

    public ErrorEntity saveProcedimiento(ProcedimientoEntity pro, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        pro.setId_procedimientos(dao.getId());
        ErrorEntity error;
        if(pro.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.saveProcedimiento(con, pro, sesion);
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
    
    public ErrorEntity updateProcedimiento(ProcedimientoEntity pro, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error;
        if(pro.getSeleccionar()){
            error = dao.updateSeleccionar(con);
            if(error.getError()) return error;
        }
        error = dao.updateProcedimiento(con, pro, sesion);
        if(error.getError()) return error;
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al modificar los datos");
        }
        return error;
    }
    
    public ErrorEntity deleteProcedimiento(String id){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteProcedimiento(con,id);
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
