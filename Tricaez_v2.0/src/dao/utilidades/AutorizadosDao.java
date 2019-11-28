package dao.utilidades;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutorizadoEntity;
import entitys.DomicilioEntity;
import entitys.MunicipioEntity;
import entitys.NombreEntity;
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
public class AutorizadosDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public AutorizadosDao() {
    }
    
    public ArrayList getAutorizadores(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_autorizado, a.nombre, a.paterno, a.materno, a.calle, a.num_ext, a.num_int, a.colonia, a.cp, if(a.id_municipio IS NULL, '56', a.id_municipio), "
                + "a.telefono, a.email, a.observaciones, if(a.id_municipio IS NULL, 'ZACATECAS', m.nombre) FROM cat_autorizados a "
                + "LEFT OUTER JOIN gen_municipios m ON a.id_municipio = m.id_municipio";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                NombreEntity nom = new NombreEntity(rs.getString(2),rs.getString(3),rs.getString(4));
                DomicilioEntity dom = new DomicilioEntity(rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),
                new MunicipioEntity(rs.getInt(10),rs.getString(14)),rs.getString(11),rs.getString(12));
                AutorizadoEntity auto = new AutorizadoEntity(rs.getString(1),nom,dom,rs.getString(13));                
                array.add(auto);
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
    
    public ErrorEntity saveAutorizador(Connection conx, AutorizadoEntity auto, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_autorizados VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, auto.getId_autorizado());
            save.setString(2, u.eliminaEspacios(auto.getNombre().getNombre()));
            save.setString(3, u.eliminaEspacios(auto.getNombre().getPaterno()));
            save.setString(4, u.eliminaEspacios(auto.getNombre().getMaterno()));
            save.setString(5, u.eliminaEspacios(auto.getDom().getCalle()));
            save.setString(6, u.eliminaEspacios(auto.getDom().getNum_ext()));
            save.setString(7, u.eliminaEspacios(auto.getDom().getNum_int()));
            save.setString(8, u.eliminaEspacios(auto.getDom().getColonia()));
            save.setString(9, auto.getDom().getCp());
            if(auto.getDom().getMun()==null)
                save.setNull(10, java.sql.Types.INTEGER);
            else
                save.setInt(10, auto.getDom().getMun().getId_municipio());
            save.setString(11, auto.getDom().getTelefono());
            save.setString(12, auto.getDom().getEmail());
            save.setString(13, auto.getObservaciones());
            save.setBoolean(14, auto.getEstatus());
            save.setString(15, sesion.getId_usuario());
            save.setDate(16, sesion.getFechaActual());
            save.setDate(17, sesion.getFechaCero());
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
    
    public ErrorEntity updateAutorizador(Connection con, AutorizadoEntity auto, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_autorizados SET nombre = ?, paterno = ?, materno = ?, calle = ?, num_ext = ?, num_int = ?, colonia = ?, "
                    + "cp = ?, id_municipio = ?, telefono = ?, email = ?, observaciones = ?, id_usuario = ?, f_cambio = ? WHERE id_autorizado = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, u.eliminaEspacios(auto.getNombre().getNombre()));
            save.setString(2, u.eliminaEspacios(auto.getNombre().getPaterno()));
            save.setString(3, u.eliminaEspacios(auto.getNombre().getMaterno()));
            save.setString(4, u.eliminaEspacios(auto.getDom().getCalle()));
            save.setString(5, u.eliminaEspacios(auto.getDom().getNum_ext()));
            save.setString(6, u.eliminaEspacios(auto.getDom().getNum_int()));
            save.setString(7, u.eliminaEspacios(auto.getDom().getColonia()));
            save.setString(8, auto.getDom().getCp());
            if(auto.getDom().getMun()==null)
                save.setNull(9, java.sql.Types.INTEGER);
            else
                save.setInt(9, auto.getDom().getMun().getId_municipio());
            save.setString(10, auto.getDom().getTelefono());
            save.setString(11, auto.getDom().getEmail());
            save.setString(12, auto.getObservaciones());
            save.setString(13, sesion.getId_usuario());
            save.setDate(14, sesion.getFechaActual());
            save.setString(15, auto.getId_autorizado());
            error.setError(save.execute());            
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteAutorizador(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_autorizados WHERE id_autorizado = '"+id+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_autorizado,2) as SIGNED INTEGER)),0) FROM cat_autorizados";
        try{
            Integer valor = 0;
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
               valor = rs.getInt(1);
            }
            valor++;
            clave = u.formatearClave("A0000", valor);
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return clave;
    }
}
