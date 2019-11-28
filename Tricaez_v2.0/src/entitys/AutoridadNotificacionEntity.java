package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class AutoridadNotificacionEntity {

    private String id_autoridad;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = true;
    
    public AutoridadNotificacionEntity() {
        this.id_autoridad = "-1";
        this.nombre = "[Elija una opción]";
    }

    public AutoridadNotificacionEntity(String id_autoridad) {
        this.id_autoridad = id_autoridad;
    }

    public AutoridadNotificacionEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public AutoridadNotificacionEntity(String id_autoridad, String nombre, String observaciones) {
        this.id_autoridad = id_autoridad;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public AutoridadNotificacionEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public AutoridadNotificacionEntity(String id_autoridad, String nombre, String observaciones, Boolean seleccionar) {
        this.id_autoridad = id_autoridad;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }
    
    public String getId_autoridad() {
        return id_autoridad;
    }

    public void setId_autoridad(String id_autoridad) {
        this.id_autoridad = id_autoridad;
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
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.id_autoridad);
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
        final AutoridadNotificacionEntity other = (AutoridadNotificacionEntity) obj;
        if (!Objects.equals(this.id_autoridad, other.id_autoridad)) {
            return false;
        }
        return true;
    }

}
