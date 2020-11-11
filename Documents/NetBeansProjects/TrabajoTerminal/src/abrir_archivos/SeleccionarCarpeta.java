package abrir_archivos;

import java.io.File;

import javax.swing.JFileChooser;

public class SeleccionarCarpeta {
	public static File seleccionarCarpeta() {
		JFileChooser chooser = new JFileChooser();//crea el selector de carpeta
		chooser.setDialogTitle("Seleccione carpeta con codigos");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//Configura para aceptar
		chooser.setAcceptAllFileFilterUsed(false);//                  unicamente carpetas
		if( chooser.showOpenDialog(null)== JFileChooser.APPROVE_OPTION)
			return chooser.getSelectedFile();
		else 
			return null;
	}
}
