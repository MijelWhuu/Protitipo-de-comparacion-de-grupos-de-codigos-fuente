package guardar_archivos;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import abrir_archivos.SeleccionarCarpeta;
import analisis.Codigo;

public class GuardarCodigos {
	private File direccion;
	private String nombreCarpeta;
	public GuardarCodigos() {
		
	}
	
	public void seleccionaCarpeta() {
		this.direccion= SeleccionarCarpeta.seleccionarCarpeta();
	}
	
	public void setNombreCarpeta(String nombre) {
		this.nombreCarpeta=nombre;
	}
	
	public boolean guardarArchivos(ArrayList<Codigo> codigos) {
            boolean flag=true;
		String carpeta=this.direccion.getAbsolutePath();
		File nueva= new File(carpeta+((char)92)+this.nombreCarpeta);
		if(!nueva.mkdir()){
			javax.swing.JOptionPane.showMessageDialog(null,"No se pudo crear la carpeta", "Aviso", 0);
                        flag=false;
                }
		for(Codigo cod: codigos) {
			File f= new File(nueva.getAbsolutePath()+((char)92)+cod.getNombre());
			if(!f.exists()) {
				try {
					f.createNewFile();
					FileWriter fw = new FileWriter(f);
					BufferedWriter bw= new BufferedWriter(fw);
					bw.write(cod.getOriginal());
					bw.close();
					fw.close();
				} catch (IOException e) {
                                    flag=false;
					javax.swing.JOptionPane.showMessageDialog(null,"Error en la creación de archivos", "Aviso", 0);
					break;
				}
			}
		}
                return flag;
	}
}
