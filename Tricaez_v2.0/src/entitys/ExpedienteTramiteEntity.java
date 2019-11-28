package entitys;

import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class ExpedienteTramiteEntity {

    private String id_inicio_tramite;
    private String id_tipo_amparo;
    private Date fecha;
    private String razon;
    private String expediente;
    private String nombre;

    public ExpedienteTramiteEntity() {
        this.expediente = "[Elija una opción]";
        this.id_inicio_tramite = "-1";
        this.nombre = "";
    }

    public ExpedienteTramiteEntity(String id_inicio_tramite) {
        this.id_inicio_tramite = id_inicio_tramite;
    }
    
    public String getId_inicio_tramite() {
        return id_inicio_tramite;
    }

    public void setId_inicio_tramite(String id_inicio_tramite) {
        this.id_inicio_tramite = id_inicio_tramite;
    }

    public String getId_tipo_amparo() {
        return id_tipo_amparo;
    }

    public void setId_tipo_amparo(String id_tipo_amparo) {
        this.id_tipo_amparo = id_tipo_amparo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return expediente + "  -  " + nombre;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id_inicio_tramite);
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
        final ExpedienteTramiteEntity other = (ExpedienteTramiteEntity) obj;
        if (!Objects.equals(this.id_inicio_tramite, other.id_inicio_tramite)) {
            return false;
        }
        return true;
    }
    
    
    
}
