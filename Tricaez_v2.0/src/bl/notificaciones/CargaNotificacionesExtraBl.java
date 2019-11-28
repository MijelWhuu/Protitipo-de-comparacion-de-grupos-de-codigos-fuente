package bl.notificaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.notificaciones.CargaNotificacionesExtraDao;
import entitys.CargaNotificacionExtraEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class CargaNotificacionesExtraBl {
    
    CargaNotificacionesExtraDao dao = new CargaNotificacionesExtraDao();

    public CargaNotificacionesExtraBl() {
    }
    
    public ArrayList getOficios(String expediente){
        return dao.getOficios(expediente);
    }
    
    public CargaNotificacionExtraEntity getOficio(String oficio, int anio){
        return dao.getOficio(oficio, anio);
    }
     
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public ErrorEntity saveNotificacion(CargaNotificacionExtraEntity noti, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        noti.setId_carga_extra(dao.getIdNotificacion(con));
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
    
    public ErrorEntity updateNotificacion(CargaNotificacionExtraEntity noti, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.updateNotificacion(con, noti, sesion);
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
    
    public ErrorEntity deleteNotificacion(String id_noti_extra){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deleteNotificacion(con, id_noti_extra);
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
