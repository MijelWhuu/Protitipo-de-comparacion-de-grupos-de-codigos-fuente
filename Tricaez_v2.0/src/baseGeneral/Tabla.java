package baseGeneral;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author GSA, MAVG
 */

public class Tabla {
    JTable jTabla;
    int columnas=0;
    int renglones=0;
    ArrayList arrayColumnas=new ArrayList();
    public javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(){
        public Class getColumnClass(int c){ return getValueAt(0,c).getClass(); }
        
        @Override
        public boolean isCellEditable(int row, int col){
            Class clase=modelo.getColumnClass(col);
            //if(clase.getCanonicalName().toUpperCase().equals("JAVA.LANG.STRING"))
            //    return false;
            if(clase.getCanonicalName().toUpperCase().equals("JAVA.LANG.BOOLEAN")){
                return true;
            }
            return false;
        }
    };
    ArrayList defaultValues=new ArrayList();
    
    /*CONSTRUCTOR*/
    public Tabla(JTable jTabla){
        this.jTabla=jTabla;
    }
    
    /*VERIFICAR QUE LAS VARIABLES DE LOS TITULOS NO SE REPITAN*/
    private boolean variablesNoRepetidas(String[] variables){
        for (int i=0;i<variables.length;i++){
            for (int j=0;j<variables.length;j++){
                if (i!=j&&variables[i].equals(variables[j])) return false;
            }
        }
        return true;
    }
    
    /*CREAR TABLA FORMA 1*/
    public void setTabla (String[][] propiedadesTabla){
        if (propiedadesTabla.length==2&&variablesNoRepetidas(propiedadesTabla[0])&&(propiedadesTabla[0].length==propiedadesTabla[1].length)){
            for (int i=0;i<propiedadesTabla[0].length;i++)
                arrayColumnas.add(propiedadesTabla[0][i]);
            jTabla.setModel(modelo);
            for (int i=0;i<propiedadesTabla[1].length;i++)
                modelo.addColumn(propiedadesTabla[1][i]);
            columnas=propiedadesTabla[0].length;
        }
    }
    
    /*CREAR TABLA FORMA 2*/
    public void setTablaArray (String[] nombreCampos){
        arrayColumnas=null;
        jTabla.setModel(modelo);
        for (String nombreCampo:nombreCampos)
            modelo.addColumn(nombreCampo);
        columnas=nombreCampos.length;
    }
    
    public void setTabla (String ...nombreCampos){
        arrayColumnas=null;
        jTabla.setModel(modelo);
        for (String nombreCampo:nombreCampos)
            modelo.addColumn(nombreCampo);
        columnas=nombreCampos.length;
    }
    
    /*AGREGAR UN RENGLON A LA TABLA A PARTEIR DE UN ARREGLO*/
    
    public void addRenglonArray(Object[] arrayRenglon){
        modelo.addRow(arrayRenglon);
        renglones+=1;
    }
    public void addRenglon(Object ...arrayRenglon){
        modelo.addRow(arrayRenglon);
        renglones+=1;
    }
    
    /*AGREGAR UN RENGLON A LA TABLA A PARTEIR DE UN ArrayList*/
    
    public void addRenglon(ArrayList arrayRenglon){
        Object[] arreglo=new Object[arrayRenglon.size()];
        for (int i=0;i<arreglo.length;i++)
            arreglo[i]=arrayRenglon.get(i);
        if (arrayRenglon.size()>0){
            modelo.addRow(arreglo);
            renglones+=1;
        }
    }
    
    /*AGREGAR DATOS A LA TABLA A PARTEIR DE UN BASE_TablaEntity*/
    public void addData (BASE_TablaEntity entity){
        ArrayList array=entity.getData();
        for (int i=0;i<array.size();i++){
            Object obj=array.get(i);
            Class cls=obj.getClass();
            Field[] fld=cls.getDeclaredFields();
            Method[] metodos=cls.getDeclaredMethods();
            
            ArrayList arrayNamesFld=new ArrayList();
            for (int j=0;j<fld.length;j++) arrayNamesFld.add(fld[j].getName());
            
            ArrayList arrayNamesMds=new ArrayList();
            for (int j=0;j<metodos.length;j++) arrayNamesMds.add(metodos[j].getName().toUpperCase());
            
            ArrayList entityData=new ArrayList();
            for (int j=0;j<arrayColumnas.size();j++){
                String columna=arrayColumnas.get(j).toString();
                if (arrayNamesFld.contains(columna)){
                        int indiceNombreFld=arrayNamesFld.indexOf(columna);
                        if (arrayNamesMds.contains("GET"+arrayNamesFld.get(indiceNombreFld).toString().toUpperCase())){
                            int indiceNombreMds=arrayNamesMds.indexOf("GET"+arrayNamesFld.get(indiceNombreFld).toString().toUpperCase());
                            try{
                                Object retornoMetodo=metodos[indiceNombreMds].invoke(obj);
                                if (retornoMetodo!=null&&!retornoMetodo.equals(""))
                                    entityData.add(retornoMetodo.toString());
                                else{
                                    if (defaultValues.size()>0&&j<defaultValues.size())
                                        entityData.add(defaultValues.get(j));
                                    else
                                        entityData.add("");
                                }
                            }catch(Exception e){
                                System.out.println(e.getMessage());
                            }
                        }else{
                            if (defaultValues.size()>0&&j<defaultValues.size())
                                entityData.add(defaultValues.get(j));
                            else
                                entityData.add("");
                        }
                }else{
                    if (defaultValues.size()>0&&j<defaultValues.size())
                        entityData.add(defaultValues.get(j));
                    else
                        entityData.add("");
                }
            }
            addRenglon(entityData);
        }
    }
    
    /*OBTENER UN CAMPO DE LA TABLA A PARTIR DE VARIABLE DE TITULO Y RENGLON*/
    public String getElement(String strColumna,int renglon){
        if (arrayColumnas!=null){
            int columna=arrayColumnas.indexOf(strColumna);
            if (columna>=0)
                return jTabla.getValueAt(renglon,columna).toString();
            else
                return "";
        }
        return "";
    }
    
    /*OBTENER UN CAMPO DE LA TABLA A PARTIR DE COLUMNA Y RENGLON*/
    public String getElement(int columna,int renglon){
        return jTabla.getValueAt(renglon,columna).toString();
    }
        
    /*OBTENER UNA COLUMNA DE LA TABLA A PARTIR DE VARIABLE DE TITULO*/
    public ArrayList getColumnData(String strColumna){
        ArrayList retorno=new ArrayList();
        if (arrayColumnas!=null){
            int columna=arrayColumnas.indexOf(strColumna);
            for (int i=0;i<renglones;i++)
                retorno.add(jTabla.getValueAt(i,columna).toString());
        }
        return retorno;
    }
    
    /*OBTENER UNA COLUMNA DE LA TABLA A PARTIR DEL INDICE DE COLUMNA*/
    public ArrayList getColumnData(int columna){
        ArrayList retorno=new ArrayList();
        for (int i=0;i<renglones;i++)
            retorno.add(jTabla.getValueAt(i,columna).toString());
        return retorno;
    }
    
    /*OBTENER UN RENGLON DE LA TABLA A PARTIR DEL INDICE*/
    public ArrayList getRowData(int renglon){
        ArrayList retorno=new ArrayList();
        for (int i=0;i<columnas;i++)
            retorno.add(jTabla.getValueAt(renglon,i).toString());
        return retorno;
    }
    
    /*MODIFICAR UN CAMPO DE LA TABLA A PARTIR DE VARIABLE DE TITULO Y RENGLON*/
    public void setElement(String strColumna,int renglon,Object valor){
        if (arrayColumnas!=null){
            int columna=arrayColumnas.indexOf(strColumna);
            if (columna>=0) jTabla.setValueAt(valor,renglon,columna);
        }
    }
    
    /*MODIFICAR UN CAMPO DE LA TABLA A PARTIR DE COLUMNA Y RENGLON*/
    public void setElement(int columna,int renglon,Object valor){
        jTabla.setValueAt(valor,renglon,columna);
    }
    
    /*ELIMINAR RENGLON DE LA TABLA*/
    public void removeRow(int renglon){
        modelo.removeRow(renglon);
        renglones-=1;
    }
    
    /*ELIMINAR VARIOS RENGLONES DE LA TABLA*/
    public void removeRows(int ...renglon){
        Utilerias util=new Utilerias();
        renglon=burbuja(renglon);
        for (int i=renglon.length-1;i>=0;i--){
            modelo.removeRow(renglon[i]);
            renglones-=1;
        }
    }
    
    /*ELIMINAR RENGLONES DE LA TABLA*/
    public void clearRows(){
        for(int i=modelo.getRowCount()-1;i>=0;i--) modelo.removeRow(i);
        renglones=0;
    }
    
    /*CONFIGURAR EL ANCHO DE UNA COLUMNA DETERMINADA*/
    public void setAnchoColumna(int column,int width){
        jTabla.getColumnModel().getColumn(column).setPreferredWidth(width);
    }
    
    /*CONFIGURAR EL ANCHO DE VARIAS O TODAS LAS COLUMNAS*/
    public void setAnchoColumnaArray(Integer[] columnas){
        jTabla.setAutoResizeMode(0);
        if (getColumnas()>0){
            for (int i=0;i<columnas.length;i++){
                if (getColumnas()>i)
                    jTabla.getColumnModel().getColumn(i).setPreferredWidth(columnas[i]);
                else break;
            }
        }
    }
    
    public void setAnchoColumnas(Integer ...columnas){
        jTabla.setAutoResizeMode(0);
        if (getColumnas()>0){
            for (int i=0;i<columnas.length;i++){
                if (getColumnas()>i)
                    jTabla.getColumnModel().getColumn(i).setPreferredWidth(columnas[i]);
                else break;
            }
        }
    }
    
    /*OCULATR UNA COLUMNA*/
    public void ocultarColumna(int column){
        jTabla.getColumnModel().getColumn(column).setWidth(0);
        jTabla.getColumnModel().getColumn(column).setMinWidth(0);
        jTabla.getColumnModel().getColumn(column).setMaxWidth(0);
    }
    
    /*OCULTAR VARIAS COLUMNAS*/
    public void ocultarColumnasArray(Integer[] columnas){
        if (getColumnas()>0){
            for (Integer columna:columnas){
                jTabla.getColumnModel().getColumn(columna).setWidth(0);
                jTabla.getColumnModel().getColumn(columna).setMinWidth(0);
                jTabla.getColumnModel().getColumn(columna).setMaxWidth(0);
            }
        }
    }
    public void ocultarColumnas(Integer ...columnas){
        for(int i=0; i<columnas.length; i++){
            jTabla.getColumnModel().getColumn(columnas[i]).setWidth(0);
            jTabla.getColumnModel().getColumn(columnas[i]).setMinWidth(0);
            jTabla.getColumnModel().getColumn(columnas[i]).setMaxWidth(0);
        }
    }
    
    /*OBTENER NUMERO DE COLUMNAS DE LA TABLA*/
    public int getColumnas(){
        return columnas;
    }
    
    /*OBTENER NUMERO DE RENGLONES DE LA TABLA*/
    public int getRenglones(){
        return renglones;
    }
    
    public int getSelectedRow(){
        return jTabla.getSelectedRow();
    }
    
    public int getSelectedColumn(){
        return jTabla.getSelectedColumn();
    }
    
    public void setSelectedRow(int row){
        jTabla.setRowSelectionInterval(row, row);
    }
    
    public void setSelectedRows(int inicio, int fin){
        jTabla.setRowSelectionInterval(inicio, fin);
    }
    
    public void setSelectedColumn(int column){
        jTabla.setColumnSelectionInterval(column, column);
    }
    
    public void setSelectedColumns(int inicio, int fin){
        jTabla.setColumnSelectionInterval(inicio, fin);
    }
    
    /*ALINEAR INFORMACION DE UNA COLUMNA*/
    public void alinear(Integer columna, Integer alineacion){
        DefaultTableCellRenderer modeloA = new DefaultTableCellRenderer();
        if(alineacion.equals(3))
            modeloA.setHorizontalAlignment(SwingConstants.RIGHT);
        else if(alineacion.equals(2))
            modeloA.setHorizontalAlignment(SwingConstants.CENTER);
        else
            modeloA.setHorizontalAlignment(SwingConstants.LEFT);
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(modeloA);
    }
    
    public void alinearVerticalCenter(Integer columna){
        DefaultTableCellRenderer modeloA = new DefaultTableCellRenderer();
        modeloA.setVerticalAlignment(SwingConstants.CENTER);
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(modeloA);
    }
    
    public void alinearS(String strColumna, Integer alineacion){
        if (arrayColumnas!=null){
            int columna=arrayColumnas.indexOf(strColumna);
            if (columna>=0){
                DefaultTableCellRenderer modeloA = new DefaultTableCellRenderer();
                if(alineacion.equals(3))
                    modeloA.setHorizontalAlignment(SwingConstants.RIGHT);
                else if(alineacion.equals(2))
                    modeloA.setHorizontalAlignment(SwingConstants.CENTER);
                else
                    modeloA.setHorizontalAlignment(SwingConstants.LEFT);
                jTabla.getColumnModel().getColumn(columna).setCellRenderer(modeloA);
            }
        }
    }
    
    public void setDefaultValues(Object ...arrayDefault){
        for (int i=0;i<columnas;i++){
            if (i<arrayDefault.length)
                defaultValues.add(arrayDefault[i]);
            else
                defaultValues.add("");
        }
    }
    
    public void setRowSelectionAllowed(Boolean alowed){
        jTabla.setRowSelectionAllowed(alowed);
    }
    
    public void removeRowSelectionInterval(int a,int b){
        jTabla.removeRowSelectionInterval(a, b);
    }
    
    public int[] getSelectedRows(){
        return jTabla.getSelectedRows();
    }
    
    public void setAlturaRow(int alt){
        jTabla.setRowHeight(alt);
    }
    
    public int[] burbuja(int[] a){
        for(int i=a.length-1; i>0; i--){
            for(int j=0; j<i; j++){
                if(a[j]>a[j+1]){
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
	}
        return a;
    }
    
    public void quitarSeleccion(){
        jTabla.clearSelection();
    }
    
    public void cebraTabla(){
        StripedTableCellRenderer.installInTable(jTabla,new Color(235,235,235), Color.black, null, null);
    }
    
    public void cebraTabla(Color fondo, Color texto){
        StripedTableCellRenderer.installInTable(jTabla, fondo, texto, null, null);
    }
    
    public void cebraTabla(Color fondo1, Color texto1, Color fondo2, Color texto2){
        StripedTableCellRenderer.installInTable(jTabla, fondo1, texto1, fondo2, texto2);
    }
    
    public void multilinea(){
        MultiLineCellRenderer multiLineRenderer = new MultiLineCellRenderer(SwingConstants.LEFT, SwingConstants.CENTER);
        TableColumnModel tcm = jTabla.getColumnModel();
        for(int i=0; i<jTabla.getColumnCount(); i++)
            tcm.getColumn(i).setCellRenderer(multiLineRenderer);
    }
    
    public void multilinea(int columna, int pos_horizontal){
        MultiLineCellRenderer multiLineRendererDefault = new MultiLineCellRenderer(SwingConstants.LEFT, SwingConstants.CENTER);
        MultiLineCellRenderer multiLineRenderer;
        if(pos_horizontal == 1){
            multiLineRenderer = new MultiLineCellRenderer(SwingConstants.LEFT, SwingConstants.CENTER);
        }else if(pos_horizontal == 2){
            multiLineRenderer = new MultiLineCellRenderer(SwingConstants.CENTER, SwingConstants.CENTER);
        }else{
            multiLineRenderer = new MultiLineCellRenderer(SwingConstants.RIGHT, SwingConstants.CENTER);
        }
        TableColumnModel tcm = jTabla.getColumnModel();
        for(int i=0; i<jTabla.getColumnCount(); i++){
            if(columna == i){
                tcm.getColumn(i).setCellRenderer(multiLineRenderer);
            }else{
                tcm.getColumn(i).setCellRenderer(multiLineRendererDefault);
            }
        }
    }
    
    public void multilinea(int aco[]){
        MultiLineCellRenderer multiLineRenderer1 = new MultiLineCellRenderer(SwingConstants.LEFT, SwingConstants.CENTER);
        MultiLineCellRenderer multiLineRenderer2 = new MultiLineCellRenderer(SwingConstants.CENTER, SwingConstants.CENTER);
        MultiLineCellRenderer multiLineRenderer3 = new MultiLineCellRenderer(SwingConstants.RIGHT, SwingConstants.CENTER);
        TableColumnModel tcm = jTabla.getColumnModel();
        for(int i=0; i<jTabla.getColumnCount(); i++){
            if(aco[i] == 1) tcm.getColumn(i).setCellRenderer(multiLineRenderer1);
            else if(aco[i] == 2) tcm.getColumn(i).setCellRenderer(multiLineRenderer2);
            else if(aco[i] == 3) tcm.getColumn(i).setCellRenderer(multiLineRenderer3);
        }
    }
    
    public void multilinea(int aco[][]){
        MultiLineCellRenderer multiLineRenderer = new MultiLineCellRenderer(SwingConstants.LEFT, SwingConstants.CENTER);
        TableColumnModel tcm = jTabla.getColumnModel();
        for(int i=0; i<jTabla.getColumnCount(); i++)
            tcm.getColumn(i).setCellRenderer(multiLineRenderer);
        MultiLineCellRenderer multiLineRenderer1 = new MultiLineCellRenderer(SwingConstants.LEFT, SwingConstants.CENTER);
        MultiLineCellRenderer multiLineRenderer2 = new MultiLineCellRenderer(SwingConstants.CENTER, SwingConstants.CENTER);
        MultiLineCellRenderer multiLineRenderer3 = new MultiLineCellRenderer(SwingConstants.RIGHT, SwingConstants.CENTER);
        for(int i=0; i<aco.length; i++){
            if(aco[i][1] == 1) tcm.getColumn(aco[i][0]).setCellRenderer(multiLineRenderer1);
            else if(aco[i][1] == 2) tcm.getColumn(aco[i][0]).setCellRenderer(multiLineRenderer2);
            else if(aco[i][1] == 3) tcm.getColumn(aco[i][0]).setCellRenderer(multiLineRenderer3);
        }
    }
    
    public void dibujarCheckBox(int columna){
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(new CheckBoxRenderer());//para que se vea el checkbox hay que indicar la columna
    }
    
    public void dibujarDateChooser(int columna){
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(new JDateChooserRenderer());//para que se vea el DateChooser hay que indicar la columna
        jTabla.getColumnModel().getColumn(columna).setCellEditor(new JDateChooserCellEditor());//para que se vea el DateChooser hay que indicar la columna
    }
        
    public void dibujarJTextField(int columna){
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(new JTextFieldRenderer());//para que se vea el JTextField hay que indicar la columna
        jTabla.getColumnModel().getColumn(columna).setCellEditor(new JTextFieldCellEditor());//para que se vea el JTextField hay que indicar la columna
    }
    
    public void cellLineWrap(int columna){
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(new TableCellLongTextRenderer());
    }
    
    public void cellLineWrap2(int columna){
        jTabla.getColumnModel().getColumn(columna).setCellRenderer(new TableCellLongTextRenderer2());
    }
    
}

class MultiLineCellRenderer extends JPanel implements TableCellRenderer {
    public MultiLineCellRenderer(int horizontalAlignment, int verticalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        switch (horizontalAlignment) {
            case SwingConstants.LEFT: alignmentX = (float) 0.0; break;
            case SwingConstants.CENTER: alignmentX = (float) 0.5; break;
            case SwingConstants.RIGHT: alignmentX = (float) 1.0; break;
            default: throw new IllegalArgumentException("Illegal horizontal alignment value");
        }
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(true);
        setBorder(border);
        background = null;
        foreground = null;
    }
    
    public void setForeground(Color foreground) {
        super.setForeground(foreground);
        Component[] comps = this.getComponents();
        int ncomp = comps.length;
        for (int i = 0; i < ncomp; i++) {
            Component comp = comps[i];
            if (comp instanceof JLabel) {
                comp.setForeground(foreground);
            }
        }
    }

    public void setBackground(Color background) {
        this.background = background;
        super.setBackground(background);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    // Implementation of TableCellRenderer interface
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
        invalidate();
        if (value == null || table == null) {// Do nothing if no value
            return this;
        }
        
        // Set the foreground and background colors from the table if they are not set
        Color cellForeground = cellForeground = (foreground == null ? table.getForeground(): foreground);
        Color cellBackground = cellBackground = (background == null ? table.getBackground(): background);

        // Handle selection and focus colors
        if (isSelected == true) {
            cellForeground = table.getSelectionForeground();
            cellBackground = table.getSelectionBackground();
        }

        if (hasFocus == true) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(border);
        }

        super.setForeground(cellForeground);
        super.setBackground(cellBackground);

        font = table.getFont();

        if (verticalAlignment != SwingConstants.TOP) {
            add(Box.createVerticalGlue());
        }

        Object[] values;
        int length;
        if (value instanceof Object[]) { // Input is an array - use it
            values = (Object[]) value;
        }else{ // Not an array - turn it into one
            values = new Object[1];
            values[0] = value;
        }
        length = values.length;

        // Configure each row of the cell using a separate JLabel. If a given row is a JComponent, add it directly..
        for (int i = 0; i < length; i++) {
            Object thisRow = values[i];
            if (thisRow instanceof JComponent) {
                add((JComponent) thisRow);
            } else {
                JLabel l = new JLabel();
                setValue(l, thisRow, i, cellForeground);
                add(l);
            }
        }
        
        if (verticalAlignment != SwingConstants.BOTTOM) {
            add(Box.createVerticalGlue());
        }
        return this;
    }

    // Configures a label for one line of the cell.
    // This can be overridden by derived classes
    protected void setValue(JLabel l, Object value, int lineNumber, Color cellForeground) {
        if (value != null && value instanceof Icon) {
            l.setIcon((Icon) value);
        } else {
            l.setText(value == null ? "" : value.toString());
        }
        l.setHorizontalAlignment(horizontalAlignment);
        l.setAlignmentX(alignmentX);
        l.setOpaque(false);
        l.setForeground(cellForeground);
        l.setFont(font);
    }

    protected int verticalAlignment;
    protected int horizontalAlignment;
    protected float alignmentX;
    protected Color foreground;
    protected Color background;
    protected Font font;
    protected static Border border = new EmptyBorder(1, 2, 1, 2);
}

class StripedTableCellRenderer implements TableCellRenderer {
    public StripedTableCellRenderer(TableCellRenderer targetRenderer, Color evenBack, Color evenFore, Color oddBack, Color oddFore) {
        this.targetRenderer = targetRenderer;
        this.evenBack = evenBack;
        this.evenFore = evenFore;
        this.oddBack = oddBack;
        this.oddFore = oddFore;
    }

    // Implementation of TableCellRenderer interface
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        TableCellRenderer renderer = targetRenderer;
        if (renderer == null) {// Get default renderer from the table
            renderer = table.getDefaultRenderer(table.getColumnClass(column));
        }

        // Let the real renderer create the component
        Component comp = renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Now apply the stripe effect
        if (isSelected == false && hasFocus == false) {
            if ((row & 1) == 0) {
                comp.setBackground(evenBack != null ? evenBack : table.getBackground());
                comp.setForeground(evenFore != null ? evenFore : table.getForeground());
            } else {
                comp.setBackground(oddBack != null ? oddBack : table.getBackground());
                comp.setForeground(oddFore != null ? oddFore : table.getForeground());
            }
        }
        return comp;
    }

    // Convenience method to apply this renderer to single column
    public static void installInColumn(JTable table, int columnIndex, Color evenBack, Color evenFore, Color oddBack, Color oddFore) {
        TableColumn tc = table.getColumnModel().getColumn(columnIndex);
        // Get the cell renderer for this column, if any
        TableCellRenderer targetRenderer = tc.getCellRenderer();
        // Create a new StripedTableCellRenderer and install it
        tc.setCellRenderer(new StripedTableCellRenderer(targetRenderer, evenBack, evenFore, oddBack, oddFore));
    }

    // Convenience method to apply this renderer to an entire table
    public static void installInTable(JTable table, Color evenBack, Color evenFore, Color oddBack, Color oddFore) {
        StripedTableCellRenderer sharedInstance = null;
        int columns = table.getColumnCount();
        for (int i = 0; i < columns; i++) {
            TableColumn tc = table.getColumnModel().getColumn(i);
            TableCellRenderer targetRenderer = tc.getCellRenderer();
            if (targetRenderer != null) {
                // This column has a specific renderer
                tc.setCellRenderer(new StripedTableCellRenderer(targetRenderer, evenBack, evenFore, oddBack, oddFore));
            } else {
                // This column uses a class renderer - use a shared renderer
                if (sharedInstance == null) {
                    sharedInstance = new StripedTableCellRenderer(null, evenBack, evenFore, oddBack, oddFore);
                }
                tc.setCellRenderer(sharedInstance);
            }
        }
    }

    protected TableCellRenderer targetRenderer;
    protected Color evenBack;
    protected Color evenFore;
    protected Color oddBack;
    protected Color oddFore;
}

class CheckBoxRenderer extends javax.swing.JCheckBox implements TableCellRenderer {

          CheckBoxRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
          }

          public Component getTableCellRendererComponent(JTable table, Object value,
              boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
              setForeground(table.getSelectionForeground());
              //super.setBackground(table.getSelectionBackground());
              setBackground(table.getSelectionBackground());
            } else {
              setForeground(table.getForeground());
              setBackground(table.getBackground());
            }
            setSelected((value != null && ((Boolean) value).booleanValue()));
            return this;
          }
    }

class JDateChooserRenderer extends com.toedter.calendar.JDateChooser implements TableCellRenderer{

    java.util.Date inDate;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub

        if (value instanceof java.util.Date){
            this.setDate((java.util.Date) value);
        } else if (value instanceof java.util.Calendar){
            this.setCalendar((java.util.Calendar) value);
        }
        return this;
    }
}

class JDateChooserCellEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {

    private static final long serialVersionUID = 917881575221755609L;
    private com.toedter.calendar.JDateChooser dateChooser = new com.toedter.calendar.JDateChooser();

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        java.util.Date date = null;
        if (value instanceof java.util.Date)
            date = (java.util.Date) value;
        dateChooser.setDate(date);
        return dateChooser;
    }

    public Object getCellEditorValue() {
        return dateChooser.getDate();
    }
    
}

class JTextFieldCellEditor extends javax.swing.JTextField implements javax.swing.table.TableCellEditor {
    
    public JTextFieldCellEditor(){
        super();
        this.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        this.setDocument(new JTextMascara(29,true,"todo"));
        //this.addActionListener(new ActionListener(){ public void actionPerformed (ActionEvent evento){}});
        //this.addFocusListener(new FocusListener(){ public void focusGained (FocusEvent e) {} public void focusLost (FocusEvent e){}});
    }
    
    public void addCellEditorListener(CellEditorListener l) {}
    public void cancelCellEditing() {}
     
    public Object getCellEditorValue() {
        return this.getText();
    }
     
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return this;
    }
     
    @Override
    public boolean isCellEditable(EventObject anEvent){
        return true;
    }
     
    public void removeCellEditorListener(CellEditorListener l) {}

    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    public boolean stopCellEditing() {
        return true;
    }
}

class JTextFieldRenderer implements TableCellRenderer {

    javax.swing.JTextField campo = new javax.swing.JTextField();
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        campo.setText("");
        if (value instanceof String)
            campo.setText((String) value);
        campo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        return campo;
    }
}


class TableCellLongTextRenderer extends javax.swing.JTextArea implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        this.setText((String)value);
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
        this.setFont(new Font("Tahoma", Font.PLAIN, 13));
        if(isSelected){
            this.setBackground((Color)UIManager.get("Table.selectionBackground"));
        }
        //set the JTextArea to the width of the table column
        setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
        //if(table.getRowHeight(row) != getPreferredSize().height){
        if(table.getRowHeight(row) < getPreferredSize().height){
            //set the height of the table row to the calculated height of the JTextArea
            table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
}


class TableCellLongTextRenderer2 extends DefaultTableCellRenderer implements TableCellRenderer{
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        final javax.swing.JTextArea jtext = new javax.swing.JTextArea();
        jtext.setText((String)value);
        jtext.setWrapStyleWord(true);
        jtext.setLineWrap(true);
        if(isSelected){
            jtext.setBackground((Color)UIManager.get("Table.selectionBackground"));
        }
        return jtext;
    }

    //METHODS overridden for performance
    @Override
    public void validate(){}

    @Override
    public void revalidate(){}

    @Override
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue){}

    @Override
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue){}

}