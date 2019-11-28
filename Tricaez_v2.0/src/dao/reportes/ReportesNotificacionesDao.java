package dao.reportes;

import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mavg
 */
public class ReportesNotificacionesDao extends ConexionMySql{
    
    public ReportesNotificacionesDao() {
    }
    
    public String[] getSelection(Integer folio, Integer anio){
        String cad[]={"0","0"};
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_relacion, IF(id_registro_autos IS NOT NULL, '1', IF(id_reg_resolucion IS NOT NULL, '2','3')) "
                + "FROM pri_relacion_folios WHERE folio = "+folio+" AND anio = "+anio;
        try{
            PreparedStatement save = con.prepareStatement(query);
            rs = save.executeQuery();
            while (rs.next()){
                cad[0]=rs.getString(2);
                cad[1]=rs.getString(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return cad;
    }
    
    public Boolean existeFolio(Integer folio, Integer anio){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_relacion_folios WHERE folio = "+folio+" AND anio = "+anio;
        try{
            int valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            if(valor==0)
                existe = false;
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return existe;
    }
}
