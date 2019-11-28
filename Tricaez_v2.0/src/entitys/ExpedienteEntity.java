package entitys;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class ExpedienteEntity {

    private String expediente;
    private Date fecha;
    private ProcedimientoEntity tipoProcedimiento;
    private ActoEntity tipoActo;
    private TipoPretensionEntity tipoPretension;
    private Double cantidad;
    private String observaciones;
    private String caja;
    private Date fechaArchivo;
    private String numero;
        
    public ExpedienteEntity() {
    }

    public ExpedienteEntity(String expediente) {
        this.expediente = expediente;
    }
    
    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public ProcedimientoEntity getTipoProcedimiento() {
        return tipoProcedimiento;
    }

    public void setTipoProcedimiento(ProcedimientoEntity tipoProcedimiento) {
        this.tipoProcedimiento = tipoProcedimiento;
    }

    public ActoEntity getTipoActo() {
        return tipoActo;
    }

    public void setTipoActo(ActoEntity tipoActo) {
        this.tipoActo = tipoActo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public TipoPretensionEntity getTipoPretension() {
        return tipoPretension;
    }

    public void setTipoPretension(TipoPretensionEntity tipoPretension) {
        this.tipoPretension = tipoPretension;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public Date getFechaArchivo() {
        return fechaArchivo;
    }

    public void setFechaArchivo(Date fechaArchivo) {
        this.fechaArchivo = fechaArchivo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.expediente);
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
        final ExpedienteEntity other = (ExpedienteEntity) obj;
        if (!Objects.equals(this.expediente, other.expediente)) {
            return false;
        }
        return true;
    }

}
