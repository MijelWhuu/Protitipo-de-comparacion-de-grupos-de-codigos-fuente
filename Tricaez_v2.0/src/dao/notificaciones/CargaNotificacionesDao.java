package dao.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoEntity;
import entitys.CargaNotificacionEntity;
import entitys.ComboFechasCargaEntity;
import entitys.ExpedienteEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mavg
 */
public class CargaNotificacionesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public CargaNotificacionesDao() {
    }
    
    public RelacionEntity getRelacion(Integer folio, Integer anio){
        RelacionEntity rel = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        /*String query = "SELECT p.id_relacion, p.id_registro_autos, p.id_reg_sentencia, p.id_reg_resolucion, p.folio, p.anio "
                + "FROM pri_relacion_folios p LEFT JOIN pri_carga_notificaciones pc ON p.id_relacion=pc.id_relacion "
                + "WHERE pc.id_relacion IS NULL AND p.folio = "+folio+" AND p.anio = "+anio;
        */
        String query = "SELECT p.id_relacion, p.id_registro_autos, p.id_reg_sentencia, p.id_reg_resolucion, p.folio, p.anio "
                + "FROM pri_relacion_folios p WHERE p.folio = "+folio+" AND p.anio = "+anio;
        
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
    
    public ArrayList getActores(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_actor, CONCAT(a.nombre,' ',a.paterno,' ',a.materno) FROM pri_actores a  WHERE a.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               String []datos = new String[2];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               array.add(datos);
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
    
    public ArrayList getAutoridades(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT au.id_autoridad, c.nombre FROM rel_expediente_has_cat_autoridades au INNER JOIN cat_autoridades c ON au.id_autoridad = c.id_autoridad WHERE au.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               String []datos = new String[2];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               array.add(datos);
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
    
    public ArrayList getTerceroAutoridades(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT ta.id_autoridad, c.nombre FROM rel_expediente_has_pri_terceros_autoridades ta INNER JOIN cat_autoridades c ON ta.id_autoridad = c.id_autoridad WHERE ta.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               String []datos = new String[2];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               array.add(datos);
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
    
    public ArrayList getTerceroActores(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tercero_actor, CONCAT(nombre,' ',paterno,' ',materno) FROM pri_terceros_actores t WHERE t.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               String []datos = new String[2];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               array.add(datos);
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
    
    public ArrayList getAutoridadesAjenas(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT aa.id_autoridad, c.nombre, '04' FROM rel_expediente_has_pri_autoridades_ajenas aa INNER JOIN cat_autoridades c ON aa.id_autoridad = c.id_autoridad WHERE aa.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               String []datos = new String[2];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               array.add(datos);
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
    
    public ArrayList getPersonasAjenas(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_persona_ajena, CONCAT(nombre,' ',paterno,' ',materno), '05' FROM pri_personas_ajenas pa WHERE pa.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               String []datos = new String[2];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               array.add(datos);
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
    
    
    public ErrorEntity saveNotificacion(Connection conx, CargaNotificacionEntity carga, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_carga_notificaciones VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, carga.getId_carga());
            save.setString(2, carga.getAuto().getId_registro_auto());
            save.setString(3, carga.getTipo_notificacion());
            save.setString(4, carga.getOficio());
            save.setDate(5, u.convertirDateToSqlDate(carga.getFecha_recibida()));
            save.setBoolean(6, carga.getCorreo());
            save.setBoolean(7, carga.getLista());
            
            switch(carga.getTipo_parte()){
                case 1: 
                    save.setString(8, carga.getId_parte());
                    save.setNull(9, java.sql.Types.VARCHAR);
                    save.setNull(10, java.sql.Types.VARCHAR);
                    save.setNull(11, java.sql.Types.VARCHAR);
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    break;
                case 2: 
                    save.setNull(8, java.sql.Types.VARCHAR);
                    save.setString(9, carga.getId_parte());
                    save.setNull(10, java.sql.Types.VARCHAR);
                    save.setNull(11, java.sql.Types.VARCHAR);
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    break;
                case 3:
                    save.setNull(8, java.sql.Types.VARCHAR);
                    save.setNull(9, java.sql.Types.VARCHAR);
                    save.setString(10, carga.getId_parte());
                    save.setNull(11, java.sql.Types.VARCHAR);
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    break;
                case 4:
                    save.setNull(8, java.sql.Types.VARCHAR);
                    save.setNull(9, java.sql.Types.VARCHAR);
                    save.setNull(10, java.sql.Types.VARCHAR);
                    save.setString(11, carga.getId_parte());
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    break;
                case 5:
                    save.setNull(8, java.sql.Types.VARCHAR);
                    save.setNull(9, java.sql.Types.VARCHAR);
                    save.setNull(10, java.sql.Types.VARCHAR);
                    save.setNull(11, java.sql.Types.VARCHAR);
                    save.setString(12, carga.getId_parte());
                    save.setNull(13, java.sql.Types.VARCHAR);
                    break;
                case 6:
                    save.setNull(8, java.sql.Types.VARCHAR);
                    save.setNull(9, java.sql.Types.VARCHAR);
                    save.setNull(10, java.sql.Types.VARCHAR);
                    save.setNull(11, java.sql.Types.VARCHAR);
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setString(13, carga.getId_parte());
                    break;
            }
            save.setString(14, sesion.getId_usuario());
            save.setDate(15, sesion.getFechaActual());
            save.setDate(16, sesion.getFechaCero());
            System.out.println("dao_fun_001:   "+save);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdCargaNotificacion(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_carga_noti,10) as SIGNED INTEGER)),0) FROM "
                +"pri_carga_notificaciones WHERE SUBSTRING(id_carga_noti,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("C", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    
    public ArrayList getActoresNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_actor FROM pri_carga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_actor IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               array.add(rs.getString(1));
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
    
    public ArrayList getAutoridadesNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_autoridad FROM pri_carga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_autoridad IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
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
    
    public ArrayList getTerceroAutoridadesNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tercero_autoridad FROM pri_carga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_tercero_autoridad IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
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
    
    public ArrayList getTerceroActoresNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tercero_actor FROM pri_carga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_tercero_actor IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
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
    
    public ArrayList getAutoridadesAjenasNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_autoridad_ajena FROM pri_carga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_autoridad_ajena IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
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
    
    public ArrayList getPersonasAjenasNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_persona_ajena FROM pri_carga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_persona_ajena IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
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
    
    public ArrayList getFechasCargas(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_registro_autos AS id_oper, p.folio, p.anio, pg.fecha_auto AS fecha, '1' AS tipo "
                + "FROM pri_relacion_folios p LEFT JOIN pri_carga_notificaciones pc ON p.id_relacion=pc.id_relacion "
                + "INNER JOIN pri_registro_autos pg ON p.id_registro_autos=pg.id_registro_autos WHERE pc.id_relacion IS NULL AND p.anio>=2016 "
                + "UNION "
                + "SELECT p.id_reg_resolucion AS id_oper, p.folio, p.anio, pg.fecha_resolucion AS fecha, '2' AS tipo "
                + "FROM pri_relacion_folios p LEFT JOIN pri_carga_notificaciones pc ON p.id_relacion=pc.id_relacion "
                + "INNER JOIN pri_registro_resoluciones pg ON p.id_reg_resolucion=pg.id_reg_resolucion WHERE pc.id_relacion IS NULL AND p.anio>=2016 "
                + "UNION "
                + "SELECT p.id_reg_sentencia AS id_oper, p.folio, p.anio, pg.fecha_sentencia AS fecha, '3' AS tipo "
                + "FROM pri_relacion_folios p LEFT JOIN pri_carga_notificaciones pc ON p.id_relacion=pc.id_relacion "
                + "INNER JOIN pri_registro_sentencias pg ON p.id_reg_sentencia=pg.id_reg_sentencia WHERE pc.id_relacion IS NULL AND p.anio>=2016 "
                + "ORDER BY anio, folio";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                ComboFechasCargaEntity cmb = new ComboFechasCargaEntity();
                cmb.setId(rs.getString(1));
                cmb.setFolio(rs.getString(2));
                cmb.setAnio(rs.getString(3));
                cmb.setFecha(rs.getDate(4));
                cmb.setFechaTxt(u.dateToString(rs.getDate(4)));
                cmb.setTipo(rs.getInt(5));
                array.add(cmb);
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
}
