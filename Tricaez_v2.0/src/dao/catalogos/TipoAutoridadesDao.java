package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.SesionEntity;
import entitys.TipoAutoridadEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class TipoAutoridadesDao extends ConexionMySql{

    Utilidades u = new Utilidades();

    public TipoAutoridadesDao() {
    }
    
    public ArrayList getTipoAutoridades(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tipo_autoridad, nombre, regla_municipal, observaciones FROM cat_tipo_autoridades";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(new TipoAutoridadEntity(rs.getString(1),rs.getString(2),rs.getBoolean(3),rs.getString(4)));
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
    
    public ErrorEntity saveTipoAutoridad(Connection conx, TipoAutoridadEntity tipo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_tipo_autoridades VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, tipo.getId_tipo_autoridad());
            save.setString(2, u.eliminaEspacios(tipo.getNombre()));
            save.setBoolean(3, tipo.getRegla());
            save.setString(4, tipo.getObservaciones());
            save.setBoolean(5, tipo.getEstatus());
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
    
    public ErrorEntity updateTipoAutoridad(Connection con, TipoAutoridadEntity tipo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_tipo_autoridades SET nombre = ?, regla_municipal = ?, observaciones = ?, "
                    + "id_usuario = ?, f_cambio = ? WHERE id_tipo_autoridad = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(tipo.getNombre()));
            save.setBoolean(2, tipo.getRegla());
            save.setString(3, tipo.getObservaciones());
            save.setString(4, sesion.getId_usuario());
            save.setDate(5, sesion.getFechaActual());
            save.setString(6, tipo.getId_tipo_autoridad());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteTipoAutoridad(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_tipo_autoridades WHERE id_tipo_autoridad = '"+id+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_tipo_autoridad,2) as SIGNED INTEGER)),0) FROM cat_tipo_autoridades";
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
}
