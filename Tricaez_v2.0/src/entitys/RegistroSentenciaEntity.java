/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import java.util.Date;

/**
 *
 * @author mavg
 */
public class RegistroSentenciaEntity {
    
    private RegistroAutoEntity auto;
    private ExpedienteEntity exp;
    private String id_reg_sentencia;
    
    private String fecha_audiencia;
    private String fecha_sentencia;
    private String fecha_acuerdo;
    //private Date fecha_audiencia;
    //private Date fecha_sentencia;
    //private Date fecha_acuerdo;
    
    private TipoDocumentoAsrEntity doc;
    private TipoSentenciaEntity tipo;
    private Double cantidad;
    private String observaciones;
    private Integer anio;
    private Integer folio;

    public RegistroSentenciaEntity() {
    }

    public RegistroAutoEntity getAuto() {
        return auto;
    }

    public void setAuto(RegistroAutoEntity auto) {
        this.auto = auto;
    }

    public ExpedienteEntity getExp() {
        return exp;
    }

    public void setExp(ExpedienteEntity exp) {
        this.exp = exp;
    }

    public String getId_reg_sentencia() {
        return id_reg_sentencia;
    }

    public void setId_reg_sentencia(String id_reg_sentencia) {
        this.id_reg_sentencia = id_reg_sentencia;
    }

    public TipoDocumentoAsrEntity getDoc() {
        return doc;
    }

    public void setDoc(TipoDocumentoAsrEntity doc) {
        this.doc = doc;
    }

    public TipoSentenciaEntity getTipo() {
        return tipo;
    }

    public void setTipo(TipoSentenciaEntity tipo) {
        this.tipo = tipo;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }
    
    public String getFecha_audiencia() {
        return fecha_audiencia;
    }

    public void setFecha_audiencia(String fecha_audiencia) {
        this.fecha_audiencia = fecha_audiencia;
    }

    public String getFecha_sentencia() {
        return fecha_sentencia;
    }

    public void setFecha_sentencia(String fecha_sentencia) {
        this.fecha_sentencia = fecha_sentencia;
    }

    public String getFecha_acuerdo() {
        return fecha_acuerdo;
    }

    public void setFecha_acuerdo(String fecha_acuerdo) {
        this.fecha_acuerdo = fecha_acuerdo;
    }
}
