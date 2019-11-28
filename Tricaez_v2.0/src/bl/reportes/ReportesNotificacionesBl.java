package bl.reportes;

import baseSistema.ConexionMySql;
import dao.reportes.ReportesNotificacionesDao;
import java.io.File;
import java.sql.Connection;
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
public class ReportesNotificacionesBl {

    ReportesNotificacionesDao dao = new ReportesNotificacionesDao();
    
    public ReportesNotificacionesBl() {}

    public Boolean reporteToPDFFolio(String archivo, String ubicacion, String id_relacion){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pRelacion", id_relacion);
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
    
    public Boolean reporteToPDFExpediente(String archivo, String ubicacion, String expediente){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pExpediente", expediente);
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
    
    public void reporteVisualizarFolio(String archivo, String id_relacion){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pRelacion", id_relacion);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteVisualizarExpediente(String archivo, String expediente){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pExpediente", expediente);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimirFolio(String archivo, String id_relacion){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pRelacion", id_relacion);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimirExpediente(String archivo, String expediente){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pExpediente", expediente);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public String[] getSeleccion(Integer folio, Integer anio){
        return dao.getSelection(folio, anio);
    }
    
    public Boolean existeFolio(Integer folio, Integer anio){
        return dao.existeFolio(folio, anio);
    }
    
}