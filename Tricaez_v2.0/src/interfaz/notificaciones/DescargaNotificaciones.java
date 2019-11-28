package interfaz.notificaciones;

import baseSistema.TablaEspecial;
import baseGeneral.ErrorEntity;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.catalogos.DestinosBl;
import bl.notificaciones.DescargaNotificacionesBl;
import bl.operaciones.RegistroAutosBl;
import bl.utilidades.CalendarioOficialBl;
import entitys.CargaNotificacionEntity;
import entitys.ComboAutoEntity;
import entitys.ComboFechaEntity;
import entitys.ComboFechasCargaEntity;
import entitys.DescargaNotificacionEntity;
import entitys.DestinoEntity;
import entitys.MunicipioEntity;
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
public class DescargaNotificaciones extends javax.swing.JInternalFrame {

    DescargaNotificacionesBl bl = new DescargaNotificacionesBl();
    SesionEntity sesion;
    Utilidades u = new Utilidades();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    DestinosBl blDestino = new DestinosBl();
    RegistroAutosBl blAuto = new RegistroAutosBl();
    
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    TablaEspecial tabla1, tabla2, tabla3, tabla4, tabla5, tabla6, tabla0;
    
    /**
     * Creates new form CargaNotificaciones
     * @param sesion
     */
    public DescargaNotificaciones(SesionEntity sesion) {
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
        u.inicializaCombo(cmbDestino, new DestinoEntity(), blDestino.getDestinos());
        u.formatoJDateChooser(dateDestino);
        dateDestino.setDate(cal.getTime());
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
            arrayDatos = bl.getAuto(rel.getId_registro_autos());
            expediente = bl.getAutoExpediente(rel.getId_registro_autos());
        }else if(rel.getId_reg_resolucion()!=null){
            arrayDatos = bl.getResolucion(rel.getId_reg_resolucion());
            expediente = bl.getResolucionExpediente(rel.getId_reg_resolucion());
        }else if(rel.getId_reg_sentencia()!=null){
            arrayDatos = bl.getSentencia(rel.getId_reg_sentencia());
            expediente = bl.getSentenciaExpediente(rel.getId_reg_sentencia());
        }
        
        for(Object array1 : arrayDatos){            
            tabla0.addRow((Object[])array1);
        }
        //Falta datos de carga
        
        String []mun = bl.getMunicipios();
        ArrayList array = bl.getActores(rel.getId_relacion());
        if(array.size()>0)
            llenaTabla(array,mun);
        ArrayList array2 = bl.getAutoridades(rel.getId_relacion());
        if(array2.size()>0)
            llenaTabla2(array2,mun);
        ArrayList array3 = bl.getTerceroAutoridades(rel.getId_relacion());
        if(array3.size()>0)
            llenaTabla3(array3,mun);
        ArrayList array4 = bl.getTerceroActores(rel.getId_relacion());
        if(array4.size()>0)
            llenaTabla4(array4,mun);
        ArrayList array5 = bl.getAutoridadesAjenas(rel.getId_relacion());
        if(array5.size()>0)
            llenaTabla5(array5,mun);
        ArrayList array6 = bl.getPersonasAjenas(rel.getId_relacion());
        if(array6.size()>0)
            llenaTabla6(array6,mun);
        
        removeNotificaciones(rel.getId_relacion());
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
        
        if(tabla1.getRenglones()==0 && tabla2.getRenglones()==0 && tabla3.getRenglones()==0 && tabla4.getRenglones()==0 && 
                tabla5.getRenglones()==0 && tabla6.getRenglones()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No hay Partes que notificar", "Aviso", 1);
        }
        
    }
    
    private void validaBusquedaFolio(){
        u.cleanComponent(lblAuto);
        u.inicializaCombo(cmbDestino, new DestinoEntity(), blDestino.getDestinos());
        mostrarComponentes();
        dateDestino.setDate(cal.getTime());
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
                RelacionEntity reg = bl.getRelacion(folio, anio);
                if(reg!=null){
                    cargaInfoAuto(reg);
                }else{
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"El Folio ya fue cargado anteriormente", "Alerta", 2);
                    txtBuscarFolio.requestFocus();
                }
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
                    if(tab.getElement(10, row).toString().equals(id_parte)){
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
                    if(tab.getElement(11, row).toString().equals(id_parte)){
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
                return col != 1;
            }
        };
        Object []titulos = new Object[] {"Notificar","Nombre","T. Notificación","F. Notificacion","F. Recepción",//0-4
            "F.Depósito","No. Identificación","Municipio","Observaciones","id_municipio","id_parte","id_carga"};//5-11
        dm.setColumnIdentifiers(titulos);
        jTable1.setModel(dm);
        tabla1 = new TablaEspecial(jTable1);
        tabla1.cellLineWrap(1);
        tabla1.cellLineWrap(8);
        tabla1.alinearCentro(6);
        tabla1.ocultarColumnas(9,10,11);
        tabla1.setAnchoColumnas(55,190,90,100,100,100,100,200,300,50,50,50);
    }
    
    private void incializaTabla2(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 1;
            }
        };
        Object []titulos = new Object[] {"Notificar","Nombre","T. Notificación","Oficio","F. Notificacion","F. Recepción",//0-5
            "F.Depósito","No. Identificación","Municipio","Observaciones","id_municipio","id_parte","id_carga"};//6-12
        dm.setColumnIdentifiers(titulos);
        jTable2.setModel(dm);
        tabla2 = new TablaEspecial(jTable2);
        tabla2.cellLineWrap(1);
        tabla2.cellLineWrap(9);
        tabla2.alinearCentro(3);
        tabla2.alinearCentro(7);
        tabla2.ocultarColumnas(10,11,12);
        tabla2.setAnchoColumnas(55,320,90,70,100,100,100,100,200,300,50,50,50);
    }
    
    private void incializaTabla3(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 1;
            }
        };
        Object []titulos = new Object[] {"Notificar","Nombre","T. Notificación","Oficio","F. Notificacion","F. Recepción",//0-5
            "F.Depósito","No. Identificación","Municipio","Observaciones","id_municipio","id_parte","id_carga"};//6-12
        dm.setColumnIdentifiers(titulos);
        jTable3.setModel(dm);
        tabla3 = new TablaEspecial(jTable3);
        tabla3.cellLineWrap(1);
        tabla3.cellLineWrap(9);
        tabla3.alinearCentro(3);
        tabla3.alinearCentro(7);
        tabla3.ocultarColumnas(10,11,12);
        tabla3.setAnchoColumnas(55,320,90,70,100,100,100,100,200,300,50,50,50);
    }
    
    private void incializaTabla4(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 1;
            }
        };
        Object []titulos = new Object[] {"Notificar","Nombre","T. Notificación","F. Notificacion","F. Recepción",//0-4
            "F.Depósito","No. Identificación","Municipio","Observaciones","id_municipio","id_parte","id_carga"};//5-11
        dm.setColumnIdentifiers(titulos);
        jTable4.setModel(dm);
        tabla4 = new TablaEspecial(jTable4);
        tabla4.cellLineWrap(1);
        tabla4.cellLineWrap(8);
        tabla4.alinearCentro(6);
        tabla4.ocultarColumnas(9,10,11);
        tabla4.setAnchoColumnas(55,190,90,100,100,100,100,200,300,50,50,50);
    }
    
    private void incializaTabla5(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 1;
            }
        };
        Object []titulos = new Object[] {"Notificar","Nombre","T. Notificación","Oficio","F. Notificacion","F. Recepción",//0-5
            "F.Depósito","No. Identificación","Municipio","Observaciones","id_municipio","id_parte","id_carga"};//6-12
        dm.setColumnIdentifiers(titulos);
        jTable5.setModel(dm);
        tabla5 = new TablaEspecial(jTable5);
        tabla5.cellLineWrap(1);
        tabla5.cellLineWrap(9);
        tabla5.alinearCentro(3);
        tabla5.alinearCentro(7);
        tabla5.ocultarColumnas(10,11,12);
        tabla5.setAnchoColumnas(55,320,90,70,100,100,100,100,200,300,50,50,50);
    }
    
    private void incializaTabla6(){
        DefaultTableModel dm = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int col) {
                return col != 1;
            }
        };
        Object []titulos = new Object[] {"Notificar","Nombre","T. Notificación","F. Notificacion","F. Recepción",//0-4
            "F.Depósito","No. Identificación","Municipio","Observaciones","id_municipio","id_parte","id_carga"};//5-11
        dm.setColumnIdentifiers(titulos);
        jTable6.setModel(dm);
        tabla6 = new TablaEspecial(jTable6);
        tabla6.cellLineWrap(1);
        tabla6.cellLineWrap(8);
        tabla6.alinearCentro(6);
        tabla6.ocultarColumnas(9,10,11);
        tabla6.setAnchoColumnas(55,190,90,100,100,100,100,200,300,50,50,50);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Llenar tablas">
    private void llenaTabla(ArrayList array, String []mun){
        for (Object array1 : array) {
            Object []datos = new Object[12];
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            datos[0] = false;
            datos[1] = carga.getNombre();
            datos[2] = carga.getTipo_notificacion();
            datos[3] = null;
            datos[4] = null;
            datos[5] = null;
            datos[6] = "";
            datos[7] = "[Elija un municipio]";
            datos[8] = "";
            datos[9] = "-1";
            datos[10] = carga.getId_parte();
            datos[11] = carga.getId_carga();
            tabla1.addRow(datos);
        }
        tabla1.crearComboBox(array.size(), 2, new String[]{"[Elija una opción]","PERSONAL","CORREO","LISTA"});
        tabla1.crearDateChooserChica(array.size(),3);
        tabla1.crearDateChooserChica(array.size(),4);
        tabla1.crearDateChooserChica(array.size(),5);
        tabla1.crearComboBox(mun.length, 7, mun);
        tabla1.crearCheckbox(array.size(),0);
    }
    
    private void llenaTabla2(ArrayList array, String []mun){
        for (Object array1 : array) {
            Object []datos = new Object[13];
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            datos[0] = false;
            datos[1] = carga.getNombre();
            datos[2] = carga.getTipo_notificacion();
            datos[3] = carga.getOficio();
            datos[4] = null;
            datos[5] = null;
            datos[6] = null;
            datos[7] = "";
            datos[8] = carga.getMun().getNombre();
            datos[9] = "";
            datos[10] = carga.getMun().getId_municipio().toString();
            datos[11] = carga.getId_parte();
            datos[12] = carga.getId_carga();
            tabla2.addRow(datos);
        }
        tabla2.crearComboBox(array.size(), 2, new String[]{"[Elija una opción]","OFICIO","CORREO","LISTA"});
        tabla2.crearDateChooserChica(array.size(),4);
        tabla2.crearDateChooserChica(array.size(),5);
        tabla2.crearDateChooserChica(array.size(),6);
        tabla2.crearComboBox(mun.length, 8, mun);
        tabla2.crearCheckbox(array.size(),0);
    }
    
    private void llenaTabla3(ArrayList array, String []mun){
        for (Object array1 : array) {
            Object []datos = new Object[13];
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            datos[0] = false;
            datos[1] = carga.getNombre();
            datos[2] = carga.getTipo_notificacion();
            datos[3] = carga.getOficio();
            datos[4] = null;
            datos[5] = null;
            datos[6] = null;
            datos[7] = "";
            datos[8] = carga.getMun().getNombre();
            datos[9] = "";
            datos[10] = carga.getMun().getId_municipio().toString();
            datos[11] = carga.getId_parte();
            datos[12] = carga.getId_carga();
            tabla3.addRow(datos);
        }
        tabla3.crearComboBox(array.size(), 2, new String[]{"[Elija una opción]","OFICIO","CORREO","LISTA"});
        tabla3.crearDateChooserChica(array.size(),4);
        tabla3.crearDateChooserChica(array.size(),5);
        tabla3.crearDateChooserChica(array.size(),6);
        tabla3.crearComboBox(mun.length, 8, mun);
        tabla3.crearCheckbox(array.size(),0);
    }
    
    private void llenaTabla4(ArrayList array, String []mun){
        for (Object array1 : array) {
            Object []datos = new Object[12];
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            datos[0] = false;
            datos[1] = carga.getNombre();
            datos[2] = carga.getTipo_notificacion();
            datos[3] = null;
            datos[4] = null;
            datos[5] = null;
            datos[6] = "";
            datos[7] = "[Elija un municipio]";
            datos[8] = "";
            datos[9] = "-1";
            datos[10] = carga.getId_parte();
            datos[11] = carga.getId_carga();
            tabla4.addRow(datos);
        }
        tabla4.crearComboBox(array.size(), 2, new String[]{"[Elija una opción]","PERSONAL","CORREO","LISTA"});
        tabla4.crearDateChooserChica(array.size(),3);
        tabla4.crearDateChooserChica(array.size(),4);
        tabla4.crearDateChooserChica(array.size(),5);
        tabla4.crearComboBox(mun.length, 7, mun);
        tabla4.crearCheckbox(array.size(),0);
    }
    
    private void llenaTabla5(ArrayList array, String []mun){
        for (Object array1 : array) {
            Object []datos = new Object[13];
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            datos[0] = false;
            datos[1] = carga.getNombre();
            datos[2] = carga.getTipo_notificacion();
            datos[3] = carga.getOficio();
            datos[4] = null;
            datos[5] = null;
            datos[6] = null;
            datos[7] = "";
            datos[8] = carga.getMun().getNombre();
            datos[9] = "";
            datos[10] = carga.getMun().getId_municipio().toString();
            datos[11] = carga.getId_parte();
            datos[12] = carga.getId_carga();
            tabla5.addRow(datos);
        }
        tabla5.crearComboBox(array.size(), 2, new String[]{"[Elija una opción]","OFICIO","CORREO","LISTA"});
        tabla5.crearDateChooserChica(array.size(),4);
        tabla5.crearDateChooserChica(array.size(),5);
        tabla5.crearDateChooserChica(array.size(),6);
        tabla5.crearComboBox(mun.length, 8, mun);
        tabla5.crearCheckbox(array.size(),0);
    }
    
    private void llenaTabla6(ArrayList array, String []mun){
        for (Object array1 : array) {
            Object []datos = new Object[12];
            CargaNotificacionEntity carga = (CargaNotificacionEntity) array1;
            datos[0] = false;
            datos[1] = carga.getNombre();
            datos[2] = carga.getTipo_notificacion();
            datos[3] = null;
            datos[4] = null;
            datos[5] = null;
            datos[6] = "";
            datos[7] = "[Elija un municipio]";
            datos[8] = "";
            datos[9] = "-1";
            datos[10] = carga.getId_parte();
            datos[11] = carga.getId_carga();
            tabla6.addRow(datos);
        }
        tabla6.crearComboBox(array.size(), 2, new String[]{"[Elija una opción]","PERSONAL","CORREO","LISTA"});
        tabla6.crearDateChooserChica(array.size(),3);
        tabla6.crearDateChooserChica(array.size(),4);
        tabla6.crearDateChooserChica(array.size(),5);
        tabla6.crearComboBox(mun.length, 7, mun);
        tabla6.crearCheckbox(array.size(),0);
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
                    /*tab.setElement(2, row, "[Elija una opción]");
                    tab.setElement(3, row, null);
                    tab.setElement(4, row, null);
                    tab.setElement(5, row, null);
                    tab.setElement(6, row, "");
                    tab.setElement(7, row, "[Elija un municipio]");
                    tab.setElement(8, row, "");
                    tab.setElement(9, row, "-1");*/
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
                    /*tab.setElement(2, row, "[Elija una opción]");
                    tab.setElement(4, row, null);
                    tab.setElement(5, row, null);
                    tab.setElement(6, row, null);
                    tab.setElement(7, row, "");
                    tab.setElement(8, row, "[Elija un municipio]");
                    tab.setElement(9, row, "");
                    tab.setElement(10, row, "-1");*/
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
            tab.setElement(2, row, "[Elija una opción]");
            tab.setElement(3, row, null);
            tab.setElement(4, row, null);
            tab.setElement(5, row, null);
            tab.setElement(6, row, "");
            tab.setElement(7, row, "[Elija un municipio]");
            tab.setElement(8, row, "");
            tab.setElement(9, row, "-1");
        }
    }
    
    private void desnotificarAllTabla2(TablaEspecial tab){
        for(int row=0; row<tab.getRenglones(); row++){
            tab.setElement(2, row, "[Elija una opción]");
            tab.setElement(4, row, null);
            tab.setElement(5, row, null);
            tab.setElement(6, row, null);
            tab.setElement(7, row, "");
            tab.setElement(8, row, "[Elija un municipio]");
            tab.setElement(9, row, "");
            tab.setElement(10, row, "-1");
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
        
    private void eventoBotonFechaDeposito(){
        Date date = dateDeposito.getDate();
        if(date!=null){
            if(tabla1.getRenglones()>0) tabla1.setValorAllColumnWithTrue(0, 3, date);
            if(tabla2.getRenglones()>0) tabla2.setValorAllColumnWithTrue(0, 4, date);
            if(tabla3.getRenglones()>0) tabla3.setValorAllColumnWithTrue(0, 4, date);
            if(tabla4.getRenglones()>0) tabla4.setValorAllColumnWithTrue(0, 3, date);
            if(tabla5.getRenglones()>0) tabla5.setValorAllColumnWithTrue(0, 4, date);
            if(tabla6.getRenglones()>0) tabla6.setValorAllColumnWithTrue(0, 3, date);
        }
    }
    
    private void eventoBotonFechaRecepcion(){
        Date date = dateRecepcion.getDate();
        if(date!=null){
            if(tabla1.getRenglones()>0) tabla1.setValorAllColumnWithTrue(0, 4, date);
            if(tabla2.getRenglones()>0) tabla2.setValorAllColumnWithTrue(0, 5, date);
            if(tabla3.getRenglones()>0) tabla3.setValorAllColumnWithTrue(0, 5, date);
            if(tabla4.getRenglones()>0) tabla4.setValorAllColumnWithTrue(0, 4, date);
            if(tabla5.getRenglones()>0) tabla5.setValorAllColumnWithTrue(0, 5, date);
            if(tabla6.getRenglones()>0) tabla6.setValorAllColumnWithTrue(0, 4, date);
        }
    }
    
    private void eventoBotonFechaNotificacion(){
        Date date = dateNotificacion.getDate();
        if(date!=null){
            if(tabla1.getRenglones()>0) tabla1.setValorAllColumnWithTrue(0, 5, date);
            if(tabla2.getRenglones()>0) tabla2.setValorAllColumnWithTrue(0, 6, date);
            if(tabla3.getRenglones()>0) tabla3.setValorAllColumnWithTrue(0, 6, date);
            if(tabla4.getRenglones()>0) tabla4.setValorAllColumnWithTrue(0, 5, date);
            if(tabla5.getRenglones()>0) tabla5.setValorAllColumnWithTrue(0, 6, date);
            if(tabla6.getRenglones()>0) tabla6.setValorAllColumnWithTrue(0, 5, date);
        }
    }
    
    private Boolean validarDatosRenglon(TablaEspecial tab, int row, String nombreTabla){
        String opcion = tab.getElement(2, row).toString();
        System.out.println("llego1");
        if(!opcion.equals("PERSONAL")&&!opcion.equals("CORREO")&&!opcion.equals("LISTA")){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Tipo de notificación incorrecta"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
            return false;
        }else if(!validaFechaColumnaRenglonNotificacion(tab,3,row,nombreTabla,"Fecha de Notificación")){
            return false;
        }else if(!validaFechaColumnaRenglon(tab,5,row,nombreTabla,"Fecha de Depósito")){
            return false;
        }else if(!validaFechaColumnaRenglon(tab,4,row,nombreTabla,"Fecha de Recepción")){
            return false;
        }else if(!validaMunicipio(tab.getElement(7, row).toString())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Municipio incorrecto"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
            return false;
        }else{
            return true;
        }
    }
    
    private Boolean validarDatosRenglon2(TablaEspecial tab, int row, String nombreTabla){
        String opcion = tab.getElement(2, row).toString();
        if(!opcion.equals("OFICIO")&&!opcion.equals("CORREO")&&!opcion.equals("LISTA")){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Tipo de notificación incorrecta"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
            return false;
        }/*else if(opcion.equals("OFICIO")&&tab.getElement(3, row).toString().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No ha ingresado el Número de Oficio"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
            return false;
        }*/
        else if(!validaFechaColumnaRenglonNotificacion(tab,4,row,nombreTabla,"Fecha de Notificación")){
            System.out.println("llego3");
            return false;
        }else if(!validaFechaColumnaRenglon(tab,6,row,nombreTabla,"Fecha de Depósito")){
            return false;
        }else if(!validaFechaColumnaRenglon(tab,5,row,nombreTabla,"Fecha de Recepción")){
            return false;
        }else if(!validaMunicipio(tab.getElement(8, row).toString())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Municipio incorrecto"
                    + ".\nTabla: "+nombreTabla
                    + ".\nNo. de Renglon: "+(row+1)
                    + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
            return false;
        }else{
            return true;
        }
    }
    
    private Boolean validaFechaColumnaRenglon(TablaEspecial tab, int col, int row, String nombreTabla, String nombreColumna){
        Date fecha = (java.util.Date)tab.getElement(col, row);
        if(fecha == null){
            return true;
        }else{
            if(!blCale.isfechaValida(u.dateCastString(fecha))){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Día inhábil"
                + ".\nTabla: "+nombreTabla
                + ".\nNo. de Renglon: "+(row+1)
                + ".\nColumna: "+nombreColumna
                + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
                return false;
            }else{
                return true;
            }           
        }
    }
    
    private Boolean validaFechaColumnaRenglonNotificacion(TablaEspecial tab, int col, int row, String nombreTabla, String nombreColumna){
        Date fecha = (java.util.Date)tab.getElement(col, row);
        if(fecha == null){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado la Fecha de Notificacion "
                    + ".\nTabla: "+nombreTabla
                + ".\nNo. de Renglon: "+(row+1)
                + ".\nColumna: "+nombreColumna
                + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
            return false;
        }else{
            if(!blCale.isfechaValida(u.dateCastString(fecha))){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Día inhábil"
                + ".\nTabla: "+nombreTabla
                + ".\nNo. de Renglon: "+(row+1)
                + ".\nColumna: "+nombreColumna
                + ".\nNombre de la Parte: "+tab.getElement(1,row), "Aviso", 1);
                return false;
            }else{
                return true;
            }           
        }
    }
    
    private Boolean validaMunicipio(String buscar){
        String []muni = bl.getMunicipios();
        for(String mun : muni){
            if(mun.equals(buscar))
                return true;
        }
        return false;
    }
    
    private Boolean validaTabla(TablaEspecial tab, String nombreTabla){
        if(tab.getRenglones()>0){
            for(int row=0; row<tab.getRenglones(); row++){
                if(Boolean.valueOf(tab.getElement(0, row).toString())){
                    if(!validarDatosRenglon(tab,row,nombreTabla)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private Boolean validaTabla2(TablaEspecial tab, String nombreTabla){
        if(tab.getRenglones()>0){
            for(int row=0; row<tab.getRenglones(); row++){
                if(Boolean.valueOf(tab.getElement(0, row).toString())){
                    if(!validarDatosRenglon2(tab,row,nombreTabla)){
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
        }else if(!validaTabla(tabla1,"ACTORES")){
            return false;
        }else if(!validaTabla2(tabla2,"AUTORIDADES")){
            return false;
        }else if(!validaTabla2(tabla3,"TERCEROS AUTORIDADES")){
            return false;
        }else if(!validaTabla(tabla4,"TERCEROS ACTORES")){
            return false;
        }else if(!validaTabla2(tabla5,"AUTORIDADES AJENAS")){
            return false;
        }else if(!validaTabla(tabla6,"PERSONAS AJENAS")){
            return false;
        }else{
            return true;
        }
    }
    
    private int getIdMunicipio(String nombre){
        String []muni = bl.getMunicipios();
        for(int i=1; i<muni.length; i++){
            if(muni[i].equals(nombre)){
                return i;
            }
        }
        return -1;
    }
    
    private ArrayList obtenerDatosTabla(TablaEspecial tab, int tipo_parte){
        ArrayList array = new ArrayList();
        if(tab.getRenglones()>0){
            for(int row=0; row<tab.getRenglones(); row++){
                if(Boolean.valueOf(tab.getElement(0, row).toString())){
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();
                    descarga.setId_carga(tab.getElement(11, row).toString());
                    descarga.setId_relacion(lblAuto.getText());
                    descarga.setId_parte(tab.getElement(10, row).toString());
                    descarga.setTipo_parte(tipo_parte);
                    descarga.setTipo_notificacion(tab.getElement(2, row).toString());
                    descarga.setMun(null);
                    int id_mun = getIdMunicipio(tab.getElement(7, row).toString());
                    if(id_mun != -1){
                        descarga.setMun(new MunicipioEntity(id_mun));
                    }
                    descarga.setFecha_notificacion(null);
                    if((java.util.Date)tab.getElement(3, row)!=null){
                        descarga.setFecha_notificacion((java.util.Date)tab.getElement(3, row));
                    }
                    descarga.setFecha_deposito(null);
                    if((java.util.Date)tab.getElement(5, row)!=null){
                        descarga.setFecha_deposito((java.util.Date)tab.getElement(5, row));
                    }
                    descarga.setFecha_recepcion(null);
                    if((java.util.Date)tab.getElement(4, row)!=null){
                        descarga.setFecha_recepcion((java.util.Date)tab.getElement(4, row));
                    }
                    descarga.setSpm(tab.getElement(6, row).toString());
                    descarga.setObservaciones(tab.getElement(8, row).toString());
                    array.add(descarga);
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
                    DescargaNotificacionEntity descarga = new DescargaNotificacionEntity();                   
                    descarga.setId_carga(tab.getElement(12, row).toString());
                    descarga.setId_relacion(lblAuto.getText());
                    descarga.setId_parte(tab.getElement(11, row).toString());
                    descarga.setTipo_parte(tipo_parte);
                    descarga.setTipo_notificacion(tab.getElement(2, row).toString());
                    descarga.setMun(null);
                    int id_mun = getIdMunicipio(tab.getElement(8, row).toString());
                    if(id_mun != -1){
                        descarga.setMun(new MunicipioEntity(id_mun));
                    }
                    descarga.setOficio(tab.getElement(3, row).toString());
                    descarga.setFecha_notificacion(null);
                    if((java.util.Date)tab.getElement(4, row)!=null){
                        descarga.setFecha_notificacion((java.util.Date)tab.getElement(4, row));
                    }
                    descarga.setFecha_deposito(null);
                    if((java.util.Date)tab.getElement(6, row)!=null){
                        descarga.setFecha_deposito((java.util.Date)tab.getElement(6, row));
                    }
                    descarga.setFecha_recepcion(null);
                    if((java.util.Date)tab.getElement(5, row)!=null){
                        descarga.setFecha_recepcion((java.util.Date)tab.getElement(5, row));
                    }
                    descarga.setSpm(tab.getElement(7, row).toString());
                    descarga.setObservaciones(tab.getElement(9, row).toString());
                    array.add(descarga);
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
        u.inicializaCombo(cmbDestino, new DestinoEntity(), blDestino.getDestinos());
        //u.formatoJDateChooser(dateDestino);
        dateDestino.setDate(cal.getTime());
        tabla0.clearTabla();
        tabla1.clearTabla();
        tabla2.clearTabla();
        tabla3.clearTabla();
        tabla4.clearTabla();
        tabla5.clearTabla();
        tabla6.clearTabla();
        mostrarComponentes();
        txtBuscarFolio.setText("");
        txtBuscarAnio.setText(""+cal.get(Calendar.YEAR));
        txtBuscarFolio.requestFocus();
        cargaFechas();
    }
    
    private void botonF2(){
        if(jTable1.isEditing())jTable1.getCellEditor().stopCellEditing();
        if(jTable2.isEditing())jTable2.getCellEditor().stopCellEditing();
        if(jTable3.isEditing())jTable3.getCellEditor().stopCellEditing();
        if(jTable4.isEditing())jTable4.getCellEditor().stopCellEditing();
        if(jTable5.isEditing())jTable5.getCellEditor().stopCellEditing();
        if(jTable6.isEditing())jTable6.getCellEditor().stopCellEditing();

        if(lblAuto.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha realizado la búsqueda del Auto", "Aviso", 1);
            txtBuscarFolio.requestFocus();
        }else if(cmbDestino.getItemCount()>0 && cmbDestino.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Destino", "Aviso", 1);
            cmbDestino.requestFocus();
        }else if(!blCale.isfechaValida(u.dateCastString(dateDestino.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Fecha de entrega al Destino es inhábil", "Aviso", 1);
            cmbDestino.requestFocus();
        }else if(validarTablas()){
            ArrayList actores = obtenerDatosTabla(tabla1,1);
            ArrayList autoridades = obtenerDatosTabla2(tabla2,2);
            ArrayList terceroAutoridades = obtenerDatosTabla2(tabla3,3);
            ArrayList terceroActores = obtenerDatosTabla(tabla4,4);
            ArrayList autoridadesAjenas = obtenerDatosTabla2(tabla5,5);
            ArrayList personasAjenas = obtenerDatosTabla(tabla6,6);
            Date fecha_destino = dateDestino.getDate();
            String id_destino = ((DestinoEntity)cmbDestino.getSelectedItem()).getId_destino();
            ErrorEntity error = bl.saveNotificacion(actores, autoridades, terceroAutoridades, terceroActores, autoridadesAjenas, personasAjenas,
                    fecha_destino, id_destino, sesion);
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
        dateDestino = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbDestino = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        cbxNotiActores = new javax.swing.JCheckBox();
        cbxNotiAutoridades = new javax.swing.JCheckBox();
        cbxNotiTerceroAutoridades = new javax.swing.JCheckBox();
        cbxNotiTerceroActores = new javax.swing.JCheckBox();
        cbxNotiAutoridadesAjenas = new javax.swing.JCheckBox();
        cbxNotiPersonasAjenas = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        dateNotificacion = new com.toedter.calendar.JDateChooser();
        btnAplicarNotificacion = new javax.swing.JButton();
        lblDepositoP1 = new javax.swing.JLabel();
        dateDeposito = new com.toedter.calendar.JDateChooser();
        btnAplicarDeposito = new javax.swing.JButton();
        lblRecepcionP1 = new javax.swing.JLabel();
        dateRecepcion = new com.toedter.calendar.JDateChooser();
        btnAplicarRecepcion = new javax.swing.JButton();
        panelInformacion = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable0 = new javax.swing.JTable();
        panelBuscar = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtBuscarFolio = new javax.swing.JTextField();
        btnBusarFolio = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        txtBuscarAnio = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        cmbFechas = new javax.swing.JComboBox();
        btnF2 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF12 = new javax.swing.JButton();
        lblAuto = new javax.swing.JLabel();

        setClosable(true);
        setTitle("Descarga de Notificaciones");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Descarga_notificaciones.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");

        jScrollPane7.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panelActores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Actores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

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
            .addComponent(jScrollPane1)
        );
        panelActoresLayout.setVerticalGroup(
            panelActoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
        );

        panelTerceroAutoridades.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Terceros Autoridades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

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
            .addComponent(jScrollPane2)
        );
        panelTerceroAutoridadesLayout.setVerticalGroup(
            panelTerceroAutoridadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelAutoridades.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autoridades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

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
            .addComponent(jScrollPane3)
        );
        panelAutoridadesLayout.setVerticalGroup(
            panelAutoridadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelPersonasAjenas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Personas Ajenas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

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
            .addComponent(jScrollPane4)
        );
        panelPersonasAjenasLayout.setVerticalGroup(
            panelPersonasAjenasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelTerceroActores.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Terceros Actores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

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
            .addComponent(jScrollPane5)
        );
        panelTerceroActoresLayout.setVerticalGroup(
            panelTerceroActoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelAutoridadesAjenas.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autoridades Ajenas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

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
            .addComponent(jScrollPane6)
        );
        panelAutoridadesAjenasLayout.setVerticalGroup(
            panelAutoridadesAjenasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Destino", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        dateDestino.setDateFormatString("dd/MMM/yyyy");
        dateDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Fecha de entrega");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Destino");

        cmbDestino.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(dateDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setLayout(new org.jdesktop.swingx.VerticalLayout());

        cbxNotiActores.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiActores.setText("Actores");
        cbxNotiActores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiActoresActionPerformed(evt);
            }
        });
        jPanel3.add(cbxNotiActores);

        cbxNotiAutoridades.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiAutoridades.setText("Autoridades");
        cbxNotiAutoridades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiAutoridadesActionPerformed(evt);
            }
        });
        jPanel3.add(cbxNotiAutoridades);

        cbxNotiTerceroAutoridades.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiTerceroAutoridades.setText("Tercero Autoridades");
        cbxNotiTerceroAutoridades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiTerceroAutoridadesActionPerformed(evt);
            }
        });
        jPanel3.add(cbxNotiTerceroAutoridades);

        cbxNotiTerceroActores.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiTerceroActores.setText("Tercero Actores");
        cbxNotiTerceroActores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiTerceroActoresActionPerformed(evt);
            }
        });
        jPanel3.add(cbxNotiTerceroActores);

        cbxNotiAutoridadesAjenas.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiAutoridadesAjenas.setText("Autoridades Ajenas");
        cbxNotiAutoridadesAjenas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiAutoridadesAjenasActionPerformed(evt);
            }
        });
        jPanel3.add(cbxNotiAutoridadesAjenas);

        cbxNotiPersonasAjenas.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        cbxNotiPersonasAjenas.setText("Personas Ajenas");
        cbxNotiPersonasAjenas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNotiPersonasAjenasActionPerformed(evt);
            }
        });
        jPanel3.add(cbxNotiPersonasAjenas);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Fecha de Depósito");

        dateNotificacion.setDateFormatString("dd/MMM/yyyy");
        dateNotificacion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        btnAplicarNotificacion.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAplicarNotificacion.setText("Aplicar");
        btnAplicarNotificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarNotificacionActionPerformed(evt);
            }
        });

        lblDepositoP1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblDepositoP1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDepositoP1.setText("Fecha de Notificación");

        dateDeposito.setDateFormatString("dd/MMM/yyyy");
        dateDeposito.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        btnAplicarDeposito.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAplicarDeposito.setText("Aplicar");
        btnAplicarDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarDepositoActionPerformed(evt);
            }
        });

        lblRecepcionP1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblRecepcionP1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblRecepcionP1.setText("Fecha de Recepción (Tricazac)");

        dateRecepcion.setDateFormatString("dd/MMM/yyyy");
        dateRecepcion.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N

        btnAplicarRecepcion.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAplicarRecepcion.setText("Aplicar");
        btnAplicarRecepcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAplicarRecepcionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblDepositoP1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblRecepcionP1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateNotificacion, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(dateRecepcion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateDeposito, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAplicarDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarNotificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDepositoP1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRecepcionP1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAplicarRecepcion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dateNotificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAplicarNotificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelActores, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTerceroAutoridades, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAutoridades, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelPersonasAjenas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTerceroActores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAutoridadesAjenas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 966, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelActores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAutoridades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jScrollPane8.setViewportView(jTable0);

        panelInformacion.add(jScrollPane8);
        jScrollPane8.setBounds(10, 20, 890, 120);

        panelBuscar.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda de Folios", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
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
        jLabel27.setBounds(140, 20, 30, 30);

        txtBuscarAnio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtBuscarAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtBuscarAnio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBuscarAnioKeyPressed(evt);
            }
        });
        panelBuscar.add(txtBuscarAnio);
        txtBuscarAnio.setBounds(170, 20, 60, 30);

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Seleccionar Folio");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        panelBuscar.add(jButton1);
        jButton1.setBounds(750, 20, 160, 30);

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel28.setText("Folios");
        panelBuscar.add(jLabel28);
        jLabel28.setBounds(440, 20, 40, 30);

        cmbFechas.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cmbFechas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        panelBuscar.add(cmbFechas);
        cmbFechas.setBounds(480, 20, 260, 30);

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
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1184, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelInformacion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(lblAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(138, 138, 138))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnF12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEsc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                                .addComponent(btnF2, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblAuto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelInformacion, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                .addContainerGap())
        );

        setBounds(0, 0, 1200, 720);
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

    private void btnAplicarNotificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarNotificacionActionPerformed
        eventoBotonFechaNotificacion();
    }//GEN-LAST:event_btnAplicarNotificacionActionPerformed

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

    private void btnAplicarDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarDepositoActionPerformed
        eventoBotonFechaDeposito();
    }//GEN-LAST:event_btnAplicarDepositoActionPerformed

    private void btnAplicarRecepcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAplicarRecepcionActionPerformed
        eventoBotonFechaRecepcion();
    }//GEN-LAST:event_btnAplicarRecepcionActionPerformed

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
    private javax.swing.JButton btnAplicarDeposito;
    private javax.swing.JButton btnAplicarNotificacion;
    private javax.swing.JButton btnAplicarRecepcion;
    private javax.swing.JButton btnBusarFolio;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JCheckBox cbxNotiActores;
    private javax.swing.JCheckBox cbxNotiAutoridades;
    private javax.swing.JCheckBox cbxNotiAutoridadesAjenas;
    private javax.swing.JCheckBox cbxNotiPersonasAjenas;
    private javax.swing.JCheckBox cbxNotiTerceroActores;
    private javax.swing.JCheckBox cbxNotiTerceroAutoridades;
    private javax.swing.JComboBox cmbDestino;
    private javax.swing.JComboBox cmbFechas;
    private com.toedter.calendar.JDateChooser dateDeposito;
    private com.toedter.calendar.JDateChooser dateDestino;
    private com.toedter.calendar.JDateChooser dateNotificacion;
    private com.toedter.calendar.JDateChooser dateRecepcion;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable0;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JLabel lblAuto;
    private javax.swing.JLabel lblDepositoP1;
    private javax.swing.JLabel lblRecepcionP1;
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
