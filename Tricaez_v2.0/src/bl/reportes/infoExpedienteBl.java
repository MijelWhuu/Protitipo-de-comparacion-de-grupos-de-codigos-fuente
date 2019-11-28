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
public class infoExpedienteBl {

    infoExpedienteDao dao = new infoExpedienteDao();
    
    public infoExpedienteBl() {}

    public Boolean reporteToPDF(String archivo, String ubicacion, String expediente){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("expediente", expediente);
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
    
    public void reporteVisualizar(String archivo, String expediente){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("expediente", expediente);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            System.out.println("2");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperViewer.viewReport(jasperPrint, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void reporteImprimir(String archivo, String expediente){
        try{
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("expediente", expediente);
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