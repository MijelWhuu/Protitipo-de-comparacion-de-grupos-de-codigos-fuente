package baseSistema;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;
import javax.swing.CellEditor;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author mavg
 * @version: 15/JULIO/2015
 */
public class Utilidades {

    public Utilidades() {
    }

    /**
     * Método que formatea el ID correspondiente de los catálogos.
     * @param formato Define la longitud y el tipo de letra utilizados para formatear, ejemplo: "C0000".
     * @param valor Es el numero consecutivo que le corresponde al elemento en la base de datos.
     * @return Regresa un objeto del tipo String el cual contiene la clave formateada.
     */
    public String formatearClave(String formato, Integer valor){
        String letra = formato.substring(0,1);
        Integer digitos = formato.length();
        Integer longitud = valor.toString().length();
        Integer max = (digitos-1) - longitud;
        String cadCeros = "";
        for(int i=0; i<max; i++)
            cadCeros+="0";
        return letra + cadCeros + valor.toString();
    }
    
    /**
     * Método que formatea el ID correspondiente a tablas principales, ejemplo: "015C000000".
     * @deprecated sustituida por: formatearClaveFecha
     * @param anio Los ultimos tres digitos del año actual.
     * @param letra Letra para asignar a la clase.
     * @param valor Es el numero consecutivo que le corresponde al elemento en la base de datos.
     * @return Regresa un objeto del tipo String el cual contiene la clave formateada.
     */
    public String formatearClave(String anio, String letra, Integer valor){
        Integer longitud = valor.toString().length();
        Integer max = 6 - longitud;
        String cadCeros = "";
        for(int i=0; i<max; i++)
            cadCeros+="0";
        return anio + letra + cadCeros + valor.toString();
    }
    
    /**
     * Método que formatea el ID correspondiente a tablas principales, ejemplo: "20150715A0001".
     * @param letra Letra para asignar a la clase.
     * @param valor Es el numero consecutivo que le corresponde al elemento en la base de datos.
     * @return Regresa un objeto del tipo String el cual contiene la clave formateada.
     */
    public String formatearClaveFecha(String letra, Integer valor){
        Integer longitud = valor.toString().length();
        Integer max = 4 - longitud;
        String cadCeros = "";
        for(int i=0; i<max; i++)
            cadCeros+="0";
        return getFormatoFechaString().concat(letra).concat(cadCeros).concat(valor.toString());
    }
    
    /**
     * Método que une la fecha actual en un string sin separaciones iniciando por el año, mes y día, ejemplo: 20150715.
     * @return Regresa un objeto del tipo String.
     */
    public String getFormatoFechaString(){
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        Integer anio = cal.get(Calendar.YEAR);
        Integer mes = cal.get(Calendar.MONTH)+1;
        Integer dia = cal.get(Calendar.DAY_OF_MONTH);
        String anioTxt = anio.toString();
        String mesTxt = (mes.toString().length()==1)?"0".concat(mes.toString()):mes.toString();
        String diaTxt = (dia.toString().length()==1)?"0".concat(dia.toString()):dia.toString();
        return anioTxt.concat(mesTxt).concat(diaTxt);
    }
    
    /**
     * Método que elimina los espacios innecesarios en un String".
     * @param cadena Texto al que se eliminaran los espacios.
     * @return Regresa un objeto del tipo String del texto sin espacios innecesarios.
     */
    public String eliminaEspacios(String cadena){
        cadena=cadena.replaceAll(" +", " ");
        cadena=cadena.trim();
        return cadena;
    }
    
    /**
     * Método que verifica que un número decimal sea válido.
     * @param numero Es el número decimal que se desea verificar, se recibe como del tipo object para aceptar cualquier tipo de dato.
     * @return Regresa un objeto del tipo Boolean, TRUE es válido, FALSE es inválido.
     */
    public Boolean isDouble(Object numero){
        return numero!=null && java.util.regex.Pattern.matches("((([0-9]*)?[.]?[0-9]+))",numero.toString());
    }
    
    /**
     * Método que convierte una fecha del Tipo util.Date a una fecha sql.Date.
     * @param date Es la fecha del tipo java.util.Date.
     * @return Regresa la fecha convertida al tipo java.sql.Date.
     */
    public java.sql.Date convertirDateToSqlDate(java.util.Date date){
        return new java.sql.Date(date.getTime());
    }
    
    /**
     * Método que permite inicializar (Agregar datos) un JComboBox .
     * @param combo Es el JComboBox al que inicializaremos.
     * @param inicio Es el objeto inicial que será agregado al JComboBox.
     */
    public void inicializaCombo(javax.swing.JComboBox combo, Object inicio){
        combo.removeAllItems();
        combo.addItem(inicio);
    }
    
    /**
     * Método que permite inicializar (Agregar datos) un JComboBox .
     * @param combo Es el JComboBox al que inicializaremos.
     * @param inicio Es el objeto inicial que será agregado al JComboBox.
     * @param flag Es para habilitar o deshabilitar el JComboBox.
     */
    public void inicializaCombo(javax.swing.JComboBox combo, Object inicio, boolean flag){
        combo.removeAllItems();
        combo.addItem(inicio);
        combo.setEnabled(flag);
    }
    
    /**
     * Método que permite inicializar (Agregar datos) un JComboBox .
     * @param combo Es el JComboBox al que inicializaremos.
     * @param inicio Es el objeto inicial que será agregado al JComboBox.
     * @param array Son los objetos o datos que se agregarán al JComboBox.
     */
    public void inicializaCombo(javax.swing.JComboBox combo, Object inicio, ArrayList array){
        combo.removeAllItems();
        combo.addItem(inicio);
        if(!array.isEmpty()){
            for (Object array1 : array)
                combo.addItem(array1);
        }
    }
    
    /**
     * Método que permite habilitar o inhabilitar componentes swing.
     * @param flag Es el valor boolean que indica si habilita (true) o inhabilita(false).
     * @param compo Es la lista o arreglo de los compones swing.
     */
    public void enabledComponet(boolean flag, javax.swing.JComponent ...compo){
        for(javax.swing.JComponent co : compo){
            co.setEnabled(flag);
        }
    }
    
    public void editabledComponet(boolean flag, javax.swing.JComponent ...compo){
        for(javax.swing.JComponent co : compo){
            if(co instanceof javax.swing.JTextField){
                javax.swing.JTextField texto = (javax.swing.JTextField)co;
                texto.setEditable(flag);
            }else if(co instanceof javax.swing.JTextArea){
                javax.swing.JTextArea texto = (javax.swing.JTextArea)co;
                texto.setEditable(flag);
            }
        }
    }

    /**
     * Método que permite seleccionar un JRadioButton.
     * @param flag Es el valor boolean para selccionar los JRadioButton.
     * @param rbtn1 Es el JRadioButton que será seleccionado si flag es true.
     * @param rbtn2 Es el JRadioButton que será seleccionado si flag es false.
     */
    public void selectedJRadioButton(boolean flag, javax.swing.JRadioButton rbtn1, javax.swing.JRadioButton rbtn2){
        if(flag){
            rbtn1.setSelected(true);
            rbtn2.setSelected(false);
        }else{
            rbtn1.setSelected(false);
            rbtn2.setSelected(true);
        }
    }
    
    public String callVentanaGuardarPDF(){
        javax.swing.JFileChooser ventanaGuardar= new javax.swing.JFileChooser();
        ventanaGuardar.setFileFilter(new FileNameExtensionFilter("Archivo Pdf .pdf", "pdf"));
        while(true){
            if(ventanaGuardar.showSaveDialog(null)==javax.swing.JFileChooser.APPROVE_OPTION){
                String path = ventanaGuardar.getSelectedFile().getAbsolutePath();
                String ruta = ventanaGuardar.getSelectedFile().getParent();
                String nameFile = ventanaGuardar.getName(new File(path));
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
    
    /*-----------------------------------------------------------------------------
       CAMBIA LA FECHA DEL FORMATO DD/MM/AAAA AL FORMATO AAAA-MM-DD
    -----------------------------------------------------------------------------*/
    public String cambiaFechaAGuion( String fecha ){
        String[] split = fecha.split("/");
        String fe = split[2] + "-" + split[1] + "-" + split[0];
        return fe;
    }
    
    /*-----------------------------------------------------------------------------
       CAMBIA LA FECHA DEL FORMATO AAAA-MM-DD AL FORMATO DD/MM/AAAA
    -----------------------------------------------------------------------------*/
    public String cambiaFechaADiagonal( String fecha ){
        String[] split = fecha.split("-");
        String fe = split[2] + "/" + split[1] + "/" + split[0];
        return fe;
    }
    
    /**
     * Método que permite pasar una fecha en String a Date con el formato yyyy-MM-dd.
     * @param fechaTxt Es la fecha en formato String.
     * @return Regresa la fecha en formato Date.
     */
    public java.util.Date stringToDate(String fechaTxt){
        java.util.Date fecha = null;
        try{
            fecha= new SimpleDateFormat("yyyy-MM-dd").parse(fechaTxt);
        }catch(Exception e){
            System.out.println("Error convertir fecha: "+e.getMessage());
        }
        return fecha;
    }
    
    public java.util.Date stringToDateEspecial(String fechaTxt){
        java.util.Date fecha = null;
        try{
            fecha= new SimpleDateFormat("dd-MMM-yyyy").parse(fechaTxt);
        }catch(Exception e){
            System.out.println("Error convertir fecha: "+e.getMessage());
        }
        return fecha;
    }
    
    /**
     * Método que permite pasar una fecha en String a Date con el formato dd-MMM-yyyy.
     * @param fecha Es la fecha en formato Date.
     * @return Regresa la fecha en formato String.
     */
    public String dateToString(java.util.Date fecha){
        System.out.println(fecha);
        String fechaTxt= "";
        try{
            fechaTxt = new SimpleDateFormat("dd-MMM-yyyy").format(fecha);
            System.out.println(fechaTxt);
        }catch(Exception e){
            System.out.println("Error convertir fecha: "+e.getMessage());
        }
        return fechaTxt;
    }
    
    /**
     * Método que permite pasar una fecha en String a Date con el formato yyyy-MM-dd.
     * @param fecha Es la fecha en formato Date.
     * @return Regresa la fecha en formato String.
     */
    public String dateCastString(java.util.Date fecha){
        String fechaTxt= "";
        try{
            fechaTxt = new SimpleDateFormat("yyyy-MM-dd").format(fecha);
        }catch(Exception e){
            System.out.println("Error convertir fecha: "+e.getMessage());
        }
        return fechaTxt;
    }
    
    public String dateToStringTabla(java.util.Date fecha){
        String fechaTxt= "";
        try{
            fechaTxt = new SimpleDateFormat("dd/MM/yyyy").format(fecha);
        }catch(Exception e){
            System.out.println("Error convertir fecha: "+e.getMessage());
        }
        return fechaTxt;
    }
    
    public void focus(java.awt.event.KeyEvent evt, javax.swing.JComponent componente){
        if(evt.getKeyChar()==KeyEvent.VK_TAB){
            evt.consume();
            componente.transferFocus();
        }
        if(evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_TAB){
            componente.transferFocusBackward();
        }
    }
    
    public void cleanComponent(javax.swing.JComponent ...componentes){
        for(javax.swing.JComponent campo : componentes){
            if(campo instanceof javax.swing.JTextField){
                javax.swing.JTextField texto = (javax.swing.JTextField)campo;
                texto.setText("");
            }else if(campo instanceof javax.swing.JTextArea){
                javax.swing.JTextArea texto = (javax.swing.JTextArea)campo;
                texto.setText("");
            }else if(campo instanceof javax.swing.JLabel){
                javax.swing.JLabel texto = (javax.swing.JLabel)campo;
                texto.setText("");
            }else if(campo instanceof javax.swing.JComboBox){
                javax.swing.JComboBox combo = (javax.swing.JComboBox)campo;
                if(combo.getItemCount()>0)
                    combo.setSelectedIndex(0);
            }else if(campo instanceof javax.swing.JCheckBox){
                javax.swing.JCheckBox cbx = (javax.swing.JCheckBox)campo;
                cbx.setSelected(false);
            }else if(campo instanceof com.toedter.calendar.JDateChooser){
                com.toedter.calendar.JDateChooser fecha = (com.toedter.calendar.JDateChooser)campo;
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                fecha.setDate(cal.getTime());
            }
        }
    }
    
    public void formatoJDateChooser(com.toedter.calendar.JDateChooser fecha){
        fecha.getDateEditor().setEnabled(false);
        ((com.toedter.calendar.JTextFieldDateEditor)fecha.getDateEditor()).setDisabledTextColor(java.awt.Color.darkGray);
    }
    
    /*public String completaNumeroExpediente(String num){
        if(num.length()==0)
            return null;
        else if(java.util.regex.Pattern.matches("[0-9]+[/][0-9][0-9][0-9][0-9][-][I]{1,2}",num)){
            return num;
        }else{
            String exp;
            try{
                int numero = Integer.parseInt(num);
                Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                Integer anio = cal.get(Calendar.YEAR);
                String mesa = "I";
                if(numero%2==0)
                    mesa = "II";
                exp = "" + numero + "/" + anio + "-" + mesa;
            }catch (NumberFormatException e){
                return null;
            }
            return exp;
        }
    }
    
    public Boolean validaNumeroExpediente(String num){
        return java.util.regex.Pattern.matches("[0-9]+[/][0-9][0-9][0-9][0-9][-][I]{1,2}",num);
    }*/
    
    public Integer verificaInteger(String num){
        try{
            return Integer.parseInt(num);
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    
    public void copyPasteTabla(final javax.swing.JTable table){
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                doCopy(table);
            }
        };
        final KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        table.registerKeyboardAction(listener, "Copy", stroke, javax.swing.JComponent.WHEN_FOCUSED);
        
        ActionListener listener2 = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent event) {
                doPaste(table);
            }
        };      
        final KeyStroke stroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        table.registerKeyboardAction(listener2, "Paste", stroke2, javax.swing.JComponent.WHEN_FOCUSED);
    }
    
    private void doCopy(javax.swing.JTable table) {
        int col = table.getSelectedColumn();
        int row = table.getSelectedRow();
        if(col!=-1 && row!=-1){
            Object value = table.getValueAt(row, col);
            String data = (value==null)? "":value.toString();
            final StringSelection selection = new StringSelection(data);     
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }
    
    private void doPaste(javax.swing.JTable table) {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        final Transferable content = clipboard.getContents(this);
        if(content!=null){
            try{
                final String value = content.getTransferData(DataFlavor.stringFlavor).toString();     
                final int col = table.getSelectedColumn();
                final int row = table.getSelectedRow();
                if (table.isCellEditable(row, col)) {
                    table.setValueAt(value, row, col);
                    if(table.getEditingRow() == row && table.getEditingColumn() == col){
                        final CellEditor editor = table.getCellEditor();
                        editor.cancelCellEditing();
                        table.editCellAt(row, col);
                    }
                }
                table.repaint();
            }catch(UnsupportedFlavorException e){
                System.err.println("UNSUPPORTED FLAVOR EXCEPTION " + e.getLocalizedMessage());// String have to be the standard flavor
            }catch(IOException e){
                System.err.println("DATA CONSUMED EXCEPTION " + e.getLocalizedMessage());// The data is consumed?
            }
        }
    }
    
    public java.util.Date esFormatoFechaValida(String fecha){
        if(!java.util.regex.Pattern.matches("[0-9]{1,2}[/][0-9][0-9][/][0-9][0-9][0-9][0-9]",fecha))
            return null;
        
        String [] cadFecha;
        cadFecha = fecha.split("/");
        int dia = new Integer(cadFecha[0]);
        int mes = new Integer(cadFecha[1]);
        int anio = new Integer(cadFecha[2]);
        
        if(dia<1 || dia>31) return null;
        if(mes<1 || mes>12) return null;
        if(anio<1900) return null;
        
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH,dia);
        calendar.set(Calendar.MONTH,mes-1);
        calendar.set(Calendar.YEAR,anio);
        
        return calendar.getTime();
    }   
    
    /**
     * Método que convierte una fecha del Tipo sql.Date a una fecha util.Date.
     * @param sqlDate Es la fecha del tipo java.sql.Date.
     * @return Regresa la fecha convertida al tipo java.util.Date.
     */
    public java.util.Date convertirSqlDateToUtilDate(java.sql.Date sqlDate){
        java.util.Date javaDate = null;
        if(sqlDate != null){
            javaDate = new java.util.Date(sqlDate.getTime());
        }
        return javaDate;
    }
    
}
