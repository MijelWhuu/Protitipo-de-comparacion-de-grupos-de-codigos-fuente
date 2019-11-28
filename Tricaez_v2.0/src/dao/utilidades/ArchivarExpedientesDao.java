package dao.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.NombresTablas;
import baseSistema.Utilidades;
import entitys.ExpedienteEntity;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class ArchivarExpedientesDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    NombresTablas tbl = new NombresTablas();
    private final String tabla;
    
    public ArchivarExpedientesDao() {
        this.tabla = tbl.getPri_archivo();
    }
    
    public Boolean existeCaja(String caja){
        Boolean existe = false;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM "+tabla+" WHERE caja = '"+caja+"'";
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
    
    public ArrayList getCajas(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT caja FROM "+tabla+" GROUP BY caja";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return array;
    }
    
    public ErrorEntity saveCaja(Connection conx, String caja, String usuario, Date fechaActual){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO "+tabla+" VALUES(?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, caja);
            save.setNull(2, java.sql.Types.VARCHAR);
            save.setNull(3, java.sql.Types.DATE);
            save.setString(4, usuario);
            save.setDate(5, fechaActual);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateCaja(Connection con, String oldCaja, String newCaja, String usuario, Date fechaActual){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE "+tabla+" SET caja = ?, id_usuario = ?, f_alta = ? WHERE caja = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, newCaja);
            save.setString(2, usuario);
            save.setDate(3, fechaActual);
            save.setString(4, oldCaja);
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteCaja(Connection con, String caja){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM "+tabla+" WHERE caja = '"+caja+"'";
        try{
            Statement stmt = con.createStatement();
            error.setError(stmt.execute(query));
        }catch (SQLException e){
            error.setNumError(e.getErrorCode());
            new ErroresClase(e,"Error al eliminar los datos").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error al eliminar los datos").getErrorJava();
        }finally{
        }
        return error;
    }
    
    public ArrayList getExpedientes(String caja){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT caja, expediente, fecha FROM "+tabla+" WHERE caja = '"+caja+"' AND expediente IS NOT NULL";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                ExpedienteEntity expe = new ExpedienteEntity();
                expe.setCaja(rs.getString(1));
                expe.setExpediente(rs.getString(2));
                expe.setFechaArchivo(rs.getDate(3));
                array.add(expe);
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return array;
    }
    
    public ArrayList getExpedientesSinArchivar(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT expediente FROM pri_expediente e WHERE NOT EXISTS "
                + "(SELECT a.expediente FROM pri_archivo_expedientes a WHERE e.expediente = a.expediente)";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(rs.getString(1));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return array;
    }
    
    public ErrorEntity saveExpedientes(Connection conx, ExpedienteEntity expe, String usuario, Date fechaActual){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO "+tabla+" VALUES(?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expe.getCaja());
            save.setString(2, expe.getExpediente());
            save.setDate(3, u.convertirDateToSqlDate(expe.getFechaArchivo()));
            save.setString(4, usuario);
            save.setDate(5, fechaActual);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
     
}
