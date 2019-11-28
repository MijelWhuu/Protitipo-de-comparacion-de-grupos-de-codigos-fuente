package dao.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.ExpedienteEntity;
import entitys.RegistroSentenciaEntity;
import entitys.SesionEntity;
import entitys.TipoDocumentoAsrEntity;
import entitys.TipoSentenciaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author mavg
 */
public class RegistroSentenciasDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    SimpleDateFormat formatnow = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
    
    public RegistroSentenciasDao() {
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
    
    public RegistroSentenciaEntity getSentencia(Integer folio, Integer anio){
        RegistroSentenciaEntity reg = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_reg_sentencia, expediente, fecha_audiencia, fecha_sentencia, fecha_acuerdo, id_tipo_documento, "
                + "id_tipo_sentencia, cantidad_pretension, observaciones, pl.folio, pl.anio FROM tribunal.pri_registro_sentencias p "
                + "INNER JOIN tribunal.pri_relacion_folios pl ON pl.id_reg_sentencia=p.id_reg_sentencia "
                + "WHERE pl.folio = "+folio+" AND pl.anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                reg = new RegistroSentenciaEntity();
                reg.setId_reg_sentencia(rs.getString(1));
                reg.setExp(null);
                if(rs.getString(2)!=null)
                    reg.setExp(new ExpedienteEntity(rs.getString(2)));
                
                reg.setFecha_audiencia(rs.getString(3));
                reg.setFecha_sentencia(rs.getString(4));
                reg.setFecha_acuerdo(rs.getString(5));
                /*reg.setFecha_audiencia(rs.getDate(3));
                reg.setFecha_sentencia(rs.getDate(4));
                reg.setFecha_acuerdo(rs.getDate(5));*/
                reg.setDoc(null);
                if(rs.getString(6)!=null)
                    reg.setDoc(new TipoDocumentoAsrEntity(rs.getString(6),"",""));
                reg.setTipo(null);
                if(rs.getString(7)!=null)
                    reg.setTipo(new TipoSentenciaEntity(rs.getString(7),"",""));
                reg.setCantidad(rs.getDouble(8));
                reg.setObservaciones(rs.getString(9));
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
    
    public ErrorEntity saveRegistro(Connection conx, RegistroSentenciaEntity re, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);  
        
        try{
            String query = "INSERT INTO pri_registro_sentencias VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, re.getId_reg_sentencia());
            save.setString(2, re.getExp().getExpediente());
            
            save.setDate(3, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_audiencia())));
            save.setDate(4, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_sentencia())));
            save.setDate(5, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_acuerdo())));
            /*save.setDate(3, u.convertirDateToSqlDate(re.getFecha_audiencia()));
            save.setDate(4, u.convertirDateToSqlDate(re.getFecha_sentencia()));
            save.setDate(5, u.convertirDateToSqlDate(re.getFecha_acuerdo()));*/
            if(re.getDoc()==null)
                save.setNull(6, java.sql.Types.VARCHAR);
            else
                save.setString(6, re.getDoc().getId_tipo_documento_asr());
            if(re.getTipo()==null)
                save.setNull(7, java.sql.Types.VARCHAR);
            else
                save.setString(7, re.getTipo().getId_tipo_sentencia());
            save.setDouble(8, re.getCantidad());
            save.setString(9, re.getObservaciones());
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
    
    public ErrorEntity updateRegistro(Connection conx, RegistroSentenciaEntity re, SesionEntity sesion ){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_registro_sentencias SET expediente = ?, fecha_audiencia = ?, fecha_sentencia = ?, "
                    + "fecha_acuerdo = ?, id_tipo_documento = ?, id_tipo_sentencia = ?, cantidad_pretension = ?, observaciones = ?, "
                    + "id_usuario = ?, f_cambio = ? WHERE id_reg_sentencia = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, re.getExp().getExpediente());
            
            save.setDate(2, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_audiencia())));
            save.setDate(3, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_sentencia())));
            save.setDate(4, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_acuerdo())));
            
            /*save.setDate(2, u.convertirDateToSqlDate(re.getFecha_audiencia()));
            save.setDate(3, u.convertirDateToSqlDate(re.getFecha_sentencia()));
            save.setDate(4, u.convertirDateToSqlDate(re.getFecha_acuerdo()));*/
            if(re.getDoc()==null)
                save.setNull(5, java.sql.Types.VARCHAR);
            else
                save.setString(5, re.getDoc().getId_tipo_documento_asr());
            if(re.getTipo()==null)
                save.setNull(6, java.sql.Types.VARCHAR);
            else
                save.setString(6, re.getTipo().getId_tipo_sentencia());
            save.setDouble(7, re.getCantidad());
            save.setString(8, re.getObservaciones());
            save.setString(9, sesion.getId_usuario());
            save.setDate(10, sesion.getFechaActual());
            save.setString(11, re.getId_reg_sentencia());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRegistro(Connection conx, String id_sentencia){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_registro_sentencias WHERE id_reg_sentencia = '"+id_sentencia+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_reg_sentencia,10) as SIGNED INTEGER)),0) FROM "
                +"pri_registro_sentencias WHERE SUBSTRING(id_reg_sentencia,1,8) = '"+u.getFormatoFechaString()+"'";
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
    
    public ErrorEntity saveRelacion(Connection conx, String id_reg_sentencia, Integer folio, Integer anio){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_relacion_folios VALUES(?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, getIdRelacion(conx));
            save.setNull(2, java.sql.Types.VARCHAR);
            save.setString(3, id_reg_sentencia);
            save.setNull(4, java.sql.Types.VARCHAR);
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
    
    public ErrorEntity updateRelacion(Connection conx, String id_reg_sentencia, Integer folio, Integer anio){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_relacion_folios SET folio = ?, anio = ? WHERE id_reg_sentencia = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setInt(1, folio);
            save.setInt(2, anio);
            save.setString(3, id_reg_sentencia);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRelacion(Connection conx, String id_reg_sentencia){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_relacion_folios WHERE id_reg_sentencia = '"+id_reg_sentencia+"'";
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
