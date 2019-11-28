package dao.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoridadNotificacionEntity;
import entitys.CargaNotificacionExtraEntity;
import entitys.DescargaNotificacionExtraEntity;
import entitys.DestinoEntity;
import entitys.ExpedienteEntity;
import entitys.MunicipioEntity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author mavg
 */
public class BorrarNotificacionesExtraDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public BorrarNotificacionesExtraDao() {
    }
    
    public CargaNotificacionExtraEntity getCarga(String oficio, Integer anio){
        CargaNotificacionExtraEntity carga = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_carga_extra, oficio, anio, expediente, fecha, p.id_autoridad, c.nombre, tipo_notificacion "
                + "FROM pri_carga_notificaciones_extra p "
                + "LEFT OUTER JOIN cat_autoridades_notificaciones c ON p.id_autoridad=c.id_autoridad "
                + "WHERE oficio = '"+oficio+"' AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                carga = new CargaNotificacionExtraEntity();
                carga.setId_carga_extra(rs.getString(1));
                carga.setOficio(rs.getString(2));
                carga.setAnio(rs.getInt(3));
                carga.setExpe(new ExpedienteEntity(rs.getString(4)));
                carga.setFecha_recibida(rs.getDate(5));
                carga.setAuto(new AutoridadNotificacionEntity(rs.getString(6),rs.getString(7),""));
                carga.setTipo_notificacion(rs.getString(8));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return carga;
    }
    
    public DescargaNotificacionExtraEntity getDescarga(String oficio, Integer anio){
        DescargaNotificacionExtraEntity descarga = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_descarga_extra, id_carga_extra, p.id_autoridad, c.nombre, expediente, oficio, anio, tipo_notificacion, "
                + "id_muncipio, g.nombre, fecha_deposito, spm, fecha_recepcion, fecha_notificacion, p.id_destino, cd.nombre, fecha_destino "
                + "FROM pri_descarga_notificaciones_extra p "
                + "LEFT OUTER JOIN cat_autoridades_notificaciones c ON p.id_autoridad=c.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios g ON p.id_muncipio=g.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON p.id_destino=cd.id_destino "
                + "WHERE oficio = '"+oficio+"' AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                descarga = new DescargaNotificacionExtraEntity();
                descarga.setId_descarga_extra(rs.getString(1));
                descarga.setId_carga_extra(rs.getString(2));
                descarga.setAuto(new AutoridadNotificacionEntity(rs.getString(3),rs.getString(4),""));
                descarga.setExpe(new ExpedienteEntity(rs.getString(5)));
                descarga.setOficio(rs.getString(6));
                descarga.setAnio(rs.getInt(7));                
                descarga.setTipo_notificacion(rs.getString(8));
                descarga.setMun(null);
                if(rs.getString(9)!=null){
                    descarga.setMun(new MunicipioEntity(rs.getInt(9),rs.getString(10)));
                }
                descarga.setFecha_deposito(null);
                if(rs.getDate(11)!=null){
                    descarga.setFecha_deposito(rs.getDate(11));
                }
                descarga.setSpm(rs.getString(12));
                descarga.setFecha_recepcion(null);
                if(rs.getDate(13)!=null){
                    descarga.setFecha_recepcion(rs.getDate(13));
                }
                descarga.setFecha_notificacion(rs.getDate(14));
                descarga.setDesti(new DestinoEntity(rs.getString(15),rs.getString(16),""));
                descarga.setFecha_destino(rs.getDate(17));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return descarga;
    }

    
    public ErrorEntity deleteCarga(Connection conx, String id_carga){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_carga_notificaciones_extra WHERE id_carga_extra = '"+id_carga+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteDescarga(Connection conx, String id_descarga){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_descarga_notificaciones_extra WHERE id_descarga_extra = '"+id_descarga+"'";
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
