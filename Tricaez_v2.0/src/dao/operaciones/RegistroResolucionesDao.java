package dao.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.ExpedienteEntity;
import entitys.ExpedienteTramiteEntity;
import entitys.RegistroResolucionEntity;
import entitys.SentidoEjecutoriaEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author mavg
 */
public class RegistroResolucionesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    SimpleDateFormat formatnow = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
    
    public RegistroResolucionesDao() {
    }
    
    public Integer getFolio(Integer anio){
        Integer folio = 1;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT IF(max(folio) IS NULL, 1, max(folio)+1) FROM pri_relacion_folios p WHERE anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               folio = rs.getInt(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return folio;
    }
    
    public ArrayList getExpedientesTramites(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_inicio_tramite, p.id_tipo_amparo, fecha, razon, expediente, nombre FROM tribunal.pri_inicio_tramites p "
                + "INNER JOIN tribunal.cat_tipo_amparos c ON p.id_tipo_amparo=c.id_tipo_amparo ORDER BY fecha DESC";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                ExpedienteTramiteEntity tra = new ExpedienteTramiteEntity();
                tra.setId_inicio_tramite(rs.getString(1));
                tra.setId_tipo_amparo(rs.getString(2));
                tra.setFecha(rs.getDate(3));
                tra.setRazon(rs.getString(4));
                tra.setExpediente(rs.getString(5));
                tra.setNombre(rs.getString(6));
                array.add(tra);
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
    
    public RegistroResolucionEntity getResolucion(Integer folio, Integer anio){
        RegistroResolucionEntity reg = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT "
                + "p.id_reg_resolucion, "
                + "id_inicio_tramite, "
                + "expediente, "
                + "fecha_resolucion, "
                + "fecha_acuerdo, "
                + "actuaria, "
                + "id_sentido_ejecutoria, "
                + "archivar, "
                + "motivo, "
                + "folio, "
                + "anio "
                + "FROM pri_registro_resoluciones p "
                + "INNER JOIN pri_relacion_folios pl "
                + "ON pl.id_reg_resolucion=p.id_reg_resolucion "
                + "WHERE pl.folio = "+folio+" "
                + "AND pl.anio = "+anio;
        //System.out.println(query);
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                reg = new RegistroResolucionEntity();
                reg.setId_reg_resolucion(rs.getString(1));
                reg.setId_tramite(null);
                reg.setTramite(null);
                if(rs.getString(2)!=null){
                    reg.setId_tramite(rs.getString(2));
                    reg.setTramite(new ExpedienteTramiteEntity(rs.getString(2)));
                }
                reg.setExp(new ExpedienteEntity(rs.getString(3)));
                reg.setFecha_resolucion(rs.getString(4));
                reg.setFecha_acuerdo(rs.getString(5));
                reg.setActuaria(rs.getBoolean(6));
                reg.setSentido(null);
                if(rs.getString(7)!=null)
                    reg.setSentido(new SentidoEjecutoriaEntity(rs.getString(7),"",""));
                reg.setArchivar(rs.getBoolean(8));
                reg.setMotivo(rs.getString(9));
                reg.setFolio(rs.getInt(10));
                reg.setAnio(rs.getInt(11));
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
    
    public Boolean existeExpediente(String expediente){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_expediente WHERE expediente = '"+expediente+"'";
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
    
    public ErrorEntity saveRegistro(Connection conx, RegistroResolucionEntity re, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_registro_resoluciones VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, re.getId_reg_resolucion());
            if(re.getTramite()==null)
                save.setNull(2, java.sql.Types.VARCHAR);
            else
                save.setString(2, re.getTramite().getId_inicio_tramite());
            save.setString(3, re.getExp().getExpediente());
                        
            save.setDate(4, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_resolucion())));
            save.setDate(5, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_acuerdo())));
            /*save.setDate(4, u.convertirDateToSqlDate(re.getFecha_resolucion()));
            save.setDate(5, u.convertirDateToSqlDate(re.getFecha_acuerdo()));*/
            
            save.setBoolean(6, re.getActuaria());
            if(re.getSentido()==null)
                save.setNull(7, java.sql.Types.VARCHAR);
            else
                save.setString(7, re.getSentido().getId_sentido_ejecutoria());
            save.setBoolean(8, re.getArchivar());
            save.setString(9, re.getMotivo());
            save.setString(10, sesion.getId_usuario());
            save.setDate(11, sesion.getFechaActual());
            save.setDate(12, sesion.getFechaCero());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateRegistro(Connection conx, RegistroResolucionEntity re, SesionEntity sesion ){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_registro_resoluciones SET id_inicio_tramite = ?, expediente = ?, fecha_resolucion = ?, fecha_acuerdo = ?, "
                    + "actuaria = ?, id_sentido_ejecutoria = ?, archivar = ?, motivo = ?, id_usuario = ?, f_cambio = ? "
                    + "WHERE id_reg_resolucion = ?";
            PreparedStatement save = conx.prepareStatement(query);
            if(re.getTramite()==null)
                save.setNull(1, java.sql.Types.VARCHAR);
            else
                save.setString(1, re.getTramite().getId_inicio_tramite());
            save.setString(2, re.getExp().getExpediente());
            
            save.setDate(3, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_resolucion())));
            save.setDate(4, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_acuerdo())));
            /*save.setDate(3, u.convertirDateToSqlDate(re.getFecha_resolucion()));
            save.setDate(4, u.convertirDateToSqlDate(re.getFecha_acuerdo()));*/
            
            save.setBoolean(5, re.getActuaria());
            if(re.getSentido()==null)
                save.setNull(6, java.sql.Types.VARCHAR);
            else
                save.setString(6, re.getSentido().getId_sentido_ejecutoria());
            save.setBoolean(7, re.getArchivar());
            save.setString(8, re.getMotivo());
            save.setString(9, sesion.getId_usuario());
            save.setDate(10, sesion.getFechaActual());
            save.setString(11, re.getId_reg_resolucion());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRegistro(Connection conx, String id_resolucion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_registro_resoluciones WHERE id_reg_resolucion = '"+id_resolucion+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdRegistroAuto(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_reg_resolucion,10) as SIGNED INTEGER)),0) FROM "
                +"pri_registro_resoluciones WHERE SUBSTRING(id_reg_resolucion,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("R", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    
    public ErrorEntity saveRelacion(Connection conx, String id_reg_resolucion, Integer folio, Integer anio){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_relacion_folios VALUES(?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, getIdRelacion(conx));
            save.setNull(2, java.sql.Types.VARCHAR);
            save.setNull(3, java.sql.Types.VARCHAR);
            save.setString(4, id_reg_resolucion);
            save.setInt(5, folio);
            save.setInt(6, anio);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateRelacion(Connection conx, String id_reg_resolucion, Integer folio, Integer anio){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_relacion_folios SET folio = ?, anio = ? WHERE id_reg_resolucion = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setInt(1, folio);
            save.setInt(2, anio);
            save.setString(3, id_reg_resolucion);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRelacion(Connection conx, String id_reg_resolucion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_relacion_folios WHERE id_reg_resolucion = '"+id_reg_resolucion+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdRelacion(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_relacion,10) as SIGNED INTEGER)),0) FROM "
                +"pri_relacion_folios WHERE SUBSTRING(id_relacion,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("R", valor);
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
