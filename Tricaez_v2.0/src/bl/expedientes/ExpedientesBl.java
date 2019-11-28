package bl.expedientes;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.expedientes.ExpedienteDao;
import entitys.ActorEntity;
import entitys.PromocionEntity;
import entitys.ActorTerceroEntity;
import entitys.AutoridadEntity;
import entitys.AutorizadoEntity;
import entitys.ExpedienteEntity;
import entitys.PersonaAjenaEntity;
import entitys.SesionEntity;
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
public class ExpedientesBl {

    ExpedienteDao dao = new ExpedienteDao();
    
    public ExpedientesBl() {
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public ArrayList existeActo(String id_acto, String numero){
        return dao.existeActo(id_acto, numero);
    }
    
    public String getNumeroExpediente(){
        return dao.getNumeroExpediente();
    }
    
    public ErrorEntity saveExpediente(ExpedienteEntity expe, SesionEntity sesion, ArrayList actores, ArrayList autoridades, 
            ArrayList terceroAutoridades, ArrayList terceroActores, ArrayList autoridadesAjenas, ArrayList personasAjenas, ArrayList autorizados){
    
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = dao.saveExpediente(con, expe, sesion);
        if(error.getError()) return error;
        
        if(actores != null && !actores.isEmpty()){
            error = saveActores(con,actores);
            if(error.getError()) return error;
        }
        if(autoridades != null && !autoridades.isEmpty()){
            error = saveAutoridades(con,expe.getExpediente(),autoridades);
            if(error.getError()) return error;
        }
        if(terceroAutoridades != null && !terceroAutoridades.isEmpty()){
            error = saveTerceroAutoridades(con,expe.getExpediente(),terceroAutoridades);
            if(error.getError()) return error;
        }
        if(terceroActores != null && !terceroActores.isEmpty()){
            error = saveTercerosActores(con,terceroActores);
            if(error.getError()) return error;
        }
        if(autoridadesAjenas != null && !autoridadesAjenas.isEmpty()){
            error = saveAutoridadAjena(con,expe.getExpediente(),autoridadesAjenas);
            if(error.getError()) return error;
        }
        if(personasAjenas != null && !personasAjenas.isEmpty()){
            error = savePersonaAjena(con,personasAjenas);
            if(error.getError()) return error;
        }
        if(autorizados != null && !autorizados.isEmpty()){
            error = saveAutorizados(con,expe.getExpediente(),autorizados);
            if(error.getError()) return error;
        }
        
        if(conx.doCommit(con)){
            error.setError(false);
            error.setTipo("");
        }else{
            error.setError(true);
            error.setTipo("Error al guardar los datos");
        }
        return error;
        
    }
        
    private ErrorEntity saveActores(Connection con, ArrayList actores){
        ErrorEntity error = new ErrorEntity(false);
        for (Object actor : actores) {
            ActorEntity act = (ActorEntity) actor;                    
            act.setId_actor(dao.getIdActores(con));
            error = dao.saveActor(con, act);
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity saveAutoridades(Connection con, String expediente, ArrayList autoridades){
        ErrorEntity error = new ErrorEntity(false);
        for (Object autoridad : autoridades) {
            AutoridadEntity auto = (AutoridadEntity) autoridad;                    
            error = dao.saveExpedienteAutoridad(con, expediente, auto.getId_autoridad());
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity saveTerceroAutoridades(Connection con, String expediente, ArrayList terceroAutoridades){
        ErrorEntity error = new ErrorEntity(false);
        for (Object autoridad : terceroAutoridades) {
            AutoridadEntity auto = (AutoridadEntity) autoridad;                    
            error = dao.saveExpedienteTerceroAutoridad(con, expediente, auto.getId_autoridad());
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity saveTercerosActores(Connection con, ArrayList terceroActores){
        ErrorEntity error = new ErrorEntity(false);
        for (Object tercero : terceroActores) {
            ActorTerceroEntity actor = (ActorTerceroEntity) tercero; 
            actor.setId_tercero_actor(dao.getIdTerceroActores(con));
            error = dao.saveTerceroActor(con, actor);
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity saveAutorizados(Connection con, String expediente, ArrayList autorizados){
        ErrorEntity error = new ErrorEntity(false);
        for (Object autorizado : autorizados) {             
            error = dao.saveExpedienteAutorizado(con, expediente, (AutorizadoEntity) autorizado);
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity saveAutoridadAjena(Connection con, String expediente, ArrayList autoridadesAjenas){
        ErrorEntity error = new ErrorEntity(false);
        for (Object autoridad : autoridadesAjenas) {
            AutoridadEntity auto = (AutoridadEntity) autoridad;                    
            error = dao.saveExpedienteAutoridaAjena(con, expediente, auto.getId_autoridad());
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity savePersonaAjena(Connection con, ArrayList personasAjenas){
        ErrorEntity error = new ErrorEntity(false);
        for (Object persona : personasAjenas) {
            PersonaAjenaEntity perso = (PersonaAjenaEntity) persona; 
            perso.setId_persona_ajena(dao.getIdPersonaAjena(con));
            error = dao.savePersonaAjena(con, perso);
            if(error.getError())return error;
        }
        return error;
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
