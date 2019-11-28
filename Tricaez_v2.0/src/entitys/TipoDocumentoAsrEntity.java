package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoDocumentoAsrEntity {

    private String id_tipo_documento_asr;
    private String nombre;
    private String observaciones;
    private Boolean estatus = true;
    
    public TipoDocumentoAsrEntity() {
        this.id_tipo_documento_asr = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public TipoDocumentoAsrEntity(String id_tipo_documento_asr) {
        this.id_tipo_documento_asr = id_tipo_documento_asr;
    }

    public TipoDocumentoAsrEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public TipoDocumentoAsrEntity(String id_tipo_documento_asr, String nombre, String observaciones) {
        this.id_tipo_documento_asr = id_tipo_documento_asr;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public String getId_tipo_documento_asr() {
        return id_tipo_documento_asr;
    }

    public void setId_tipo_documento_asr(String id_tipo_documento_asr) {
        this.id_tipo_documento_asr = id_tipo_documento_asr;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id_tipo_documento_asr);
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
        final TipoDocumentoAsrEntity other = (TipoDocumentoAsrEntity) obj;
        if (!Objects.equals(this.id_tipo_documento_asr, other.id_tipo_documento_asr)) {
            return false;
        }
        return true;
    }
    
}
