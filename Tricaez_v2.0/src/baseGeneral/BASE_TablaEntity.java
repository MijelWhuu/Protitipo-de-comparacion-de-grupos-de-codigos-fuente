package baseGeneral;

import java.util.ArrayList;

/**
 *
 * @author GSA
 */
public class BASE_TablaEntity {
    ArrayList data;
    int columns;
    int rows;
    public BASE_TablaEntity(){}

    public void setData(ArrayList data) {
        this.data = data;
        rows=data.size();
    }

    public int getColumns() {
        return columns;
    }

    public ArrayList getData() {
        return data;
    }

    public int getRows() {
        return rows;
    }
}
