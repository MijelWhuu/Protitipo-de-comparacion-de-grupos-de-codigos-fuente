package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoAutoridadEntity {

    private String id_tipo_autoridad;
    private String nombre;
    private Boolean regla;
    private String observaciones;
    private Boolean estatus = true;
    
    public TipoAutoridadEntity() {
        this.id_tipo_autoridad = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public TipoAutoridadEntity(String id_tipo_autoridad) {
        this.id_tipo_autoridad = id_tipo_autoridad;
    }

    public TipoAutoridadEntity(String nombre, Boolean regla, String observaciones) {
        this.nombre = nombre;
        this.regla = regla;
        this.observaciones = observaciones;
    }

    public TipoAutoridadEntity(String id_tipo_autoridad, String nombre, Boolean regla, String observaciones) {
        this.id_tipo_autoridad = id_tipo_autoridad;
        this.nombre = nombre;
        this.regla = regla;
        this.observaciones = observaciones;
    }

    public String getId_tipo_autoridad() {
        return id_tipo_autoridad;
    }

    public void setId_tipo_autoridad(String id_tipo_autoridad) {
        this.id_tipo_autoridad = id_tipo_autoridad;
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
    
    public Boolean getRegla() {
        return regla;
    }

    public void setRegla(Boolean regla) {
        this.regla = regla;
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
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id_tipo_autoridad);
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
        final TipoAutoridadEntity other = (TipoAutoridadEntity) obj;
        if (!Objects.equals(this.id_tipo_autoridad, other.id_tipo_autoridad)) {
            return false;
        }
        return true;
    }
    
}
