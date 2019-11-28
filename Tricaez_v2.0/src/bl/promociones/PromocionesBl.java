package bl.promociones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.promociones.PromocionesDao;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import java.sql.Connection;

/**
 *
 * @author mavg
 */
public class PromocionesBl {

    PromocionesDao dao = new PromocionesDao();
    
    public PromocionesBl() {
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public Integer getFolio(Integer anio){
        return dao.getFolio(anio);
    }
    
    public Boolean existeFolio(Integer folio, Integer anio){
        return dao.existeFolio(folio, anio);
    }
    
    public Boolean existeFolio(Integer folio, Integer anio, String id_promocion){
        return dao.existeFolio(folio, anio, id_promocion);
    }
    
    public ErrorEntity savePromocion(PromocionEntity promo, SesionEntity sesion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        promo.setId_promocion(dao.getIdPromociones(con));
        ErrorEntity error = dao.savePromocion(con, promo, sesion);
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
