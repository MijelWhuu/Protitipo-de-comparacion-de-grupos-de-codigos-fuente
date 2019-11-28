package entitys;

/**
 *
 * @author mavg
 */
public class RelacionEntity {

    private String id_relacion;
    private String id_registro_autos;
    private String id_reg_sentencia;
    private String id_reg_resolucion;
    private Integer folio;
    private Integer anio;

    public RelacionEntity() {
    }

    public String getId_relacion() {
        return id_relacion;
    }

    public void setId_relacion(String id_relacion) {
        this.id_relacion = id_relacion;
    }

    public String getId_registro_autos() {
        return id_registro_autos;
    }

    public void setId_registro_autos(String id_registro_autos) {
        this.id_registro_autos = id_registro_autos;
    }

    public String getId_reg_sentencia() {
        return id_reg_sentencia;
    }

    public void setId_reg_sentencia(String id_reg_sentencia) {
        this.id_reg_sentencia = id_reg_sentencia;
    }

    public String getId_reg_resolucion() {
        return id_reg_resolucion;
    }

    public void setId_reg_resolucion(String id_reg_resolucion) {
        this.id_reg_resolucion = id_reg_resolucion;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }
    
    
}
