package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoPretensionEntity {

    private String id_tipo_pretension;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = false;
    
    public TipoPretensionEntity() {
        this.id_tipo_pretension = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public TipoPretensionEntity(String id_tipo_pretension) {
        this.id_tipo_pretension = id_tipo_pretension;
    }

    public TipoPretensionEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public TipoPretensionEntity(String id_tipo_pretension, String nombre, String observaciones) {
        this.id_tipo_pretension = id_tipo_pretension;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public TipoPretensionEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public TipoPretensionEntity(String id_tipo_pretension, String nombre, String observaciones, Boolean seleccionar) {
        this.id_tipo_pretension = id_tipo_pretension;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_tipo_pretension() {
        return id_tipo_pretension;
    }

    public void setId_tipo_pretension(String id_tipo_pretension) {
        this.id_tipo_pretension = id_tipo_pretension;
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
    
    public Boolean getSeleccionar() {
        return seleccionar;
    }

    public void setSeleccionar(Boolean seleccionar) {
        this.seleccionar = seleccionar;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id_tipo_pretension);
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
        final TipoPretensionEntity other = (TipoPretensionEntity) obj;
        if (!Objects.equals(this.id_tipo_pretension, other.id_tipo_pretension)) {
            return false;
        }
        return true;
    }
    
}
