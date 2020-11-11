
import analisis.Codigo;
import herramientas.ManejadorCodigoJava;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class Pruebas {
//		public static void main(String args[]) {
//			AbrirCodigos ac= new AbrirCodigos();
//			try {
//				ac.abrirArchivos();
//				new PantallaDivididaFrame(ac.getCodigos().get(0),ac.getCodigos().get(1)).setVisible(true);
//			} catch (IOException e) {
//                            System.out.println("dead");
//			}
//		}
    public static void main(String[] args) {
        JFileChooser chooser = new JFileChooser();//crea el selector de carpeta
        chooser.showOpenDialog(null);
        File f= chooser.getSelectedFile();
        FileReader fr;
        Codigo code;
        String texto="";
        try {
            fr = new FileReader(f); //lee cada archivo
            BufferedReader br= new BufferedReader(fr);
            String aux;
            while((aux=br.readLine())!=null) {
            	texto+=aux+"\n";//Escribe el contenido del archivo
            }
            br.close();
            fr.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
	code= new Codigo(f.getAbsolutePath(), texto);
        ManejadorCodigoJava c= new ManejadorCodigoJava(code.getOriginal());
        
        
        
    }
}
	