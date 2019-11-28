package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class TipoTramiteEntity {

    private String id_tipo_amparo;
    private String nombre;
    private String persona;
    private String autoridad;
    private String documento;
    private Boolean solicitaPartes;
    private String observaciones;
    private Boolean seleccionar = true;
    
    public TipoTramiteEntity() {
        this.id_tipo_amparo = "-1";
        this.nombre = "[Elija una opción]";
    }
    
    public TipoTramiteEntity(String id_tipo_amparo) {
        this.id_tipo_amparo = id_tipo_amparo;
    }

    public TipoTramiteEntity(String id_tipo_amparo, String nombre) {
        this.id_tipo_amparo = id_tipo_amparo;
        this.nombre = nombre;
    }

    public TipoTramiteEntity(String id_tipo_amparo, String nombre, Boolean solicitaPartes) {
        this.id_tipo_amparo = id_tipo_amparo;
        this.nombre = nombre;
        this.solicitaPartes = solicitaPartes;
    }
    
    public String getId_tipo_amparo() {
        return id_tipo_amparo;
    }

    public void setId_tipo_amparo(String id_tipo_amparo) {
        this.id_tipo_amparo = id_tipo_amparo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getAutoridad() {
        return autoridad;
    }

    public void setAutoridad(String autoridad) {
        this.autoridad = autoridad;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Boolean getSolicitaPartes() {
        return solicitaPartes;
    }

    public void setSolicitaPartes(Boolean solicitaPartes) {
        this.solicitaPartes = solicitaPartes;
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
        hash = 89 * hash + Objects.hashCode(this.id_tipo_amparo);
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
        final TipoTramiteEntity other = (TipoTramiteEntity) obj;
        if (!Objects.equals(this.id_tipo_amparo, other.id_tipo_amparo)) {
            return false;
        }
        return true;
    }
    
}
