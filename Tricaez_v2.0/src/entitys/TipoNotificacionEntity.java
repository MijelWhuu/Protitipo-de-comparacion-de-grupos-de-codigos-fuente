package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoNotificacionEntity {

    private String id_tipo_notificacion;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = false;
    
    public TipoNotificacionEntity() {
        this.id_tipo_notificacion = "-1";
        this.nombre = "[Elija una opción]";
    }

    public TipoNotificacionEntity(String id_tipo_notificacion) {
        this.id_tipo_notificacion = id_tipo_notificacion;
    }

    public TipoNotificacionEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public TipoNotificacionEntity(String id_tipo_notificacion, String nombre, String observaciones) {
        this.id_tipo_notificacion = id_tipo_notificacion;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public TipoNotificacionEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public TipoNotificacionEntity(String id_tipo_notificacion, String nombre, String observaciones, Boolean seleccionar) {
        this.id_tipo_notificacion = id_tipo_notificacion;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_tipo_notificacion() {
        return id_tipo_notificacion;
    }

    public void setId_tipo_notificacion(String id_tipo_notificacion) {
        this.id_tipo_notificacion = id_tipo_notificacion;
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
        hash = 19 * hash + Objects.hashCode(this.id_tipo_notificacion);
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
        final TipoNotificacionEntity other = (TipoNotificacionEntity) obj;
        if (!Objects.equals(this.id_tipo_notificacion, other.id_tipo_notificacion)) {
            return false;
        }
        return true;
    }
    
}
