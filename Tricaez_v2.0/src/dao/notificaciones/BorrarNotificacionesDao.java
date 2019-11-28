package dao.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoEntity;
import entitys.CargaNotificacionEntity;
import entitys.DescargaNotificacionEntity;
import entitys.DestinoEntity;
import entitys.ExpedienteEntity;
import entitys.MunicipioEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
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
public class BorrarNotificacionesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public BorrarNotificacionesDao() {
    }
    
    public RegistroAutoEntity getAuto(Integer folio, Integer anio){
        RegistroAutoEntity reg = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_registro_autos, p.expediente, p.folio, p.anio, p.id_tipo_auto, p.fecha_auto, p.fecha_acuerdo, c.nombre "
                + "FROM pri_registro_autos p INNER JOIN cat_autos c ON p.id_tipo_auto=c.id_tipo_auto WHERE p.folio = "+folio+" AND p.anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                reg = new RegistroAutoEntity();
                reg.setId_registro_auto(rs.getString(1));
                reg.setExp(new ExpedienteEntity(rs.getString(2)));
                reg.setFolio(rs.getInt(3));
                reg.setAnio(rs.getInt(4));
                reg.setTipo_auto(new AutoEntity(rs.getString(5),rs.getString(8),""));
                reg.setFecha_auto(u.cambiaFechaADiagonal(rs.getString(6)));
                reg.setFecha_acuerdo(u.cambiaFechaADiagonal(rs.getString(7)));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return reg;
    }
    
    public ArrayList getActores(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, c.id_actor, CONCAT(a.nombre,' ',a.paterno,' ',a.materno), "//1-3
                + "(CASE WHEN c.noti_correo IS TRUE THEN 'CORREO' WHEN c.noti_lista IS TRUE THEN 'LISTA' ELSE 'PERSONAL' END) AS noti, "//4
                + "c.fecha_recibida, "//5
                + "d.id_descarga_noti, d.tipo_notificacion, d.id_municipio, m.nombre, d.fecha_notificacion, d.fecha_deposito, "//6-11
                + "d.fecha_recepcion, d.spm, d.fecha_destino, d.id_destino, cd.nombre "//12-16
                + "FROM pri_carga_notificaciones c "
                + "LEFT OUTER JOIN pri_actores a ON c.id_actor=a.id_actor "
                + "LEFT OUTER JOIN pri_descarga_notificaciones d ON c.id_carga_noti=d.id_carga_noti "
                + "LEFT OUTER JOIN gen_municipios m ON d.id_municipio=m.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON d.id_destino=cd.id_destino "
                + "WHERE c.id_relacion = '"+id_relacion+"' AND c.id_actor IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionEntity carga = new CargaNotificacionEntity();
                carga.setId_carga(rs.getString(1));
                carga.setId_parte(rs.getString(2));
                carga.setNombre(rs.getString(3));
                carga.setTipo_notificacion(rs.getString(4));
                carga.setFecha_recibida(rs.getDate(5));
                if(rs.getString(6)==null)
                    carga.setDescarga(null);
                else{
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_descarga(rs.getString(6));
                    descarga.setTipo_notificacion(rs.getString(7));
                    if(rs.getString(8)==null)
                        descarga.setMun(null);
                    else{
                        descarga.setMun(new MunicipioEntity(rs.getInt(8),rs.getString(9)));
                    }
                    descarga.setFecha_notificacion(rs.getDate(10));
                    descarga.setFecha_deposito(rs.getDate(11));
                    descarga.setFecha_recepcion(rs.getDate(12));
                    descarga.setSpm(rs.getString(13));
                    descarga.setFecha_destino(rs.getDate(14));
                    descarga.setDesti(new DestinoEntity(rs.getString(15),rs.getString(16),""));
                    carga.setDescarga(descarga);
               }
               array.add(carga);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return array;
    }
    
    public ArrayList getAutoridades(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, c.id_autoridad, a.nombre, "//1-3
                + "(CASE WHEN c.noti_correo IS TRUE THEN 'CORREO' WHEN c.noti_lista IS TRUE THEN 'LISTA' ELSE 'OFICIO' END) AS noti, "//4
                + "c.fecha_recibida, c.oficio, "//5-6
                + "d.id_descarga_noti, d.tipo_notificacion, d.id_municipio, m.nombre, d.fecha_notificacion, d.fecha_deposito, "//7-122
                + "d.fecha_recepcion, d.spm, d.fecha_destino, d.id_destino, cd.nombre, d.oficio "//13-18
                + "FROM pri_carga_notificaciones c "
                + "LEFT OUTER JOIN cat_autoridades a ON c.id_autoridad=a.id_autoridad "
                + "LEFT OUTER JOIN pri_descarga_notificaciones d ON c.id_carga_noti=d.id_carga_noti "
                + "LEFT OUTER JOIN gen_municipios m ON d.id_municipio=m.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON d.id_destino=cd.id_destino "
                + "WHERE c.id_relacion = '"+id_relacion+"' AND c.id_autoridad IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionEntity carga = new CargaNotificacionEntity();
                carga.setId_carga(rs.getString(1));
                carga.setId_parte(rs.getString(2));
                carga.setNombre(rs.getString(3));
                carga.setTipo_notificacion(rs.getString(4));
                carga.setFecha_recibida(rs.getDate(5));
                carga.setOficio(rs.getString(6));
                if(rs.getString(7)==null)
                    carga.setDescarga(null);
                else{
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_descarga(rs.getString(7));
                    descarga.setTipo_notificacion(rs.getString(8));
                    if(rs.getString(9)==null)
                        descarga.setMun(null);
                    else{
                        descarga.setMun(new MunicipioEntity(rs.getInt(9),rs.getString(10)));
                    }
                    descarga.setFecha_notificacion(rs.getDate(11));
                    descarga.setFecha_deposito(rs.getDate(12));
                    descarga.setFecha_recepcion(rs.getDate(13));
                    descarga.setSpm(rs.getString(14));
                    descarga.setFecha_destino(rs.getDate(15));
                    descarga.setDesti(new DestinoEntity(rs.getString(16),rs.getString(17),""));
                    descarga.setOficio(rs.getString(18));
                    carga.setDescarga(descarga);
               }
               array.add(carga);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return array;
    }
    
    public ArrayList getTerceroAutoridades(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, c.id_tercero_autoridad, a.nombre, "//1-3
                + "(CASE WHEN c.noti_correo IS TRUE THEN 'CORREO' WHEN c.noti_lista IS TRUE THEN 'LISTA' ELSE 'OFICIO' END) AS noti, "//4
                + "c.fecha_recibida, c.oficio, "//5-6
                + "d.id_descarga_noti, d.tipo_notificacion, d.id_municipio, m.nombre, d.fecha_notificacion, d.fecha_deposito, "//7-122
                + "d.fecha_recepcion, d.spm, d.fecha_destino, d.id_destino, cd.nombre, d.oficio "//13-18
                + "FROM pri_carga_notificaciones c "
                + "LEFT OUTER JOIN cat_autoridades a ON c.id_tercero_autoridad=a.id_autoridad "
                + "LEFT OUTER JOIN pri_descarga_notificaciones d ON c.id_carga_noti=d.id_carga_noti "
                + "LEFT OUTER JOIN gen_municipios m ON d.id_municipio=m.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON d.id_destino=cd.id_destino "
                + "WHERE c.id_relacion = '"+id_relacion+"' AND c.id_tercero_autoridad IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionEntity carga = new CargaNotificacionEntity();
                carga.setId_carga(rs.getString(1));
                carga.setId_parte(rs.getString(2));
                carga.setNombre(rs.getString(3));
                carga.setTipo_notificacion(rs.getString(4));
                carga.setFecha_recibida(rs.getDate(5));
                carga.setOficio(rs.getString(6));
                if(rs.getString(7)==null)
                    carga.setDescarga(null);
                else{
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_descarga(rs.getString(7));
                    descarga.setTipo_notificacion(rs.getString(8));
                    if(rs.getString(9)==null)
                        descarga.setMun(null);
                    else{
                        descarga.setMun(new MunicipioEntity(rs.getInt(9),rs.getString(10)));
                    }
                    descarga.setFecha_notificacion(rs.getDate(11));
                    descarga.setFecha_deposito(rs.getDate(12));
                    descarga.setFecha_recepcion(rs.getDate(13));
                    descarga.setSpm(rs.getString(14));
                    descarga.setFecha_destino(rs.getDate(15));
                    descarga.setDesti(new DestinoEntity(rs.getString(16),rs.getString(17),""));
                    descarga.setOficio(rs.getString(18));
                    carga.setDescarga(descarga);
               }
               array.add(carga);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return array;
    }
    
    public ArrayList getTerceroActores(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, c.id_tercero_actor, CONCAT(a.nombre,' ',a.paterno,' ',a.materno), "//1-3
                + "(CASE WHEN c.noti_correo IS TRUE THEN 'CORREO' WHEN c.noti_lista IS TRUE THEN 'LISTA' ELSE 'PERSONAL' END) AS noti, "//4
                + "c.fecha_recibida, "//5
                + "d.id_descarga_noti, d.tipo_notificacion, d.id_municipio, m.nombre, d.fecha_notificacion, d.fecha_deposito, "//6-11
                + "d.fecha_recepcion, d.spm, d.fecha_destino, d.id_destino, cd.nombre "//12-16
                + "FROM pri_carga_notificaciones c "
                + "LEFT OUTER JOIN pri_terceros_actores a ON c.id_tercero_actor=a.id_tercero_actor "
                + "LEFT OUTER JOIN pri_descarga_notificaciones d ON c.id_carga_noti=d.id_carga_noti "
                + "LEFT OUTER JOIN gen_municipios m ON d.id_municipio=m.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON d.id_destino=cd.id_destino "
                + "WHERE c.id_relacion = '"+id_relacion+"' AND c.id_tercero_actor IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionEntity carga = new CargaNotificacionEntity();
                carga.setId_carga(rs.getString(1));
                carga.setId_parte(rs.getString(2));
                carga.setNombre(rs.getString(3));
                carga.setTipo_notificacion(rs.getString(4));
                carga.setFecha_recibida(rs.getDate(5));
                if(rs.getString(6)==null)
                    carga.setDescarga(null);
                else{
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_descarga(rs.getString(6));
                    descarga.setTipo_notificacion(rs.getString(7));
                    if(rs.getString(8)==null)
                        descarga.setMun(null);
                    else{
                        descarga.setMun(new MunicipioEntity(rs.getInt(8),rs.getString(9)));
                    }
                    descarga.setFecha_notificacion(rs.getDate(10));
                    descarga.setFecha_deposito(rs.getDate(11));
                    descarga.setFecha_recepcion(rs.getDate(12));
                    descarga.setSpm(rs.getString(13));
                    descarga.setFecha_destino(rs.getDate(14));
                    descarga.setDesti(new DestinoEntity(rs.getString(15),rs.getString(16),""));
                    carga.setDescarga(descarga);
               }
               array.add(carga);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return array;
    }
    
    public ArrayList getAutoridadesAjenas(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, c.id_autoridad_ajena, a.nombre, "//1-3
                + "(CASE WHEN c.noti_correo IS TRUE THEN 'CORREO' WHEN c.noti_lista IS TRUE THEN 'LISTA' ELSE 'OFICIO' END) AS noti, "//4
                + "c.fecha_recibida, c.oficio, "//5-6
                + "d.id_descarga_noti, d.tipo_notificacion, d.id_municipio, m.nombre, d.fecha_notificacion, d.fecha_deposito, "//7-122
                + "d.fecha_recepcion, d.spm, d.fecha_destino, d.id_destino, cd.nombre, d.oficio "//13-18
                + "FROM pri_carga_notificaciones c "
                + "LEFT OUTER JOIN cat_autoridades a ON c.id_autoridad_ajena=a.id_autoridad "
                + "LEFT OUTER JOIN pri_descarga_notificaciones d ON c.id_carga_noti=d.id_carga_noti "
                + "LEFT OUTER JOIN gen_municipios m ON d.id_municipio=m.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON d.id_destino=cd.id_destino "
                + "WHERE c.id_relacion = '"+id_relacion+"' AND c.id_autoridad_ajena IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionEntity carga = new CargaNotificacionEntity();
                carga.setId_carga(rs.getString(1));
                carga.setId_parte(rs.getString(2));
                carga.setNombre(rs.getString(3));
                carga.setTipo_notificacion(rs.getString(4));
                carga.setFecha_recibida(rs.getDate(5));
                carga.setOficio(rs.getString(6));
                if(rs.getString(7)==null)
                    carga.setDescarga(null);
                else{
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_descarga(rs.getString(7));
                    descarga.setTipo_notificacion(rs.getString(8));
                    if(rs.getString(9)==null)
                        descarga.setMun(null);
                    else{
                        descarga.setMun(new MunicipioEntity(rs.getInt(9),rs.getString(10)));
                    }
                    descarga.setFecha_notificacion(rs.getDate(11));
                    descarga.setFecha_deposito(rs.getDate(12));
                    descarga.setFecha_recepcion(rs.getDate(13));
                    descarga.setSpm(rs.getString(14));
                    descarga.setFecha_destino(rs.getDate(15));
                    descarga.setDesti(new DestinoEntity(rs.getString(16),rs.getString(17),""));
                    descarga.setOficio(rs.getString(18));
                    carga.setDescarga(descarga);
               }
               array.add(carga);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return array;
    }
    
    public ArrayList getPersonasAjenas(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, c.id_persona_ajena, CONCAT(a.nombre,' ',a.paterno,' ',a.materno), "//1-3
                + "(CASE WHEN c.noti_correo IS TRUE THEN 'CORREO' WHEN c.noti_lista IS TRUE THEN 'LISTA' ELSE 'PERSONAL' END) AS noti, "//4
                + "c.fecha_recibida, "//5
                + "d.id_descarga_noti, d.tipo_notificacion, d.id_municipio, m.nombre, d.fecha_notificacion, d.fecha_deposito, "//6-11
                + "d.fecha_recepcion, d.spm, d.fecha_destino, d.id_destino, cd.nombre "//12-16
                + "FROM pri_carga_notificaciones c "
                + "LEFT OUTER JOIN pri_personas_ajenas a ON c.id_persona_ajena=a.id_persona_ajena "
                + "LEFT OUTER JOIN pri_descarga_notificaciones d ON c.id_carga_noti=d.id_carga_noti "
                + "LEFT OUTER JOIN gen_municipios m ON d.id_municipio=m.id_municipio "
                + "LEFT OUTER JOIN cat_destinos cd ON d.id_destino=cd.id_destino "
                + "WHERE c.id_relacion = '"+id_relacion+"' AND c.id_persona_ajena IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                CargaNotificacionEntity carga = new CargaNotificacionEntity();
                carga.setId_carga(rs.getString(1));
                carga.setId_parte(rs.getString(2));
                carga.setNombre(rs.getString(3));
                carga.setTipo_notificacion(rs.getString(4));
                carga.setFecha_recibida(rs.getDate(5));
                if(rs.getString(6)==null)
                    carga.setDescarga(null);
                else{
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_descarga(rs.getString(6));
                    descarga.setTipo_notificacion(rs.getString(7));
                    if(rs.getString(8)==null)
                        descarga.setMun(null);
                    else{
                        descarga.setMun(new MunicipioEntity(rs.getInt(8),rs.getString(9)));
                    }
                    descarga.setFecha_notificacion(rs.getDate(10));
                    descarga.setFecha_deposito(rs.getDate(11));
                    descarga.setFecha_recepcion(rs.getDate(12));
                    descarga.setSpm(rs.getString(13));
                    descarga.setFecha_destino(rs.getDate(14));
                    descarga.setDesti(new DestinoEntity(rs.getString(15),rs.getString(16),""));
                    carga.setDescarga(descarga);
               }
               array.add(carga);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return array;
    }
    
    public ErrorEntity deleteCarga(Connection conx, String id_carga){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_carga_notificaciones WHERE id_carga_noti = '"+id_carga+"'";
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
            String query = "DELETE FROM pri_descarga_notificaciones WHERE id_descarga_noti = '"+id_descarga+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    
    
    
    
    public RelacionEntity getRelacion(Integer folio, Integer anio){
        RelacionEntity rel = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT DISTINCT p.id_relacion, p.id_registro_autos, p.id_reg_sentencia, p.id_reg_resolucion, p.folio, p.anio "
                + "FROM pri_relacion_folios p LEFT JOIN pri_carga_notificaciones pc ON p.id_relacion=pc.id_relacion "
                + "WHERE pc.id_relacion IS NOT NULL AND p.folio = "+folio+" AND p.anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                rel = new RelacionEntity();
                rel.setId_relacion(rs.getString(1));
                rel.setId_registro_autos(rs.getString(2));
                rel.setId_reg_sentencia(rs.getString(3));
                rel.setId_reg_resolucion(rs.getString(4));
                rel.setFolio(rs.getInt(5));
                rel.setAnio(rs.getInt(6));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return rel;
    }
    
    public ArrayList getAuto(String id_registro_autos){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.expediente, pl.folio, pl.anio, p.fecha_auto, p.fecha_acuerdo, c.nombre, c.tipo FROM pri_registro_autos p "
                + "INNER JOIN cat_autos c ON p.id_tipo_auto=c.id_tipo_auto "
                + "LEFT OUTER JOIN pri_relacion_folios pl ON pl.id_registro_autos=p.id_registro_autos WHERE p.id_registro_autos = '"+id_registro_autos+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                if(rs.getBoolean(7)){
                    array.add(new Object[] {"Tipo","Auto de Mesas de Tramite"});
                }else{
                    array.add(new Object[] {"Tipo","Auto de Secretaría de Acuerdos"});   
                }
                array.add(new Object[] {"Nombre del Auto",rs.getString(6)});
                array.add(new Object[] {"Expediente",rs.getString(1)});
                array.add(new Object[] {"Folio",rs.getString(2)});
                array.add(new Object[] {"Año",rs.getString(3)});
                array.add(new Object[] {"Fecha del Auto",u.cambiaFechaADiagonal(rs.getString(4))});
                array.add(new Object[] {"Fecha de Lista de Acuerdos",u.cambiaFechaADiagonal(rs.getString(5))});
                /*--------------------------------------------------------------
                    CÓDIGO ORIGINAL DE MARCO (DABA LA FECHA RESTÁNDOLE UN DÍA)
                ---------------------------------------------------------------*/
                /*array.add(new Object[] {"Fecha del Auto",u.dateToString(rs.getDate(4))});
                array.add(new Object[] {"Fecha de Lista de Acuerdos",u.dateToString(rs.getDate(5))});*/
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
    
    public ArrayList getResolucion(String id_reg_resolucion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.expediente, fecha_resolucion, fecha_acuerdo, IF(fecha IS NULL,'',fecha), IF(nombre IS NULL,'',nombre), folio, anio FROM pri_registro_resoluciones p "
                + "LEFT OUTER JOIN pri_inicio_tramites pir ON p.id_inicio_tramite=pir.id_inicio_tramite "
                + "LEFT OUTER JOIN cat_tipo_amparos c ON pir.id_tipo_amparo=c.id_tipo_amparo "
                + "INNER JOIN tribunal.pri_relacion_folios pl ON pl.id_reg_resolucion=p.id_reg_resolucion "
                + "WHERE pl.id_reg_resolucion = '"+id_reg_resolucion+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(new Object[] {"Tipo","Resolucion de la Secretaría de Acuerdos"});
                array.add(new Object[] {"Expediente",rs.getString(1)});
                array.add(new Object[] {"Trámite",rs.getString(5)});
                array.add(new Object[] {"Fecha del Trámite",u.dateToString(rs.getDate(4))});
                array.add(new Object[] {"Folio",rs.getString(6)});
                array.add(new Object[] {"Año",rs.getString(7)});
                array.add(new Object[] {"Fecha de la Resolucion",u.dateToString(rs.getDate(2))});
                array.add(new Object[] {"Fecha de Lista de Acuerdos",u.dateToString(rs.getDate(3))});
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
    
    public ArrayList getSentencia(String id_reg_sentencia){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT expediente, fecha_audiencia, fecha_sentencia, fecha_acuerdo, IF(c.nombre IS NULL,'',c.nombre), IF(cs.nombre IS NULL,'',cs.nombre), folio, anio FROM pri_registro_sentencias p "
                + "LEFT OUTER JOIN cat_tipo_documentos c ON p.id_tipo_documento=c.id_tipo_documento "
                + "LEFT OUTER JOIN cat_tipo_sentencias cs ON p.id_tipo_sentencia=cs.id_tipo_sentencia "
                + "INNER JOIN pri_relacion_folios pl ON pl.id_reg_sentencia=p.id_reg_sentencia "
                + "WHERE pl.id_reg_sentencia = '"+id_reg_sentencia+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(new Object[] {"Tipo","Sentencia - Resolución de Mesas de Tramite"});
                array.add(new Object[] {"Expediente",rs.getString(1)});
                array.add(new Object[] {"Fecha de Audiencia",u.dateToString(rs.getDate(2))});
                array.add(new Object[] {"Fecha de Sentencia",u.dateToString(rs.getDate(3))});
                array.add(new Object[] {"Fecha de Lista de Acuerdos",u.dateToString(rs.getDate(4))});
                array.add(new Object[] {"Folio",rs.getString(7)});
                array.add(new Object[] {"Año",rs.getString(8)});
                array.add(new Object[] {"Tipo de Documento",rs.getString(5)});
                array.add(new Object[] {"Tipo de Sentencia",rs.getString(6)});
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
    
    public String getAutoExpediente (String id_registro_autos){
        String exp = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.expediente FROM pri_registro_autos p WHERE id_registro_autos = '"+id_registro_autos+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                exp = rs.getString(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return exp;
    }
    
    public String getResolucionExpediente(String id_reg_resolucion){
        String exp = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.expediente FROM pri_registro_resoluciones p "
                + "INNER JOIN tribunal.pri_relacion_folios pl ON pl.id_reg_resolucion=p.id_reg_resolucion "
                + "WHERE pl.id_reg_resolucion = '"+id_reg_resolucion+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                exp = rs.getString(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return exp;
    }
    
    public String getSentenciaExpediente(String id_reg_sentencia){
        String exp = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT expediente FROM pri_registro_sentencias p "
                + "INNER JOIN pri_relacion_folios pl ON pl.id_reg_sentencia=p.id_reg_sentencia "
                + "WHERE pl.id_reg_sentencia = '"+id_reg_sentencia+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                exp = rs.getString(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return exp;
    }
    
}
