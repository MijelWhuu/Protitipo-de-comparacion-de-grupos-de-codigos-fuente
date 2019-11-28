package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class DestinoEntity {

    private String id_destino;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = true;
    
    public DestinoEntity() {
        this.id_destino = "-1";
        this.nombre = "[Elija una opción]";
    }

    public DestinoEntity(String id_destino) {
        this.id_destino = id_destino;
    }

    public DestinoEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public DestinoEntity(String id_destino, String nombre, String observaciones) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public DestinoEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public DestinoEntity(String id_destino, String nombre, String observaciones, Boolean seleccionar) {
        this.id_destino = id_destino;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }
    
    public String getId_destino() {
        return id_destino;
    }

    public void setId_destino(String id_destino) {
        this.id_destino = id_destino;
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
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id_destino);
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
        final DestinoEntity other = (DestinoEntity) obj;
        if (!Objects.equals(this.id_destino, other.id_destino)) {
            return false;
        }
        return true;
    }
    
    
    
}
