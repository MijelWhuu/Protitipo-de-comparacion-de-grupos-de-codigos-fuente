package dao.operaciones;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.AutoEntity;
import entitys.ExpedienteEntity;
import entitys.TramiteEntity;
import entitys.PromocionEntity;
import entitys.RegistroAutoEntity;
import entitys.SesionEntity;
import entitys.SuspensionEntity;
import entitys.TipoPromocionEntity;
import entitys.TipoTramiteEntity;
import interfaz.operaciones.PromoTramiteEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author mavg
 */
public class RegistroAutosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    SimpleDateFormat formatnow = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy", Locale.ENGLISH);
    
    public RegistroAutosDao() {
    }
    
    public RegistroAutoEntity getAuto(Integer folio, Integer anio){
        RegistroAutoEntity reg = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_registro_autos, expediente, pl.folio, pl.anio, id_tipo_auto, ordenar, fecha_auto, fecha_acuerdo, "
                + "prueba1, prueba2, prueba3, prueba4, prueba5, prueba6, motivo, id_suspension, efectos, observaciones "
                + "FROM pri_registro_autos p INNER JOIN pri_relacion_folios pl ON pl.id_registro_autos=p.id_registro_autos "
                + "WHERE pl.folio = "+folio+" AND pl.anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                reg = new RegistroAutoEntity();
                reg.setId_registro_auto(rs.getString(1));
                reg.setExp(null);
                if(rs.getString(2)!=null)
                    reg.setExp(new ExpedienteEntity(rs.getString(2)));
                reg.setFolio(rs.getInt(3));
                reg.setAnio(rs.getInt(4));
                reg.setTipo_auto(new AutoEntity(rs.getString(5)));
                reg.setOrdenar(rs.getBoolean(6));
                reg.setFecha_auto(rs.getString(7));
                reg.setFecha_acuerdo(rs.getString(8));
                
                /*--------------------------------------------------------------
                    CÓDIGO ORIGINAL DE MARCO (DABA LA FECHA RESTÁNDOLE UN DÍA)
                ---------------------------------------------------------------*/
                /*reg.setFecha_auto(rs.getDate(7));
                reg.setFecha_acuerdo(rs.getDate(8));*/
                
                reg.setPrueba1(rs.getBoolean(9));
                reg.setPrueba2(rs.getBoolean(10));
                reg.setPrueba3(rs.getBoolean(11));
                reg.setPrueba4(rs.getBoolean(12));
                reg.setPrueba5(rs.getBoolean(13));
                reg.setPrueba6(rs.getBoolean(14));
                reg.setMotivo(rs.getString(15));
                reg.setSuspe(null);
                if(rs.getString(16)!=null)
                    reg.setSuspe(new SuspensionEntity(rs.getString(16)));
                reg.setEfectos(rs.getString(17));
                reg.setObservaciones(rs.getString(18));
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
    
    public Boolean existeFolio(Integer folio, Integer anio){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_relacion_folios WHERE folio = "+folio+" AND anio = "+anio+" AND id_registro_autos IS NOT NULL";
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
    
    public TramiteEntity getTramite(String id_tramite){
        TramiteEntity ini = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT i.id_inicio_tramite, i.id_tipo_amparo, ta.nombre FROM pri_inicio_tramites i "
                + "LEFT OUTER JOIN cat_tipo_amparos ta ON i.id_tipo_amparo=ta.id_tipo_amparo "
                + "WHERE i.id_inicio_tramite = '"+id_tramite+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               ini = new TramiteEntity();
               ini.setId_inicio_tramite(rs.getString(1));
               ini.setTramite(new TipoTramiteEntity(rs.getString(2),rs.getString(3)));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return ini;
    }
    
    public PromocionEntity getPromocion(String id_promocion){
        PromocionEntity pro = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_promocion, p.folio, p.anio, p.id_tipo_promocion, tp.nombre, p.expediente FROM pri_promociones p "
                + "LEFT OUTER JOIN cat_tipo_promociones tp ON p.id_tipo_promocion=tp.id_tipo_promocion "
                + "WHERE p.id_promocion = '"+id_promocion+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               pro = new PromocionEntity();
               pro.setId_promocion(rs.getString(1));
               pro.setFolio(rs.getInt(2));
               pro.setAnio(rs.getInt(3));
               pro.setTipoPromocion(new TipoPromocionEntity(rs.getString(4),rs.getString(5),""));
               pro.setExpe(new ExpedienteEntity(rs.getString(6)));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return pro;
    }
    
    public TramiteEntity getTramite(Integer folio, Integer anio){
        TramiteEntity ini = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT i.id_inicio_tramite, i.id_tipo_amparo, ta.nombre FROM pri_promociones p "
                + "LEFT OUTER JOIN pri_inicio_tramites i ON p.id_promocion=i.id_promocion "
                + "LEFT OUTER JOIN cat_tipo_amparos ta ON i.id_tipo_amparo=ta.id_tipo_amparo "
                + "WHERE p.folio = "+folio+" AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                if(rs.getString(1)!=null){
                    ini = new TramiteEntity();
                    ini.setId_inicio_tramite(rs.getString(1));
                    ini.setTramite(new TipoTramiteEntity(rs.getString(2),rs.getString(3)));
                }
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return ini;
    }
    
    public PromocionEntity getPromocion(Integer folio, Integer anio){
        PromocionEntity pro = null;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_promocion, p.folio, p.anio, p.id_tipo_promocion, tp.nombre, p.expediente FROM pri_promociones p "
                + "LEFT OUTER JOIN cat_tipo_promociones tp ON p.id_tipo_promocion=tp.id_tipo_promocion "
                + "WHERE p.folio = "+folio+" AND anio = "+anio;
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               pro = new PromocionEntity();
               pro.setId_promocion(rs.getString(1));
               pro.setFolio(rs.getInt(2));
               pro.setAnio(rs.getInt(3));
               pro.setTipoPromocion(new TipoPromocionEntity(rs.getString(4),rs.getString(5),""));
               pro.setExpe(new ExpedienteEntity(rs.getString(6)));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return pro;
    }
    
    public Boolean existeFolioPromocion(Integer folio, Integer anio){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_inicio_tramites i INNER JOIN pri_promociones p ON i.id_promocion=p.id_promocion WHERE folio = "+folio+" AND anio = "+anio;
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
    
    public ErrorEntity saveRegistro(Connection conx, RegistroAutoEntity re, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_registro_autos VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, re.getId_registro_auto());
            if(re.getExp()==null)
                save.setNull(2, java.sql.Types.VARCHAR);
            else
                save.setString(2, re.getExp().getExpediente());
            save.setString(3, re.getTipo_auto().getId_tipo_auto());
            save.setBoolean(4, re.getOrdenar());
            
            save.setDate(5, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_auto())));
            save.setDate(6, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_acuerdo())));
            /*save.setDate(5, u.convertirDateToSqlDate(re.getFecha_auto()));
            save.setDate(6, u.convertirDateToSqlDate(re.getFecha_acuerdo()));*/
            
            save.setBoolean(7, re.getPrueba1());
            save.setBoolean(8, re.getPrueba2());
            save.setBoolean(9, re.getPrueba3());
            save.setBoolean(10, re.getPrueba4());
            save.setBoolean(11, re.getPrueba5());
            save.setBoolean(12, re.getPrueba6());
            save.setString(13, re.getMotivo());
            if(re.getSuspe()==null)
                save.setNull(14, java.sql.Types.VARCHAR);
            else 
                save.setString(14, re.getSuspe().getId_suspension());
            save.setString(15, re.getEfectos());
            save.setString(16, re.getObservaciones());
            save.setString(17, sesion.getId_usuario());
            save.setDate(18, sesion.getFechaActual());
            save.setDate(19, sesion.getFechaCero());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateRegistro(Connection conx, RegistroAutoEntity re, SesionEntity sesion ){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_registro_autos SET expediente = ?, id_tipo_auto = ?, ordenar = ?, fecha_auto = ?, fecha_acuerdo = ?, "
                    + "prueba1 = ?, prueba2 = ?, prueba3 = ?, prueba4 = ?, prueba5 = ?, prueba6 = ?, motivo = ?, id_suspension = ?, efectos = ?, "
                    + "observaciones = ?, id_usuario = ?, f_cambio = ? WHERE id_registro_autos = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, re.getExp().getExpediente());
            save.setString(2, re.getTipo_auto().getId_tipo_auto());
            save.setBoolean(3, re.getOrdenar());
            
            save.setDate(4, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_auto())));
            save.setDate(5, u.convertirDateToSqlDate(formatnow.parse(re.getFecha_acuerdo())));
            /*save.setDate(4, u.convertirDateToSqlDate(re.getFecha_auto()));
            save.setDate(5, u.convertirDateToSqlDate(re.getFecha_acuerdo()));*/
            
            save.setBoolean(6, re.getPrueba1());
            save.setBoolean(7, re.getPrueba2());
            save.setBoolean(8, re.getPrueba3());
            save.setBoolean(9, re.getPrueba4());
            save.setBoolean(10, re.getPrueba5());
            save.setBoolean(11, re.getPrueba6());
            save.setString(12, re.getMotivo());
            if(re.getSuspe()==null)
                save.setNull(13, java.sql.Types.VARCHAR);
            else 
                save.setString(13, re.getSuspe().getId_suspension());
            save.setString(14, re.getEfectos());
            save.setString(15, re.getObservaciones());
            save.setString(16, sesion.getId_usuario());
            save.setDate(17, sesion.getFechaActual());
            save.setString(18, re.getId_registro_auto());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRegistro(Connection conx, String id_registro){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_registro_autos WHERE id_registro_autos = '"+id_registro+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_registro_autos,10) as SIGNED INTEGER)),0) FROM "
                +"pri_registro_autos WHERE SUBSTRING(id_registro_autos,1,8) = '"+u.getFormatoFechaString()+"'";
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
    
    public String getIdRegistroAuto(){
        String clave = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_registro_autos,10) as SIGNED INTEGER)),0) FROM "
                +"pri_registro_autos WHERE SUBSTRING(id_registro_autos,1,8) = '"+u.getFormatoFechaString()+"'";
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
            cleanVariables(rs,con);
        }
        return clave;
    }
    
    public Boolean existeFolioAuto(Integer folio, Integer anio){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_relacion_folios WHERE folio = "+folio+" AND anio = "+anio;
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
    
    public Boolean existeFolioAuto(Integer folio, Integer anio, String id_auto){
        Boolean existe = true;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_relacion_folios WHERE folio = "+folio+" AND anio = "+anio+" AND id_registro_autos != '"+id_auto+"'";
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
    
    
    public ErrorEntity saveRelacion(Connection conx, String id_registro_auto, Integer folio, Integer anio){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_relacion_folios VALUES(?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, getIdRelacion());
            save.setString(2, id_registro_auto);
            save.setNull(3, java.sql.Types.VARCHAR);
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
    
    public ErrorEntity updateRelacion(Connection conx, String id_registro_auto, Integer folio, Integer anio){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "UPDATE pri_relacion_folios SET folio = ?, anio = ? WHERE id_registro_autos = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setInt(1, folio);
            save.setInt(2, anio);
            save.setString(3, id_registro_auto);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRelacion(Connection conx, String id_registro_auto){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_relacion_folios WHERE id_registro_autos = '"+id_registro_auto+"'";
            error.setError(conx.createStatement().execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdRelacion(){
        String clave = "";
        Connection con = getConexionBD();
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
            cleanVariables(rs,con);
        }
        return clave;
    }
    
    
    public ArrayList getPromociones(String id_auto){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT p.id_promocion, p.folio, p.anio, p.id_tipo_promocion, tp.nombre, p.expediente, i.id_inicio_tramite, "
                + "i.id_tipo_amparo, ta.nombre FROM pri_promociones p "
                + "LEFT OUTER JOIN cat_tipo_promociones tp ON p.id_tipo_promocion=tp.id_tipo_promocion "
                + "LEFT OUTER JOIN pri_inicio_tramites i ON i.id_promocion=p.id_promocion "
                + "LEFT OUTER JOIN cat_tipo_amparos ta ON i.id_tipo_amparo=ta.id_tipo_amparo "
                + "INNER JOIN pri_relacion_promo_auto pr ON pr.id_promocion=p.id_promocion "
                + "WHERE pr.id_registro_autos = '"+id_auto+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                PromocionEntity pro = new PromocionEntity();
                pro.setId_promocion(rs.getString(1));
                pro.setFolio(rs.getInt(2));
                pro.setAnio(rs.getInt(3));
                pro.setTipoPromocion(new TipoPromocionEntity(rs.getString(4),rs.getString(5),""));
                pro.setExpe(new ExpedienteEntity(rs.getString(6)));
                TramiteEntity ini = null;
                if(rs.getString(7)!=null){
                    ini = new TramiteEntity();
                    ini.setId_inicio_tramite(rs.getString(7));
                    ini.setTramite(new TipoTramiteEntity(rs.getString(8),rs.getString(9)));
                }
                array.add(new PromoTramiteEntity(pro,ini));
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
    
    public ErrorEntity saveRelacionPromo(Connection conx, String id_registro_auto, String id_promo){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_relacion_promo_auto VALUES(?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, id_registro_auto);
            save.setString(2, id_promo);
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteRelacionPromo(Connection conx, String id_registro_auto){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "DELETE FROM pri_relacion_promo_auto WHERE id_registro_autos = '"+id_registro_auto+"'";
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
