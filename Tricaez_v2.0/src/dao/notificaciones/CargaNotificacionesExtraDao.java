package dao.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoridadNotificacionEntity;
import entitys.ExpedienteEntity;
import entitys.CargaNotificacionExtraEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class CargaNotificacionesExtraDao extends ConexionMySql{
    
    Utilidades u = new Utilidades();

    public CargaNotificacionesExtraDao() {
    }
    
    public ArrayList getOficios(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_carga_extra, oficio, anio, expediente, fecha, id_autoridad, tipo_notificacion, observaciones "
                + "FROM pri_carga_notificaciones_extra WHERE expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionExtraEntity noti = new CargaNotificacionExtraEntity();
                noti.setId_carga_extra(rs.getString(1));
                noti.setOficio(rs.getString(2));
                noti.setAnio(rs.getInt(3));
                noti.setExpe(new ExpedienteEntity(rs.getString(4)));
                noti.setFecha_recibida(rs.getDate(5));
                noti.setAuto(new AutoridadNotificacionEntity(rs.getString(6)));
                noti.setTipo_notificacion(rs.getString(7));
                noti.setObservaciones(rs.getString(8));
                array.add(noti);
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
    
    public CargaNotificacionExtraEntity getOficio(String oficio, int anio){
        CargaNotificacionExtraEntity noti = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_carga_extra, oficio, anio, expediente, fecha, id_autoridad, tipo_notificacion, observaciones "
                + "FROM pri_carga_notificaciones_extra WHERE oficio = '"+oficio+"' AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                noti = new CargaNotificacionExtraEntity();
                noti.setId_carga_extra(rs.getString(1));
                noti.setOficio(rs.getString(2));
                noti.setAnio(rs.getInt(3));
                noti.setExpe(new ExpedienteEntity(rs.getString(4)));
                noti.setFecha_recibida(rs.getDate(5));
                noti.setAuto(new AutoridadNotificacionEntity(rs.getString(6)));
                noti.setTipo_notificacion(rs.getString(7));
                noti.setObservaciones(rs.getString(8));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return noti;
    }
    
    public Boolean existeExpediente(String expediente){
        Boolean existe = false;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_expediente p WHERE expediente = '"+expediente+"'";
        try{
            int valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                valor = rs.getInt(1);
            }
            if(valor>0)
                existe = true;
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return existe;
    }
    
    public String getIdNotificacion(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_carga_extra,10) as SIGNED INTEGER)),0) FROM "
                +"pri_carga_notificaciones_extra WHERE SUBSTRING(id_carga_extra,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("N", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }

    public ErrorEntity saveNotificacion(Connection conx, CargaNotificacionExtraEntity noti, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_carga_notificaciones_extra VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, noti.getId_carga_extra());
            save.setString(2, noti.getOficio());
            save.setInt(3, noti.getAnio());
            save.setString(4, noti.getExpe().getExpediente());
            save.setDate(5, u.convertirDateToSqlDate(noti.getFecha_recibida()));
            save.setString(6, noti.getAuto().getId_autoridad());
            save.setString(7, noti.getTipo_notificacion());
            save.setString(8, noti.getObservaciones());
            save.setString(9, sesion.getId_usuario());
            save.setDate(10, sesion.getFechaActual());
            save.setDate(11, sesion.getFechaCero());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateNotificacion(Connection conx, CargaNotificacionExtraEntity noti, SesionEntity sesion ){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_carga_notificaciones_extra SET oficio = ?, anio = ?, expediente = ?, fecha = ?, id_autoridad = ?, "
                    + "tipo_notificacion = ?, observaciones = ?, id_usuario = ?, f_cambio = ? WHERE id_carga_extra = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, noti.getOficio());
            save.setInt(2, noti.getAnio());
            save.setString(3, noti.getExpe().getExpediente());
            save.setDate(4, u.convertirDateToSqlDate(noti.getFecha_recibida()));
            save.setString(5, noti.getAuto().getId_autoridad());
            save.setString(6, noti.getTipo_notificacion());
            save.setString(7, noti.getObservaciones());
            save.setString(8, sesion.getId_usuario());
            save.setDate(9, sesion.getFechaActual());
            save.setString(10, noti.getId_carga_extra());            
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteNotificacion(Connection conx, String id_carga_extra){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_carga_notificaciones_extra WHERE id_carga_extra = '"+id_carga_extra+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
}