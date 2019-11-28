package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class DatoLaEntity {

    private String id_dato_la;
    private String cargo;
    private String nombre;
    private String observaciones;
    private Boolean estatus = true;
    
    public DatoLaEntity() {
        this.id_dato_la = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public DatoLaEntity(String id_dato_la) {
        this.id_dato_la = id_dato_la;
    }

    public DatoLaEntity(String cargo, String nombre, String observaciones) {
        this.cargo = cargo;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public DatoLaEntity(String id_dato_la, String cargo, String nombre, String observaciones) {
        this.id_dato_la = id_dato_la;
        this.cargo = cargo;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public String getId_dato_la() {
        return id_dato_la;
    }

    public void setId_dato_la(String id_dato_la) {
        this.id_dato_la = id_dato_la;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
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
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id_dato_la);
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
        final DatoLaEntity other = (DatoLaEntity) obj;
        if (!Objects.equals(this.id_dato_la, other.id_dato_la)) {
            return false;
        }
        return true;
    }
    
}
