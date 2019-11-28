package bl.operaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.operaciones.RegistroAutosDao;
import entitys.PromocionEntity;
import entitys.RegistroAutoEntity;
import entitys.SesionEntity;
import entitys.TramiteEntity;
import interfaz.operaciones.PromoTramiteEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class RegistroAutosBl {

    RegistroAutosDao dao = new RegistroAutosDao();
    
    public RegistroAutosBl() {
    }
    
    public RegistroAutoEntity getAuto(Integer folio, Integer anio){
        return dao.getAuto(folio, anio);
    }
    
    public Boolean existeFolio(Integer folio, Integer anio){
        return dao.existeFolio(folio, anio);
    }
        
    public TramiteEntity getTramite(String id_tramite){
        return dao.getTramite(id_tramite);
    }
    
    public PromocionEntity getPromocion(String id_promocion){
        return dao.getPromocion(id_promocion);
    }
    
    public TramiteEntity getTramite(Integer folio, Integer anio){
        return dao.getTramite(folio, anio);
    }
    
    public PromocionEntity getPromocion(Integer folio, Integer anio){
        return dao.getPromocion(folio, anio);
    }
        
    public Boolean existeFolioPromocion(Integer folio, Integer anio){
        return dao.existeFolioPromocion(folio, anio);
    }
    
    public Integer getFolio(Integer anio){
        return dao.getFolio(anio);
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public ErrorEntity saveRegistro(RegistroAutoEntity re, SesionEntity sesion){
        
        re.setId_registro_auto(dao.getIdRegistroAuto());
        
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        //re.setId_registro_auto(dao.getIdRegistroAuto(con));
        ErrorEntity error = dao.saveRegistro(con, re, sesion);
        if(error.getError())
            return error;
        error = dao.saveRelacion(con, re.getId_registro_auto(), re.getFolio(), re.getAnio());
        if(error.getError())
            return error;
        
        ArrayList array = re.getProtra();
         if(array!=null && !array.isEmpty()){
            for(Object array1 : array) {
                PromoTramiteEntity protra = (PromoTramiteEntity) array1;
                PromocionEntity pro = protra.getPro();
                error = dao.saveRelacionPromo(con, re.getId_registro_auto(), pro.getId_promocion());
                if(error.getError())
                    return error;
            }
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
    
    public ErrorEntity updateRegistro(RegistroAutoEntity re, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updateRegistro(con, re, sesion);
        if(error.getError())
            return error;
        System.out.println("llego1");
        error = dao.updateRelacion(con, re.getId_registro_auto(), re.getFolio(), re.getAnio());
        if(error.getError())
            return error;
        System.out.println("llego2");
        error = dao.deleteRelacionPromo(con, re.getId_registro_auto());
        if(error.getError())
            return error;
        System.out.println("llego3");
        ArrayList array = re.getProtra();
        if(array!=null && !array.isEmpty()){
            for(Object array1 : array) {
                PromoTramiteEntity protra = (PromoTramiteEntity) array1;
                PromocionEntity pro = protra.getPro();
                error = dao.saveRelacionPromo(con, re.getId_registro_auto(), pro.getId_promocion());
                if(error.getError())
                    return error;
            }
        }
        System.out.println("llego4");
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al modificar los datos");
        }
        return error;
    }
    
    public ErrorEntity deleteRegistro(String id_registro){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deleteRelacionPromo(con, id_registro);
        if(error.getError())
            return error;
        error = dao.deleteRelacion(con, id_registro);
        if(error.getError())
            return error;
        error = dao.deleteRegistro(con, id_registro);
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
    
    public Boolean existeFolioAuto(Integer folio, Integer anio){
        return dao.existeFolioAuto(folio, anio);
    }
    
    public Boolean existeFolioAuto(Integer folio, Integer anio, String id_auto){
        return dao.existeFolioAuto(folio, anio, id_auto);
    }
    
    public ArrayList getPromociones(String id_auto){
        return dao.getPromociones(id_auto);
    }
    
}
