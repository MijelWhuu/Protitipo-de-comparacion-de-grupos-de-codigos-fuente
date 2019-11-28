package bl.notificaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.notificaciones.BorrarNotificacionesExtraDao;
import entitys.CargaNotificacionExtraEntity;
import entitys.DescargaNotificacionExtraEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class BorrarNotificacionesExtraBl {

    BorrarNotificacionesExtraDao dao = new BorrarNotificacionesExtraDao();
    
    public BorrarNotificacionesExtraBl() {
    }
    
    public CargaNotificacionExtraEntity getCarga(String oficio, Integer anio){
        return dao.getCarga(oficio, anio);
    }
    
    public DescargaNotificacionExtraEntity getDescarga(String oficio, Integer anio){
        return dao.getDescarga(oficio, anio);
    }
    
    public ErrorEntity deleteNotificacion(String ids[]){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = new ErrorEntity();
        if(ids[1].length()!=0){
            error = dao.deleteDescarga(con, ids[1]);
            if(error.getError()) return error;
        }
        if(ids[0].length()!=0){
            error = dao.deleteCarga(con, ids[0]);
            if(error.getError()) return error;
        }
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
