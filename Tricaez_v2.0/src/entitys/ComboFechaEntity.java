package entitys;

import java.util.Date;

/**
 *
 * @author mavg
 */
public class ComboFechaEntity {

    private String fechaTxt;
    private Date fecha;

    public ComboFechaEntity() {
    }

    public ComboFechaEntity(String fechaTxt, Date fecha) {
        this.fechaTxt = fechaTxt;
        this.fecha = fecha;
    }

    public String getFechaTxt() {
        return fechaTxt;
    }

    public void setFechaTxt(String fechaTxt) {
        this.fechaTxt = fechaTxt;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return fechaTxt;
    }
    
    
    
}
