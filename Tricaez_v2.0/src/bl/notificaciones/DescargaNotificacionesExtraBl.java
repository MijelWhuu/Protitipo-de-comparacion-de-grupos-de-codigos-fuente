package bl.notificaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.notificaciones.DescargaNotificacionesExtraDao;
import entitys.CargaNotificacionExtraEntity;
import entitys.DescargaNotificacionExtraEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class DescargaNotificacionesExtraBl {
    
    DescargaNotificacionesExtraDao dao = new DescargaNotificacionesExtraDao();

    public DescargaNotificacionesExtraBl() {
    }
    
    public ArrayList getOficios(){
        return dao.getOficios();
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public ErrorEntity saveNotificacion(DescargaNotificacionExtraEntity noti, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        noti.setId_descarga_extra(dao.getIdNotificacion(con));
        ErrorEntity error = dao.saveNotificacion(con, noti, sesion);
        if(error.getError()) return error;
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
       
    public ErrorEntity deleteNotificacion(String id_descarga_extra){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteNotificacion(con, id_descarga_extra);
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
