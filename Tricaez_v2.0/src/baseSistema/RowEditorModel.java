package baseSistema;

import java.util.Hashtable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author mavg
 */
public class RowEditorModel {
    
    private Hashtable data;
    
    public RowEditorModel(){
        data = new Hashtable();
    }
    
    public void addEditorForRow(int row, TableCellEditor e){
        data.put(row, e);
    }

    public void removeEditorForRow(int row){
        data.remove(row);
    }

    public TableCellEditor getEditor(int row){
        return (TableCellEditor)data.get(row);
    }
    
}
