package bl.reportes;

import baseSistema.ConexionMySql;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author mavg
 */
public class ReportesDinamicosAutosBl {

    public ReportesDinamicosAutosBl() {}

    public Boolean reporteToPDFCifra(String archivo, String ubicacion, Date inicio, Date fin, ArrayList array){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("inicio", inicio);
            params.put("fin", fin);
            params.put("array", array);
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
    
    public Boolean reporteToPDFListado(String archivo, String ubicacion, Date inicio, Date fin, ArrayList array){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("inicio", inicio);
            params.put("fin", fin);
            params.put("array", array);
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
    
    public void reporteVisualizarCifra(String archivo, Date inicio, Date fin, ArrayList array){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("inicio", inicio);
            params.put("fin", fin);
            params.put("array", array);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteVisualizarListado(String archivo, Date inicio, Date fin, ArrayList array){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("inicio", inicio);
            params.put("fin", fin);
            params.put("array", array);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimirCifra(String archivo, Date inicio, Date fin, ArrayList array){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("inicio", inicio);
            params.put("fin", fin);
            params.put("array", array);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimirListado(String archivo, Date inicio, Date fin, ArrayList array){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("inicio", inicio);
            params.put("fin", fin);
            params.put("array", array);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
}