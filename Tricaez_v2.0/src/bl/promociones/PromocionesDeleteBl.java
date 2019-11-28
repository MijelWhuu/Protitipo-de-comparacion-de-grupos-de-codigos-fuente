package bl.promociones;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.promociones.PromocionesDeleteDao;
import entitys.PromocionEntity;
import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class PromocionesDeleteBl {

    PromocionesDeleteDao dao = new PromocionesDeleteDao();
    
    public PromocionesDeleteBl() {
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
    
    public ErrorEntity deletePromocion(String id_promocion){
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        ErrorEntity error = dao.deletePromocion(con, id_promocion);
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
}
