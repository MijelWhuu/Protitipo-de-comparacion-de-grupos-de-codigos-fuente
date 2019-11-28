package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.SesionEntity;
import entitys.SupuestoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class SupuestosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public SupuestosDao() {
    }
    
    public ArrayList getSupuestos(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_supuesto, nombre, observaciones, seleccionar FROM cat_supuestos";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(new SupuestoEntity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getBoolean(4)));
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
    
    public ErrorEntity saveSupuesto(Connection conx, SupuestoEntity sup, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_supuestos VALUES(?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, sup.getId_supuesto());
            save.setString(2, u.eliminaEspacios(sup.getNombre()));
            save.setString(3, sup.getObservaciones());
            save.setBoolean(4, sup.getSeleccionar());
            save.setString(5, sesion.getId_usuario());
            save.setDate(6, sesion.getFechaActual());
            save.setDate(7, sesion.getFechaCero());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateSupuesto(Connection con, SupuestoEntity sup, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_supuestos SET nombre = ?, observaciones = ?, seleccionar = ?, "
                    + "id_usuario = ?, f_cambio = ? WHERE id_supuesto = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(sup.getNombre()));
            save.setString(2, sup.getObservaciones());
            save.setBoolean(3, sup.getSeleccionar());
            save.setString(4, sesion.getId_usuario());
            save.setDate(5, sesion.getFechaActual());
            save.setString(6, sup.getId_supuesto());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteSupuesto(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_supuestos WHERE id_supuesto = '"+id+"'";
        try{
            error.setError(con.createStatement().execute(query));
        }catch (SQLException e){
            error.setNumError(e.getErrorCode());
            new ErroresClase(e,"Error al eliminar los datos").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error al eliminar los datos").getErrorJava();
        }finally{
        }
        return error;
    }
    
    public String getId(){
        String clave = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_supuesto,2) as SIGNED INTEGER)),0) FROM cat_supuestos";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClave("C0000", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return clave;
    }
    
    public ErrorEntity updateSeleccionar(Connection con){
        ErrorEntity error = new ErrorEntity(true);
        try{
            error.setError(con.prepareStatement("UPDATE cat_supuestos SET seleccionar = false").execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
}
