package bl.utilidades;

import baseGeneral.ErrorEntity;
import dao.utilidades.CalendarioOficialDao;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 *
 * @author mavg
 */
public class CalendarioOficialBl {

    CalendarioOficialDao dao = new CalendarioOficialDao();
    
    public CalendarioOficialBl() {
    }

    public ArrayList getDiasInhabiles(int anio, int mes){
        return dao.getDiasInhabiles(anio, mes);
    }
    
    public int getDia(int anio, int mes, int dia){
        return dao.getDia(anio, mes, dia);
    }
    
    public ErrorEntity saveFecha(int anio, int mes, int dia, String usuario, Date fecha){
        return dao.saveFecha(anio, mes, dia, usuario, fecha);
    }
    
    public ErrorEntity deleteFecha(int anio, int mes, int dia){
        return dao.deleteFecha(anio, mes, dia);
    }
    
    public Boolean isfechaValida(String fecha){
        String [] cadFecha;
        cadFecha = fecha.split("-");
        int anio = new Integer(cadFecha[0]);
        int mes = new Integer(cadFecha[1]);
        int dia = new Integer(cadFecha[2]);
        int count = getDia(anio, mes, dia);
        
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        calendar.set(Calendar.MONTH,mes-1);
        calendar.set(Calendar.YEAR,anio);
        
        return count==0 && calendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY;
    }
    
}
