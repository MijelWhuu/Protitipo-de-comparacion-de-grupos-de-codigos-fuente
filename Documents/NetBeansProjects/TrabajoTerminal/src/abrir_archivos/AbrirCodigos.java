package abrir_archivos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;


import analisis.Codigo;
import herramientas.FormarNombres;

public class AbrirCodigos {
	private ArrayList<Codigo> codigos;
	private AbrirArchivos abArchivos;
	private String lenguaje;
	public AbrirCodigos() {
		this.abArchivos= new AbrirArchivos();
		this.codigos=new ArrayList<>();
	}
	
	public String getLenguaje() {
		return lenguaje;
	}

	public void setLenguaje(String lenguaje) {
		this.lenguaje = lenguaje;
	}

	public void abrirArchivos() throws IOException {
                this.codigos.clear();
		this.abArchivos.ubicarArchivos();
		if(this.abArchivos.getArchivosJava().size()<2) {//No encontr� codigos java
			if(this.abArchivos.getArchivosC().size()<2) {//No encontro codigos C
				if(this.abArchivos.getArchivosScilab().size()<2){//No encontro codigos Scilab
					javax.swing.JOptionPane.showMessageDialog(null,"No se encontró ningún lenguaje de programación", "Aviso", 0);
				}else {//Se encontraron codigos de scilab
					//solo codigos scilab
					this.lenguaje="scilab";
					for(File f:this.abArchivos.getArchivosScilab()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";//Escribe el contenido del archivo
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosScilab().clear();
				}
			}else {//Se encontraron codigos de c
				if(this.abArchivos.getArchivosScilab().size()<2){
					//Solo c�digos de c
					this.lenguaje="c";
					for(File f:this.abArchivos.getArchivosC()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosC().clear();
				}else {//Se encontraron codigos de scilab
					int seleccion=javax.swing.JOptionPane.showOptionDialog(null, "Eliga el lenguaje", "Lenguajes detectados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"C","Scilab"}, "C");
					//0 para c y 1 para scilab
					switch (seleccion) {
					case 0://C
						this.lenguaje="c";
						for(File f:this.abArchivos.getArchivosC()) {//recorre cada archivo
							FileReader fr= new FileReader(f);//lee cada archivo
							BufferedReader br= new BufferedReader(fr);
							String texto="",aux;
							
							while((aux=br.readLine())!=null) {
								texto+=aux+"\n";
							}
							br.close();
							fr.close();
							Codigo code= new Codigo(f.getAbsolutePath(), texto);
							this.codigos.add(code);
						}
						this.abArchivos.getArchivosC().clear();
						this.abArchivos.getArchivosScilab().clear();
						break;
					case 1://Scilab
						this.lenguaje="scilab";
						for(File f:this.abArchivos.getArchivosScilab()) {//recorre cada archivo
							FileReader fr= new FileReader(f);//lee cada archivo
							BufferedReader br= new BufferedReader(fr);
							String texto="",aux;
							
							while((aux=br.readLine())!=null) {
								texto+=aux+"\n";
							}
							br.close();
							fr.close();
							Codigo code= new Codigo(f.getAbsolutePath(), texto);
							this.codigos.add(code);
						}
						this.abArchivos.getArchivosC().clear();
						this.abArchivos.getArchivosScilab().clear();
						break;
					default:
						break;
					}
				}
			}
		}else if(this.abArchivos.getArchivosC().size()<2) {//Se encontraron c�digos Java y ninguno de C
			if(this.abArchivos.getArchivosScilab().size()<2){//No encontro codigos Scilab
				//Solo java
				this.lenguaje="java";
				for(File f:this.abArchivos.getArchivosJava()) {//recorre cada archivo
					FileReader fr= new FileReader(f);//lee cada archivo
					BufferedReader br= new BufferedReader(fr);
					String texto="",aux;
					
					while((aux=br.readLine())!=null) {
						texto+=aux+"\n";
					}
					br.close();
					fr.close();
					Codigo code= new Codigo(f.getAbsolutePath(), texto);
					this.codigos.add(code);
				}
				this.abArchivos.getArchivosJava().clear();
			}else {//Se encontraron tambien de Scilab
				int seleccion =javax.swing.JOptionPane.showOptionDialog(null, "Eliga el lenguaje", "Lenguajes detectados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Java","Scilab"}, "Java");
				//0 para java, 1 para scilab
				switch (seleccion) {
				case 0://java
					this.lenguaje="java";
					for(File f:this.abArchivos.getArchivosJava()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosScilab().clear();
					break;
				case 1://Scilab
					this.lenguaje="scilab";
					for(File f:this.abArchivos.getArchivosScilab()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosScilab().clear();
					break;
				default:
					break;
				}
			}
		}else {//Se encontraron tambien de C
			if(this.abArchivos.getArchivosScilab().size()<2){//No encontro codigos Scilab
				int seleccion =javax.swing.JOptionPane.showOptionDialog(null, "Eliga el lenguaje", "Lenguajes detectados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Java","C"}, "Java");
				//0 para java, 1 para c
				switch (seleccion) {
				case 0://java
					this.lenguaje="java";
					for(File f:this.abArchivos.getArchivosJava()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosC().clear();
					break;
				case 1://C
					this.lenguaje="c";
					for(File f:this.abArchivos.getArchivosC()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosC().clear();
					break;
				default:
					break;
				}
			}else {//Se encontraron tambien de Scilab
				int seleccion =javax.swing.JOptionPane.showOptionDialog(null, "Eliga el lenguaje", "Lenguajes detectados", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Java","C","Scilab"}, "Java");
				//0 para java, 1 para c, 2 para scilab
				switch (seleccion) {
				case 0://java
					this.lenguaje="java";
					for(File f:this.abArchivos.getArchivosJava()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosC().clear();
					this.abArchivos.getArchivosScilab().clear();
					break;
				case 1://C
					this.lenguaje="c";
					for(File f:this.abArchivos.getArchivosC()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosC().clear();
					this.abArchivos.getArchivosScilab().clear();
					break;
				case 2://Scilab
					this.lenguaje="scilab";
					for(File f:this.abArchivos.getArchivosScilab()) {//recorre cada archivo
						FileReader fr= new FileReader(f);//lee cada archivo
						BufferedReader br= new BufferedReader(fr);
						String texto="",aux;
						
						while((aux=br.readLine())!=null) {
							texto+=aux+"\n";
						}
						br.close();
						fr.close();
						Codigo code= new Codigo(f.getAbsolutePath(), texto);
						this.codigos.add(code);
					}
					this.abArchivos.getArchivosJava().clear();
					this.abArchivos.getArchivosC().clear();
					this.abArchivos.getArchivosScilab().clear();
				default:
					break;
				}
			}
		}
		if(!FormarNombres.crearNombres(this.codigos))
			javax.swing.JOptionPane.showMessageDialog(null,"Error en la apertura de archivos", "Aviso", 0);
	}

	public ArrayList<Codigo> getCodigos() {
		return codigos;
	}

	public void setCodigos(ArrayList<Codigo> codigos) {
		this.codigos = codigos;
	}
}