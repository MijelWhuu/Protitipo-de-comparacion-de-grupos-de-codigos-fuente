package entitys;

/**
 *
 * @author mavg
 */
public class PersonaAjenaEntity {

    
    private String id_persona_ajena;
    private NombreEntity nombre;
    private DomicilioEntity dom;
    private String observaciones;
    private ExpedienteEntity expe;
    
    public PersonaAjenaEntity() {
    }

    public PersonaAjenaEntity(String id_persona_ajena) {
        this.id_persona_ajena = id_persona_ajena;
    }

    public PersonaAjenaEntity(String id_persona_ajena, NombreEntity nombre) {
        this.id_persona_ajena = id_persona_ajena;
        this.nombre = nombre;
    }

    public String getId_persona_ajena() {
        return id_persona_ajena;
    }

    public void setId_persona_ajena(String id_persona_ajena) {
        this.id_persona_ajena = id_persona_ajena;
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
