package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.SesionEntity;
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
public class TipoAmparosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public TipoAmparosDao() {
    }
    
    public ArrayList getTipoAmparos(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_tipo_amparo, nombre, persona, autoridad, documento, solicita_partes, "
                + "observaciones, seleccionar FROM cat_tipo_amparos";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                TipoTramiteEntity tipo = new TipoTramiteEntity();
                tipo.setId_tipo_amparo(rs.getString(1));
                tipo.setNombre(rs.getString(2));
                tipo.setPersona(rs.getString(3));
                tipo.setAutoridad(rs.getString(4));
                tipo.setDocumento(rs.getString(5));
                tipo.setSolicitaPartes(rs.getBoolean(6));
                tipo.setObservaciones(rs.getString(7));
                tipo.setSeleccionar(rs.getBoolean(8));
                array.add(tipo);
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
    
    public ErrorEntity saveTipoAmparo(Connection conx, TipoTramiteEntity tipo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_tipo_amparos VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, tipo.getId_tipo_amparo());
            save.setString(2, u.eliminaEspacios(tipo.getNombre()));
            save.setString(3, u.eliminaEspacios(tipo.getPersona()));
            save.setString(4, u.eliminaEspacios(tipo.getAutoridad()));
            save.setString(5, u.eliminaEspacios(tipo.getDocumento()));
            save.setBoolean(6, tipo.getSolicitaPartes());
            save.setString(7, tipo.getObservaciones());
            save.setBoolean(8, tipo.getSeleccionar());
            save.setString(9, sesion.getId_usuario());
            save.setDate(10, sesion.getFechaActual());
            save.setDate(11, sesion.getFechaCero());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateTipoAmparo(Connection con, TipoTramiteEntity tipo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_tipo_amparos SET nombre = ?, persona = ?, autoridad = ?, documento = ?, "
                    + " solicita_partes = ?, observaciones = ?, seleccionar = ?, id_usuario = ?, f_cambio = ? WHERE id_tipo_amparo = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(tipo.getNombre()));
            save.setString(2, u.eliminaEspacios(tipo.getPersona()));
            save.setString(3, u.eliminaEspacios(tipo.getAutoridad()));
            save.setString(4, u.eliminaEspacios(tipo.getDocumento()));
            save.setBoolean(5, tipo.getSolicitaPartes());
            save.setString(6, tipo.getObservaciones());
            save.setBoolean(7, tipo.getSeleccionar());
            save.setString(8, sesion.getId_usuario());
            save.setDate(9, sesion.getFechaActual());
            save.setString(10, tipo.getId_tipo_amparo());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteTipoAmparo(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_tipo_amparos WHERE id_tipo_amparo = '"+id+"'";
        try{
            error.setError(con.createStatement().execute(query));
        }catch (SQLException e){
            error.setNumError(e.getErrorCode());
            new ErroresClase(e,"Error al eliminar los datos").getErrorSql();
        }catch (Exception ex){
            new ErroresClase(ex,"Error al eliminar los datos").getErrorJava();
        }finally{
        }
        return error;
    }
    
    public String getId(){
        String clave = "";
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_tipo_amparo,2) as SIGNED INTEGER)),0) FROM cat_tipo_amparos";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClave("C0000", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return clave;
    }
    
    public ErrorEntity updateSeleccionar(Connection con){
        ErrorEntity error = new ErrorEntity(true);
        try{
            error.setError(con.prepareStatement("UPDATE cat_tipo_amparos SET seleccionar = false").execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
}
