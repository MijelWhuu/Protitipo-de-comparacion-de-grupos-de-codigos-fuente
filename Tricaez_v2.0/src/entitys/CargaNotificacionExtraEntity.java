package entitys;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class CargaNotificacionExtraEntity {

    private String id_carga_extra;
    private String oficio;
    private Integer anio;
    private ExpedienteEntity expe;
    private AutoridadNotificacionEntity auto;
    private String tipo_notificacion;
    private String observaciones;
    private Date fecha_recibida;
    private String mensaje;

    public CargaNotificacionExtraEntity() {
        this.id_carga_extra = "-1";
        this.mensaje = "[Elija una opción]";
    }

    public CargaNotificacionExtraEntity(String id_carga_extra) {
        this.id_carga_extra = id_carga_extra;
    }
    
    public String getId_carga_extra() {
        return id_carga_extra;
    }

    public void setId_carga_extra(String id_carga_extra) {
        this.id_carga_extra = id_carga_extra;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public ExpedienteEntity getExpe() {
        return expe;
    }

    public void setExpe(ExpedienteEntity expe) {
        this.expe = expe;
    }

    public AutoridadNotificacionEntity getAuto() {
        return auto;
    }

    public void setAuto(AutoridadNotificacionEntity auto) {
        this.auto = auto;
    }

    public String getTipo_notificacion() {
        return tipo_notificacion;
    }

    public void setTipo_notificacion(String tipo_notificacion) {
        this.tipo_notificacion = tipo_notificacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha_recibida() {
        return fecha_recibida;
    }

    public void setFecha_recibida(Date fecha_recibida) {
        this.fecha_recibida = fecha_recibida;
    }
    
    @Override
    public String toString() {
        if(!id_carga_extra.equals("-1"))
            mensaje = this.getExpe().getExpediente() + "  -  " + this.oficio + "  -  " + this.getAuto().getNombre();
        return mensaje;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id_carga_extra);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CargaNotificacionExtraEntity other = (CargaNotificacionExtraEntity) obj;
        if (!Objects.equals(this.id_carga_extra, other.id_carga_extra)) {
            return false;
        }
        return true;
    }

    
}
