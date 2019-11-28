package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.SesionEntity;
import entitys.TipoAutoMesaTramiteEntity;
import entitys.TipoNotificacionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class TipoAutosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public TipoAutosDao() {
    }
    
    public ArrayList getTipoAutos(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_tipo_auto, a.nombre, a.id_tipo_notificacion, a.dias, a.observaciones, "
                + "n.nombre FROM cat_tipo_autos a "
                + "INNER JOIN cat_tipo_notificaciones n ON a.id_tipo_notificacion = n.id_tipo_notificacion;";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                TipoAutoMesaTramiteEntity tipo = new TipoAutoMesaTramiteEntity();
                tipo.setId_tipo_auto_mesa_tramite(rs.getString(1));
                tipo.setNombre(rs.getString(2));
                tipo.setNoti(new TipoNotificacionEntity(rs.getString(3),rs.getString(6),""));
                tipo.setDias(rs.getInt(4));
                tipo.setObservaciones(rs.getString(5));
                array.add(tipo);
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
    
    public ErrorEntity saveTipoAuto(Connection conx, TipoAutoMesaTramiteEntity tipo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_tipo_autos VALUES(?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, tipo.getId_tipo_auto_mesa_tramite());
            save.setString(2, tipo.getNoti().getId_tipo_notificacion());
            save.setString(3, u.eliminaEspacios(tipo.getNombre()));
            save.setInt(4, tipo.getDias());
            save.setString(5, tipo.getObservaciones());
            save.setBoolean(6, tipo.getEstatus());
            save.setString(7, sesion.getId_usuario());
            save.setDate(8, sesion.getFechaActual());
            save.setDate(9, sesion.getFechaCero());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateTipoAuto(Connection con, TipoAutoMesaTramiteEntity tipo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_tipo_autos SET nombre = ?, id_tipo_notificacion = ?, dias = ?, "
                    + " observaciones = ?, id_usuario = ?, f_cambio = ? WHERE id_tipo_auto = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(tipo.getNombre()));
            save.setString(2, tipo.getNoti().getId_tipo_notificacion());
            save.setInt(3, tipo.getDias());
            save.setString(4, tipo.getObservaciones());
            save.setString(5, sesion.getId_usuario());
            save.setDate(6, sesion.getFechaActual());
            save.setString(7, tipo.getId_tipo_auto_mesa_tramite());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteTipoAuto(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_tipo_autos WHERE id_tipo_auto = '"+id+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_tipo_auto,2) as SIGNED INTEGER)),0) FROM cat_tipo_autos";
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
