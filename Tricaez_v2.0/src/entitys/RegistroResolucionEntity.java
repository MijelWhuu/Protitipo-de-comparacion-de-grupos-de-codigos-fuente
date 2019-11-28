package entitys;

import java.util.Date;

/**
 *
 * @author mavg
 */
public class RegistroResolucionEntity {

    private RegistroAutoEntity auto;
    private String id_reg_resolucion;
    
    private String fecha_resolucion;
    private String fecha_acuerdo;
    //private Date fecha_resolucion;
    //private Date fecha_acuerdo;
    
    private Boolean actuaria;
    private SentidoEjecutoriaEntity sentido;
    private Boolean archivar;
    private String motivo;
    private ExpedienteEntity exp;
    private String id_tramite;
    private ExpedienteTramiteEntity tramite;
    private Integer folio;
    private Integer anio;
    
    public RegistroResolucionEntity() {
    }

    public RegistroAutoEntity getAuto() {
        return auto;
    }

    public void setAuto(RegistroAutoEntity auto) {
        this.auto = auto;
    }

    public String getId_reg_resolucion() {
        return id_reg_resolucion;
    }

    public void setId_reg_resolucion(String id_reg_resolucion) {
        this.id_reg_resolucion = id_reg_resolucion;
    }

    public Boolean getActuaria() {
        return actuaria;
    }

    public void setActuaria(Boolean actuaria) {
        this.actuaria = actuaria;
    }

    public SentidoEjecutoriaEntity getSentido() {
        return sentido;
    }

    public void setSentido(SentidoEjecutoriaEntity sentido) {
        this.sentido = sentido;
    }

    public Boolean getArchivar() {
        return archivar;
    }

    public void setArchivar(Boolean archivar) {
        this.archivar = archivar;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public ExpedienteEntity getExp() {
        return exp;
    }

    public void setExp(ExpedienteEntity exp) {
        this.exp = exp;
    }

    public String getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(String id_tramite) {
        this.id_tramite = id_tramite;
    }

    public ExpedienteTramiteEntity getTramite() {
        return tramite;
    }

    public void setTramite(ExpedienteTramiteEntity tramite) {
        this.tramite = tramite;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    
    public String getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(String fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public String getFecha_acuerdo() {
        return fecha_acuerdo;
    }

    public void setFecha_acuerdo(String fecha_acuerdo) {
        this.fecha_acuerdo = fecha_acuerdo;
    }
}
