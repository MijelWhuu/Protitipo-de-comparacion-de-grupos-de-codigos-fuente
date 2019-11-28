package dao.expedientes;

import baseGeneral.ErrorEntity;
import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import baseSistema.Utilidades;
import entitys.ActoEntity;
import entitys.ActorEntity;
import entitys.AutoridadEntity;
import entitys.ExpedienteEntity;
import entitys.ActorTerceroEntity;
import entitys.MunicipioEntity;
import entitys.AutorizadoEntity;
import entitys.DomicilioEntity;
import entitys.NombreEntity;
import entitys.PersonaAjenaEntity;
import entitys.ProcedimientoEntity;
import entitys.SesionEntity;
import entitys.TipoAutoridadEntity;
import entitys.TipoPretensionEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class ExpedienteUpdateDao extends ConexionMySql{

    Utilidades u = new Utilidades();

    public ExpedienteUpdateDao() {
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
    
    // <editor-fold defaultstate="collapsed" desc="Obtener informacion expediente">
    public ExpedienteEntity getExpediente(String expediente){
        ExpedienteEntity expe = new ExpedienteEntity();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String query = "SELECT expediente, fecha, id_procedimientos, id_actos, id_tipo_pretension, numero, cantidad_pretension, observaciones "
                + "FROM pri_expediente WHERE expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                expe.setExpediente(rs.getString(1));
                expe.setFecha(rs.getDate(2));
                expe.setTipoProcedimiento(new ProcedimientoEntity(rs.getString(3)));
                expe.setTipoActo(new ActoEntity(rs.getString(4)));
                expe.setTipoPretension(new TipoPretensionEntity(rs.getString(5)));
                expe.setNumero(rs.getString(6));
                expe.setCantidad(rs.getDouble(7));
                expe.setObservaciones(rs.getString(8));
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
                + "a.email, a.representante_legal, a.nombre_asociacion, a.representante_legal_2, a.nombre_asociacion_2, m.id_municipio, "
                + "m.nombre, a.observaciones, a.expediente FROM gen_municipios m "
                + "RIGHT OUTER JOIN pri_actores a ON m.id_municipio = a.id_municipio "
                + "WHERE a.expediente = '"+expediente+"'";
        try{
            rs = con.createStatement().executeQuery(query);
            while (rs.next()){
                ActorEntity actor = new ActorEntity();
                actor.setId_actor(rs.getString(1));
                actor.setNombre(new NombreEntity(rs.getString(2),rs.getString(3),rs.getString(4)));
                MunicipioEntity mun = null;
                if(rs.getInt(12) != 0){ 
                    mun = new MunicipioEntity(rs.getInt(16),rs.getString(17));
                }
                actor.setDom(new DomicilioEntity(rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9),mun,rs.getString(10),rs.getString(11)));
                actor.setRepresentante1(rs.getBoolean(12));
                actor.setGrupo1(rs.getString(13));
                actor.setRepresentante2(rs.getBoolean(14));
                actor.setGrupo2(rs.getString(15));
                actor.setObservaciones(rs.getString(18));
                actor.setExpe(new ExpedienteEntity(rs.getString(19)));
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
        String query = "SELECT t.nombre, a.id_tipo_autoridad, if(t.regla_municipal is true, m.nombre, 'NO APLICA'), if(a.id_municipio IS NULL, '', a.id_municipio), a.nombre, "
                + "a.id_autoridad FROM cat_tipo_autoridades t "
                + "INNER JOIN  cat_autoridades a ON t.id_tipo_autoridad = a.id_tipo_autoridad "
                + "INNER JOIN rel_expediente_has_cat_autoridades r ON r.id_autoridad = a.id_autoridad "
                + "LEFT OUTER JOIN gen_municipios m ON m.id_municipio = a.id_municipio "
                + "WHERE r.expediente = '"+expediente+"'";
        System.out.println(query);
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
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Guardar datos">
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
    
    public ErrorEntity saveExpedienteAutoridadAjena(Connection conx, String expediente, String id_tercero_autoridad){
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
    
    // <editor-fold defaultstate="collapsed" desc="Actualizar datos">
    public ErrorEntity updateExpediente(Connection conx, ExpedienteEntity expe, ExpedienteEntity viejo, SesionEntity sesion){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_expediente SET expediente = ?, fecha = ?, id_procedimientos = ?, "
                    + "id_actos = ?, id_tipo_pretension = ?, numero = ?, cantidad_pretension = ?, observaciones = ?, id_usuario = ?, f_cambio = ? "
                    + "WHERE expediente = ?";
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
            save.setString(11, viejo.getExpediente());
            error.setError(save.execute());            
        }catch (SQLException e){
            System.out.println("UError1");
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorSql());
        }catch (Exception e){
            error.setTipo(new ErroresClase(e,"Error al modificar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateActor(Connection conx, ActorEntity actor){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_actores p SET nombre = ?, paterno = ?, materno = ?, calle = ?, exterior = ?, interior = ?, "
                    + "colonia = ?, cp = ?, telefono = ?, email = ?, id_municipio = ?, representante_legal = ?, nombre_asociacion = ?, "
                    + "representante_legal = ?, nombre_asociacion = ?, observaciones = ?, expediente = ? WHERE id_actor = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, actor.getNombre().getNombre());
            save.setString(2, actor.getNombre().getPaterno());
            save.setString(3, actor.getNombre().getMaterno());
            save.setString(4, actor.getDom().getCalle());
            save.setString(5, actor.getDom().getNum_ext());
            save.setString(6, actor.getDom().getNum_int());
            save.setString(7, actor.getDom().getColonia());
            save.setString(8, actor.getDom().getCp());
            save.setString(9, actor.getDom().getTelefono());
            save.setString(10, actor.getDom().getEmail());
            if(actor.getDom().getMun()!=null)
                save.setInt(11, actor.getDom().getMun().getId_municipio());
            else
                save.setNull(11, java.sql.Types.VARCHAR);
            save.setBoolean(12, actor.getRepresentante1());
            save.setString(13, actor.getGrupo1());
            save.setBoolean(14, actor.getRepresentante2());
            save.setString(15, actor.getGrupo2());
            save.setString(16, actor.getObservaciones());
            save.setString(17, actor.getExpe().getExpediente());
            save.setString(18, actor.getId_actor());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("UError3");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updateTerceroActor(Connection conx, ActorTerceroEntity actor){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_terceros_actores p SET nombre = ?, paterno = ?, materno = ?, calle = ?, exterior = ?, interior = ?, "
                    + "colonia = ?, cp = ?, telefono = ?, email = ?, id_municipio = ?, observaciones = ?, expediente = ? WHERE id_tercero_actor = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, actor.getNombre().getNombre());
            save.setString(2, actor.getNombre().getPaterno());
            save.setString(3, actor.getNombre().getMaterno());
            save.setString(4, actor.getDom().getCalle());
            save.setString(5, actor.getDom().getNum_ext());
            save.setString(6, actor.getDom().getNum_int());
            save.setString(7, actor.getDom().getColonia());
            save.setString(8, actor.getDom().getCp());
            save.setString(9, actor.getDom().getTelefono());
            save.setString(10, actor.getDom().getEmail());
            if(actor.getDom().getMun()!=null)
                save.setInt(11, actor.getDom().getMun().getId_municipio());
            else
                save.setNull(11, java.sql.Types.VARCHAR);
            save.setString(12, actor.getObservaciones());
            save.setString(13, actor.getExpe().getExpediente());
            save.setString(14, actor.getId_tercero_actor());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("UError4");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity updatePersonaAjena(Connection conx, PersonaAjenaEntity perso){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_personas_ajenas p SET nombre = ?, paterno = ?, materno = ?, calle = ?, exterior = ?, interior = ?, "
                    + "colonia = ?, cp = ?, telefono = ?, email = ?, id_municipio = ?, observaciones = ?, expediente = ? WHERE id_persona_ajena = ?";
            PreparedStatement save = conx.prepareStatement(query);
            save.setString(1, perso.getNombre().getNombre());
            save.setString(2, perso.getNombre().getPaterno());
            save.setString(3, perso.getNombre().getMaterno());
            save.setString(4, perso.getDom().getCalle());
            save.setString(5, perso.getDom().getNum_ext());
            save.setString(6, perso.getDom().getNum_int());
            save.setString(7, perso.getDom().getColonia());
            save.setString(8, perso.getDom().getCp());
            save.setString(9, perso.getDom().getTelefono());
            save.setString(10, perso.getDom().getEmail());
            if(perso.getDom().getMun()!=null)
                save.setInt(11, perso.getDom().getMun().getId_municipio());
            else
                save.setNull(11, java.sql.Types.VARCHAR);
            save.setString(12, perso.getObservaciones());
            save.setString(13, perso.getExpe().getExpediente());
            save.setString(14, perso.getId_persona_ajena());
            error.setError(save.execute());
        }catch (SQLException e){
            System.out.println("UError");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Actualizar a NULL">
    public ErrorEntity nullActor(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_actores p SET expediente = NULL WHERE expediente = '"+expediente+"'";
            error.setError(conx.prepareStatement(query).execute());
        }catch (SQLException e){
            System.out.println("NError2");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity nullTerceroActor(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_terceros_actores p SET expediente = NULL WHERE expediente = '"+expediente+"'";
            error.setError(conx.prepareStatement(query).execute());
        }catch (SQLException e){
            System.out.println("NError3");
            error.setTipo(new ErroresClase(e,"Error al guardar los datos").getErrorSql());
        }catch (Exception ex){
            error.setTipo(new ErroresClase(ex,"Error al guardar los datos").getErrorJava());
        }finally{
        }
        return error;
    }
    
    public ErrorEntity nullPersonaAjena(Connection conx, String expediente){
        ErrorEntity error = new ErrorEntity(true);
        try{
            String query = "UPDATE pri_personas_ajenas p SET expediente = NULL WHERE expediente = '"+expediente+"'";
            error.setError(conx.prepareStatement(query).execute());
        }catch (SQLException e){
            System.out.println("NError4");
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
    // </editor-fold>
    
}
