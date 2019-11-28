package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class SuspensionEntity {

    private String id_suspension;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = false;
    
    public SuspensionEntity() {
        this.id_suspension = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public SuspensionEntity(String id_suspension) {
        this.id_suspension = id_suspension;
    }

    public SuspensionEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public SuspensionEntity(String id_suspension, String nombre, String observaciones) {
        this.id_suspension = id_suspension;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public SuspensionEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public SuspensionEntity(String id_suspension, String nombre, String observaciones, Boolean seleccionar) {
        this.id_suspension = id_suspension;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_suspension() {
        return id_suspension;
    }

    public void setId_suspension(String id_suspension) {
        this.id_suspension = id_suspension;
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
        hash = 79 * hash + Objects.hashCode(this.id_suspension);
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
        final SuspensionEntity other = (SuspensionEntity) obj;
        if (!Objects.equals(this.id_suspension, other.id_suspension)) {
            return false;
        }
        return true;
    }

}
