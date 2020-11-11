package abrir_archivos;

import java.io.File;
import java.util.ArrayList;
/*
 * 
 * Probablemente haya que crear una variable para indicar la carpeta original
 * 
 */

public class AbrirArchivos {
	private ArrayList<File> archivosJava;
	private ArrayList<File> archivosC;
	private ArrayList<File> archivosScilab;
	public AbrirArchivos() {
		this.archivosJava= new ArrayList<File>();
		this.archivosC= new ArrayList<File>();
		this.archivosScilab= new ArrayList<File>();
	}
	
	public void ubicarArchivos() {
		File carpetaP = SeleccionarCarpeta.seleccionarCarpeta();
		if(carpetaP!=null) {
			File[] direcciones=carpetaP.listFiles();			
			if(direcciones!=null)
				for(File f: direcciones) {
					addArchivo(f);
				}
		}
	}
	
	public ArrayList<File> getArchivosJava() {
		return this.archivosJava;
	}
	
	public ArrayList<File> getArchivosC() {
		return this.archivosC;
	}
	
	public ArrayList<File> getArchivosScilab() {
		return this.archivosScilab;
	}
	
	private void addArchivo(File f) {
		if(f.isDirectory()) {
			File archivosInt[]= f.listFiles();
			if(archivosInt!=null)
				for(File a: archivosInt)
					addArchivo(a);
		}else {
			if(f.getAbsolutePath().endsWith(".java")){
				this.archivosJava.add(f);
			}else if (f.getAbsolutePath().endsWith(".c")) {
				this.archivosC.add(f);
			}else if(f.getAbsolutePath().endsWith(".sce")) {
				this.archivosScilab.add(f);
			}
		}
	}
}
