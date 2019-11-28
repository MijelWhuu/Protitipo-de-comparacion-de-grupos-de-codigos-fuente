package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoAutoSriaAcuerdosEntity {

    private String id_tipo_auto_sria_acuerdos;
    private String nombre;
    private Integer dias;
    private String observaciones;
    private Boolean estatus = true;
    private TipoNotificacionEntity noti;
    
    public TipoAutoSriaAcuerdosEntity() {
        this.id_tipo_auto_sria_acuerdos = "-1";
        this.nombre = "[Elija una opción]";
    }

    public TipoAutoSriaAcuerdosEntity(String id_tipo_auto_sria_acuerdos) {
        this.id_tipo_auto_sria_acuerdos = id_tipo_auto_sria_acuerdos;
    }

    public TipoAutoSriaAcuerdosEntity(String id_tipo_auto_sria_acuerdos, String nombre) {
        this.id_tipo_auto_sria_acuerdos = id_tipo_auto_sria_acuerdos;
        this.nombre = nombre;
    }

    public TipoAutoSriaAcuerdosEntity(String nombre, Integer dias, String observaciones) {
        this.nombre = nombre;
        this.dias = dias;
        this.observaciones = observaciones;
    }
    
    public String getId_tipo_auto_sria_acuerdos() {
        return id_tipo_auto_sria_acuerdos;
    }

    public void setId_tipo_auto_sria_acuerdos(String id_tipo_auto_sria_acuerdos) {
        this.id_tipo_auto_sria_acuerdos = id_tipo_auto_sria_acuerdos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
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
    
    public TipoNotificacionEntity getNoti() {
        return noti;
    }

    public void setNoti(TipoNotificacionEntity noti) {
        this.noti = noti;
    }
    
    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id_tipo_auto_sria_acuerdos);
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
        final TipoAutoSriaAcuerdosEntity other = (TipoAutoSriaAcuerdosEntity) obj;
        if (!Objects.equals(this.id_tipo_auto_sria_acuerdos, other.id_tipo_auto_sria_acuerdos)) {
            return false;
        }
        return true;
    }
    
}
