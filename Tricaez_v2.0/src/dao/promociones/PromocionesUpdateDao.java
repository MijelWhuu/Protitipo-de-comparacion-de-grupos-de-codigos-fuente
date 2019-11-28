package dao.promociones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.DestinoEntity;
import entitys.ExpedienteEntity;
import entitys.PromocionEntity;
import entitys.SesionEntity;
import entitys.TipoPromocionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author mavg
 */
public class PromocionesUpdateDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    SimpleDateFormat formatnow = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);

    public PromocionesUpdateDao() {
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
    
    //DESHABILITE EL MÉTODO DE MARCO PORQUE NO GUARDABA CORRECTAMENTE LA HORA DE LA RECEPCIÓN //////////
    
    /*public ErrorEntity updatePromocion(Connection conx, PromocionEntity promo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_promociones p SET id_tipo_promocion = ?, expediente = ?, id_destino = ?, fecha_recepcion = ?, "
                    + "hora_recepcion = ?, no_anexos_originales = ?, no_anexos_copias = ?, acreditacion = ?, no_hojas_acreditacion = ?, "
                    + "observaciones = ?, id_usuario = ?, f_cambio = ? WHERE id_promocion = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, promo.getTipoPromocion().getId_tipo_promocion());
            if(promo.getExpe()==null)
                save.setNull(2, java.sql.Types.VARCHAR);
            else
                save.setString(2, promo.getExpe().getExpediente());
            if(promo.getDestino()==null)
                save.setNull(3, java.sql.Types.VARCHAR);
            else
                save.setString(3, promo.getDestino().getId_destino());
            save.setDate(4, u.convertirDateToSqlDate(promo.getFechaR()));
            System.out.println(java.sql.Time.valueOf(promo.getHoraR()));
            save.setTime(5, java.sql.Time.valueOf(promo.getHoraR()));
            System.out.println(java.sql.Time.valueOf(promo.getHoraR()));
            save.setInt(6, promo.getaOriginales());
            save.setInt(7, promo.getaCopias());
            save.setBoolean(8, promo.getAcreditacion());
            save.setInt(9, promo.getHojas());
            save.setString(10, promo.getObservaciones());
            save.setString(11, sesion.getId_usuario());
            save.setDate(12, sesion.getF_alta());
            save.setString(13, promo.getId_promocion());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }*/
    
    public ErrorEntity updatePromocion(Connection conx, PromocionEntity promo, SesionEntity sesion){
        Boolean del = true;
        ErrorEntity error = new ErrorEntity(true);
        Connection con = getConexionBD();
        try{
            ExpedienteEntity exp;
            exp = promo.getExpe();
            Integer ac = 0;
            if(promo.getAcreditacion()){
                ac = 1;
            }
            Statement stmt = con.createStatement();
            String queryString ="UPDATE pri_promociones p "
                    + "SET id_tipo_promocion = '"+promo.getTipoPromocion().getId_tipo_promocion()+"', "
                    + "expediente = '"+promo.getExpediente()+"', "
                    + "id_destino = "+GetDestino(promo.getDestino())+", "
                    + "fecha_recepcion = '"+u.convertirDateToSqlDate(formatnow.parse(promo.getFechaR().toString()))+"', "
                    + "hora_recepcion = '"+promo.getHoraR()+"', "
                    + "no_anexos_originales = "+promo.getaOriginales()+", "
                    + "no_anexos_copias = "+promo.getaCopias()+", "
                    + "acreditacion = "+ac+", "
                    + "no_hojas_acreditacion = "+promo.getHojas()+", "
                    + "observaciones = '"+promo.getObservaciones()+"', "
                    + "id_usuario = '"+sesion.getId_usuario()+"', "
                    + "f_cambio = '"+sesion.getF_alta()+"' "
                    + "WHERE id_promocion = '"+promo.getId_promocion()+"';";
            del = stmt.execute(queryString);
            error.setError(del);
        }catch(SQLException | ParseException e){
            System.out.println(e.getMessage());
        }finally{
            cleanVariables(con);
        }
        return error;
    }
    
    public String GetDestino(DestinoEntity des){
        String destino = null;
        if(des != null){
            destino = des.getId_destino();
        }
        return destino;
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
    
    public PromocionEntity getPromocion(Integer folio, Integer anio){
        PromocionEntity promo = new PromocionEntity();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_promocion, expediente, folio, anio, id_tipo_promocion, id_destino, fecha_recepcion, hora_recepcion, "
                + "no_anexos_originales, no_anexos_copias, acreditacion, no_hojas_acreditacion, observaciones FROM pri_promociones "
                + "WHERE folio = "+folio+" AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                promo.setId_promocion(rs.getString(1));
                promo.setExpe(null);
                if(rs.getString(2)!=null)
                    promo.setExpe(new ExpedienteEntity(rs.getString(2)));
                promo.setFolio(rs.getInt(3));
                promo.setAnio(rs.getInt(4));
                promo.setTipoPromocion(new TipoPromocionEntity(rs.getString(5)));
                promo.setDestino(new DestinoEntity(rs.getString(6)));
                
                promo.setFechaR(rs.getString(7));
                /*--------------------------------------------------------------
                    CÓDIGO ORIGINAL DE MARCO (DABA LA FECHA RESTÁNDOLE UN DÍA)
                ---------------------------------------------------------------*/
                //promo.setFechaR(rs.getDate(7));
                
                promo.setHoraR(rs.getString(8));
                promo.setaOriginales(rs.getInt(9));
                promo.setaCopias(rs.getInt(10));
                promo.setAcreditacion(rs.getBoolean(11));
                promo.setHojas(rs.getInt(12));
                promo.setObservaciones(rs.getString(13));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return promo;
    }
}
