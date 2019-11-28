package entitys;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TramiteEntity {

    private String id_inicio_tramite;
    private PromocionEntity promocion;
    private TipoTramiteEntity tramite;
    private Date fecha;
    private String razon;
    private String observaciones;
    private ExpedienteEntity exp;
    
    public TramiteEntity() {
    }

    public TramiteEntity(String id_inicio_tramite) {
        this.id_inicio_tramite = id_inicio_tramite;
    }

    public TramiteEntity(String id_inicio_tramite, PromocionEntity promocion) {
        this.id_inicio_tramite = id_inicio_tramite;
        this.promocion = promocion;
    }
    
    public String getId_inicio_tramite() {
        return id_inicio_tramite;
    }

    public void setId_inicio_tramite(String id_inicio_tramite) {
        this.id_inicio_tramite = id_inicio_tramite;
    }
    
    public PromocionEntity getPromocion() {
        return promocion;
    }

    public void setPromocion(PromocionEntity promocion) {
        this.promocion = promocion;
    }

    public TipoTramiteEntity getTramite() {
        return tramite;
    }

    public void setTramite(TipoTramiteEntity tramite) {
        this.tramite = tramite;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ExpedienteEntity getExp() {
        return exp;
    }

    public void setExp(ExpedienteEntity exp) {
        this.exp = exp;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id_inicio_tramite);
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
        final TramiteEntity other = (TramiteEntity) obj;
        if (!Objects.equals(this.id_inicio_tramite, other.id_inicio_tramite)) {
            return false;
        }
        return true;
    }
    
}
