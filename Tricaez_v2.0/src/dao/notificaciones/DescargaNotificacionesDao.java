package dao.notificaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoEntity;
import entitys.CargaNotificacionEntity;
import entitys.ComboAutoEntity;
import entitys.ComboFechaEntity;
import entitys.ComboFechasCargaEntity;
import entitys.DescargaNotificacionEntity;
import entitys.ExpedienteEntity;
import entitys.MunicipioEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class DescargaNotificacionesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public DescargaNotificacionesDao() {
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
    
    public ArrayList getActores(String id_registro_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti,"
                + "(CASE "
                + "WHEN c.noti_correo IS TRUE THEN 'CORREO' "
                + "WHEN c.noti_lista IS TRUE THEN 'LISTA' "
                + "WHEN c.tipo_notificacion = 'PERSONAL' THEN 'PERSONAL' "
                + "ELSE '[Elija una opción]' "
                + "END) AS noti, "
                + "c.id_actor,CONCAT(a.nombre,' ',a.paterno,' ',a.materno) "
                + "FROM pri_carga_notificaciones c INNER JOIN pri_actores a ON c.id_actor = a.id_actor "
                + "WHERE id_relacion = '"+id_registro_auto+"' AND c.id_actor IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               CargaNotificacionEntity carga = new CargaNotificacionEntity();
               carga.setId_carga(rs.getString(1));
               carga.setTipo_notificacion(rs.getString(2));
               carga.setId_parte(rs.getString(3));
               carga.setNombre(rs.getString(4));
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
    
    public ArrayList getAutoridades(String id_registro_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, (CASE "
                + "WHEN c.noti_correo IS TRUE THEN 'CORREO' "
                + "WHEN c.noti_lista IS TRUE THEN 'LISTA' "
                + "WHEN c.tipo_notificacion = 'OFICIO' THEN 'OFICIO' "
                + "ELSE '[Elija una opción]' "
                + "END) AS noti, c.oficio, if(a.id_municipio IS NULL, '-1', a.id_municipio), if(a.id_municipio IS NULL, '[Elija un municipio]', m.nombre), "
                + "c.id_autoridad, a.nombre "
                + "FROM pri_carga_notificaciones c INNER JOIN cat_autoridades a ON c.id_autoridad=a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON a.id_municipio=m.id_municipio "
                + "WHERE id_relacion = '"+id_registro_auto+"' AND c.id_autoridad IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               CargaNotificacionEntity carga = new CargaNotificacionEntity();
               carga.setId_carga(rs.getString(1));
               carga.setTipo_notificacion(rs.getString(2));
               carga.setOficio(rs.getString(3));
               carga.setMun(new MunicipioEntity(rs.getInt(4),rs.getString(5)));
               carga.setId_parte(rs.getString(6));
               carga.setNombre(rs.getString(7));
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
    
    public ArrayList getTerceroAutoridades(String id_registro_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, (CASE "
                + "WHEN c.noti_correo IS TRUE THEN 'CORREO' "
                + "WHEN c.noti_lista IS TRUE THEN 'LISTA' "
                + "WHEN c.tipo_notificacion = 'OFICIO' THEN 'OFICIO' "
                + "ELSE '[Elija una opción]' "
                + "END) AS noti, c.oficio, if(a.id_municipio IS NULL, '-1', a.id_municipio), if(a.id_municipio IS NULL, '[Elija un municipio]', m.nombre), "
                + "c.id_tercero_autoridad, a.nombre "
                + "FROM pri_carga_notificaciones c INNER JOIN cat_autoridades a ON c.id_tercero_autoridad=a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON a.id_municipio=m.id_municipio "
                + "WHERE id_relacion = '"+id_registro_auto+"' AND c.id_tercero_autoridad IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               CargaNotificacionEntity carga = new CargaNotificacionEntity();
               carga.setId_carga(rs.getString(1));
               carga.setTipo_notificacion(rs.getString(2));
               carga.setOficio(rs.getString(3));
               carga.setMun(new MunicipioEntity(rs.getInt(4),rs.getString(5)));
               carga.setId_parte(rs.getString(6));
               carga.setNombre(rs.getString(7));
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
    
    public ArrayList getTerceroActores(String id_registro_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti,"
                + "(CASE "
                + "WHEN c.noti_correo IS TRUE THEN 'CORREO' "
                + "WHEN c.noti_lista IS TRUE THEN 'LISTA' "
                + "WHEN c.tipo_notificacion = 'PERSONAL' THEN 'PERSONAL' "
                + "ELSE '[Elija una opción]' "
                + "END) AS noti, "
                + "c.id_tercero_actor,CONCAT(a.nombre,' ',a.paterno,' ',a.materno) "
                + "FROM pri_carga_notificaciones c INNER JOIN pri_terceros_actores a ON c.id_tercero_actor = a.id_tercero_actor "
                + "WHERE id_relacion = '"+id_registro_auto+"' AND c.id_tercero_actor IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               CargaNotificacionEntity carga = new CargaNotificacionEntity();
               carga.setId_carga(rs.getString(1));
               carga.setTipo_notificacion(rs.getString(2));
               carga.setId_parte(rs.getString(3));
               carga.setNombre(rs.getString(4));
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
    
    public ArrayList getAutoridadesAjenas(String id_registro_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti, (CASE "
                + "WHEN c.noti_correo IS TRUE THEN 'CORREO' "
                + "WHEN c.noti_lista IS TRUE THEN 'LISTA' "
                + "WHEN c.tipo_notificacion = 'OFICIO' THEN 'OFICIO' "
                + "ELSE '[Elija una opción]' "
                + "END) AS noti, c.oficio, if(a.id_municipio IS NULL, '-1', a.id_municipio), if(a.id_municipio IS NULL, '[Elija un municipio]', m.nombre), "
                + "c.id_autoridad_ajena, a.nombre "
                + "FROM pri_carga_notificaciones c INNER JOIN cat_autoridades a ON c.id_autoridad_ajena=a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON a.id_municipio=m.id_municipio "
                + "WHERE id_relacion = '"+id_registro_auto+"' AND c.id_autoridad_ajena IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               CargaNotificacionEntity carga = new CargaNotificacionEntity();
               carga.setId_carga(rs.getString(1));
               carga.setTipo_notificacion(rs.getString(2));
               carga.setOficio(rs.getString(3));
               carga.setMun(new MunicipioEntity(rs.getInt(4),rs.getString(5)));
               carga.setId_parte(rs.getString(6));
               carga.setNombre(rs.getString(7));
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
    
    public ArrayList getPersonasAjenas(String id_registro_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_carga_noti,"
                + "(CASE "
                + "WHEN c.noti_correo IS TRUE THEN 'CORREO' "
                + "WHEN c.noti_lista IS TRUE THEN 'LISTA' "
                + "WHEN c.tipo_notificacion = 'PERSONAL' THEN 'PERSONAL' "
                + "ELSE '[Elija una opción]' "
                + "END) AS noti, "
                + "c.id_persona_ajena,CONCAT(a.nombre,' ',a.paterno,' ',a.materno) "
                + "FROM pri_carga_notificaciones c INNER JOIN pri_personas_ajenas a ON c.id_persona_ajena = a.id_persona_ajena "
                + "WHERE id_relacion = '"+id_registro_auto+"' AND c.id_persona_ajena IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               CargaNotificacionEntity carga = new CargaNotificacionEntity();
               carga.setId_carga(rs.getString(1));
               carga.setTipo_notificacion(rs.getString(2));
               carga.setId_parte(rs.getString(3));
               carga.setNombre(rs.getString(4));
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
    
    private int getTotalMunicipios(){
        int total = 0;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM gen_municipios g";
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
            cleanVariables(rs);
        }
        return total+1;
    }
    
    public String[] getMunicipios(){
        String [] mun = new String[getTotalMunicipios()];
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT nombre FROM gen_municipios g ORDER BY g.id_municipio";
        try{
            mun[0] = "[Elija un municipio]";
            int pos = 1;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                mun[pos] = rs.getString(1);
                pos++;
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return mun;
    }
    
    public ErrorEntity saveNotificacion(Connection conx, DescargaNotificacionEntity carga, java.util.Date fecha_destino, String id_destino, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_descarga_notificaciones VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, carga.getId_descarga());
            save.setString(2, carga.getId_carga());
            save.setString(3, carga.getId_relacion());
            save.setString(4, carga.getTipo_notificacion());
            save.setString(5, carga.getOficio());
            
            if(carga.getMun()!=null) save.setInt(6, carga.getMun().getId_municipio());
            else save.setNull(6, java.sql.Types.INTEGER);
            
            if(carga.getFecha_notificacion()!=null) save.setDate(7, u.convertirDateToSqlDate(carga.getFecha_notificacion()));
            else save.setNull(7, java.sql.Types.DATE);
            
            if(carga.getFecha_deposito()!=null) save.setDate(8, u.convertirDateToSqlDate(carga.getFecha_deposito()));
            else save.setNull(8, java.sql.Types.DATE);
            
            if(carga.getFecha_recepcion()!=null) save.setDate(9, u.convertirDateToSqlDate(carga.getFecha_recepcion()));
            else save.setNull(9, java.sql.Types.DATE);
            
            save.setString(10, carga.getSpm());
            save.setString(11, carga.getObservaciones());
            
            switch(carga.getTipo_parte()){
                case 1: 
                    save.setString(12, carga.getId_parte());
                    save.setNull(13, java.sql.Types.VARCHAR);
                    save.setNull(14, java.sql.Types.VARCHAR);
                    save.setNull(15, java.sql.Types.VARCHAR);
                    save.setNull(16, java.sql.Types.VARCHAR);
                    save.setNull(17, java.sql.Types.VARCHAR);
                    break;
                case 2: 
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setString(13, carga.getId_parte());
                    save.setNull(14, java.sql.Types.VARCHAR);
                    save.setNull(15, java.sql.Types.VARCHAR);
                    save.setNull(16, java.sql.Types.VARCHAR);
                    save.setNull(17, java.sql.Types.VARCHAR);
                    break;
                case 3:
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    save.setString(14, carga.getId_parte());
                    save.setNull(15, java.sql.Types.VARCHAR);
                    save.setNull(16, java.sql.Types.VARCHAR);
                    save.setNull(17, java.sql.Types.VARCHAR);
                    break;
                case 4:
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    save.setNull(14, java.sql.Types.VARCHAR);
                    save.setString(15, carga.getId_parte());
                    save.setNull(16, java.sql.Types.VARCHAR);
                    save.setNull(17, java.sql.Types.VARCHAR);
                    break;
                case 5:
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    save.setNull(14, java.sql.Types.VARCHAR);
                    save.setNull(15, java.sql.Types.VARCHAR);
                    save.setString(16, carga.getId_parte());
                    save.setNull(17, java.sql.Types.VARCHAR);
                    break;
                case 6:
                    save.setNull(12, java.sql.Types.VARCHAR);
                    save.setNull(13, java.sql.Types.VARCHAR);
                    save.setNull(14, java.sql.Types.VARCHAR);
                    save.setNull(15, java.sql.Types.VARCHAR);
                    save.setNull(16, java.sql.Types.VARCHAR);
                    save.setString(17, carga.getId_parte());
                    break;
            }
            save.setDate(18, u.convertirDateToSqlDate(fecha_destino));
            save.setString(19, id_destino);
            save.setString(20, sesion.getId_usuario());
            save.setDate(21, sesion.getFechaActual());
            save.setDate(22, sesion.getFechaCero());
            System.out.println("Dao_fun_001:  "+save);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdDescargaNotificacion(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_descarga_noti,10) as SIGNED INTEGER)),0) FROM "
                +"pri_descarga_notificaciones WHERE SUBSTRING(id_descarga_noti,1,8) = '"+u.getFormatoFechaString()+"'";
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
    
    public ArrayList getActoresNoti(String id_relacion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_actor FROM pri_descarga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_actor IS NOT NULL";
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
        String query = "SELECT id_autoridad FROM pri_descarga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_autoridad IS NOT NULL";
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
        String query = "SELECT id_tercero_autoridad FROM pri_descarga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_tercero_autoridad IS NOT NULL";
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
        String query = "SELECT id_tercero_actor FROM pri_descarga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_tercero_actor IS NOT NULL";
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
        String query = "SELECT id_autoridad_ajena FROM pri_descarga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_autoridad_ajena IS NOT NULL";
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
        String query = "SELECT id_persona_ajena FROM pri_descarga_notificaciones WHERE id_relacion = '"+id_relacion+"' AND id_persona_ajena IS NOT NULL";
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
        
    public ArrayList getAutosFechasCargas(java.util.Date fecha){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        try{
            String query = "SELECT cn.id_registro_autos, ra.folio, ra.anio FROM pri_carga_notificaciones cn "
                    + "INNER JOIN pri_registro_autos ra ON cn.id_registro_autos = ra.id_registro_autos "
                    + "WHERE cn.id_carga_noti NOT IN(SELECT dn.id_carga_noti FROM pri_descarga_notificaciones dn) "
                    + "AND cn.fecha_recibida = ? GROUP BY cn.id_registro_autos";
            PreparedStatement save = con.prepareStatement(query);
            save.setDate(1, u.convertirDateToSqlDate(fecha));
            rs = save.executeQuery();            
            while (rs.next()){
                array.add(new ComboAutoEntity(rs.getString(1),rs.getString(2),rs.getString(3)));
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
    
    public RelacionEntity getRelacion(Integer folio, Integer anio){
        RelacionEntity rel = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT DISTINCT p.id_relacion, p.id_registro_autos, p.id_reg_sentencia, p.id_reg_resolucion, p.folio, p.anio "
                + "FROM pri_relacion_folios p "
                + "INNER JOIN pri_carga_notificaciones pcc ON pcc.id_relacion=p.id_relacion "
                + "LEFT JOIN pri_descarga_notificaciones pc ON pc.id_relacion=p.id_relacion "
                + "WHERE p.folio = "+folio+" AND p.anio = "+anio;
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
    
    public ArrayList getFechasCargas(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT DISTINCT p.id_registro_autos AS id_oper, p.folio, p.anio, r.fecha_auto AS fecha, '1' AS tipo "
                + "FROM tribunal.pri_relacion_folios p "
                + "INNER JOIN tribunal.pri_carga_notificaciones pc ON pc.id_relacion=p.id_relacion "
                + "LEFT JOIN tribunal.pri_descarga_notificaciones pd ON pd.id_carga_noti=pc.id_carga_noti "
                //+ "LEFT JOIN tribunal.pri_descarga_notificaciones pd ON pd.id_relacion=p.id_relacion "
                + "INNER JOIN tribunal.pri_registro_autos r ON r.id_registro_autos=p.id_registro_autos "
                + "WHERE pd.id_carga_noti IS NULL "
                + "UNION "
                + "SELECT DISTINCT p.id_reg_resolucion AS id_oper, p.folio, p.anio, r.fecha_resolucion AS fecha, '2' AS tipo "
                + "FROM tribunal.pri_relacion_folios p "
                + "INNER JOIN tribunal.pri_carga_notificaciones pc ON pc.id_relacion=p.id_relacion "
                + "LEFT JOIN tribunal.pri_descarga_notificaciones pd ON pd.id_carga_noti=pc.id_carga_noti "
                //+ "LEFT JOIN tribunal.pri_descarga_notificaciones pd ON pd.id_relacion=p.id_relacion "
                + "INNER JOIN tribunal.pri_registro_resoluciones r ON r.id_reg_resolucion=p.id_reg_resolucion "
                + "WHERE pd.id_carga_noti IS NULL "
                + "UNION "
                + "SELECT DISTINCT p.id_reg_sentencia AS id_oper, p.folio, p.anio, r.fecha_sentencia AS fecha, '3' AS tipo "
                + "FROM tribunal.pri_relacion_folios p "
                + "INNER JOIN tribunal.pri_carga_notificaciones pc ON pc.id_relacion=p.id_relacion "
                + "LEFT JOIN tribunal.pri_descarga_notificaciones pd ON pd.id_carga_noti=pc.id_carga_noti "
                //+ "LEFT JOIN tribunal.pri_descarga_notificaciones pd ON pd.id_relacion=p.id_relacion "
                + "INNER JOIN tribunal.pri_registro_sentencias r ON r.id_reg_sentencia=p.id_reg_sentencia "
                + "WHERE pd.id_carga_noti IS NULL "
                + "ORDER BY anio,folio";
        System.out.println(query);
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
/*
SELECT fecha_recibida FROM pri_carga_notificaciones p WHERE id_carga_noti NOT IN(SELECT id_carga_noti FROM pri_descarga_notificaciones)
GROUP BY id_registro_autos, fecha_recibida ORDER BY fecha_recibida asc;

SELECT cn.id_registro_autos, ra.folio, ra.anio FROM pri_carga_notificaciones cn INNER JOIN pri_registro_autos ra ON cn.id_registro_autos=ra.id_registro_autos
WHERE cn.id_carga_noti NOT IN(SELECT dn.id_carga_noti FROM pri_descarga_notificaciones dn)
AND cn.fecha_recibida='2015-11-05' GROUP BY cn.id_registro_autos;
*/