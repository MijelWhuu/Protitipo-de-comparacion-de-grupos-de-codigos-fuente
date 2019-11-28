/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bl.catalogos;

import dao.catalogos.UbicacionDao;
import java.util.ArrayList;

/**
 *
 * @author RAULDELHOYO
 */
public class UbicacionBl {
    
    UbicacionDao dao = new UbicacionDao();
    
    public String existeExpediente(String expe){ 
        return dao.existeExpediente(expe);
    }
    
    public String dameNombre(String id_exp){
        return dao.dameExpediente(id_exp);
    }
    
    public ArrayList CargaCombo(){ 
        return dao.CargaCombo();
    }
    
    public Boolean GuardaUbicacion(String exp, String id, String prestador){
        return dao.GuardaUbicacion(exp, id, prestador);
    }
}
