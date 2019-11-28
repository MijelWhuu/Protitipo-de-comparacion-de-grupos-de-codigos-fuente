package bl.notificaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.notificaciones.BorrarNotificacionesDao;
import entitys.CargaNotificacionEntity;
import entitys.DescargaNotificacionEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class BorrarNotificacionesBl {

    BorrarNotificacionesDao dao = new BorrarNotificacionesDao();
    
    public BorrarNotificacionesBl() {
    }
    
    public RegistroAutoEntity getAuto(Integer folio, Integer anio){
        return dao.getAuto(folio, anio);
    }
    
    public ArrayList getActores(String id_registro_auto){
        return dao.getActores(id_registro_auto);
    }
    
    public ArrayList getAutoridades(String id_registro_auto){
        return dao.getAutoridades(id_registro_auto);
    }
    
    public ArrayList getTerceroAutoridades(String id_registro_auto){
        return dao.getTerceroAutoridades(id_registro_auto);
    }
    
    public ArrayList getTerceroActores(String id_registro_auto){
        return dao.getTerceroActores(id_registro_auto);
    }
    
    public ArrayList getAutoridadesAjenas(String id_registro_auto){
        return dao.getAutoridadesAjenas(id_registro_auto);
    }
    
    public ArrayList getPersonasAjenas(String id_registro_auto){
        return dao.getPersonasAjenas(id_registro_auto);
    }
    
    public ErrorEntity deleteNotificacion(ArrayList array){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = new ErrorEntity();
        for(Object datos:array){
            DescargaNotificacionEntity carga = (DescargaNotificacionEntity)datos;
            if(carga.getId_descarga().length()!=0){
                error = dao.deleteDescarga(con, carga.getId_descarga());
                if(error.getError()) return error;
            }
            if(carga.getId_carga().length()!=0){
                error = dao.deleteCarga(con, carga.getId_carga());
                if(error.getError()) return error;
            }
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
    
    
    public RelacionEntity getRelacion(Integer folio, Integer anio){
        return dao.getRelacion(folio, anio);
    }
    
    public ArrayList getAuto(String id_registro_autos){
        return dao.getAuto(id_registro_autos);
    }
    
    public ArrayList getResolucion(String id_reg_resolucion){
        return dao.getResolucion(id_reg_resolucion);
    }
    
    public ArrayList getSentencia(String id_reg_sentencia){
        return dao.getSentencia(id_reg_sentencia);
    }
    
    public String getAutoExpediente(String id_registro_autos){
        return dao.getAutoExpediente(id_registro_autos);
    }
    
    public String getResolucionExpediente(String id_reg_resolucion){
        return dao.getResolucionExpediente(id_reg_resolucion);
    }
    
    public String getSentenciaExpediente(String id_reg_sentencia){
        return dao.getSentenciaExpediente(id_reg_sentencia);
    }
}
