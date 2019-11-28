package entitys;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author mavg
 */
public class RegistroAutoEntity {
    
    private String id_registro_auto;
    private TramiteEntity tramite;
    private ExpedienteEntity exp;
    private Integer folio;
    private Integer anio;
    private AutoEntity tipo_auto;
    private Boolean ordenar;
    
    private String fecha_auto;
    private String fecha_acuerdo;

    
    /*private Date fecha_auto;
    private Date fecha_acuerdo;*/
    private Boolean prueba1;
    private Boolean prueba2;
    private Boolean prueba3;
    private Boolean prueba4;
    private Boolean prueba5;
    private Boolean prueba6;
    private String motivo;
    private SuspensionEntity suspe;
    private String efectos;
    private String observaciones;
    private String id_tramite;
    private String id_promocion;
    private ArrayList protra;

    public String getFecha_auto() {
        return fecha_auto;
    }

    public void setFecha_auto(String fecha_auto) {
        this.fecha_auto = fecha_auto;
    }

    public String getFecha_acuerdo() {
        return fecha_acuerdo;
    }

    public void setFecha_acuerdo(String fecha_acuerdo) {
        this.fecha_acuerdo = fecha_acuerdo;
    }
    
    public RegistroAutoEntity() {
    }

    public RegistroAutoEntity(String id_registro_auto) {
        this.id_registro_auto = id_registro_auto;
    }
    
    public String getId_registro_auto() {
        return id_registro_auto;
    }

    public void setId_registro_auto(String id_registro_auto) {
        this.id_registro_auto = id_registro_auto;
    }

    public TramiteEntity getTramite() {
        return tramite;
    }

    public void setTramite(TramiteEntity tramite) {
        this.tramite = tramite;
    }

    public ExpedienteEntity getExp() {
        return exp;
    }

    public void setExp(ExpedienteEntity exp) {
        this.exp = exp;
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

    public AutoEntity getTipo_auto() {
        return tipo_auto;
    }

    public void setTipo_auto(AutoEntity tipo_auto) {
        this.tipo_auto = tipo_auto;
    }

    public Boolean getOrdenar() {
        return ordenar;
    }

    public void setOrdenar(Boolean ordenar) {
        this.ordenar = ordenar;
    }

    /*public Date getFecha_auto() {
        return fecha_auto;
    }

    public void setFecha_auto(Date fecha_auto) {
        this.fecha_auto = fecha_auto;
    }

    public Date getFecha_acuerdo() {
        return fecha_acuerdo;
    }

    public void setFecha_acuerdo(Date fecha_acuerdo) {
        this.fecha_acuerdo = fecha_acuerdo;
    }*/

    public Boolean getPrueba1() {
        return prueba1;
    }

    public void setPrueba1(Boolean prueba1) {
        this.prueba1 = prueba1;
    }

    public Boolean getPrueba2() {
        return prueba2;
    }

    public void setPrueba2(Boolean prueba2) {
        this.prueba2 = prueba2;
    }

    public Boolean getPrueba3() {
        return prueba3;
    }

    public void setPrueba3(Boolean prueba3) {
        this.prueba3 = prueba3;
    }

    public Boolean getPrueba4() {
        return prueba4;
    }

    public void setPrueba4(Boolean prueba4) {
        this.prueba4 = prueba4;
    }

    public Boolean getPrueba5() {
        return prueba5;
    }

    public void setPrueba5(Boolean prueba5) {
        this.prueba5 = prueba5;
    }

    public Boolean getPrueba6() {
        return prueba6;
    }

    public void setPrueba6(Boolean prueba6) {
        this.prueba6 = prueba6;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public SuspensionEntity getSuspe() {
        return suspe;
    }

    public void setSuspe(SuspensionEntity suspe) {
        this.suspe = suspe;
    }

    public String getEfectos() {
        return efectos;
    }

    public void setEfectos(String efectos) {
        this.efectos = efectos;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getId_tramite() {
        return id_tramite;
    }

    public void setId_tramite(String id_tramite) {
        this.id_tramite = id_tramite;
    }

    public String getId_promocion() {
        return id_promocion;
    }

    public void setId_promocion(String id_promocion) {
        this.id_promocion = id_promocion;
    }

    public ArrayList getProtra() {
        return protra;
    }

    public void setProtra(ArrayList protra) {
        this.protra = protra;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id_registro_auto);
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
        final RegistroAutoEntity other = (RegistroAutoEntity) obj;
        if (!Objects.equals(this.id_registro_auto, other.id_registro_auto)) {
            return false;
        }
        return true;
    }

    
    
}
