package entitys;

import java.util.Date;

/**
 *
 * @author 182446
 */
public class BuscarAutoridadEntity {

    private String expediente;
    private Date fecha; 
    private String procedimiento;
    private String acto;

    public BuscarAutoridadEntity() {
    }

    public BuscarAutoridadEntity(String expediente, Date fecha, String procedimiento, String acto) {
        this.expediente = expediente;
        this.fecha = fecha;
        this.procedimiento = procedimiento;
        this.acto = acto;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        this.procedimiento = procedimiento;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }
    
}
