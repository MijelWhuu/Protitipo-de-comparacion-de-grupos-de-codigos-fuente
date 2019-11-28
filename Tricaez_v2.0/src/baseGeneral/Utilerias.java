package baseGeneral;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author GLP, MAVG
 */

public class Utilerias {
    public Utilerias(){
        // ¿?
    }
    
    /*--------------------------------------------------------------------------
     * Función para cargar la imagen de fondo de pantalla
    --------------------------------------------------------------------------*/
    /*
    public void cargarImagen(javax.swing.JDesktopPane panel){
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        URL url = getClass().getClassLoader().getSystemResource("imagenes/background"+d.width+"x"+d.height+".png");
        try{
            BufferedImage image = ImageIO.read(url);
            panel.setBorder(new ImageDesktopPane(image));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    */
    
    public void cargarImagen(javax.swing.JDesktopPane panel){
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        try{
            BufferedImage image = ImageIO.read(new File("C:\\java\\system\\img\\background" + d.width + "x" + d.height + ".png"));
            panel.setBorder(new ImageDesktopPane(image));
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    /*--------------------------------------------------------------------------
     * Función para cargar el icono de la ventana
    --------------------------------------------------------------------------*/
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
        getImage(ClassLoader.getSystemResource("img/icoissstezac.png"));
        return retValue;
    }
    
    /*--------------------------------------------------------------------------
     * Función para mostrar formulario en pantalla
    --------------------------------------------------------------------------*/
    public void muestraForm (javax.swing.JInternalFrame f, javax.swing.JDesktopPane dp){
        try{
            boolean existeFrame = false;
            for(int i=0; i<dp.getAllFrames().length; i++){
                if(dp.getAllFrames()[i].getClass() == f.getClass()){
                    existeFrame = true;
                }
            }
            if(!existeFrame){
                int x = (dp.getSize().width-f.getWidth())/2;
                int y = (dp.getSize().height-f.getHeight())/2;
                f.show();
                f.setLocation(x, y);
                f.toFront();
                dp.add(f);
            }
            f.toFront();
            f.setSelected(true);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void muestraForm (javax.swing.JPanel p, javax.swing.JDesktopPane dp){
        int x = (dp.getSize().width-p.getWidth())/2;
        int y = (dp.getSize().height-p.getHeight())/2;
        p.show();
        p.setLocation(x, y);
        dp.add(p);
    }
    
    public void muestraForm (javax.swing.JPanel p, javax.swing.JLayeredPane lp){
        int x = (lp.getSize().width-p.getWidth())/2;
        int y = (lp.getSize().height-p.getHeight())/2;
        p.show();
        p.setLocation(x, y);
        lp.add(p);
    }
    
    public void muestraForm (javax.swing.JInternalFrame f, javax.swing.JLayeredPane lp){
        int x = (lp.getSize().width-f.getWidth())/2;
        int y = (lp.getSize().height-f.getHeight())/2;
        f.show();
        f.setLocation(x, y);
        lp.add(f);
    }
    
    public void muestraForm (javax.swing.JDialog jd, javax.swing.JLayeredPane lp){
        int x = (lp.getSize().width-jd.getWidth())/2;
        int y = (lp.getSize().height-jd.getHeight())/2;
        jd.show();
        jd.setLocation(x, y);
        lp.add(jd);
    }
    
    /*--------------------------------------------------------------------------
     * Función para limpiar una o varias cajas de texto simultameamente
    --------------------------------------------------------------------------*/
    public void LimpiaTxt(Component componente){
        // Si componente es un JTextField limpia unicamente una caja de texto
        // Si componente es un JDesktopPane u otro limpia las cajas contenidas en dicho contenedor
        if(componente instanceof JTextField){
            JTextField text = (JTextField)componente;
            text.setText("");
        }else if(componente instanceof JTextArea){
            JTextArea area = (JTextArea)componente;
            area.setText("");
        }else{
            if(componente instanceof Container){
                for(Component c : ((Container)componente).getComponents()){
                    LimpiaTxt(c);
                }
            }
        }
    }
    
    /*--------------------------------------------------------------------------
     * Función para validar que sea una curp valida
    --------------------------------------------------------------------------*/
    public Boolean curpValida (String curp){
	if (curp==null)
            return false;
        if (curp.length()!=18)
            return false;
	if (!(Character.isLetter(curp.charAt(0))&&Character.isLetter(curp.charAt(1))&&Character.isLetter(curp.charAt(2))&&Character.isLetter(curp.charAt(3))))
            return false;
        if (!(Character.isDigit(curp.charAt(4))&&Character.isDigit(curp.charAt(5))&&Character.isDigit(curp.charAt(6))&&Character.isDigit(curp.charAt(7))&&Character.isDigit(curp.charAt(8))&&Character.isDigit(curp.charAt(9))))
            return false;
        if (!(Character.isLetter(curp.charAt(10))&&Character.isLetter(curp.charAt(11))&&Character.isLetter(curp.charAt(12))&&Character.isLetter(curp.charAt(13))&&Character.isLetter(curp.charAt(14))&&Character.isLetter(curp.charAt(15))))
            return false;
        if (!(Character.isDigit(curp.charAt(16))||Character.isLetter(curp.charAt(16))))
            return false;
		if (!(Character.isDigit(curp.charAt(17))))
            return false;
        return true;
    }
    
    /*--------------------------------------------------------------------------
     * Función para validar que sea un email correcto
    --------------------------------------------------------------------------*/
    public Boolean validaEmail(String email){  
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
        CharSequence inputStr = email;  
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);  
        Matcher matcher = pattern.matcher(inputStr);  
        return matcher.matches();  
    }
    
    /*--------------------------------------------------------------------------
     * Función para validar un rfc correcto
    --------------------------------------------------------------------------*/
    public boolean rfcValida (String rfc,int moral){
	if (rfc==null)
            return false;
        if (moral==0){//Persona fisica
            if (rfc.length()!=13)
                return false;
            if (!(Character.isLetter(rfc.charAt(0))&&Character.isLetter(rfc.charAt(1))&&Character.isLetter(rfc.charAt(2))&&Character.isLetter(rfc.charAt(3))))
                return false;
            if (!(Character.isDigit(rfc.charAt(4))&&Character.isDigit(rfc.charAt(5))&&Character.isDigit(rfc.charAt(6))&&Character.isDigit(rfc.charAt(7))&&Character.isDigit(rfc.charAt(8))&&Character.isDigit(rfc.charAt(9))))
                return false;
            //Los 3 caracteres restantes pueden ser digitos o letra
            if (!((Character.isDigit(rfc.charAt(10))||Character.isLetter(rfc.charAt(10)))&&(Character.isDigit(rfc.charAt(11))||Character.isLetter(rfc.charAt(11)))&&(Character.isDigit(rfc.charAt(12))||Character.isLetter(rfc.charAt(12)))))
                return false;
        }else{//Persona moral
            if (rfc.length()!=12)
                return false;
            if (!(Character.isLetter(rfc.charAt(0))&&Character.isLetter(rfc.charAt(1))&&Character.isLetter(rfc.charAt(2))))
                return false;
            if (!(Character.isDigit(rfc.charAt(3))&&Character.isDigit(rfc.charAt(4))&&Character.isDigit(rfc.charAt(5))&&Character.isDigit(rfc.charAt(6))&&Character.isDigit(rfc.charAt(7))&&Character.isDigit(rfc.charAt(8))))
                return false;
            //Los 3 caracteres restantes pueden ser digitos o letra
            if (!((Character.isDigit(rfc.charAt(9))||Character.isLetter(rfc.charAt(9)))&&(Character.isDigit(rfc.charAt(10))||Character.isLetter(rfc.charAt(10)))&&(Character.isDigit(rfc.charAt(11))||Character.isLetter(rfc.charAt(11)))))
                return false;
        }
        return true;
    }
    
    /*--------------------------------------------------------------------------
     * Función para validar si un numero c/s decimal es correcto
    --------------------------------------------------------------------------*/
    public Boolean isDouble(Object numero){
        if(numero!=null && java.util.regex.Pattern.matches("((([0-9]*)?[.]?[0-9]+))",numero.toString()))
            return true;
        return false;
    }
    
    /*--------------------------------------------------------------------------
     * Función para convertir la fecha de dd/mm/aaaa a aaaa-mm-dd
    --------------------------------------------------------------------------*/
    public String fechaMySQL(String fechaDiagonal){
        String[] split = fechaDiagonal.split("/");
        String fecha = split[2] + "-" + split[1] + "-" + split[0];
        return fecha;
    }
    
    /*--------------------------------------------------------------------------
     * Función para convertir la fecha de aaaa-mm-dd a dd/mm/aaaa
    --------------------------------------------------------------------------*/
    public String fechaDiagonal(String fechaGuion){
        String[] split = fechaGuion.split("-");
        String fecha = split[2] + "/" + split[1] + "/" + split[0];
        return fecha;
    }
    
    private String completaCeros(int decimales){
        if(decimales<=0)
            return "";
        String cad = ".";
        for(int i=0; i<decimales; i++)
            cad+="0";
        return cad;
    }
    
    public String convierteMoneda(Double numero, Integer decimales){
        String formato = "$ #,##0"+completaCeros(new Integer(decimales));
        NumberFormat f = new DecimalFormat(formato);
        return f.format(redondearConBigDecimal(numero,decimales));
    }
    
    public String convierteMonedaTruncar(Double numero, Integer decimales){
        String formato = "$ #,##0"+completaCeros(new Integer(decimales));
        NumberFormat f = new DecimalFormat(formato);
        return f.format(truncarDecimal(numero,decimales));
    }
    
    public String convierteMoneda(Double numero){
        String formato = "###0.00";
        NumberFormat f = new DecimalFormat(formato);
        return f.format(redondearConBigDecimal(numero,2));
    }
    
    public String formateaNumero3Decimales(Double numero){
        String formato = "###0.000";
        NumberFormat f = new DecimalFormat(formato);
        return f.format(redondearConBigDecimal(numero,3));
    }
    
    public Double desformatearNumero(String numero) {
        return new Double(numero.replaceAll("[^0-9.]",""));
    }
    
    public Double redondearConBigDecimal(Double num, int digitos){
        String cad = num.toString();
        BigDecimal big = new BigDecimal(cad);
        big = big.setScale(digitos, RoundingMode.HALF_UP);
        return big.doubleValue();
    }
    
    public double truncarDecimal(double num, int digitos){
        if(num > 0)
            num = Math.floor(num * Math.pow(10,digitos))/Math.pow(10,digitos);
        else
            num = Math.ceil(num * Math.pow(10,digitos))/Math.pow(10,digitos);

        return num;
    }
    
     /*--------------------------------------------------------------------------
     * Función para sumar dias a una fecha determinada *El parametro fecha entra como yyyy-MM-dd
    --------------------------------------------------------------------------*/
    public String sumarDiasFecha(String fecha, int dias) {
        java.sql.Date fch = null;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("es","ES"));
        try{
            fch = new java.sql.Date(sdf.parse(fecha).getTime());
            cal.setTimeInMillis(fch.getTime());
            cal.add(Calendar.DATE, dias);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return new java.sql.Date(cal.getTimeInMillis()).toString();
    }
    
    /*--------------------------------------------------------------------------
     * Función para restar dias a una fecha determinada
    --------------------------------------------------------------------------*/
    public String restarFechasDias(String fecha, int dias) {
        java.sql.Date fch = null;
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("es","ES"));
        try{
            fch = new java.sql.Date(sdf.parse(fecha).getTime());
            cal.setTimeInMillis(fch.getTime());
            cal.add(Calendar.DATE, -dias);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return new java.sql.Date(cal.getTimeInMillis()).toString();
    }
    
    public Integer diferenciaDeFechas(Date fechaInicial, Date fechaFinal) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);
        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = df.format(fechaFinal);
        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException ex) {
        }

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }
    
    /*--------------------------------------------------------------------------
     * Función para validar una fecha determinada
    --------------------------------------------------------------------------*/
    public Boolean fechaValida(String fecha) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false);
            sdf.parse(fecha);
        }catch(Exception e){
            return false;
        }
        return true;
    }
    
    public static void cascade( JDesktopPane desktopPane, int layer ) {
    JInternalFrame[] frames = desktopPane.getAllFramesInLayer( layer );
    if ( frames.length == 0) return;
 
        cascade( frames, desktopPane.getBounds(), 24 );
    }
    public static void cascade( JDesktopPane desktopPane ) {
        JInternalFrame[] frames = desktopPane.getAllFrames();
        if ( frames.length == 0) return;
        
        cascade( frames, desktopPane.getBounds(), 24 );
    }
    private static void cascade( JInternalFrame[] frames, Rectangle dBounds, int separation ) {
        int margin = frames.length*separation + separation;
        int width = dBounds.width - margin;
        int height = dBounds.height - margin;
        for ( int i = 0; i < frames.length; i++) {
            frames[i].setBounds( separation + dBounds.x + i*separation,
                                 separation + dBounds.y + i*separation,
                                 width, height );
        }
    }
    
    /***************NUEVA FUNCION DE CONVERTIR UN NUMERO A LETRA******************/
    private String unidades2[] = {"UN", "DOS", "TRES", "CUATRO", "CINCO", "SEIS", "SIETE", "OCHO", "NUEVE", "DIEZ", "ONCE", "DOCE", "TRECE", "CATORCE", "QUINCE", "DIECISÉIS", "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE", "VEINTIUN", "VEINTIDÓS", "VEINTITRÉS", "VEINTICUATRO", "VEINTICINCO", "VEINTISÉIS", "VEINTISIETE", "VEINTIOCHO", "VEINTINUEVE"};
    private String decenas2[] = {"DIEZ","VEINTE","TREINTA", "CUARENTA", "CINCUENTA", "SESENTA", "SETENTA", "OCHENTA", "NOVENTA"};
    private String centenas2[] = {"CIENTO", "DOSCIENTOS", "TRESCIENTOS", "CUATROCIENTOS", "QUINIENTOS", "SEISCIENTOS", "SETECIENTOS", "OCHOCIENTOS", "NOVECIENTOS"};   
    
    public String convierteLetra(String numero){
        String []arrayNumero = numero.split("\\.");
        String agregaDecimal="";
        if(arrayNumero.length==2){
            agregaDecimal = agregaFinal(arrayNumero[1]);
        }else{
            agregaDecimal="00/100 M.N.";
        }
        if((arrayNumero[0].length()>=1 && arrayNumero[0].length()<=3) && new Integer(arrayNumero[0]).equals(1))
            return "UN PESO "+agregaDecimal;
        if((arrayNumero[0].length()>=0 && arrayNumero[0].length()<=3) && new Integer(arrayNumero[0]).equals(0))
            return "CERO PESOS "+agregaDecimal;
        return letra(arrayMiles(arrayNumero[0]))+" PESOS "+agregaDecimal;
    }
    private String agregaFinal(String decimal){
        if(decimal.length()==1){
            return decimal+"0/100 M.N.";
        }else if(decimal.length()==2){
            return decimal+"/100 M.N.";
        }else{
            return "00/100 M.N.";
        }
    } 
    private String CDU(String numero, Boolean flag){
        Integer valor = new Integer(numero);
        if(flag && valor.equals(1)){
            return "UN";
        }else if(valor.equals(0) || valor.equals(1)){
            return "";
        }else if(valor.equals(100)){
            return "CIEN";
        }else if(valor>1 && valor<30){
            return unidades2[valor-1];
        }else if(valor.toString().length()==2){
            if(valor%10==0)
                return decenas2[(valor/10)-1];
            else
                return decenas2[(valor/10)-1]+" Y "+unidades2[(valor%10)-1];
        }else{
            if(valor%100==0){
                return centenas2[(valor/100)-1];
            }else if((valor%100)>0 && (valor%100)<30){
                return centenas2[(valor/100)-1]+" "+unidades2[(valor%100)-1];
            }else{
                 String t1 = centenas2[(valor/100)-1];
                 String t2 = " "+decenas2[((valor%100)/10)-1];
                 String t3 = "";
                 if(((valor%100)%10)!=0)
                    t3 = " Y "+unidades2[((valor%100)%10)-1];
                 return t1+t2+t3;
            }
        }
    }
    private String invertir2(String cadena) {
        String resultado = "";
        for (int i=cadena.length()-1; i>=0; i--)resultado += cadena.charAt (i);
            return resultado;
    }   
    private String[] arrayMiles(String numero){
        int cont=0;
        String cadena = "";
        for(int i=numero.length()-1; i>=0; i--){
            Character c = new Character(numero.charAt(i));
            cont++;
            cadena+=c.toString();
            if(cont==3 && i>0){
                cadena+=",";
                cont=0;
            }
        }
        cadena=invertir2(cadena);
        String cad[] = cadena.split("\\,");
        return cad;
    }
    
    private String letra(String[] a){
        int lon = a.length;
        String cadena = "";
        switch(lon){
            case 10:{ 
                cadena = miles(cadena,a[0],a[1])+terminoMiles2(a[0],a[1]," CUATRILLONES");
                if(sonCeros(a[2],a[3],a[4],a[5],a[6],a[7],a[8],a[9])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[2],a[3])+terminoMiles2(a[2],a[3]," TRILLONES"," TRILLÓN");
                    if(sonCeros(a[4],a[5],a[6],a[7],a[8],a[9])){
                        cadena+= " DE";
                    }else{
                        cadena = miles(cadena,a[4],a[5])+terminoMiles2(a[4],a[5]," BILLONES"," BILLÓN");
                        if(sonCeros(a[6],a[7],a[8],a[9])){
                            cadena+= " DE";
                        }else{
                            cadena = miles(cadena,a[6],a[7])+terminoMiles2(a[6],a[7]," MILLONES"," MILLÓN");
                            if(sonCeros(a[8],a[9])){
                                cadena+= " DE";
                            }else{
                                cadena = miles(cadena,a[8],a[9]);
                            }
                        }
                    }
                }
                break;
            }
            case 9:{ 
                cadena = miles(cadena,"0",a[0])+terminoMiles(a[0]," CUATRILLÓN"," CUATRILLONES");
                if(sonCeros(a[1],a[2],a[3],a[4],a[5],a[6],a[7],a[8])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[1],a[2])+terminoMiles2(a[1],a[2]," TRILLONES"," TRILLÓN");
                    if(sonCeros(a[3],a[4],a[5],a[6],a[7],a[8])){
                        cadena+= " DE";
                    }else{
                        cadena = miles(cadena,a[3],a[4])+terminoMiles2(a[3],a[4]," BILLONES"," BILLÓN");
                        if(sonCeros(a[5],a[6],a[7],a[8])){
                            cadena+= " DE";
                        }else{
                            cadena = miles(cadena,a[5],a[6])+terminoMiles2(a[5],a[6]," MILLONES"," MILLÓN");
                            if(sonCeros(a[7],a[8])){
                                cadena+= " DE";
                            }else{
                                cadena = miles(cadena,a[7],a[8]);
                            }
                        }
                    }
                }
                break;
            }
            case 8:{ 
                cadena = miles(cadena,a[0],a[1])+terminoMiles2(a[0],a[1]," TRILLONES");
                if(sonCeros(a[2],a[3],a[4],a[5],a[6],a[7])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[2],a[3])+terminoMiles2(a[2],a[3]," BILLONES"," BILLÓN");
                    if(sonCeros(a[4],a[5],a[6],a[7])){
                        cadena+= " DE";
                    }else{
                        cadena = miles(cadena,a[4],a[5])+terminoMiles2(a[4],a[5]," MILLONES"," MILLÓN");
                        if(sonCeros(a[6],a[7])){
                            cadena+= " DE";
                        }else{
                            cadena = miles(cadena,a[6],a[7]);
                        }
                    }
                }
                break;
            }
            case 7:{ 
                cadena = miles(cadena,"0",a[0])+terminoMiles(a[0]," TRILLÓN"," TRILLONES");
                if(sonCeros(a[1],a[2],a[3],a[4],a[5],a[6])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[1],a[2])+terminoMiles2(a[1],a[2]," BILLONES"," BILLÓN");
                    if(sonCeros(a[3],a[4],a[5],a[6])){
                        cadena+= " DE";
                    }else{
                        cadena = miles(cadena,a[3],a[4])+terminoMiles2(a[3],a[4]," MILLONES"," MILLÓN");
                        if(sonCeros(a[5],a[6])){
                            cadena+= " DE";
                        }else{
                            cadena = miles(cadena,a[5],a[6]);
                        }
                    }
                }
                break;
            }
            case 6:{ 
                cadena = miles(cadena,a[0],a[1])+terminoMiles2(a[0],a[1]," BILLONES");
                if(sonCeros(a[2],a[3],a[4],a[5])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[2],a[3])+terminoMiles2(a[2],a[3]," MILLONES"," MILLÓN");
                    if(sonCeros(a[4],a[5])){
                        cadena+= " DE";
                    }else{
                        cadena = miles(cadena,a[4],a[5]);
                    }
                }
                break;
            }
            case 5:{ 
                cadena = miles(cadena,"0",a[0])+terminoMiles(a[0]," BILLÓN"," BILLONES");
                if(sonCeros(a[1],a[2],a[3],a[4])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[1],a[2])+terminoMiles2(a[1],a[2]," MILLONES"," MILLÓN");
                    if(sonCeros(a[3],a[4])){
                        cadena+= " DE";
                    }else{
                        cadena = miles(cadena,a[3],a[4]);
                    }
                }
                break;
            }
            case 4:{ 
                cadena = miles(cadena,a[0],a[1])+terminoMiles2(a[0],a[1]," MILLONES");
                if(sonCeros(a[2],a[3])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[2],a[3]);
                }
                break;
            }
            case 3:{ 
                cadena = miles(cadena,"0",a[0])+terminoMiles(a[0]," MILLÓN"," MILLONES");
                if(sonCeros(a[1],a[2])){
                    cadena+= " DE";
                }else{
                    cadena = miles(cadena,a[1],a[2]);
                }
                break;
            }
            case 2:{ 
                cadena = miles(cadena,a[0],a[1]);
                break;
            }
            default:{ 
                cadena = miles(cadena,"0",a[0]);
            }
        }
       return cadena.trim();
    }
    
    private String miles(String cadena, String op1, String op2){
        if(!sonCeros(op1))
            cadena+=" "+CDU(op1,false)+" MIL";
        if(!sonCeros(op2))
            cadena+=" "+CDU(op2,true);
        return cadena;
    }
    private Boolean sonCeros(String ...array){
        for(int i=0; i<array.length; i++)
            if(!new Integer(array[i]).equals(0))
                return false;
        return true;
    }
    
    private String terminoMiles(String num, String op1, String op2){
        if(new Integer(num).equals(0))
            return "";
        if(new Integer(num).equals(1))
            return op1;
        return op2;
    }
    private String terminoMiles2(String num1, String num2, String op1){
        if(new Integer(num1).equals(0) && new Integer(num2).equals(0))
            return "";
        return op1;
    }
    private String terminoMiles2(String num1, String num2, String op1, String op2){
        if(new Integer(num1).equals(0) && new Integer(num2).equals(0))
            return "";
        if(new Integer(num1).equals(0) && new Integer(num2).equals(1))
            return op2;
        return op1;
    }
    /*****************************************************************************/
    
    /*--------------------------------------------------------------------------
     * Función para obtener la fecha en formato largo
    --------------------------------------------------------------------------*/

    /**
     *
     * @param fecha_actual
     * @return 
     */
    
    public String fechaLetra(String fecha_actual){
        String fecha[] = fecha_actual.split("-");
        String cadena = fecha[2]+" DE ";
        switch (new Integer(fecha[1])){
            case  1: cadena += "ENERO DEL ";break;
            case  2: cadena += "FEBRERO DEL ";break;
            case  3: cadena += "MARZO DEL ";break;
            case  4: cadena += "ABRIL DEL ";break;
            case  5: cadena += "MAYO DEL ";break;
            case  6: cadena += "JUNIO DEL ";break;
            case  7: cadena += "JULIO DEL ";break;
            case  8: cadena += "AGOSTO DEL ";break;
            case  9: cadena += "SEPTIEMBRE DEL ";break;
            case 10: cadena += "OCTUBRE DEL ";break;
            case 11: cadena += "NOVIEMBRE DEL ";break;
            case 12: cadena += "DICIEMBRE DEL ";break;
        }
        cadena += fecha[0];
        return cadena;
    }
    
    public String fechaLetra(String fecha_actual, int formato){
        String fecha[] = fecha_actual.split("-");
        String cadena = "";
        if(formato == 1){
            cadena = fecha[2]+" DE ";
            switch (new Integer(fecha[1])){
                case  1: cadena += "ENERO DEL ";break;
                case  2: cadena += "FEBRERO DEL ";break;
                case  3: cadena += "MARZO DEL ";break;
                case  4: cadena += "ABRIL DEL ";break;
                case  5: cadena += "MAYO DEL ";break;
                case  6: cadena += "JUNIO DEL ";break;
                case  7: cadena += "JULIO DEL ";break;
                case  8: cadena += "AGOSTO DEL ";break;
                case  9: cadena += "SEPTIEMBRE DEL ";break;
                case 10: cadena += "OCTUBRE DEL ";break;
                case 11: cadena += "NOVIEMBRE DEL ";break;
                case 12: cadena += "DICIEMBRE DEL ";break;
            }
            cadena += fecha[0];
        }else if(formato == 2){
            switch (new Integer(fecha[1])){
                case  1: cadena += "ENERO DEL ";break;
                case  2: cadena += "FEBRERO DEL ";break;
                case  3: cadena += "MARZO DEL ";break;
                case  4: cadena += "ABRIL DEL ";break;
                case  5: cadena += "MAYO DEL ";break;
                case  6: cadena += "JUNIO DEL ";break;
                case  7: cadena += "JULIO DEL ";break;
                case  8: cadena += "AGOSTO DEL ";break;
                case  9: cadena += "SEPTIEMBRE DEL ";break;
                case 10: cadena += "OCTUBRE DEL ";break;
                case 11: cadena += "NOVIEMBRE DEL ";break;
                case 12: cadena += "DICIEMBRE DEL ";break;
            }
            cadena += fecha[0];
        }else if(formato == 3){
            cadena += fecha[0];
        }
        return cadena;
    }
    
    public String redondeaDecimal(Double d){
        String s = d.toString();
        s = s.substring(s.indexOf(".")+1,s.length());
        Integer digitos = 0;
        DecimalFormat df = null;
        if(s.equals("00")){
            df = new DecimalFormat("0");
            digitos = 0;
        }else if(s.equals("0")){
            df = new DecimalFormat("0");
            digitos = 0;
        }else{
            df = new DecimalFormat("0.##");
            digitos = 2;
        }
        df.setMinimumFractionDigits(digitos);
        String sDecimal = df.format(d);
        return sDecimal;
    }
    
    /*------------------------------------------------------------------------------
    FUNCION PARA SABER SI LA CLAVE CATASTRAL ESTA EN EL FORMATO XX-XXX-XX-XX-X-X-X-X
    ------------------------------------------------------------------------------*/
    public boolean isClaveContieneGuion(String clave){
        Boolean contiene = false;
        char[] array = new char[clave.length()];
        for(int i=0;i<clave.length();i++){
            array[i] = clave.charAt(i);
        }
        for (int z=0;z<array.length;z++){
            if (array[z] == '-'){
                contiene = true;
            }
        }
        return contiene;
    }
    
    /*------------------------------------------------------------------------------
    FUNCION PARA TRASNFORMAR LA CLAVE CATASTRAL CON AL FORMATO XX-XX-X-X-X-X-X Y VISCEVERSA
    ------------------------------------------------------------------------------*/
    public String getClaveConGuion(String clave){
        boolean contiene = false;
        String cveCat="";
        char[] array = new char[clave.length()];
        for(int i=0;i<clave.length();i++){
            array[i] = clave.charAt(i);
        }
        for (int z=0;z<array.length;z++){
            if (array[z]=='-'){
                contiene = true;
            }
        }
        if(contiene){
            for(int x=0;x<array.length;x++){
                if(array[x]!='-'){
                    cveCat = cveCat + array[x];
                }
            }
        }else{
            if(clave.length()==15){
                cveCat = ""+array[0]+array[1]+"-"+array[2]+array[3]+array[4]+"-"+array[5]+array[6]+"-"+array[7]+array[8]+array[9]+"-"+array[10]+array[11]+array[12]+"-"+array[13]+array[14];
            }else if(clave.length()==11){
                cveCat = ""+array[0]+array[1]+"-"+array[2]+array[3]+array[4]+"-"+array[5]+array[6]+array[7]+array[8]+array[9]+array[10];
            }else{
                cveCat = clave;
            }
        }
        return cveCat;
    }
    
    public String mesToString(Integer mes){
        switch(mes){
            case 1: return "ENERO";
            case 2: return "FEBRERO";
            case 3: return "MARZO";
            case 4: return "ABRIL";
            case 5: return "MAYO";
            case 6: return "JUNIO";
            case 7: return "JULIO";
            case 8: return "AGOSTO";
            case 9: return "SEPTIEMBRE";
            case 10: return "OCTUBRE";
            case 11: return "NOVIEMBRE";
            case 12: return "DICIEMBRE";
            default: return "";
        }
    }
    
    public String LPADString(String varString, int length, char charRelleno){
        String relleno = "";
        if(varString != null){
            for (int i=varString.length()-1; i<length; i++)
                relleno += charRelleno;
            varString = relleno + varString;
        }
        return varString;
    }
    
    public String RPADString(String varString, int length, char charRelleno){
        String relleno = "";
        if(varString != null){
            for (int i=varString.length()-1 ;i<length; i++)
                relleno += charRelleno;
            varString += relleno;
        }
        return varString;
    }
    
    public String convierteMoneda(Integer numero){
        String formato = "$ #,##0";
        NumberFormat f = new DecimalFormat(formato);
        return f.format(numero);
    }
    
    public String numeroMiles(Double numero, Integer decimales){
        String formato = "#,##0"+completaCeros(new Integer(decimales));
        NumberFormat f = new DecimalFormat(formato);
        return f.format(redondearConBigDecimal(numero,decimales));
    }
    
    public String numeroMiles(Double numero){
        String formato = "#,##0.00";
        NumberFormat f = new DecimalFormat(formato);
        return f.format(redondearConBigDecimal(numero,2));
    }
    
    public String numeroMiles(Integer numero){
        String formato = "#,##0";
        NumberFormat f = new DecimalFormat(formato);
        return f.format(numero);
    }
    
    public String callVentanaGuardarPDF(){
        String path = "";
        String ruta = "";
        String nameFile = "";
        javax.swing.JFileChooser ventanaGuardar= new javax.swing.JFileChooser();
        ventanaGuardar.setFileFilter(new FileNameExtensionFilter("Archivo Pdf .pdf", "pdf"));
        while(true){
            if(ventanaGuardar.showSaveDialog(null)==javax.swing.JFileChooser.APPROVE_OPTION){
                path = ventanaGuardar.getSelectedFile().getAbsolutePath();
                ruta = ventanaGuardar.getSelectedFile().getParent();
                nameFile = ventanaGuardar.getName(new File(path));
                String[] arrayNameFile = nameFile.split("\\.");
                if(ventanaGuardar.getFileFilter().getDescription().equals("Archivo Pdf .pdf")){
                    if(!arrayNameFile[arrayNameFile.length-1].toUpperCase().equals("PDF")){
                        nameFile+=".pdf";
                    }
                    File archPredio = new File(ruta,nameFile);
                    if(archPredio.exists()){
                        if(JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,"El Archivo ya existe,desea reemplazarlo","Guardar",JOptionPane.YES_NO_OPTION)){
                            return archPredio.getPath();
                        }
                    }else{
                        return archPredio.getPath();
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Para poder guardar tienes que seleccionar el tipo de extensión .pdf","Aviso",0);
                }
            }
            else{
                return "";
            }
        }
    }
    
    public String eliminaEspacios(String cadena){
        cadena=cadena.replaceAll(" +", " ");
        cadena=cadena.trim();
        return cadena;
    }
    
    public int booleanToTinyint(boolean arg){
        if(arg) return 1;
        return 0;
    }
    
    /*public boolean validaFormatoExpediente(String exp){
        return java.util.regex.Pattern.matches("[0-9]+[/][0-9][0-9][0-9][0-9][-][I]+",exp);
    }*/
    
}
