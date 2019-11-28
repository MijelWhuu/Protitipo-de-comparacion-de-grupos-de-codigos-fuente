package dao.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.ExpedienteEntity;
import entitys.TramiteEntity;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import entitys.TipoPromocionEntity;
import entitys.TipoTramiteEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class InicioTramiteDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public InicioTramiteDao() {
    }
    
    public Boolean existeFolio(Integer folio, Integer anio){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_promociones WHERE folio = "+folio+" AND anio = "+anio;
        try{
            int valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            if(valor==0)
                existe = false;
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return existe;
    }
    
    public ArrayList getAnios(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT anio FROM pri_promociones p GROUP BY anio ORDER BY anio DESC";
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
            cleanVariables(rs,con);
        }
        return array;
    }
    
    public TramiteEntity getTramite(Integer folio, Integer anio){
        TramiteEntity ini = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_promocion, p.expediente, p.folio, p.anio, p.id_tipo_promocion, i.id_inicio_tramite, i.id_tipo_amparo, "//1-7
                + "i.fecha, i.razon, i.observaciones, tp.nombre, ta.nombre, ta.solicita_partes FROM pri_promociones p "//8-13
                + "LEFT OUTER JOIN pri_inicio_tramites i ON p.id_promocion=i.id_promocion "
                + "LEFT OUTER JOIN cat_tipo_promociones tp ON p.id_tipo_promocion=tp.id_tipo_promocion "
                + "LEFT OUTER JOIN cat_tipo_amparos ta ON i.id_tipo_amparo=ta.id_tipo_amparo "
                + "WHERE p. folio = "+folio+" AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                ini = new TramiteEntity();
                PromocionEntity pro = new PromocionEntity();
                pro.setId_promocion(rs.getString(1));
                pro.setExpe(null);
                if(rs.getString(2)!=null)
                    pro.setExpe(new ExpedienteEntity(rs.getString(2)));
                pro.setFolio(rs.getInt(3));
                pro.setAnio(rs.getInt(4));
                pro.setTipoPromocion(new TipoPromocionEntity(rs.getString(5),rs.getString(11),""));
                ini.setPromocion(pro);
                ini.setId_inicio_tramite(rs.getString(6));
                ini.setTramite(new TipoTramiteEntity(rs.getString(7),rs.getString(12),rs.getBoolean(13)));
                ini.setFecha(rs.getDate(8));
                ini.setRazon(rs.getString(9));
                ini.setObservaciones(rs.getString(10));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return ini;
    }
    
    public ArrayList getPromovedores(String expediente, int opcion){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String []query = {
            "SELECT a.id_actor, CONCAT(a.nombre,' ',a.paterno,' ',a.materno), '00' FROM pri_actores a  WHERE a.expediente = '"+expediente+"'",
            "SELECT au.id_autoridad, c.nombre, '01' FROM rel_expediente_has_cat_autoridades au INNER JOIN cat_autoridades c ON au.id_autoridad = c.id_autoridad WHERE au.expediente = '"+expediente+"'",
            "SELECT ta.id_autoridad, c.nombre, '02' FROM rel_expediente_has_pri_terceros_autoridades ta INNER JOIN cat_autoridades c ON ta.id_autoridad = c.id_autoridad WHERE ta.expediente = '"+expediente+"'",
            "SELECT id_tercero_actor, CONCAT(nombre,' ',paterno,' ',materno), '03' FROM pri_terceros_actores t WHERE t.expediente = '"+expediente+"'",
            "SELECT aa.id_autoridad, c.nombre, '04' FROM rel_expediente_has_pri_autoridades_ajenas aa INNER JOIN cat_autoridades c ON aa.id_autoridad = c.id_autoridad WHERE aa.expediente = '"+expediente+"'",
            "SELECT id_persona_ajena, CONCAT(nombre,' ',paterno,' ',materno), '05' FROM pri_personas_ajenas pa WHERE pa.expediente = '"+expediente+"'"};
        try{
            rs = con.createStatement().executeQuery(query[opcion]);
            while (rs.next()){
               String []datos = new String[3];
               datos[0] = rs.getString(1); //ID
               datos[1] = rs.getString(2); //NOMBRE
               datos[2] = rs.getString(3); //IDENTIFICADOR
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
    
    public Boolean existeTramite(String id_inicio_tramite){
        Boolean existe = false;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_inicio_tramites p WHERE id_inicio_tramite = '"+id_inicio_tramite+"'";
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
    
    public ArrayList getPromovedoresActor(String id_inicio_tramite){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_actor FROM pri_promueve p WHERE id_inicio_tramite = '"+id_inicio_tramite+"' AND id_Actor is not null";
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
    
    public ArrayList getPromovedoresAutoridad(String id_inicio_tramite){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_autoridad FROM pri_promueve p WHERE id_inicio_tramite = '"+id_inicio_tramite+"' AND id_autoridad is not null";
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
    
    public ArrayList getPromovedoresTerceroAutoridad(String id_inicio_tramite){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tercero_autoridad FROM pri_promueve p WHERE id_inicio_tramite = '"+id_inicio_tramite+"' AND id_tercero_autoridad is not null";
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
    
    public ArrayList getPromovedoresTerceroActor(String id_inicio_tramite){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tercero_actor FROM pri_promueve p WHERE id_inicio_tramite = '"+id_inicio_tramite+"' AND id_tercero_actor is not null";
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
    
    public ArrayList getPromovedoresAutoridadAjena(String id_inicio_tramite){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_autoridad_ajena FROM pri_promueve p WHERE id_inicio_tramite = '"+id_inicio_tramite+"' AND id_autoridad_ajena is not null";
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
    
    public ArrayList getPromovedoresPersonaAjena(String id_inicio_tramite){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_persona_ajena FROM pri_promueve p WHERE id_inicio_tramite = '"+id_inicio_tramite+"' AND id_persona_ajena is not null";
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
    
    public ErrorEntity saveTramite(Connection conx, TramiteEntity tra, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_inicio_tramites VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, tra.getId_inicio_tramite());
            save.setString(2, tra.getPromocion().getId_promocion());
            save.setString(3, tra.getTramite().getId_tipo_amparo());
            save.setDate(4, u.convertirDateToSqlDate(tra.getFecha()));
            save.setString(5, tra.getRazon());
            save.setString(6, tra.getObservaciones());
            save.setString(7, tra.getExp().getExpediente());
            save.setString(8, sesion.getId_usuario());
            save.setDate(9, sesion.getFechaActual());
            save.setDate(10, sesion.getFechaCero());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateTramite(Connection conx, TramiteEntity tra, SesionEntity sesion ){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_inicio_tramites SET id_tipo_amparo = ?, fecha = ?, razon = ?, observaciones = ?, "
                    + "id_usuario = ?, f_cambio = ? WHERE id_inicio_tramite = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, tra.getTramite().getId_tipo_amparo());
            save.setDate(2,  u.convertirDateToSqlDate(tra.getFecha()));
            save.setString(3, tra.getRazon());
            save.setString(4, tra.getObservaciones());
            save.setString(5, sesion.getId_usuario());
            save.setDate(6, sesion.getFechaActual());
            save.setString(7, tra.getId_inicio_tramite());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteTramite(Connection conx, String id_tramite){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_inicio_tramites WHERE id_inicio_tramite = '"+id_tramite+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deletePromovedores(Connection conx, String id_tramite){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_promueve WHERE id_inicio_tramite = '"+id_tramite+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity savePromovedor(Connection conx, String id_promueve, String id_tramite, String id_actor, String id_autoridad, 
            String id_ter_auto, String id_ter_actor, String id_auto_ajena, String id_per_ajena){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_promueve VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, id_promueve);
            save.setString(2, id_tramite);
            if(id_actor == null) save.setNull(3, java.sql.Types.VARCHAR); else save.setString(3, id_actor);
            if(id_autoridad == null) save.setNull(4, java.sql.Types.VARCHAR); else save.setString(4, id_autoridad);
            if(id_ter_auto == null) save.setNull(5, java.sql.Types.VARCHAR); else save.setString(5, id_ter_auto);
            if(id_ter_actor == null) save.setNull(6, java.sql.Types.VARCHAR); else save.setString(6, id_ter_actor);
            if(id_auto_ajena == null) save.setNull(7, java.sql.Types.VARCHAR); else save.setString(7, id_auto_ajena);
            if(id_per_ajena == null) save.setNull(8, java.sql.Types.VARCHAR); else save.setString(8, id_per_ajena);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdTramite(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_inicio_tramite,10) as SIGNED INTEGER)),0) FROM "
                +"pri_inicio_tramites WHERE SUBSTRING(id_inicio_tramite,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("I", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    public String getIdPromueve(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_promueve,10) as SIGNED INTEGER)),0) FROM "
                +"pri_promueve WHERE SUBSTRING(id_promueve,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("I", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }

}
