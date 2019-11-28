package dao.promociones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.DestinoEntity;
import entitys.ExpedienteEntity;
import entitys.PromocionEntity;
import entitys.TipoPromocionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author mavg
 */
public class PromocionesDeleteDao extends ConexionMySql{

    Utilidades u = new Utilidades();

    public PromocionesDeleteDao() {
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
    
    public ErrorEntity deletePromocion(Connection conx, String id_promocion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM pri_promociones WHERE id_promocion = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, id_promocion);
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
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
        String query = "SELECT p.id_promocion, p.expediente, p.folio, p.anio, p.id_tipo_promocion, p.id_destino, p.fecha_recepcion, "
                + "p.hora_recepcion, p.no_anexos_originales, p.no_anexos_copias, p.acreditacion, p.no_hojas_acreditacion, p.observaciones, "
                + "t.nombre, d.nombre FROM pri_promociones p INNER JOIN cat_tipo_promociones t ON p.id_tipo_promocion=t.id_tipo_promocion "
                + "INNER JOIN cat_destinos d ON p.id_destino=d.id_destino WHERE folio = "+folio+" AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                promo.setId_promocion(rs.getString(1));
                promo.setExpe(new ExpedienteEntity(rs.getString(2)));
                promo.setFolio(rs.getInt(3));
                promo.setAnio(rs.getInt(4));
                promo.setTipoPromocion(new TipoPromocionEntity(rs.getString(5),rs.getString(14),""));
                promo.setDestino(new DestinoEntity(rs.getString(6),rs.getString(15),""));
                promo.setFechaR(rs.getString(7));
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
