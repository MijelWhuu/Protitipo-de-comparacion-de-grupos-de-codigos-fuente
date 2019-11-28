package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class AutoridadEntity {

    private String id_autoridad;
    private TipoAutoridadEntity tipo;
    private MunicipioEntity mun;
    private String nombre;
    private DomicilioEntity dom;
    private String observaciones;
    private Boolean estatus = true;
    private ExpedienteEntity expe;
    
    public AutoridadEntity() {
        this.id_autoridad = "-1";
        this.nombre = "[Elija una opción]";
    }

    public AutoridadEntity(String id_autoridad) {
        this.id_autoridad = id_autoridad;
    }

    public AutoridadEntity(TipoAutoridadEntity tipo, MunicipioEntity mun, String nombre, DomicilioEntity dom, String observaciones) {
        this.tipo = tipo;
        this.mun = mun;
        this.nombre = nombre;
        this.dom = dom;
        this.observaciones = observaciones;
    }

    public AutoridadEntity(String id_autoridad, TipoAutoridadEntity tipo, MunicipioEntity mun, String nombre, DomicilioEntity dom, String observaciones) {
        this.id_autoridad = id_autoridad;
        this.tipo = tipo;
        this.mun = mun;
        this.nombre = nombre;
        this.dom = dom;
        this.observaciones = observaciones;
    }
    
    public String getId_autoridad() {
        return id_autoridad;
    }

    public void setId_autoridad(String id_autoridad) {
        this.id_autoridad = id_autoridad;
    }

    public TipoAutoridadEntity getTipo() {
        return tipo;
    }

    public void setTipo(TipoAutoridadEntity tipo) {
        this.tipo = tipo;
    }

    public MunicipioEntity getMun() {
        return mun;
    }

    public void setMun(MunicipioEntity mun) {
        this.mun = mun;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
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
        
    @Override
    public String toString() {
        return this.nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id_autoridad);
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
        final AutoridadEntity other = (AutoridadEntity) obj;
        if (!Objects.equals(this.id_autoridad, other.id_autoridad)) {
            return false;
        }
        return true;
    }
    
}
