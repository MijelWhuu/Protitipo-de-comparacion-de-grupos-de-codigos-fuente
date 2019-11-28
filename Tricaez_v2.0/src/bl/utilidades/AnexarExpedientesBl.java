package bl.utilidades;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.utilidades.AnexarExpedientesDao;
import entitys.ExpedientesAnexoEntity;
import entitys.SesionEntity;
import java.sql.Connection;

/**
 *
 * @author mavg
 */
public class AnexarExpedientesBl {

    AnexarExpedientesDao dao = new AnexarExpedientesDao();
    
    public AnexarExpedientesBl() {
    }

    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
        
    public ErrorEntity saveAnexos(ExpedientesAnexoEntity exp, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.saveAnexos(con, exp, sesion);
        if(error.getError()) return error;
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al anexar los datos");
        }
        return error;
    }
    
}
