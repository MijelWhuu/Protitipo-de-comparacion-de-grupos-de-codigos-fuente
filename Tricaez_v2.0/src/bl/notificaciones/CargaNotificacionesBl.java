package bl.notificaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.notificaciones.CargaNotificacionesDao;
import entitys.CargaNotificacionEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class CargaNotificacionesBl {

    CargaNotificacionesDao dao = new CargaNotificacionesDao();
    
    public CargaNotificacionesBl() {
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
    
    public ArrayList getActores(String expediente){
        return dao.getActores(expediente);
    }
    
    public ArrayList getAutoridades(String expediente){
        return dao.getAutoridades(expediente);
    }
    
    public ArrayList getTerceroAutoridades(String expediente){
        return dao.getTerceroAutoridades(expediente);
    }
    
    public ArrayList getTerceroActores(String expediente){
        return dao.getTerceroActores(expediente);
    }
    
    public ArrayList getAutoridadesAjenas(String expediente){
        return dao.getAutoridadesAjenas(expediente);
    }
    
    public ArrayList getPersonasAjenas(String expediente){
        return dao.getPersonasAjenas(expediente);
    }
    
    public ErrorEntity saveNotificacion(ArrayList actores, ArrayList autoridades, ArrayList terceroAutoridades, ArrayList terceroActores, 
            ArrayList autoridadesAjenas, ArrayList personasAjenas, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        System.out.println("bl_fun_001");
        ErrorEntity error = new ErrorEntity();
        if(!actores.isEmpty()){
            error = guardaArray(con, actores, sesion);
            if(error.getError()) return error;
        }
        System.out.println("bl_fun_002");
        if(!autoridades.isEmpty()){
            error = guardaArray(con, autoridades, sesion);
            if(error.getError()) return error;
        }
        System.out.println("bl_fun_003");
        if(!terceroAutoridades.isEmpty()){
            error = guardaArray(con, terceroAutoridades, sesion);
            if(error.getError()) return error;
        }
        System.out.println("bl_fun_004");
        if(!terceroActores.isEmpty()){
            error = guardaArray(con, terceroActores, sesion);
            if(error.getError()) return error;
        }
        System.out.println("bl_fun_005");
        if(!autoridadesAjenas.isEmpty()){
            error = guardaArray(con, autoridadesAjenas, sesion);
            if(error.getError()) return error;
        }
        System.out.println("bl_fun_006");
        if(!personasAjenas.isEmpty()){
            error = guardaArray(con, personasAjenas, sesion);
            if(error.getError()) return error;
        }
        System.out.println("bl_fun_007");
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        System.out.println("bl_fun_008");
        return error;
    }
    
    private ErrorEntity guardaArray(Connection con, ArrayList array, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(false);
        for (Object array1 : array) {
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            carga.setId_carga(dao.getIdCargaNotificacion(con));
            error = dao.saveNotificacion(con, carga, sesion);
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
        return dao.getAutoridadesAjenasNoti(id_relacion);
    }
    
    public ArrayList getPersonasAjenasNoti(String id_relacion){
        return dao.getPersonasAjenasNoti(id_relacion);
    }
    
    public ArrayList getFechasCargas(){
        return dao.getFechasCargas();
    }
}
