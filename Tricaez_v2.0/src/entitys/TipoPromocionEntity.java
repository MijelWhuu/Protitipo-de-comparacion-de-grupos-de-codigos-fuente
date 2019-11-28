package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoPromocionEntity {

    private String id_tipo_promocion;
    private String nombre;
    private String observaciones;
    private Boolean seleccionar = false;
    
    public TipoPromocionEntity() {
        this.id_tipo_promocion = "-1";
        this.nombre = "[Elija una opción]";
    }

    public TipoPromocionEntity(String id_tipo_promocion) {
        this.id_tipo_promocion = id_tipo_promocion;
    }

    public TipoPromocionEntity(String nombre, String observaciones) {
        this.nombre = nombre;
        this.observaciones = observaciones;
    }

    public TipoPromocionEntity(String id_tipo_promocion, String nombre, String observaciones) {
        this.id_tipo_promocion = id_tipo_promocion;
        this.nombre = nombre;
        this.observaciones = observaciones;
    }
    
    public TipoPromocionEntity(String nombre, String observaciones, Boolean seleccionar) {
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }

    public TipoPromocionEntity(String id_tipo_promocion, String nombre, String observaciones, Boolean seleccionar) {
        this.id_tipo_promocion = id_tipo_promocion;
        this.nombre = nombre;
        this.observaciones = observaciones;
        this.seleccionar = seleccionar;
    }
    
    public String getId_tipo_promocion() {
        return id_tipo_promocion;
    }

    public void setId_tipo_promocion(String id_tipo_promocion) {
        this.id_tipo_promocion = id_tipo_promocion;
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
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id_tipo_promocion);
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
        final TipoPromocionEntity other = (TipoPromocionEntity) obj;
        if (!Objects.equals(this.id_tipo_promocion, other.id_tipo_promocion)) {
            return false;
        }
        return true;
    }
    
}
