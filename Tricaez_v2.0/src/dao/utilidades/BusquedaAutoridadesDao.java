package dao.utilidades;

import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.BuscarAutoridadEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class BusquedaAutoridadesDao extends ConexionMySql{
    
    Utilidades u = new Utilidades();
    
    public BusquedaAutoridadesDao() {
    }
    
    public ArrayList getAutoridades(String id_autoridad){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;        
        try{
            String query = "SELECT p.expediente, fecha, cp.nombre, IF(ca.nombre IS NULL,'S/A',ca.nombre) FROM pri_expediente p "
                    + "INNER JOIN rel_expediente_has_cat_autoridades r ON p.expediente = r.expediente "
                    + "INNER JOIN cat_procedimientos cp ON cp.id_procedimientos = p.id_procedimientos "
                    + "LEFT OUTER JOIN cat_actos ca ON ca.id_actos = p.id_actos "
                    + "WHERE r.id_autoridad = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, id_autoridad);
            rs = save.executeQuery();
            while (rs.next()){
                array.add(new BuscarAutoridadEntity(rs.getString(1),u.convertirSqlDateToUtilDate(rs.getDate(2)),rs.getString(3),rs.getString(4)));
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
    
    public Integer existeAutoridad(String id_autoridad){
        Integer total = 0;
        Connection con = getConexionBD();
        ResultSet rs = null;        
        try{
            String query = "SELECT COUNT(*) FROM pri_expediente p INNER JOIN rel_expediente_has_cat_autoridades r ON p.expediente = r.expediente WHERE r.id_autoridad = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, id_autoridad);
            rs = save.executeQuery();
            while (rs.next()){
                total = rs.getInt(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return total;
    }
     
}
