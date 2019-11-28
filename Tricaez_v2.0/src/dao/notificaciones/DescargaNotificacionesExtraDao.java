package dao.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoridadNotificacionEntity;
import entitys.ExpedienteEntity;
import entitys.CargaNotificacionExtraEntity;
import entitys.DescargaNotificacionExtraEntity;
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
public class DescargaNotificacionesExtraDao extends ConexionMySql{
    
    Utilidades u = new Utilidades();

    public DescargaNotificacionesExtraDao() {
    }
    
    public ArrayList getOficios(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_carga_extra, p.oficio, p.anio, p.expediente, p.fecha, p.id_autoridad, c.nombre, p.tipo_notificacion "
                + "FROM pri_carga_notificaciones_extra p "
                + "INNER JOIN cat_autoridades_notificaciones c ON p.id_autoridad=c.id_autoridad "
                + "LEFT JOIN pri_descarga_notificaciones_extra pd ON pd.id_carga_extra=p.id_carga_extra "
                + "WHERE pd.id_carga_extra IS NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionExtraEntity noti = new CargaNotificacionExtraEntity();
                noti.setId_carga_extra(rs.getString(1));
                noti.setOficio(rs.getString(2));
                noti.setAnio(rs.getInt(3));
                noti.setExpe(new ExpedienteEntity(rs.getString(4)));
                noti.setFecha_recibida(rs.getDate(5));
                noti.setAuto(new AutoridadNotificacionEntity(rs.getString(6),rs.getString(7),""));
                noti.setTipo_notificacion(rs.getString(8));
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_descarga_extra,10) as SIGNED INTEGER)),0) FROM "
                +"pri_descarga_notificaciones_extra WHERE SUBSTRING(id_descarga_extra,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("D", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    public ErrorEntity saveNotificacion(Connection conx, DescargaNotificacionExtraEntity noti, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_descarga_notificaciones_extra VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, noti.getId_descarga_extra());
            save.setString(2, noti.getId_carga_extra());
            save.setString(3, noti.getAuto().getId_autoridad());
            save.setString(4, noti.getExpe().getExpediente());
            save.setString(5, noti.getOficio());
            save.setInt(6, noti.getAnio());
            save.setString(7, noti.getTipo_notificacion());
            
            if(noti.getMun()!=null) save.setInt(8, noti.getMun().getId_municipio());
            else save.setNull(8, java.sql.Types.INTEGER);
            
            if(noti.getFecha_deposito()!=null) save.setDate(9, u.convertirDateToSqlDate(noti.getFecha_deposito()));
            else save.setNull(9, java.sql.Types.DATE);
            
            save.setString(10, noti.getSpm());
            
            if(noti.getFecha_recepcion()!=null) save.setDate(11, u.convertirDateToSqlDate(noti.getFecha_recepcion()));
            else save.setNull(11, java.sql.Types.DATE);
            
            if(noti.getFecha_notificacion()!=null) save.setDate(12, u.convertirDateToSqlDate(noti.getFecha_notificacion()));
            else save.setNull(12, java.sql.Types.DATE);
            
            save.setString(13, noti.getDesti().getId_destino());
            save.setDate(14, u.convertirDateToSqlDate(noti.getFecha_destino()));
            save.setString(15, noti.getObservaciones());
            save.setString(16, sesion.getId_usuario());
            save.setDate(17, sesion.getFechaActual());
            save.setDate(18, sesion.getFechaCero());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteNotificacion(Connection conx, String id_descarga_extra){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_descarga_notificaciones_extra WHERE id_descarga_extra = '"+id_descarga_extra+"'";
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