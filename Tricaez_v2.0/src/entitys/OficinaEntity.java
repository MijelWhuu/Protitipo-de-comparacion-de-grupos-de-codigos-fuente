package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class OficinaEntity {

    private String id_oficina;
    private String nombre;
    private String observaciones;
    private Boolean estatus = true;
    
    public OficinaEntity() {
        this.id_oficina = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public OficinaEntity(String id_oficina) {
        this.id_oficina = id_oficina;
    }

    public OficinaEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public OficinaEntity(String id_oficina, String nombre, String observaciones) {
        this.id_oficina = id_oficina;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public String getId_oficina() {
        return id_oficina;
    }

    public void setId_oficina(String id_oficina) {
        this.id_oficina = id_oficina;
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
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.id_oficina);
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
        final OficinaEntity other = (OficinaEntity) obj;
        if (!Objects.equals(this.id_oficina, other.id_oficina)) {
            return false;
        }
        return true;
    }
    
}
