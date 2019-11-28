package baseGeneral;

import org.apache.poi.hssf.usermodel.*;

/**
 *
 * @author MAVG
 */

public class Excel {
    
    public Excel(){}
    
    private HSSFWorkbook workbook;
    private Boolean empty=true;
    
    public void setWorkbook(HSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public HSSFWorkbook getWorkbook() {
        return workbook;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
    
    public HSSFFont fuentes (HSSFWorkbook xls, String letra, int tamano, Boolean bold, Boolean cursiva, Boolean underline){
        HSSFFont fuente = xls.createFont();
        fuente.setFontName(letra);
        fuente.setFontHeightInPoints((short)tamano);
        if(bold)
            fuente.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        else
            fuente.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        fuente.setItalic(cursiva);
        if(underline)
            fuente.setUnderline(HSSFFont.U_SINGLE);
        return fuente;
    }

    public HSSFCellStyle estilo (HSSFWorkbook xls, HSSFFont fuente, Integer alineacion){
        HSSFCellStyle stilo = xls.createCellStyle();
        stilo.setFont(fuente);
        if(alineacion==1)
            stilo.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        else if(alineacion==2)
            stilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        else if(alineacion==3)
            stilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        else if(alineacion==4)
            stilo.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
        else
            stilo.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
        return stilo;
    }

    public HSSFCellStyle estilo (HSSFWorkbook xls, HSSFFont fuente, Integer alineacion, String formato){
        HSSFCellStyle stilo = xls.createCellStyle();
        stilo.setFont(fuente);
        if(alineacion==1)
            stilo.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        else if(alineacion==2)
            stilo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        else if(alineacion==3)
            stilo.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        else if(alineacion==4)
            stilo.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
        else
            stilo.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
        stilo.setDataFormat(HSSFDataFormat.getBuiltinFormat(formato));
        return stilo;
    }   
    
    public void setColumnaRenglon(int renglon, int columna, String dato, HSSFSheet hoja){
        if(renglon<=hoja.getLastRowNum()){
            if (!empty) hoja.getRow(renglon).createCell(columna).setCellValue(new HSSFRichTextString(dato));
            else{ hoja.createRow(renglon).createCell(columna).setCellValue(new HSSFRichTextString(dato)); empty=false;}
        }else
            hoja.createRow(renglon).createCell(columna).setCellValue(new HSSFRichTextString(dato));
    }
    
    public void setColumnaRenglon(int renglon, int columna, String dato, HSSFSheet hoja, HSSFCellStyle style){
        if(renglon<=hoja.getLastRowNum()){
            if (!empty) hoja.getRow(renglon).createCell(columna).setCellValue(new HSSFRichTextString(dato));
            else{ hoja.createRow(renglon).createCell(columna).setCellValue(new HSSFRichTextString(dato)); empty=false;}
        }else
            hoja.createRow(renglon).createCell(columna).setCellValue(new HSSFRichTextString(dato));
        hoja.getRow(renglon).getCell(columna).setCellStyle(style);
    }
    
    public void setColumnaRenglon(int renglon, int columna, Double x , HSSFSheet hoja){
        if(renglon<=hoja.getLastRowNum()){
            if (!empty) hoja.getRow(renglon).createCell(columna).setCellValue(new Double(x));
            else{ hoja.createRow(renglon).createCell(columna).setCellValue(new Double(x)); empty=false;}
        }else
            hoja.createRow(renglon).createCell(columna).setCellValue(new Double(x));
    }
    
    public void setColumnaRenglon(int renglon, int columna, Double x, HSSFSheet hoja, HSSFCellStyle style){
        if(renglon<=hoja.getLastRowNum()){
            if (!empty) hoja.getRow(renglon).createCell(columna).setCellValue(new Double(x));
            else{ hoja.createRow(renglon).createCell(columna).setCellValue(new Double(x)); empty=false;}
        }else
            hoja.createRow(renglon).createCell(columna).setCellValue(new Double(x));
        hoja.getRow(renglon).getCell(columna).setCellStyle(style);
        hoja.getRow(renglon).getCell(columna).setCellType(org.apache.poi.hssf.usermodel.HSSFCell.CELL_TYPE_NUMERIC);
    }
    
    public void setFormulaExcel(int renglon, int columna, String formula, HSSFSheet hoja){
        if(renglon<=hoja.getLastRowNum()){
            if (!empty) hoja.getRow(renglon).createCell(columna).setCellFormula(formula);
                else{ hoja.createRow(renglon).createCell(columna).setCellFormula(formula); empty=false;}
        }else
            hoja.createRow(renglon).createCell(columna).setCellFormula(formula);
    }
    
    public void setFormulaExcel(int renglon, int columna, String formula, HSSFSheet hoja, HSSFCellStyle style){
        if(renglon<=hoja.getLastRowNum()){
            if (!empty) hoja.getRow(renglon).createCell(columna).setCellFormula(formula);
            else{ hoja.createRow(renglon).createCell(columna).setCellFormula(formula); empty=false;}
        }else
            hoja.createRow(renglon).createCell(columna).setCellFormula(formula);
        hoja.getRow(renglon).getCell(columna).setCellStyle(style);
    }
    
}
