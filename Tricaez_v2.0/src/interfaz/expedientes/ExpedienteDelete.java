package interfaz.expedientes;

import baseGeneral.ErrorEntity;
import baseGeneral.Tabla;
import baseGeneral.JTextMascara;
import baseSistema.Utilidades;
import bl.expedientes.ExpedientesDeleteBl;
import entitys.ActorEntity;
import entitys.SesionEntity;
import entitys.ActorTerceroEntity;
import entitys.ExpedienteEntity;
import entitys.AutoridadEntity;
import entitys.AutorizadoEntity;
import entitys.MunicipioEntity;
import entitys.PersonaAjenaEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class ExpedienteDelete extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    ExpedientesDeleteBl bl = new ExpedientesDeleteBl();
    Utilidades u = new Utilidades();
    
    Tabla tablaT1;
    Tabla tablaT2;
    Tabla tablaT3;
    Tabla tablaT4;
    Tabla tablaT5;
    Tabla tablaT6;
    Tabla tablaT7;
    
    /**
     * Creates new form Destinos
     */
    public ExpedienteDelete(){
        initComponents();
    }
    
    public ExpedienteDelete(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        t0_txtBuscar.setDocument(new JTextMascara(29,true,"todo"));
        mapeoTeclas();
        declaracionTablaT1();
        declaracionTablaT2();
        declaracionTablaT3();
        declaracionTablaT4();
        declaracionTablaT5();
        declaracionTablaT6();
        declaracionTablaT7();
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones globales">
    private Boolean validar(){
        Boolean flag = false;
        if(t0_txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado un número Expediente", "Alerta", 2);
            t0_txtBuscar.requestFocus();
        }else{
            flag = true;
        }
        return flag;
    }
    
    private void limpiar(){
        t0_txtExpediente.setText("");
        t0_txtFecha.setText("");
        t0_txtTipo.setText("");
        t0_txtActo.setText("");
        t0_txtPretension.setText("");
        t0_txtCantidad.setText("");
        t0_txtObservaciones.setText("");   
        tablaT1.clearRows();
        tablaT2.clearRows();
        tablaT3.clearRows();
        tablaT4.clearRows();
        tablaT5.clearRows();
        tablaT6.clearRows();
        tablaT7.clearRows();
        setTitle("Eliminación de Expedientes");
    }
    
    private String getDatos1(String nombre, String paterno, String materno){
        return nombre+" "+paterno+" "+materno;
    }
    
    private String getDatos2(String calle, String ext, String inte, String colonia, String cp, MunicipioEntity mun){
        String datos = "";
        if(calle.length()!=0){
            datos = datos.concat("CALLE:   "+calle);
        }
        if(ext.length()!=0){
            if(datos.length()!=0) datos = datos.concat("\n");
            datos = datos.concat("NO. EXT:   "+ext);
        }
        if(inte.length()!=0){
            if(datos.length()!=0) datos = datos.concat("\n");
            datos = datos.concat("NO. INT :   "+inte);
        }
        if(colonia.length()!=0){
            if(datos.length()!=0) datos = datos.concat("\n");
            datos = datos.concat("COLONIA:   "+colonia);
        }
        if(cp.length()!=0){
            if(datos.length()!=0) datos = datos.concat("\n");
            datos = datos.concat("CP:   "+cp);
        }
        if(mun != null && mun.getId_municipio()!=-1){
            if(datos.length()!=0) datos = datos.concat("\n");
            datos = datos.concat("MUNICIPIO:   "+mun.getNombre());
        }
        return datos;
    }
    
    private String getDatos3(Boolean repre1, String grupo1, Boolean repre2, String grupo2){
        String datos = "";
        if(repre1){
            if(grupo1.length()!=0)
                datos = datos.concat("ES REPRESENTANTE LEGAL DE: "+grupo1);
            else
                datos = datos.concat("ES REPRESENTANTE LEGAL");
        }
        if(repre2){
            if(datos.length()!=0) datos = datos.concat("\n");
            if(grupo2.length()!=0)
                datos = datos.concat("ES ASESOR LEGAL DE: "+grupo2);
            else
                datos = datos.concat("ES ASESOR LEGAL");
        }
        return datos;
    }
    
    private String getDatos4(String telefono, String email){
        String datos = "";
        if(telefono.length()!=0){
            datos = datos.concat("TELÉFONO:   "+telefono);
        }
        if(email.length()!=0){
            if(datos.length()!=0) datos = datos.concat("\n");
            datos = datos.concat("E-MAIL:   "+email);
        }
        return datos;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Declarar tablas">
    private void declaracionTablaT1(){
        t1_table.getTableHeader().setResizingAllowed(false);
        tablaT1 = new Tabla(t1_table);
        String[][] titulos={
            {"no","datos1","datos2","datos3","datos4","observaciones"}//0-5
           ,{"No","Actor","Domicilio","Representante","Información Extra","observaciones"}
        };
        tablaT1.setTabla(titulos);
        tablaT1.setDefaultValues("","","","","","");
        tablaT1.setAnchoColumnas(30,200,300,300,250,400);
        tablaT1.setAlturaRow(30);
        tablaT1.alinear(0,2);
        tablaT1.cellLineWrap(1);
        tablaT1.cellLineWrap(2);
        tablaT1.cellLineWrap(3);
        tablaT1.cellLineWrap(4);
        tablaT1.cellLineWrap(5);
        tablaT1.cebraTabla();
    }
    
    private void declaracionTablaT2(){
        t2_table.getTableHeader().setResizingAllowed(false);
        tablaT2 = new Tabla(t2_table);
        String[][] titulos={
            {"no","tipo","municipio","autoridad"}//0-3
           ,{"No","Tipo de Autoridad","Municipio","Autoridad"}
        };
        tablaT2.setTabla(titulos);
        tablaT2.setDefaultValues("","","","");
        tablaT2.setAnchoColumnas(30,200,220,400);
        tablaT2.setAlturaRow(30);
        tablaT2.alinear(0,2);
        tablaT2.cellLineWrap(3);
        tablaT2.cebraTabla();
    }
    
    private void declaracionTablaT3(){
        tablaT3 = new Tabla(t3_table);
        String[][] titulos={
            {"no","tipo","municipio","autoridad"}//0-3
           ,{"No","Tipo de Autoridad","Municipio","Autoridad"}
        };
        tablaT3.setTabla(titulos);
        tablaT3.setDefaultValues("","","","");
        tablaT3.setAnchoColumnas(30,200,220,400);
        tablaT3.setAlturaRow(30);
        tablaT3.alinear(0,2);
        tablaT3.cellLineWrap(3);
        tablaT3.cebraTabla();
    }
    
    private void declaracionTablaT4(){
        tablaT4 = new Tabla(t4_table);
        String[][] titulos={
            {"no","datos1","datos2","datos4","observaciones"}//0-4
           ,{"No","Tercero Actor","Domicilio","Información Extra","Observaciones"}
        };
        tablaT4.setTabla(titulos);
        tablaT4.setDefaultValues("","","","","");
        tablaT4.setAnchoColumnas(30,200,300,250,400);
        tablaT4.setAlturaRow(30);
        tablaT4.alinear(0,2);
        tablaT4.cellLineWrap(1);
        tablaT4.cellLineWrap(2);
        tablaT4.cellLineWrap(3);
        tablaT4.cellLineWrap(4);
        tablaT4.cebraTabla();
    }
    
    private void declaracionTablaT5(){
        tablaT5 = new Tabla(t5_table);
        String[][] titulos={
            {"no","tipo","municipio","autoridad"}//0-3
           ,{"No","Tipo de Autoridad","Municipio","Autoridad"}
        };
        tablaT5.setTabla(titulos);
        tablaT5.setDefaultValues("","","","");
        tablaT5.setAnchoColumnas(30,200,220,400);
        tablaT5.setAlturaRow(30);
        tablaT5.alinear(0,2);
        tablaT5.cellLineWrap(3);
        tablaT5.cebraTabla();
    }
    
    private void declaracionTablaT6(){
        tablaT6 = new Tabla(t6_table);
        String[][] titulos={
            {"no","datos1","datos2","datos4","observaciones"}//0-4
           ,{"No","Persona Ajena","Domicilio","Información Extra","Observaciones"}
        };
        tablaT6.setTabla(titulos);
        tablaT6.setDefaultValues("","","","","");
        tablaT6.setAnchoColumnas(30,200,300,250,400);
        tablaT6.setAlturaRow(30);
        tablaT6.alinear(0,2);
        tablaT6.cellLineWrap(1);
        tablaT6.cellLineWrap(2);
        tablaT6.cellLineWrap(3);
        tablaT6.cellLineWrap(4);
        tablaT6.cebraTabla();
    }
    
    private void declaracionTablaT7(){
        t7_table.getTableHeader().setResizingAllowed(false);
        tablaT7 = new Tabla(t7_table);
        String[][] titulos={
            {"no","datos1","parte","datos2","datos4","observaciones"}//0-5
           ,{"No","Autorizado","Representa a la Parte","Domicilio","Información Extra","Observaciones"}
        };
        tablaT7.setTabla(titulos);
        tablaT7.setDefaultValues("","","","","","");
        tablaT7.setAnchoColumnas(30,200,150,300,150,400);
        tablaT7.setAlturaRow(30);
        tablaT7.alinear(0,2);
        tablaT7.cellLineWrap(1);
        tablaT7.cellLineWrap(3);
        tablaT7.cellLineWrap(4);
        tablaT7.cellLineWrap(5);
        tablaT7.cebraTabla();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="carga de TabbedPanes">
    private void cargaExpediente(String expediente){
        limpiar();
        //String expediente = u.completaNumeroExpediente(exp);
        //if(expediente != null){
            if(expediente.length()==0){
                setTitle("Eliminación de Expedientes");
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el número de Expediente.\nPor favor ingrese un número de expediente", "Aviso", 1);
                t0_txtBuscar.requestFocus();
            }else{
                if(!bl.existeExpediente(expediente)){
                    setTitle("Eliminación de Expedientes");
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"El número de Expediente no existe.\nPor favor verifique el número de expediente o ingrese uno nuevo", "Aviso", 1);
                    t0_txtBuscar.requestFocus();
                }else{
                    setTitle("Eliminación de Expedientes - Expediente a eliminar: "+expediente);
                    t0_txtBuscar.setText("");
                    t0_txtExpediente.requestFocus();
                    cargaTabbedPane0(expediente);
                    cargaTabbedPane1(expediente);
                    cargaTabbedPane2(expediente);
                    cargaTabbedPane3(expediente);
                    cargaTabbedPane4(expediente);
                    cargaTabbedPane5(expediente);
                    cargaTabbedPane6(expediente);
                    cargaTabbedPane7(expediente);
                }
            }
        /*}else{
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Formato de Número de Expediente incorrecto", "Aviso", 1);
            t0_txtBuscar.requestFocus();
        }*/
    }
    
    private void cargaTabbedPane0(String expediente){
        ExpedienteEntity expe = bl.getExpediente(expediente);
        t0_txtExpediente.setText(expe.getExpediente());
        t0_txtFecha.setText(u.dateToString(expe.getFecha()));
        t0_txtTipo.setText(expe.getTipoProcedimiento().getNombre());
        if(expe.getTipoActo()!=null)
            t0_txtActo.setText(expe.getTipoActo().getNombre());        
        if(expe.getTipoPretension()!=null)
            t0_txtPretension.setText(expe.getTipoPretension().getNombre());
        if(expe.getCantidad()!=0.0)
            t0_txtCantidad.setText(expe.getCantidad().toString());
        t0_txtObservaciones.setText(expe.getObservaciones());
    }
    
    private void cargaTabbedPane1(String expediente){
        ArrayList array = bl.getActores(expediente);
        if(array != null && array.size()>0){
            tablaT1.clearRows();
            for (Object array1 : array) {
                ActorEntity actor = (ActorEntity) array1;
                String datos1 = getDatos1(actor.getNombre().getNombre(),actor.getNombre().getPaterno(),actor.getNombre().getMaterno());
                String datos2 = getDatos2(actor.getDom().getCalle(),actor.getDom().getNum_ext(),actor.getDom().getNum_int(),actor.getDom().getColonia(),actor.getDom().getCp(),actor.getDom().getMun());
                String datos3 = getDatos3(actor.getRepresentante1(),actor.getGrupo1(),actor.getRepresentante2(),actor.getGrupo2());
                String datos4 = getDatos4(actor.getDom().getTelefono(),actor.getDom().getEmail());
                Object obj[] = new Object[6];
                obj[0] = tablaT1.getRenglones()+1;
                obj[1] = datos1;
                obj[2] = datos2;
                obj[3] = datos3;
                obj[4] = datos4;
                obj[5] = actor.getObservaciones();
                tablaT1.addRenglonArray(obj);
            }
        }
    }

    private void cargaTabbedPane2(String expediente){
        ArrayList array = bl.getAutoridades(expediente);
        if(array != null && array.size()>0){
            tablaT2.clearRows();
            for (Object array1 : array) {
                AutoridadEntity auto = (AutoridadEntity) array1;
                Object obj[] = new Object[4];
                obj[0] = tablaT2.getRenglones()+1;
                obj[1] = auto.getTipo().getNombre();
                obj[2] = auto.getMun().getNombre();
                obj[3] = auto.getNombre();
                tablaT2.addRenglonArray(obj);
            }
        }    
    }
    
    private void cargaTabbedPane3(String expediente){
        ArrayList array = bl.getAutoridadesTercero(expediente);
        if(array != null && array.size()>0){
            tablaT3.clearRows();
            for (Object array1 : array) {
                AutoridadEntity auto = (AutoridadEntity) array1;
                Object obj[] = new Object[4];
                obj[0] = tablaT3.getRenglones()+1;
                obj[1] = auto.getTipo().getNombre();
                obj[2] = auto.getMun().getNombre();
                obj[3] = auto.getNombre();
                tablaT3.addRenglonArray(obj);
            }
        }    
    }
    
    private void cargaTabbedPane4(String expediente){
        ArrayList array = bl.getActoresTercero(expediente);
        if(array != null && array.size()>0){
            tablaT4.clearRows();
            for (Object array1 : array) {
                ActorTerceroEntity actor = (ActorTerceroEntity) array1;
                String datos1 = getDatos1(actor.getNombre().getNombre(),actor.getNombre().getPaterno(),actor.getNombre().getMaterno());
                String datos2 = getDatos2(actor.getDom().getCalle(),actor.getDom().getNum_ext(),actor.getDom().getNum_int(),actor.getDom().getColonia(),actor.getDom().getCp(),actor.getDom().getMun());
                String datos4 = getDatos4(actor.getDom().getTelefono(),actor.getDom().getEmail());
                Object obj[] = new Object[5];
                obj[0] = tablaT4.getRenglones()+1;
                obj[1] = datos1;
                obj[2] = datos2;
                obj[3] = datos4;
                obj[4] = actor.getObservaciones();
                tablaT4.addRenglonArray(obj);
            }
        }    
    }
    
    private void cargaTabbedPane5(String expediente){
        ArrayList array = bl.getAutoridadesAjenas(expediente);
        if(array != null && array.size()>0){
            tablaT5.clearRows();
            for (Object array1 : array) {
                AutoridadEntity auto = (AutoridadEntity) array1;
                Object obj[] = new Object[7];
                obj[0] = tablaT5.getRenglones()+1;
                obj[1] = auto.getTipo().getNombre();
                obj[2] = auto.getMun().getNombre();
                obj[3] = auto.getNombre();
                tablaT5.addRenglonArray(obj);
            }
        }    
    }
    
    private void cargaTabbedPane6(String expediente){
        ArrayList array = bl.getPersonasAjenas(expediente);
        if(array != null && array.size()>0){
            tablaT6.clearRows();
            for (Object array1 : array) {
                PersonaAjenaEntity actor = (PersonaAjenaEntity) array1;
                String datos1 = getDatos1(actor.getNombre().getNombre(),actor.getNombre().getPaterno(),actor.getNombre().getMaterno());
                String datos2 = getDatos2(actor.getDom().getCalle(),actor.getDom().getNum_ext(),actor.getDom().getNum_int(),actor.getDom().getColonia(),actor.getDom().getCp(),actor.getDom().getMun());
                String datos4 = getDatos4(actor.getDom().getTelefono(),actor.getDom().getEmail());
                Object obj[] = new Object[5];
                obj[0] = tablaT6.getRenglones()+1;
                obj[1] = datos1;
                obj[2] = datos2;
                obj[3] = datos4;
                obj[4] = actor.getObservaciones();
                tablaT6.addRenglonArray(obj);
            }
        }    
    }
    
    private void cargaTabbedPane7(String expediente){
        String []partes = {"","ACTORES","AUTORIDADES","TERCERO AUTORIDADES","TERCERO ACTORES","AUTORIDADES AJENAS","PERSONAS AJENAS"};
        ArrayList array = bl.getAutorizados(expediente);
        if(array != null && array.size()>0){
            tablaT7.clearRows();
            for (Object array1 : array) {
                AutorizadoEntity auto = (AutorizadoEntity) array1;
                String datos1 = getDatos1(auto.getNombre().getNombre(),auto.getNombre().getPaterno(),auto.getNombre().getMaterno());
                String datos2 = getDatos2(auto.getDom().getCalle(),auto.getDom().getNum_ext(),auto.getDom().getNum_int(),auto.getDom().getColonia(),auto.getDom().getCp(),auto.getDom().getMun());
                String datos4 = getDatos4(auto.getDom().getTelefono(),auto.getDom().getEmail());                
                Object obj[] = new Object[6];
                obj[0] = tablaT7.getRenglones()+1;
                obj[1] = datos1;
                obj[2] = partes[auto.getId_parte()];
                obj[3] = datos2;
                obj[4] = datos4;
                obj[5] = auto.getObservaciones();
                tablaT7.addRenglonArray(obj);
            }
        }    
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        limpiar();
        jTabbedPane1.setSelectedIndex(0);
        t0_txtBuscar.setText("");
        t0_txtBuscar.requestFocus();
    }
    
    private void botonF2(){}
    
    private void botonF4(){}
    
    private void botonF5(){}
    
    private void botonF6(){
        if(validar()){
            ErrorEntity error = bl.deleteExpediente(t0_txtExpediente.getText());
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha eliminado correctamente", "Aviso", 1);
                botonEsc();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
            }
        }
    }
    
    private void botonF7(){}
    
    private void botonF12(){
        dispose();
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
        t1_table.getActionMap().put("Accion_tab",Accion_tab());
        t1_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        t2_table.getActionMap().put("Accion_tab",Accion_tab());
        t2_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        t3_table.getActionMap().put("Accion_tab",Accion_tab());
        t3_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        t4_table.getActionMap().put("Accion_tab",Accion_tab());
        t4_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        t5_table.getActionMap().put("Accion_tab",Accion_tab());
        t5_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        t6_table.getActionMap().put("Accion_tab",Accion_tab());
        t6_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        t7_table.getActionMap().put("Accion_tab",Accion_tab());
        t7_table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,0),"Accion_tab");
        /////
    }
    public AbstractAction Accion_Esc(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonEsc();}};}
    public AbstractAction Accion_F2(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF2();}};}
    public AbstractAction Accion_F4(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF4();}};}
    public AbstractAction Accion_F5(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF5();}};}
    public AbstractAction Accion_F6(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF6();}};}
    public AbstractAction Accion_F7(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF7();}};}
    public AbstractAction Accion_F12(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){ botonF12();}};}
    
    //Para cambiar el focus cuando se encuentra en una tabla
    public AbstractAction Accion_tab(){ return new AbstractAction(){ public void actionPerformed(ActionEvent e){  
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();  
    }};}
    /////
    
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
        btnF12 = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnF2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel0 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        t0_txtBuscar = new javax.swing.JTextField();
        t0_btnBuscar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        t0_txtExpediente = new javax.swing.JTextField();
        t0_txtFecha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        t0_txtTipo = new javax.swing.JTextField();
        t0_txtPretension = new javax.swing.JTextField();
        jLabel69 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        t0_txtActo = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        t0_txtCantidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t0_txtObservaciones = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        t1_table = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        t2_table = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        t3_table = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        t4_table = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane12 = new javax.swing.JScrollPane();
        t7_table = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane15 = new javax.swing.JScrollPane();
        t5_table = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane17 = new javax.swing.JScrollPane();
        t6_table = new javax.swing.JTable();

        setClosable(true);
        setTitle("Eliminar Expediente");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_eliminar_expediente.jpg"))); // NOI18N

        btnF12.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF12.setText("F12 - Salir");
        btnF12.setFocusable(false);
        btnF12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF12ActionPerformed(evt);
            }
        });

        btnEsc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnEsc.setText("Esc - Limpiar");
        btnEsc.setFocusable(false);
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });

        btnF2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnF2.setText("F6 - Eliminar");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel0.setLayout(null);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel10.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("Número de Expediente a buscar");
        jPanel10.add(jLabel10);
        jLabel10.setBounds(20, 20, 220, 30);

        t0_txtBuscar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t0_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t0_txtBuscarKeyPressed(evt);
            }
        });
        jPanel10.add(t0_txtBuscar);
        t0_txtBuscar.setBounds(240, 20, 180, 30);

        t0_btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t0_btnBuscar.setText("Buscar Expediente");
        t0_btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t0_btnBuscarActionPerformed(evt);
            }
        });
        jPanel10.add(t0_btnBuscar);
        t0_btnBuscar.setBounds(440, 20, 170, 30);

        jPanel0.add(jPanel10);
        jPanel10.setBounds(10, 20, 830, 70);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información del Expediente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel8.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Número de Expediente");
        jPanel8.add(jLabel3);
        jLabel3.setBounds(0, 30, 160, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Fecha");
        jPanel8.add(jLabel5);
        jLabel5.setBounds(0, 70, 160, 30);

        t0_txtExpediente.setEditable(false);
        t0_txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel8.add(t0_txtExpediente);
        t0_txtExpediente.setBounds(170, 30, 190, 30);

        t0_txtFecha.setEditable(false);
        t0_txtFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtFecha.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel8.add(t0_txtFecha);
        t0_txtFecha.setBounds(170, 70, 190, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de Procedimiento");
        jPanel8.add(jLabel4);
        jLabel4.setBounds(0, 110, 160, 30);

        t0_txtTipo.setEditable(false);
        t0_txtTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel8.add(t0_txtTipo);
        t0_txtTipo.setBounds(170, 110, 640, 30);

        t0_txtPretension.setEditable(false);
        t0_txtPretension.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel8.add(t0_txtPretension);
        t0_txtPretension.setBounds(170, 190, 640, 30);

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel69.setText("Pretensión Aducida");
        jPanel8.add(jLabel69);
        jLabel69.setBounds(0, 190, 160, 30);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Acto Impugnado");
        jPanel8.add(jLabel2);
        jLabel2.setBounds(0, 150, 160, 30);

        t0_txtActo.setEditable(false);
        t0_txtActo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel8.add(t0_txtActo);
        t0_txtActo.setBounds(170, 150, 640, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Pretensión Cantidad");
        jPanel8.add(jLabel6);
        jLabel6.setBounds(0, 230, 160, 30);

        t0_txtCantidad.setEditable(false);
        t0_txtCantidad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel8.add(t0_txtCantidad);
        t0_txtCantidad.setBounds(170, 230, 190, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Observaciones");
        jPanel8.add(jLabel8);
        jLabel8.setBounds(0, 270, 160, 30);

        t0_txtObservaciones.setEditable(false);
        t0_txtObservaciones.setColumns(20);
        t0_txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtObservaciones.setLineWrap(true);
        t0_txtObservaciones.setRows(3);
        t0_txtObservaciones.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        t0_txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t0_txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(t0_txtObservaciones);

        jPanel8.add(jScrollPane2);
        jScrollPane2.setBounds(170, 270, 640, 90);

        jPanel0.add(jPanel8);
        jPanel8.setBounds(10, 100, 830, 370);

        jTabbedPane1.addTab("Expediente", jPanel0);

        jPanel1.setLayout(null);

        t1_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t1_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t1_table.setFocusable(false);
        t1_table.setNextFocusableComponent(jTabbedPane1);
        t1_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(t1_table);

        jPanel1.add(jScrollPane4);
        jScrollPane4.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Actores", jPanel1);

        jPanel2.setLayout(null);

        t2_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t2_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t2_table.setFocusable(false);
        t2_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(t2_table);

        jPanel2.add(jScrollPane7);
        jScrollPane7.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Autoridades", jPanel2);

        jPanel3.setLayout(null);

        t3_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t3_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t3_table.setFocusable(false);
        t3_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(t3_table);

        jPanel3.add(jScrollPane9);
        jScrollPane9.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Tercero Autoridades", jPanel3);

        jPanel4.setLayout(null);

        t4_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t4_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t4_table.setFocusable(false);
        t4_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane10.setViewportView(t4_table);

        jPanel4.add(jScrollPane10);
        jScrollPane10.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Tercero Actores", jPanel4);

        jPanel7.setLayout(null);

        t7_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t7_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t7_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        t7_table.setFocusable(false);
        t7_table.setNextFocusableComponent(jTabbedPane1);
        t7_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane12.setViewportView(t7_table);

        jPanel7.add(jScrollPane12);
        jScrollPane12.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Autorizados", jPanel7);

        jPanel5.setLayout(null);

        t5_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t5_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t5_table.setFocusable(false);
        t5_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane15.setViewportView(t5_table);

        jPanel5.add(jScrollPane15);
        jScrollPane15.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Autoridades Ajenas", jPanel5);

        jPanel6.setLayout(null);

        t6_table.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t6_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        t6_table.setFocusable(false);
        t6_table.getTableHeader().setReorderingAllowed(false);
        jScrollPane17.setViewportView(t6_table);

        jPanel6.add(jScrollPane17);
        jScrollPane17.setBounds(10, 50, 830, 400);

        jTabbedPane1.addTab("Personas Ajenas", jPanel6);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1011, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 378, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF6();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void t0_txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t0_txtObservacionesKeyPressed
        u.focus(evt, t0_txtObservaciones);
    }//GEN-LAST:event_t0_txtObservacionesKeyPressed

    private void t0_btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t0_btnBuscarActionPerformed
        cargaExpediente(t0_txtBuscar.getText());
    }//GEN-LAST:event_t0_btnBuscarActionPerformed

    private void t0_txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t0_txtBuscarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            cargaExpediente(t0_txtBuscar.getText());
    }//GEN-LAST:event_t0_txtBuscarKeyPressed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        t0_txtBuscar.requestFocus();
    }//GEN-LAST:event_formComponentShown


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel0;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton t0_btnBuscar;
    private javax.swing.JTextField t0_txtActo;
    private javax.swing.JTextField t0_txtBuscar;
    private javax.swing.JTextField t0_txtCantidad;
    private javax.swing.JTextField t0_txtExpediente;
    private javax.swing.JTextField t0_txtFecha;
    private javax.swing.JTextArea t0_txtObservaciones;
    private javax.swing.JTextField t0_txtPretension;
    private javax.swing.JTextField t0_txtTipo;
    private javax.swing.JTable t1_table;
    private javax.swing.JTable t2_table;
    private javax.swing.JTable t3_table;
    private javax.swing.JTable t4_table;
    private javax.swing.JTable t5_table;
    private javax.swing.JTable t6_table;
    private javax.swing.JTable t7_table;
    // End of variables declaration//GEN-END:variables
}
