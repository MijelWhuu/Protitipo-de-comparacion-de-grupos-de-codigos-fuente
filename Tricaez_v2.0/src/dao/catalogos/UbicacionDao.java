/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.catalogos;

import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import entitys.BASE_Combos;
import entitys.UbicacionEntity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import net.sourceforge.jtds.jdbc.DateTime;

/**
 *
 * @author RAULDELHOYO
 */
public class UbicacionDao extends ConexionMySql{
    
    
    public ArrayList CargaCombo(){
        ArrayList array = new ArrayList();
        Connection con  = getConexionBD();
        ResultSet rs     = null;
        String queryString = "select "
                + "id_usuario, "
                + "concat(nombre, ' ',paterno, ' ', materno) as nombrec "
                + "from gen_usuarios "
                + "order by nombrec;";
        try{
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            while(rs.next()){
                BASE_Combos e = new BASE_Combos();
                e.setId(rs.getString(1));
                e.setClave(rs.getString(1));
                e.setNombre(rs.getString(2));
                array.add(e);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            cleanVariables(rs,con);
        }
        return array;
    }
    
    public String existeExpediente(String expe){
        String id = "";
        Connection con  = getConexionBD();
        ResultSet rs     = null;
        String queryString = "select "
                + "id_usuario "
                + "from pri_ubicacion_expediente "
                + "where id_expediente = '"+expe+"' "
                + "order by fecha desc "
                + "limit 1;";
        try{
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            while(rs.next()){
                id = rs.getString(1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            cleanVariables(rs,con);
        }
        return id;
    }
    
    public String dameExpediente(String id_expe){
        String nombre = "";
        Connection con  = getConexionBD();
        ResultSet rs     = null;
        String queryString = "select "
                + "CONCAT(nombre, ' ', paterno, ' ', materno) "
                + "from gen_usuarios "
                + "where id_usuario='"+id_expe+"';";
        try{
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(queryString);
            while(rs.next()){
                nombre = rs.getString(1);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            cleanVariables(rs,con);
        }
        return nombre;
    }
    
    public Boolean GuardaUbicacion(String exp, String id, String prestador){
        Connection con  = getConexionBD();
        Boolean res = true;
        java.util.Date utilDate = new java.util.Date();
        long time = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(time);
        java.sql.Time sqlTime = new java.sql.Time(time);
        String fechaHora = sqlDate + " " + sqlTime;
        String queryString = "insert into "
                + "pri_ubicacion_expediente "
                + "values('"+exp+"','"+id+"','"+fechaHora+"','"+prestador+"');";
        try{
            Statement stmt = con.createStatement();
            res = !stmt.execute(queryString);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }finally{
            cleanVariables(con);
        }
        return res;
    }
    
    
}
