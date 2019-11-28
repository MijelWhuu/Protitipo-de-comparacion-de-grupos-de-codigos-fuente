package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoAutoMesaTramiteEntity {

    private String id_tipo_auto_mesa_tramite;
    private String nombre;
    private Integer dias;
    private String observaciones;
    private Boolean estatus = true;
    private TipoNotificacionEntity noti;
    
    public TipoAutoMesaTramiteEntity() {
        this.id_tipo_auto_mesa_tramite = "-1";
        this.nombre = "[Elija una opción]";
    }

    public TipoAutoMesaTramiteEntity(String id_tipo_auto_mesa_tramite) {
        this.id_tipo_auto_mesa_tramite = id_tipo_auto_mesa_tramite;
    }

    public TipoAutoMesaTramiteEntity(String id_tipo_auto_mesa_tramite, String nombre) {
        this.id_tipo_auto_mesa_tramite = id_tipo_auto_mesa_tramite;
        this.nombre = nombre;
    }

    public TipoAutoMesaTramiteEntity(String nombre, Integer dias, String observaciones) {
        this.nombre = nombre;
        this.dias = dias;
        this.observaciones = observaciones;
    }
    
    public String getId_tipo_auto_mesa_tramite() {
        return id_tipo_auto_mesa_tramite;
    }

    public void setId_tipo_auto_mesa_tramite(String id_tipo_auto_mesa_tramite) {
        this.id_tipo_auto_mesa_tramite = id_tipo_auto_mesa_tramite;
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
        hash = 97 * hash + Objects.hashCode(this.id_tipo_auto_mesa_tramite);
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
        final TipoAutoMesaTramiteEntity other = (TipoAutoMesaTramiteEntity) obj;
        if (!Objects.equals(this.id_tipo_auto_mesa_tramite, other.id_tipo_auto_mesa_tramite)) {
            return false;
        }
        return true;
    }
    
}
