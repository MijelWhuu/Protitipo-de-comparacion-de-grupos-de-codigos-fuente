package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoSentenciaEntity {

    private String id_tipo_sentencia;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = false;
    
    public TipoSentenciaEntity() {
        this.id_tipo_sentencia = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public TipoSentenciaEntity(String id_tipo_sentencia) {
        this.id_tipo_sentencia = id_tipo_sentencia;
    }

    public TipoSentenciaEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public TipoSentenciaEntity(String id_tipo_sentencia, String nombre, String observaciones) {
        this.id_tipo_sentencia = id_tipo_sentencia;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public TipoSentenciaEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public TipoSentenciaEntity(String id_tipo_sentencia, String nombre, String observaciones, Boolean seleccionar) {
        this.id_tipo_sentencia = id_tipo_sentencia;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public String getId_tipo_sentencia() {
        return id_tipo_sentencia;
    }

    public void setId_tipo_sentencia(String id_tipo_sentencia) {
        this.id_tipo_sentencia = id_tipo_sentencia;
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
        hash = 83 * hash + Objects.hashCode(this.id_tipo_sentencia);
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
        final TipoSentenciaEntity other = (TipoSentenciaEntity) obj;
        if (!Objects.equals(this.id_tipo_sentencia, other.id_tipo_sentencia)) {
            return false;
        }
        return true;
    }
    
}
