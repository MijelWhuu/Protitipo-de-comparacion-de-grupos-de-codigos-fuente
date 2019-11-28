package dao.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.NombreEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class UsuariosDao extends ConexionMySql{
    
    Utilidades u = new Utilidades();
    
    public UsuariosDao() {
    }
    
    public ArrayList getEmpleados(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_usuario,usuario,nombre,paterno,materno,pass,pantallas FROM gen_usuarios WHERE estatus IS TRUE";
        try{
            PreparedStatement save = con.prepareStatement(query);
            rs = save.executeQuery();
            while (rs.next()){
                SesionEntity usuario = new SesionEntity();
                usuario.setId_usuario(rs.getString(1));
                usuario.setUsuario(rs.getString(2));
                usuario.setNombre(new NombreEntity(rs.getString(3),rs.getString(4),rs.getString(5)));
                usuario.setPass(rs.getString(6));
                usuario.setPantallas(rs.getString(7));
                array.add(usuario);
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
    
    public Boolean existeUsuario(String usuario){
        Boolean existe = false;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM gen_usuarios WHERE usuario = '"+usuario+"'";
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
    
    public ErrorEntity saveUsuario(Connection conx, SesionEntity usuario, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO gen_usuarios VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1,usuario.getId_usuario());
            save.setString(2,usuario.getUsuario());
            save.setString(3,usuario.getNombre().getNombre());
            save.setString(4,usuario.getNombre().getPaterno());
            save.setString(5,usuario.getNombre().getMaterno());
            save.setString(6,usuario.getPass());
            save.setBoolean(7,true);
            save.setString(8,usuario.getPantallas());
            save.setDate(9, sesion.getFechaActual());
            save.setDate(10, sesion.getFechaCero());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateUsuario(Connection con, SesionEntity usuario){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE gen_usuarios SET usuario = ?, nombre = ?, paterno = ?, materno = ?, pass = ?, pantallas = ? WHERE id_usuario = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1,usuario.getUsuario());
            save.setString(2,usuario.getNombre().getNombre());
            save.setString(3,usuario.getNombre().getPaterno());
            save.setString(4,usuario.getNombre().getMaterno());
            save.setString(5,usuario.getPass());
            save.setString(6,usuario.getPantallas());
            save.setString(7,usuario.getId_usuario());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteUsuario(Connection con, String id_usuario){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE gen_usuarios SET estatus = false WHERE id_usuario = '"+id_usuario+"'";
            Statement stmt = con.createStatement();
            error.setError(stmt.execute(query));
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public String getIdUsuario(Connection con){
        String clave = "";
        ResultSet rs = null;
        String query = "SELECT MAX(CAST(SUBSTRING(id_usuario,2) as SIGNED INTEGER)) FROM gen_usuarios";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClave("U0000", valor);
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
