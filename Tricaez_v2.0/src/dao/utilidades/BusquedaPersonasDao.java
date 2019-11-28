package dao.utilidades;

import baseGeneral.ErroresClase;
import baseSistema.ConexionMySql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author mavg
 */
public class BusquedaPersonasDao extends ConexionMySql{
    
    public BusquedaPersonasDao() {
    }
    
    public ArrayList getNombres(String lugar, String tabla, String filtro1, String filtro2, String filtro3, String oper1, String oper2, String oper3){
        
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;
        String global = "SELECT nombre, paterno, materno, '"+lugar+"', expediente FROM "+tabla+" WHERE ";
        try{
            PreparedStatement save = null;
            if(filtro1.length()!=0 && filtro2.length()!=0 && filtro3.length()!=0){
                save = con.prepareStatement(global + "nombre "+oper1+" ? AND paterno "+oper2+" ? AND materno "+oper3+" ? ORDER BY expediente");
                save.setString(1, filtro1);
                save.setString(2, filtro2);
                save.setString(3, filtro3);
            }else if(filtro1.length()!=0 && filtro2.length()!=0 && filtro3.length()==0){
                save = con.prepareStatement(global + "nombre "+oper1+" ? AND paterno "+oper2+" ? ORDER BY expediente");
                save.setString(1, filtro1);
                save.setString(2, filtro2);
            }else if(filtro1.length()!=0 && filtro2.length()==0 && filtro3.length()!=0){
                save = con.prepareStatement(global + "nombre "+oper1+" ? AND materno "+oper3+" ? ORDER BY expediente");
                save.setString(1, filtro1);
                save.setString(2, filtro3);
            }else if(filtro1.length()==0 && filtro2.length()!=0 && filtro3.length()!=0){
                save = con.prepareStatement(global + "paterno "+oper2+" ? AND materno "+oper3+" ? ORDER BY expediente");
                save.setString(1, filtro2);
                save.setString(2, filtro3);
            }else if(filtro1.length()!=0 && filtro2.length()==0 && filtro3.length()==0){
                save = con.prepareStatement(global + "nombre "+oper1+" ? ORDER BY expediente");
                save.setString(1, filtro1);
            }else if(filtro1.length()==0 && filtro2.length()!=0 && filtro3.length()==0){
                save = con.prepareStatement(global + "paterno "+oper2+" ? ORDER BY expediente");
                save.setString(1, filtro2);
            }else if(filtro1.length()==0 && filtro2.length()==0 && filtro3.length()!=0){
                save = con.prepareStatement(global + "materno "+oper1+" ? ORDER BY expediente");
                save.setString(1, filtro3);
            }
            if(save != null){
                rs = save.executeQuery();
                while (rs.next()){
                    Object []obj = new Object[5];
                    obj[0] = rs.getString(1);
                    obj[1] = rs.getString(2);
                    obj[2] = rs.getString(3);
                    obj[3] = rs.getString(4);
                    obj[4] = rs.getString(5);
                    array.add(obj);
                }
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
    
    public ArrayList getNombres(String nombre, String paterno, String materno){
        ArrayList array = new ArrayList();
        Connection con = getConexionBD();
        ResultSet rs = null;        
        try{
            String query = "SELECT id_actor, 'ACTORES', expediente FROM pri_actores WHERE nombre = ? AND paterno = ? AND materno = ? "
                    + "UNION SELECT id_tercero_actor, 'TERCEROS ACTORES', expediente FROM pri_terceros_actores WHERE nombre = ? "
                    + "AND paterno = ? AND materno = ? UNION SELECT id_persona_ajena, 'PERSONAS AJENAS', expediente FROM pri_personas_ajenas "
                    + "WHERE nombre = ? AND paterno = ? AND materno = ?";
            PreparedStatement save = con.prepareStatement(query);
            save.setString(1, nombre);
            save.setString(2, paterno);
            save.setString(3, materno);
            save.setString(4, nombre);
            save.setString(5, paterno);
            save.setString(6, materno);
            save.setString(7, nombre);
            save.setString(8, paterno);
            save.setString(9, materno);
            rs = save.executeQuery();
            while (rs.next()){
                String cad = "<html><body>Expediente: <b>"+rs.getString(3)+"</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Forma parte de: <b>"
                        +rs.getString(2)+"</b></body></html>";
                array.add(cad);
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
     
}
