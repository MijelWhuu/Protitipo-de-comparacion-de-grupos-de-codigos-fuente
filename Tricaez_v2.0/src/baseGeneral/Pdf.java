package baseGeneral;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.awt.Color;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Rectangle;

/**
 *
 * @author MAVG
 */

public class Pdf {
    
    public Pdf() {}
    
    /*
        cell.setBackgroundColor(c);
        cell.setRowspan(row);
        cell.setColspan(col);
        cell.setFixedHeight(30.0f);
        document.add(new Paragraph(..)
        document.newPage();
        com.lowagie.text.Image image = com.lowagie.text.Image.getInstance("src/alianza.png");
        image.scalePercent(50.0f);
        document.add(image);
        new Paragraph("N°",FontFactory.getFont("src/FrutigerCn.ttf",10.0f, Font.BOLD, new Color(255, 255, 255)))),2,1,1,1,new Color(121,121,120));
     */
    
    public Font fuente(String letra, float size, int tipo, Color c){
        return FontFactory.getFont(letra, size, tipo, c); 
    }
    
    public void texto(Document document, String info, Font font){
        try{
            Paragraph p = new Paragraph(info,font);
            p.setAlignment(Paragraph.ALIGN_JUSTIFIED_ALL);
            document.add(p);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void texto(Document document, String info, int alineacion, Font font){
        try{
            Paragraph p = new Paragraph(info,font);
            if(alineacion==1)
                p.setAlignment(Paragraph.ALIGN_LEFT);
            else if(alineacion==2)
                p.setAlignment(Paragraph.ALIGN_CENTER);
            else if(alineacion==3)
                p.setAlignment(Paragraph.ALIGN_RIGHT);
            else if(alineacion==4)
                p.setAlignment(Paragraph.ALIGN_JUSTIFIED_ALL);
            else
                p.setAlignment(Paragraph.ALIGN_MIDDLE);
            document.add(p);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    
    public void celda(PdfPTable table,String info,Font font){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void celda(PdfPTable table,String info,int alineacion,Font font){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void celda(PdfPTable table,String info,int alineacion, int borde, Font font){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void celda(PdfPTable table,String info,int alineacion, int borde, Font font, Color c, int padding_top, int padding_bottom){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else if(borde==4)
            cell.setBorder(Rectangle.TOP);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setPaddingTop(padding_top);
        cell.setPaddingBottom(padding_bottom);
        cell.setBorderColor(c);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    /*public void celda1(PdfPTable table,String info,int alineacion, int borde, Font font){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor();
        table.addCell(cell);
    }*/
    
    public void renglonVacio(PdfPTable table, int celdas, Font font){
        PdfPCell cell = new PdfPCell(new Paragraph(" ",font));
        cell.setColspan(celdas);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }
    
    public void renglonVacio(PdfPTable table, int celdas, int borde, Font font){
        PdfPCell cell = new PdfPCell(new Paragraph(" ",font));
        cell.setColspan(celdas);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        table.addCell(cell);
    }
    
    public void renglonDatos(PdfPTable table, Font font, String... info){
        for(int i=0; i<info.length; i++){
            PdfPCell cell = new PdfPCell(new Paragraph(info[i],font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }
    
    public void renglonDatos(PdfPTable table, Font font, int alineacion, String... info){
        for(int i=0; i<info.length; i++){
            PdfPCell cell = new PdfPCell(new Paragraph(info[i],font));
            if(alineacion==1)
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            else if(alineacion==2)
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            else if(alineacion==3)
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            else if(alineacion==4)
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            else
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);;
            cell.setBorder(Rectangle.NO_BORDER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE); 
            table.addCell(cell);
        }
    }
    
    public void renglonDatos(PdfPTable table, Font font, int alineacion, int borde, String... info){
        for(int i=0; i<info.length; i++){
            PdfPCell cell = new PdfPCell(new Paragraph(info[i],font));
            if(alineacion==1)
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            else if(alineacion==2)
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            else if(alineacion==3)
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            else if(alineacion==4)
                cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
            else
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            if(borde==1)
                cell.setBorder(Rectangle.NO_BORDER);
            else if(borde==2)
                cell.setBorder(Rectangle.BOX);
            else if(borde==3)
                cell.setBorder(Rectangle.BOTTOM);
            else
                cell.setBorder(Rectangle.IMGTEMPLATE);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }
    public void renglonDatos(PdfPTable table, Font font, String info, int celdas, int alineacion, int borde){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        cell.setColspan(celdas);
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void renglonDatos(PdfPTable table, Font font, String info, int celdas, int alineacion, int borde, Color c, int padding_top, int padding_bottom){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        cell.setColspan(celdas);
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setPaddingTop(padding_top);
        cell.setPaddingBottom(padding_bottom);
        cell.setBorderColor(c);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void renglonDatos(PdfPTable table, Font font, String info, int celdas){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        cell.setColspan(celdas);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void foot(Document document, String info, Font font, int alineacion, int borde){
        HeaderFooter footer = new HeaderFooter(new Phrase(info, font), true);
        if(alineacion==1)
            footer.setAlignment(HeaderFooter.ALIGN_LEFT);
        else if(alineacion==2)
            footer.setAlignment(HeaderFooter.ALIGN_CENTER);
        else if(alineacion==3)
            footer.setAlignment(HeaderFooter.ALIGN_RIGHT);
        else if(alineacion==4)
            footer.setAlignment(HeaderFooter.ALIGN_JUSTIFIED);
        else
            footer.setAlignment(HeaderFooter.ALIGN_MIDDLE);
        if(borde==1)
            footer.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            footer.setBorder(Rectangle.BOX);
        else if(borde==3)
            footer.setBorder(Rectangle.BOTTOM);
        else if(borde==4)
            footer.setBorder(Rectangle.TOP);
        else
            footer.setBorder(Rectangle.IMGTEMPLATE);
        document.setFooter(footer);
    }
    
    public void header(Document document, String info, Font font, int alineacion, int borde){
        HeaderFooter footer = new HeaderFooter(new Phrase(info, font), false);
        if(alineacion==1)
            footer.setAlignment(HeaderFooter.ALIGN_LEFT);
        else if(alineacion==2)
            footer.setAlignment(HeaderFooter.ALIGN_CENTER);
        else if(alineacion==3)
            footer.setAlignment(HeaderFooter.ALIGN_RIGHT);
        else if(alineacion==4)
            footer.setAlignment(HeaderFooter.ALIGN_JUSTIFIED);
        else
            footer.setAlignment(HeaderFooter.ALIGN_MIDDLE);
        if(borde==1)
            footer.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            footer.setBorder(Rectangle.BOX);
        else if(borde==3)
            footer.setBorder(Rectangle.BOTTOM);
        else if(borde==4)
            footer.setBorder(Rectangle.TOP);
        else
            footer.setBorder(Rectangle.IMGTEMPLATE);
        document.setHeader(footer);
    }
    
    public PdfPTable columnasTabla(float width, Boolean locked, float[] widths){
        PdfPTable tabla = new PdfPTable(widths);
        tabla.setTotalWidth(width);
        tabla.setLockedWidth(locked);
        return tabla;
    }
    
    public PdfPTable columnasTabla(float width, Boolean locked, int alineacion, float[] widths){
        PdfPTable tabla = new PdfPTable(widths);
        tabla.setTotalWidth(width);
        tabla.setLockedWidth(locked);
        if(alineacion == 1)
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion == 2)
            tabla.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion == 3)
            tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return tabla;
    }
    
    public void celda(PdfPTable table,String info,int alineacion, int borde, Font font, int celdasHor, int celdasVer){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        cell.setColspan(celdasHor);
        cell.setRowspan(celdasVer);
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
    public void celda(PdfPTable table,String info,int alineacion, int borde, Font font, int celdasHor, int celdasVer, Color col){
        PdfPCell cell = new PdfPCell(new Paragraph(info,font));
        cell.setBackgroundColor(col);
        cell.setColspan(celdasHor);
        cell.setRowspan(celdasVer);
        if(alineacion==1)
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        else if(alineacion==2)
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        else if(alineacion==3)
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        else if(alineacion==4)
            cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        else
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        if(borde==1)
            cell.setBorder(Rectangle.NO_BORDER);
        else if(borde==2)
            cell.setBorder(Rectangle.BOX);
        else if(borde==3)
            cell.setBorder(Rectangle.BOTTOM);
        else
            cell.setBorder(Rectangle.IMGTEMPLATE);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell(cell);
    }
    
}