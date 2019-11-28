package dao.promociones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 * @author mavg
 */
public class PromocionesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    SimpleDateFormat formatnow = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);

    public PromocionesDao() {
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
    
    public ErrorEntity savePromocion(Connection conx, PromocionEntity promo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO pri_promociones VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, promo.getId_promocion());
            if(promo.getExpe()==null)
                save.setNull(2, java.sql.Types.VARCHAR);
            else
                save.setString(2, promo.getExpe().getExpediente());
            save.setInt(3, promo.getFolio());
            save.setInt(4, promo.getAnio());
            save.setString(5, promo.getTipoPromocion().getId_tipo_promocion());
            if(promo.getDestino()==null)
                save.setNull(6, java.sql.Types.VARCHAR);
            else
                save.setString(6, promo.getDestino().getId_destino());
            save.setDate(7, u.convertirDateToSqlDate(formatnow.parse(promo.getFechaR())));
            save.setTime(8, java.sql.Time.valueOf(promo.getHoraR()));
            save.setInt(9, promo.getaOriginales());
            save.setInt(10, promo.getaCopias());
            save.setBoolean(11, promo.getAcreditacion());
            save.setInt(12, promo.getHojas());
            save.setString(13, promo.getObservaciones());
            save.setString(14, sesion.getId_usuario());
            save.setDate(15, sesion.getF_alta());
            save.setDate(16, sesion.getF_cambio());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
     
    public String getIdPromociones(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_promocion,10) as SIGNED INTEGER)),0) "
                + "FROM pri_promociones WHERE SUBSTRING(id_promocion,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("P", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    public Integer getFolio(Integer anio){
        Integer folio = 1;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT IF(max(folio) IS NULL, 1, max(folio)+1) FROM pri_promociones p WHERE anio = "+anio;
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
    
    public Boolean existeFolio(Integer folio, Integer anio, String id_promocion){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_promociones WHERE folio = "+folio+" AND anio = "+anio+" AND id_promocion != '"+id_promocion+"'";
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
}
