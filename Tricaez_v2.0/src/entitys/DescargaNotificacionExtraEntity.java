package entitys;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class DescargaNotificacionExtraEntity {

    private String id_descarga_extra;
    private String id_carga_extra;
    private String oficio;
    private Integer anio;
    private ExpedienteEntity expe;
    private Date fecha_notificacion;
    private AutoridadNotificacionEntity auto;
    private String tipo_notificacion;
    private String observaciones;
    private Date fecha_deposito;
    private Date fecha_recepcion;
    private String spm;
    private Date fecha_destino;
    private DestinoEntity desti;
    private MunicipioEntity mun;
    
    public DescargaNotificacionExtraEntity() {
        this.id_descarga_extra = "-1";
        this.oficio = "[Elija una opción]";
        this.anio = 0;
    }
    
    public String getId_descarga_extra() {
        return id_descarga_extra;
    }

    public void setId_descarga_extra(String id_descarga_extra) {
        this.id_descarga_extra = id_descarga_extra;
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

    public Date getFecha_notificacion() {
        return fecha_notificacion;
    }

    public void setFecha_notificacion(Date fecha_notificacion) {
        this.fecha_notificacion = fecha_notificacion;
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

    public Date getFecha_deposito() {
        return fecha_deposito;
    }

    public void setFecha_deposito(Date fecha_deposito) {
        this.fecha_deposito = fecha_deposito;
    }

    public Date getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(Date fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public String getSpm() {
        return spm;
    }

    public void setSpm(String spm) {
        this.spm = spm;
    }

    public Date getFecha_destino() {
        return fecha_destino;
    }

    public void setFecha_destino(Date fecha_destino) {
        this.fecha_destino = fecha_destino;
    }

    public DestinoEntity getDesti() {
        return desti;
    }

    public void setDesti(DestinoEntity desti) {
        this.desti = desti;
    }

    public MunicipioEntity getMun() {
        return mun;
    }

    public void setMun(MunicipioEntity mun) {
        this.mun = mun;
    }

    @Override
    public String toString() {
        if(anio==0)
            return oficio;
        return oficio + " - " + anio.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id_descarga_extra);
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
        final DescargaNotificacionExtraEntity other = (DescargaNotificacionExtraEntity) obj;
        if (!Objects.equals(this.id_descarga_extra, other.id_descarga_extra)) {
            return false;
        }
        return true;
    }

}
