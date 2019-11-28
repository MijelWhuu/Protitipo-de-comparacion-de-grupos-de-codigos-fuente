package bl.notificaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.notificaciones.DescargaNotificacionesDao;
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
public class DescargaNotificacionesBl {

    DescargaNotificacionesDao dao = new DescargaNotificacionesDao();
    
    public DescargaNotificacionesBl() {
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
    
    public String[] getMunicipios(){
        return dao.getMunicipios();
    }
    
    public ErrorEntity saveNotificacion(ArrayList actores, ArrayList autoridades, ArrayList terceroAutoridades, ArrayList terceroActores, 
            ArrayList autoridadesAjenas, ArrayList personasAjenas, java.util.Date fecha_destino, String id_destino, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = new ErrorEntity();
        if(!actores.isEmpty()){
            error = guardaArray(con, actores, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        if(!autoridades.isEmpty()){
            error = guardaArray(con, autoridades, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        if(!terceroAutoridades.isEmpty()){
            error = guardaArray(con, terceroAutoridades, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        if(!terceroActores.isEmpty()){
            error = guardaArray(con, terceroActores, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        if(!autoridadesAjenas.isEmpty()){
            error = guardaArray(con, autoridadesAjenas, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        if(!personasAjenas.isEmpty()){
            error = guardaArray(con, personasAjenas, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
    }
    
    private ErrorEntity guardaArray(Connection con, ArrayList array, java.util.Date fecha_destino, String id_destino, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(false);
        for (Object array1 : array) {
            DescargaNotificacionEntity carga = (DescargaNotificacionEntity) array1;
            carga.setId_descarga(dao.getIdDescargaNotificacion(con));
            error = dao.saveNotificacion(con, carga, fecha_destino, id_destino, sesion);
            if(error.getError()) return error;
        }
        return error;
    }
    
    public ArrayList getActoresNoti(String id_relacion){
        return dao.getActoresNoti(id_relacion);
    }
    
    public ArrayList getAutoridadesNoti(String id_relacion){
        return dao.getAutoridadesNoti(id_relacion);
    }
    
    public ArrayList getTerceroAutoridadesNoti(String id_relacion){
        return dao.getTerceroAutoridadesNoti(id_relacion);
    }
    
    public ArrayList getTerceroActoresNoti(String id_relacion){
        return dao.getTerceroActoresNoti(id_relacion);
    }
    
    public ArrayList getAutoridadesAjenasNoti(String id_relacion){
        return dao.getAutoridadesNoti(id_relacion);
    }
    
    public ArrayList getPersonasAjenasNoti(String id_relacion){
        return dao.getPersonasAjenasNoti(id_relacion);
    }
    
    public ArrayList getFechasCargas(){
        return dao.getFechasCargas();
    }
    
    public ArrayList getAutosFechasCargas(java.util.Date fecha){
        return dao.getAutosFechasCargas(fecha);
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
