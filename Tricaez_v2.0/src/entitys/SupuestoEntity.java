package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class SupuestoEntity {

    private String id_supuesto;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = true;
    
    public SupuestoEntity() {
        this.id_supuesto = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public SupuestoEntity(String id_supuesto) {
        this.id_supuesto = id_supuesto;
    }

    public SupuestoEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public SupuestoEntity(String id_supuesto, String nombre, String observaciones) {
        this.id_supuesto = id_supuesto;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public SupuestoEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public SupuestoEntity(String id_supuesto, String nombre, String observaciones, Boolean seleccionar) {
        this.id_supuesto = id_supuesto;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_supuesto() {
        return id_supuesto;
    }

    public void setId_supuesto(String id_supuesto) {
        this.id_supuesto = id_supuesto;
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
        hash = 17 * hash + Objects.hashCode(this.id_supuesto);
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
        final SupuestoEntity other = (SupuestoEntity) obj;
        if (!Objects.equals(this.id_supuesto, other.id_supuesto)) {
            return false;
        }
        return true;
    }

}
