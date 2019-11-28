package baseSistema;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author mavg
 */
public class TablaEspecial {

    private JTable tabla;
    
    
    public TablaEspecial() {
    }

    public TablaEspecial(JTable tabla) {
        this.tabla = tabla;
    }
    
    public void addRow(Object []info){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        model.addRow(info);
    }
    
    public void ocultarColumnas(Integer ...columnas){
        for (Integer columna : columnas) {
            tabla.getColumnModel().getColumn(columna).setWidth(0);
            tabla.getColumnModel().getColumn(columna).setMinWidth(0);
            tabla.getColumnModel().getColumn(columna).setMaxWidth(0);
        }
    }
    
    public void setAnchoColumnas(Integer ...columnas){
        tabla.setAutoResizeMode(0);
        if (tabla.getColumnCount()>0){
            for (int i=0;i<columnas.length;i++){
                if (tabla.getColumnCount()>i)
                    tabla.getColumnModel().getColumn(i).setPreferredWidth(columnas[i]);
                else break;
            }
        }
    }
    
    public void alinearCentro(Integer columna){
        DefaultTableCellRenderer modeloA = new DefaultTableCellRenderer();
        modeloA.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(columna).setCellRenderer(modeloA);
    }
    
    public Object getElement(int columna,int renglon){
        return tabla.getValueAt(renglon,columna);
    }
    
    public void setElement(int columna,int renglon,Object valor){
        tabla.setValueAt(valor,renglon,columna);
    }
    
    public void removeRow(int renglon){
        ((DefaultTableModel)tabla.getModel()).removeRow(renglon);
    }
    
    public void clearTabla(){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel(); 
        int rows = model.getRowCount(); 
        for(int i = rows - 1; i >=0; i--)
            model.removeRow(i); 
        model.setRowCount(0);
    }
    
    public int getColumnas(){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel(); 
        return model.getColumnCount();
    }
    
    /*OBTENER NUMERO DE RENGLONES DE LA TABLA*/
    public int getRenglones(){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel(); 
        return model.getRowCount(); 
    }
    
    public void cebraTabla(){
        StripedTableCellRenderer.installInTable(tabla,new Color(235,235,235), Color.black, null, null);
    }
    
    public void dibujarCheckBox(int columna){
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new CheckBoxRenderer());//para que se vea el checkbox hay que indicar la columna
    }
    
    public void cellLineWrap(int columna){
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new TableCellLongTextRenderer());
    }
    
    public void dibujarComboBox(int columna){
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new DefaultTableCellRenderer());//para que se vea el checkbox hay que indicar la columna
    }
    
    public void crearCheckbox(int rows, int columna){
        dibujarCheckBox(columna);
        EachRowEditor rowEditor = new EachRowEditor(tabla);
        for(int i=0; i<rows; i++)
            rowEditor.setEditorAt(i, new DefaultCellEditor(new javax.swing.JCheckBox()));
        tabla.getColumnModel().getColumn(columna).setCellEditor(rowEditor);
    }
    
    public void crearCheckboxCelda(int col, int row){
        //dibujarCheckBox(col);
        EachRowEditor rowEditor = new EachRowEditor(tabla);
        rowEditor.setEditorAt(row, new DefaultCellEditor(new javax.swing.JCheckBox()));
        tabla.getColumnModel().getColumn(col).setCellEditor(rowEditor);
    }
    
    public void crearComboBox(int rows, int columna, String[] lista){
        //dibujarComboBox(columna);
        EachRowEditor rowEditor = new EachRowEditor(tabla);
        for(int i=0; i<rows; i++){
            javax.swing.JComboBox cmb = new javax.swing.JComboBox(lista);
            cmb.setFont(new Font("Tahoma", Font.PLAIN, 11));
            rowEditor.setEditorAt(i, new DefaultCellEditor(cmb));
        
        }
        tabla.getColumnModel().getColumn(columna).setCellEditor(rowEditor);
    }
    
    public void setValorAllColumn(int column, Object valor){
        for(int i=0; i<getRenglones(); i++){
            setElement(column,i,valor);
        }
    }
    
    public void setValorAllColumnWithTrue(int flag, int column, Object valor){
        for(int i=0; i<getRenglones(); i++){
            if(Boolean.valueOf(getElement(flag, i).toString()))
                setElement(column,i,valor);
        }
    }
    
    public void crearDateChooser(int rows, int columna){
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new JDateChooserRenderer());
        EachRowEditor rowEditor = new EachRowEditor(tabla);
        for(int i=0; i<rows; i++)
            rowEditor.setEditorAt(i, new JDateChooserCellEditor());
        tabla.getColumnModel().getColumn(columna).setCellEditor(rowEditor);
    }
    
    public void crearDateChooserChica(int rows, int columna){
        tabla.getColumnModel().getColumn(columna).setCellRenderer(new JDateChooserRendererChica());
        EachRowEditor rowEditor = new EachRowEditor(tabla);
        for(int i=0; i<rows; i++)
            rowEditor.setEditorAt(i, new JDateChooserCellEditor());
        tabla.getColumnModel().getColumn(columna).setCellEditor(rowEditor);
    }
    
}

class CheckBoxRenderer extends javax.swing.JCheckBox implements TableCellRenderer {

    CheckBoxRenderer() {
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        /*if(isSelected){
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        }else{
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }*/
        setSelected((value != null && ((Boolean) value)));
        return this;
    }
}

class TableCellLongTextRenderer extends javax.swing.JTextArea implements TableCellRenderer{

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        this.setText((String)value);
        this.setWrapStyleWord(true);
        this.setLineWrap(true);
        this.setFont(new Font("Tahoma", Font.PLAIN, 11));
        //if(isSelected){
        //    this.setBackground((Color)UIManager.get("Table.selectionBackground"));
        //}
        setSize(table.getColumnModel().getColumn(column).getWidth(),getPreferredSize().height);
        if(table.getRowHeight(row) < getPreferredSize().height){
            table.setRowHeight(row, getPreferredSize().height);
        }
        return this;
    }
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
    @Override
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

class ComboCellRenderer extends JComboBox implements TableCellRenderer {

    protected static Border noFocusBorder = new EmptyBorder(1,1,1,1);
    protected static Border focusBorder = UIManager.getBorder("Table.focusCellHighlightBorder");

    public ComboCellRenderer() {
        super();
        //setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected && !hasFocus ? table.getSelectionBackground() : table.getBackground());
        setForeground(isSelected && !hasFocus ? table.getSelectionForeground() : table.getForeground());
        setFont(table.getFont());
        setSelectedItem(value);
        return this;
    }
}

class JDateChooserRenderer extends com.toedter.calendar.JDateChooser implements TableCellRenderer{

    java.util.Date inDate;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub
        this.setDateFormatString("dd/MMM/yyyy");
        this.setFont(new Font("Tahoma", Font.PLAIN, 13));
        
        if (value instanceof java.util.Date){
            this.setDate((java.util.Date) value);
        } else if (value instanceof java.util.Calendar){
            this.setCalendar((java.util.Calendar) value);
        } else if(value == null)
            this.setDate(null);
        return this;
    }
}

class JDateChooserRendererChica extends com.toedter.calendar.JDateChooser implements TableCellRenderer{

    java.util.Date inDate;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub
        this.setDateFormatString("dd/MMM/yyyy");
        this.setFont(new Font("Tahoma", Font.PLAIN, 11));
        
        if (value instanceof java.util.Date){
            this.setDate((java.util.Date) value);
        } else if (value instanceof java.util.Calendar){
            this.setCalendar((java.util.Calendar) value);
        } else if(value == null)
            this.setDate(null);
        return this;
    }
}

class JDateChooserCellEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {

    //private static final long serialVersionUID = 917881575221755609L;
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