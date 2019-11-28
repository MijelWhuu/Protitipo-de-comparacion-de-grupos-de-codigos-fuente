package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.MunicipioEntity;
import entitys.SesionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class MunicipiosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public MunicipiosDao() {
    }
    
    public ArrayList getMunicipios(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT id_municipio, nombre FROM gen_municipios ORDER BY nombre";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                array.add(new MunicipioEntity(rs.getInt(1),rs.getString(2)));
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
    
    public ErrorEntity saveMunicipio(Connection conx, MunicipioEntity mun, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO gen_municipios VALUES(?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setInt(1, mun.getId_municipio());
            save.setString(2, u.eliminaEspacios(mun.getNombre()));
            save.setString(3, sesion.getId_usuario());
            save.setDate(4, sesion.getFechaActual());
            save.setDate(5, sesion.getFechaCero());
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
    
    public ErrorEntity updateMunicipio(Connection con, MunicipioEntity mun, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE gen_municipios SET nombre = ?, id_usuario = ?, "
                    + "f_cambio = ? WHERE id_municipio = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(mun.getNombre()));
            save.setString(2, sesion.getId_usuario());
            save.setDate(3, sesion.getFechaActual());
            save.setInt(4, mun.getId_municipio());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteMunicipio(Connection con, Integer id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM gen_municipios WHERE id_municipio = "+id;
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
       
    public int getMaxId(){
        int max = 0;
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT MAX(id_municipio) FROM gen_municipios";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               max = rs.getInt(1);
            }
            max++;
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return max;
    }
    
}
