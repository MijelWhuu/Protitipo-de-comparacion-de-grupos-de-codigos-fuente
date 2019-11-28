package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class AutorizadoEntity {

    private String id_autorizado;
    private NombreEntity nombre;
    private DomicilioEntity dom;
    private String observaciones;
    private Boolean estatus = true;
    private ExpedienteEntity expe;
    private Integer id_parte;
    
    public AutorizadoEntity() {
        this.id_autorizado = "-1";
        nombre = new NombreEntity();
        this.nombre.setNombre("[Elija una opción]");
        this.nombre.setPaterno("");
        this.nombre.setMaterno("");
    }

    public AutorizadoEntity(String id_autorizado) {
        this.id_autorizado = id_autorizado;
    }

    public AutorizadoEntity(String id_autorizado, NombreEntity nombre) {
        this.id_autorizado = id_autorizado;
        this.nombre = nombre;
    }
    
    public AutorizadoEntity(NombreEntity nombre, DomicilioEntity dom, String observaciones) {
        this.nombre = nombre;
        this.dom = dom;
        this.observaciones = observaciones;
    }
    
    public AutorizadoEntity(String id_autorizado, NombreEntity nombre, DomicilioEntity dom, String observaciones) {
        this.id_autorizado = id_autorizado;
        this.nombre = nombre;
        this.dom = dom;
        this.observaciones = observaciones;
    }

    public String getId_autorizado() {
        return id_autorizado;
    }

    public void setId_autorizado(String id_autorizado) {
        this.id_autorizado = id_autorizado;
    }

    public NombreEntity getNombre() {
        return nombre;
    }

    public void setNombre(NombreEntity nombre) {
        this.nombre = nombre;
    }

    public DomicilioEntity getDom() {
        return dom;
    }

    public void setDom(DomicilioEntity dom) {
        this.dom = dom;
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

    public ExpedienteEntity getExpe() {
        return expe;
    }

    public void setExpe(ExpedienteEntity expe) {
        this.expe = expe;
    }

    public Integer getId_parte() {
        return id_parte;
    }

    public void setId_parte(Integer id_parte) {
        this.id_parte = id_parte;
    }
    
    @Override
    public String toString() {
        return nombre.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.id_autorizado);
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
        final AutorizadoEntity other = (AutorizadoEntity) obj;
        if (!Objects.equals(this.id_autorizado, other.id_autorizado)) {
            return false;
        }
        return true;
    }
    
}
