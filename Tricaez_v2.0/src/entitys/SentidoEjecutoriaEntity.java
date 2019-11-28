package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class SentidoEjecutoriaEntity {

    private String id_sentido_ejecutoria;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = true;
    
    public SentidoEjecutoriaEntity() {
        this.id_sentido_ejecutoria = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public SentidoEjecutoriaEntity(String id_sentido_ejecutoria) {
        this.id_sentido_ejecutoria = id_sentido_ejecutoria;
    }

    public SentidoEjecutoriaEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public SentidoEjecutoriaEntity(String id_sentido_ejecutoria, String nombre, String observaciones) {
        this.id_sentido_ejecutoria = id_sentido_ejecutoria;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public SentidoEjecutoriaEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public SentidoEjecutoriaEntity(String id_sentido_ejecutoria, String nombre, String observaciones, Boolean seleccionar) {
        this.id_sentido_ejecutoria = id_sentido_ejecutoria;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_sentido_ejecutoria() {
        return id_sentido_ejecutoria;
    }

    public void setId_sentido_ejecutoria(String id_sentido_ejecutoria) {
        this.id_sentido_ejecutoria = id_sentido_ejecutoria;
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
        hash = 67 * hash + Objects.hashCode(this.id_sentido_ejecutoria);
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
        final SentidoEjecutoriaEntity other = (SentidoEjecutoriaEntity) obj;
        if (!Objects.equals(this.id_sentido_ejecutoria, other.id_sentido_ejecutoria)) {
            return false;
        }
        return true;
    }
    
}
