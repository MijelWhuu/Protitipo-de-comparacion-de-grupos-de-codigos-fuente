package entitys;

import java.util.Date;

/**
 *
 * @author tricazac
 */
public class ComboFechasCargaEntity {

    private String id;
    private String folio;
    private String anio;
    private Date fecha;
    private String fechaTxt;
    private Integer tipo;

    public ComboFechasCargaEntity() {
        this.folio = "[Elija una Opción]";
        this.anio = "";
        this.fechaTxt = "";
        this.id = "-1";
        this.tipo = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFechaTxt() {
        return fechaTxt;
    }

    public void setFechaTxt(String fechaTxt) {
        this.fechaTxt = fechaTxt;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return folio + " - " + anio + "  /  " + fechaTxt;
    }
    
    
}
