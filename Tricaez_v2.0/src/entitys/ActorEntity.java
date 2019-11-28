package entitys;

import java.util.Objects;

/**
 *
 * @author mavg
 */
public class ActorEntity {

    private String id_actor;
    private NombreEntity nombre;
    private DomicilioEntity dom;
    private Boolean representante1;
    private String grupo1;
    private Boolean representante2;
    private String grupo2;
    private String observaciones;
    private ExpedienteEntity expe;
    
    public ActorEntity() {
    }

    public ActorEntity(String id_actor) {
        this.id_actor = id_actor;
    }

    public ActorEntity(String id_actor, NombreEntity nombre) {
        this.id_actor = id_actor;
        this.nombre = nombre;
    }
    
    public String getId_actor() {
        return id_actor;
    }

    public void setId_actor(String id_actor) {
        this.id_actor = id_actor;
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

    public Boolean getRepresentante1() {
        return representante1;
    }

    public void setRepresentante1(Boolean representante1) {
        this.representante1 = representante1;
    }

    public String getGrupo1() {
        return grupo1;
    }

    public void setGrupo1(String grupo1) {
        this.grupo1 = grupo1;
    }

    public Boolean getRepresentante2() {
        return representante2;
    }

    public void setRepresentante2(Boolean representante2) {
        this.representante2 = representante2;
    }

    public String getGrupo2() {
        return grupo2;
    }

    public void setGrupo2(String grupo2) {
        this.grupo2 = grupo2;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public ExpedienteEntity getExpe() {
        return expe;
    }

    public void setExpe(ExpedienteEntity expe) {
        this.expe = expe;
    }

    @Override
    public String toString() {
        return nombre.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id_actor);
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
        final ActorEntity other = (ActorEntity) obj;
        if (!Objects.equals(this.id_actor, other.id_actor)) {
            return false;
        }
        return true;
    }
    
}
