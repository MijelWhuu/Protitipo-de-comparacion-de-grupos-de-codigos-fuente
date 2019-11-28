package bl.utilidades;

import baseSistema.ConexionMySql;
import dao.utilidades.BusquedaAutoridadesDao;
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

/**
 *
 * @author mavg
 */
public class BusquedaAutoridadesBl {

    BusquedaAutoridadesDao dao = new BusquedaAutoridadesDao();
    
    public BusquedaAutoridadesBl() {
    }
    
    public ArrayList getAutoridades(String id_autoridad){
        return dao.getAutoridades(id_autoridad);
    }
    
    public Integer existeAutoridad(String id_autoridad){
        return dao.existeAutoridad(id_autoridad);
    }
       
    public Boolean reporteToPDF(String archivo, String ubicacion, String id_autoridad){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("idAutoridad", id_autoridad);
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
    
    public Boolean reporteImprimir(String archivo, String id_autoridad){
        Boolean error;
        try{ 
            Connection con = new ConexionMySql().getConexionBD();
            Map params = new HashMap();
            params.put("idAutoridad", id_autoridad);
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(new File("src/jasper/"+archivo));
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, con);
            JasperPrintManager.printReport(jasperPrint, true);
            error = false;
        }catch(Exception e){
            System.out.println(e.getMessage());
            error = true;
        }
        return error;
    }
    
}