package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.SesionEntity;
import entitys.AutoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class AutosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public AutosDao() {
    }
    
    public ArrayList getAutos(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tipo_auto, nombre, observaciones, seleccionar FROM cat_autos ORDER BY nombre";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(new AutoEntity(rs.getString(1),rs.getString(2),rs.getString(3),rs.getBoolean(4)));
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
    
    public ErrorEntity saveAuto(Connection conx, AutoEntity fil, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_autos VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, fil.getId_tipo_auto());
            save.setString(2, u.eliminaEspacios(fil.getNombre()));
            save.setBoolean(3, fil.getTipo());
            save.setString(4, fil.getObservaciones());
            save.setBoolean(5, fil.getSeleccionar());
            save.setString(6, sesion.getId_usuario());
            save.setDate(7, sesion.getFechaActual());
            save.setDate(8, sesion.getFechaCero());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateAuto(Connection con, AutoEntity fil, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_autos SET nombre = ?, tipo = ?, observaciones = ?, seleccionar = ?, "
                    + "id_usuario = ?, f_cambio = ? WHERE id_tipo_auto = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(fil.getNombre()));
            save.setBoolean(2, fil.getTipo());
            save.setString(3, fil.getObservaciones());
            save.setBoolean(4, fil.getSeleccionar());
            save.setString(5, sesion.getId_usuario());
            save.setDate(6, sesion.getFechaActual());
            save.setString(7, fil.getId_tipo_auto());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteAuto(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_autos WHERE id_tipo_auto = '"+id+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_tipo_auto,2) as SIGNED INTEGER)),0) FROM cat_autos";
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
            error.setError(con.prepareStatement("UPDATE cat_autos SET seleccionar = false").execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public AutoEntity getAutoNombre(String nombre){
        AutoEntity auto = new AutoEntity();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tipo_auto FROM cat_autos WHERE nombre = '"+nombre+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                auto.setId_tipo_auto(rs.getString(1));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return auto;
    }
    
}
