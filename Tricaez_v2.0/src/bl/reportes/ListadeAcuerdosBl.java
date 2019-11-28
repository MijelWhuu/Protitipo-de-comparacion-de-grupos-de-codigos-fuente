package bl.reportes;

import baseSistema.ConexionMySql;
import java.io.File;
import java.sql.Connection;
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
public class ListadeAcuerdosBl {

    public ListadeAcuerdosBl() {}

    public Boolean reporteToPDF(String archivo, String ubicacion, Date fecha, String fechaTxt){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pFecha", fechaTxt);
            params.put("fecha", fecha);
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
    
    public void reporteVisualizar(String archivo, Date fecha, String fechaTxt){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pFecha", fechaTxt);
            params.put("fecha", fecha);
            //params.put("SUBREPORT_DIR", "src/jasper/");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimir(String archivo, Date fecha, String fechaTxt){
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("pFecha", fechaTxt);
            params.put("fecha", fecha);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}