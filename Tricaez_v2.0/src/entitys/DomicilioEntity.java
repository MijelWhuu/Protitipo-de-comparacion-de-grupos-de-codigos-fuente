package entitys;

/**
 *
 * @author mavg
 */
public class DomicilioEntity {
    
    private String calle;
    private String num_ext;
    private String num_int;
    private String colonia;
    private String cp;
    private MunicipioEntity mun;
    private String telefono;
    private String email;

    public DomicilioEntity() {
    }

    public DomicilioEntity(String calle, String num_ext, String num_int, String colonia, String cp, MunicipioEntity mun, String telefono, String email) {
        this.calle = calle;
        this.num_ext = num_ext;
        this.num_int = num_int;
        this.colonia = colonia;
        this.cp = cp;
        this.mun = mun;
        this.telefono = telefono;
        this.email = email;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNum_ext() {
        return num_ext;
    }

    public void setNum_ext(String num_ext) {
        this.num_ext = num_ext;
    }

    public String getNum_int() {
        return num_int;
    }

    public void setNum_int(String num_int) {
        this.num_int = num_int;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public MunicipioEntity getMun() {
        return mun;
    }

    public void setId_municipio(MunicipioEntity mun) {
        this.mun = mun;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Calle:" + calle + ", No. Ext.:" + num_ext + ", Colonia:" + colonia + '}';
    }
    
    
}
