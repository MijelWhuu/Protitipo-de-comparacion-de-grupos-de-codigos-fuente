package dao.expedientes;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.*;
import entitys.ActoEntity;
import entitys.ActorEntity;
import entitys.ActorTerceroEntity;
import entitys.AutorizadoEntity;
import entitys.ExpedienteEntity;
import entitys.PersonaAjenaEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author mavg
 */
public class ExpedienteDao extends ConexionMySql{

    Utilidades u = new Utilidades();

    public ExpedienteDao() {
    }
    
    public Boolean existeExpediente(String expediente){
        // <editor-fold defaultstate="collapsed" desc="Codigo">
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
        // </editor-fold>
    }
    
    public ArrayList existeActo(String id_acto, String numero){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT expediente, fecha, p.id_actos, numero FROM pri_expediente p INNER JOIN cat_actos c ON p.id_actos=c.id_actos "
                + "WHERE p.id_actos = '"+id_acto+"' AND numero = '"+numero+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                String cad = "<html><body>Expediente: <b>"+rs.getString(1)+"</b></body></html>";
                array.add(cad);
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
    
    // <editor-fold defaultstate="collapsed" desc="Guardar datos">
    public ErrorEntity saveExpediente(Connection conx, ExpedienteEntity expe, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);    
        try{
            String query = "INSERT INTO pri_expediente VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expe.getExpediente());
            save.setDate(2, u.convertirDateToSqlDate(expe.getFecha()));
            save.setString(3, expe.getTipoProcedimiento().getId_procedimientos());
            save.setString(4, expe.getTipoActo().getId_acto());
            if(expe.getTipoPretension()!=null)
                save.setString(5, expe.getTipoPretension().getId_tipo_pretension());
            else
                save.setNull(5, java.sql.Types.VARCHAR);
            save.setString(6, expe.getNumero());
            save.setDouble(7, expe.getCantidad());
            save.setString(8, expe.getObservaciones());
            save.setString(9, sesion.getId_usuario());
            save.setDate(10, sesion.getFechaActual());
            save.setDate(11, sesion.getFechaCero());
            error.setError(save.execute());            
        }catch (SQLException e){
            System.out.println("Error1");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity saveActor(Connection conx, ActorEntity actor){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO pri_actores VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, actor.getId_actor());
            save.setString(2, actor.getNombre().getNombre());
            save.setString(3, actor.getNombre().getPaterno());
            save.setString(4, actor.getNombre().getMaterno());
            save.setString(5, actor.getDom().getCalle());
            save.setString(6, actor.getDom().getNum_ext());
            save.setString(7, actor.getDom().getNum_int());
            save.setString(8, actor.getDom().getColonia());
            save.setString(9, actor.getDom().getCp());
            save.setString(10, actor.getDom().getTelefono());
            save.setString(11, actor.getDom().getEmail());
            if(actor.getDom().getMun()!=null)
                save.setInt(12, actor.getDom().getMun().getId_municipio());
            else
                save.setNull(12, java.sql.Types.VARCHAR);
            save.setBoolean(13, actor.getRepresentante1());
            save.setString(14, actor.getGrupo1());
            save.setBoolean(15, actor.getRepresentante2());
            save.setString(16, actor.getGrupo2());
            save.setString(17, actor.getObservaciones());
            save.setString(18, actor.getExpe().getExpediente());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error3");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity saveTerceroActor(Connection conx, ActorTerceroEntity actor){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO pri_terceros_actores VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, actor.getId_tercero_actor());
            save.setString(2, actor.getNombre().getNombre());
            save.setString(3, actor.getNombre().getPaterno());
            save.setString(4, actor.getNombre().getMaterno());
            save.setString(5, actor.getDom().getCalle());
            save.setString(6, actor.getDom().getNum_ext());
            save.setString(7, actor.getDom().getNum_int());
            save.setString(8, actor.getDom().getColonia());
            save.setString(9, actor.getDom().getCp());
            save.setString(10, actor.getDom().getTelefono());
            save.setString(11, actor.getDom().getEmail());
            if(actor.getDom().getMun()!=null)
                save.setInt(12, actor.getDom().getMun().getId_municipio());
            else
                save.setNull(12, java.sql.Types.VARCHAR);
             save.setString(13, actor.getObservaciones());
            save.setString(14, actor.getExpe().getExpediente());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error4");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity saveExpedienteAutoridad(Connection conx, String expediente, String id_autoridad){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO rel_expediente_has_cat_autoridades VALUES(?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            save.setString(2, id_autoridad);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error5");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity saveExpedienteAutorizado(Connection conx, String expediente, AutorizadoEntity auto){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO rel_expediente_has_cat_autorizados VALUES(?,?,?,?)";
            System.out.println(query);
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            save.setString(2, auto.getId_autorizado());
            save.setInt(3, auto.getId_parte());
            save.setString(4, auto.getObservaciones());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error6");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity saveExpedienteTerceroAutoridad(Connection conx, String expediente, String id_tercero_autoridad){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO rel_expediente_has_pri_terceros_autoridades VALUES(?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            save.setString(2, id_tercero_autoridad);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error7");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity saveExpedienteAutoridaAjena(Connection conx, String expediente, String id_tercero_autoridad){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO rel_expediente_has_pri_autoridades_ajenas VALUES(?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            save.setString(2, id_tercero_autoridad);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error8");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity savePersonaAjena(Connection conx, PersonaAjenaEntity perso){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO pri_personas_ajenas VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, perso.getId_persona_ajena());
            save.setString(2, perso.getNombre().getNombre());
            save.setString(3, perso.getNombre().getPaterno());
            save.setString(4, perso.getNombre().getMaterno());
            save.setString(5, perso.getDom().getCalle());
            save.setString(6, perso.getDom().getNum_ext());
            save.setString(7, perso.getDom().getNum_int());
            save.setString(8, perso.getDom().getColonia());
            save.setString(9, perso.getDom().getCp());
            save.setString(10, perso.getDom().getTelefono());
            save.setString(11, perso.getDom().getEmail());
            if(perso.getDom().getMun()!=null)
                save.setInt(12, perso.getDom().getMun().getId_municipio());
            else
                save.setNull(12, java.sql.Types.VARCHAR);
            save.setString(13, perso.getObservaciones());
            save.setString(14, perso.getExpe().getExpediente());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("Error9");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Obtener ID'S">
    public String getIdActores(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_actor,10) as SIGNED INTEGER)),0) "
                + "FROM pri_actores WHERE SUBSTRING(id_actor,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("A", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    public String getIdTerceroActores(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_tercero_actor,10) as SIGNED INTEGER)),0) "
                + "FROM pri_terceros_actores WHERE SUBSTRING(id_tercero_actor,1,8) = '"+u.getFormatoFechaString()+"'";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClaveFecha("T", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    
    public String getIdPersonaAjena(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_persona_ajena,10) as SIGNED INTEGER)),0) "
                + "FROM pri_personas_ajenas WHERE SUBSTRING(id_persona_ajena,1,8) = '"+u.getFormatoFechaString()+"'";
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
    
    public String getNumeroExpediente(){
        String clave = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Integer anio = cal.get(Calendar.YEAR);
        String query = "SELECT CONCAT((IF(MAX(CAST(SUBSTRING_INDEX(expediente,'/',1) AS UNSIGNED))+1 IS NULL, 1, "
                + "MAX(CAST(SUBSTRING_INDEX(expediente,'/',1) AS UNSIGNED))+1)),'/','"+anio+"','-', "
                + "if(mod((IF(MAX(CAST(SUBSTRING_INDEX(expediente,'/',1) AS UNSIGNED))+1 IS NULL, 1, "
                + "MAX(CAST(SUBSTRING_INDEX(expediente,'/',1) AS UNSIGNED))+1)),2) = 0,'II','I')) "
                + "FROM pri_expediente WHERE expediente LIKE '%/"+anio+"-%'";

        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               clave = rs.getString(1);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs);
        }
        return clave;
    }
    // </editor-fold>
    
}
