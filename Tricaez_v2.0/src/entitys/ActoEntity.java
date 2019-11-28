package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class ActoEntity {

    private String id_acto;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = true;
    
    public ActoEntity() {
        this.id_acto = "-1";
        this.nombre = "[Elija una opción]";
    }

    public ActoEntity(String id_acto) {
        this.id_acto = id_acto;
    }

    public ActoEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public ActoEntity(String id_acto, String nombre, String observaciones) {
        this.id_acto = id_acto;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public ActoEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public ActoEntity(String id_acto, String nombre, String observaciones, Boolean seleccionar) {
        this.id_acto = id_acto;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }
    
    public String getId_acto() {
        return id_acto;
    }

    public void setId_acto(String id_acto) {
        this.id_acto = id_acto;
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
        hash = 79 * hash + Objects.hashCode(this.id_acto);
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
        final ActoEntity other = (ActoEntity) obj;
        if (!Objects.equals(this.id_acto, other.id_acto)) {
            return false;
        }
        return true;
    }
    
}
