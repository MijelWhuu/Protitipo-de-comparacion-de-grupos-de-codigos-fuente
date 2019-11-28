package dao.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.NombresTablas;
import baseSistema.Utilidades;
import entitys.ExpedientesAnexoEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * @author mavg
 */
public class AnexarExpedientesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    NombresTablas tbl = new NombresTablas();
    
    public AnexarExpedientesDao(){
    }
    
    public Boolean existeExpediente(String expediente){
        Boolean existe = false;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM pri_expediente p WHERE expediente = '"+expediente+"'";
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
    
    public ErrorEntity saveAnexos(Connection conx, ExpedientesAnexoEntity anex, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO pri_anexo_expedientes VALUES(?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, anex.getPrincipal());
            save.setString(2, anex.getSecundario());
            save.setDate(3, u.convertirDateToSqlDate(anex.getFecha()));
            save.setString(4, sesion.getId_usuario());
            save.setDate(5, sesion.getFechaActual());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity exportarHistoricos(Connection conx, ExpedientesAnexoEntity anex){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String []query ={
            "INSERT IGNORE his_pri_expediente SELECT * FROM pri_expediente WHERE expediente = ?;",
            "INSERT IGNORE his_pri_actores SELECT * FROM pri_actores WHERE expediente = ?;",
            "INSERT IGNORE his_pri_personas_ajenas SELECT * FROM pri_personas_ajenas WHERE expediente = ?;",
            "INSERT IGNORE his_pri_promociones SELECT * FROM pri_promociones WHERE expediente = ?;",
            "INSERT IGNORE his_pri_terceros_actores SELECT * FROM pri_terceros_actores WHERE expediente = ?;",
            "INSERT IGNORE his_rel_expediente_has_cat_autoridades SELECT * FROM rel_expediente_has_cat_autoridades WHERE expediente = ?;",
            "INSERT IGNORE his_rel_expediente_has_cat_autorizados SELECT * FROM rel_expediente_has_cat_autorizados WHERE expediente = ?;",
            "INSERT IGNORE his_rel_expediente_has_pri_autoridades_ajenas SELECT * FROM rel_expediente_has_pri_autoridades_ajenas WHERE expediente = ?;",
            "INSERT IGNORE his_rel_expediente_has_pri_terceros_autoridades SELECT * FROM rel_expediente_has_pri_terceros_autoridades WHERE expediente = ?;"
            };
            for(int i=0; i<query.length; i++){
                PreparedStatement save = conx.prepareStatement(query[i]);
                save.setString(1, anex.getSecundario());
                error.setError(save.execute());
            }
        }catch (SQLException e){
            System.out.println("EError1:"+e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            System.out.println("EError1:"+ex.getMessage());
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
     
    
    public ErrorEntity updateNumExpediente(Connection conx, ExpedientesAnexoEntity anex){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String []query ={
                "UPDATE pri_actores SET expediente = ? WHERE expediente = ?",
                "UPDATE pri_personas_ajenas SET expediente = ? WHERE expediente = ?",
                "UPDATE pri_promociones SET expediente = ? WHERE expediente = ?",
                "UPDATE pri_terceros_actores SET expediente = ? WHERE expediente = ?",
            };
            for(int i=0; i<query.length; i++){
                PreparedStatement save = conx.prepareStatement(query[i]);
                save.setString(1, anex.getPrincipal());
                save.setString(2, anex.getSecundario());
                error.setError(save.execute());
            }
        }catch (SQLException e){
            System.out.println("UError1:"+e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            System.out.println("UError1:"+ex.getMessage());
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateEstatusExpediente(Connection conx, ExpedientesAnexoEntity anex){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_expediente SET estatus = false WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, anex.getSecundario());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("UEError1:"+e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            System.out.println("UEError1:"+ex.getMessage());
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity insertDateTables(Connection conx, ExpedientesAnexoEntity anex){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String []query ={
                "INSERT IGNORE rel_expediente_has_cat_autoridades SELECT ?, id_autoridad FROM rel_expediente_has_cat_autoridades WHERE expediente = ?",
                "INSERT IGNORE rel_expediente_has_cat_autorizados SELECT ?, id_autorizado, defensor FROM rel_expediente_has_cat_autorizados WHERE expediente = ?",
                "INSERT IGNORE rel_expediente_has_pri_autoridades_ajenas SELECT ?, id_autoridad FROM rel_expediente_has_pri_autoridades_ajenas WHERE expediente = ?",
                "INSERT IGNORE rel_expediente_has_pri_terceros_autoridades SELECT ?, id_autoridad FROM rel_expediente_has_pri_terceros_autoridades WHERE expediente = ?"
            };
            for(int i=0; i<query.length; i++){
                PreparedStatement save = conx.prepareStatement(query[i]);
                save.setString(1, anex.getPrincipal());
                save.setString(2, anex.getSecundario());
                error.setError(save.execute());
            }
        }catch (SQLException e){
            System.out.println("IError1:"+e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            System.out.println("IError1:"+ex.getMessage());
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteDateTables(Connection conx, ExpedientesAnexoEntity anex){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String []query ={
                "DELETE FROM rel_expediente_has_cat_autoridades WHERE expediente = ?",
                "DELETE FROM rel_expediente_has_cat_autorizados WHERE expediente = ?",
                "DELETE FROM rel_expediente_has_pri_autoridades_ajenas WHERE expediente = ?",
                "DELETE FROM rel_expediente_has_pri_terceros_autoridades WHERE expediente = ?"
            };
            for(int i=0; i<query.length; i++){
                PreparedStatement save = conx.prepareStatement(query[i]);
                save.setString(1, anex.getSecundario());
                error.setError(save.execute());
            }
        }catch (SQLException e){
            System.out.println("DError1:"+e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            System.out.println("DError1:"+ex.getMessage());
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
}


