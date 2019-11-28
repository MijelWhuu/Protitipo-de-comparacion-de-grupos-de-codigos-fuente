package entitys;

/**
 *
 * @author mavg
 */
public class ComboAutoEntity {

    private String id_auto;
    private String folio;
    private String anio;

    public ComboAutoEntity() {
    }

    public ComboAutoEntity(String id_auto, String folio, String anio) {
        this.id_auto = id_auto;
        this.folio = folio;
        this.anio = anio;
    }

    public String getId_auto() {
        return id_auto;
    }

    public void setId_auto(String id_auto) {
        this.id_auto = id_auto;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return folio + " - " + anio;
    }
    
    
}
