package interfaz.expedientes;

import baseGeneral.ErrorEntity;
import baseGeneral.JTextDecimal;
import baseGeneral.Tabla;
import baseGeneral.JTextMascara;
import baseGeneral.JTextMascaraTextArea;
import baseSistema.Utilidades;
import bl.catalogos.ActosBl;
import bl.catalogos.AutoridadBl;
import bl.utilidades.AutorizadosBl;
import bl.utilidades.CalendarioOficialBl;
import bl.catalogos.DestinosBl;
import bl.catalogos.MunicipiosBl;
import bl.catalogos.ProcedimientosBl;
import bl.catalogos.TipoAutoridadesBl;
import bl.catalogos.TipoPretensionesBl;
import bl.catalogos.TipoPromocionesBl;
import bl.utilidades.BusquedaPersonasBl;
import bl.expedientes.ExpedientesUpdateBl;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.TextMatcherEditor;
import ca.odell.glazedlists.swing.AutoCompleteSupport;
import entitys.ActoEntity;
import entitys.ActorEntity;
import entitys.MunicipioEntity;
import entitys.SesionEntity;
import entitys.ActorTerceroEntity;
import entitys.ExpedienteEntity;
import entitys.AutoridadEntity;
import entitys.AutorizadoEntity;
import entitys.DomicilioEntity;
import entitys.NombreEntity;
import entitys.PersonaAjenaEntity;
import entitys.ProcedimientoEntity;
import entitys.TipoAutoridadEntity;
import entitys.TipoPretensionEntity;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author mavg
 */
public class ExpedienteUpdate extends javax.swing.JInternalFrame {

    SesionEntity sesion;
    ExpedientesUpdateBl bl = new ExpedientesUpdateBl();
    ProcedimientosBl blPro = new ProcedimientosBl();
    TipoPretensionesBl blPre = new TipoPretensionesBl();
    TipoPromocionesBl blTipo = new TipoPromocionesBl();
    DestinosBl blDestino = new DestinosBl();
    MunicipiosBl blMunicipio = new MunicipiosBl();
    TipoAutoridadesBl blTipAuto = new TipoAutoridadesBl();
    AutoridadBl blAuto = new AutoridadBl();
    AutorizadosBl blAutoriz = new AutorizadosBl();
    CalendarioOficialBl blCale = new CalendarioOficialBl();
    Utilidades u = new Utilidades();
    BusquedaPersonasBl blBuscar = new BusquedaPersonasBl();
    ActosBl blActo = new ActosBl();
    
    Tabla tablaT1;
    Tabla tablaT2;
    Tabla tablaT3;
    Tabla tablaT4;
    Tabla tablaT5;
    Tabla tablaT6;
    Tabla tablaT7;
    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
    AutoCompleteSupport t1_autocomplete;
    AutoCompleteSupport t2_autocomplete;
    AutoCompleteSupport t3_autocomplete;
    AutoCompleteSupport t5_autocomplete;
    AutoCompleteSupport t7_autocomplete;
    
    /**
     * Creates new form Destinos
     */
    public ExpedienteUpdate(){
        initComponents();
    }
    
    public ExpedienteUpdate(SesionEntity sesion) {
        this.sesion = sesion;
        initComponents();
        mapeoTeclas();
        lblExpediente.setVisible(false);
        t1_seleccion.setVisible(false);
        t4_seleccion.setVisible(false);
        t6_seleccion.setVisible(false);
        inicializarT0();
        inicializarT1();
        inicializarT2();
        inicializarT3();
        inicializarT4();
        inicializarT5();
        inicializarT6();
        inicializarT7();
        t0_btnMasActo.setVisible(false);
        t0_btnMasPretension.setVisible(false);
    }
    
    // <editor-fold defaultstate="collapsed" desc="Funciones globales">
    private void seleccionarDefaultMunicio(javax.swing.JComboBox combo){
        combo.setSelectedItem(new MunicipioEntity(56));
    }
    
    private void getExisteNombre(String nombre, String paterno, String materno){
        String nom = u.eliminaEspacios(nombre);
        String pat = u.eliminaEspacios(paterno);
        String mat = u.eliminaEspacios(materno);
        ArrayList array = blBuscar.getNombres(nom, pat, mat);
        if(!array.isEmpty() && array.size()>0){
            String mensaje = "<html><body>La persona:  <b>"+nom+" "+pat+" "+mat+"</b>  aparece en:</body></html>\n\n";
            int total = array.size();
            int limite = 6;
            for(int i=0; i<total; i++){
                mensaje = mensaje + array.get(i).toString()+"\n";
                if(i==(limite-1)){
                    i = total +5;
                }
            }
            if(total>limite){
                mensaje = mensaje + "La Lista es demasiado larga para mostrar";
            }
            int seleccion = javax.swing.JOptionPane.showOptionDialog(this,mensaje,"Búsqueda de Personas",javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Aceptar", "Imprimir", "Guardar como PDF" },"Aceptar");
            if(seleccion==1){
                Map params = new HashMap();
                params.put("nombre", nom);
                params.put("paterno", pat);
                params.put("materno", mat);
                params.put("completo", nom+" "+pat+" "+mat);
                blBuscar.reporteImprimir("existePersona.jasper",params);
            }else if(seleccion==2){
                try{
                    Map params = new HashMap();
                    params.put("nombre", nom);
                    params.put("paterno", pat);
                    params.put("materno", mat);
                    params.put("completo", nom+" "+pat+" "+mat);
                    String dir = u.callVentanaGuardarPDF();
                    if(!dir.equals("")){
                        if(blBuscar.reporteToPDF("existePersona.jasper",params,dir)){
                            JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el archivo","Error",JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el archivo","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    private void getExisteActo(String id_acto, String nombre, String numero){
        ArrayList array = bl.existeActo(id_acto, numero);
        if(!array.isEmpty() && array.size()>0){
            String mensaje = "<html><body>Acto impugnado:  <b>"+nombre+"</b><br>Oficio impugnado: <b>"+numero+"</b></body></html>\n\n";
            int total = array.size();
            int limite = 6;
            for(int i=0; i<total; i++){
                mensaje = mensaje + array.get(i).toString()+"\n";
                if(i==(limite-1)){
                    i = total +5;
                }
            }
            if(total>limite){
                mensaje = mensaje + "La Lista es demasiado larga para mostrar";
            }
            int seleccion = javax.swing.JOptionPane.showOptionDialog(this,mensaje,"Oficios impugnados",javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE, null, new Object[] { "Aceptar", "Imprimir", "Guardar como PDF" },"Aceptar");
            if(seleccion==1){
                Map params = new HashMap();
                params.put("id_acto", id_acto);
                params.put("numero", numero);
                params.put("nombre", nombre);
                bl.reporteImprimir("existeActo.jasper",params);
            }else if(seleccion==2){
                try{
                    Map params = new HashMap();
                    params.put("id_acto", id_acto);
                    params.put("numero", numero);
                    params.put("nombre", nombre);
                    String dir = u.callVentanaGuardarPDF();
                    if(!dir.equals("")){
                        if(bl.reporteToPDF("existeActo.jasper",params,dir)){
                            JOptionPane.showInternalMessageDialog(this,"Ocurrio un error al generar el archivo","Error",JOptionPane.ERROR_MESSAGE);
                        }else{
                            JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente el archivo","Aviso",JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    private Boolean verificarRepetidos(Tabla tab, String campo, String id){
        for(int i=0; i<tab.getRenglones(); i++){
            if(tab.getElement(campo, i).equals(id)){
                return true;
            }
        }
        return false;
    }
    
    private void quitarRenglonTabla(Tabla table){
        if(table.getRenglones()>0){
            int row = table.getSelectedRow();
            if(row!=-1){
                table.removeRow(row);
                if(table.getRenglones()>0){
                    for(int i=0; i<table.getRenglones(); i++){
                        table.setElement("no",i,i+1);
                    }
                }
            }
        }
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
    
    private void separarNombre(javax.swing.JTextField tNombre, javax.swing.JTextField tPaterno, javax.swing.JTextField tMaterno){
        if(tNombre.getText().length()>0){
            String tempo = u.eliminaEspacios(tNombre.getText());
            String delimiter = " ";
            String[] cade;
            cade = tempo.split(delimiter);
            if(cade.length > 2 ){
                tMaterno.setText(cade[cade.length-1]);
                tPaterno.setText(cade[cade.length-2]);
                String let = "";
                for(int i=0; i<cade.length-2; i++)
                    let = let.concat(cade[i]).concat(" ");
                tNombre.setText(u.eliminaEspacios(let));
            }
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane0 - Expediente">
    private void inicializarT0(){
        formatearCamposT0();
        limpiarT0();
        t0_txtExpediente.requestFocus();
    }
    
    private void formatearCamposT0(){
        t0_txtBuscar.setDocument(new JTextMascara(29,true,"todo"));
        t0_txtExpediente.setDocument(new JTextMascara(29,true,"todo"));
        t0_txtNumero.setDocument(new JTextMascara(200,true,"todo"));
        t0_txtCantidad.setDocument(new JTextDecimal(10,4));    
        t0_txtObservaciones.setDocument(new JTextMascaraTextArea());
        u.formatoJDateChooser(t0_dateFecha);
    }
    
    private void inicializaCmbProcedimientoT0(){
        u.inicializaCombo(t0_cmbTipo, new ProcedimientoEntity(), blPro.getProcedimientos());
        seleccionarDefaultProcedimiento();
    }
    
    private void seleccionarDefaultProcedimiento(){
        for(int i=0; i<t0_cmbTipo.getItemCount(); i++){
            if(((ProcedimientoEntity)t0_cmbTipo.getItemAt(i)).getSeleccionar()){
                t0_cmbTipo.setSelectedIndex(i);
                i=t0_cmbTipo.getItemCount()+5;
            }
        }
    }
    
    /*private void inicializaCmbActoT0(){
        u.inicializaCombo(t0_cmbActo, new ActoEntity(), blActo.getActos());
        seleccionarDefaultActo();
    }
    
    private void seleccionarDefaultActo(){
        for(int i=0; i<t0_cmbActo.getItemCount(); i++){
            if(((ActoEntity)t0_cmbActo.getItemAt(i)).getSeleccionar()){
                t0_cmbActo.setSelectedIndex(i);
                //i=t0_cmbActo.getItemCount()+5;
            }
        }
    }*/
    
    private void seleccionarDefaultActo(ActoEntity tipo){
        for(int i=0; i<t0_cmbActo.getItemCount(); i++){
            ActoEntity promo = (ActoEntity)t0_cmbActo.getItemAt(i);
            if(promo.equals(tipo)){
                t0_cmbActo.setSelectedIndex(i);
                i=t0_cmbActo.getItemCount()+5;
            }
        }
    }
    
    private void inicializaCmbActoT0(){
        if(t1_autocomplete != null && t1_autocomplete.isInstalled())
            t1_autocomplete.uninstall();
        EventList<ActoEntity> items = new BasicEventList<>();
        ArrayList array = blActo.getActos();
        items.add(new ActoEntity());
        for(Object obj : array)
            items.add((ActoEntity)obj);
        t1_autocomplete = AutoCompleteSupport.install(t0_cmbActo, items);
        t1_autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);        
    }
        
    private void inicializaCmbPretensionT0(){
        u.inicializaCombo(t0_cmbPretension, new TipoPretensionEntity(), blPre.getTipoPretensiones());
        seleccionarDefaultPretension();
    }
    
    private void seleccionarDefaultPretension(){
        for(int i=0; i<t0_cmbPretension.getItemCount(); i++){
            if(((TipoPretensionEntity)t0_cmbPretension.getItemAt(i)).getSeleccionar()){
                t0_cmbPretension.setSelectedIndex(i);
                i=t0_cmbPretension.getItemCount()+5;
            }
        }
    }
    
    private Boolean validarT0(){
        if(t0_txtExpediente.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El Número de Expediente es obligatorio", "Alerta", 2);
            t0_txtExpediente.requestFocus();
        }/*else if(!u.validaNumeroExpediente(t0_txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"Formato de Número de Expediente incorrecto", "Alerta", 2);
            t0_txtExpediente.requestFocus();
        }*/else if(!lblExpediente.getText().equals(t0_txtExpediente.getText()) && bl.existeExpediente(t0_txtExpediente.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El número de Expediente ya existe, por favor ingrese uno diferente", "Aviso", 1);
            t0_txtExpediente.requestFocus();
        }else if(t0_cmbTipo.getItemCount()>0 && t0_cmbTipo.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado un Tipo de Procedimiento", "Alerta", 2);
            t0_cmbTipo.requestFocus();
        }else if(t0_cmbActo.getItemCount()>0 && t0_cmbActo.getSelectedIndex()<=0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el Acto Impugnado", "Alerta", 2);
            t0_cmbActo.requestFocus();
        }else if(!blCale.isfechaValida(new SimpleDateFormat("yyyy-MM-dd").format(t0_dateFecha.getDate()))){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La fecha es un día inhábil, ingrese una nueva fecha", "Alerta", 2);
        }else if(t0_txtCantidad.getText().length()>0 && !u.isDouble(t0_txtCantidad.getText())){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"La cantidad de Pretensión es incorrecta", "Alerta", 2);
            t0_txtCantidad.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private void limpiarT0(){
        u.cleanComponent(lblExpediente,t0_txtExpediente,t0_txtNumero,t0_txtCantidad,t0_txtObservaciones,t0_dateFecha);
        inicializaCmbProcedimientoT0();
        inicializaCmbActoT0();
        inicializaCmbPretensionT0();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane1 - Actores">
    private void inicializarT1(){
        formatearCamposT1();
        inicializaCmbMunicipioT1();
        declaracionTablaT1();
        seleccionarDefaultMunicio(t1_cmbMunicipio);
    }
    
    private void formatearCamposT1(){
        t1_txtNombre.setDocument(new JTextMascara(299));
        t1_txtPaterno.setDocument(new JTextMascara(69));
        t1_txtMaterno.setDocument(new JTextMascara(69));
        t1_txtCalle.setDocument(new JTextMascara(69));
        t1_txtExterior.setDocument(new JTextMascara(19));
        t1_txtInterior.setDocument(new JTextMascara(19));
        t1_txtColonia.setDocument(new JTextMascara(69));
        t1_txtCP.setDocument(new JTextMascara(5,true,"numerico"));
        t1_txtTelefono.setDocument(new JTextMascara(19,true,"numerico"));
        t1_txtEmail.setDocument(new JTextMascara(69,true,"email"));
        t1_txtGrupo1.setDocument(new JTextMascara(299));
        t1_txtGrupo2.setDocument(new JTextMascara(299));
        t1_txtObservaciones.setDocument(new JTextMascaraTextArea());        
    }
    
    private void inicializaCmbMunicipioT1(){
        u.inicializaCombo(t1_cmbMunicipio, new MunicipioEntity(), blMunicipio.getMunicipios());
    }
    
    private void declaracionTablaT1(){
        t1_table.getTableHeader().setResizingAllowed(false);
        tablaT1 = new Tabla(t1_table);
        String[][] titulos={
            {"no","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",//0-11
                "representante1","grupo1","representante2","grupo2","datos1","datos2","datos3","datos4","observaciones","id_actor"}//12-21
           ,{"No","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",
               "representante1","grupo1","representante2","grupo2","Actor","Domicilio","Representante","Información Extra","observaciones","id_actor"}
        };
        tablaT1.setTabla(titulos);
        tablaT1.setDefaultValues("","","","","","","","","","","","","","","","","","","","","","");
        tablaT1.ocultarColumnas(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,21);
        tablaT1.setAnchoColumnas(30,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,200,300,300,250,400,50);
        tablaT1.setAlturaRow(30);
        tablaT1.alinear(0,2);
        tablaT1.cellLineWrap(16);
        tablaT1.cellLineWrap(17);
        tablaT1.cellLineWrap(18);
        tablaT1.cellLineWrap(19);
        tablaT1.cellLineWrap(20);
        tablaT1.cebraTabla();
    }
    
    private void agregarRenglonTablaT1(){
        if(validarT1()){
            MunicipioEntity mun = (MunicipioEntity)t1_cmbMunicipio.getSelectedItem();
            String datos1 = getDatos1(t1_txtNombre.getText(),t1_txtPaterno.getText(),t1_txtMaterno.getText());
            String datos2 = getDatos2(t1_txtCalle.getText(),t1_txtExterior.getText(),t1_txtInterior.getText(),t1_txtColonia.getText(),t1_txtCP.getText(),mun);
            String datos3 = getDatos3(t1_cbxRepresentante1.isSelected(),t1_txtGrupo1.getText(),t1_cbxRepresentante2.isSelected(),t1_txtGrupo2.getText());
            String datos4 = getDatos4(t1_txtTelefono.getText(),t1_txtEmail.getText());
            if(t1_seleccion.getText().length()==0){
                Object obj[] = new Object[22];
                obj[0] = tablaT1.getRenglones()+1;
                obj[1] = t1_txtNombre.getText();
                obj[2] = t1_txtPaterno.getText();
                obj[3] = t1_txtMaterno.getText();
                obj[4] = t1_txtCalle.getText();
                obj[5] = t1_txtExterior.getText();
                obj[6] = t1_txtInterior.getText();
                obj[7] = t1_txtColonia.getText();
                obj[8] = t1_txtCP.getText();
                obj[9] = t1_txtTelefono.getText();
                obj[10] = t1_txtEmail.getText();
                obj[11] = mun.getId_municipio();
                obj[12] = (t1_cbxRepresentante1.isSelected()) ? "SI" : "NO";
                obj[13] = t1_txtGrupo1.getText();
                obj[14] = (t1_cbxRepresentante2.isSelected()) ? "SI" : "NO";
                obj[15] = t1_txtGrupo2.getText();
                obj[16] = datos1;
                obj[17] = datos2;
                obj[18] = datos3;
                obj[19] = datos4;
                obj[20] = t1_txtObservaciones.getText();
                obj[21] = "";
                getExisteNombre(t1_txtNombre.getText(),t1_txtPaterno.getText(),t1_txtMaterno.getText());
                tablaT1.addRenglonArray(obj);
            }else{
                int row = new Integer(t1_seleccion.getText());
                getExisteNombre(t1_txtNombre.getText(),t1_txtPaterno.getText(),t1_txtMaterno.getText());
                tablaT1.setElement("nombre",row,t1_txtNombre.getText());
                tablaT1.setElement("paterno",row,t1_txtPaterno.getText());
                tablaT1.setElement("materno",row,t1_txtMaterno.getText());
                tablaT1.setElement("calle",row,t1_txtCalle.getText());
                tablaT1.setElement("exterior",row,t1_txtExterior.getText());
                tablaT1.setElement("interior",row,t1_txtInterior.getText());
                tablaT1.setElement("colonia",row,t1_txtColonia.getText());
                tablaT1.setElement("cp",row,t1_txtCP.getText());
                tablaT1.setElement("telefono",row,t1_txtTelefono.getText());
                tablaT1.setElement("email",row,t1_txtEmail.getText());
                tablaT1.setElement("municipio",row,mun.getId_municipio());
                tablaT1.setElement("representante",row,(t1_cbxRepresentante1.isSelected()) ? "SI" : "NO");
                tablaT1.setElement("grupo",row,t1_txtGrupo1.getText());
                tablaT1.setElement("representante2",row,(t1_cbxRepresentante2.isSelected()) ? "SI" : "NO");
                tablaT1.setElement("grupo2",row,t1_txtGrupo2.getText());
                tablaT1.setElement("datos1",row,datos1);
                tablaT1.setElement("datos2",row,datos2);
                tablaT1.setElement("datos3",row,datos3);
                tablaT1.setElement("datos4",row,datos4);
                tablaT1.setElement("observaciones",row,t1_txtObservaciones.getText());
            }
            limpiarCamposT1();
        }
    }
    
    private Boolean validarT1(){
        if(t1_txtNombre.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
            t1_txtNombre.requestFocus();
        }else if(t1_txtPaterno.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Paterno es obligatorio", "Alerta", 2);
            t1_txtPaterno.requestFocus();
        }else if(t1_txtMaterno.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Materno es obligatorio", "Alerta", 2);
            t1_txtMaterno.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private void obtenerDatosTablaT1(int row){
        tablaT1.setSelectedRow(row);
        t1_seleccion.setText(""+row);
        t1_txtNombre.setText(tablaT1.getElement("nombre",row));
        t1_txtPaterno.setText(tablaT1.getElement("paterno",row));
        t1_txtMaterno.setText(tablaT1.getElement("materno",row));
        t1_txtCalle.setText(tablaT1.getElement("calle",row));
        t1_txtExterior.setText(tablaT1.getElement("exterior",row));
        t1_txtInterior.setText(tablaT1.getElement("interior",row));
        t1_txtColonia.setText(tablaT1.getElement("colonia",row));
        t1_txtCP.setText(tablaT1.getElement("cp",row));
        t1_txtTelefono.setText(tablaT1.getElement("telefono",row));
        t1_txtEmail.setText(tablaT1.getElement("email",row));
        t1_cmbMunicipio.setSelectedItem(new MunicipioEntity(new Integer(tablaT1.getElement("municipio",row))));
        t1_cbxRepresentante1.setSelected(tablaT1.getElement("representante1",row).equals("SI")); 
        t1_txtGrupo1.setText(tablaT1.getElement("grupo1",row));
        t1_cbxRepresentante2.setSelected(tablaT1.getElement("representante2",row).equals("SI")); 
        t1_txtGrupo2.setText(tablaT1.getElement("grupo2",row));
        t1_txtObservaciones.setText(tablaT1.getElement("observaciones",row));
    }
    
    private void limpiarCamposT1(){
        u.cleanComponent(t1_seleccion,t1_txtNombre,t1_txtPaterno,t1_txtMaterno,t1_txtTelefono,t1_txtEmail,t1_txtGrupo1,t1_txtGrupo2,t1_txtObservaciones,t1_cbxRepresentante1,t1_cbxRepresentante2);
        if(!t1_cbxMantener.isSelected())
            u.cleanComponent(t1_txtCalle,t1_txtExterior,t1_txtInterior,t1_txtColonia,t1_txtCP,t1_cmbMunicipio);
        t1_txtNombre.requestFocus();
    }
    
    private void limpiarT1(){
        t1_cbxMantener.setSelected(false);
        u.editabledComponet(true,t1_txtCalle,t1_txtExterior,t1_txtInterior,t1_txtColonia,t1_txtCP);
        t1_cmbMunicipio.setEnabled(true);
        u.cleanComponent(t1_seleccion,t1_txtNombre,t1_txtPaterno,t1_txtMaterno,t1_txtCalle,t1_txtExterior,t1_txtInterior,t1_txtColonia,
                t1_txtCP,t1_txtTelefono, t1_txtEmail,t1_cbxRepresentante1,t1_txtGrupo1,t1_cbxRepresentante2,t1_txtGrupo2,t1_txtObservaciones);
        inicializaCmbMunicipioT1();
        tablaT1.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane2 - Autoridades">
    private void inicializarT2(){   
        inicializaCmbAutoridadT2();
        declaracionTablaT2();
    }
        
    private void declaracionTablaT2(){
        t2_table.getTableHeader().setResizingAllowed(false);
        tablaT2 = new Tabla(t2_table);
        String[][] titulos={
            {"no","tipo","id_tipo","municipio","id_municipio","autoridad","id_autoridad"}
           ,{"No","Tipo de Autoridad","id_tipo","Municipio","id_municipio","Autoridad","id_autoridad"}
        };
        tablaT2.setTabla(titulos);
        tablaT2.setDefaultValues("","","","","","","");
        tablaT2.ocultarColumnas(2,4,6);
        tablaT2.setAnchoColumnas(20,200,20,220,20,400,20);
        tablaT2.setAlturaRow(30);
        tablaT2.alinear(0,2);
        tablaT2.cellLineWrap(5);
        tablaT2.cebraTabla();
    }
    
    private void inicializaCmbAutoridadT2(){
        if(t2_autocomplete != null && t2_autocomplete.isInstalled())
            t2_autocomplete.uninstall();
        EventList<AutoridadEntity> items = new BasicEventList<>();
        ArrayList array = blAuto.getAutoridadesComplete();
        items.add(new AutoridadEntity());
        for(Object obj : array)
            items.add((AutoridadEntity)obj);
        t2_autocomplete = AutoCompleteSupport.install(t2_cmbAutoridad, items);
        t2_autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);        
    }
    
    private void agregarRenglonTablaT2(){
        if(validarT2()){
            AutoridadEntity autoT2 = (AutoridadEntity) t2_cmbAutoridad.getSelectedItem();
            TipoAutoridadEntity tipoT2 = autoT2.getTipo();
            MunicipioEntity munT2 = autoT2.getMun();
            Object obj[] = new Object[7];
            obj[0] = tablaT2.getRenglones()+1;
            obj[1] = tipoT2.getNombre();
            obj[2] = tipoT2.getId_tipo_autoridad();
            obj[3] = munT2.getNombre();
            obj[4] = (tipoT2.getRegla())?munT2.getId_municipio():"";
            obj[5] = autoT2.getNombre();
            obj[6] = autoT2.getId_autoridad();
            if(!verificarRepetidos(tablaT2, "id_autoridad", autoT2.getId_autoridad()))
                tablaT2.addRenglonArray(obj);
            inicializaCmbAutoridadT2();
            t2_cmbAutoridad.requestFocus();
        }
    }
    
    private Boolean validarT2(){
        if(t2_cmbAutoridad.getItemCount()>0 && t2_cmbAutoridad.getSelectedIndex()<=0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado una Autoridad", "Alerta", 2);
            t2_cmbAutoridad.requestFocus();
            return false;
        }
        return true;
    }
    
    private void limpiarT2(){
        inicializaCmbAutoridadT2();
        tablaT2.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane3 - Terceros Autoridades">
    private void inicializarT3(){
        inicializaCmbAutoridadT3();
        declaracionTablaT3();
    }
    
    private void inicializaCmbAutoridadT3(){
        if(t3_autocomplete != null && t3_autocomplete.isInstalled())
            t3_autocomplete.uninstall();
        EventList<AutoridadEntity> items = new BasicEventList<>();
        ArrayList array = blAuto.getAutoridadesComplete();
        items.add(new AutoridadEntity());
        for(Object obj : array)
            items.add((AutoridadEntity)obj);
        t3_autocomplete = AutoCompleteSupport.install(t3_cmbAutoridad, items);
        t3_autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);        
    }
    
    private void declaracionTablaT3(){
        tablaT3 = new Tabla(t3_table);
        String[][] titulos={
            {"no","tipo","id_tipo","municipio","id_municipio","autoridad","id_autoridad"}
           ,{"No","Tipo de Autoridad","id_tipo","Municipio","id_municipio","Autoridad","id_autoridad"}
        };
        tablaT3.setTabla(titulos);
        tablaT3.setDefaultValues("","","","","","","");
        tablaT3.ocultarColumnas(2,4,6);
        tablaT3.setAnchoColumnas(20,200,20,220,20,400,20);
        tablaT3.setAlturaRow(30);
        tablaT3.alinear(0,2);
        tablaT3.cellLineWrap(5);
        tablaT3.cebraTabla();
    }
    
    private void agregarRenglonTablaT3(){
        if(validarT3()){
            AutoridadEntity autoT3 = (AutoridadEntity) t3_cmbAutoridad.getSelectedItem();
            TipoAutoridadEntity tipoT3 = autoT3.getTipo();
            MunicipioEntity munT3 = autoT3.getMun();
            Object obj[] = new Object[7];
            obj[0] = tablaT3.getRenglones()+1;
            obj[1] = tipoT3.getNombre();
            obj[2] = tipoT3.getId_tipo_autoridad();
            obj[3] = munT3.getNombre();
            obj[4] = (tipoT3.getRegla())?munT3.getId_municipio():"";
            obj[5] = autoT3.getNombre();
            obj[6] = autoT3.getId_autoridad();
            if(!verificarRepetidos(tablaT3, "id_autoridad", autoT3.getId_autoridad()))
                tablaT3.addRenglonArray(obj);
            inicializaCmbAutoridadT3();
            t3_cmbAutoridad.requestFocus();
        }
    }
    
    private Boolean validarT3(){
        if(t3_cmbAutoridad.getItemCount()>0 && t3_cmbAutoridad.getSelectedIndex()<=0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado una Autoridad", "Alerta", 2);
            t3_cmbAutoridad.requestFocus();
            return false;
        }
        return true;
    }
    
    private void limpiarT3(){
        inicializaCmbAutoridadT3();
        tablaT3.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane4 - Terceros Actores">
    private void inicializarT4(){
        formatearCamposT4();
        inicializaCmbMunicipioT4();
        declaracionTablaT4();
        seleccionarDefaultMunicio(t4_cmbMunicipio);
    }
    
    private void formatearCamposT4(){
        t4_txtNombre.setDocument(new JTextMascara(299));
        t4_txtPaterno.setDocument(new JTextMascara(69));
        t4_txtMaterno.setDocument(new JTextMascara(69));
        t4_txtCalle.setDocument(new JTextMascara(69));
        t4_txtExterior.setDocument(new JTextMascara(19));
        t4_txtInterior.setDocument(new JTextMascara(19));
        t4_txtColonia.setDocument(new JTextMascara(69));
        t4_txtCP.setDocument(new JTextMascara(5,true,"numerico"));
        t4_txtTelefono.setDocument(new JTextMascara(20,true,"*"));
        t4_txtEmail.setDocument(new JTextMascara(50,true,"email"));
        t4_txtObservaciones.setDocument(new JTextMascaraTextArea());
    }
    
    private void inicializaCmbMunicipioT4(){
        u.inicializaCombo(t4_cmbMunicipio, new MunicipioEntity(), blMunicipio.getMunicipios());
    }
    
    private void declaracionTablaT4(){
        tablaT4 = new Tabla(t4_table);
        String[][] titulos={
            {"no","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",//0-11
                "datos1","datos2","datos4","observaciones","id_tercero"}//12-16
           ,{"No","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",
               "Tercero Actor","Domicilio","Información Extra","Observaciones","id_tercero"}
        };
        tablaT4.setTabla(titulos);
        tablaT4.setDefaultValues("","","","","","","","","","","","","","","","","");
        tablaT4.ocultarColumnas(1,2,3,4,5,6,7,8,9,10,11,16);
        tablaT4.setAnchoColumnas(30,50,50,50,50,50,50,50,50,50,50,50,200,300,250,400,50);
        tablaT4.setAlturaRow(30);
        tablaT4.alinear(0,2);
        tablaT4.cellLineWrap(12);
        tablaT4.cellLineWrap(13);
        tablaT4.cellLineWrap(14);
        tablaT4.cellLineWrap(15);
        tablaT4.cebraTabla();
    }
    
    private void agregarRenglonTablaT4(){
        if(validarT4()){
            MunicipioEntity mun = (MunicipioEntity)t4_cmbMunicipio.getSelectedItem();
            String datos1 = getDatos1(t4_txtNombre.getText(),t4_txtPaterno.getText(),t4_txtMaterno.getText());
            String datos2 = getDatos2(t4_txtCalle.getText(),t4_txtExterior.getText(),t4_txtInterior.getText(),t4_txtColonia.getText(),t4_txtCP.getText(),mun);
            String datos4 = getDatos4(t4_txtTelefono.getText(),t4_txtEmail.getText());
            if(t4_seleccion.getText().length()==0){
                Object obj[] = new Object[17];
                obj[0] = tablaT4.getRenglones()+1;
                obj[1] = t4_txtNombre.getText();
                obj[2] = t4_txtPaterno.getText();
                obj[3] = t4_txtMaterno.getText();
                obj[4] = t4_txtCalle.getText();
                obj[5] = t4_txtExterior.getText();
                obj[6] = t4_txtInterior.getText();
                obj[7] = t4_txtColonia.getText();
                obj[8] = t4_txtCP.getText();
                obj[9] = t4_txtTelefono.getText();
                obj[10] = t4_txtEmail.getText();
                obj[11] = mun.getId_municipio();
                obj[12] = datos1;
                obj[13] = datos2;
                obj[14] = datos4;
                obj[15] = t4_txtObservaciones.getText();
                obj[16] = "";
                getExisteNombre(t4_txtNombre.getText(),t4_txtPaterno.getText(),t4_txtMaterno.getText());
                tablaT4.addRenglonArray(obj);                
            }else{
                int row = new Integer(t4_seleccion.getText());
                getExisteNombre(t4_txtNombre.getText(),t4_txtPaterno.getText(),t4_txtMaterno.getText());
                tablaT4.setElement("nombre",row,t4_txtNombre.getText());
                tablaT4.setElement("paterno",row,t4_txtPaterno.getText());
                tablaT4.setElement("materno",row,t4_txtMaterno.getText());
                tablaT4.setElement("calle",row,t4_txtCalle.getText());
                tablaT4.setElement("exterior",row,t4_txtExterior.getText());
                tablaT4.setElement("interior",row,t4_txtInterior.getText());
                tablaT4.setElement("colonia",row,t4_txtColonia.getText());
                tablaT4.setElement("cp",row,t4_txtCP.getText());
                tablaT4.setElement("telefono",row,t4_txtTelefono.getText());
                tablaT4.setElement("email",row,t4_txtEmail.getText());
                tablaT4.setElement("municipio",row,mun.getId_municipio());
                tablaT4.setElement("datos1",row,datos1);
                tablaT4.setElement("datos2",row,datos2);
                tablaT4.setElement("datos4",row,datos4);
                tablaT4.setElement("observaciones",row,t4_txtObservaciones.getText());
            }
            limpiarCamposT4();
        }
    }
    
    private Boolean validarT4(){
        if(t4_txtNombre.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
            t4_txtNombre.requestFocus();
        }else if(t4_txtPaterno.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Paterno es obligatorio", "Alerta", 2);
            t4_txtPaterno.requestFocus();
        }else if(t4_txtMaterno.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Materno es obligatorio", "Alerta", 2);
            t4_txtMaterno.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private void obtenerDatosTablaT4(int row){
        tablaT4.setSelectedRow(row);
        t4_seleccion.setText(""+row);
        t4_txtNombre.setText(tablaT4.getElement("nombre",row));
        t4_txtPaterno.setText(tablaT4.getElement("paterno",row));
        t4_txtMaterno.setText(tablaT4.getElement("materno",row));
        t4_txtCalle.setText(tablaT4.getElement("calle",row));
        t4_txtExterior.setText(tablaT4.getElement("exterior",row));
        t4_txtInterior.setText(tablaT4.getElement("interior",row));
        t4_txtColonia.setText(tablaT4.getElement("colonia",row));
        t4_txtCP.setText(tablaT4.getElement("cp",row));
        t4_txtTelefono.setText(tablaT4.getElement("telefono",row));
        t4_txtEmail.setText(tablaT4.getElement("email",row));
        t4_cmbMunicipio.setSelectedItem(new MunicipioEntity(new Integer(tablaT4.getElement("municipio",row))));
        t4_txtObservaciones.setText(tablaT4.getElement("observaciones",row));
    }
    
    private void limpiarCamposT4(){
        u.cleanComponent(t4_seleccion,t4_txtNombre,t4_txtPaterno,t4_txtMaterno,t4_txtTelefono,t4_txtEmail,t4_txtObservaciones);
        if(!t4_cbxMantener.isSelected())
            u.cleanComponent(t4_txtCalle,t4_txtExterior,t4_txtInterior,t4_txtColonia,t4_txtCP,t4_cmbMunicipio);
        t4_txtNombre.requestFocus();
    }
    
    private void limpiarT4(){
        t4_cbxMantener.setSelected(false);
        u.editabledComponet(true,t4_txtCalle,t4_txtExterior,t4_txtInterior,t4_txtColonia,t4_txtCP);
        t4_cmbMunicipio.setEnabled(true);
        u.cleanComponent(t4_seleccion,t4_txtNombre,t4_txtPaterno,t4_txtMaterno,t4_txtCalle,t4_txtExterior,t4_txtInterior,t4_txtColonia,
                t4_txtCP,t4_txtTelefono, t4_txtEmail,t4_txtObservaciones);
        inicializaCmbMunicipioT4();
        tablaT4.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane5 - Autoridades Ajenas">
    private void inicializarT5(){
        inicializaCmbAutoridadT5();
        declaracionTablaT5();
    }
    
    private void declaracionTablaT5(){
        tablaT5 = new Tabla(t5_table);
        String[][] titulos={
            {"no","tipo","id_tipo","municipio","id_municipio","autoridad","id_autoridad"}
           ,{"No","Tipo de Autoridad","id_tipo","Municipio","id_municipio","Autoridad","id_autoridad"}
        };
        tablaT5.setTabla(titulos);
        tablaT5.setDefaultValues("","","","","","","");
        tablaT5.ocultarColumnas(2,4,6);
        tablaT5.setAnchoColumnas(20,200,20,220,20,400,20);
        tablaT5.setAlturaRow(30);
        tablaT5.alinear(0,2);
        tablaT5.cellLineWrap(5);
        tablaT5.cebraTabla();
    }
    
    private void inicializaCmbAutoridadT5(){
        if(t5_autocomplete != null && t5_autocomplete.isInstalled())
            t5_autocomplete.uninstall();
        EventList<AutoridadEntity> items = new BasicEventList<>();
        ArrayList array = blAuto.getAutoridadesComplete();
        items.add(new AutoridadEntity());
        for(Object obj : array)
            items.add((AutoridadEntity)obj);
        t5_autocomplete = AutoCompleteSupport.install(t5_cmbAutoridad, items);
        t5_autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);        
    }
    
    private void agregarRenglonTablaT5(){
        if(validarT5()){
            AutoridadEntity autoT5 = (AutoridadEntity) t5_cmbAutoridad.getSelectedItem();
            TipoAutoridadEntity tipoT5 = autoT5.getTipo();
            MunicipioEntity munT5 = autoT5.getMun();
            Object obj[] = new Object[7];
            obj[0] = tablaT5.getRenglones()+1;
            obj[1] = tipoT5.getNombre();
            obj[2] = tipoT5.getId_tipo_autoridad();
            obj[3] = munT5.getNombre();
            obj[4] = (tipoT5.getRegla())?munT5.getId_municipio():"";
            obj[5] = autoT5.getNombre();
            obj[6] = autoT5.getId_autoridad();
            if(!verificarRepetidos(tablaT5, "id_autoridad", autoT5.getId_autoridad()))
                tablaT5.addRenglonArray(obj);
            inicializaCmbAutoridadT5();
            t5_cmbAutoridad.requestFocus();
        }
    }
    
    private Boolean validarT5(){
        if(t5_cmbAutoridad.getItemCount()>0 && t5_cmbAutoridad.getSelectedIndex()<=0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado una Autoridad", "Alerta", 2);
            t5_cmbAutoridad.requestFocus();
            return false;
        }
        return true;
    }
    
    private void limpiarT5(){
        inicializaCmbAutoridadT5();
        tablaT5.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane6 - Personas Ajenas">
    private void inicializarT6(){
        formatearCamposT6();
        inicializaCmbMunicipioT6();
        declaracionTablaT6();
    }
    
    private void formatearCamposT6(){
        t6_txtNombre.setDocument(new JTextMascara(299));
        t6_txtPaterno.setDocument(new JTextMascara(69));
        t6_txtMaterno.setDocument(new JTextMascara(69));
        t6_txtCalle.setDocument(new JTextMascara(69));
        t6_txtExterior.setDocument(new JTextMascara(19));
        t6_txtInterior.setDocument(new JTextMascara(19));
        t6_txtColonia.setDocument(new JTextMascara(69));
        t6_txtCP.setDocument(new JTextMascara(5,true,"numerico"));
        t6_txtTelefono.setDocument(new JTextMascara(20,true,"*"));
        t6_txtEmail.setDocument(new JTextMascara(50,true,"email"));
        t6_txtObservaciones.setDocument(new JTextMascaraTextArea());
    }
    
    private void inicializaCmbMunicipioT6(){
        u.inicializaCombo(t6_cmbMunicipio, new MunicipioEntity(), blMunicipio.getMunicipios());
    }
    
    private void declaracionTablaT6(){
        tablaT6 = new Tabla(t6_table);       
        String[][] titulos={
            {"no","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",//0-11
                "datos1","datos2","datos4","observaciones","id_persona"}//12-16
           ,{"No","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",
               "Tercero Actor","Domicilio","Información Extra","Observaciones","id_persona"}
        };
        tablaT6.setTabla(titulos);
        tablaT6.setDefaultValues("","","","","","","","","","","","","","","","","");
        tablaT6.ocultarColumnas(1,2,3,4,5,6,7,8,9,10,11,16);
        tablaT6.setAnchoColumnas(30,50,50,50,50,50,50,50,50,50,50,50,200,300,250,400,50);
        tablaT6.setAlturaRow(30);
        tablaT6.alinear(0,2);
        tablaT6.cellLineWrap(12);
        tablaT6.cellLineWrap(13);
        tablaT6.cellLineWrap(14);
        tablaT6.cellLineWrap(15);
        tablaT6.cebraTabla();
    }
    
    private void agregarRenglonTablaT6(){
        if(validarT6()){
            MunicipioEntity mun = (MunicipioEntity)t6_cmbMunicipio.getSelectedItem();
            String datos1 = getDatos1(t6_txtNombre.getText(),t6_txtPaterno.getText(),t6_txtMaterno.getText());
            String datos2 = getDatos2(t6_txtCalle.getText(),t6_txtExterior.getText(),t6_txtInterior.getText(),t6_txtColonia.getText(),t6_txtCP.getText(),mun);
            String datos4 = getDatos4(t6_txtTelefono.getText(),t6_txtEmail.getText());
            if(t6_seleccion.getText().length()==0){
                Object obj[] = new Object[17];
                obj[0] = tablaT6.getRenglones()+1;
                obj[1] = t6_txtNombre.getText();
                obj[2] = t6_txtPaterno.getText();
                obj[3] = t6_txtMaterno.getText();
                obj[4] = t6_txtCalle.getText();
                obj[5] = t6_txtExterior.getText();
                obj[6] = t6_txtInterior.getText();
                obj[7] = t6_txtColonia.getText();
                obj[8] = t6_txtCP.getText();
                obj[9] = t6_txtTelefono.getText();
                obj[10] = t6_txtEmail.getText();
                obj[11] = mun.getId_municipio();
                obj[12] = datos1;
                obj[13] = datos2;
                obj[14] = datos4;
                obj[15] = t6_txtObservaciones.getText();
                obj[16] = "";
                getExisteNombre(t6_txtNombre.getText(),t6_txtPaterno.getText(),t6_txtMaterno.getText());
                tablaT6.addRenglonArray(obj);                
            }else{
                int row = new Integer(t6_seleccion.getText());
                getExisteNombre(t6_txtNombre.getText(),t6_txtPaterno.getText(),t6_txtMaterno.getText());
                tablaT6.setElement("nombre",row,t6_txtNombre.getText());
                tablaT6.setElement("paterno",row,t6_txtPaterno.getText());
                tablaT6.setElement("materno",row,t6_txtMaterno.getText());
                tablaT6.setElement("calle",row,t6_txtCalle.getText());
                tablaT6.setElement("exterior",row,t6_txtExterior.getText());
                tablaT6.setElement("interior",row,t6_txtInterior.getText());
                tablaT6.setElement("colonia",row,t6_txtColonia.getText());
                tablaT6.setElement("cp",row,t6_txtCP.getText());
                tablaT6.setElement("telefono",row,t6_txtTelefono.getText());
                tablaT6.setElement("email",row,t6_txtEmail.getText());
                tablaT6.setElement("municipio",row,mun.getId_municipio());
                tablaT6.setElement("datos1",row,datos1);
                tablaT6.setElement("datos2",row,datos2);
                tablaT6.setElement("datos4",row,datos4);
                tablaT6.setElement("observaciones",row,t6_txtObservaciones.getText());
            }
            limpiarCamposT6();
        }
    }
    
    private Boolean validarT6(){
        if(t6_txtNombre.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Nombre es obligatorio", "Alerta", 2);
            t6_txtNombre.requestFocus();
        }else if(t6_txtPaterno.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Paterno es obligatorio", "Alerta", 2);
            t6_txtPaterno.requestFocus();
        }else if(t6_txtMaterno.getText().length()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"El campo Apellido Materno es obligatorio", "Alerta", 2);
            t6_txtMaterno.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
    private void obtenerDatosTablaT6(int row){
        tablaT6.setSelectedRow(row);
        t6_seleccion.setText(""+row);
        t6_txtNombre.setText(tablaT6.getElement("nombre",row));
        t6_txtPaterno.setText(tablaT6.getElement("paterno",row));
        t6_txtMaterno.setText(tablaT6.getElement("materno",row));
        t6_txtCalle.setText(tablaT6.getElement("calle",row));
        t6_txtExterior.setText(tablaT6.getElement("exterior",row));
        t6_txtInterior.setText(tablaT6.getElement("interior",row));
        t6_txtColonia.setText(tablaT6.getElement("colonia",row));
        t6_txtCP.setText(tablaT6.getElement("cp",row));
        t6_txtTelefono.setText(tablaT6.getElement("telefono",row));
        t6_txtEmail.setText(tablaT6.getElement("email",row));
        t6_cmbMunicipio.setSelectedItem(new MunicipioEntity(new Integer(tablaT6.getElement("municipio",row))));
        t6_txtObservaciones.setText(tablaT6.getElement("observaciones",row));
    }
    
    private void limpiarCamposT6(){
        u.cleanComponent(t6_seleccion,t6_txtNombre,t6_txtPaterno,t6_txtMaterno,t6_txtTelefono,t6_txtEmail,t6_txtObservaciones);
        if(!t6_cbxMantener.isSelected())
            u.cleanComponent(t6_txtCalle,t6_txtExterior,t6_txtInterior,t6_txtColonia,t6_txtCP,t6_cmbMunicipio);
        t6_txtNombre.requestFocus();
    }
    
    private void limpiarT6(){
        t6_cbxMantener.setSelected(false);
        u.editabledComponet(true,t6_txtCalle,t6_txtExterior,t6_txtInterior,t6_txtColonia,t6_txtCP);
        t6_cmbMunicipio.setEnabled(true);
        u.cleanComponent(t6_seleccion,t6_txtNombre,t6_txtPaterno,t6_txtMaterno,t6_txtCalle,t6_txtExterior,t6_txtInterior,t6_txtColonia,
                t6_txtCP,t6_txtTelefono, t6_txtEmail,t6_txtObservaciones);
        inicializaCmbMunicipioT6();
        tablaT6.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TabbedPane7 - Autorizados">
    private void inicializarT7(){
        formatearCamposT7();
        inicializaCmbAutoridadT7();
        declaracionTablaT7();
        t7_cmbPartes.setSelectedIndex(1);
    }
    
    private void formatearCamposT7(){
        t7_txtObservaciones.setDocument(new JTextMascaraTextArea());
    }
    
    private void declaracionTablaT7(){
        t7_table.getTableHeader().setResizingAllowed(false);
        tablaT7 = new Tabla(t7_table);
        String[][] titulos={
            {"id","no","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",//0-12
                "datos1","datos2","datos4","id_parte","parte","observaciones"}//13-18
           ,{"id","No","nombre","paterno","materno","calle","exterior","interior","colonia","cp","telefono","email","municipio",
               "Persona Ajena","Domicilio","Información Extra","id_parte","Representa a la Parte","Observaciones"}
        };
        tablaT7.setTabla(titulos);
        tablaT7.setDefaultValues("","","","","","","","","","","","","","","","","","","");
        tablaT7.ocultarColumnas(0,2,3,4,5,6,7,8,9,10,11,12,16);
        tablaT7.setAnchoColumnas(50,30,50,50,50,50,50,50,50,50,50,50,50,200,300,250,50,150,400);
        tablaT7.setAlturaRow(30);
        tablaT7.alinear(1,2);
        tablaT7.cellLineWrap(13);
        tablaT7.cellLineWrap(14);
        tablaT7.cellLineWrap(15);
        tablaT7.cellLineWrap(18);
        tablaT7.cebraTabla();
    }
    
    private void inicializaCmbAutoridadT7(){
        if(t7_autocomplete != null && t7_autocomplete.isInstalled())
            t7_autocomplete.uninstall();
        EventList<AutorizadoEntity> items = new BasicEventList<>();
        ArrayList array = blAutoriz.getAutorizadores();
        items.add(new AutorizadoEntity());
        for(Object obj : array)
            items.add((AutorizadoEntity)obj);
        t7_autocomplete = AutoCompleteSupport.install(t7_cmbAutorizador, items);
        t7_autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);        
    }
    
    private Boolean validarT7(){
        if(t7_cmbAutorizador.getItemCount()>0 && t7_cmbAutorizador.getSelectedIndex()<=0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado el Autorizado", "Alerta", 2);
            t7_cmbAutorizador.requestFocus();
        }else if(t7_cmbPartes.getSelectedIndex()==0){
            javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha seleccionado a la parte que representa", "Alerta", 2);
            t7_cmbPartes.requestFocus();
        }else{
            return true;
        }
        return false;
    }
    
   private void agregarRenglonTablaT7(){
        if(validarT7()){
            AutorizadoEntity auto = (AutorizadoEntity) t7_cmbAutorizador.getSelectedItem();
            String datos1 = getDatos1(auto.getNombre().getNombre(),auto.getNombre().getPaterno(),auto.getNombre().getMaterno());
            String datos2 = getDatos2(auto.getDom().getCalle(),auto.getDom().getNum_ext(),auto.getDom().getNum_int(),auto.getDom().getColonia(),auto.getDom().getCp(),auto.getDom().getMun());
            String datos4 = getDatos4(auto.getDom().getTelefono(),auto.getDom().getEmail());
            Object obj[] = new Object[19];
            obj[0] = auto.getId_autorizado();
            obj[1] = tablaT7.getRenglones()+1;
            obj[2] = auto.getNombre().getNombre();
            obj[3] = auto.getNombre().getPaterno();
            obj[4] = auto.getNombre().getMaterno();
            obj[5] = auto.getDom().getCalle();
            obj[6] = auto.getDom().getNum_ext();
            obj[7] = auto.getDom().getNum_int();
            obj[8] = auto.getDom().getColonia();
            obj[9] = auto.getDom().getCp();
            obj[10] = auto.getDom().getTelefono();
            obj[11] = auto.getDom().getEmail();
            obj[12] = auto.getDom().getMun().getId_municipio();
            obj[13] = datos1;
            obj[14] = datos2;
            obj[15] = datos4;
            obj[16] = t7_cmbPartes.getSelectedIndex();
            obj[17] = t7_cmbPartes.getSelectedItem().toString();
            obj[18] = t7_txtObservaciones.getText();
            if(!verificarRepetidos(tablaT7, "id", auto.getId_autorizado()))
                tablaT7.addRenglonArray(obj);
            inicializaCmbAutoridadT7();
        }
    }
       
    private void limpiarT7(){
        inicializaCmbAutoridadT7();
        tablaT7.clearRows();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Obtener datos TabbedPanes">
    private ExpedienteEntity getTabbedPane0(){ //Expediente
        ExpedienteEntity expe = new ExpedienteEntity();
        expe.setExpediente(t0_txtExpediente.getText());
        expe.setFecha(t0_dateFecha.getDate());
        expe.setTipoProcedimiento((ProcedimientoEntity)t0_cmbTipo.getSelectedItem());
        expe.setTipoActo((ActoEntity) t0_cmbActo.getSelectedItem());
        expe.setNumero(t0_txtNumero.getText());
        expe.setTipoPretension(null);
        if(t0_cmbPretension.getSelectedIndex()!=0)
            expe.setTipoPretension((TipoPretensionEntity)t0_cmbPretension.getSelectedItem());
        expe.setCantidad((t0_txtCantidad.getText().length()>0)?new Double(t0_txtCantidad.getText()):0.0);
        expe.setObservaciones(t0_txtObservaciones.getText());
        return expe;
    }
    
    private ArrayList getTabbedPane1(ExpedienteEntity expe){ //Actores
        ArrayList actores = new ArrayList();
        for(int i=0; i<tablaT1.getRenglones(); i++){
            ActorEntity actor = new ActorEntity(); 
            actor.setId_actor(tablaT1.getElement("id_actor", i));
            MunicipioEntity mun = null;
            if(new Integer(tablaT1.getElement("municipio", i))!=-1)
                mun = new MunicipioEntity(new Integer(tablaT1.getElement("municipio", i)));
            actor.setNombre(new NombreEntity(tablaT1.getElement("nombre", i),tablaT1.getElement("paterno", i),tablaT1.getElement("materno", i)));
            actor.setDom(new DomicilioEntity(tablaT1.getElement("calle", i),tablaT1.getElement("exterior", i),tablaT1.getElement("interior", i),
                    tablaT1.getElement("colonia", i),tablaT1.getElement("cp", i),mun,tablaT1.getElement("telefono", i),tablaT1.getElement("email", i)));
            actor.setRepresentante1((tablaT1.getElement("representante1", i).equals("SI")));
            actor.setGrupo1(tablaT1.getElement("grupo1", i));
            actor.setRepresentante2((tablaT1.getElement("representante2", i).equals("SI")));
            actor.setGrupo2(tablaT1.getElement("grupo2", i));
            actor.setObservaciones(tablaT1.getElement("observaciones", i));
            actor.setExpe(expe);
            actores.add(actor);
        }
        return actores;
    }
    
    private ArrayList getTabbedPane2(){ //Autoridades
        ArrayList autoridades = new ArrayList();
        for(int i=0; i<tablaT2.getRenglones(); i++)
            autoridades.add(new AutoridadEntity(tablaT2.getElement("id_autoridad", i)));
        return autoridades;
    }
    
    private ArrayList getTabbedPane3(){ //Terceros Autoridades
        ArrayList terceroAutoridades = new ArrayList();
        for(int i=0; i<tablaT3.getRenglones(); i++)
            terceroAutoridades.add(new AutoridadEntity(tablaT3.getElement("id_autoridad", i)));
        return terceroAutoridades;
    }
    
    private ArrayList getTabbedPane4(ExpedienteEntity expe){ //Terceros Actores
        ArrayList terceroActores = new ArrayList();
        for(int i=0; i<tablaT4.getRenglones(); i++){
            ActorTerceroEntity actor = new ActorTerceroEntity();
            actor.setId_tercero_actor(tablaT4.getElement("id_tercero", i));
            MunicipioEntity mun = null;
            if(new Integer(tablaT4.getElement("municipio", i))!=-1)
                mun = new MunicipioEntity(new Integer(tablaT4.getElement("municipio", i)));
            actor.setNombre(new NombreEntity(tablaT4.getElement("nombre", i),tablaT4.getElement("paterno", i),tablaT4.getElement("materno", i)));
            actor.setDom(new DomicilioEntity(tablaT4.getElement("calle", i),tablaT4.getElement("exterior", i),tablaT4.getElement("interior", i),
                    tablaT4.getElement("colonia", i),tablaT4.getElement("cp", i),mun,tablaT4.getElement("telefono", i),tablaT4.getElement("email", i)));
            actor.setObservaciones(tablaT4.getElement("observaciones", i));
            actor.setExpe(expe);
            terceroActores.add(actor);
        }
        return terceroActores;
    }
    
    private ArrayList getTabbedPane5(){ //Autoridades Ajenas
        ArrayList autoridadesAjenas = new ArrayList();
        for(int i=0; i<tablaT5.getRenglones(); i++)
            autoridadesAjenas.add(new AutoridadEntity(tablaT5.getElement("id_autoridad", i)));
        return autoridadesAjenas;
    }
    
    private ArrayList getTabbedPane6(ExpedienteEntity expe){ //Personas Ajenas
        ArrayList personasAjenas = new ArrayList();
        for(int i=0; i<tablaT6.getRenglones(); i++){
            PersonaAjenaEntity perso = new PersonaAjenaEntity();
            perso.setId_persona_ajena(tablaT6.getElement("id_persona", i));
            MunicipioEntity mun = null;
            if(new Integer(tablaT6.getElement("municipio", i))!=-1)
                mun = new MunicipioEntity(new Integer(tablaT6.getElement("municipio", i)));
            perso.setNombre(new NombreEntity(tablaT6.getElement("nombre", i),tablaT6.getElement("paterno", i),tablaT6.getElement("materno", i)));
            perso.setDom(new DomicilioEntity(tablaT6.getElement("calle", i),tablaT6.getElement("exterior", i),tablaT6.getElement("interior", i),
                    tablaT6.getElement("colonia", i),tablaT6.getElement("cp", i),mun,tablaT6.getElement("telefono", i),tablaT6.getElement("email", i)));
            perso.setObservaciones(tablaT6.getElement("observaciones", i));
            perso.setExpe(expe);
            personasAjenas.add(perso);
        }
        return personasAjenas;
    }
    
    private ArrayList getTabbedPane7(){ //Autorizados
        ArrayList autorizados = new ArrayList();
        for(int i=0; i<tablaT7.getRenglones(); i++){
            AutorizadoEntity auto = new AutorizadoEntity();
            auto.setId_autorizado(tablaT7.getElement("id", i));
            auto.setId_parte(new Integer(tablaT7.getElement("id_parte", i)));
            auto.setObservaciones(tablaT7.getElement("observaciones", i));
            autorizados.add(auto);
        }
        return autorizados;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="carga de TabbedPanes">
    private void cargaExpediente(String expediente){
        limpiarT0();
        limpiarT1();
        limpiarT2();
        limpiarT3();
        limpiarT4();
        limpiarT5();
        limpiarT6();
        limpiarT7();
        ///String expediente = u.completaNumeroExpediente(exp);
        //if(expediente != null){
            if(expediente.length()==0){   
                setTitle("Modificación de Expedientes");
                javax.swing.JOptionPane.showInternalMessageDialog(this,"No se ha ingresado el número de Expediente.\nPor favor ingrese un número de expediente", "Aviso", 1);
                t0_txtBuscar.requestFocus();
            }else{
                if(!bl.existeExpediente(expediente)){
                    setTitle("Modificación de Expedientes");
                    javax.swing.JOptionPane.showInternalMessageDialog(this,"El número de Expediente no existe.\nPor favor verifique el número de expediente o ingrese uno nuevo", "Aviso", 1);
                    t0_txtBuscar.requestFocus();
                }else{
                    setTitle("Modificación de Expedientes - Modificando el expediente: "+expediente);
                    lblExpediente.setText(expediente);
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
        lblExpediente.setText(expe.getExpediente());
        t0_txtExpediente.setText(expe.getExpediente());
        t0_dateFecha.setDate(expe.getFecha());
        t0_cmbTipo.setSelectedItem(expe.getTipoProcedimiento());
        t0_cmbActo.setSelectedIndex(0);
        if(expe.getTipoActo()!=null)
            seleccionarDefaultActo(expe.getTipoActo());
            //t0_cmbActo.setSelectedItem(expe.getTipoActo());
        t0_txtNumero.setText(expe.getNumero());
        t0_cmbPretension.setSelectedIndex(0);
        if(expe.getTipoPretension()!=null)
            t0_cmbPretension.setSelectedItem(expe.getTipoPretension());
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
                Object obj[] = new Object[22];
                obj[0] = tablaT1.getRenglones()+1;
                obj[1] = actor.getNombre().getNombre();
                obj[2] = actor.getNombre().getPaterno();
                obj[3] = actor.getNombre().getMaterno();
                obj[4] = actor.getDom().getCalle();
                obj[5] = actor.getDom().getNum_ext();
                obj[6] = actor.getDom().getNum_int();
                obj[7] = actor.getDom().getColonia();
                obj[8] = actor.getDom().getCp();
                obj[9] = actor.getDom().getTelefono();
                obj[10] = actor.getDom().getEmail();
                obj[11] = -1;
                if(actor.getDom().getMun()!=null)
                    obj[11] = actor.getDom().getMun().getId_municipio();
                obj[12] = (actor.getRepresentante1()) ? "SI" : "NO";
                obj[13] = actor.getGrupo1();
                obj[14] = (actor.getRepresentante2()) ? "SI" : "NO";
                obj[15] = actor.getGrupo2();
                obj[16] = datos1;
                obj[17] = datos2;
                obj[18] = datos3;
                obj[19] = datos4;
                obj[20] = actor.getObservaciones();
                obj[21] = actor.getId_actor();
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
                Object obj[] = new Object[7];
                obj[0] = tablaT2.getRenglones()+1;
                obj[1] = auto.getTipo().getNombre();
                obj[2] = auto.getTipo().getId_tipo_autoridad();
                obj[3] = auto.getMun().getNombre();
                obj[4] = auto.getMun().getId_municipio();
                obj[5] = auto.getNombre();
                obj[6] = auto.getId_autoridad();
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
                Object obj[] = new Object[7];
                obj[0] = tablaT3.getRenglones()+1;
                obj[1] = auto.getTipo().getNombre();
                obj[2] = auto.getTipo().getId_tipo_autoridad();
                obj[3] = auto.getMun().getNombre();
                obj[4] = auto.getMun().getId_municipio();
                obj[5] = auto.getNombre();
                obj[6] = auto.getId_autoridad();
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
                Object obj[] = new Object[17];
                obj[0] = tablaT4.getRenglones()+1;
                obj[1] = actor.getNombre().getNombre();
                obj[2] = actor.getNombre().getPaterno();
                obj[3] = actor.getNombre().getMaterno();
                obj[4] = actor.getDom().getCalle();
                obj[5] = actor.getDom().getNum_ext();
                obj[6] = actor.getDom().getNum_int();
                obj[7] = actor.getDom().getColonia();
                obj[8] = actor.getDom().getCp();
                obj[9] = actor.getDom().getTelefono();
                obj[10] = actor.getDom().getEmail();
                obj[11] = -1;
                if(actor.getDom().getMun()!=null)
                    obj[11] = actor.getDom().getMun().getId_municipio();
                obj[12] = datos1;
                obj[13] = datos2;
                obj[14] = datos4;
                obj[15] = actor.getObservaciones();
                obj[16] = actor.getId_tercero_actor();
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
                obj[2] = auto.getTipo().getId_tipo_autoridad();
                obj[3] = auto.getMun().getNombre();
                obj[4] = auto.getMun().getId_municipio();
                obj[5] = auto.getNombre();
                obj[6] = auto.getId_autoridad();
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
                Object obj[] = new Object[17];
                obj[0] = tablaT6.getRenglones()+1;
                obj[1] = actor.getNombre().getNombre();
                obj[2] = actor.getNombre().getPaterno();
                obj[3] = actor.getNombre().getMaterno();
                obj[4] = actor.getDom().getCalle();
                obj[5] = actor.getDom().getNum_ext();
                obj[6] = actor.getDom().getNum_int();
                obj[7] = actor.getDom().getColonia();
                obj[8] = actor.getDom().getCp();
                obj[9] = actor.getDom().getTelefono();
                obj[10] = actor.getDom().getEmail();
                obj[11] = -1;
                if(actor.getDom().getMun()!=null)
                    obj[11] = actor.getDom().getMun().getId_municipio();
                obj[12] = datos1;
                obj[13] = datos2;
                obj[14] = datos4;
                obj[15] = actor.getObservaciones();
                obj[16] = actor.getId_persona_ajena();
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
                Object obj[] = new Object[19];
                obj[0] = auto.getId_autorizado();
                obj[1] = tablaT7.getRenglones()+1;
                obj[2] = auto.getNombre().getNombre();
                obj[3] = auto.getNombre().getPaterno();
                obj[4] = auto.getNombre().getMaterno();
                obj[5] = auto.getDom().getCalle();
                obj[6] = auto.getDom().getNum_ext();
                obj[7] = auto.getDom().getNum_int();
                obj[8] = auto.getDom().getColonia();
                obj[9] = auto.getDom().getCp();
                obj[10] = auto.getDom().getTelefono();
                obj[11] = auto.getDom().getEmail();
                obj[12] = -1;
                if(auto.getDom().getMun()!=null)
                    obj[12] = auto.getDom().getMun().getId_municipio();
                obj[13] = datos1;
                obj[14] = datos2;
                obj[15] = datos4;
                obj[16] = auto.getId_parte();
                obj[17] = partes[auto.getId_parte()];
                obj[18] = auto.getObservaciones();
                tablaT7.addRenglonArray(obj);
            }
        }    
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Funciones de los Botones">
    private void botonEsc(){
        limpiarT0();
        limpiarT1();
        limpiarT2();
        limpiarT3();
        limpiarT4();
        limpiarT5();
        limpiarT6();
        limpiarT7();
        jTabbedPane1.setSelectedIndex(0);
        t0_txtExpediente.requestFocus();
    }
    
    private void botonF2(){}
    
    private void botonF4(){
        if(validarT0()){
            ArrayList actores = null, autoridades = null, terceroAutoridades = null, terceroActores = null;
            ArrayList autoridadesAjenas = null, personasAjenas = null, autorizados = null;

            ExpedienteEntity expe = getTabbedPane0();
            if(tablaT1.getRenglones()>0) actores = getTabbedPane1(expe);
            if(tablaT2.getRenglones()>0) autoridades = getTabbedPane2();
            if(tablaT3.getRenglones()>0) terceroAutoridades = getTabbedPane3();
            if(tablaT4.getRenglones()>0) terceroActores = getTabbedPane4(expe);
            if(tablaT5.getRenglones()>0) autoridadesAjenas = getTabbedPane5();
            if(tablaT6.getRenglones()>0) personasAjenas = getTabbedPane6(expe);
            if(tablaT7.getRenglones()>0) autorizados = getTabbedPane7();
            
            ExpedienteEntity viejo = new ExpedienteEntity(lblExpediente.getText());
            
            ErrorEntity error = bl.updateExpediente(expe,viejo,sesion,actores,autoridades,terceroAutoridades,terceroActores,autoridadesAjenas,personasAjenas,autorizados);
            if(!error.getError()){
                javax.swing.JOptionPane.showInternalMessageDialog(this,"Se ha guardado correctamente", "Aviso", 1);
                botonEsc();
            }else{
                javax.swing.JOptionPane.showInternalMessageDialog(this,error.getTipo(), "Error", 0);
            }
        }
    }
    
    private void botonF5(){
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: break;
            case 1: agregarRenglonTablaT1();break;
            case 2: agregarRenglonTablaT2();break;
            case 3: agregarRenglonTablaT3();break;
            case 4: agregarRenglonTablaT4();break;
            case 5: agregarRenglonTablaT7();break;
            case 6: agregarRenglonTablaT5();break;
            case 7: agregarRenglonTablaT6();break;
        }
    }
    
    private void botonF6(){}
    
    private void botonF7(){
        switch(jTabbedPane1.getSelectedIndex()){
            case 0: break;
            case 1: quitarRenglonTabla(tablaT1);break;
            case 2: quitarRenglonTabla(tablaT2);break;
            case 3: quitarRenglonTabla(tablaT3);break;
            case 4: quitarRenglonTabla(tablaT4);break;
            case 5: quitarRenglonTabla(tablaT7);break;
            case 6: quitarRenglonTabla(tablaT5);break;
            case 7: quitarRenglonTabla(tablaT6);break;
        }
    }
    
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
        jLabel2 = new javax.swing.JLabel();
        t0_txtCantidad = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        t0_cmbTipo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        t0_dateFecha = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        t0_cmbPretension = new javax.swing.JComboBox();
        t0_txtExpediente = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        t0_txtObservaciones = new javax.swing.JTextArea();
        lblAsterisco1 = new javax.swing.JLabel();
        lblAsterisco2 = new javax.swing.JLabel();
        lblAsterisco3 = new javax.swing.JLabel();
        lblAsterisco5 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        t0_btnMasPretension = new javax.swing.JButton();
        t0_cmbActo = new javax.swing.JComboBox();
        t0_btnMasActo = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        t0_txtBuscar = new javax.swing.JTextField();
        t0_btnBuscar = new javax.swing.JButton();
        lblExpediente = new javax.swing.JLabel();
        t0_txtNumero = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        t1_cmbMunicipio = new javax.swing.JComboBox();
        t1_txtColonia = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        t1_txtCP = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        t1_txtTelefono = new javax.swing.JTextField();
        t1_txtEmail = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        t1_txtInterior = new javax.swing.JTextField();
        t1_txtExterior = new javax.swing.JTextField();
        t1_txtCalle = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        t1_txtNombre = new javax.swing.JTextField();
        t1_txtPaterno = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        t1_txtMaterno = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        t1_cbxRepresentante1 = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        t1_table = new javax.swing.JTable();
        t1_btnF7 = new javax.swing.JButton();
        t1_btnF5 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        t1_txtObservaciones = new javax.swing.JTextArea();
        jLabel25 = new javax.swing.JLabel();
        t1_seleccion = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        t1_txtGrupo1 = new javax.swing.JTextField();
        t1_cbxMantener = new javax.swing.JCheckBox();
        t1_cbxRepresentante2 = new javax.swing.JCheckBox();
        jLabel31 = new javax.swing.JLabel();
        t1_txtGrupo2 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        t2_cmbAutoridad = new javax.swing.JComboBox();
        jScrollPane7 = new javax.swing.JScrollPane();
        t2_table = new javax.swing.JTable();
        t2_btnF7 = new javax.swing.JButton();
        t2_btnF5 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        t3_cmbAutoridad = new javax.swing.JComboBox();
        jScrollPane9 = new javax.swing.JScrollPane();
        t3_table = new javax.swing.JTable();
        t3_btnF7 = new javax.swing.JButton();
        t3_btnF5 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        t4_cmbMunicipio = new javax.swing.JComboBox();
        t4_txtColonia = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        t4_txtCP = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        t4_txtTelefono = new javax.swing.JTextField();
        t4_txtEmail = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        t4_txtInterior = new javax.swing.JTextField();
        t4_txtExterior = new javax.swing.JTextField();
        t4_txtCalle = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        t4_txtNombre = new javax.swing.JTextField();
        t4_txtPaterno = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        t4_txtMaterno = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        t4_table = new javax.swing.JTable();
        t4_btnF7 = new javax.swing.JButton();
        t4_btnF5 = new javax.swing.JButton();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        t4_txtObservaciones = new javax.swing.JTextArea();
        jLabel66 = new javax.swing.JLabel();
        t4_seleccion = new javax.swing.JLabel();
        t4_cbxMantener = new javax.swing.JCheckBox();
        jPanel7 = new javax.swing.JPanel();
        t7_btnF7 = new javax.swing.JButton();
        t7_btnF5 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        t7_table = new javax.swing.JTable();
        jLabel67 = new javax.swing.JLabel();
        t7_cmbAutorizador = new javax.swing.JComboBox();
        jLabel93 = new javax.swing.JLabel();
        t7_cmbPartes = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        t7_txtObservaciones = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        t5_cmbAutoridad = new javax.swing.JComboBox();
        t5_btnF7 = new javax.swing.JButton();
        t5_btnF5 = new javax.swing.JButton();
        jScrollPane15 = new javax.swing.JScrollPane();
        t5_table = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        t6_txtNombre = new javax.swing.JTextField();
        t6_txtPaterno = new javax.swing.JTextField();
        t6_txtMaterno = new javax.swing.JTextField();
        t6_txtCalle = new javax.swing.JTextField();
        jLabel83 = new javax.swing.JLabel();
        t6_txtExterior = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        t6_txtInterior = new javax.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        t6_txtColonia = new javax.swing.JTextField();
        jLabel87 = new javax.swing.JLabel();
        t6_txtCP = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        t6_txtTelefono = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        t6_txtEmail = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        t6_cmbMunicipio = new javax.swing.JComboBox();
        jLabel92 = new javax.swing.JLabel();
        jScrollPane16 = new javax.swing.JScrollPane();
        t6_txtObservaciones = new javax.swing.JTextArea();
        t6_btnF7 = new javax.swing.JButton();
        t6_btnF5 = new javax.swing.JButton();
        jScrollPane17 = new javax.swing.JScrollPane();
        t6_table = new javax.swing.JTable();
        t6_seleccion = new javax.swing.JLabel();
        t6_cbxMantener = new javax.swing.JCheckBox();

        setClosable(true);
        setTitle("Modificar Expediente");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Mazo.jpg"))); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/pri_modificar_expediente.jpg"))); // NOI18N

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
        btnF2.setText("<html><body><center>F4 - Guardar<br>Cambios</center></body></html>");
        btnF2.setFocusable(false);
        btnF2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnF2ActionPerformed(evt);
            }
        });

        jTabbedPane1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        jPanel0.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Acto Impugnado");
        jPanel0.add(jLabel2);
        jLabel2.setBounds(70, 240, 120, 30);

        t0_txtCantidad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtCantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel0.add(t0_txtCantidad);
        t0_txtCantidad.setBounds(200, 380, 150, 30);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Número de Expediente");
        jPanel0.add(jLabel3);
        jLabel3.setBounds(40, 90, 150, 30);

        t0_cmbTipo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel0.add(t0_cmbTipo);
        t0_cmbTipo.setBounds(200, 190, 460, 30);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de Procedimiento");
        jPanel0.add(jLabel4);
        jLabel4.setBounds(40, 190, 150, 30);

        t0_dateFecha.setDateFormatString("dd/MMM/yyyy");
        t0_dateFecha.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel0.add(t0_dateFecha);
        t0_dateFecha.setBounds(200, 140, 140, 30);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Fecha");
        jPanel0.add(jLabel5);
        jLabel5.setBounds(140, 140, 50, 30);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Cantidad de la Pretensión");
        jPanel0.add(jLabel6);
        jLabel6.setBounds(10, 380, 180, 30);

        t0_cmbPretension.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel0.add(t0_cmbPretension);
        t0_cmbPretension.setBounds(200, 330, 460, 30);

        t0_txtExpediente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        t0_txtExpediente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel0.add(t0_txtExpediente);
        t0_txtExpediente.setBounds(200, 90, 140, 30);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Observaciones");
        jPanel0.add(jLabel8);
        jLabel8.setBounds(30, 420, 160, 30);

        t0_txtObservaciones.setColumns(20);
        t0_txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtObservaciones.setLineWrap(true);
        t0_txtObservaciones.setRows(5);
        t0_txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t0_txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(t0_txtObservaciones);

        jPanel0.add(jScrollPane2);
        jScrollPane2.setBounds(200, 420, 630, 160);

        lblAsterisco1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblAsterisco1.setForeground(new java.awt.Color(255, 0, 0));
        lblAsterisco1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAsterisco1.setText("*");
        jPanel0.add(lblAsterisco1);
        lblAsterisco1.setBounds(10, 90, 30, 30);

        lblAsterisco2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblAsterisco2.setForeground(new java.awt.Color(255, 0, 0));
        lblAsterisco2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAsterisco2.setText("*");
        jPanel0.add(lblAsterisco2);
        lblAsterisco2.setBounds(130, 140, 20, 30);

        lblAsterisco3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblAsterisco3.setForeground(new java.awt.Color(255, 0, 0));
        lblAsterisco3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAsterisco3.setText("*");
        jPanel0.add(lblAsterisco3);
        lblAsterisco3.setBounds(20, 190, 30, 30);

        lblAsterisco5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lblAsterisco5.setForeground(new java.awt.Color(255, 0, 0));
        lblAsterisco5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAsterisco5.setText("*");
        jPanel0.add(lblAsterisco5);
        lblAsterisco5.setBounds(60, 240, 20, 30);

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel69.setText("Pretensión Aducida");
        jPanel0.add(jLabel69);
        jLabel69.setBounds(50, 330, 140, 30);

        t0_btnMasPretension.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_btnMasPretension.setText("+");
        t0_btnMasPretension.setFocusable(false);
        t0_btnMasPretension.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t0_btnMasPretensionActionPerformed(evt);
            }
        });
        jPanel0.add(t0_btnMasPretension);
        t0_btnMasPretension.setBounds(660, 330, 40, 30);

        t0_cmbActo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel0.add(t0_cmbActo);
        t0_cmbActo.setBounds(200, 240, 600, 30);

        t0_btnMasActo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_btnMasActo.setText("+");
        t0_btnMasActo.setFocusable(false);
        t0_btnMasActo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t0_btnMasActoActionPerformed(evt);
            }
        });
        jPanel0.add(t0_btnMasActo);
        t0_btnMasActo.setBounds(800, 240, 40, 30);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel10.setLayout(null);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel10.setText("Número de Expediente a buscar");
        jPanel10.add(jLabel10);
        jLabel10.setBounds(80, 20, 220, 30);

        t0_txtBuscar.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t0_txtBuscar.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t0_txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t0_txtBuscarKeyPressed(evt);
            }
        });
        jPanel10.add(t0_txtBuscar);
        t0_txtBuscar.setBounds(300, 20, 150, 30);

        t0_btnBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t0_btnBuscar.setText("Buscar Expediente");
        t0_btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t0_btnBuscarActionPerformed(evt);
            }
        });
        jPanel10.add(t0_btnBuscar);
        t0_btnBuscar.setBounds(470, 20, 170, 30);
        jPanel10.add(lblExpediente);
        lblExpediente.setBounds(710, 20, 90, 40);

        jPanel0.add(jPanel10);
        jPanel10.setBounds(10, 10, 830, 70);

        t0_txtNumero.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        t0_txtNumero.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        t0_txtNumero.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t0_txtNumeroKeyPressed(evt);
            }
        });
        jPanel0.add(t0_txtNumero);
        t0_txtNumero.setBounds(200, 280, 150, 30);

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel80.setText("Oficio impugnado");
        jPanel0.add(jLabel80);
        jLabel80.setBounds(50, 280, 140, 30);

        jTabbedPane1.addTab("Expediente", jPanel0);

        jPanel1.setLayout(null);

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel24.setText("Asociación o Grupo que representa o pertenece");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(210, 160, 330, 40);

        t1_cmbMunicipio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_cmbMunicipio);
        t1_cmbMunicipio.setBounds(80, 130, 340, 30);

        t1_txtColonia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_txtColonia);
        t1_txtColonia.setBounds(190, 90, 230, 30);

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel38.setText("Colonia");
        jPanel1.add(jLabel38);
        jLabel38.setBounds(190, 60, 170, 40);

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 0, 0));
        jLabel37.setText("*");
        jPanel1.add(jLabel37);
        jLabel37.setBounds(430, 0, 30, 40);

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel39.setText("C.P.");
        jPanel1.add(jLabel39);
        jLabel39.setBounds(430, 60, 50, 40);

        t1_txtCP.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtCP.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(t1_txtCP);
        t1_txtCP.setBounds(430, 90, 80, 30);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel40.setText("Teléfono");
        jPanel1.add(jLabel40);
        jLabel40.setBounds(520, 60, 90, 40);

        t1_txtTelefono.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtTelefono.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(t1_txtTelefono);
        t1_txtTelefono.setBounds(520, 90, 110, 30);

        t1_txtEmail.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtEmail.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(t1_txtEmail);
        t1_txtEmail.setBounds(640, 90, 200, 30);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel41.setText("E-mail");
        jPanel1.add(jLabel41);
        jLabel41.setBounds(640, 60, 60, 40);

        t1_txtInterior.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtInterior.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(t1_txtInterior);
        t1_txtInterior.setBounds(100, 90, 80, 30);

        t1_txtExterior.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtExterior.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(t1_txtExterior);
        t1_txtExterior.setBounds(10, 90, 80, 30);

        t1_txtCalle.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_txtCalle);
        t1_txtCalle.setBounds(640, 30, 200, 30);

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel26.setText("Calle");
        jPanel1.add(jLabel26);
        jLabel26.setBounds(640, 0, 120, 40);

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel35.setText("No. Ext.");
        jPanel1.add(jLabel35);
        jLabel35.setBounds(10, 60, 60, 40);

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel36.setText("No. Int.");
        jPanel1.add(jLabel36);
        jLabel36.setBounds(100, 60, 60, 40);

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel20.setText("Nombre");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(20, 0, 90, 40);

        t1_txtNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t1_txtNombreKeyPressed(evt);
            }
        });
        jPanel1.add(t1_txtNombre);
        t1_txtNombre.setBounds(10, 30, 200, 30);

        t1_txtPaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_txtPaterno);
        t1_txtPaterno.setBounds(220, 30, 200, 30);

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel22.setText("A. Paterno");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(230, 0, 90, 40);

        t1_txtMaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_txtMaterno);
        t1_txtMaterno.setBounds(430, 30, 200, 30);

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel23.setText("A. Materno");
        jPanel1.add(jLabel23);
        jLabel23.setBounds(440, 0, 90, 40);

        t1_cbxRepresentante1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_cbxRepresentante1.setText("Es Representante Legal");
        jPanel1.add(t1_cbxRepresentante1);
        t1_cbxRepresentante1.setBounds(10, 190, 190, 30);

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
        t1_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t1_tableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(t1_table);

        jPanel1.add(jScrollPane4);
        jScrollPane4.setBounds(10, 420, 830, 160);

        t1_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_btnF7.setText("F7 - Quitar Actor");
        t1_btnF7.setFocusable(false);
        t1_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1_btnF7ActionPerformed(evt);
            }
        });
        jPanel1.add(t1_btnF7);
        t1_btnF7.setBounds(10, 380, 160, 30);

        t1_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_btnF5.setText("F5 - Agregar Actor");
        t1_btnF5.setFocusable(false);
        t1_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1_btnF5ActionPerformed(evt);
            }
        });
        jPanel1.add(t1_btnF5);
        t1_btnF5.setBounds(680, 380, 160, 30);

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 0, 0));
        jLabel44.setText("*");
        jPanel1.add(jLabel44);
        jLabel44.setBounds(10, 0, 30, 40);

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 0, 0));
        jLabel45.setText("*");
        jPanel1.add(jLabel45);
        jLabel45.setBounds(220, 0, 30, 40);

        t1_txtObservaciones.setColumns(20);
        t1_txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t1_txtObservaciones.setLineWrap(true);
        t1_txtObservaciones.setRows(2);
        t1_txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t1_txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane5.setViewportView(t1_txtObservaciones);

        jPanel1.add(jScrollPane5);
        jScrollPane5.setBounds(10, 310, 830, 60);

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel25.setText("Municipio");
        jPanel1.add(jLabel25);
        jLabel25.setBounds(10, 130, 70, 30);
        jPanel1.add(t1_seleccion);
        t1_seleccion.setBounds(210, 380, 50, 30);

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel68.setText("Observaciones");
        jPanel1.add(jLabel68);
        jLabel68.setBounds(10, 280, 110, 40);

        t1_txtGrupo1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_txtGrupo1);
        t1_txtGrupo1.setBounds(210, 190, 630, 30);

        t1_cbxMantener.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_cbxMantener.setText("Mantener domicilio para otros actores");
        t1_cbxMantener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t1_cbxMantenerActionPerformed(evt);
            }
        });
        jPanel1.add(t1_cbxMantener);
        t1_cbxMantener.setBounds(430, 130, 310, 30);

        t1_cbxRepresentante2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t1_cbxRepresentante2.setText("Es Representante Legal");
        jPanel1.add(t1_cbxRepresentante2);
        t1_cbxRepresentante2.setBounds(10, 250, 190, 30);

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel31.setText("Asociación o Grupo que representa o pertenece");
        jPanel1.add(jLabel31);
        jLabel31.setBounds(210, 220, 330, 40);

        t1_txtGrupo2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel1.add(t1_txtGrupo2);
        t1_txtGrupo2.setBounds(210, 250, 630, 30);

        jTabbedPane1.addTab("Actores", jPanel1);

        jPanel2.setLayout(null);

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel11.setLayout(null);

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel29.setText("Autoridades");
        jPanel11.add(jLabel29);
        jLabel29.setBounds(10, 20, 120, 40);

        t2_cmbAutoridad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel11.add(t2_cmbAutoridad);
        t2_cmbAutoridad.setBounds(10, 50, 810, 30);

        jPanel2.add(jPanel11);
        jPanel11.setBounds(10, 10, 830, 120);

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
        jScrollPane7.setBounds(10, 200, 830, 380);

        t2_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t2_btnF7.setText("F7 - Quitar Autoridad");
        t2_btnF7.setFocusable(false);
        t2_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2_btnF7ActionPerformed(evt);
            }
        });
        jPanel2.add(t2_btnF7);
        t2_btnF7.setBounds(10, 150, 200, 30);

        t2_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t2_btnF5.setText("F5 - Agregar Autoridad");
        t2_btnF5.setFocusable(false);
        t2_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t2_btnF5ActionPerformed(evt);
            }
        });
        jPanel2.add(t2_btnF5);
        t2_btnF5.setBounds(640, 150, 200, 30);

        jTabbedPane1.addTab("Autoridades", jPanel2);

        jPanel3.setLayout(null);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel13.setLayout(null);

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel46.setText("Autoridades");
        jPanel13.add(jLabel46);
        jLabel46.setBounds(10, 30, 120, 20);

        t3_cmbAutoridad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel13.add(t3_cmbAutoridad);
        t3_cmbAutoridad.setBounds(10, 50, 810, 30);

        jPanel3.add(jPanel13);
        jPanel13.setBounds(10, 10, 830, 120);

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
        jScrollPane9.setBounds(10, 200, 830, 380);

        t3_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t3_btnF7.setText("F7 - Quitar Autoridad");
        t3_btnF7.setFocusable(false);
        t3_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t3_btnF7ActionPerformed(evt);
            }
        });
        jPanel3.add(t3_btnF7);
        t3_btnF7.setBounds(10, 150, 200, 30);

        t3_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t3_btnF5.setText("F5 - Agregar Autoridad");
        t3_btnF5.setFocusable(false);
        t3_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t3_btnF5ActionPerformed(evt);
            }
        });
        jPanel3.add(t3_btnF5);
        t3_btnF5.setBounds(640, 150, 200, 30);

        jTabbedPane1.addTab("Tercero Autoridades", jPanel3);

        jPanel4.setLayout(null);

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel48.setText("Observaciones");
        jPanel4.add(jLabel48);
        jLabel48.setBounds(10, 170, 110, 40);

        t4_cmbMunicipio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_cmbMunicipio);
        t4_cmbMunicipio.setBounds(90, 140, 340, 30);

        t4_txtColonia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtColonia);
        t4_txtColonia.setBounds(230, 100, 200, 30);

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel49.setText("Colonia");
        jPanel4.add(jLabel49);
        jLabel49.setBounds(230, 70, 120, 30);

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 0, 0));
        jLabel50.setText("*");
        jPanel4.add(jLabel50);
        jLabel50.setBounds(430, 10, 30, 30);

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel51.setText("C.P.");
        jPanel4.add(jLabel51);
        jLabel51.setBounds(440, 70, 50, 30);

        t4_txtCP.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtCP);
        t4_txtCP.setBounds(440, 100, 80, 30);

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel52.setText("Teléfono");
        jPanel4.add(jLabel52);
        jLabel52.setBounds(530, 70, 90, 30);

        t4_txtTelefono.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtTelefono);
        t4_txtTelefono.setBounds(530, 100, 110, 30);

        t4_txtEmail.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtEmail);
        t4_txtEmail.setBounds(650, 100, 190, 30);

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel53.setText("E-mail");
        jPanel4.add(jLabel53);
        jLabel53.setBounds(650, 70, 60, 30);

        t4_txtInterior.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtInterior);
        t4_txtInterior.setBounds(120, 100, 100, 30);

        t4_txtExterior.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtExterior);
        t4_txtExterior.setBounds(10, 100, 100, 30);

        t4_txtCalle.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtCalle);
        t4_txtCalle.setBounds(640, 40, 200, 30);

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel55.setText("Calle");
        jPanel4.add(jLabel55);
        jLabel55.setBounds(640, 10, 130, 30);

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel57.setText("No. Ext.");
        jPanel4.add(jLabel57);
        jLabel57.setBounds(10, 70, 90, 30);

        jLabel58.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel58.setText("No. Int.");
        jPanel4.add(jLabel58);
        jLabel58.setBounds(120, 70, 60, 30);

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel59.setText("Nombre");
        jPanel4.add(jLabel59);
        jLabel59.setBounds(20, 10, 90, 30);

        t4_txtNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t4_txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t4_txtNombreKeyPressed(evt);
            }
        });
        jPanel4.add(t4_txtNombre);
        t4_txtNombre.setBounds(10, 40, 195, 30);

        t4_txtPaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtPaterno);
        t4_txtPaterno.setBounds(220, 40, 195, 30);

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel60.setText("A. Paterno");
        jPanel4.add(jLabel60);
        jLabel60.setBounds(230, 10, 90, 30);

        t4_txtMaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel4.add(t4_txtMaterno);
        t4_txtMaterno.setBounds(430, 40, 195, 30);

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel61.setText("A. Materno");
        jPanel4.add(jLabel61);
        jLabel61.setBounds(440, 10, 90, 30);

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
        t4_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t4_tableMouseClicked(evt);
            }
        });
        jScrollPane10.setViewportView(t4_table);

        jPanel4.add(jScrollPane10);
        jScrollPane10.setBounds(10, 320, 830, 260);

        t4_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t4_btnF7.setText("F7 - Quitar Actor");
        t4_btnF7.setFocusable(false);
        t4_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t4_btnF7ActionPerformed(evt);
            }
        });
        jPanel4.add(t4_btnF7);
        t4_btnF7.setBounds(10, 280, 160, 30);

        t4_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t4_btnF5.setText("F5 - Agregar Actor");
        t4_btnF5.setFocusable(false);
        t4_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t4_btnF5ActionPerformed(evt);
            }
        });
        jPanel4.add(t4_btnF5);
        t4_btnF5.setBounds(680, 280, 160, 30);

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 0, 0));
        jLabel64.setText("*");
        jPanel4.add(jLabel64);
        jLabel64.setBounds(10, 10, 30, 30);

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(255, 0, 0));
        jLabel65.setText("*");
        jPanel4.add(jLabel65);
        jLabel65.setBounds(220, 10, 30, 30);

        t4_txtObservaciones.setColumns(20);
        t4_txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t4_txtObservaciones.setLineWrap(true);
        t4_txtObservaciones.setRows(2);
        t4_txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t4_txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane11.setViewportView(t4_txtObservaciones);

        jPanel4.add(jScrollPane11);
        jScrollPane11.setBounds(10, 200, 830, 60);

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel66.setText("Municipio");
        jPanel4.add(jLabel66);
        jLabel66.setBounds(10, 140, 80, 30);
        jPanel4.add(t4_seleccion);
        t4_seleccion.setBounds(290, 280, 60, 30);

        t4_cbxMantener.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t4_cbxMantener.setText("Mantener domicilio para otros actores");
        t4_cbxMantener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t4_cbxMantenerActionPerformed(evt);
            }
        });
        jPanel4.add(t4_cbxMantener);
        t4_cbxMantener.setBounds(440, 140, 310, 30);

        jTabbedPane1.addTab("Tercero Actores", jPanel4);

        jPanel7.setLayout(null);

        t7_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t7_btnF7.setText("F7 - Quitar Autorizado");
        t7_btnF7.setFocusable(false);
        t7_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t7_btnF7ActionPerformed(evt);
            }
        });
        jPanel7.add(t7_btnF7);
        t7_btnF7.setBounds(10, 290, 230, 30);

        t7_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t7_btnF5.setText("F5 - Agregar Autorizado");
        t7_btnF5.setFocusable(false);
        t7_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t7_btnF5ActionPerformed(evt);
            }
        });
        jPanel7.add(t7_btnF5);
        t7_btnF5.setBounds(620, 290, 210, 30);

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
        jScrollPane12.setBounds(10, 330, 820, 240);

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel67.setText("Representa a la Parte");
        jPanel7.add(jLabel67);
        jLabel67.setBounds(10, 100, 150, 30);

        t7_cmbAutorizador.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel7.add(t7_cmbAutorizador);
        t7_cmbAutorizador.setBounds(170, 60, 660, 30);

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel93.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel93.setText("Autorizados");
        jPanel7.add(jLabel93);
        jLabel93.setBounds(10, 60, 150, 30);

        t7_cmbPartes.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t7_cmbPartes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "[Elija una opción]", "ACTORES", "AUTORIDADES", "TERCERO AUTORIDADES", "TERCERO ACTORES", "AUTORIDADES AJENAS", "PERSONAS AJENAS" }));
        jPanel7.add(t7_cmbPartes);
        t7_cmbPartes.setBounds(170, 100, 260, 30);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Observaciones");
        jPanel7.add(jLabel9);
        jLabel9.setBounds(10, 140, 150, 30);

        t7_txtObservaciones.setColumns(20);
        t7_txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t7_txtObservaciones.setLineWrap(true);
        t7_txtObservaciones.setRows(5);
        t7_txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t7_txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(t7_txtObservaciones);

        jPanel7.add(jScrollPane3);
        jScrollPane3.setBounds(170, 140, 660, 90);

        jTabbedPane1.addTab("Autorizados", jPanel7);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanel16.setLayout(null);

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel72.setText("Autoridades");
        jPanel16.add(jLabel72);
        jLabel72.setBounds(10, 30, 120, 20);

        t5_cmbAutoridad.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel16.add(t5_cmbAutoridad);
        t5_cmbAutoridad.setBounds(10, 50, 810, 30);

        t5_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t5_btnF7.setText("F7 - Quitar Autoridad");
        t5_btnF7.setFocusable(false);
        t5_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t5_btnF7ActionPerformed(evt);
            }
        });

        t5_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t5_btnF5.setText("F5 - Agregar Autoridad");
        t5_btnF5.setFocusable(false);
        t5_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t5_btnF5ActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(t5_btnF7, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(430, 430, 430)
                .addComponent(t5_btnF5, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(t5_btnF7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(t5_btnF5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(403, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel5Layout.createSequentialGroup()
                    .addGap(0, 15, Short.MAX_VALUE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(77, 77, 77)
                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 11, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Autoridades Ajenas", jPanel5);

        jPanel6.setLayout(null);

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(255, 0, 0));
        jLabel74.setText("*");
        jPanel6.add(jLabel74);
        jLabel74.setBounds(8, 10, 30, 30);

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel75.setText("Nombre");
        jPanel6.add(jLabel75);
        jLabel75.setBounds(18, 10, 90, 30);

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 0, 0));
        jLabel76.setText("*");
        jPanel6.add(jLabel76);
        jLabel76.setBounds(218, 10, 30, 30);

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel77.setText("A. Paterno");
        jPanel6.add(jLabel77);
        jLabel77.setBounds(228, 10, 90, 30);

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 0, 0));
        jLabel78.setText("*");
        jPanel6.add(jLabel78);
        jLabel78.setBounds(428, 10, 30, 30);

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel79.setText("A. Materno");
        jPanel6.add(jLabel79);
        jLabel79.setBounds(438, 10, 90, 30);

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel81.setText("Calle");
        jPanel6.add(jLabel81);
        jLabel81.setBounds(638, 10, 130, 30);

        t6_txtNombre.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t6_txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t6_txtNombreKeyPressed(evt);
            }
        });
        jPanel6.add(t6_txtNombre);
        t6_txtNombre.setBounds(8, 40, 195, 30);

        t6_txtPaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtPaterno);
        t6_txtPaterno.setBounds(218, 40, 195, 30);

        t6_txtMaterno.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtMaterno);
        t6_txtMaterno.setBounds(428, 40, 195, 30);

        t6_txtCalle.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtCalle);
        t6_txtCalle.setBounds(638, 40, 200, 30);

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel83.setText("No. Ext.");
        jPanel6.add(jLabel83);
        jLabel83.setBounds(8, 70, 88, 30);

        t6_txtExterior.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtExterior);
        t6_txtExterior.setBounds(8, 100, 100, 30);

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel84.setText("No. Int.");
        jPanel6.add(jLabel84);
        jLabel84.setBounds(118, 70, 58, 30);

        t6_txtInterior.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtInterior);
        t6_txtInterior.setBounds(118, 100, 100, 30);

        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel86.setText("Colonia");
        jPanel6.add(jLabel86);
        jLabel86.setBounds(228, 70, 118, 30);

        t6_txtColonia.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtColonia);
        t6_txtColonia.setBounds(228, 100, 200, 30);

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel87.setText("C.P.");
        jPanel6.add(jLabel87);
        jLabel87.setBounds(438, 70, 48, 30);

        t6_txtCP.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtCP);
        t6_txtCP.setBounds(438, 100, 80, 30);

        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel88.setText("Teléfono");
        jPanel6.add(jLabel88);
        jLabel88.setBounds(528, 70, 88, 30);

        t6_txtTelefono.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtTelefono);
        t6_txtTelefono.setBounds(528, 100, 110, 30);

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel89.setText("E-mail");
        jPanel6.add(jLabel89);
        jLabel89.setBounds(648, 70, 58, 30);

        t6_txtEmail.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_txtEmail);
        t6_txtEmail.setBounds(648, 100, 190, 30);

        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel90.setText("Municipio");
        jPanel6.add(jLabel90);
        jLabel90.setBounds(8, 140, 78, 30);

        t6_cmbMunicipio.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jPanel6.add(t6_cmbMunicipio);
        t6_cmbMunicipio.setBounds(90, 140, 340, 30);

        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel92.setText("Observaciones");
        jPanel6.add(jLabel92);
        jLabel92.setBounds(8, 170, 110, 40);

        t6_txtObservaciones.setColumns(20);
        t6_txtObservaciones.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        t6_txtObservaciones.setLineWrap(true);
        t6_txtObservaciones.setRows(2);
        t6_txtObservaciones.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                t6_txtObservacionesKeyPressed(evt);
            }
        });
        jScrollPane16.setViewportView(t6_txtObservaciones);

        jPanel6.add(jScrollPane16);
        jScrollPane16.setBounds(8, 200, 830, 60);

        t6_btnF7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t6_btnF7.setText("F7 - Quitar Persona");
        t6_btnF7.setFocusable(false);
        t6_btnF7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t6_btnF7ActionPerformed(evt);
            }
        });
        jPanel6.add(t6_btnF7);
        t6_btnF7.setBounds(8, 280, 176, 30);

        t6_btnF5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t6_btnF5.setText("F5 - Agregar Persona");
        t6_btnF5.setFocusable(false);
        t6_btnF5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t6_btnF5ActionPerformed(evt);
            }
        });
        jPanel6.add(t6_btnF5);
        t6_btnF5.setBounds(645, 280, 193, 30);

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
        t6_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                t6_tableMouseClicked(evt);
            }
        });
        jScrollPane17.setViewportView(t6_table);

        jPanel6.add(jScrollPane17);
        jScrollPane17.setBounds(8, 320, 830, 260);
        jPanel6.add(t6_seleccion);
        t6_seleccion.setBounds(240, 280, 52, 30);

        t6_cbxMantener.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        t6_cbxMantener.setText("Mantener domicilio para otros actores");
        t6_cbxMantener.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                t6_cbxMantenerActionPerformed(evt);
            }
        });
        jPanel6.add(t6_cbxMantener);
        t6_cbxMantener.setBounds(440, 140, 310, 30);

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
            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnF2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnF12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnF2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF2ActionPerformed
        botonF4();
    }//GEN-LAST:event_btnF2ActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
        botonEsc();
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnF12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnF12ActionPerformed
        botonF12();
    }//GEN-LAST:event_btnF12ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        t0_txtBuscar.requestFocus();
    }//GEN-LAST:event_formComponentShown

    private void t4_txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t4_txtObservacionesKeyPressed
        u.focus(evt, t4_txtObservaciones);
    }//GEN-LAST:event_t4_txtObservacionesKeyPressed

    private void t4_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t4_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t4_btnF5ActionPerformed

    private void t4_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t4_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t4_btnF7ActionPerformed

    private void t4_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t4_tableMouseClicked
        if(tablaT4.getRenglones()>0 && evt.getClickCount()==2){
            int row = tablaT4.getSelectedRow();
            if(row!=-1){
                obtenerDatosTablaT4(row);
            }
        }
    }//GEN-LAST:event_t4_tableMouseClicked

    private void t3_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t3_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t3_btnF5ActionPerformed

    private void t3_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t3_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t3_btnF7ActionPerformed

    private void t7_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t7_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t7_btnF5ActionPerformed

    private void t7_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t7_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t7_btnF7ActionPerformed

    private void t2_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t2_btnF5ActionPerformed

    private void t2_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t2_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t2_btnF7ActionPerformed

    private void t1_txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t1_txtObservacionesKeyPressed
        u.focus(evt, t1_txtObservaciones);
    }//GEN-LAST:event_t1_txtObservacionesKeyPressed

    private void t1_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t1_btnF5ActionPerformed

    private void t1_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t1_btnF7ActionPerformed

    private void t1_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t1_tableMouseClicked
        if(tablaT1.getRenglones()>0 && evt.getClickCount()==2){
            int row = tablaT1.getSelectedRow();
            if(row!=-1){
                obtenerDatosTablaT1(row);
            }
        }
    }//GEN-LAST:event_t1_tableMouseClicked

    private void t0_txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t0_txtObservacionesKeyPressed
        u.focus(evt, t0_txtObservaciones);
    }//GEN-LAST:event_t0_txtObservacionesKeyPressed

    private void t5_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t5_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t5_btnF7ActionPerformed

    private void t5_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t5_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t5_btnF5ActionPerformed

    private void t6_txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t6_txtObservacionesKeyPressed
        u.focus(evt, t6_txtObservaciones);
    }//GEN-LAST:event_t6_txtObservacionesKeyPressed

    private void t6_btnF7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t6_btnF7ActionPerformed
        botonF7();
    }//GEN-LAST:event_t6_btnF7ActionPerformed

    private void t6_btnF5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t6_btnF5ActionPerformed
        botonF5();
    }//GEN-LAST:event_t6_btnF5ActionPerformed

    private void t6_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_t6_tableMouseClicked
        if(tablaT6.getRenglones()>0 && evt.getClickCount()==2){
            int row = tablaT6.getSelectedRow();
            if(row!=-1){
                obtenerDatosTablaT6(row);
            }
        }
    }//GEN-LAST:event_t6_tableMouseClicked

    private void t0_btnMasPretensionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t0_btnMasPretensionActionPerformed
        TipoPretensionEntity tipo = new TipoPretensionEntity("-1","","");
        AgregarPretension agrega = new AgregarPretension(null,true,tipo,sesion);
        agrega.setLocationRelativeTo(null);
        agrega.show();
        if(!tipo.getId_tipo_pretension().equals("-1")){
            u.inicializaCombo(t0_cmbPretension, new TipoPretensionEntity(), blPre.getTipoPretensiones());
            t0_cmbPretension.setSelectedItem(tipo);
        }
    }//GEN-LAST:event_t0_btnMasPretensionActionPerformed

    private void t0_btnMasActoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t0_btnMasActoActionPerformed
        ActoEntity acto = new ActoEntity("-1","","");
        AgregarActo agrega = new AgregarActo(null,true,acto,sesion);
        agrega.setLocationRelativeTo(null);
        agrega.show();
        if(!acto.getId_acto().equals("-1")){
            t1_autocomplete.uninstall();
            EventList<ActoEntity> items = new BasicEventList<>();
            ArrayList array = blActo.getActos();
            items.add(new ActoEntity());
            for(Object obj : array)
                items.add((ActoEntity)obj);
            t1_autocomplete = AutoCompleteSupport.install(t0_cmbActo, items);
            t1_autocomplete.setFilterMode(TextMatcherEditor.CONTAINS);
            seleccionarDefaultActo(acto);            
        }
    }//GEN-LAST:event_t0_btnMasActoActionPerformed

    private void t1_cbxMantenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t1_cbxMantenerActionPerformed
        if(t1_cbxMantener.isSelected()){
            u.editabledComponet(false,t1_txtCalle,t1_txtExterior,t1_txtInterior,t1_txtColonia,t1_txtCP);
            t1_cmbMunicipio.setEnabled(false);
        }else{
            u.editabledComponet(true,t1_txtCalle,t1_txtExterior,t1_txtInterior,t1_txtColonia,t1_txtCP);
            t1_cmbMunicipio.setEnabled(true);
        }
    }//GEN-LAST:event_t1_cbxMantenerActionPerformed

    private void t4_cbxMantenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t4_cbxMantenerActionPerformed
        if(t4_cbxMantener.isSelected()){
            u.editabledComponet(false,t4_txtCalle,t4_txtExterior,t4_txtInterior,t4_txtColonia,t4_txtCP);
            t4_cmbMunicipio.setEnabled(false);
        }else{
            u.editabledComponet(true,t4_txtCalle,t4_txtExterior,t4_txtInterior,t4_txtColonia,t4_txtCP);
            t4_cmbMunicipio.setEnabled(true);
        }
    }//GEN-LAST:event_t4_cbxMantenerActionPerformed

    private void t6_cbxMantenerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t6_cbxMantenerActionPerformed
        if(t6_cbxMantener.isSelected()){
            u.editabledComponet(false,t6_txtCalle,t6_txtExterior,t6_txtInterior,t6_txtColonia,t6_txtCP);
            t6_cmbMunicipio.setEnabled(false);
        }else{
            u.editabledComponet(true,t6_txtCalle,t6_txtExterior,t6_txtInterior,t6_txtColonia,t6_txtCP);
            t6_cmbMunicipio.setEnabled(true);
        }
    }//GEN-LAST:event_t6_cbxMantenerActionPerformed

    private void t7_txtObservacionesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t7_txtObservacionesKeyPressed
        u.focus(evt, t7_txtObservaciones);
    }//GEN-LAST:event_t7_txtObservacionesKeyPressed

    private void t0_btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_t0_btnBuscarActionPerformed
        cargaExpediente(t0_txtBuscar.getText());
    }//GEN-LAST:event_t0_btnBuscarActionPerformed

    private void t0_txtBuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t0_txtBuscarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            cargaExpediente(t0_txtBuscar.getText());
    }//GEN-LAST:event_t0_txtBuscarKeyPressed

    private void t0_txtNumeroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t0_txtNumeroKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if(t0_cmbActo.getItemCount()>0 && t0_cmbActo.getSelectedIndex()!=0 && t0_txtNumero.getText().length()>0){
                ActoEntity val = (ActoEntity)t0_cmbActo.getSelectedItem();
                getExisteActo(val.getId_acto(),val.getNombre(),u.eliminaEspacios(t0_txtNumero.getText()));
            }
        }
    }//GEN-LAST:event_t0_txtNumeroKeyPressed

    private void t1_txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t1_txtNombreKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            separarNombre(t1_txtNombre, t1_txtPaterno, t1_txtMaterno);
    }//GEN-LAST:event_t1_txtNombreKeyPressed

    private void t4_txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t4_txtNombreKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            separarNombre(t4_txtNombre, t4_txtPaterno, t4_txtMaterno);
    }//GEN-LAST:event_t4_txtNombreKeyPressed

    private void t6_txtNombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_t6_txtNombreKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER)
            separarNombre(t6_txtNombre, t6_txtPaterno, t6_txtMaterno);
    }//GEN-LAST:event_t6_txtNombreKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnF12;
    private javax.swing.JButton btnF2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JPanel jPanel0;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblAsterisco1;
    private javax.swing.JLabel lblAsterisco2;
    private javax.swing.JLabel lblAsterisco3;
    private javax.swing.JLabel lblAsterisco5;
    private javax.swing.JLabel lblExpediente;
    private javax.swing.JButton t0_btnBuscar;
    private javax.swing.JButton t0_btnMasActo;
    private javax.swing.JButton t0_btnMasPretension;
    private javax.swing.JComboBox t0_cmbActo;
    private javax.swing.JComboBox t0_cmbPretension;
    private javax.swing.JComboBox t0_cmbTipo;
    private com.toedter.calendar.JDateChooser t0_dateFecha;
    private javax.swing.JTextField t0_txtBuscar;
    private javax.swing.JTextField t0_txtCantidad;
    private javax.swing.JTextField t0_txtExpediente;
    private javax.swing.JTextField t0_txtNumero;
    private javax.swing.JTextArea t0_txtObservaciones;
    private javax.swing.JButton t1_btnF5;
    private javax.swing.JButton t1_btnF7;
    private javax.swing.JCheckBox t1_cbxMantener;
    private javax.swing.JCheckBox t1_cbxRepresentante1;
    private javax.swing.JCheckBox t1_cbxRepresentante2;
    private javax.swing.JComboBox t1_cmbMunicipio;
    private javax.swing.JLabel t1_seleccion;
    private javax.swing.JTable t1_table;
    private javax.swing.JTextField t1_txtCP;
    private javax.swing.JTextField t1_txtCalle;
    private javax.swing.JTextField t1_txtColonia;
    private javax.swing.JTextField t1_txtEmail;
    private javax.swing.JTextField t1_txtExterior;
    private javax.swing.JTextField t1_txtGrupo1;
    private javax.swing.JTextField t1_txtGrupo2;
    private javax.swing.JTextField t1_txtInterior;
    private javax.swing.JTextField t1_txtMaterno;
    private javax.swing.JTextField t1_txtNombre;
    private javax.swing.JTextArea t1_txtObservaciones;
    private javax.swing.JTextField t1_txtPaterno;
    private javax.swing.JTextField t1_txtTelefono;
    private javax.swing.JButton t2_btnF5;
    private javax.swing.JButton t2_btnF7;
    private javax.swing.JComboBox t2_cmbAutoridad;
    private javax.swing.JTable t2_table;
    private javax.swing.JButton t3_btnF5;
    private javax.swing.JButton t3_btnF7;
    private javax.swing.JComboBox t3_cmbAutoridad;
    private javax.swing.JTable t3_table;
    private javax.swing.JButton t4_btnF5;
    private javax.swing.JButton t4_btnF7;
    private javax.swing.JCheckBox t4_cbxMantener;
    private javax.swing.JComboBox t4_cmbMunicipio;
    private javax.swing.JLabel t4_seleccion;
    private javax.swing.JTable t4_table;
    private javax.swing.JTextField t4_txtCP;
    private javax.swing.JTextField t4_txtCalle;
    private javax.swing.JTextField t4_txtColonia;
    private javax.swing.JTextField t4_txtEmail;
    private javax.swing.JTextField t4_txtExterior;
    private javax.swing.JTextField t4_txtInterior;
    private javax.swing.JTextField t4_txtMaterno;
    private javax.swing.JTextField t4_txtNombre;
    private javax.swing.JTextArea t4_txtObservaciones;
    private javax.swing.JTextField t4_txtPaterno;
    private javax.swing.JTextField t4_txtTelefono;
    private javax.swing.JButton t5_btnF5;
    private javax.swing.JButton t5_btnF7;
    private javax.swing.JComboBox t5_cmbAutoridad;
    private javax.swing.JTable t5_table;
    private javax.swing.JButton t6_btnF5;
    private javax.swing.JButton t6_btnF7;
    private javax.swing.JCheckBox t6_cbxMantener;
    private javax.swing.JComboBox t6_cmbMunicipio;
    private javax.swing.JLabel t6_seleccion;
    private javax.swing.JTable t6_table;
    private javax.swing.JTextField t6_txtCP;
    private javax.swing.JTextField t6_txtCalle;
    private javax.swing.JTextField t6_txtColonia;
    private javax.swing.JTextField t6_txtEmail;
    private javax.swing.JTextField t6_txtExterior;
    private javax.swing.JTextField t6_txtInterior;
    private javax.swing.JTextField t6_txtMaterno;
    private javax.swing.JTextField t6_txtNombre;
    private javax.swing.JTextArea t6_txtObservaciones;
    private javax.swing.JTextField t6_txtPaterno;
    private javax.swing.JTextField t6_txtTelefono;
    private javax.swing.JButton t7_btnF5;
    private javax.swing.JButton t7_btnF7;
    private javax.swing.JComboBox t7_cmbAutorizador;
    private javax.swing.JComboBox t7_cmbPartes;
    private javax.swing.JTable t7_table;
    private javax.swing.JTextArea t7_txtObservaciones;
    // End of variables declaration//GEN-END:variables
}
