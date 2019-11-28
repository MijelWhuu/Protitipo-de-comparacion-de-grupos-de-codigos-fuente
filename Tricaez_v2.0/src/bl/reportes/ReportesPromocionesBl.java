package bl.reportes;

import baseSistema.ConexionMySql;
import dao.reportes.infoExpedienteDao;
import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
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
public class ReportesPromocionesBl {

    infoExpedienteDao dao = new infoExpedienteDao();
    
    public ReportesPromocionesBl() {}

    public Boolean reporteToPDFSimple(String archivo, String ubicacion, Integer folio, Integer anio){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("folio", folio);
            params.put("anio", anio);
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
    
    public Boolean reporteToPDFGrupo(String archivo, String ubicacion, Integer anio){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("anio", anio);
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
    
    public void reporteVisualizarSimple(String archivo, Integer folio, Integer anio){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("folio", folio);
            params.put("anio", anio);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteVisualizarGrupo(String archivo, Integer anio){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("anio", anio);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimirSimple(String archivo, Integer folio, Integer anio){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("folio", folio);
            params.put("anio", anio);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimirGrupo(String archivo, Integer anio){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("anio", anio);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public ArrayList getNombres(String nombre){
        return dao.getNombres(nombre);
    }
}