package dao.principal;

import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.NombreEntity;
import entitys.SesionEntity;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;


/**
 *
 * @author mavg
 */
public class InicioSesionDao extends ConexionMySql {

    Utilidades u = new Utilidades();
    
    public InicioSesionDao() {
    }
        
    public SesionEntity cargaUsuarios(String usuario, String pass){
        SesionEntity user = null;
        try{
            String query = "SELECT id_usuario, usuario, nombre, paterno, materno, pass, pantallas FROM gen_usuarios "
                + "WHERE usuario = ? AND pass = ? AND estatus IS TRUE";
            PreparedStatement save = getConexionBD().prepareStatement(query);
            save.setString(1,usuario);
            save.setString(2,pass);
            ResultSet rs = save.executeQuery();
            while(rs.next()){
                user = new SesionEntity();
                user.setId_usuario(rs.getString(1));
                user.setUsuario(rs.getString(2));
                user.setNombre(new NombreEntity(rs.getString(3),rs.getString(4),rs.getString(5)));
                user.setPass(rs.getString(6));
                user.setPantallas(rs.getString(7));
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                user.setFechaActual(u.convertirDateToSqlDate(calendar.getTime()));
                user.setF_alta(u.convertirDateToSqlDate(calendar.getTime()));
                user.setFechaCero(u.convertirDateToSqlDate(u.stringToDate("1900-01-01")));
                user.setF_cambio(u.convertirDateToSqlDate(u.stringToDate("1900-01-01")));
            }
        }catch(SQLException sqlE){
            System.out.println(sqlE.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
        }
        return user;
    }
    
}
