package herramientas;
import java.util.ArrayList;

import analisis.Codigo;

public class FormarNombres {

	public static boolean crearNombres(ArrayList<Codigo> codigos) {
		if(codigos.size()<2) {
			return false;
		}
		int c=0;
		String partes1[]= codigos.get(0).getNombre().replace((char)92, (char)42).split("\\*");
		String partes2[]= codigos.get(codigos.size()-1).getNombre().replace((char)92, (char)42).split("\\*");
		while(partes1[c].equals(partes2[c]))
			c++;
		for(int i=0;i<codigos.size();i++) {
			String partes[]= codigos.get(i).getNombre().replace((char)92, (char)42).split("\\*");
			codigos.get(i).setNombre(partes[c]+"_"+partes[partes.length-1]);
			
		}
		return true;
	}
}
