package baseSistema;

import baseGeneral.ErroresClase;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mavg
 */
public class ConexionMySql {

    private Connection conexion = null;
    
    public ConexionMySql() {
    }

    public Connection getConexionBD (){

        try {
            
            String baseDeDatos;
            String usuario;
            String contrasena;
            //String dirDriver="com.mysql.jdbc.Driver";
            String dirDriver="com.mysql.cj.jdbc.Driver";
            String url;
            String cad;
            BufferedReader br = new BufferedReader(new FileReader(new File("src/files/config.cll")));
            
            if ((cad = br.readLine()) != null) url=cad; else url="jdbc:mysql://192.168.1.250:3306/";
            if ((cad = br.readLine()) != null) baseDeDatos=cad; else baseDeDatos="tribunal";
            if ((cad = br.readLine()) != null) usuario=cad; else usuario="root";
            if ((cad = br.readLine()) != null) contrasena=cad; else contrasena="tricaez";
            br.close();
            Class.forName(dirDriver);
            //String conexionUrl = url+baseDeDatos+"?"+"user="+usuario+"&password="+contrasena;
            String conexionUrl = url+baseDeDatos+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&"+"user="+usuario+"&password="+contrasena+"&useSSL=false";
            conexion = DriverManager.getConnection(conexionUrl);
            
        } catch (SQLException sqlE) {
            new ErroresClase(sqlE,"Error en la consulta").getErrorSql();
        } catch (ClassNotFoundException classNotFoundE) {
            System.out.println(classNotFoundE.getMessage());
        } catch (Exception e) {
            new ErroresClase(e,"Error en la consulta").getErrorJava();
        }
        return conexion;
    }
       
    protected static void cleanVariables(ResultSet rs, PreparedStatement ps, Connection con ){
        if(rs!=null){
            try{
                rs.close();
            }catch (SQLException sqlE){
                new ErroresClase(sqlE,"Error en la consulta").getErrorSql();
            }
        }
        if(ps!=null){
            try{
                ps.close();
            }catch (SQLException sqlE){
                new ErroresClase(sqlE,"Error en la consulta").getErrorSql();
            }
        }
        try {
            if(con!=null && !con.isClosed()) con.close();
        } catch (SQLException sqlE){
                new ErroresClase(sqlE,"Error en la consulta").getErrorSql();                
        }
    }
    protected static void cleanVariables(PreparedStatement ps, Connection con ){
        cleanVariables(null, ps,con);
    }
    protected static void cleanVariables(ResultSet rs, Connection con ){
        cleanVariables(rs, null,con);
    }
    protected static void cleanVariables(Connection con ){
        cleanVariables(null, null,con);
    }
    protected static void cleanVariables(ResultSet rs ){
        cleanVariables(rs, null,null);
    }
    public static void cleanVariables(Connection con ,Boolean doit){
      if (doit) cleanVariables(null, null,con);
    }
    
    public boolean doCommit(Connection con){
        Boolean retorno=false;
        try{
            String queryString="COMMIT";
            Statement stmt = con.createStatement();
            retorno = !stmt.execute(queryString);
        }catch (SQLException sqlE){
            new ErroresClase(sqlE,"Error en la consulta").getErrorSql();
            return false;
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
            return false;
        }finally{
            cleanVariables(con);
        }
        return retorno;
    }
    
    public boolean setAutoCommit(Connection con,Boolean setting){
        Boolean retorno=false;
        try{
            String queryString="SET AUTOCOMMIT="+setting;
            Statement stmt = con.createStatement();
            retorno = !stmt.execute(queryString);
        }catch (SQLException sqlE){
            new ErroresClase(sqlE,"Error en la consulta").getErrorSql();
            return false;
        }catch (Exception e){
            new ErroresClase(e,"Error en la consulta").getErrorJava();
            return false;
        }finally{
            //cleanVariables(con);
        }
        return retorno;
    }
    
}
