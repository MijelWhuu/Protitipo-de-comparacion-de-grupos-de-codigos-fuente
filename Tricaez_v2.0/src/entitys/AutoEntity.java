package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class AutoEntity {

    private String id_tipo_auto;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = true;
    private Boolean tipo = true;
    
    public AutoEntity() {
        this.id_tipo_auto = "-1";
        this.nombre = "[Elija una opción]";
    }

    public AutoEntity(String id_tipo_auto) {
        this.id_tipo_auto = id_tipo_auto;
    }

    public AutoEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public AutoEntity(String id_tipo_auto, String nombre, String observaciones) {
        this.id_tipo_auto = id_tipo_auto;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public AutoEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public AutoEntity(String nombre, String observaciones, Boolean seleccionar, Boolean tipo) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
        this.tipo = tipo;
    }

    public AutoEntity(String id_tipo_auto, String nombre, String observaciones, Boolean seleccionar) {
        this.id_tipo_auto = id_tipo_auto;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }
    
    public AutoEntity(String id_tipo_auto, String nombre, String observaciones, Boolean seleccionar, Boolean tipo) {
        this.id_tipo_auto = id_tipo_auto;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
        this.tipo = tipo;
    }
    
    public String getId_tipo_auto() {
        return id_tipo_auto;
    }

    public void setId_tipo_auto(String id_tipo_auto) {
        this.id_tipo_auto = id_tipo_auto;
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

    public Boolean getTipo() {
        return tipo;
    }

    public void setTipo(Boolean tipo) {
        this.tipo = tipo;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id_tipo_auto);
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
        final AutoEntity other = (AutoEntity) obj;
        if (!Objects.equals(this.id_tipo_auto, other.id_tipo_auto)) {
            return false;
        }
        return true;
    }
    
}
