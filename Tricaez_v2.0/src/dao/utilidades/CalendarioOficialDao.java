package dao.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import entitys.DiasInhabiles;
import baseSistema.NombresTablas;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class CalendarioOficialDao  extends ConexionMySql{

    NombresTablas tbl = new NombresTablas();
    private final String tabla;
    
    public CalendarioOficialDao() {
        tabla = tbl.getGen_calendario();
    }
    
    public ArrayList getDiasInhabiles(int anio, int mes){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT dia FROM "+tabla+" WHERE anio = "+anio+" AND mes = "+mes+" ORDER BY dia";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                DiasInhabiles day = new DiasInhabiles();
                day.setDia(rs.getInt(1));
                array.add(day);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return array;
    }
    
    public int getDia(int anio, int mes, int dia){
        int total = 0;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM "+tabla+" WHERE anio = "+anio+" AND mes = "+mes+" AND dia = "+dia;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                total = rs.getInt(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return total;
    }
    
    public ErrorEntity saveFecha(int anio, int mes, int dia, String usuario, Date fecha){
        ErrorEntity error = new ErrorEntity(true);
        Connection con = getConexionBD();
        try{
            String query = "INSERT INTO "+tabla+" VALUES(?,?,?,?,?)";
            PreparedStatement save = con.prepareStatement(query);
            save.setInt(1, anio);
            save.setInt(2, mes);
            save.setInt(3, dia);
            save.setString(4, usuario);
            save.setDate(5, fecha);
            error.setError(save.execute());
            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorJava());
        }finally{
            cleanVariables(con);
        }
        return error;
    }
    
    public ErrorEntity deleteFecha(int anio, int mes, int dia){
        ErrorEntity error = new ErrorEntity(true);
        Connection con = getConexionBD();
        try{
            String query = "DELETE FROM "+tabla+" WHERE anio = "+anio+" AND mes = "+mes+" AND dia = "+dia;
            error.setError(con.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al eliminar los datos").getErrorSql());
        }catch (Exception e){
            error.setTipo(new ErroresClase(e,"Error al eliminar los datos").getErrorJava());
        }finally{
            cleanVariables(con);
        }
        return error;
    }
    
    
}
