package entitys;

import java.sql.Date;

/**
 *
 * @author mavg
 */
public class SesionEntity {

    private String id_usuario;
    private String usuario;
    private String pass;
    private NombreEntity nombre;
    private Date fechaActual;
    private Date fechaCero;
    private Date f_alta;
    private Date f_cambio;
    private String pantallas;
    private Boolean estatus;
    
    public SesionEntity() {
        this.id_usuario = "-1";
        this.nombre = new NombreEntity("[Elija una opción]","","");
    }

    public SesionEntity(String id_usuario) {
        this.id_usuario = id_usuario;
    }
    
    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }   

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Date getFechaCero() {
        return fechaCero;
    }

    public void setFechaCero(Date fechaCero) {
        this.fechaCero = fechaCero;
    }

    public Date getF_alta() {
        return f_alta;
    }

    public void setF_alta(Date f_alta) {
        this.f_alta = f_alta;
    }

    public Date getF_cambio() {
        return f_cambio;
    }

    public void setF_cambio(Date f_cambio) {
        this.f_cambio = f_cambio;
    }

    public NombreEntity getNombre() {
        return nombre;
    }

    public void setNombre(NombreEntity nombre) {
        this.nombre = nombre;
    }

    public String getPantallas() {
        return pantallas;
    }

    public void setPantallas(String pantallas) {
        this.pantallas = pantallas;
    }

    public Boolean getEstatus() {
        return estatus;
    }

    public void setEstatus(Boolean estatus) {
        this.estatus = estatus;
    }

    @Override
    public String toString() {
        return this.nombre.toString();
    }
    
}
