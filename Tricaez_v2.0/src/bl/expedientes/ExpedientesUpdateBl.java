package bl.expedientes;

import baseGeneral.ErrorEntity;
import baseSistema.ConexionMySql;
import dao.expedientes.ExpedienteUpdateDao;
import entitys.ActorEntity;
import entitys.AutoridadEntity;
import entitys.ExpedienteEntity;
import entitys.ActorTerceroEntity;
import entitys.AutorizadoEntity;
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
public class ExpedientesUpdateBl {

    ExpedienteUpdateDao dao = new ExpedienteUpdateDao();
    
    public ExpedientesUpdateBl() {
    }
    
    public Boolean existeExpediente(String expediente){
        return dao.existeExpediente(expediente);
    }
    
    public ArrayList existeActo(String id_acto, String numero){
        return dao.existeActo(id_acto, numero);
    }
    
    public ErrorEntity updateExpediente(ExpedienteEntity expe, ExpedienteEntity viejo, SesionEntity sesion, ArrayList actores, 
            ArrayList autoridades, ArrayList terceroAutoridades, ArrayList terceroActores, ArrayList autoridadesAjenas, 
            ArrayList personasAjenas, ArrayList autorizados){
    
        ConexionMySql conx = new ConexionMySql();
        Connection con = conx.getConexionBD();
        conx.setAutoCommit(con,false);
        
        ErrorEntity error = deleteTablas(con, viejo.getExpediente());
        if(error.getError()) return error;
        
        error = dao.updateExpediente(con, expe, viejo, sesion);
        if(error.getError()) return error;
        
        if(actores != null && !actores.isEmpty()){
            error = saveActores(con, actores);
            if(error.getError()) return error;
        }
        if(autoridades != null && !autoridades.isEmpty()){
            error = saveAutoridades(con, expe.getExpediente(), autoridades);
            if(error.getError()) return error;
        }
        if(terceroAutoridades != null && !terceroAutoridades.isEmpty()){
            error = saveTerceroAutoridades(con, expe.getExpediente(), terceroAutoridades);
            if(error.getError()) return error;
        }
        if(terceroActores != null && !terceroActores.isEmpty()){
            error = saveTercerosActores(con, terceroActores);
            if(error.getError()) return error;
        }
        if(autoridadesAjenas != null && !autoridadesAjenas.isEmpty()){
            error = saveAutoridadesAjenas(con, expe.getExpediente(), autoridadesAjenas);
            if(error.getError()) return error;
        }
        if(personasAjenas != null && !personasAjenas.isEmpty()){
            error = savePersonasAjenas(con, personasAjenas);
            if(error.getError()) return error;
        }
        if(autorizados != null && !autorizados.isEmpty()){
            error = saveAutorizados(con, expe.getExpediente(), autorizados);
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
            if(act.getId_actor().length()==0 || act.getId_actor()==null){
                act.setId_actor(dao.getIdActores(con));
                error = dao.saveActor(con, act);
            }else{
                error = dao.updateActor(con, act);
            }
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
            ActorTerceroEntity act = (ActorTerceroEntity) tercero;
            if(act.getId_tercero_actor().length()==0 || act.getId_tercero_actor()==null){
                act.setId_tercero_actor(dao.getIdTerceroActores(con));
                error = dao.saveTerceroActor(con, act);
            }else{
                error = dao.updateTerceroActor(con, act);
            }
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity saveAutoridadesAjenas(Connection con, String expediente, ArrayList autoridadesAjenas){
        ErrorEntity error = new ErrorEntity(false);
        for (Object autoridad : autoridadesAjenas) {
            AutoridadEntity auto = (AutoridadEntity) autoridad;
            error = dao.saveExpedienteAutoridadAjena(con, expediente, auto.getId_autoridad());
            if(error.getError())return error;
        }
        return error;
    }
    
    private ErrorEntity savePersonasAjenas(Connection con, ArrayList personasAjenas){
        ErrorEntity error = new ErrorEntity(false);
        for (Object persona : personasAjenas) {
            PersonaAjenaEntity act = (PersonaAjenaEntity) persona;
            if(act.getId_persona_ajena().length()==0 || act.getId_persona_ajena()==null){
                act.setId_persona_ajena(dao.getIdPersonaAjena(con));
                error = dao.savePersonaAjena(con, act);
            }else{
                error = dao.updatePersonaAjena(con, act);
            }
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
    
    public ExpedienteEntity getExpediente(String expediente){
        return dao.getExpediente(expediente);
    }
        
    public ArrayList getActores(String expediente){
        return dao.getActores(expediente);
    }
    
    public ArrayList getAutoridades(String expediente){
        return dao.getAutoridades(expediente);
    }
    
    public ArrayList getAutoridadesTercero(String expediente){
        return dao.getAutoridadesTercero(expediente);
    }
    
    public ArrayList getActoresTercero(String expediente){
        return dao.getActoresTercero(expediente);
    }
    
    public ArrayList getAutorizados(String expediente){
        return dao.getAutorizados(expediente);
    }
    
    public ArrayList getAutoridadesAjenas(String expediente){
        return dao.getAutoridadesAjenas(expediente);
    }
    
    public ArrayList getPersonasAjenas(String expediente){
        return dao.getPersonasAjenas(expediente);
    }
    
    public ErrorEntity deleteTablas(Connection conx, String expediente){
        ErrorEntity error = dao.deleteExpedienteTerceroAutoridad(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteAutoridadAjena(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteAutorizado(conx,expediente);
        if(error.getError()) return error;
        error = dao.deleteExpedienteAutoridad(conx,expediente);
        if(error.getError()) return error;
        error = dao.nullActor(conx, expediente);
        if(error.getError()) return error;
        error = dao.nullPersonaAjena(conx, expediente);
        if(error.getError()) return error;
        return dao.nullTerceroActor(conx, expediente);
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
