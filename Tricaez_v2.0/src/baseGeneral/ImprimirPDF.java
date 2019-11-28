package baseGeneral;

import baseGeneral.ErroresClase;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import org.jpedal.PdfDecoder;

public class ImprimirPDF {

    public ImprimirPDF() {}
    
    public Boolean imprimirPDF(String direccion){
        PdfDecoder pdf = null;
        Boolean bien = false;
        try {
            //PrintService[] service = PrinterJob.lookupPrintServices();
            PrinterJob printJob = PrinterJob.getPrinterJob();
            //printJob.setPrintService(service[1]);

            Paper paper = new Paper();
            //TAMAÑO CARTA
            paper.setSize(612, 792);
            paper.setImageableArea(0, 0, 612, 792);
            PageFormat pf = printJob.defaultPage();
            pf.setPaper(paper);		

            pdf = new PdfDecoder(true);
            pdf.openPdfFile(direccion);
            pdf.setPageFormat(pf);

            printJob.setPageable(pdf);
            printJob.print();
            bien = true;
        } catch (Exception e) {
            new ErroresClase(e,"Error al Imprimir PDF").getErrorJava();
            bien = false;
        } finally {
            pdf.closePdfFile();
        }
        return bien;
    }
}

/*
    //A4 (border)
    printDescription="A4";
    paper = new Paper();
    paper.setSize(595, 842);
    paper.setImageableArea(43, 43, 509, 756);
    paperDefinitions.put(printDescription,paper);
    paperList.add(printDescription);
    
    //A4 (borderless)
    printDescription="A4 (borderless)";
    paper = new Paper();
    paper.setSize(595, 842);
    paper.setImageableArea(0, 0, 595, 842);
    paperDefinitions.put(printDescription,paper);
    paperList.add(printDescription);
     
    //A5
    printDescription="A5";
    paper = new Paper();
    paper.setSize(420, 595);
    paper.setImageableArea(43,43,334,509);
    paperDefinitions.put(printDescription,paper);
    paperList.add(printDescription);

    //Added for Adobe
    printDescription="US Letter (8.5 x 11)";
    paper = new Paper();
    paper.setSize(612, 792);
    paper.setImageableArea(43,43,526,706);
    paperDefinitions.put(printDescription,paper);
        paperList.add(printDescription);
    
    //custom
    printDescription="Custom 2.9cm x 8.9cm";
    int customW=(int) (29*2.83);
    int customH=(int) (89*2.83); //2.83 is scaling factor to convert mm to pixels
    paper = new Paper();
    paper.setSize(customW, customH);
    paper.setImageableArea(0,0,customW,customH); //MUST BE SET ALSO
    paperDefinitions.put(printDescription,paper);
    paperList.add(printDescription);
    
    //architectural D (1728x2592)
    printDescription="Architectural D";
    paper = new Paper();
    paper.setSize(1728, 2592);
    paper.setImageableArea(25,25,1703,2567);
    paperDefinitions.put(printDescription,paper);
    paperList.add(printDescription);
 */