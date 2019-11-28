package entitys;

/**
 *
 * @author mavg
 */
public class ActorTerceroEntity {

    
    private String id_tercero_actor;
    private NombreEntity nombre;
    private DomicilioEntity dom;
    private String observaciones;
    private ExpedienteEntity expe;
    
    public ActorTerceroEntity() {
    }

    public ActorTerceroEntity(String id_tercero_actor) {
        this.id_tercero_actor = id_tercero_actor;
    }

    public ActorTerceroEntity(String id_tercero_actor, NombreEntity nombre) {
        this.id_tercero_actor = id_tercero_actor;
        this.nombre = nombre;
    }
    
    public String getId_tercero_actor() {
        return id_tercero_actor;
    }

    public void setId_tercero_actor(String id_tercero_actor) {
        this.id_tercero_actor = id_tercero_actor;
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

}
