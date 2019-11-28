package entitys;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class PromocionEntity {

    private String id_promocion;
    private ExpedienteEntity expe;
    private String expediente;
    private Integer folio;
    private Integer anio;
    private TipoPromocionEntity tipoPromocion;
    private DestinoEntity destino;
    private String fechaR;
    private String horaR;
    private Integer aOriginales;
    private Integer aCopias;
    private Boolean acreditacion;
    private Integer hojas;
    private String observaciones;
    
    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String Expediente) {
        this.expediente = Expediente;
    }   
    
    public PromocionEntity() {
    }

    public PromocionEntity(String id_promocion) {
        this.id_promocion = id_promocion;
    }

    public String getId_promocion() {
        return id_promocion;
    }

    public void setId_promocion(String id_promocion) {
        this.id_promocion = id_promocion;
    }

    public ExpedienteEntity getExpe() {
        return expe;
    }

    public void setExpe(ExpedienteEntity expe) {
        this.expe = expe;
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

    public TipoPromocionEntity getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion(TipoPromocionEntity tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }

    public DestinoEntity getDestino() {
        return destino;
    }

    public void setDestino(DestinoEntity destino) {
        this.destino = destino;
    }

    public String getFechaR() {
        return fechaR;
    }

    public void setFechaR(String fechaR) {
        this.fechaR = fechaR;
    }

    public String getHoraR() {
        return horaR;
    }

    public void setHoraR(String horaR) {
        this.horaR = horaR;
    }

    public Integer getaOriginales() {
        return aOriginales;
    }

    public void setaOriginales(Integer aOriginales) {
        this.aOriginales = aOriginales;
    }

    public Integer getaCopias() {
        return aCopias;
    }

    public void setaCopias(Integer aCopias) {
        this.aCopias = aCopias;
    }

    public Boolean getAcreditacion() {
        return acreditacion;
    }

    public void setAcreditacion(Boolean acreditacion) {
        this.acreditacion = acreditacion;
    }

    public Integer getHojas() {
        return hojas;
    }

    public void setHojas(Integer hojas) {
        this.hojas = hojas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    @Override
    public String toString() {
        return tipoPromocion.getNombre();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id_promocion);
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
        final PromocionEntity other = (PromocionEntity) obj;
        if (!Objects.equals(this.id_promocion, other.id_promocion)) {
            return false;
        }
        return true;
    }
    
}
