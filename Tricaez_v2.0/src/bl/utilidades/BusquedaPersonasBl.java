package bl.utilidades;

import baseSistema.ConexionMySql;
import dao.utilidades.BusquedaPersonasDao;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author mavg
 */
public class BusquedaPersonasBl {

    BusquedaPersonasDao dao = new BusquedaPersonasDao();
    
    public BusquedaPersonasBl() {
    }
    
    public ArrayList getNombres(String lugar, String tabla, String filtro1, String filtro2, String filtro3, String oper1, String oper2, String oper3){
        return dao.getNombres(lugar, tabla, filtro1, filtro2, filtro3, oper1, oper2, oper3);
    }
    
    public ArrayList getNombres(String nombre, String paterno, String materno){
        return dao.getNombres(nombre, paterno, materno);
    }
    
    public Boolean reporteToPDF(String archivo, Map params, String ubicacion){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperExportManager.exportReportToPdfFile(jasperPrint, ubicacion);
            error = false;
        }catch(Exception e){
            System.out.println(e.getMessage());
            error = true;
        }
        return error;
    }
    
    public void reporteImprimir(String archivo, Map params){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}
