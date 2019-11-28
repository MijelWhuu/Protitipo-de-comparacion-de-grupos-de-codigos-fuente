/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entitys;

import java.util.Date;

/**
 *
 * @author RAULDELHOYO
 */
public class UbicacionEntity {
    
    private String id_ubica_expediente;
    private String id_usuario_origen;
    private String id_usuario_destino;
    private Date fecha;
    
    public UbicacionEntity() {
        this.id_ubica_expediente = "-1";
        this.id_usuario_destino = "[Elija una opción]";
    }
    
    public UbicacionEntity(String id_ubica_expediente, String id_usuario_origen, 
            String id_usuario_destino, Date fecha) {
        this.id_ubica_expediente = id_ubica_expediente;
        this.id_usuario_origen = id_usuario_origen;
        this.id_usuario_destino = id_usuario_destino;
        this.fecha = fecha;
    }

    public String getId_ubica_expediente() {
        return id_ubica_expediente;
    }

    public void setId_ubica_expediente(String id_ubica_expediente) {
        this.id_ubica_expediente = id_ubica_expediente;
    }

    public String getId_usuario_origen() {
        return id_usuario_origen;
    }

    public void setId_usuario_origen(String id_usuario_origen) {
        this.id_usuario_origen = id_usuario_origen;
    }

    public String getId_usuario_destino() {
        return id_usuario_destino;
    }

    public void setId_usuario_destino(String id_usuario_destino) {
        this.id_usuario_destino = id_usuario_destino;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
