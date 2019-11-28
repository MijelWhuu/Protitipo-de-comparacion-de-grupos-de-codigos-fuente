package entitys;

import java.util.Date;

/**
 *
 * @author mavg
 */
public class CargaNotificacionEntity {

    private String id_carga;
    private RegistroAutoEntity auto;
    private String tipo_notificacion;
    private String oficio;
    private Date fecha_recibida;
    private String Observaciones;
    private String id_parte;
    private int tipo_parte;
    private String nombre;
    private Boolean correo;
    private Boolean lista;
    private MunicipioEntity mun;
    private DescargaNotificacionEntity descarga;
    
    public CargaNotificacionEntity() {
    }

    public String getId_carga() {
        return id_carga;
    }

    public void setId_carga(String id_carga) {
        this.id_carga = id_carga;
    }

    public RegistroAutoEntity getAuto() {
        return auto;
    }

    public void setAuto(RegistroAutoEntity auto) {
        this.auto = auto;
    }

    public String getTipo_notificacion() {
        return tipo_notificacion;
    }

    public void setTipo_notificacion(String tipo_notificacion) {
        this.tipo_notificacion = tipo_notificacion;
    }

    public String getOficio() {
        return oficio;
    }

    public void setOficio(String oficio) {
        this.oficio = oficio;
    }

    public Date getFecha_recibida() {
        return fecha_recibida;
    }

    public void setFecha_recibida(Date fecha_recibida) {
        this.fecha_recibida = fecha_recibida;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }

    public String getId_parte() {
        return id_parte;
    }

    public void setId_parte(String id_parte) {
        this.id_parte = id_parte;
    }

    public int getTipo_parte() {
        return tipo_parte;
    }

    public void setTipo_parte(int tipo_parte) {
        this.tipo_parte = tipo_parte;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getCorreo() {
        return correo;
    }

    public void setCorreo(Boolean correo) {
        this.correo = correo;
    }

    public Boolean getLista() {
        return lista;
    }

    public void setLista(Boolean lista) {
        this.lista = lista;
    }

    public MunicipioEntity getMun() {
        return mun;
    }

    public void setMun(MunicipioEntity mun) {
        this.mun = mun;
    }

    public DescargaNotificacionEntity getDescarga() {
        return descarga;
    }

    public void setDescarga(DescargaNotificacionEntity descarga) {
        this.descarga = descarga;
    }
    
}
