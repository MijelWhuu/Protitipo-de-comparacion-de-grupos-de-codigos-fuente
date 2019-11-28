package bl.reportes;

/**
 *
 * @author mavg
 */
public class buscarPersonaEntity {

    private String nombre;
    private String expediente;

    public buscarPersonaEntity() {
    }

    public buscarPersonaEntity(String nombre, String expediente) {
        this.nombre = nombre;
        this.expediente = expediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    @Override
    public String toString() {
        return expediente + "  -  " + nombre;
    }
    
}
