package interfaz.notificaciones;

import baseSistema.TablaEspecial;
import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.notificaciones.CargaNotificacionesBl;
import bl.operaciones.RegistroAutosBl;
import bl.utilidades.CalendarioOficialBl;
import entitys.CargaNotificacionEntity;
import entitys.ComboAutoEntity;
import entitys.ComboFechaEntity;
import entitys.ComboFechasCargaEntity;
import entitys.RegistroAutoEntity;
import entitys.RelacionEntity;
import entitys.SesionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mavg
 */
public class CargaNotificaciones extends javax.swing.JInternalFrame {

    CargaNotificacionesBl bl = new CargaNotificacionesBl();
    SesionEntity sesion;
    Utilidades u = new Utilidades();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    RegistroAutosBl blAuto = new RegistroAutosBl();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    TablaEspecial tabla1, tabla2, tabla3, tabla4, tabla5, tabla6, tabla0;
    
    /**
     * Creates new form CargaNotificaciones
     * @param sesion
     */
    public CargaNotificaciones(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        lblAuto.setVisible(false);
        formatearCampos();
        mapeoTeclas();
        u.copyPasteTabla(jTable1);
        u.copyPasteTabla(jTable2);
        u.copyPasteTabla(jTable3);
        u.copyPasteTabla(jTable4);
        u.copyPasteTabla(jTable5);
        u.copyPasteTabla(jTable6);
        incializaTabla0();
        incializaTabla1();
        incializaTabla2();
        incializaTabla3();
        incializaTabla4();
        incializaTabla5();
        incializaTabla6();
        cargaFechas();
        txtBuscarAnio.setText(""+cal.get(Calendar.YEAR));
    }
    
    private void formatearCampos(){
        txtBuscarFolio.setDocument(new JTextMascara(5,true,"numerico"));
        txtBuscarAnio.setDocument(new JTextMascara(4,true,"numerico"));
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de Buscar y Cargar">
    private void cargaInfoAuto(RelacionEntity rel){
        lblAuto.setText(rel.getId_relacion());
        tabla0.clearTabla();
        tabla1.clearTabla();
        tabla2.clearTabla();
        tabla3.clearTabla();
        tabla4.clearTabla();
        tabla5.clearTabla();
        tabla6.clearTabla();
        ArrayList arrayDatos = new ArrayList();
        String expediente = "";
        if(rel.getId_registro_autos()!=null){
            System.out.println("Int_fun_001");
            arrayDatos = bl.getAuto(rel.getId_registro_autos());
            expediente = bl.getAutoExpediente(rel.getId_registro_autos());
            System.out.println("Int_fun_002");
        }else if(rel.getId_reg_resolucion()!=null){
            System.out.println("Int_fun_003");
            arrayDatos = bl.getResolucion(rel.getId_reg_resolucion());
            expediente = bl.getResolucionExpediente(rel.getId_reg_resolucion());
            System.out.println("Int_fun_004");
        }else if(rel.getId_reg_sentencia()!=null){
            System.out.println("Int_fun_005");
            arrayDatos = bl.getSentencia(rel.getId_reg_sentencia());
            expediente = bl.getSentenciaExpediente(rel.getId_reg_sentencia());
            System.out.println("Int_fun_006");
        }
        
        for(Object array1 : arrayDatos){            
            tabla0.addRow((Object[])array1);
        }
        System.out.println("Int_fun_007");
        ArrayList array = bl.getActores(expediente);
        if(array.size()>0)
            llenaTabla(array);
        ArrayList array2 = bl.getAutoridades(expediente);
        if(array2.size()>0)
            llenaTabla2(array2);
        ArrayList array3 = bl.getTerceroAutoridades(expediente);
        if(array3.size()>0)
            llenaTabla3(array3);
        ArrayList array4 = bl.getTerceroActores(expediente);
        if(array4.size()>0)
            llenaTabla4(array4);
        ArrayList array5 = bl.getAutoridadesAjenas(expediente);
        if(array5.size()>0)
            llenaTabla5(array5);
        ArrayList array6 = bl.getPersonasAjenas(expediente);
        if(array6.size()>0)
            llenaTabla6(array6);
        System.out.println("Int_fun_008");
        removeNotificaciones(rel.getId_relacion());
        System.out.println("Int_fun_009");
        if(tabla1.getRenglones()==0){
            cbxNotiActores.setVisible(false);
            panelActores.setVisible(false);
        }
        if(tabla2.getRenglones()==0){
            cbxNotiAutoridades.setVisible(false);
            panelAutoridades.setVisible(false);
        }
        if(tabla3.getRenglones()==0){
            cbxNotiTerceroAutoridades.setVisible(false);
            panelTerceroAutoridades.setVisible(false);
        }
        if(tabla4.getRenglones()==0){
            cbxNotiTerceroActores.setVisible(false);
            panelTerceroActores.setVisible(false);
        }
        if(tabla5.getRenglones()==0){
            cbxNotiAutoridadesAjenas.setVisible(false);
            panelAutoridadesAjenas.setVisible(false);
        }
        if(tabla6.getRenglones()==0){
            cbxNotiPersonasAjenas.setVisible(false);
            panelPersonasAjenas.setVisible(false);
        }
        
        /*
            Verificar si hay partes notificadas y que tenga partes el expediente
        */
        if(tabla1.getRenglones()==0 && tabla2.getRenglones()==0 && tabla3.getRenglones()==0 && tabla4.getRenglones()==0 && 
                tabla5.getRenglones()==0 && tabla6.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay Partes que notificar", "Aviso", 1);
        }
        
        System.out.println("Int_fun_010");
        
    }
        
    private void validaBusquedaFolio(){
        u.cleanComponent(lblAuto);
        mostrarComponentes();
        if(txtBuscarFolio.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el Folio a buscar", "Alerta", 2);
            txtBuscarFolio.requestFocus();
        }else{
            Integer folio = new Integer(txtBuscarFolio.getText());
            Integer anio = cal.get(Calendar.YEAR);
            if(txtBuscarAnio.getText().length()!=0){
                Integer num = u.verificaInteger(txtBuscarAnio.getText());
                if(num!=null)
                    anio = num;
            }
            if(!blAuto.existeFolioAuto(folio, anio)){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio no existe", "Alerta", 2);
                txtBuscarFolio.requestFocus();
            }else{
                RelacionEntity rel = bl.getRelacion(folio, anio);
                if(rel!=null){
                    cargaInfoAuto(rel);
                }/*else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio ya fue cargado anteriormente", "Alerta", 2);
                    txtBuscarFolio.requestFocus();
                }*/
            }
        }
    }
    
    private void removeNotificaciones(String id_relacion){
        removeNotiTabla(bl.getActoresNoti(id_relacion),tabla1);
        removeNotiTabla2(bl.getAutoridadesNoti(id_relacion),tabla2);
        removeNotiTabla2(bl.getTerceroAutoridadesNoti(id_relacion),tabla3);
        removeNotiTabla(bl.getTerceroActoresNoti(id_relacion),tabla4);
        removeNotiTabla2(bl.getAutoridadesAjenasNoti(id_relacion),tabla5);
        removeNotiTabla(bl.getPersonasAjenasNoti(id_relacion),tabla6);
    }
    
    private void removeNotiTabla(ArrayList array, TablaEspecial tab){
        if(!array.isEmpty() && tab.getRenglones()>0){
            for(Object obj : array){
                String id_parte = obj.toString();
                for(int row=0; row<tab.getRenglones(); row++){
                    if(tab.getElement(5, row).equals(id_parte)){
                        tab.removeRow(row);
                    }
                }
            }
        }
    }
    
    private void removeNotiTabla2(ArrayList array, TablaEspecial tab){
        if(!array.isEmpty() && tab.getRenglones()>0){
            for(Object obj : array){
                String id_parte = obj.toString();
                for(int row=0; row<tab.getRenglones(); row++){
                    if(tab.getElement(6, row).equals(id_parte)){
                        tab.removeRow(row);
                    }
                }
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Inicializar tablas">
    private void incializaTabla0(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        Object []titulos = new Object[] {"Dato","Valor"};
        dm.setColumnIdentifiers(titulos);
        jTable0.setModel(dm);
        tabla0 = new TablaEspecial(jTable0);
        tabla0.setAnchoColumnas(200,600);
    }
    
    private void incializaTabla1(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 2;
            }
        };
        Object []titulos = new Object[] {"Notificar","Fecha de Recibida","Nombre","N. Correo","N. Lista","id_parte"};
        dm.setColumnIdentifiers(titulos);
        jTable1.setModel(dm);
        tabla1 = new TablaEspecial(jTable1);
        tabla1.ocultarColumnas(5);
        tabla1.setAnchoColumnas(60,120,590,60,60,50);
    }
    
    private void incializaTabla2(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 3;
            }
        };
        Object []titulos = new Object[] {"Notificar","Oficio","Fecha de Recibida","Nombre","N. Correo","N. Lista","id_parte"};
        dm.setColumnIdentifiers(titulos);
        jTable2.setModel(dm);
        tabla2 = new TablaEspecial(jTable2);
        tabla2.cellLineWrap(3);
        tabla2.ocultarColumnas(6);
        tabla2.setAnchoColumnas(60,60,120,530,60,60,50);
    }
    
    private void incializaTabla3(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 3;
            }
        };
        Object []titulos = new Object[] {"Notificar","Oficio","Fecha de Recibida","Nombre","N. Correo","N. Lista","id_parte"};
        dm.setColumnIdentifiers(titulos);
        jTable3.setModel(dm);
        tabla3 = new TablaEspecial(jTable3);
        tabla3.cellLineWrap(3);
        tabla3.ocultarColumnas(6);
        tabla3.setAnchoColumnas(60,60,120,530,60,60,50);
    }
    
    private void incializaTabla4(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 2;
            }
        };
        Object []titulos = new Object[] {"Notificar","Fecha de Recibida","Nombre","N. Correo","N. Lista","id_parte"};
        dm.setColumnIdentifiers(titulos);
        jTable4.setModel(dm);
        tabla4 = new TablaEspecial(jTable4);
        tabla4.ocultarColumnas(5);
        tabla4.setAnchoColumnas(60,120,590,60,60,50);
    }
    
    private void incializaTabla5(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 3;
            }
        };
        Object []titulos = new Object[] {"Notificar","Oficio","Fecha de Recibida","Nombre","N. Correo","N. Lista","id_parte"};
        dm.setColumnIdentifiers(titulos);
        jTable5.setModel(dm);
        tabla5 = new TablaEspecial(jTable5);
        tabla5.cellLineWrap(3);
        tabla5.ocultarColumnas(6);
        tabla5.setAnchoColumnas(60,60,120,530,60,60,50);
    }
    
    private void incializaTabla6(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 2;
            }
        };
        Object []titulos = new Object[] {"Notificar","Fecha de Recibida","Nombre","N. Correo","N. Lista","id_parte"};
        dm.setColumnIdentifiers(titulos);
        jTable6.setModel(dm);
        tabla6 = new TablaEspecial(jTable6);
        tabla6.ocultarColumnas(5);
        tabla6.setAnchoColumnas(60,120,590,60,60,50);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Llenar tablas">
    private void llenaTabla(ArrayList array){
        for (Object array1 : array) {
            Object []datos = new Object[6];
            String[] info = (String[]) array1;
            datos[0] = false;
            datos[1] = null;
            datos[2] = info[1];
            datos[3] = false;
            datos[4] = false;
            datos[5] = info[0];
            tabla1.addRow(datos);
        }
        tabla1.crearCheckbox(array.size(),0);
        tabla1.crearDateChooser(array.size(),1);
        tabla1.crearCheckbox(array.size(),3);
        tabla1.crearCheckbox(array.size(),4);
    }
    
    private void llenaTabla2(ArrayList array){
        for (Object array1 : array) {
            Object []datos = new Object[7];
            String[] info = (String[]) array1;
            datos[0] = false;
            datos[1] = "";
            datos[2] = null;
            datos[3] = info[1];
            datos[4] = false;
            datos[5] = false;
            datos[6] = info[0];
            tabla2.addRow(datos);
        }
        tabla2.crearCheckbox(array.size(),0);
        tabla2.crearDateChooser(array.size(),2);
        tabla2.crearCheckbox(array.size(),4);
        tabla2.crearCheckbox(array.size(),5);
    }
    
    private void llenaTabla3(ArrayList array){
        for (Object array1 : array) {
            Object []datos = new Object[7];
            String[] info = (String[]) array1;
            datos[0] = false;
            datos[1] = "";
            datos[2] = null;
            datos[3] = info[1];
            datos[4] = false;
            datos[5] = false;
            datos[6] = info[0];
            tabla3.addRow(datos);
        }
        tabla3.crearCheckbox(array.size(),0);
        tabla3.crearDateChooser(array.size(),2);
        tabla3.crearCheckbox(array.size(),4);
        tabla3.crearCheckbox(array.size(),5);
    }
    
    private void llenaTabla4(ArrayList array){
        for (Object array1 : array) {
            Object []datos = new Object[6];
            String[] info = (String[]) array1;
            datos[0] = false;
            datos[1] = null;
            datos[2] = info[1];
            datos[3] = false;
            datos[4] = false;
            datos[5] = info[0];
            tabla4.addRow(datos);
        }
        tabla4.crearCheckbox(array.size(),0);
        tabla4.crearDateChooser(array.size(),1);
        tabla4.crearCheckbox(array.size(),3);
        tabla4.crearCheckbox(array.size(),4);
    }
    
    private void llenaTabla5(ArrayList array){
        for (Object array1 : array) {
            Object []datos = new Object[7];
            String[] info = (String[]) array1;
            datos[0] = false;
            datos[1] = "";
            datos[2] = null;
            datos[3] = info[1];
            datos[4] = false;
            datos[5] = false;
            datos[6] = info[0];
            tabla5.addRow(datos);
        }
        tabla5.crearCheckbox(array.size(),0);
        tabla5.crearDateChooser(array.size(),2);
        tabla5.crearCheckbox(array.size(),4);
        tabla5.crearCheckbox(array.size(),5);
    }
    
    private void llenaTabla6(ArrayList array){
        for (Object array1 : array) {
            Object []datos = new Object[6];
            String[] info = (String[]) array1;
            datos[0] = false;
            datos[1] = null;
            datos[2] = info[1];
            datos[3] = false;
            datos[4] = false;
            datos[5] = info[0];
            tabla6.addRow(datos);
        }
        tabla6.crearCheckbox(array.size(),0);
        tabla6.crearDateChooser(array.size(),1);
        tabla6.crearCheckbox(array.size(),3);
        tabla6.crearCheckbox(array.size(),4);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Varias Funciones">
    
    private void cargaFechas(){
        u.inicializaCombo(cmbFechas, new ComboFechasCargaEntity(), bl.getFechasCargas());
    }
    
    private void desnotificar(javax.swing.JTable table, TablaEspecial tab, javax.swing.JCheckBox cbx){
        if(tab.getRenglones()>0){
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if(row!=-1 && col!=-1 && col==0){
                if(!Boolean.valueOf(tab.getElement(col, row).toString())){
                    tab.setElement(1, row, null);
                    tab.setElement(3, row, false);
                    tab.setElement(4, row, false);
                    cbx.setSelected(false);
                }else{
                    if(allRowTrueTabla(tab)){
                        cbx.setSelected(true);
                    }
                }
            }
        }
    }
    
    private void desnotificar2(javax.swing.JTable table, TablaEspecial tab, javax.swing.JCheckBox cbx){
        if(tab.getRenglones()>0){
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();
            if(row!=-1 && col!=-1 && col==0){
                if(!Boolean.valueOf(tab.getElement(col, row).toString())){
                    tab.setElement(1, row, "");
                    tab.setElement(2, row, null);
                    tab.setElement(4, row, false);
                    tab.setElement(5, row, false);
                    cbx.setSelected(false);
                }else{
                    if(allRowTrueTabla(tab)){
                        cbx.setSelected(true);
                    }
                }
            }
        }
    }
    
    private void desnotificarAllTabla(TablaEspecial tab){
        for(int row=0; row<tab.getRenglones(); row++){
            tab.setElement(1, row, null);
            tab.setElement(3, row, false);
            tab.setElement(4, row, false);
        }
    }
    
    private void desnotificarAllTabla2(TablaEspecial tab){
        for(int row=0; row<tab.getRenglones(); row++){
            tab.setElement(1, row, "");
            tab.setElement(2, row, null);
            tab.setElement(4, row, false);
            tab.setElement(5, row, false);
        }
    }
    
    private void eventoCbxNotificar(TablaEspecial tab, javax.swing.JCheckBox cbx){
        if(tab.getRenglones()>0){
            if(cbx.isSelected()){
                tab.setValorAllColumn(0, true);
            }else{
                tab.setValorAllColumn(0, false);
                desnotificarAllTabla(tab);
            }
        }
    }
    
    private void eventoCbxNotificar2(TablaEspecial tab, javax.swing.JCheckBox cbx){
        if(tab.getRenglones()>0){
            if(cbx.isSelected()){
                tab.setValorAllColumn(0, true);
            }else{
                tab.setValorAllColumn(0, false);
                desnotificarAllTabla2(tab);
            }
        }
    }
    
    private Boolean allRowTrueTabla(TablaEspecial tab){
        int total = tab.getRenglones();
        for(int i=0; i<total; i++){
            if(!Boolean.valueOf(tab.getElement(0,i).toString())){
                return false;
            }
        }
        return true;
    }
        
    private void eventoBotonFecha(){
        Date date = dateRecibida.getDate();
        if(date!=null){
            if(tabla1.getRenglones()>0) tabla1.setValorAllColumnWithTrue(0, 1, date);
            if(tabla2.getRenglones()>0) tabla2.setValorAllColumnWithTrue(0, 2, date);
            if(tabla3.getRenglones()>0) tabla3.setValorAllColumnWithTrue(0, 2, date);
            if(tabla4.getRenglones()>0) tabla4.setValorAllColumnWithTrue(0, 1, date);
            if(tabla5.getRenglones()>0) tabla5.setValorAllColumnWithTrue(0, 2, date);
            if(tabla6.getRenglones()>0) tabla6.setValorAllColumnWithTrue(0, 1, date);
            //tabla1.setValorAllColumnWithTrue(0, 1, null);
        }
    }
    
    private Boolean validarDatosRenglon(TablaEspecial tab, int row, String nombreTabla, Boolean auto){
        Date fecha = (auto)?(java.util.Date)tab.getElement(2, row):(java.util.Date)tab.getElement(1, row);
        String parte = (auto)?tab.getElement(3, row).toString():tab.getElement(2, row).toString();
        if(fecha!=null){
            if(!blCale.isfechaValida(u.dateCastString(fecha))){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Día inhábil"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+parte, "Aviso", 1);
                return false;
            }
        }else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado la Fecha de Recibida"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+parte, "Aviso", 1);
            return false;
        }
        /*if(auto){
            String numOficio = tab.getElement(1, row).toString();
            if(numOficio.length()==0){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ingreso el Número de Folio"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+parte, "Aviso", 1);
                return false;
            }
        }*/
        return true;
    }
    
    private Boolean validaTabla(TablaEspecial tab, String nombreTabla, Boolean auto){
        if(tab.getRenglones()>0){
            for(int row=0; row<tab.getRenglones(); row++){
                if(Boolean.valueOf(tab.getElement(0, row).toString())){
                    if(!validarDatosRenglon(tab,row,nombreTabla,auto)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private Boolean existeRowWithTrue(TablaEspecial tab){
        if(tab.getRenglones()>0){
            for(int i=0; i<tab.getRenglones(); i++){
                if(Boolean.valueOf(tab.getElement(0,i).toString())){
                    return true;
                }
            }
        }
        return false;
    }
    
    private Boolean validarTablas(){
        if(tabla1.getRenglones()==0 && tabla2.getRenglones()==0 && tabla3.getRenglones()==0 && tabla4.getRenglones()==0 && 
                tabla5.getRenglones()==0 && tabla6.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay registros que notificar", "Aviso", 1);
            return false;
        }else if(!existeRowWithTrue(tabla1) && !existeRowWithTrue(tabla2) && !existeRowWithTrue(tabla3) && !existeRowWithTrue(tabla4) && 
                !existeRowWithTrue(tabla5) && !existeRowWithTrue(tabla6)){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay registros marcados para notificar", "Aviso", 1);
            return false;
        }else if(!validaTabla(tabla1,"ACTORES",false)){
            return false;
        }else if(!validaTabla(tabla2,"AUTORIDADES",true)){
            return false;
        }else if(!validaTabla(tabla3,"TERCEROS AUTORIDADES",true)){
            return false;
        }else if(!validaTabla(tabla4,"TERCEROS ACTORES",false)){
            return false;
        }else if(!validaTabla(tabla5,"AUTORIDADES AJENAS",true)){
            return false;
        }else if(!validaTabla(tabla6,"PERSONAS AJENAS",false)){
            return false;
        }else{
            return true;
        }
    }
    
    private ArrayList obtenerDatosTabla(TablaEspecial tab, int tipo_parte){
        ArrayList array = new ArrayList();
        if(tab.getRenglones()>0){
            for(int row=0; row<tab.getRenglones(); row++){
                if(Boolean.valueOf(tab.getElement(0, row).toString())){
                    CargaNotificacionEntity carga = new CargaNotificacionEntity();
                    carga.setAuto(new RegistroAutoEntity(lblAuto.getText()));
                    carga.setTipo_notificacion("PERSONAL");
                    carga.setOficio("");
                    carga.setFecha_recibida((java.util.Date)tab.getElement(1, row));
                    carga.setCorreo(Boolean.valueOf(tab.getElement(3, row).toString()));
                    carga.setLista(Boolean.valueOf(tab.getElement(4, row).toString()));
                    carga.setId_parte(tab.getElement(5, row).toString());
                    carga.setTipo_parte(tipo_parte);
                    array.add(carga);
                    System.out.println(carga);
                }
            }
        }
        return array;
    }
    
    private ArrayList obtenerDatosTabla2(TablaEspecial tab, int tipo_parte){
        ArrayList array = new ArrayList();
        if(tab.getRenglones()>0){
            for(int row=0; row<tab.getRenglones(); row++){
                if(Boolean.valueOf(tab.getElement(0, row).toString())){
                    CargaNotificacionEntity carga = new CargaNotificacionEntity();
                    carga.setAuto(new RegistroAutoEntity(lblAuto.getText()));
                    carga.setTipo_notificacion("OFICIO");
                    carga.setOficio(tab.getElement(1, row).toString());
                    carga.setFecha_recibida((java.util.Date)tab.getElement(2, row));
                    carga.setCorreo(Boolean.valueOf(tab.getElement(4, row).toString()));
                    carga.setLista(Boolean.valueOf(tab.getElement(5, row).toString()));
                    carga.setId_parte(tab.getElement(6, row).toString());
                    carga.setTipo_parte(tipo_parte);
                    array.add(carga);
                    System.out.println(carga);
                }
            }
        }
        return array;
    }
    
    private void mostrarComponentes(){
        cbxNotiActores.setVisible(true);
        panelActores.setVisible(true);
        cbxNotiAutoridades.setVisible(true);
        panelAutoridades.setVisible(true);
        cbxNotiTerceroAutoridades.setVisible(true);
        panelTerceroAutoridades.setVisible(true);
        cbxNotiTerceroActores.setVisible(true);
        panelTerceroActores.setVisible(true);
        cbxNotiAutoridadesAjenas.setVisible(true);
        panelAutoridadesAjenas.setVisible(true);
        cbxNotiPersonasAjenas.setVisible(true);
        panelPersonasAjenas.setVisible(true);
        
        cbxNotiActores.setSelected(false);
        cbxNotiAutoridades.setSelected(false);
        cbxNotiTerceroAutoridades.setSelected(false);
        cbxNotiTerceroActores.setSelected(false);
        cbxNotiAutoridadesAjenas.setSelected(false);
        cbxNotiPersonasAjenas.setSelected(false);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Mapeo de teclas para los botones">
    public final void mapeoTeclas(){
        ActionMap mapaAccion = this.getActionMap();
        InputMap map = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"accion_Esc"); mapaAccion.put("accion_Esc",Accion_Esc());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2,0),"accion_F2"); mapaAccion.put("accion_F2",Accion_F2());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F4,0),"accion_F4"); mapaAccion.put("accion_F4",Accion_F4());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5,0),"accion_F5"); mapaAccion.put("accion_F5",Accion_F5());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F6,0),"accion_F6"); mapaAccion.put("accion_F6",Accion_F6());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7,0),"accion_F7"); mapaAccion.put("accion_F7",Accion_F7());
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_F12,0),"accion_F12"); mapaAccion.put("accion_F12",Accion_F12());
        
        /////Para cambiar el focus cuando se encuentra en una tabla
        jTable1.getActionMap().put("Accion_tab",Accion_tab());
        jTable1.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        jTable2.getActionMap().put("Accion_tab",Accion_tab());
        jTable2.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        jTable3.getActionMap().put("Accion_tab",Accion_tab());
        jTable3.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        jTable4.getActionMap().put("Accion_tab",Accion_tab());
        jTable4.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        jTable5.getActionMap().put("Accion_tab",Accion_tab());
        jTable5.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        jTable6.getActionMap().put("Accion_tab",Accion_tab());
        jTable6.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        /////
    }
    public AbstractAction Accion_Esc(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F5(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF5();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F7(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF7();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){ botonF12();}};}
    
    //Para cambiar el focus cuando se encuentra en una tabla
    public AbstractAction Accion_tab(){ return new AbstractAction(){@Override public void actionPerformed(ActionEvent e){  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();  
    }};}
    /////
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        u.cleanComponent(lblAuto);
        tabla0.clearTabla();
        tabla1.clearTabla();
        tabla2.clearTabla();
        tabla3.clearTabla();
        tabla4.clearTabla();
        tabla5.clearTabla();
        tabla6.clearTabla();
        mostrarComponentes();
        cargaFechas();
        dateRecibida.setDate(null);
        txtBuscarFolio.setText("");
        txtBuscarAnio.setText(""+cal.get(Calendar.YEAR));
        txtBuscarFolio.requestFocus();
    }
    
    private void botonF2(){
        if(jTable1.isEditing())jTable1.getCellEditor().stopCellEditing();
        if(jTable2.isEditing())jTable2.getCellEditor().stopCellEditing();
        if(jTable3.isEditing())jTable3.getCellEditor().stopCellEditing();
        if(jTable4.isEditing())jTable4.getCellEditor().stopCellEditing();
        if(jTable5.isEditing())jTable5.getCellEditor().stopCellEditing();
        if(jTable6.isEditing())jTable6.getCellEditor().stopCellEditing();
        
        txtBuscarFolio.requestFocus();
        if(lblAuto.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha realizado la búsqueda del Auto", "Aviso", 1);
        }else if(validarTablas()){
            ArrayList actores = obtenerDatosTabla(tabla1,1);
            ArrayList autoridades = obtenerDatosTabla2(tabla2,2);
            ArrayList terceroAutoridades = obtenerDatosTabla2(tabla3,3);
            ArrayList terceroActores = obtenerDatosTabla(tabla4,4);
            ArrayList autoridadesAjenas = obtenerDatosTabla2(tabla5,5);
            ArrayList personasAjenas = obtenerDatosTabla(tabla6,6);
            ErrorEntity error = bl.saveNotificacion(actores,autoridades,terceroAutoridades,terceroActores,autoridadesAjenas,personasAjenas,sesion);
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                botonEsc();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
            }
        }
    }
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){}
    
    private void botonF7(){}
    
    private void botonF12(){
        dispose();
    }
    // </editor-fold>
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        panelActores = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        panelTerceroAutoridades = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        panelAutoridades = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        panelPersonasAjenas = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        panelTerceroActores = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        panelAutoridadesAjenas = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        cbxNotiActores = new javax.swing.JCheckBox();
        cbxNotiAutoridades = new javax.swing.JCheckBox();
        cbxNotiTerceroAutoridades = new javax.swing.JCheckBox();
        cbxNotiTerceroActores = new javax.swing.JCheckBox();
        cbxNotiAutoridadesAjenas = new javax.swing.JCheckBox();
        cbxNotiPersonasAjenas = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        dateRecibida = new com.toedter.calendar.JDateChooser();
        btnNotiActoresA2 = new javax.swing.JButton();
        panelInformacion = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable0 = new javax.swing.JTable();
        panelBuscar = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtBuscarFolio = new javax.swing.JTextField();
        btnBusarFolio = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtBuscarAnio = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        cmbFechas = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF12 = new javax.swing.JButton();
        lblAuto = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Carga de Notificaciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Carga_notificaciones.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelActores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Actores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jTable1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.setRowHeight(30);
        jTable1.setRowSelectionAllowed(false);
        jTable1.getTableHeader().setResizingAllowed(false);
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout panelActoresLayout = new javax.swing.GroupLayout(panelActores);
        panelActores.setLayout(panelActoresLayout);
        panelActoresLayout.setHorizontalGroup(
            panelActoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelActoresLayout.setVerticalGroup(
            panelActoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        panelTerceroAutoridades.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Terceros Autoridades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jTable3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable3.setRowHeight(30);
        jTable3.setRowSelectionAllowed(false);
        jTable3.getTableHeader().setResizingAllowed(false);
        jTable3.getTableHeader().setReorderingAllowed(false);
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable3);

        javax.swing.GroupLayout panelTerceroAutoridadesLayout = new javax.swing.GroupLayout(panelTerceroAutoridades);
        panelTerceroAutoridades.setLayout(panelTerceroAutoridadesLayout);
        panelTerceroAutoridadesLayout.setHorizontalGroup(
            panelTerceroAutoridadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelTerceroAutoridadesLayout.setVerticalGroup(
            panelTerceroAutoridadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        panelAutoridades.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autoridades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N
        panelAutoridades.setPreferredSize(new java.awt.Dimension(464, 300));

        jTable2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable2.setRowHeight(30);
        jTable2.setRowSelectionAllowed(false);
        jTable2.getTableHeader().setResizingAllowed(false);
        jTable2.getTableHeader().setReorderingAllowed(false);
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        javax.swing.GroupLayout panelAutoridadesLayout = new javax.swing.GroupLayout(panelAutoridades);
        panelAutoridades.setLayout(panelAutoridadesLayout);
        panelAutoridadesLayout.setHorizontalGroup(
            panelAutoridadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelAutoridadesLayout.setVerticalGroup(
            panelAutoridadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        panelPersonasAjenas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Personas Ajenas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jTable6.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable6.setRowHeight(30);
        jTable6.setRowSelectionAllowed(false);
        jTable6.getTableHeader().setResizingAllowed(false);
        jTable6.getTableHeader().setReorderingAllowed(false);
        jTable6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable6MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable6);

        javax.swing.GroupLayout panelPersonasAjenasLayout = new javax.swing.GroupLayout(panelPersonasAjenas);
        panelPersonasAjenas.setLayout(panelPersonasAjenasLayout);
        panelPersonasAjenasLayout.setHorizontalGroup(
            panelPersonasAjenasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelPersonasAjenasLayout.setVerticalGroup(
            panelPersonasAjenasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        panelTerceroActores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Terceros Actores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jTable4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable4.setRowHeight(30);
        jTable4.setRowSelectionAllowed(false);
        jTable4.getTableHeader().setResizingAllowed(false);
        jTable4.getTableHeader().setReorderingAllowed(false);
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable4);

        javax.swing.GroupLayout panelTerceroActoresLayout = new javax.swing.GroupLayout(panelTerceroActores);
        panelTerceroActores.setLayout(panelTerceroActoresLayout);
        panelTerceroActoresLayout.setHorizontalGroup(
            panelTerceroActoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelTerceroActoresLayout.setVerticalGroup(
            panelTerceroActoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        panelAutoridadesAjenas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autoridades Ajenas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        jTable5.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable5.setRowHeight(30);
        jTable5.setRowSelectionAllowed(false);
        jTable5.getTableHeader().setResizingAllowed(false);
        jTable5.getTableHeader().setReorderingAllowed(false);
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(jTable5);

        javax.swing.GroupLayout panelAutoridadesAjenasLayout = new javax.swing.GroupLayout(panelAutoridadesAjenas);
        panelAutoridadesAjenas.setLayout(panelAutoridadesAjenasLayout);
        panelAutoridadesAjenasLayout.setHorizontalGroup(
            panelAutoridadesAjenasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 921, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        panelAutoridadesAjenasLayout.setVerticalGroup(
            panelAutoridadesAjenasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        cbxNotiActores.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiActores.setText("Actores");
        cbxNotiActores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiActoresActionPerformed(evt);
            }
        });

        cbxNotiAutoridades.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiAutoridades.setText("Autoridades");
        cbxNotiAutoridades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiAutoridadesActionPerformed(evt);
            }
        });

        cbxNotiTerceroAutoridades.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiTerceroAutoridades.setText("Terceros Autoridades");
        cbxNotiTerceroAutoridades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiTerceroAutoridadesActionPerformed(evt);
            }
        });

        cbxNotiTerceroActores.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiTerceroActores.setText("Terceros Actores");
        cbxNotiTerceroActores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiTerceroActoresActionPerformed(evt);
            }
        });

        cbxNotiAutoridadesAjenas.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiAutoridadesAjenas.setText("Autoridades Ajenas");
        cbxNotiAutoridadesAjenas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiAutoridadesAjenasActionPerformed(evt);
            }
        });

        cbxNotiPersonasAjenas.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiPersonasAjenas.setText("Personas Ajenas");
        cbxNotiPersonasAjenas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiPersonasAjenasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cbxNotiActores, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbxNotiAutoridades, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbxNotiTerceroAutoridades, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbxNotiTerceroActores, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbxNotiAutoridadesAjenas, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cbxNotiPersonasAjenas, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(cbxNotiActores)
                .addGap(0, 0, 0)
                .addComponent(cbxNotiAutoridades)
                .addGap(0, 0, 0)
                .addComponent(cbxNotiTerceroAutoridades)
                .addGap(0, 0, 0)
                .addComponent(cbxNotiTerceroActores)
                .addGap(0, 0, 0)
                .addComponent(cbxNotiAutoridadesAjenas)
                .addGap(0, 0, 0)
                .addComponent(cbxNotiPersonasAjenas))
        );

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Fecha de Recibida");

        dateRecibida.setDateFormatString("dd/MMM/yyyy");
        dateRecibida.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        btnNotiActoresA2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnNotiActoresA2.setText("Aplicar");
        btnNotiActoresA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotiActoresA2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(dateRecibida, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNotiActoresA2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateRecibida, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNotiActoresA2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelActores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTerceroAutoridades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAutoridades, javax.swing.GroupLayout.DEFAULT_SIZE, 933, Short.MAX_VALUE)
                    .addComponent(panelTerceroActores, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAutoridadesAjenas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPersonasAjenas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelActores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAutoridades, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTerceroAutoridades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTerceroActores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAutoridadesAjenas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelPersonasAjenas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane7.setViewportView(jPanel1);

        panelInformacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Folio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelInformacion.setLayout(null);

        jTable0.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTable0.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable0.setRowHeight(30);
        jTable0.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(jTable0);

        panelInformacion.add(jScrollPane9);
        jScrollPane9.setBounds(12, 20, 820, 120);

        panelBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda por Folio", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panelBuscar.setLayout(null);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Folio");
        panelBuscar.add(jLabel20);
        jLabel20.setBounds(10, 20, 40, 30);

        txtBuscarFolio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarFolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarFolio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarFolioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtBuscarFolio);
        txtBuscarFolio.setBounds(50, 20, 80, 30);

        btnBusarFolio.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBusarFolio.setText("Buscar Folio");
        btnBusarFolio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusarFolioActionPerformed(evt);
            }
        });
        panelBuscar.add(btnBusarFolio);
        btnBusarFolio.setBounds(240, 20, 120, 30);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel27.setText("Año");
        panelBuscar.add(jLabel27);
        jLabel27.setBounds(140, 20, 40, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarAnioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(170, 20, 60, 30);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Folios");
        panelBuscar.add(jLabel28);
        jLabel28.setBounds(410, 20, 40, 30);

        cmbFechas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbFechas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelBuscar.add(cmbFechas);
        cmbFechas.setBounds(450, 20, 230, 30);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Seleccionar Folio");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelBuscar.add(jButton1);
        jButton1.setBounds(690, 20, 140, 30);

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("<html><center>F2 - Guardar<br>Notificaciones</center></html>");
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        btnEsc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnEsc.setText("Esc - Cancelar");
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        lblAuto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 992, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
                            .addComponent(panelInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(49, 49, 49))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnF2)
                                    .addComponent(btnF12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEsc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE))
        );

        setBounds(0, 0, 1008, 720);
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarFolioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarFolioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            validaBusquedaFolio();
    }//GEN-LAST:event_txtBuscarFolioKeyPressed

    private void txtBuscarAnioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAnioKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            validaBusquedaFolio();
    }//GEN-LAST:event_txtBuscarAnioKeyPressed

    private void cbxNotiActoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNotiActoresActionPerformed
        eventoCbxNotificar(tabla1,cbxNotiActores);
    }//GEN-LAST:event_cbxNotiActoresActionPerformed

    private void btnNotiActoresA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotiActoresA2ActionPerformed
        eventoBotonFecha();
    }//GEN-LAST:event_btnNotiActoresA2ActionPerformed

    private void cbxNotiAutoridadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNotiAutoridadesActionPerformed
        eventoCbxNotificar2(tabla2,cbxNotiAutoridades);
    }//GEN-LAST:event_cbxNotiAutoridadesActionPerformed

    private void cbxNotiTerceroAutoridadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNotiTerceroAutoridadesActionPerformed
        eventoCbxNotificar2(tabla3,cbxNotiTerceroAutoridades);
    }//GEN-LAST:event_cbxNotiTerceroAutoridadesActionPerformed

    private void cbxNotiTerceroActoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNotiTerceroActoresActionPerformed
        eventoCbxNotificar(tabla4,cbxNotiTerceroActores);
    }//GEN-LAST:event_cbxNotiTerceroActoresActionPerformed

    private void cbxNotiAutoridadesAjenasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNotiAutoridadesAjenasActionPerformed
        eventoCbxNotificar2(tabla5,cbxNotiAutoridadesAjenas);
    }//GEN-LAST:event_cbxNotiAutoridadesAjenasActionPerformed

    private void cbxNotiPersonasAjenasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNotiPersonasAjenasActionPerformed
        eventoCbxNotificar(tabla6,cbxNotiPersonasAjenas);
    }//GEN-LAST:event_cbxNotiPersonasAjenasActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        desnotificar(jTable1, tabla1, cbxNotiActores);
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        desnotificar2(jTable2, tabla2, cbxNotiAutoridades);
    }//GEN-LAST:event_jTable2MouseClicked

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        desnotificar2(jTable3, tabla3, cbxNotiTerceroAutoridades);
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked
        desnotificar(jTable4, tabla4, cbxNotiTerceroActores);
    }//GEN-LAST:event_jTable4MouseClicked

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        desnotificar2(jTable5, tabla5, cbxNotiAutoridadesAjenas);
    }//GEN-LAST:event_jTable5MouseClicked

    private void jTable6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable6MouseClicked
        desnotificar(jTable6, tabla6, cbxNotiPersonasAjenas);
    }//GEN-LAST:event_jTable6MouseClicked

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF2();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnBusarFolioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusarFolioActionPerformed
        validaBusquedaFolio();
    }//GEN-LAST:event_btnBusarFolioActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if(cmbFechas.getItemCount()>1 && cmbFechas.getSelectedIndex()!=0){
            u.cleanComponent(lblAuto);
            mostrarComponentes();
            ComboFechasCargaEntity cmb = (ComboFechasCargaEntity)cmbFechas.getSelectedItem();
            Integer folio = new Integer(cmb.getFolio());
            Integer anio = new Integer(cmb.getAnio());
            RelacionEntity rel = bl.getRelacion(folio, anio);
            if(rel!=null){
                tabla0.clearTabla();
                cargaInfoAuto(rel);
            }            
        }
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBusarFolio;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JButton btnNotiActoresA2;
    private javax.swing.JCheckBox cbxNotiActores;
    private javax.swing.JCheckBox cbxNotiAutoridades;
    private javax.swing.JCheckBox cbxNotiAutoridadesAjenas;
    private javax.swing.JCheckBox cbxNotiPersonasAjenas;
    private javax.swing.JCheckBox cbxNotiTerceroActores;
    private javax.swing.JCheckBox cbxNotiTerceroAutoridades;
    private javax.swing.JComboBox cmbFechas;
    private com.toedter.calendar.JDateChooser dateRecibida;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable0;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JPanel panelActores;
    private javax.swing.JPanel panelAutoridades;
    private javax.swing.JPanel panelAutoridadesAjenas;
    private javax.swing.JPanel panelBuscar;
    private javax.swing.JPanel panelInformacion;
    private javax.swing.JPanel panelPersonasAjenas;
    private javax.swing.JPanel panelTerceroActores;
    private javax.swing.JPanel panelTerceroAutoridades;
    private javax.swing.JTextField txtBuscarAnio;
    private javax.swing.JTextField txtBuscarFolio;
    // End of variables declaration//GEN-END:variables
}
