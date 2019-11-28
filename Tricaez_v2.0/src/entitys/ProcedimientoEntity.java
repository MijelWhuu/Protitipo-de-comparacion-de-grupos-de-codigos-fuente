package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class ProcedimientoEntity {

    private String id_procedimientos;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = false;
    
    public ProcedimientoEntity() {
        this.id_procedimientos = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public ProcedimientoEntity(String id_procedimientos) {
        this.id_procedimientos = id_procedimientos;
    }

    public ProcedimientoEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public ProcedimientoEntity(String id_procedimientos, String nombre, String observaciones) {
        this.id_procedimientos = id_procedimientos;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public ProcedimientoEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public ProcedimientoEntity(String id_procedimientos, String nombre, String observaciones, Boolean seleccionar) {
        this.id_procedimientos = id_procedimientos;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_procedimientos() {
        return id_procedimientos;
    }

    public void setId_procedimientos(String id_procedimientos) {
        this.id_procedimientos = id_procedimientos;
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
        hash = 71 * hash + Objects.hashCode(this.id_procedimientos);
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
        final ProcedimientoEntity other = (ProcedimientoEntity) obj;
        if (!Objects.equals(this.id_procedimientos, other.id_procedimientos)) {
            return false;
        }
        return true;
    }
        
}
