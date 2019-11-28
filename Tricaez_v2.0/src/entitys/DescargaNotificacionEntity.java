package entitys;

import java.util.Date;

/**
 *
 * @author mavg
 */
public class DescargaNotificacionEntity {

    private String id_descarga;
    private String id_carga;
    private RegistroAutoEntity auto;
    private String id_parte;
    private int tipo_parte;
    private String nombre;
    private String tipo_notificacion;
    private String oficio;
    private MunicipioEntity mun;
    private Date fecha_notificacion;
    private Date fecha_deposito;
    private Date fecha_recepcion;
    private String spm;
    private String Observaciones;
    private Boolean quitar;
    private DestinoEntity desti;
    private Date fecha_destino;
    private String id_relacion;

    public DescargaNotificacionEntity() {
    }

    public String getId_descarga() {
        return id_descarga;
    }

    public void setId_descarga(String id_descarga) {
        this.id_descarga = id_descarga;
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

    public MunicipioEntity getMun() {
        return mun;
    }

    public void setMun(MunicipioEntity mun) {
        this.mun = mun;
    }

    public Date getFecha_notificacion() {
        return fecha_notificacion;
    }

    public void setFecha_notificacion(Date fecha_notificacion) {
        this.fecha_notificacion = fecha_notificacion;
    }

    public Date getFecha_deposito() {
        return fecha_deposito;
    }

    public void setFecha_deposito(Date fecha_deposito) {
        this.fecha_deposito = fecha_deposito;
    }

    public Date getFecha_recepcion() {
        return fecha_recepcion;
    }

    public void setFecha_recepcion(Date fecha_recepcion) {
        this.fecha_recepcion = fecha_recepcion;
    }

    public String getSpm() {
        return spm;
    }

    public void setSpm(String spm) {
        this.spm = spm;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }

    public Boolean getQuitar() {
        return quitar;
    }

    public void setQuitar(Boolean quitar) {
        this.quitar = quitar;
    }

    public DestinoEntity getDesti() {
        return desti;
    }

    public void setDesti(DestinoEntity desti) {
        this.desti = desti;
    }

    public Date getFecha_destino() {
        return fecha_destino;
    }

    public void setFecha_destino(Date fecha_destino) {
        this.fecha_destino = fecha_destino;
    }

    public String getId_relacion() {
        return id_relacion;
    }

    public void setId_relacion(String id_relacion) {
        this.id_relacion = id_relacion;
    }
    
    
}
