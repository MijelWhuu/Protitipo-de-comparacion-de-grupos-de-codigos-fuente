package dao.catalogos;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.AutoridadEntity;
import entitys.DomicilioEntity;
import entitys.MunicipioEntity;
import entitys.SesionEntity;
import entitys.TipoAutoridadEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class AutoridadDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public AutoridadDao() {
    }
    
    public ArrayList getAutoridades(String id_tipo){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_autoridad, a.id_tipo_autoridad, t.nombre, t.regla_municipal, a.id_municipio, t.nombre, a.nombre, a.calle, "
                + "a.exterior, a.interior, a.colonia, a.cp, a.telefono, a.email, a.observaciones FROM cat_autoridades a "
                + "INNER JOIN cat_tipo_autoridades t ON a.id_tipo_autoridad = t.id_tipo_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON a.id_municipio = m.id_municipio WHERE a.id_tipo_autoridad = '"+id_tipo+"' ORDER BY a.nombre";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                TipoAutoridadEntity tipo = new TipoAutoridadEntity(rs.getString(2), rs.getString(3), rs.getBoolean(4), "");
                MunicipioEntity mun = new MunicipioEntity(rs.getInt(5), rs.getString(6));
                DomicilioEntity dom = new DomicilioEntity(rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), null, rs.getString(13), rs.getString(14));
                array.add(new AutoridadEntity(rs.getString(1), tipo, mun, rs.getString(7), dom, rs.getString(15)));
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
    
    public ArrayList getAutoridades(String id_tipo, int id_municipio){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_autoridad, a.id_tipo_autoridad, t.nombre, t.regla_municipal, a.id_municipio, t.nombre, a.nombre, a.calle, "
                + "a.exterior, a.interior, a.colonia, a.cp, a.telefono, a.email, a.observaciones FROM cat_autoridades a "
                + "INNER JOIN cat_tipo_autoridades t ON a.id_tipo_autoridad = t.id_tipo_autoridad "
                + "INNER JOIN gen_municipios m ON a.id_municipio = m.id_municipio "
                + "WHERE a.id_tipo_autoridad = '"+id_tipo+"' AND a.id_municipio = "+id_municipio+" ORDER BY a.nombre";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                TipoAutoridadEntity tipo = new TipoAutoridadEntity(rs.getString(2), rs.getString(3), rs.getBoolean(4), "");
                MunicipioEntity mun = new MunicipioEntity(rs.getInt(5), rs.getString(6));
                DomicilioEntity dom = new DomicilioEntity(rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), null, rs.getString(13), rs.getString(14));
                array.add(new AutoridadEntity(rs.getString(1), tipo, mun, rs.getString(7), dom, rs.getString(15)));
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
    
    public ArrayList getMunicipiosTipoAutoridad(String id_tipo){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT m.id_municipio, m.nombre FROM gen_municipios m INNER JOIN cat_autoridades a ON m.id_municipio = a.id_municipio "
                + "WHERE id_tipo_autoridad = '"+id_tipo+"' GROUP BY id_municipio ORDER BY m.nombre";        
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
    
    public ErrorEntity saveAutoridad(Connection conx, AutoridadEntity auto, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "INSERT INTO cat_autoridades VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, auto.getId_autoridad());
            save.setString(2, auto.getTipo().getId_tipo_autoridad());
            if(!auto.getTipo().getRegla())
                save.setNull(3, java.sql.Types.INTEGER);
            else
                save.setInt(3, auto.getMun().getId_municipio());
            save.setString(4, auto.getNombre());
            save.setString(5, auto.getDom().getCalle());
            save.setString(6, auto.getDom().getNum_ext());
            save.setString(7, auto.getDom().getNum_int());
            save.setString(8, auto.getDom().getColonia());
            save.setString(9, auto.getDom().getCp());
            save.setString(10, auto.getDom().getTelefono());
            save.setString(11, auto.getDom().getEmail());
            save.setString(12, auto.getObservaciones());
            save.setBoolean(13, auto.getEstatus());
            save.setString(14, sesion.getId_usuario());
            save.setDate(15, sesion.getFechaActual());
            save.setDate(16, sesion.getFechaCero());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateAutoridad(Connection con, AutoridadEntity auto, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE cat_autoridades SET id_tipo_autoridad = ?, id_municipio = ?, nombre = ?, calle = ?, interior = ?, "
                    + "exterior = ?, colonia = ?, cp = ?, telefono = ?, email = ?, observaciones = ?, id_usuario = ?, f_cambio = ? "
                    + "WHERE id_autoridad = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, auto.getTipo().getId_tipo_autoridad());
            if(!auto.getTipo().getRegla())
                save.setNull(2, java.sql.Types.INTEGER);
            else
                save.setInt(2, auto.getMun().getId_municipio());
            save.setString(3, auto.getNombre());
            save.setString(4, auto.getDom().getCalle());
            save.setString(5, auto.getDom().getNum_ext());
            save.setString(6, auto.getDom().getNum_int());
            save.setString(7, auto.getDom().getColonia());
            save.setString(8, auto.getDom().getCp());
            save.setString(9, auto.getDom().getTelefono());
            save.setString(10, auto.getDom().getEmail());
            save.setString(11, auto.getObservaciones());
            save.setString(12, sesion.getId_usuario());
            save.setDate(13, sesion.getFechaActual());
            save.setString(14, auto.getId_autoridad());
            error.setError(save.execute());
        }catch (SQLException e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteAutoridad(Connection con, String id){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM cat_autoridades WHERE id_autoridad = '"+id+"'";
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
        String query = "SELECT IFNULL(MAX(CAST(SUBSTRING(id_autoridad,2) as SIGNED INTEGER)),0) FROM cat_autoridades";
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
    
    public ArrayList getAutoridadesComplete(){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT c.id_autoridad, c.nombre, c.id_tipo_autoridad, a.nombre, a.regla_municipal, c.id_municipio, "
                + "if(a.regla_municipal is true, m.nombre, 'NO APLICA') FROM cat_autoridades c "
                + "LEFT OUTER JOIN cat_tipo_autoridades a ON c.id_tipo_autoridad = a.id_tipo_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON c.id_municipio = m.id_municipio ORDER BY c.nombre";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                AutoridadEntity auto = new AutoridadEntity();
                auto.setId_autoridad(rs.getString(1));
                auto.setNombre(rs.getString(2));
                auto.setTipo(new TipoAutoridadEntity(rs.getString(3),rs.getString(4),rs.getBoolean(5),""));                
                auto.setMun(new MunicipioEntity(rs.getInt(6),rs.getString(7)));
                array.add(auto);
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
}
