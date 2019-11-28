package bl.operaciones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.operaciones.InicioTramiteDao;
import entitys.SesionEntity;
import entitys.TramiteEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class InicioTramiteBl {

    InicioTramiteDao dao = new InicioTramiteDao();
    
    public InicioTramiteBl() {
    }
    
    public Boolean existeFolio(Integer folio, Integer anio){
        return dao.existeFolio(folio, anio);
    }
    
    public ArrayList getAnios(){
        return dao.getAnios();
    }
    
    public TramiteEntity getTramite(Integer folio, Integer anio){
        return dao.getTramite(folio, anio);
    }
    
    public ArrayList getPromovedores(String expediente, int opcion){
        return dao.getPromovedores(expediente, opcion);
    }
    
    public Boolean existeTramite(String id_inicio_tramite){
        return dao.existeTramite(id_inicio_tramite);
    }
    
    public ArrayList getPromovedoresActor(String id_inicio_tramite){
        return dao.getPromovedoresActor(id_inicio_tramite);
    }
    
    public ArrayList getPromovedoresAutoridad(String id_inicio_tramite){
        return dao.getPromovedoresAutoridad(id_inicio_tramite);
    }
    
    public ArrayList getPromovedoresTerceroAutoridad(String id_inicio_tramite){
        return dao.getPromovedoresTerceroAutoridad(id_inicio_tramite);
    }
    
    public ArrayList getPromovedoresTerceroActor(String id_inicio_tramite){
        return dao.getPromovedoresTerceroActor(id_inicio_tramite);
    }
    
    public ArrayList getPromovedoresAutoridadAjena(String id_inicio_tramite){
        return dao.getPromovedoresAutoridadAjena(id_inicio_tramite);
    }
    
    public ArrayList getPromovedoresPersonaAjena(String id_inicio_tramite){
        return dao.getPromovedoresPersonaAjena(id_inicio_tramite);
    }
    
    public ErrorEntity saveTramite(TramiteEntity tra, SesionEntity sesion, ArrayList array){
    
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        tra.setId_inicio_tramite(dao.getIdTramite(con));
        ErrorEntity error = dao.saveTramite(con, tra, sesion);
        if(error.getError()) return error;
        
        if(!array.isEmpty()){
            for(Object array1 : array){
                String []datos = (String[]) array1;
                switch(datos[1]){
                    case "00": 
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), datos[0], null, null, null, null, null);
                        if(error.getError()) return error;
                        break;
                    case "01":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, datos[0], null, null, null, null);
                        if(error.getError()) return error;
                        break;
                    case "02":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, datos[0], null, null, null);
                        if(error.getError()) return error;
                        break;
                    case "03":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, null, datos[0], null, null);
                        if(error.getError()) return error;
                        break;
                    case "04":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, null,  null,  datos[0], null);
                        if(error.getError()) return error;
                        break;
                    case "05":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, null, null, null, datos[0]);
                        if(error.getError()) return error;
                        break;    
                    
                }                
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
    
    public ErrorEntity updateTramite(TramiteEntity tra, SesionEntity sesion, ArrayList array){
    
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.updateTramite(con, tra, sesion);
        if(error.getError()) return error;
        
        error = dao.deletePromovedores(con, tra.getId_inicio_tramite());
        if(error.getError()) return error;
        
        if(!array.isEmpty()){
            for(Object array1 : array){
                String []datos = (String[]) array1;
                switch(datos[1]){
                    case "00": 
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), datos[0], null, null, null, null, null);
                        if(error.getError()) return error;
                        break;
                    case "01":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, datos[0], null, null, null, null);
                        if(error.getError()) return error;
                        break;
                    case "02":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, datos[0], null, null, null);
                        if(error.getError()) return error;
                        break;
                    case "03":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, null, datos[0], null, null);
                        if(error.getError()) return error;
                        break;
                    case "04":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, null,  null,  datos[0], null);
                        if(error.getError()) return error;
                        break;
                    case "05":
                        error = dao.savePromovedor(con, dao.getIdPromueve(con), tra.getId_inicio_tramite(), null, null, null, null, null, datos[0]);
                        if(error.getError()) return error;
                        break;    
                    
                }                
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
    
    public ErrorEntity deleteTramite(String id_tramite){
    
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.deletePromovedores(con, id_tramite);
        if(error.getError()) return error;
        
        error = dao.deleteTramite(con, id_tramite);
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
    
}
