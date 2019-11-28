package dao.reportes;

import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import bl.reportes.buscarPersonaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class infoExpedienteDao extends ConexionMySql{
    
    public infoExpedienteDao() {
    }
    
    public ArrayList getNombres(String nombre){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT expediente, CONCAT(nombre,' ',paterno,' ',materno) FROM pri_actores p "
                + "WHERE CONCAT(nombre,' ',paterno,' ',materno) LIKE '%"+nombre+"%' "
                + "UNION "
                + "SELECT expediente, CONCAT(nombre,' ',paterno,' ',materno) FROM pri_terceros_actores p "
                + "WHERE CONCAT(nombre,' ',paterno,' ',materno) LIKE '%"+nombre+"%' "
                + "UNION "
                + "SELECT expediente, CONCAT(nombre,' ',paterno,' ',materno) FROM pri_personas_ajenas p "
                + "WHERE CONCAT(nombre,' ',paterno,' ',materno) "
                + "LIKE '%"+nombre+"%' ORDER BY 2,1";
        try{
            PreparedStatement save = con.prepareStatement(query);
            rs = save.executeQuery();
            while (rs.next()){
                array.add(new buscarPersonaEntity(rs.getString(2),rs.getString(1)));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return array;
    }
}
