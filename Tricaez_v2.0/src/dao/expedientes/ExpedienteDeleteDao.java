package dao.expedientes;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.ActoEntity;
import entitys.ActorEntity;
import entitys.ExpedienteEntity;
import entitys.ActorTerceroEntity;
import entitys.AutoridadEntity;
import entitys.MunicipioEntity;
import entitys.AutorizadoEntity;
import entitys.DomicilioEntity;
import entitys.NombreEntity;
import entitys.PersonaAjenaEntity;
import entitys.ProcedimientoEntity;
import entitys.TipoAutoridadEntity;
import entitys.TipoPretensionEntity;
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
public class ExpedienteDeleteDao extends ConexionMySql{

    Utilidades u = new Utilidades();
    
    public ExpedienteDeleteDao() {
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
    
    // <editor-fold defaultstate="collapsed" desc="Obtener informacion expediente">
    public ExpedienteEntity getExpediente(String expediente){
        ExpedienteEntity expe = new ExpedienteEntity();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT e.expediente, e.fecha, e.id_procedimientos, e.id_actos, e.id_tipo_pretension, e.cantidad_pretension, "
                + "e.observaciones, p.nombre, a.nombre, t.nombre FROM pri_expediente e "
                + "LEFT OUTER JOIN cat_procedimientos p ON e.id_procedimientos=p.id_procedimientos "
                + "LEFT OUTER JOIN cat_actos a ON e.id_actos=a.id_actos "
                + "LEFT OUTER JOIN cat_tipo_pretensiones t ON e.id_tipo_pretension=t.id_tipo_pretension "
                + "WHERE e.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                expe.setExpediente(rs.getString(1));
                expe.setFecha(rs.getDate(2));
                expe.setTipoProcedimiento(new ProcedimientoEntity(rs.getString(3),rs.getString(8),""));
                expe.setTipoActo(new ActoEntity(rs.getString(4),rs.getString(9),""));
                expe.setTipoPretension(null);
                if(rs.getString(5) != null){
                    expe.setTipoPretension(new TipoPretensionEntity(rs.getString(5),rs.getString(10),""));
                }
                expe.setCantidad(rs.getDouble(6));
                expe.setObservaciones(rs.getString(7));
            }
        }catch (SQLException e){
            new ErroresClase(e,"Error en la consulta").getErrorSql();
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }finally{
            cleanVariables(rs,con);
        }
        return expe;
    }
    
    public ArrayList getActores(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_actor, a.nombre, a.paterno, a.materno, a.calle, a.exterior, a.interior, a.colonia, a.cp, a.telefono, "
                + "a.email, a.id_municipio, a.representante_legal, a.nombre_asociacion, a.representante_legal_2, a.nombre_asociacion_2, "
                + "a.observaciones, a.expediente, m.nombre FROM pri_actores a "
                + "LEFT OUTER JOIN gen_municipios m ON a.id_municipio = m.id_municipio "
                + "WHERE a.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                ActorEntity actor = new ActorEntity();
                actor.setId_actor(rs.getString(1));
                actor.setNombre(new NombreEntity(rs.getString(2),rs.getString(3),rs.getString(4)));
                MunicipioEntity mun = null;
                if(rs.getInt(12) != 0){ 
                    mun = new MunicipioEntity(rs.getInt(12),rs.getString(19));
                }
                actor.setDom(new DomicilioEntity(rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),mun,rs.getString(10),rs.getString(11)));
                actor.setRepresentante1(rs.getBoolean(13));
                actor.setGrupo1(rs.getString(14));
                actor.setRepresentante2(rs.getBoolean(15));
                actor.setGrupo2(rs.getString(16));
                actor.setObservaciones(rs.getString(17));
                actor.setExpe(new ExpedienteEntity(rs.getString(18)));
                array.add(actor);
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
    
    public ArrayList getAutoridades(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT t.nombre, a.id_tipo_autoridad, if(t.regla_municipal is true, m.nombre, 'NO APLICA'), a.id_municipio, a.nombre, "
                + "a.id_autoridad FROM cat_tipo_autoridades t "
                + "INNER JOIN  cat_autoridades a ON t.id_tipo_autoridad = a.id_tipo_autoridad "
                + "INNER JOIN rel_expediente_has_cat_autoridades r ON r.id_autoridad = a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON m.id_municipio = a.id_municipio "
                + "WHERE r.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                AutoridadEntity auto = new AutoridadEntity();                
                TipoAutoridadEntity tipo = new TipoAutoridadEntity();
                tipo.setId_tipo_autoridad(rs.getString(2));
                tipo.setNombre(rs.getString(1));
                auto.setTipo(tipo);
                auto.setMun(new MunicipioEntity(rs.getInt(4),rs.getString(3)));
                auto.setNombre(rs.getString(5));
                auto.setId_autoridad(rs.getString(6));
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
    
    public ArrayList getAutoridadesTercero(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT t.nombre, a.id_tipo_autoridad, if(t.regla_municipal is true, m.nombre, 'NO APLICA'), a.id_municipio, a.nombre, "
                + "a.id_autoridad FROM cat_tipo_autoridades t "
                + "INNER JOIN  cat_autoridades a ON t.id_tipo_autoridad = a.id_tipo_autoridad "
                + "INNER JOIN rel_expediente_has_pri_terceros_autoridades r ON r.id_autoridad = a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON m.id_municipio = a.id_municipio "
                + "WHERE r.expediente = '"+expediente+"'";    
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                AutoridadEntity auto = new AutoridadEntity();                
                TipoAutoridadEntity tipo = new TipoAutoridadEntity();
                tipo.setId_tipo_autoridad(rs.getString(2));
                tipo.setNombre(rs.getString(1));
                auto.setTipo(tipo);
                auto.setMun(new MunicipioEntity(rs.getInt(4),rs.getString(3)));
                auto.setNombre(rs.getString(5));
                auto.setId_autoridad(rs.getString(6));
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
    
    public ArrayList getActoresTercero(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_tercero_actor, a.nombre, a.paterno, a.materno, a.calle, a.exterior, a.interior, a.colonia, a.cp, "
                + "a.telefono, a.email, m.id_municipio, m.nombre, a.observaciones FROM gen_municipios m "
                + "RIGHT OUTER JOIN pri_terceros_actores a ON m.id_municipio = a.id_municipio "
                + "WHERE a.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){                
                ActorTerceroEntity actor = new ActorTerceroEntity();
                actor.setId_tercero_actor(rs.getString(1));
                actor.setNombre(new NombreEntity(rs.getString(2),rs.getString(3),rs.getString(4)));
                MunicipioEntity mun = null;
                if(rs.getInt(12) != 0){ 
                    mun = new MunicipioEntity(rs.getInt(12),rs.getString(13));
                }
                DomicilioEntity dom = new DomicilioEntity(rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),mun,rs.getString(10),rs.getString(11));
                actor.setDom(dom);
                actor.setObservaciones(rs.getString(14));
                array.add(actor);
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
    
    public ArrayList getAutoridadesAjenas(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT t.nombre, a.id_tipo_autoridad, if(t.regla_municipal is true, m.nombre, 'NO APLICA'), a.id_municipio, a.nombre, "
                + "a.id_autoridad FROM cat_tipo_autoridades t "
                + "INNER JOIN  cat_autoridades a ON t.id_tipo_autoridad = a.id_tipo_autoridad "
                + "INNER JOIN rel_expediente_has_pri_autoridades_ajenas r ON r.id_autoridad = a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON m.id_municipio = a.id_municipio "
                + "WHERE r.expediente = '"+expediente+"'";    
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                AutoridadEntity auto = new AutoridadEntity();                
                TipoAutoridadEntity tipo = new TipoAutoridadEntity();
                tipo.setId_tipo_autoridad(rs.getString(2));
                tipo.setNombre(rs.getString(1));
                auto.setTipo(tipo);
                auto.setMun(new MunicipioEntity(rs.getInt(4),rs.getString(3)));
                auto.setNombre(rs.getString(5));
                auto.setId_autoridad(rs.getString(6));
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
    
    public ArrayList getPersonasAjenas(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_persona_ajena, a.nombre, a.paterno, a.materno, a.calle, a.exterior, a.interior, a.colonia, a.cp, "
                + "a.telefono, a.email, m.id_municipio, m.nombre, a.observaciones FROM gen_municipios m "
                + "RIGHT OUTER JOIN pri_personas_ajenas a ON m.id_municipio = a.id_municipio "
                + "WHERE a.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){                
                PersonaAjenaEntity perso = new PersonaAjenaEntity();
                perso.setId_persona_ajena(rs.getString(1));
                perso.setNombre(new NombreEntity(rs.getString(2),rs.getString(3),rs.getString(4)));
                MunicipioEntity mun = null;
                if(rs.getInt(12) != 0){ 
                    mun = new MunicipioEntity(rs.getInt(12),rs.getString(13));
                }
                perso.setDom(new DomicilioEntity(rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),mun,rs.getString(10),rs.getString(11)));
                perso.setObservaciones(rs.getString(14));
                array.add(perso);
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
    
    public ArrayList getAutorizados(String expediente){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT a.id_autorizado, a.nombre, a.paterno, a.materno, a.calle, a.num_ext, a.num_int, "
                + "a.colonia, a.cp, a.telefono, a.email, m.nombre, a.id_municipio, r.defensor, r.observaciones "
                + "FROM gen_municipios m "
                + "RIGHT OUTER JOIN cat_autorizados a ON m.id_municipio = a.id_municipio "
                + "INNER JOIN rel_expediente_has_cat_autorizados r ON r.id_autorizado = a.id_autorizado "
                + "WHERE r.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){    
                AutorizadoEntity auto = new AutorizadoEntity();
                auto.setId_autorizado(rs.getString(1));
                auto.setNombre(new NombreEntity(rs.getString(2),rs.getString(3),rs.getString(4)));
                MunicipioEntity mun = null;
                if(rs.getInt(13)!=0){
                    mun = new MunicipioEntity(rs.getInt(13),rs.getString(12));
                }
                DomicilioEntity dom = new DomicilioEntity(rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),
                mun,rs.getString(10),rs.getString(11));
                auto.setDom(dom);
                auto.setId_parte(rs.getInt(14));
                auto.setObservaciones(rs.getString(15));
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
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Borrar informacion expediente">
    public ErrorEntity deleteExpediente(Connection con, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        String query = "DELETE FROM pri_expediente WHERE expediente = '"+expediente+"'";
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
    
    
    public ErrorEntity deleteExpedienteTerceroAutoridad(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM rel_expediente_has_pri_terceros_autoridades WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteExpedienteAutorizado(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM rel_expediente_has_cat_autorizados WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteExpedienteAutoridad(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM rel_expediente_has_cat_autoridades WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteExpedienteTerceroActor(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM pri_terceros_actores WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteExpedienteActor(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM pri_actores WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteExpedienteAutoridadAjena(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM rel_expediente_has_pri_autoridades_ajenas WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity deleteExpedientePersonaAjena(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "DELETE FROM pri_personas_ajenas WHERE expediente = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, expediente);
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println(e.getMessage());
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    // </editor-fold>
}
