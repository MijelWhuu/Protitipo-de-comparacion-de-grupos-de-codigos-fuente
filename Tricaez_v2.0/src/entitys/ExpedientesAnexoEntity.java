package entitys;

import java.util.Date;

/**
 *
 * @author mavg
 */
public class ExpedientesAnexoEntity {

    private String principal;
    private String secundario;
    private Date fecha;
    
    public ExpedientesAnexoEntity() {
    }

    public ExpedientesAnexoEntity(String principal, String secundario, Date fecha) {
        this.principal = principal;
        this.secundario = secundario;
        this.fecha = fecha;
    }
    
    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getSecundario() {
        return secundario;
    }

    public void setSecundario(String secundario) {
        this.secundario = secundario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
