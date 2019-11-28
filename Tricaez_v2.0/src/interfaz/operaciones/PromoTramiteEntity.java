package interfaz.operaciones;

import entitys.PromocionEntity;
import entitys.TramiteEntity;

/**
 *
 * @author mavg
 */
public class PromoTramiteEntity {
    
    private PromocionEntity pro;
    private TramiteEntity tra;

    public PromoTramiteEntity() {
    }
    
    public PromoTramiteEntity(PromocionEntity pro, TramiteEntity tra) {
        this.pro = pro;
        this.tra = tra;
    }

    public PromocionEntity getPro() {
        return pro;
    }

    public void setPro(PromocionEntity pro) {
        this.pro = pro;
    }

    public TramiteEntity getTra() {
        return tra;
    }

    public void setTra(TramiteEntity tra) {
        this.tra = tra;
    }

    @Override
    public String toString() {
        if(pro==null)
            return "";
        else{
            if(tra!=null)
                return "" + pro.getFolio() + "  -  " + pro.getAnio() + "  -  " + pro.getTipoPromocion().getNombre() + "  /  " + tra.getTramite().getNombre();
            else
                 return "" + pro.getFolio() + "  -  " + pro.getAnio() + "  -  " + pro.getTipoPromocion().getNombre();
        }
    }
 
}
