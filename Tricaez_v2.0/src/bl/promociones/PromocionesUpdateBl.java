package bl.promociones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.promociones.PromocionesUpdateDao;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class PromocionesUpdateBl {

    PromocionesUpdateDao dao = new PromocionesUpdateDao();
    
    public PromocionesUpdateBl() {
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
        
    public Boolean existeFolio(Integer folio, Integer anio){
        return dao.existeFolio(folio, anio);
    }
    
    public ArrayList getAnios(){
        return dao.getAnios();
    }
    
    public PromocionEntity getPromocion(Integer folio, Integer anio){
        return dao.getPromocion(folio, anio);
    }
    
    public ErrorEntity updatePromocion(PromocionEntity promo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.updatePromocion(con, promo, sesion);
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
}
